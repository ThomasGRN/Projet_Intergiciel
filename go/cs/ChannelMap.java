package go.cs;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ChannelMap <T> extends UnicastRemoteObject implements RemoteChannelMap<T> {
    
    // ATTRIBUTES
    private Map<String, Channel<T>> channelMap;

    public ChannelMap() throws RemoteException {
        channelMap = new HashMap<>();
    }


    private Channel<T> addChannel(String name) throws RemoteException {
        Channel<T> c = new Channel<>(name);
        channelMap.put(name, c);
   
        return c;
    }

    
    @Override
    public Channel<T> getChannel(String name) throws RemoteException {
        System.out.println("+++++++++ getChannel appelé pour : " + name);
        Channel<T> c = channelMap.get(name);
        if(c == null) {
            c = addChannel(name);
        }
        System.out.println("--------------" + c.getClass());
        return c;
    }
}
