package fouriertransform.frame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fouriertransform.fourier.Fourier;
import fouriertransform.utils.Cycle;

/**
 * @author Fabian
 */

@SuppressWarnings("serial")
public class Frame extends JPanel implements ActionListener, ChangeListener {

	JFrame frame;

	public Frame() {
		frame = new JFrame("Fourier Transform drawing 3.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(1265, 670);
		frame.setVisible(true);
		frame.add(this);

		init();
	}

	Timer timer;

	public static double xVal = 0;
	public static ArrayList<Double> drawingX;
	public static ArrayList<Double> drawingY;
	public static ArrayList<Double> valuesX;
	public static ArrayList<Double> valuesY;
	public static ArrayList<Cycle> cycles;

	public JSlider cyclesSlider;

	private void init() {

		this.setLayout(null);

		cyclesSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, 20);
		cyclesSlider.addChangeListener(this);
		cyclesSlider.setLocation(20, 600);
		cyclesSlider.setSize(600, 20);
		cyclesSlider.setVisible(true);
		this.add(cyclesSlider);

		drawingX = new ArrayList<>();
		drawingY = new ArrayList<>();
		valuesX = new ArrayList<>();
		valuesY = new ArrayList<>();
		cycles = new ArrayList<>();

		this.setBackground(Color.BLACK);

		frame.addMouseMotionListener(new mouseListener());
		frame.addMouseListener(new mouseClick());

		timer = new Timer(1000 / 120, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2D = (Graphics2D) g;

		g2D.setColor(Color.WHITE);
		g2D.fillRect(20, 20, 600, 580);
		g2D.fillRect(640, 20, 600, 600);

		g2D.setColor(Color.GRAY);
		g2D.setFont(new Font("Courier", Font.BOLD, 50));
		g2D.drawString("DRAW HERE", 650, 60);

		g2D.setStroke(new BasicStroke(3));
		if (cycles.size() > 0) {
			cycles.get(0).drawCycle(g2D, xVal);
		}

		g2D.setColor(new Color(255, 0, 0));

		for (int i = 1; i < drawingX.size() && i < drawingY.size(); i++) {
			double posX = drawingX.get(i) * 300;
			double posY = drawingY.get(i) * 300;
			double lposX = drawingX.get(i - 1) * 300;
			double lposY = drawingY.get(i - 1) * 300;
			g2D.drawLine((int) lposX + 940, (int) lposY + 320, (int) posX + 940, (int) posY + 320);
		}
		if (1 < drawingX.size() && 1 < drawingY.size()) {
			double posX = drawingX.get(0) * 300;
			double posY = drawingY.get(0) * 300;
			double lposX = drawingX.get(drawingX.size() - 1) * 300;
			double lposY = drawingY.get(drawingY.size() - 1) * 300;
			g2D.drawLine((int) lposX + 940, (int) lposY + 320, (int) posX + 940, (int) posY + 320);
		}

		for (int i = 1; i < valuesX.size() && i < valuesY.size(); i++) {
			double x = Math.round(valuesX.get(i));
			double lx =  Math.round(valuesX.get(i - 1));
			double y =  Math.round(valuesY.get(i));
			double ly =  Math.round(valuesY.get(i - 1));
			g2D.drawLine((int) lx, (int) ly, (int) x, (int) y);
		}
		
		if (valuesX.size() > 1 && valuesY.size() > 1) {
			g2D.setColor(new Color(255, 0, 0, 126));
			g2D.setColor(Color.BLACK);
			g2D.fillOval((int) ((double) valuesX.get(valuesX.size() - 1)) - 3, (int) ((double) valuesY.get(valuesY.size() - 1)) - 3, 6, 6);
		}
	}

	public static int cycleSize = 0;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (cycleSize > 0) {
			xVal += (Math.PI / (cycleSize / 4));
			if (xVal >= Math.PI * 2) {
				xVal = 0;
			}
		} else {
			xVal = 0;
		}
		repaint();
	}

	public ArrayList<double[]> fourier;

	public void calcFourier() {
		while (drawingX.size() % 4 != 0) {
			drawingX.add(drawingX.get(drawingX.size()-1));
		}
		while (drawingY.size() % 4 != 0) {
			drawingY.add(drawingY.get(drawingY.size()-1));
		}
		@SuppressWarnings("unchecked")
		ArrayList<Double>[] data = new ArrayList[2];
		data[0] = drawingX;
		data[1] = drawingY;
		
		fourier = Fourier.fourier(data, 0);
		fourier.sort(new Comparator<double[]>() {
			@Override
			public int compare(double[] d2, double[] d1) {
				if (d1[1] > d2[1]) {
					return 1;
				}
				if (d1[1] < d2[1]) {
					return -1;
				}
				return 0;
			}
		});
	}

	public void updateCycleValue() {
		xVal = 0;
		cycles.clear();
		valuesX.clear();
		valuesY.clear();
		cycleSize = fourier.size();
		for (int i = 0; i < fourier.size(); i++) {
			int cancle = cyclesSlider.getValue();
			if (cyclesSlider.getValue() > 5) {
				int c = (int) ((1d / Math.pow(21 - cyclesSlider.getValue(), 2)) * cycleSize);
				if (c > cancle) {
					cancle = c;
				}
			}
			if (i == cancle) {
				break;
			}
			double[] data = fourier.get(i);
			if (cycles.isEmpty()) {
				cycles.add(new Cycle(320, 320, data[0], data[1], data[2], data[3]));
			} else {
				cycles.add(new Cycle(data[0], data[1], data[2], data[3]));
				cycles.get(i - 1).addCycle(cycles.get(i));
			}
		}
	}

	public static boolean update = false;

	private class mouseListener extends JComponent implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			if (x >= 640 + 3 && x <= 1240 + 3 && y >= 47 && y <= 647) {
				if (update == true) {
					drawingX.clear();
					drawingY.clear();
					update = false;
				}
				double posX = x - 943;
				double posY = y - 347;

				drawingX.add(posX / 300d);
				drawingY.add(posY / 300d);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	private class mouseClick extends JComponent implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			calcFourier();
			updateCycleValue();
			update = true;
		}

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		updateCycleValue();
	}
}
