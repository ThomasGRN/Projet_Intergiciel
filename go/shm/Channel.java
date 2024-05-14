package go.shm;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    // --- Attributes ---
    private String name;
    private Semaphore inSemaphore;
    private Semaphore outSemaphore;
    private Semaphore globalSemaphore;
    List<Observer> observerOut;
    List<Observer> observerIn;
    T valeur;


    // --- Methods ---
    public Channel(String name) {
        this.name = name;
        this.inSemaphore = new Semaphore(0); 
        this.outSemaphore = new Semaphore(1);
        this.globalSemaphore = new Semaphore(1);
        observerOut = new LinkedList();
        observerIn = new LinkedList();
    }
    
    public void out(T v) {
        try {
            synchronized (observerOut) {
                if (!observerOut.isEmpty()){
                    for(Observer observer : observerOut){
                        observer.update();
                    }
                }
            }
            globalSemaphore.acquire();
            outSemaphore.acquire();

            // SC
            valeur = v;

            inSemaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public T in() {
        T retour = null;
        try {
            synchronized (observerIn) {
                if (!observerIn.isEmpty()){
                    for(Observer observer : observerIn){
                        observer.update();
                    }
                    observerIn.clear();
                }
            }
            inSemaphore.acquire();
            
            // SC
            retour = valeur;
            
            outSemaphore.release();
            inSemaphore.release();
            globalSemaphore.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    public String getName() {
        return name;
    }

    public void observe(Direction dir, Observer observer) {
        switch (dir) {
            case Out:
                observerOut.add(observer);
                break;
        
            case In:
                observerIn.add(observer);
                break;
        }
    }
        
}
