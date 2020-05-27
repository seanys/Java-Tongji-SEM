package com.sean;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D04T01 {
    private JButton button_msg;
    private JPanel panelMain;
    private JLabel newLable;
    private JTextArea textArea1;
    private JTable newTable;
    private Scanner x;

    public String[] columnNames={"Name","Eye=Color","Gender"};

    public Object[][] data={
            {"Bill","Hazel","male"},
            {"Mary","Black","female"},
    };

    public D04T01() {
        button_msg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null,"读取成功");
                FileReader file= null;
                try {
                    file = new FileReader("/Users/sean/IdeaProjects/GUITest/src/data.txt");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                BufferedReader reader=new BufferedReader(file);
                String text="";
                String line= null;

                try {
                    line = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                while (line!=null){
                    if(hasDigit(line)){
                        String[] array = line.split(" ");
                        Double price,num;
                        num=Double.parseDouble(array[1]);
                        price=Double.parseDouble(array[2]);
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        text+=line+" "+formatter.format(num*price)+"\n";
                    }else{
                        text+=line+" 总价"+"\n";
                    }
                    try {
                        line=reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                textArea1.setText(text);

                File writename = new File("/Users/sean/IdeaProjects/GUITest/src/new.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
                try {
                    writename.createNewFile(); // 创建新文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new FileWriter(writename));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    out.write(text); // \r\n即为换行
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    out.flush(); // 把缓存区内容压入文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    out.close(); // 最后记得关闭文件
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    public static void main(String[] args) throws IOException {
        JFrame frame=new JFrame("D04T01");
        frame.setContentPane(new D04T01().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,600);

    }

    public boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches())
            flag = true;
        return flag;
    }
}
