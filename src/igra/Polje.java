package igra;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Polje extends Canvas {

	private Mreza mreza;
	Color boja;

	public Polje(Mreza mreza) {
		super();
		this.mreza = mreza;
	}

	public Mreza dohvMrezu() {
		return mreza;
	}

	public int[] pozicija() {
		Polje[][] matrica = mreza.dohvMatricu();
		for (int i = 0; i < matrica.length; i++) {
			for (int j = 0; j < matrica[0].length; j++) {
				if (matrica[i][j] == this) {
					int[] pozicija = { i, j };
					return pozicija;
				}
			}
		}
		return null;
	}

	public Polje dohvPolje(int vrsta, int kolona) {
		Polje[][] matrica = mreza.dohvMatricu();
		for (int i = 0; i < matrica.length; i++) {
			for (int j = 0; j < matrica[0].length; j++) {
				if (matrica[i][j] == this) {
					if (i + kolona < matrica.length && i + kolona >= 0 && j + vrsta < matrica[0].length
							&& j + vrsta >= 0) {
						return matrica[i + kolona][j + vrsta];
					}
					return null;
				}
			}
		}
		return null;
	}

	public abstract boolean dozvoljenaFigura();
	
	public void iscrtaj() {
		setBackground(boja);
	}

}
