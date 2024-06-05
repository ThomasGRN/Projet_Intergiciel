package go.cs;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Implantation d'un serveur hébergeant des canaux.
 *
 */
public class ServerImpl {
    
    private static RemoteChannelMap<?> channelMap;
    

    public static void main(String args[]) {
        try {
            LocateRegistry.createRegistry(1099);
            channelMap = new ChannelMap<>();

            Naming.rebind("rmi://localhost:1099/channelMap", channelMap);

            System.out.println("Serveur READY");

        } catch (RemoteException remoteExc) {
            System.out.println("Impossible de créer le registre");
            remoteExc.printStackTrace();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
