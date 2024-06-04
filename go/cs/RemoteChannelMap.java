package go.cs;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteChannelMap<T> extends Remote {
    Channel<T> getChannel(String name) throws RemoteException;
}
