package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Simple demonstration program that draws Newton-Raphson iteration-based
 * fractal. Program takes the list of complex roots and draws a fractal.
 * 
 * @author Disho
 *
 */
public class Newton {
	/**
	 * Polynomial with roots that user entered
	 */
	private static ComplexRootedPolynomial polynomial;

	/**
	 * Convergence threshold for iteration
	 */
	private static final double CONVERGENCE_THRESHOLD = 1E-3;
	/**
	 * root distance threshold
	 */
	private static final double ROOT_THRESHOLD = 1E-3;
	/**
	 * maximum number of iterations
	 */
	private static int MAX_ITER = 128;

	/**
	 * Fills the given array with indexes for lines from yMin to yMax.
	 * 
	 * @author Disho
	 *
	 */
	private static class Job implements Callable<Void> {
		int yMin;
		int yMax;
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		volatile short[] data;

		/**
		 * Initializes the object with the given parameters
		 * 
		 * @param yMin
		 *            first line (inclusive)
		 * @param yMax
		 *            last line (inclusive)
		 * @param reMin
		 *            minimum of real part, used for picking a complex number for the
		 *            pixels
		 * @param reMax
		 *            maximum of real part, used for picking a complex number for the
		 *            pixels
		 * @param imMin
		 *            minimum of imaginary part, used for picking a complex number for
		 *            the pixels
		 * @param imMax
		 *            maximum of imaginary part, used for picking a complex number for
		 *            the pixels
		 * @param width
		 *            width of the screen
		 * @param height
		 *            height of the screen
		 * @param data
		 *            array of indexes
		 */
		public Job(int yMin, int yMax, double reMin, double reMax, double imMin, double imMax, int width, int height,
				short[] data) {
			this.yMin = yMin;
			this.yMax = yMax;
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.data = data;
		}

		@Override
		public Void call() throws Exception {
			for (int y = yMin; y <= yMax; ++y) {
				for (int x = 0; x < height; ++x) {
					Complex c = mapToComplexPlain(x, y, width, height, reMin, reMax, imMin, imMax);
					Complex zn = c;
					int iter = 0;
					Complex zn1;
					double module;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = polynomial.toComplexPolynom().derive().apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						iter++;
					} while (module > CONVERGENCE_THRESHOLD && iter < MAX_ITER);
					int index = polynomial.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);
					if (index == -1) {
						data[y * width + x] = 0;
					} else {
						data[y * width + x] = (short) index;
					}
				}
			}
			return null;
		}

	}

	/**
	 * Main method. Controls the flow of the program. Asks user input and starts
	 * FractalViewer.
	 * 
	 * @param args
	 *            ignorable
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");

		Scanner sc = new Scanner(System.in);
		List<Complex> roots = new ArrayList<>();

		for (int i = 1; true; ++i) {
			System.out.print("Root " + i + " > ");
			String line = sc.nextLine().trim();
			if (line.toLowerCase().equals("done")) {
				if (i > 2)
					break;
				System.out.println("You must enter at least two roots.");
				i--;
				continue;
			}

			try {
				roots.add(Complex.parse(line));
			} catch (IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Enter 'done' if done or continue entering roots of the polynomial.");
				i--;
			}
		}
		
		sc.close();

		Complex[] rootsArray = roots.toArray(new Complex[1]);
		polynomial = new ComplexRootedPolynomial(rootsArray);

		FractalViewer.show(new MyProducer());

	}

	/**
	 * Fractal producer that produces the Newton-Raphson iteration-based fractal.
	 * 
	 * @author Disho
	 *
	 */
	private static class MyProducer implements IFractalProducer {
		/**
		 * Number of available processors
		 */
		private static final int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
		/**
		 * number of jobs to create for the thread pool
		 */
		private static final int NUMBER_OF_JOBS = 8 * NUMBER_OF_PROCESSORS;
		/**
		 * thread pool job information
		 */
		private List<Future<Void>> results = new ArrayList<>();
		/**
		 * thread pool
		 */
		private ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_PROCESSORS, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable job) {
				Thread thread = new Thread(job);
				thread.setDaemon(true);
				return thread;
			}
		});

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {

			short[] data = new short[width * height];

			int yRange = height / NUMBER_OF_JOBS;
			for (int i = 0; i < NUMBER_OF_JOBS; ++i) {
				int yMin = i * yRange;
				int yMax = (i + 1) * yRange - 1;
				if (i == NUMBER_OF_JOBS - 1) {
					yMax = height - 1;
				}

				results.add(pool.submit(new Job(yMin, yMax, reMin, reMax, imMin, imMax, width, height, data)));
			}

			for (Future<Void> result : results) {
				try {
					result.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}

			observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
		}

	}

	/**
	 * Maps (x,y) coordinates of the screen to the complex number.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param width
	 *            width of the screen
	 * @param height
	 *            height of the screen
	 * @param reMin
	 *            minimum of real part
	 * @param reMax
	 *            maximum of real part
	 * @param imMin
	 *            minimum of imaginary part
	 * @param imMax
	 *            maximum of imaginary part
	 * @return complex number
	 */
	private static Complex mapToComplexPlain(int x, int y, int width, int height, double reMin, double reMax,
			double imMin, double imMax) {
		double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
		double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

		return new Complex(cre, cim);
	}
}
