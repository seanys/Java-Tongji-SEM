package sample.util;

public class TimeTread extends Thread {
    private Thread t;
    private String threadName;

    public TimeTread(String name) {
        threadName = name;
    }

    public void run() {
        try {
            Thread.sleep(60000);
            System.out.println("今日已经学习了1min");
            Thread.sleep(60000);
            System.out.println("今日已经学习了2min");
            Thread.sleep(60000);
            System.out.println("今日已经学习了3min");
            Thread.sleep(60000);
            System.out.println("今日已经学习了4min");
            Thread.sleep(60000);
            System.out.println("今日已经学习了5min");
        }catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}