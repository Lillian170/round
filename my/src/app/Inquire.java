package app;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import clas.MyTable;

public class Inquire extends JFrame {
	private MyTable myTable;

	public Inquire() {

		setLayout(new BorderLayout());
		myTable = new MyTable();
		JScrollPane jsp = new JScrollPane(myTable);
		add(jsp, BorderLayout.CENTER);

		setSize(800, 480);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Inquire();
	}

}
