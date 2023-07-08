package clas;
// 定義包名為clas

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
	// 定義MyDrawerV2類別，繼承自JPanel

	private LinkedList<Line> lines, garbag;
	// 定義LinkedList類型的lines和garbag變數，用於儲存線條資訊
	private Color nowColor;
	// 定義Color類型的變數nowColor，用於表示當前選擇的顏色
	private float nowWidth;
	// 定義float類型的變數nowWidth，用於表示當前選擇的線條寬度
	private boolean islive;
	// 定義boolean類型的變數islive，用於表示繪圖板是否可繼續繪製
	private JButton f5;
	// 定義JButton類型的變數f5，用於表示重新開始的按鈕
	private PrintWriter writer;
	private BufferedReader serverReader;
	// 定義PrintWriter和BufferedReader類型的變數writer和serverReader，用於網絡通信

	public MyDrawerV2(PrintWriter writer, BufferedReader serverReader) {
		// MyDrawerV2類別的建構子，接收PrintWriter和BufferedReader物件作為參數
		lines = new LinkedList<>();
		// 初始化lines為一個空的LinkedList物件，用於儲存線條資訊
		garbag = new LinkedList<>();
		// 初始化garbag為一個空的LinkedList物件，用於儲存被刪除的線條資訊
		MyListener myListener = new MyListener();
		// 創建MyListener物件
		addMouseListener(myListener);
		// 添加滑鼠監聽器，用於處理滑鼠點擊事件
		addMouseMotionListener(myListener);
		// 添加滑鼠移動監聽器，用於處理滑鼠拖曳事件
		this.writer = writer;
		this.serverReader = serverReader;
		// 初始化writer和serverReader變數，用於網絡通信
		f5 = new JButton("再來一局");
		// 創建一個JButton物件，標籤為"再來一局"
		add(f5);
		// 將按鈕添加到繪圖板上
		f5.addActionListener(new ActionListener() {
			// 為按鈕添加點擊事件監聽器
			public void actionPerformed(ActionEvent e) {
				// 當按鈕被點擊時觸發該方法
				clear();
				// 呼叫clear方法，清除繪圖板上的內容
			}
		});
		nowColor = Color.BLUE;
		// 將nowColor變數設置為藍色
		nowWidth = 4;
		// 將nowWidth變數設置為4
		islive = true;
		// 將islive變數設置為true，表示繪圖板可繼續繪製
	}

	public boolean getislive() {
		// 取得islive變數的值
		return islive;
	}

	public int getscore() {
		// 取得分數，即線條數量
		if (lines.size() != 0)
			return lines.getLast().getSize();
		return 0;
	}

	public void setColor(Color newColor) {
		// 設置當前顏色
		nowColor = newColor;
	}

	public Color getColor() {
		// 取得當前顏色
		return nowColor;
	}

	public void setWidth(float newWidth) {
		// 設置當前線條寬度
		nowWidth = newWidth;
	}

	private class MyListener extends MouseAdapter {
		// 繼承自MouseAdapter的內部類別，用於處理滑鼠事件

		public void mousePressed(MouseEvent e) {
			// 當滑鼠按下時觸發該方法
			int x = e.getX(), y = e.getY();
			// 取得滑鼠座標
			Line line = new Line(nowColor, nowWidth);
			// 創建Line物件，並設置顏色和寬度
			if (boom(x, y) && islive) {
				// 如果滑鼠座標在指定範圍內，並且繪圖板可繼續繪製
				line.addPoint(x, y);
				// 在線條中添加一個點
				lines.add(line);
				// 將線條添加到lines集合中

			} else if (lines.size() != 0) {
				islive = false;
				repaint();
			}
		}

		public void mouseDragged(MouseEvent e) {
			// 當滑鼠拖曳時觸發該方法
			int x = e.getX(), y = e.getY();
			// 取得滑鼠座標
			if (boom(x, y) && islive) {
				// 如果滑鼠座標在指定範圍內，並且繪圖板可繼續繪製
				lines.getLast().addPoint(x, y);
				// 在最後一個線條中添加一個點
				repaint();
				System.out.println(lines.getLast().getSize());
			} else if (lines.size() != 0) {
				islive = false;
				repaint();
			}
		}

		public void mouseReleased(MouseEvent e) {
			// 當滑鼠釋放時觸發該方法
			if (lines.size() != 0) {
				islive = false;
				repaint();
			}
		}
	}

	public boolean boom(int mouseX, int mouseY) {
		// 判斷滑鼠座標是否在指定範圍內
		int a = mouseX - 375, b = mouseY - 375;
		// 計算滑鼠座標與中心點的距離
		double L = Math.pow((Math.pow(a, 2) + Math.pow(b, 2)), 0.5);
		// 計算距離
		if (L > 225 + 2 && L < 240 - 2) {
			// 如果距離在指定範圍內，返回true
			return true;
		} else {
			// 否則返回false
			return false;
		}
	}

	protected void paintComponent(Graphics g) {
		// 覆寫JPanel的paintComponent方法，用於繪製圖形
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g.drawOval(150, 150, 450, 450);
		g.drawOval(135, 135, 480, 480);
		// 繪製兩個圓形
		for (Line line : lines) {
			// 遍歷所有的線條
			g2d.setColor(line.getColor());
			g2d.setStroke(new BasicStroke(line.getWidth()));
			// 設置畫筆顏色和寬度
			for (int i = 1; i < line.getSize(); i++) {
				// 遍歷線條中的所有點，繪製連接線段
				g2d.drawLine(line.getPointX(i - 1), line.getPointY(i - 1), line.getPointX(i), line.getPointY(i));
			}
		}
	}

	public void clear() {
		// 清除繪圖板上的內容
		lines.clear();
		
		// 清空lines集合
		repaint();
		// 重新繪製繪圖板
		islive = true;
		// 將islive設置為true，表示繪圖板可繼續繪製
	}
}

class Line implements Serializable {
	// 定義Line類別，實現Serializable接口，用於將物件序列化
	private LinkedList<HashMap<String, Integer>> points;
	// 定義LinkedList類型的points變數，用於儲存線條上的點的座標
	private Color color;
	// 定義Color類型的變數color，用於表示線條的顏色
	private float width;
	// 定義float類型的變數width，用於表示線條的寬度

	public Line(Color color, float width) {
		// Line類別的建構子，接收顏色和寬度作為參數
		points = new LinkedList<>();
		// 初始化points為一個空的LinkedList物件，用於儲存點的座標
		this.color = color;
		// 將傳入的顏色設置給color變數
		this.width = width;
		// 將傳入的寬度設置給width變數
	}

	public void addPoint(int x, int y) {
		// 在線條中添加一個點的座標
		HashMap<String, Integer> point = new HashMap<>();
		point.put("x", x);
		point.put("y", y);
		if (!points.contains(point)) {
			points.add(point);
		}
	}

	public int getPointX(int index) {
		// 取得指定索引處的點的x座標
		return points.get(index).get("x");
	}

	public int getPointY(int index) {
		// 取得指定索引處的點的y座標
		return points.get(index).get("y");
	}

	public int getSize() {
		// 取得線條上的點的數量
		return points.size();
	}

	public Color getColor() {
		// 取得線條的顏色
		return color;
	}

	public float getWidth() {
		// 取得線條的寬度
		return width;
	}
}
