package go.cs;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {
    
    private static Registry registry;
    private static ChannelMap<?> channelMap;
    

    public static void main(String args[]) {
        try {
            registry = LocateRegistry.createRegistry(1099);
            channelMap = new ChannelMap<>();

            registry.rebind("channelMap", channelMap);

            System.out.println("Serveur READY");

        } catch (RemoteException remoteExc) {
            System.out.println("Impossible de créer le registre");
            remoteExc.printStackTrace();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
