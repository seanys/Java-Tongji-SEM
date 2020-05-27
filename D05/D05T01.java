import java.io.*;
import java.util.*;


public class D05T01 {
    int max=1000;
    int min=1;

    public D05T01() {
//        this.max = max;
//        this.min = min;
    }

    // Employee 类的构造器
    public static void main(String[] args) throws IOException {
        D05T01 test=new D05T01();
        System.out.println(test.randomNum());
        OutputStream out = new FileOutputStream("./src/f.dat");
        try {
            System.out.println("执行成功");
            for(int i=0;i<1000;i++)
            {
                out.write(test.randomNum());
                out.write(',');
            }
//            out.write(1);
        } finally {
            // Make sure to close the file when done
            out.close();
        }
        File file = new File("./src/f.dat");
        Scanner scnr = new Scanner(file);
        while(scnr.hasNextLine()){
            System.out.println("执行读取文件");
            String line = scnr.nextLine();
            System.out.println(line);
        }
    }
    private int randomNum(){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
