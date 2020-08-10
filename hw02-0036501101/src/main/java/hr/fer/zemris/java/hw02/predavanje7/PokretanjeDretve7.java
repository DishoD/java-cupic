package hr.fer.zemris.java.hw02.predavanje7;

public class PokretanjeDretve7 {
	private static volatile int brojac = 0;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Metodu main izvodi dretva: " + Thread.currentThread().getName());
		System.out.println("Brojac: " + brojac);

		Thread[] radnici = new Thread[4];
		for (int i = 0; i < radnici.length; ++i) {
			radnici[i] = new Thread(new PosaoDretve(5_000_000), "radnik" + i);
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

		public PosaoDretve(int brojacUvecanja) {
			this.brojUvecanja = brojacUvecanja;
		}

		@Override
		public void run() {
			System.out.println("Metodu uvecavanja izvodi dretva: " + Thread.currentThread().getName());
			synchronized (this) {
				for (int i = 0; i < brojUvecanja; ++i) {
					brojac++;
				}
			}

		}

	}
}
