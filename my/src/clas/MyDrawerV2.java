package clas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MyDrawerV2 extends JPanel {
	private LinkedList<Line> lines, garbag;
	private Color nowColor;
	private float nowWidth;
	private boolean islive;
	private JButton f5;
	private PrintWriter writer;
	private BufferedReader serverReader;

	public MyDrawerV2(PrintWriter writer, BufferedReader serverReader) {
		lines = new LinkedList<>();
		garbag = new LinkedList<>();
		MyListener myListener = new MyListener();
		addMouseListener(myListener);
		addMouseMotionListener(myListener);
		this.writer = writer;
		this.serverReader = serverReader;
		f5 = new JButton("再來一局");
		add(f5);
		f5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		nowColor = Color.BLUE;
		nowWidth = 4;
		islive = true;
	}

	public boolean getislive() {
		return islive;
	}

	public int getscore() {
		if (lines.size() != 0)
			return lines.getLast().getSize();
		return 0;
	}

	public void setColor(Color newColor) {
		nowColor = newColor;
	}

	public Color getColor() {
		return nowColor;
	}

	public void setWidth(float newWidth) {
		nowWidth = newWidth;
	}

	private class MyListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int x = e.getX(), y = e.getY();
			Line line = new Line(nowColor, nowWidth);// 加line
			if (boom(x, y) && islive) {
				line.addPoint(x, y);// 加點
				lines.add(line);// lines+line
			} else if(lines.size() != 0 ){
				islive = false;
				repaint();
			}
		}

		public void mouseDragged(MouseEvent e) {
			int x = e.getX(), y = e.getY();
			if (boom(x, y) && islive) {
				lines.getLast().addPoint(x, y);
				repaint();
				System.out.println(lines.getLast().getSize());
			} else if(lines.size() != 0 ){
				islive = false;
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
//			new gameover();
			if (lines.size() != 0) {
				islive = false;
				repaint();
			}
		}
	}

	public boolean boom(int mouseX, int mouseY) {
		int a = mouseX - 375, b = mouseY - 375;
		double L = Math.pow((Math.pow(a, 2) + Math.pow(b, 2)), 0.5);
		if (L > 225 + 2 && L < 240 - 2) {
			return true;
		} else {
			return false;
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g.drawOval(150, 150, 450, 450);
		g.drawOval(135, 135, 480, 480);
		for (Line line : lines) {
			g2d.setColor(line.getColor());
			g2d.setStroke(new BasicStroke(line.getWidth()));
			for (int i = 1; i < line.getSize(); i++) {
				g2d.drawLine(line.getPointX(i - 1), line.getPointY(i - 1), line.getPointX(i), line.getPointY(i));
			}
		}
	}

	public void clear() {
		lines.clear();
		garbag.clear();
		repaint();
		islive = true;
	}

	public void undo() {
		if (lines.size() > 0) {
			garbag.add(lines.removeLast());
			repaint();
		}
	}

	public void redo() {
		if (garbag.size() > 0) {
			lines.add(garbag.removeLast());
			repaint();
		}
	}

	public void saveJPEG() {
		System.out.println("saveJPEG()1");
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		paint(g);
		try {
			if (ImageIO.write(img, "jpg", new File("dir1/brad.jpg"))) {
				System.out.println("OK");
			} else {
				System.out.println("XX");
			}
			System.out.println("saveJPEG()2");
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void saveLines() throws Exception {
		try (ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream("dir1/brad.sign"))) {
			oout.writeObject(lines);
			oout.flush();
		}
	}

	public void loadLines() throws Exception {
		ObjectInputStream oin = new ObjectInputStream(new FileInputStream("dir1/brad.sign"));
		Object obj = oin.readObject();
		lines = (LinkedList<Line>) obj;
		oin.close();
		repaint();
	}
}

class Line implements Serializable {
	private LinkedList<HashMap<String, Integer>> points;
	private Color color;
	private float width;

	public Line(Color color, float width) {
		points = new LinkedList<>();
		this.color = color;
		this.width = width;
	}

	public void addPoint(int x, int y) {
		HashMap<String, Integer> point = new HashMap<>();
		point.put("x", x);
		point.put("y", y);
		if (!points.contains(point)) {
			points.add(point);
		}
	}

	public int getPointX(int index) {
		return points.get(index).get("x");
	}

	public int getPointY(int index) {
		return points.get(index).get("y");
	}

	public int getSize() {
		return points.size();
	}

	public Color getColor() {
		return color;
	}

	public float getWidth() {
		return width;
	}
}