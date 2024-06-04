package go.test;

import go.Channel;
import go.Factory;

public class TestShmSelectChannel {

    private static void quit(String msg) {
        System.out.println("TestShmSelectChannel: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Channel<Integer>> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");

        new Thread(() -> {
                try { Thread.sleep(3000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                c1.out(c2);
                int v = c2.in();
                quit(v == 4 ? "ok" : "KO");
        }).start();

        new Thread(() -> {
                Channel<Integer> c = c1.in();
                c.out(4);
        }).start();
        
    }
    
}
