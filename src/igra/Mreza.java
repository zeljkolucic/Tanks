package igra;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import igra.Igra.Rezim;

public class Mreza extends Panel implements Runnable {

	private Thread nit;
	private Igra igra;
	private Polje[][] polja;
	private boolean[][] nalaziSe;
	private Igrac igrac;
	private ArrayList<Novcic> listaNovcica = new ArrayList<Novcic>();
	private ArrayList<Tenk> listaTenkova = new ArrayList<Tenk>();

	public Mreza(int dim, Igra igra) {
		polja = new Polje[dim][dim];
		nalaziSe = new boolean[dim][dim];
		setLayout(new GridLayout(dim, dim));
		double verovatnoca = 0.8;
		MisAkcija osluskivac = new MisAkcija();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (Math.random() < verovatnoca)
					polja[i][j] = new Trava(this);
				else
					polja[i][j] = new Zid(this);
				add(polja[i][j]);
				polja[i][j].addMouseListener(osluskivac);
				polja[i][j].iscrtaj();
			}
		}
		this.igra = igra;
	}

	public Mreza(Igra igra) {
		this(17, igra);
	}

	public Igrac dohvIgraca() {
		return igrac;
	}

	public Polje[][] dohvMatricu() {
		return polja;
	}

	public void inicijalizuj(int brojNovcica) {
		if (igra.dohvRezim() == Rezim.IGRANJE) {

			for (int i = 0; i < polja.length; i++)
				for (int j = 0; j < polja.length; j++)
					nalaziSe[i][j] = false;

			for (int i = 0; i < brojNovcica; i++) {
				boolean postavljen = false;
				while (!postavljen) {
					int m = (int) (Math.random() * polja.length);
					int n = (int) (Math.random() * polja[0].length);
					if (polja[m][n].dozvoljenaFigura()) {
						listaNovcica.add(new Novcic(polja[m][n]));
						nalaziSe[m][n] = true;
						postavljen = true;
					}
				}
			}
			int brojTenkova = brojNovcica / 3;
			for (int i = 0; i < brojTenkova; i++) {
				boolean postavljen = false;
				while (!postavljen) {
					int m = (int) (Math.random() * polja.length);
					int n = (int) (Math.random() * polja[0].length);
					if (polja[m][n].dozvoljenaFigura()) {
						listaTenkova.add(new Tenk(polja[m][n]));
						nalaziSe[m][n] = true;
						postavljen = true;
					}
				}
			}
			boolean postavljen = false;
			while (!postavljen) {
				int m = (int) (Math.random() * polja.length);
				int n = (int) (Math.random() * polja[0].length);
				if (polja[m][n].dozvoljenaFigura() && !nalaziSe[m][n]) {
					igrac = new Igrac(polja[m][n]);
					postavljen = true;
				}
			}
		}
	}

	private class MisAkcija extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (igra.dohvRezim() == Rezim.IZMENA && igra.dohvGrupu().getSelectedCheckbox() != null) {
				Polje polje = (Polje) e.getSource();
				Mreza mreza = polje.dohvMrezu();
				if (igra.dohvGrupu().getSelectedCheckbox().getLabel().equals("Trava") && polje instanceof Zid) {
					int[] pozicija = polje.pozicija();
					int index = getComponentZOrder(polja[pozicija[0]][pozicija[1]]);
					remove(polja[pozicija[0]][pozicija[1]]);
					polja[pozicija[0]][pozicija[1]] = new Trava(mreza);
					add(polja[pozicija[0]][pozicija[1]], index);
					polja[pozicija[0]][pozicija[1]].addMouseListener(new MisAkcija());
					polja[pozicija[0]][pozicija[1]].revalidate();
					polja[pozicija[0]][pozicija[1]].iscrtaj();
				} else if (igra.dohvGrupu().getSelectedCheckbox().getLabel().equals("Zid") && polje instanceof Trava) {
					int[] pozicija = polje.pozicija();
					int index = getComponentZOrder(polja[pozicija[0]][pozicija[1]]);
					remove(polja[pozicija[0]][pozicija[1]]);
					polja[pozicija[0]][pozicija[1]] = new Zid(mreza);
					add(polja[pozicija[0]][pozicija[1]], index);
					polja[pozicija[0]][pozicija[1]].addMouseListener(new MisAkcija());
					polja[pozicija[0]][pozicija[1]].revalidate();
					polja[pozicija[0]][pozicija[1]].iscrtaj();
				}
			} else if (igra.dohvRezim() == Rezim.IGRANJE) {
				requestFocusInWindow();
			}
		}

	}

	public synchronized void pokreni(int brojNovcica) {
		nit = new Thread(this);
		inicijalizuj(brojNovcica);
		nit.start();
		for (int i = 0; i < listaTenkova.size(); i++)
			listaTenkova.get(i).pokreni();
		igra.setFocusable(true);
		igra.requestFocus();
	}

	public void zaustavi() {
		for (int i = 0; i < listaTenkova.size(); i++)
			listaTenkova.get(i).zaustavi();
		nit.interrupt();
		igra.postTok(false);
		igrac = null;
		listaNovcica.clear();
		listaTenkova.clear();
		for (int i = 0; i < polja.length; i++)
			for (int j = 0; j < polja.length; j++)
				polja[i][j].repaint();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(40);
				azuriraj();
			} catch (InterruptedException e) {
				nit.interrupt();
			}
		}
	}

	public void azuriraj() {
		for (int i = 0; i < listaNovcica.size(); i++) {
			if (igrac.equals(listaNovcica.get(i))) {
				Polje staroPolje = listaNovcica.get(i).dohvPolje();
				listaNovcica.remove(i);
				staroPolje.repaint();
				igra.uvecajPoene();
				if (listaNovcica.size() == 0) 
					zaustavi();
				break;
			}
		}

		for (int i = 0; i < listaNovcica.size(); i++)
			listaNovcica.get(i).iscrtaj();

		for (int i = 0; i < listaTenkova.size(); i++)
			listaTenkova.get(i).iscrtaj();

		if (igrac != null)
			igrac.iscrtaj();

		for (int i = 0; i < listaTenkova.size(); i++) {
			if (igrac != null && igrac.equals(listaTenkova.get(i))) {
				zaustavi();
				break;
			}
		}
	}
}
