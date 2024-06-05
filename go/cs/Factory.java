package go.cs;

import go.Direction;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Factory implements go.Factory {

    private static Registry registry;

    public Factory() {
        try {
            registry = LocateRegistry.getRegistry(1099);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Création ou accès à un canal existant.
     * Côté serveur, le canal est créé au premier appel avec un nom donné ;
     * les appels suivants avec le même nom donneront accès au même canal.
     */
    @Override
    public <T extends Serializable> go.Channel<T> newChannel(String name) {

        Channel<T> channel = null;

        try {
            RemoteChannelMap<T> channelMap = (RemoteChannelMap<T>) Naming.lookup("rmi://localhost:1099/channelMap");
            
            if(channelMap != null){
                System.out.println("*********** " + channelMap.getClass().getName());
                channel = channelMap.getChannel(name);
            }

        } catch (RemoteException remoteExc) {
            System.out.println("Communication avec le registre impossible");
            remoteExc.printStackTrace();
        } catch (NullPointerException nullExc) {
            System.out.println("channelMap null");
            nullExc.printStackTrace();
        } catch (ClassCastException classCastExc) {
            System.out.println("Erreur de conversion de classe");
            classCastExc.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return channel;
    }
    
    /** Spécifie quels sont les canaux écoutés et la direction pour chacun. */
    @Override
    public go.Selector newSelector(Map<go.Channel, Direction> channels) {
        // TODO
        return null;
    }

    /** Spécifie quels sont les canaux écoutés et la même direction pour tous. */
    @Override
    public go.Selector newSelector(Set<go.Channel> channels, Direction direction) {
        return newSelector(channels
                           .stream() 
                           .collect(Collectors.toMap(Function.identity(), e -> direction)));
    }

}

