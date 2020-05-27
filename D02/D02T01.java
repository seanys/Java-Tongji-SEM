import java.io.*;
import java.util.*;


class Caculate1 {

   public void mutil(int a) {
      System.out.println(a<<1<<1<<1<<1);
      // return a<<1<<1<<1<<1;
   }

   public void mutil(double a) {
      System.out.println(a*16);
      // return a*16;
   }


}

public class D02T01 {
   // Employee 类的构造器
   public static void main(String[] args) throws IOException {
      Scanner scan = new Scanner(System.in);

      System.out.println("输入数字:");
      String content = scan.next();
      System.out.println(content);

      Caculate1 test=new Caculate1();

      if(content.indexOf(".")>0){
         double a=Double.parseDouble(content);
         test.mutil(a);
      }

      if(content.indexOf(".")==-1){
         int a=Integer.parseInt(content);
         test.mutil(a);
      }

      scan.close();
   }
}