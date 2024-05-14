package go.shm;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    // --- Attributes ---
    String name;
    final Semaphore inMutex;
    final Semaphore outMutex;
    final Semaphore globMutex;
    T val;


    // --- Methods ---
    public Channel(String name) {
        this.name = name;
        this.inMutex = new Semaphore(0); 
        this.outMutex = new Semaphore(1);
        this.globMutex = new Semaphore(1);
        this.val = null;
    }
    
    public void out(T v) {
        try {
            globMutex.acquire();
            outMutex.acquire();

            // SC
            val = v;


            inMutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public T in() {
        try {
            inMutex.acquire();
            
            // SC
            T retour = val;
            
            outMutex.release();
            inMutex.release();
            globMutex.release();
            return retour;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        // TODO
    }
        
}
