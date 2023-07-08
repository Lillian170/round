package clas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MemberDB {
	private Connection conn;  // 用於建立與資料庫的連接
	private String[] fieldNames;  // 儲存資料庫欄位名稱的陣列
	private ResultSet rs;  // 儲存執行查詢後的結果集
	
	public MemberDB() throws SQLException {
		Properties prop = new Properties();
		prop.put("user", "root");  // 資料庫使用者名稱
		prop.put("password", "root");  // 資料庫密碼
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/iii", prop);  // 連接到MySQL資料庫
	}
	
	public void queryData() throws SQLException {
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

		rs = stmt.executeQuery("SELECT account, score FROM member ORDER BY score DESC");  // 執行查詢，選擇member資料表中的account和score欄位，按照score欄位降序排序
		
		ResultSetMetaData rsmd = rs.getMetaData();
		fieldNames = new String[rsmd.getColumnCount()];  // 初始化fieldNames陣列，長度為結果集中的欄位數量
		for (int i = 0; i < fieldNames.length; i++) {
			fieldNames[i] = rsmd.getColumnName(i + 1);  // 獲取欄位名稱，並將其存儲到fieldNames陣列中
		}
	}
	
	public int getRows() {
		try {
			rs.last();  // 將結果集的游標移動到最後一列
			return rs.getRow();  // 獲取結果集中的列數
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int getCols() {
		return fieldNames.length;  // 返回欄位數量
	}
	
	public String getData(int row, int col) {
		try {
			rs.absolute(row);  // 將結果集的游標移動到指定的列
			return rs.getString(fieldNames[col - 1]);  // 根據欄位名稱獲取該列的資料
		} catch (Exception e) {
			return "XXX";
		}
	}
	
	public String getData(int row, String col) {
		try {
			rs.absolute(row);  // 將結果集的游標移動到指定的列
			return rs.getString(col);  // 根據欄位名稱獲取該列的資料
		} catch (Exception e) {
			return "XXX";
		}
	}	
}
