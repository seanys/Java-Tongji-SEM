
import java.util.*;
import java.io.*;
import static java.lang.Math.*;
import java.math.BigDecimal;

class MultiD01 {
    // 除法运算默认精度

    public double MutileNum(double n) {
        double mutli_result = 1;
        for (double i = 1; i <= n; i++) {
            mutli_result = mutli_result * i;
        }
        return mutli_result;
    }
}

public class D02T02 {

    public static void main(String[] args) {
        System.out.println("请输入n:");
        // 导入数据值
        Scanner scan = new Scanner(System.in);
        String n_text = scan.next();
        double n = Double.parseDouble(n_text);

        // 计算乘法的值
        MultiD01 multi = new MultiD01();

        // 设置精度初始值
        BigDecimal start = new BigDecimal("0");

        for (double i = 1; i <= n; i++) {
            double multi_num = multi.MutileNum(i);

            BigDecimal one = new BigDecimal("1");
            BigDecimal num_sub = new BigDecimal(String.valueOf(multi_num));

            BigDecimal sub_result = one.divide(num_sub, 80, BigDecimal.ROUND_HALF_UP);

            System.out.println("1/"+i+"!输出值为:"+sub_result);

            start=start.add(sub_result);
        }

        System.out.println(start);

        scan.close();
    }
}