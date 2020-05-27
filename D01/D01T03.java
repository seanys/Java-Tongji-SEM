import java.io.*;
public class D01T03{
 
   public static void main(String[] args) throws IOException{
      /* 使用构造器创建两个对象 */
      Judge judge=new Judge();
      int t=0;
      File f = new File("prime.txt");
      FileOutputStream fop = new FileOutputStream(f);
      OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
      for(int i=2;i<1000;i++){
         boolean judge_result=judge.PrimeJudge(i);
         if(judge_result){
            t++;
            writer.append(i+"\t"); 
            if(t==9){
               t=0;
               writer.append("\n");
            }
         }
      }
      writer.close();
      fop.close();
   }
}