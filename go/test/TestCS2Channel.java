package go.test;

import go.Channel;
import go.Factory;

/** Un unique in/out, ici out */
public class TestCS2Channel {

    private static void quit(String msg) {
        System.out.println("TestCS2Channel: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.cs.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(5000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();

        Channel<Integer> c2 = factory.newChannel("c");

        if (c==c2){
            quit("ok");
        }else{
            quit("KO");
        }
    }
}