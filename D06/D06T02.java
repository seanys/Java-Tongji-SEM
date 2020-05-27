package com.sean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D06T02 {
    private JPanel panelMain;
    private JButton button1;
    private JTextArea textArea1;

    public D06T02() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathname = "/Users/sean/Documents/MyProjects/IdeaProjects/GUITest/src/com/sean/index.html";
                D06T02 func=new D06T02();
                try {
                    String lines=func.ReadFile(pathname);
                    textArea1.setText(lines);
                    func.MatchFunction(lines);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void MatchFunction(String line){
        Pattern textareaPattern = Pattern.compile(".*<textarea>.*</textarea>");
        Matcher textareaMatch = textareaPattern.matcher(line);
        Pattern buttonPattern = Pattern.compile(".*<button>.*</button>");
        Matcher buttonMatch = buttonPattern.matcher(line);
        Pattern labelPattern = Pattern.compile(".*<label>.*</label>");
        Matcher labelMatch = labelPattern.matcher(line);
        String result="";
        if (textareaMatch.matches()){
            result=textareaMatch.group(0);
            System.out.println(result);
            System.out.println("textarea");
        }
        if (buttonMatch.matches()){
            result=buttonMatch.group(0);
            System.out.println(result);
            System.out.println("button");
        }
        if (labelMatch.matches()){
            result=labelMatch.group(0);
            System.out.println(result);
            System.out.println("label");
        }
    }

    public String ReadFile(String pathname) throws IOException {
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        String lines="";
        D06T02 func=new D06T02();
        lines="";
        lines=line = br.readLine();
        while (line != null) {
            line =br.readLine(); // 一次读入一行数据
            if(line!=null){
                lines=lines+"\n"+line;
                func.MatchFunction(line);
            }

        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        JFrame frame=new JFrame("生成HTML");
        frame.setContentPane(new D06T02().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,600);
    }
}
