package go.test;

import go.Channel;
import go.Factory;

public class TestShm3out1in {

    private static void quit(String msg) {
        System.out.println("TestShm3out1in: " + msg);
        System.exit(msg.equals("ok") ? 0 : 1);
    }

    public static void main(String[] a) {
        Factory factory = new go.shm.Factory();
        Channel<Integer> c = factory.newChannel("c");

        new Thread(() -> {
                try { Thread.sleep(3000);  } catch (InterruptedException e) { }
                quit("KO (deadlock)");
        }).start();
        
        new Thread(() -> {
                try { Thread.sleep(100);  } catch (InterruptedException e) { }
                c.out(1);
        }).start();

        new Thread(() -> {
                try { Thread.sleep(200);  } catch (InterruptedException e) { }
                c.out(2);
        }).start();

        new Thread(() -> {
                try { Thread.sleep(300);  } catch (InterruptedException e) { }
                c.out(3);
        }).start();

        new Thread(() -> {
                try { Thread.sleep(150);  } catch (InterruptedException e) { }
                int v1 = c.in();
                if (v1 != 1) quit("KO");
                int v2 = c.in();
                if (v2 != 2) quit("KO");
                int v3 = c.in();
                quit(v3 == 3 ? "ok" : "KO");
        }).start();
    }
}
