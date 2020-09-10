package igra;

import java.awt.Color;
import java.awt.Graphics;

public class Trava extends Polje {

	public Trava(Mreza mreza) {
		super(mreza);
		boja = Color.GREEN;
	}

	@Override
	public boolean dozvoljenaFigura() {
		return true;
	}

}
