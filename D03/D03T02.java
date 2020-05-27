package com.sean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class D03T02 {

    public double totalCost=0;
    public double totalNum=0;
    public double averagePrice=0;

    public D03T02() throws IOException {

    }

    public void fileaction() throws IOException {
        FileReader file=  new FileReader("/Users/sean/IdeaProjects/GUITest/src/data.txt");
        BufferedReader reader=new BufferedReader(file);
        String text="";
        String line= reader.readLine();

        NumberFormat formatter = new DecimalFormat("#0.00");
        while (line!=null){
            if(hasDigit(line)){
                String[] array = line.split(" ");
                Double price,num;
                num=Double.parseDouble(array[1]);
                price=Double.parseDouble(array[2]);
                totalCost+=num*price;
                totalNum+=num;
                text+=line+" "+formatter.format(num*price)+"\n";
            }else{
                text+=line+" 总计"+"\n";
            }
            line=reader.readLine();
        }
        averagePrice=totalCost/totalNum;
        text+="平均价格       "+formatter.format(averagePrice)+"元每个\n";

        File writename = new File("/Users/sean/IdeaProjects/GUITest/src/new.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        out.write(text);
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        D03T02 action =new D03T02();
        action.fileaction();
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