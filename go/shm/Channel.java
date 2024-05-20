package go.shm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    // --- Attributes ---
    private String name;
    private Semaphore inSemaphore;
    private Semaphore outSemaphore;
    private Semaphore globalSemaphore;
    Map<Direction, ArrayList<Observer>> observers;
    T value;


    // --- Methods ---
    public Channel(String name) {
        this.name = name;
        inSemaphore = new Semaphore(0); 
        outSemaphore = new Semaphore(1);
        globalSemaphore = new Semaphore(1);
        observers = new HashMap<>();
        observers.put(Direction.In, new ArrayList<>());
        observers.put(Direction.Out, new ArrayList<>());
    }
    


    public void out(T v) {
        try {
            synchronized (observers.get(Direction.Out)) {
                // SC
                ArrayList<Observer> observersOut = observers.get(Direction.Out);

                if (!observersOut.isEmpty()){
                    for(Observer observer : observersOut){
                        observer.update();
                    }
                    observersOut.clear();
                }
                // FIN SC
            }

            globalSemaphore.acquire();
            outSemaphore.acquire();

            // SC
            value = v;
            // FIN SC

            inSemaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


    public T in() {
        T retour = null;
        try {
            synchronized (observers.get(Direction.In)) {
                // SC
                ArrayList<Observer> observersIn = observers.get(Direction.In);

                if (!observersIn.isEmpty()){
                    for(Observer observer : observersIn){
                        observer.update();
                    }
                    observersIn.clear();
                }
                // FIN SC
            }
            inSemaphore.acquire();
            
            // SC
            retour = value;
            // FIN SC

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
        observers.get(dir).add(observer);
    }
        
}
