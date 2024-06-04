package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteServer<T> extends Remote {
    void addChannel(Channel<T> c) throws RemoteException;
    Channel<T> getChannel(String name) throws RemoteException;
}
