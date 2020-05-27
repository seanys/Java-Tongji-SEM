import java.util.*;
import java.io.*;
import static java.lang.Math.*;

class Judge{
    public boolean PrimeJudge(int a){
        if(a==1){
            return false;
        }
        boolean t=true;
        for(int i=2;i<a;i++){
            if(a%i==0){
                t=false;
            }
        }
        return t;
    }
    public boolean NarcissusJudge(int a){
        int firstNum=a/100;
        int secondNum=(a/10)%10;
        int thirdNum=a%10;
        if(Math.pow(firstNum,3)+Math.pow(secondNum,3)+Math.pow(thirdNum,3)==a){
            return true;
        }
        return false;
    }
}

public class D01T02 {
    public static void main(String []args){
        Scanner scan = new Scanner(System.in);
        int a=scan.nextInt();
        Judge judge=new Judge();
        boolean res=true;
        if(a>=100&&a<=999){
            res=judge.NarcissusJudge(a);
            System.out.print("水仙花数判断：");
            System.out.println(res);
        }else{
            res=judge.PrimeJudge(a);
            System.out.print("质数判断：");
            System.out.println(res);
        }
        scan.close();
    }
}