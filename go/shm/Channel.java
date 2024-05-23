package go.shm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import go.Direction;
import go.Observer;

public class Channel<T> implements go.Channel<T> {

    // --- Attributes ---
    private final String name;
    private final Semaphore inSemaphore;
    private final Semaphore outSemaphore;
    private final Map<Direction, ArrayList<Observer>> observers;
    private T value;
    private final Object lock = new Object();

    // --- Methods ---
    public Channel(String name) {
        this.name = name;
        this.inSemaphore = new Semaphore(0); 
        this.outSemaphore = new Semaphore(0);
        this.observers = new HashMap<>();
        this.observers.put(Direction.In, new ArrayList<>());
        this.observers.put(Direction.Out, new ArrayList<>());
    }

    @Override
    public void out(T v) {
        try {
            synchronized (observers.get(Direction.Out)) {
                ArrayList<Observer> observersOut = observers.get(Direction.Out);
                if (!observersOut.isEmpty()) {
                    for (Observer observer : observersOut) {
                        observer.update();
                    }
                    observersOut.clear();
                }
            }

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
        try {
            synchronized (observers.get(Direction.In)) {
                ArrayList<Observer> observersIn = observers.get(Direction.In);
                if (!observersIn.isEmpty()) {
                    for (Observer observer : observersIn) {
                        observer.update();
                    }
                    observersIn.clear();
                }
            }
            outSemaphore.release(); // Signal that the value has been consumed
                
            inSemaphore.acquire(); // Wait for a value to be available

            synchronized (lock) {
                retour = value;
                value = null;
                lock.notifyAll(); // Notify any waiting out() methods
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retour;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void observe(Direction dir, Observer observer) {
        synchronized (observers.get(dir)) {
            observers.get(dir).add(observer);
        }
    }

}
