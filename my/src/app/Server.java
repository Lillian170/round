package app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Thread;

import clas.MemberDB;
import clas.User;

public class Server extends Thread {
	User y;
	public int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		try {
			// 建立伺服器端的ServerSocket物件，並指定監聽的埠號為8888
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("等待客戶端連接..." + Integer.toString(port));

			// 等待客戶端連接，並建立Socket物件來處理連線
			Socket socket = serverSocket.accept();
			System.out.println("客戶端已連接：" + Integer.toString(port) + socket.getInetAddress());

			// 建立讀取客戶端訊息的BufferedReader物件
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 建立寫入回覆訊息的PrintWriter物件
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			while (true) {
				// 讀取客戶端傳送的使用者名稱和密碼
				String username = reader.readLine();
				String password = reader.readLine();

				// 建立User物件，使用傳送過來的使用者名稱和密碼
				y = new User(username, password);

				// 根據User物件的d值進行身份驗證
				if (y.d == 1) {
					// 若d值為1，表示創建成功，傳送創建成功訊息給客戶端
					writer.println("創建成功");

					// 關閉ServerSocket，結束程式
					serverSocket.close();
					break;
				} else if (y.d == 2) {
					// 若d值為2，表示登入成功，傳送登入成功訊息給客戶端
					writer.println("登入成功");
					// y.UpdateScore();
					// 關閉ServerSocket，結束程式
					serverSocket.close();
					break;
				} else {
					// 若d值不為1或2，表示密碼錯誤，傳送密碼錯誤訊息給客戶端
					writer.println("密碼錯誤");
				}
			}

			while (true) {
				String score = reader.readLine();
				System.out.println(score);
				y.UpdateScore(score);

				MemberDB memberDB = new MemberDB();
				memberDB.queryData();
				for (int i = 1; i < 4; i++) {// 取前3名
					writer.println(memberDB.getData(i, "account"));
					writer.println(memberDB.getData(i, "score"));
				}
			}
		} catch (Exception e) {
			// 若發生例外錯誤，印出錯誤訊息
//			System.err.println(e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		LinkedList<Server> servers;
		servers = new LinkedList<>();
		for (int port = 8888; port <= 8890; port++) {
			Server server = new Server(port);
			servers.add(server);
		}
		for (Server server : servers) {
			server.start();
		}

		while (true) {
			for (int i = 0; i < servers.size(); i++) {
				if (!servers.get(i).isAlive()) {
					int port = servers.get(i).port;
					servers.add(new Server(port));
					servers.getLast().start();
					servers.remove(i);
					break;
				}
			}
			Thread.sleep(500);
		}

	}
}
