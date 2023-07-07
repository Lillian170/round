package clas;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MyTable extends JTable {
	private MemberDB memberDB;

	public MyTable() {
		// database
		try {
			memberDB = new MemberDB();
			memberDB.queryData();

			setModel(new MyModle());

		} catch (Exception e) {
		}
	}

	private class MyModle extends DefaultTableModel {

		public boolean isCellEditable(int row, int column) {

			return column != 0;
		}

		public int getRowCount() {
			return memberDB.getRows();
		}

		public int getColumnCount() {
			return memberDB.getCols();
		}

		public Object getValueAt(int row, int column) {
			return memberDB.getData(row + 1, column + 1);
		}

	}
}
