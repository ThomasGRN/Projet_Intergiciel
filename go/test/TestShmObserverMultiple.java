package go.test;

import go.Channel;
import go.Factory;
import go.Observer;

public class TestShmObserverMultiple {

    private static boolean observer1Triggered = false; 
    private static boolean observer2Triggered = false; 

    private static void quit(String msg) {
        System.out.println("TestShmObserverMultiple: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] args) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
            try { Thread.sleep(2000);  } catch (InterruptedException e) { }
            if (observer1Triggered & observer2Triggered){
                quit("ok");
            }else{
                quit("KO (deadlock)");
            }
        }).start();

        // Premier observateur
        c.observe(go.Direction.In, new Observer() {
            @Override
            public void update() {
                observer1Triggered = true; // Mettre la variable à true lorsque le premier observateur est déclenché
            }
        });

        // Deuxième observateur
        c.observe(go.Direction.In, new Observer() {
            @Override
            public void update() {
                observer2Triggered = true;
            }
        });

        c.in();
    }
}
