package go.cs;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl<T> extends UnicastRemoteObject implements RemoteServer<T>{
    
    private static Registry registry;
    
    
    public ServerImpl()  throws RemoteException  {
        try {
            registry = LocateRegistry.createRegistry(1099);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }

    @Override
    public void addChannel(Channel<T> c) throws RemoteException {
        try {
            registry.rebind(c.getName(), c);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public Channel<T> getChannel(String name) throws RemoteException {
        Channel<T> c = null;
        try {
            c = (Channel<T>) registry.lookup(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error retrieving channel", e);
        }

        return c;
    }
    

    public static void main(String args[]) {
        try {
            RemoteServer<Object> server = new ServerImpl<>();

            registry.rebind("server", server);

            System.out.println("Serveur READY");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
