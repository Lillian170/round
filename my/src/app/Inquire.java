package app;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import clas.MyTable;

public class Inquire extends JFrame {


	public Inquire(MyTable myTable,int score) {

		setLayout(new BorderLayout());

		myTable.getColumnModel().getColumn(0).setHeaderValue("帳號");
		myTable.getColumnModel().getColumn(1).setHeaderValue("分數");
		JScrollPane jsp = new JScrollPane(myTable);	
		add(jsp, BorderLayout.CENTER);
		add(new JLabel("排行榜",JLabel.CENTER), BorderLayout.NORTH);
		add(new JLabel("本次遊玩成績為: "+score,JLabel.CENTER), BorderLayout.SOUTH);
		
		
		setSize(250, 250);
		setVisible(true);

	}
}
