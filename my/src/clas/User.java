package clas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import clas.BCrypt;

public class User {
	public int d; // 用於儲存操作結果的變數
	public String UserAccount; // 使用者帳號
	private Connection conn; // 資料庫連接物件

	public User(String a, String p) {
		try {
			this.UserAccount = a;

			Properties prop = new Properties();
			prop.put("user", "root"); // 資料庫使用者名稱
			prop.put("password", "root"); // 資料庫使用者密碼
			conn = DriverManager.getConnection("jdbc:mysql://localhost/iii", prop); // 建立與資料庫的連接

			String sqlDup = "SELECT * FROM member WHERE account = ?"; // 檢查帳號是否已存在的SQL查詢語句
			PreparedStatement pstmtDup = conn.prepareStatement(sqlDup);
			pstmtDup.setString(1, a); // 將帳號設定為查詢語句的參數
			ResultSet rs = pstmtDup.executeQuery(); // 執行查詢

			if (rs.next()) { // 如果查詢結果存在記錄，表示帳號已存在
				String hasPasswd = rs.getString("password"); // 取得資料庫中儲存的密碼
				if (BCrypt.checkpw(p, hasPasswd)) { // 檢查輸入的密碼是否與資料庫中的密碼相符
					d = 2; // 登入成功
				} else {
					d = 0; // 密碼錯誤
				}
			} else {
				String sql = "INSERT INTO member (account, password) VALUES (?, ?)"; // 新增帳號的SQL語句
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, a); // 將帳號設定為插入語句的參數
				pstmt.setString(2, BCrypt.hashpw(p, BCrypt.gensalt())); // 將密碼進行雜湊處理後設定為插入語句的參數
				d = 1; // 新增帳號成功
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public void UpdateScore() {
		try {
			String sqlDup = "SELECT * FROM member WHERE account = ?"; // 查詢指定帳號的SQL語句
			PreparedStatement pstmtDup = conn.prepareStatement(sqlDup, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE); // 預編譯查詢語句，並設定結果集的可滾動和可更新
			pstmtDup.setString(1, this.UserAccount); // 將使用者帳號設定為查詢語句的參數

			ResultSet rs = pstmtDup.executeQuery(); // 執行查詢

			rs.absolute(1); // 將結果集的指標移動到第一筆記錄

			rs.updateString("score", "0"); // 更新該筆記錄中的score欄位為0
			rs.updateRow(); // 將更新寫入資料庫

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}