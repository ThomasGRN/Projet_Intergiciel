package go.cs;

import go.Direction;
import go.Observer;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

public class Channel<T> implements go.Channel<T> {

     // --- Attributes ---
    private final String name;
    private final Semaphore inSemaphore;
    private final Semaphore outSemaphore;
    private T value;
    private final Object lock = new Object();

    public Channel(String name) throws RemoteException {
        this.name = name;
        inSemaphore = new Semaphore(0);
        outSemaphore = new Semaphore(0);
    }



    @Override
    public void out(T v) {
        try {
            outSemaphore.acquire(); // Wait for the consumer to be ready
    
            synchronized (lock) {
                value = v;
                inSemaphore.release(); // Signal that a value is available
                lock.notifyAll(); // Notify any waiting in() methods
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    


    @Override
    public T in() {
        T retour = null;

        try{
            outSemaphore.release(); // Signal that the value has been consumed
                
            inSemaphore.acquire(); // Wait for a value to be available

            synchronized (lock) {
                retour = value;
                value = null;
                lock.notifyAll(); // Notify any waiting out() methods
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        return retour;
    }



    @Override
    public String getName() {
        return name;
    }



    @Override
    public void observe(Direction direction, Observer observer) {
        // TODO
    }
}
