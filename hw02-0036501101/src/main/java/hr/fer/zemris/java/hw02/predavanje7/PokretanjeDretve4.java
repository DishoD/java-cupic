package hr.fer.zemris.java.hw02.predavanje7;

public class PokretanjeDretve4 {
	private static volatile int brojac = 0;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Metodu main izvodi dretva: " + Thread.currentThread().getName());
		System.out.println("Brojac: " + brojac);

		Object mutex = new Object();

		PosaoDretve posao = new PosaoDretve(mutex, 5_000_000);

		Thread[] radnici = new Thread[4];
		for (int i = 0; i < radnici.length; ++i) {
			radnici[i] = new Thread(posao, "radnik" + i);
		}

		for (Thread radnik : radnici) {
			radnik.start();
		}

		for (Thread radnik : radnici) {
			radnik.join();
		}

		System.out.println("Brojac: " + brojac);
	}

	public static class PosaoDretve implements Runnable {
		private int brojUvecanja;
		private Object mutex;

		public PosaoDretve(Object mutex, int brojacUvecanja) {
			this.mutex = mutex;
			this.brojUvecanja = brojacUvecanja;
		}

		@Override
		public void run() {
			System.out.println("Metodu uvecavanja izvodi dretva: " + Thread.currentThread().getName());
			synchronized (mutex) {
				for (int i = 0; i < brojUvecanja; ++i) {
					brojac++;
				}
			}

		}

	}
}
