package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A simple demonstration program of a ray tracer.
 * 
 * @author Disho
 *
 */
public class RayCasterParallel {
	/**
	 * Main method. Starts the program.
	 * 
	 * @param args ignorable
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}
	
	/**
	 * Represents a recursive action for the fork join pool. Fills the rgb array
	 * with appropriate colors for the ray casting of the given scene, view and eye.
	 * 
	 * @author Disho
	 *
	 */
	@SuppressWarnings("serial")
	private static class Job extends RecursiveAction {
		/**
		 * y axis of the view
		 */
		private Point3D yAxis;
		/**
		 * x axis of the view
		 */
		private Point3D xAxis;
		/**
		 * screen corner of the view
		 */
		private Point3D screenCorner;
		/**
		 * width of the screen
		 */
		private int width;
		/**
		 * height of the screen
		 */
		private int height;
		/**
		 * minimum row to paint (inclusive)
		 */
		private int yMin;
		/**
		 * maximum row to paint (inclusive)
		 */
		private int yMax;
		/**
		 * horizontal length of the view
		 */
		private double horizontal;
		/**
		 * vertical length of the view
		 */
		private double vertical;
		/**
		 * scene of the ray casting
		 */
		private Scene scene;
		/**
		 * eye position
		 */
		private Point3D eye;
		/**
		 * array for red color in rgb
		 */
		short[] red;
		/**
		 * array for green color in rgb
		 */
		short[] green;
		/**
		 * array for blue color in rgb
		 */
		short[] blue;
		
