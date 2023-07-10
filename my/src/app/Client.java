package app;

import java.io.*;
import java.net.*;
import javax.swing.*;

import clas.Inquire;
import clas.MyDrawerV2;
import clas.MyTable;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
	private JLabel result; // 顯示結果的標籤
	private JTextField usernameField; // 輸入使用者名稱的文本框
	private JPasswordField passwordField; // 輸入密碼的密碼框
	private JButton submitButton, submitButton2; // 提交按鈕
	private MyDrawerV2 o;
	private Socket socket; // 用於與伺服器建立連接的Socket對象
	private PrintWriter writer; // 用於向伺服器發送數據的PrintWriter對象
	private BufferedReader serverReader; // 用於從伺服器接收數據的BufferedReader對象
	private int myport;

	public Client() {
		super("入口"); // 設定窗口標題

		usernameField = new JTextField(20); // 創建一個寬度為20的文本框來輸入使用者名稱
		passwordField = new JPasswordField(20); // 創建一個寬度為20的密碼框來輸入密碼
		result = new JLabel("輸入帳密"); // 創建一個用於顯示結果的標籤
		submitButton = new JButton("送出"); // 創建一個標籤為"送出"的按鈕

		JPanel top = new JPanel(new FlowLayout()); // 使用流式布局
		top.add(new JLabel("帳號：")); // 添加一個標籤，顯示"帳號："
		top.add(usernameField); // 添加使用者名稱文本框
		top.add(new JLabel("密碼：")); // 添加一個標籤，顯示"密碼："
		top.add(passwordField); // 添加密碼框
		top.add(result);
		top.add(submitButton); // 添加提交按鈕

		add(top, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE); // 設定關閉窗口時的默認操作
		setSize(265, 150); // 設定窗口大小為300x150
		setVisible(true); // 顯示窗口

		o = new MyDrawerV2(writer, serverReader);
		submitButton2 = new JButton("送出成績");

		myport = -1;
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (myport == -1) {
					for (int port = 8888; port <= 8890; port++) {
						try {
							socket = new Socket("172.20.10.4", port); // 建立與伺服器的連接，伺服器的IP地址為172.20.10.4，端口號為8888
							writer = new PrintWriter(socket.getOutputStream(), true); // 建立向伺服器發送數據的PrintWriter對象
							serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); // 建立從伺服器接收數據的BufferedReader對象
							myport = port;
							break;
						} catch (Exception e1) {
//						System.out.println(e1); // 如果發生異常，則輸出異常信息
						}
					}

					if (myport != -1)
						setTitle("入口:" + Integer.toString(myport));
					else
						setTitle("入口:無法連線");
				}
				
				if (sendData()) {
					o.add(submitButton2);
					top.setVisible(false);
					add(o, BorderLayout.CENTER);
					setSize(750, 750); // 如果數據發送成功，則將窗口大小設定為750x750
				}
			}
		});

		submitButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					writer.println(o.getscore());
					MyTable myTable = new MyTable();
					for (int i = 1; i < 4; i++) {
						String account = serverReader.readLine();
						String score = serverReader.readLine();
						System.out.println(account);
						System.out.println(score);
						myTable.addData(account, score);

					}
					new Inquire(myTable, o.getscore());
				} catch (Exception e1) {
					System.out.println(e1);
				}

			}
		});

	}

	private boolean sendData() {
		String username = usernameField.getText(); // 獲取使用者名稱文本框的內容
		String password = new String(passwordField.getPassword()); // 獲取密碼框的內容

		if (username.equals("") || password.equals("")) {
			result.setText("不能空白");
			return false;
		}

		writer.println(username); // 向伺服器發送使用者名稱
		writer.println(password); // 向伺服器發送密碼

		String response = null;

		try {
			response = serverReader.readLine(); // 從伺服器讀取響應
			
			result.setText(String.format(response)); // 在結果標籤中顯示響應

		} catch (Exception e) {
			System.out.println(e); // 如果發生異常，則輸出異常信息
		}

//		return false;
		if (response.equals("密碼錯誤")) {
			return false; // 如果響應為"密碼錯誤"，則返回false
		} else {
			return true; // 否則返回true
		}
	}

	public static void main(String[] args) {
		new Client(); // 創建Client對象，啟動客戶端應用程序
	}
}
