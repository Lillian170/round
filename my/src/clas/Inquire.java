package clas;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import clas.MyTable;

public class Inquire extends JFrame {

    // Inquire類別的建構子，接收MyTable物件和一個分數作為參數
    public Inquire(MyTable myTable, int score) {

        // 設定視窗的佈局為邊界布局
        setLayout(new BorderLayout());

        // 設定表格的欄位標題
        myTable.getColumnModel().getColumn(0).setHeaderValue("帳號");
        myTable.getColumnModel().getColumn(1).setHeaderValue("分數");

        // 建立一個帶有捲軸的視窗容器，並將表格放置其中
        JScrollPane jsp = new JScrollPane(myTable);	
        add(jsp, BorderLayout.CENTER);

        // 在視窗的北方（上方）加入一個標籤顯示"排行榜"
        add(new JLabel("排行榜", JLabel.CENTER), BorderLayout.NORTH);

        // 在視窗的南方（下方）加入一個標籤顯示"本次遊玩成績為: "後接著的分數
        add(new JLabel("本次遊玩成績為: " + score, JLabel.CENTER), BorderLayout.SOUTH);

        // 設定視窗的大小為250x250
        setSize(250, 250);

        // 顯示視窗
        setVisible(true);
    }
}
