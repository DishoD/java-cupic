package hr.fer.zemris.java.hw02.predavanje7;

public class PokretanjeDretve6 {
	private static volatile int brojac = 0;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Metodu main izvodi dretva: " + Thread.currentThread().getName());
		System.out.println("Brojac: " + brojac);
		
		PosaoDretve posao = new PosaoDretve(5_000_000);
		
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

		public PosaoDretve(int brojacUvecanja) {
			this.brojUvecanja = brojacUvecanja;
		}

		@Override
		public void run() {
			System.out.println("Metodu uvecavanja izvodi dretva: " + Thread.currentThread().getName());
		
				for (int i = 0; i < brojUvecanja; ++i) {
					brojac++;
				}
			

		}

	}
}
