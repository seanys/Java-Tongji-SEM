import java.io.*;
import java.math.BigDecimal;

class D02Multi {
   public int MutileNum(int n) {
      int mutli_result = 1;
      for (int i = 1; i <= n; i++) {
         mutli_result = mutli_result * i;
      }
      return mutli_result;
   }

   public long MutileNum(long n) {
      long mutli_result = 1;
      for (long i = 1; i <= n; i++) {
         mutli_result = mutli_result * i;
      }
      return mutli_result;
   }

   public BigDecimal MutileNumBig(long n) {
      BigDecimal mutli_result = new BigDecimal('1');  
      for (long i = 1; i <= n; i++) {
         BigDecimal mutli_i = new BigDecimal(String.valueOf(i));  
         mutli_result = mutli_result.multiply(mutli_i);
      }
      return mutli_result;
   }

}

public class D02T03 {

   public static void main(String[] args) throws IOException {
      // System.out.println(Long.MIN_VALUE - 1);
      // System.out.println(Integer.MIN_VALUE - 1);

      D02Multi multi = new D02Multi();

      BigDecimal long_start = new BigDecimal("1");
      BigDecimal LongLength = new BigDecimal(String.valueOf(Long.MIN_VALUE - 1));
      boolean t_long = true;
      long long_n=1;
      while (t_long) {
         BigDecimal multi_long = new BigDecimal(String.valueOf(multi.MutileNumBig(long_n)));
         if (long_start.add(multi_long).compareTo(LongLength)<=0) {
            long_start = long_start.add(multi_long);
            long_n = long_n + 1;
         } else {
            t_long = false;
         }
      }
      System.out.println("相加的值是：" + long_start);
      System.out.println("long 的 n 边界值为：" + long_n);


      BigDecimal int_start = new BigDecimal("1");
      BigDecimal IntegerLength = new BigDecimal(String.valueOf(Integer.MIN_VALUE - 1));
      boolean t_int = true;
      long int_n=1;
      while (t_int) {
         BigDecimal multi_int = new BigDecimal(String.valueOf(multi.MutileNumBig(int_n)));
         if (int_start.add(multi_int).compareTo(IntegerLength)<=0) {
            int_start = int_start.add(multi_int);
            int_n = int_n + 1;
         } else {
            t_int = false;
         }
      }

      System.out.println("相加的值是：" + int_start);
      System.out.println("int 的 n 边界值为：" + int_n);

   }
}