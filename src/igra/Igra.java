package igra;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Igra extends Frame {

	public enum Rezim {
		IZMENA, IGRANJE
	}

	private Mreza mreza;
	private Rezim rezim;
	private boolean uToku;
	private TextField novcica;
	private Button pocni = new Button("Pocni");
	private int brojPoena;
	private Label poeni = new Label("Poena: 0");
	CheckboxGroup grupa = new CheckboxGroup();
	private Timer timer;
	private Label vreme;

	public Igra() {
		super("Igra");
		setSize(600, 600);
		setResizable(false);

		mreza = new Mreza(this);
		vreme = new Label("00:00");
		timer = new Timer(vreme);
		popuniProzor();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (uToku)
					mreza.zaustavi();
				dispose();
			}
		});
		addKeyListener(new TastAkcija());
		setVisible(true);
	}

	public Rezim dohvRezim() {
		return rezim;
	}

	public CheckboxGroup dohvGrupu() {
		return grupa;
	}

	public void uvecajPoene() {
		brojPoena++;
		poeni.setText("Poena: " + brojPoena);
	}

	public void postTok(boolean b) {
		uToku = b;
		brojPoena = 0;
		poeni.setText("Poena: " + brojPoena);
		timer.zaustavi();
	}

	private void popuniProzor() {
		meni();
		setLayout(new BorderLayout());
		add(mreza, BorderLayout.CENTER);
		mreza.setFocusable(true);
		mreza.requestFocusInWindow();
		add(juzniPanel(), BorderLayout.SOUTH);
		add(istocniPanel(), BorderLayout.EAST);
	}

	private void meni() {
		MenuBar trakaMenija = new MenuBar();
		Menu rezim = new Menu("Rezim");
		MeniAkcija osluskivac = new MeniAkcija();
		MenuItem rezimIzmena = new MenuItem("Rezim izmena");
		rezimIzmena.addActionListener(osluskivac);
		MenuItem rezimIgranja = new MenuItem("Rezim igranja");
		rezimIgranja.addActionListener(osluskivac);
		rezim.add(rezimIzmena);
		rezim.add(rezimIgranja);
		trakaMenija.add(rezim);
		setMenuBar(trakaMenija);
	}

	private Panel juzniPanel() {
		Panel p = new Panel();
		p.add(new Label("Novcica:"));
		novcica = new TextField("");
		p.add(novcica);
		p.add(poeni);
		DugmeAkcija osluskivac = new DugmeAkcija();
		pocni.addActionListener(osluskivac);
		p.add(pocni);
		return p;
	}

	private Panel istocniPanel() {
		Panel p = new Panel();
		p.setLayout(new GridLayout(1, 2));
		p.add(leviIstocniPanel());
		p.add(desniIstocniPanel());
		return p;
	}

	private Panel leviIstocniPanel() {
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		Label podloga = new Label("Podloga:");
		p.add(podloga, BorderLayout.CENTER);
		p.add(vreme, BorderLayout.SOUTH);
		return p;
	}

	private Panel desniIstocniPanel() {
		Panel p = new Panel();
		p.setLayout(new GridLayout(2, 1));
		Panel gornji = new Panel();
		gornji.setBackground(Color.GREEN);
		gornji.setLayout(new BorderLayout());
		Checkbox trava = new Checkbox("Trava", false, grupa);
		gornji.add(trava, BorderLayout.CENTER);
		Panel donji = new Panel();
		donji.setBackground(Color.LIGHT_GRAY);
		donji.setLayout(new BorderLayout());
		Checkbox zid = new Checkbox("Zid", false, grupa);
		donji.add(zid, BorderLayout.CENTER);
		p.add(gornji);
		p.add(donji);
		return p;
	}

	private class DugmeAkcija implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (rezim == Rezim.IGRANJE && !novcica.getText().equals("")) {
				if (uToku)
					mreza.zaustavi();
				mreza.pokreni(Integer.parseInt(novcica.getText()));
				timer.stvoriNit();
				timer.pokreni();
				uToku = true;
			}
		}
	}

	private class MeniAkcija implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (((MenuItem) e.getSource()).getLabel().equals("Rezim izmena")) {
				if (uToku) {
					mreza.zaustavi();
					postTok(false);
				}
				rezim = Rezim.IZMENA;
			} else if (((MenuItem) e.getSource()).getLabel().equals("Rezim igranja"))
				rezim = Rezim.IGRANJE;
		}
	}

	private class TastAkcija extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (uToku) {
				int k = e.getKeyCode();
				Polje p = null;
				if (k == KeyEvent.VK_A)
					p = mreza.dohvIgraca().dohvPolje().dohvPolje(-1, 0);
				else if (k == KeyEvent.VK_D)
					p = mreza.dohvIgraca().dohvPolje().dohvPolje(1, 0);
				else if (k == KeyEvent.VK_W)
					p = mreza.dohvIgraca().dohvPolje().dohvPolje(0, -1);
				else if (k == KeyEvent.VK_S)
					p = mreza.dohvIgraca().dohvPolje().dohvPolje(0, 1);
				if (p != null && p.dozvoljenaFigura())
					mreza.dohvIgraca().pomeri(p);
			}
		}
	}

	public static void main(String[] args) {
		new Igra();
	}

}
