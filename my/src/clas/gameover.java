package clas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class gameover extends JFrame{
	private MyTable myTable;
	private JButton f5;
	private MyDrawerV2 go;
	private User u;
	public gameover() {
		u.UpdateScore();
		f5=new JButton("再來一局");
		myTable = new MyTable();
		JPanel j = new JPanel(new FlowLayout()); 
		j.add(new JScrollPane(myTable));
		j.add(f5);
		f5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				j.removeAll();
				add(go, BorderLayout.CENTER);
				setSize(750, 750);
			}
		});
	}
}
