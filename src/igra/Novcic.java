package igra;

import java.awt.Color;
import java.awt.Graphics;

public class Novcic extends Figura {

	public Novcic(Polje polje) {
		super(polje);
	}

	@Override
	public void iscrtaj() {
		Graphics g = polje.getGraphics();
		int sirina = polje.getWidth();
		int visina = polje.getHeight();
		g.setColor(Color.YELLOW);
		g.fillOval(sirina / 4, visina / 4, sirina / 2, visina / 2);
	}

}
