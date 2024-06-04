package go.test;

import java.util.Map;

import go.*;

public class TestShmSelect2 {

    private static void quit(String msg) {
        System.out.println("TestShmSelect2: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c1 = factory.newChannel("c1");
        Channel<Integer> c2 = factory.newChannel("c2");
        Channel<Integer> c3 = factory.newChannel("c3");

        Selector s = factory.newSelector(Map.of(c1, Direction.Out,
                                                c2, Direction.In,
                                                c3, Direction.Out));

        new Thread(() -> {
                try { Thread.sleep(3000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
                int v = c1.in();
                if (v != 7) quit("KO");
                c2.out(5);
        }).start();
        
        new Thread(() -> {
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                @SuppressWarnings("unchecked")
                Channel<Integer> c = s.select();
                c.out(7);
                @SuppressWarnings("unchecked")
                Channel<Integer> cc = s.select();
                int v = cc.in();
                if (v != 5) quit("KO");
                quit("ok");
        }).start();
    }
}