		/**
		 * Initializes the object with the given parameters.
		 * 
		 * @param yAxis y axis of the view
		 * @param xAxis x axis of the view
		 * @param screenCorner screen corner of the view
		 * @param width width of the screen
		 * @param height height of the screen
		 * @param yMin minimum row to paint (inclusive)
		 * @param yMax maximum row to paint (inclusive)
		 * @param horizontal horizontal length of the view
		 * @param vertical vertical length of the view
		 * @param scene scene of the ray casting
		 * @param eye eye position
		 * @param red array for red color in rgb
		 * @param green array for green color in rgb
		 * @param blue array for blue color in rgb
		 */
		public Job(Point3D yAxis, Point3D xAxis, Point3D screenCorner, int width, int height, int yMin, int yMax,
				double horizontal, double vertical, Scene scene, Point3D eye, short[] red, short[] green,
				short[] blue) {
			super();
			this.yAxis = yAxis;
			this.xAxis = xAxis;
			this.screenCorner = screenCorner;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.scene = scene;
			this.eye = eye;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Override
		protected void compute() {
			if(yMin - yMax +1 <= 20) {
				computeDirect();
				return;
			}
			
			invokeAll
			(
					new Job(yAxis, xAxis, screenCorner, width, height, yMin, yMax/2, horizontal, vertical, scene, eye, red, green, blue), 
					new Job(yAxis, xAxis, screenCorner, width, height, yMax/2 + 1, yMax, horizontal, vertical, scene, eye, red, green, blue)
			);
		}
		
		/**
		 * When the y range is small enough recursion will calculate the colors.
		 */
		private void computeDirect() {
			short[] rgb = new short[3];
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply(((double)x)/(width-1)*horizontal))
													  .sub(yAxis.scalarMultiply(((double)y)/(height-1)*vertical));
					
					Ray ray = Ray.fromPoints(eye, screenPoint);
					
					tracer(scene, ray, rgb);
					
					if(x == 166 && y == 166) {
						System.out.println("x=" + x +", y=" + y);
						System.out.println("screen-point: " + point3DtoString(screenPoint));
						System.out.println("direction=" + point3DtoString(ray.direction));
						System.out.println(String.format("RGB = [%d,%d,%d]", rgb[0], rgb[1], rgb[2]));
					}
					
					red[y*width + x] = rgb[0] > 255 ? 255 : rgb[0];
					green[y*width + x] = rgb[1] > 255 ? 255 : rgb[1];
					blue[y*width + x] = rgb[2] > 255 ? 255 : rgb[2];
				}
			}
		}
		
	}
	
	/**
	 * Creates an IRayTracerProducer.
	 * 
	 * @return  IRayTracerProducer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];
				
				Point3D zAxis = view.sub(eye).normalize();
				Point3D yAxis = viewUp.normalize().sub(zAxis.scalarMultiply(zAxis.scalarProduct(viewUp.normalize()))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal/2)).add(yAxis.scalarMultiply(vertical/2));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new Job(yAxis, xAxis, screenCorner, width, height, 0, height - 1, horizontal, vertical, scene, eye, red, green, blue));
				pool.shutdown();
				
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

			
		};
	}
	
	/**
	 * Traces the ray in the scene and calculates the appropriate pixel color
	 * of the ray intersection in the scene.
	 * 
	 * @param scene scene in which the ray is cast
	 * @param ray casted ray
	 * @param rgb array with 3 elements [red, green, blue]. Resulted color is stored here.
	 */
	private static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}
		
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		
		double rDifuse = 0;
		double gDifuse = 0;
		double bDifuse = 0;
		double rReflective = 0;
		double gReflective = 0;
		double bReflective = 0;
		
		for(LightSource ls : scene.getLights()) {
			Ray lightRay = Ray.fromPoints(ls.getPoint(), closest.getPoint());
			RayIntersection closestLight = findClosestIntersection(scene, lightRay);
			if(!arePointsSame(closest.getPoint(), closestLight.getPoint(), 1e-3)) continue;
			
			Point3D lVec = lightRay.direction.negate();
			Point3D normal = closestLight.getNormal();
			Point3D vVec = ray.direction.negate();
			Point3D rVec = lVec.add(normal.scalarMultiply(normal.scalarProduct(lVec)).sub(lVec).scalarMultiply(2));
			
			double cosDifuse = Math.max(lVec.scalarProduct(normal), 0);
			double cosReflective = Math.max(rVec.scalarProduct(vVec), 0);
			cosReflective = Math.pow(cosReflective, closest.getKrn());
			
			rDifuse += ls.getR()*closest.getKdr()*cosDifuse;
			gDifuse += ls.getG()*closest.getKdg()*cosDifuse;
			bDifuse += ls.getB()*closest.getKdb()*cosDifuse;
			rReflective += ls.getR()*closest.getKrr()*cosReflective;
			gReflective += ls.getG()*closest.getKrg()*cosReflective;
			bReflective += ls.getB()*closest.getKrb()*cosReflective;
		}
		
		rgb[0] += rDifuse + rReflective;
		rgb[1] += gDifuse + gReflective;
		rgb[2] += bDifuse + bReflective;
		
	}

	/**
	 * Checks whether given points are the same.
	 * 
	 * @param p1 first point
	 * @param p2 second point
	 * @param threshold comparison threshold
	 * @return
	 */
	private static boolean arePointsSame(Point3D p1, Point3D p2, double threshold) {
		return Math.abs(p1.sub(p2).norm()) < threshold;
	}

	/**
	 * Returns the closes intersection between an graphical object and the ray in
	 * the scene or null if it doesn't exist.
	 * 
	 * @param scene scene in which the ray is cast
	 * @param ray casted ray
	 * @return closes intersection if it exist, null otherwise
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		RayIntersection r = null;
		for(GraphicalObject go : scene.getObjects()) {
			RayIntersection intr = go.findClosestRayIntersection(ray);
			if(intr == null) continue;
			r = r==null ? intr : (intr.getDistance()<r.getDistance() ? intr : r);
		}
		
		return r;
	}
	
	/**
	 * Returns a string representation of a 3d point.
	 * 
	 * @param point 3d point
	 * @return string representation of the point
	 */
	private static String point3DtoString(Point3D point) {
		return String.format("(%f,%f,%f)", point.x, point.y, point.z);
	}

}
