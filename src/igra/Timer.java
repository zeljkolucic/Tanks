package igra;

import java.awt.Label;

public class Timer implements Runnable {

	private Thread nit;
	private Label vreme;
	private boolean radi;
	private int m, s;
	
	public Timer(Label vreme) {
		this.vreme = vreme;
	}
	
	public void stvoriNit() {
		nit = new Thread(this);
	}
	
	public synchronized void pokreni() {
		nit.start();
		radi = true;
	}
	
	public void zaustavi() {
		nit.interrupt();
		m = 0; 
		s = 0;
	}
	
	public synchronized void nastavi() {
		radi = true;
		notify();
	}
	
	public synchronized void privremenoZaustavi() {
		radi = false;
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				synchronized (this) {
					while(!radi)
						wait();
				}
				vreme.setText(toString());
				vreme.revalidate();
				Thread.sleep(1000);
				s++;
				if(s == 60) {
					m++;
					s = 0;
				}			
			} catch (InterruptedException e) {
				nit.interrupt();
			}
		}
	}
	
	public synchronized String toString() {
		return String.format("%02d:%02d", m, s);
	}
	
}
