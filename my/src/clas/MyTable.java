package clas;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

public class MyTable extends JTable {
	private MemberDB memberDB;
	private LinkedList<String> accounts,scores;
	public MyTable() {
		// database
		try {
			accounts = new LinkedList<>();
			scores = new LinkedList<>();

//			memberDB = new MemberDB();
//			memberDB.queryData();

			setModel(new MyModle());

		} catch (Exception e) {
		}
	}
	
	
	public void addData(String account, String score) {
		accounts.add(account);
		scores.add(score);
	}
	
	private class MyModle extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {

			return column != 0;
		}

		public int getRowCount() {
			return accounts.size();
		}

		public int getColumnCount() {
			return 2;
		}

		public Object getValueAt(int row, int column) {
			if(column == 0)
				return accounts.get(row);
			return scores.get(row);
		}

	}	
//	private class MyModle extends DefaultTableModel {
//
//		public boolean isCellEditable(int row, int column) {
//
//			return column != 0;
//		}
//
//		public int getRowCount() {
//			return memberDB.getRows();
//		}
//
//		public int getColumnCount() {
//			return memberDB.getCols();
//		}
//
//		public Object getValueAt(int row, int column) {
//			return memberDB.getData(row + 1, column + 1);
//		}
//
//	}
}
