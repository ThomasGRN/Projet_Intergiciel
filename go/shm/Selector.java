package go.shm;

import go.Direction;
import go.Observer;
import go.Channel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

public class Selector implements go.Selector {

    private Map<Channel, Direction> channels;
    private List<Channel> channelReady;
    private Semaphore semaphore;

    public Selector(Map<Channel, Direction> channels) {
        this.channels = channels;
        this.channelReady = new LinkedList();
        this.semaphore = new Semaphore(0);

        for (Entry<Channel, Direction> entry : channels.entrySet()){
            entry.getKey().observe(entry.getValue(), new Observer(){
                @Override
                public void update(){
                    channelReady.add(entry.getKey());
                    semaphore.release();
                }
            });
        }
    }

    public Channel select() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Channel retour = channelReady.get(0);
        Direction directionRetour = channels.get(retour);
        channelReady.remove(0);
        retour.observe(Direction.inverse(directionRetour), new Observer(){
            @Override
            public void update(){
                channelReady.add(retour);
                semaphore.release();
            }
        });
        return retour;
    }

}
