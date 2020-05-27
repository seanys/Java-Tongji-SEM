package com.sean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class D06T01 {
    private JPanel panelMain;
    private JButton ConfirmCreate;
    private JTextField Name;
    private JComboBox Tag;
    private JTextField Width;
    private JTextField Content;

    public String content="这是标题";
    public String tag="textarea";
    public String name="name";
    public int width=100;
    public String[] tags={"textarea", "label", "button"};

    public D06T01(){
        ConfirmCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //绑定点击输入
                if(Content.getText()!=""){
                    content=Content.getText();
                }
                if(Name.getText()!=""){
                    name=Name.getText();
                }
                if(Width.getText()!=""){
                    width=Integer.parseInt(Width.getText());
                }
                tag=tags[Tag.getSelectedIndex()];
                System.out.println(content);
                System.out.println(tag);
                System.out.println(name);
                D06T01 func=new D06T01();
                try {
                    func.Write(width,tag,content);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void Write(int width,String tag,String content) throws IOException {
        File BasicFileName=new File("src/com/sean/index.html");

        BasicFileName.createNewFile(); // 创建新文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(BasicFileName));

        try {
            writer.write("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>"+name+"</title>\n" +
                    "</head>\n" +
                    "<body>\n");

            writer.write("<"+tag+">");
            writer.write(content);
            writer.write("</"+tag+">\n");
            writer.write("<p>宽度:"+width+"</p>\n");
            writer.write("</body>\n" +
                    "</html>");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            writer.flush(); // 把缓存区内容压入文件
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        JFrame frame=new JFrame("生成HTML");
        frame.setContentPane(new D06T01().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,600);
    }
}
