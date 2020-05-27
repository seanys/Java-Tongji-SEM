package com.sean;
import java.io.*;
import java.util.*;


public class D03T01 {


    public static void main(String []args) throws IOException {
        /* 使用构造器创建两个对象 */
        Judge judge=new Judge();
        int t=0;
        File f = new File("./prime.txt");
        FileOutputStream fop = new FileOutputStream(f);
        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");


        for(int i=0;i<1000;i++){
            boolean judge_result=judge.PrimeJudge(i);
            if(judge_result){
                t++;
                try {
                    writer.append(i+"\t");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(t==9){
                    t=0;
                    try {
                        writer.append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        writer.close();
        fop.close();
    }

}
