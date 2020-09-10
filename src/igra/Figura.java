package igra;

public abstract class Figura {

	protected Polje polje;

	public Figura(Polje polje) {
		super();
		this.polje = polje;
	}
	
	public Polje dohvPolje() {
		return polje;
	}
	
	public void pomeri(Polje polje) {
		Polje staroPolje = this.polje;
		this.polje = polje;
		staroPolje.repaint();
	}

	@Override
	public boolean equals(Object obj) {
		return polje == ((Figura)obj).polje;
	}
	
	public abstract void iscrtaj();
		
}
