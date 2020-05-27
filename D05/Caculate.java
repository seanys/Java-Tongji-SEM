import java.io.*;

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