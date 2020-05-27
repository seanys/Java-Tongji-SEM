package com.sean;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D04T02 {
    private JPanel panelMain;
    private JButton button_single;
    private JTextArea input_textarea;
    private JButton save_button;

    public D04T02() {
        button_single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 String c = input_textarea.getText();
                 Pattern pattern1 = Pattern.compile("[^0-9][0-9]*");
                 Matcher matcher1 = pattern1.matcher(c);
                 String string1 = matcher1.replaceAll("");
                 System.out.println(string1);
                 Pattern pattern2 = Pattern.compile("[0-9]*[^0-9]");
                 Matcher matcher2 = pattern2.matcher(c);
                 String string2 = matcher2.replaceAll("");
                 Pattern pattern3 = Pattern.compile("[0-9]*");
                 Matcher matcher3 = pattern3.matcher(c);
                 String method = matcher3.replaceAll("");
                 int a=Integer.parseInt(string1);
                 int b=Integer.parseInt(string2);
                 Caculate ca = new Caculate(a, b);
                 int res = 0;
                 switch (method) {
                 case "+":
                    res = ca.add();
                    break;
                 case "-":
                    res = ca.sub();
                    break;
                 case "*":
                    res = ca.multiply();
                    break;
                 case "/":
                    res = ca.divide();
                    break;
                 case "%":
                    res = ca.mod();
                    break;
                 default:
                    res = -1;
                 }
                input_textarea.setText(c+"\n"+a + method + b + "的输出结果:" + res);
            }
        });
        save_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter("/Users/sean/IdeaProjects/GUITest/src/save.txt", true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    writer.write(input_textarea.getText()+"\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    public static void main(String[] args) throws IOException {
        JFrame frame=new JFrame("App");
        frame.setContentPane(new D04T02().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,600);

    }

    public class Caculate {
        int int1;
        int int2;

        Caculate(int a, int b) {
            int1 = a;
            int2 = b;
        }

        int add() {
            return int1 + int2;
        }

        int sub() {
            return int1 - int2;
        }

        int sub(int a) {
            return int1 - int2;
        }

        int multiply() {
            return int1 * int2;
        }

        int divide() {
            return int1 / int2;
        }

        int mod() {
            return int1 % int2;
        }
    }
}
