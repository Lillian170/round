package clas;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

public class MyTable extends JTable {
	private MemberDB memberDB; // 宣告一個名為memberDB的MemberDB物件（可能是外部類別的一部分）
	private LinkedList<String> accounts, scores; // 宣告兩個LinkedList<String>物件，分別命名為accounts和scores

	public MyTable() {
		try {
			accounts = new LinkedList<>(); // 初始化accounts LinkedList
			scores = new LinkedList<>(); // 初始化scores LinkedList

			setModel(new MyModle()); // 使用自定義的MyModle作為TableModel來設置JTable的模型

		} catch (Exception e) {
			// 錯誤處理程式碼
		}
	}

	public void addData(String account, String score) {
		accounts.add(account); // 將account字串加入accounts LinkedList
		scores.add(score); // 將score字串加入scores LinkedList
	}

	private class MyModle extends DefaultTableModel {
		// 自定義的TableModel類別，繼承自DefaultTableModel

		public boolean isCellEditable(int row, int column) {
			// 覆寫isCellEditable方法，使除了第一列以外的所有儲存格都不可編輯
			return column != 0;
		}

		public int getRowCount() {
			// 覆寫getRowCount方法，返回資料的列數，即accounts LinkedList的大小
			return accounts.size();
		}

		public int getColumnCount() {
			// 覆寫getColumnCount方法，返回資料的欄數，這裡固定為2
			return 2;
		}

		public Object getValueAt(int row, int column) {
			// 覆寫getValueAt方法，返回指定儲存格(row, column)的值
			if (column == 0)
				return accounts.get(row); // 如果column為0，返回accounts LinkedList中指定row的元素
			return scores.get(row); // 否則返回scores LinkedList中指定row的元素
		}

	}
}
