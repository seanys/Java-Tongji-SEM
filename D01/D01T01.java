import java.io.*;
import java.util.*;
import java.util.regex.*;

class Caculate {
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

   // 这个是重载，但是没太读懂老师的要求，应该不需要用重载也可以写的
   // 而且本来就必须是全是int
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

public class D01T01 {
   // Employee 类的构造器
   public static void main(String[] args) throws IOException {
      // Scanner scan = new Scanner(System.in);

      // System.out.println("输入式子:");
      // String c = scan.next();
      //获取第二个数
      // Pattern pattern1 = Pattern.compile("[^0-9][0-9]*");
      // Matcher matcher1 = pattern1.matcher(c);
      // String string1 = matcher1.replaceAll("");
      // System.out.println(string1);
      // //获取第一个数
      // Pattern pattern2 = Pattern.compile("[0-9]*[^0-9]");
      // Matcher matcher2 = pattern2.matcher(c);
      // String string2 = matcher2.replaceAll("");
      // // 获取10
      // Pattern pattern3 = Pattern.compile("[0-9]*");
      // Matcher matcher3 = pattern3.matcher(c);
      // String method = matcher3.replaceAll("");
      // int a=Integer.parseInt(string1);
      // int b=Integer.parseInt(string2);
      // Caculate ca = new Caculate(a, b);
      // int res = 0;
      // switch (method) {
      // case "+":
      //    res = ca.add();
      //    break;
      // case "-":
      //    res = ca.sub();
      //    break;
      // case "*":
      //    res = ca.multiply();
      //    break;
      // case "/":
      //    res = ca.divide();
      //    break;
      // case "%":
      //    res = ca.mod();
      //    break;
      // default:
      //    res = -1;
      // }
      // System.out.println(a + method + b + "的输出结果:" + res);

      // scan.close();
      String c="zoo";
      Pattern pattern1 = Pattern.compile("[^0-9][0-9]*");
      Matcher matcher1 = pattern1.matcher(c);
      String string1 = matcher1.replaceAll("");
      System.out.println(string1);

   }
}
