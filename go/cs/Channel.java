package go.cs;

import go.Direction;
import go.Observer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Channel<T> extends UnicastRemoteObject implements go.Channel<T> {

    private RemoteChannelImpl<T> channel; 

    public Channel(String name) throws RemoteException {
        try {
            channel = new RemoteChannelImpl<>(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void out(T v) {
        try {
            channel.out(v);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    


    @Override
    public T in() {
       T result = null;

        try{
            result = channel.in();
        } catch (RemoteException e){
            e.printStackTrace();
        }

        return result;
    }



    @Override
    public String getName() {
        String name = null;

        try {
            name = channel.getName();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return name;
    }



    @Override
    public void observe(Direction direction, Observer observer) {
        // TODO
    }
}
