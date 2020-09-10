package igra;

import java.awt.Graphics;

public class Tenk extends Figura implements Runnable {

	private Thread nit;

	public Tenk(Polje polje) {
		super(polje);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(500);
				boolean pomeren = false;
				Polje p = null;
				while (!pomeren) {
					int smer = (int) (Math.random() * 4);
					switch (smer) {
					case 0:
						p = polje.dohvPolje(-1, 0);
						break;
					case 1:
						p = polje.dohvPolje(1, 0);
						break;
					case 2:
						p = polje.dohvPolje(0, -1);
						break;
					case 3:
						p = polje.dohvPolje(0, 1);						
						break;
					}
					if (p != null && p.dozvoljenaFigura()) {
						pomeri(p);
						pomeren = true;
					}
				}
			} catch (InterruptedException e) {
				nit.interrupt();
			}
		}
	}

	public synchronized void pokreni() {
		nit = new Thread(this);
		nit.start();
	}

	public void zaustavi() {
		nit.interrupt();
	}

	@Override
	public void iscrtaj() {
		Graphics g = polje.getGraphics();
		int sirina = polje.getWidth();
		int visina = polje.getHeight();
		g.drawLine(0, 0, sirina, visina);
		g.drawLine(sirina, 0, 0, visina);
	}

}
