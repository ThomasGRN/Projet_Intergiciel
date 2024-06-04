package go.cs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import go.Direction;
import go.Observer;

public class RemoteChannelImpl<T> extends UnicastRemoteObject implements RemoteChannel<T> {

      // --- Attributes ---
    private final String name;
    private final Semaphore inSemaphore;
    private final Semaphore outSemaphore;
    private T value;
    private final Object lock = new Object();



    public RemoteChannelImpl(String name) throws RemoteException {
        this.name = name;
        inSemaphore = new Semaphore(0);
        outSemaphore = new Semaphore(0);
    }



    @Override
    public void out(T v) throws RemoteException {
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
    public T in() throws RemoteException {
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
    public String getName() throws RemoteException {
        return name;
    }



    @Override
    public void observe(Direction direction, Observer observer) throws RemoteException {
        // TODO
    }
    
}
