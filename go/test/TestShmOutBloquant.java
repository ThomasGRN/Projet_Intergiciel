package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, commençant par in */
public class TestShmOutBloquant {

    private static void quit(String msg) {
        System.out.println("TestShmOutBloquant: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(2000);  } catch (InterruptedException e) { }
                quit("ok");
        }).start();
        
        new Thread(() -> {
                c.out(4);
                c.in();
                quit("KO (deadlock)");
        }).start();

                   
    }
}
