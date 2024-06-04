package go.cs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChannelMap <T>  extends UnicastRemoteObject {
    
    // ATTRIBUTES
    private Map<String, Channel<T>> channelMap;

    public ChannelMap() throws RemoteException {
        channelMap = new HashMap<>();
    }


    private Channel<T> addChannel(Channel<T> c) {
        channelMap.put(c.getName(), c);
        return c;
    }


    public Channel<T> getChannel(String name) {
        Channel<T> c;
        c = channelMap.get(name);

        if(c == null) {
            c = addChannel(c);
        }

        return c;
    }
}
