RMI : Technologie permettant à un programme java d'invoquer des méthodes sur des objets distants.

Interfaces distantes :
 - RemoteChannel.java
 - RemoteSelector.java

Classes d'implémentation de ces interfaces :
 - Channel.java
 - Selector.java


UTILISATION : 

Test.java :
Channel<Integer> c = factory.newChannel("c");


Factory.java : 
TODO --> Créé l'objet Channel et on l'ajoute au ChannelMap. C'est cette classe qui servira de "client" en contenant le registre


ChannelMap.java :
TODO --> Classe qui contiendra les ajouts de channels à la Map et la recherche de ceux-ci


ServeurImpl.java :
TODO --> 



