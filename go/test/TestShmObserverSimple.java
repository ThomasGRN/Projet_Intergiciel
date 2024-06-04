package go.test;

import go.Channel;
import go.Factory;
import go.Observer;

public class TestShmObserverSimple {

    private static void quit(String msg) {
        System.out.println("TestShmObserverSimple: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            quit("KO (deadlock)");
        }).start();

        c.observe(go.Direction.In, new Observer() {
            @Override
            public void update() {
                quit("ok");
            }
        });

        c.in(); // Doit déclencher l'observer

        quit("KO");
    }
}
