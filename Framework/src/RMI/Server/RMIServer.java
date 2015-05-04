package RMI.Server;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIServer {
   
//    public RMIServer() throws RemoteException, AlreadyBoundException{
//    	RemoteImplement obj = new RemoteImplement("Connected");
//    	Registry registry = LocateRegistry.createRegistry(Constant.RMI_PORT);
//        registry.bind(Constant.RMI_ID,obj);
//    }
    public static void main(String args[]) {
        
        try {
            //String serveur="127.0.0.3";
            //Hello stub = (Hello) UnicastRemoteObject.exportObject(obj1, Constant.RMI_PORT);
ChatImpl obj = new ChatImpl();
            // Bind the remote object's stub in the registry
           
           Registry registry = LocateRegistry.createRegistry(1090);
          String url = InetAddress.getLocalHost().getHostAddress() ;
   		 System.out.println("Enregistrement de l'objet avec l'url : " + url);
   		 registry.rebind(url, obj);
   		 //System.out.println("Serveur lancé");
            
           // registry.bind("Chat",obj);
            //Naming.rebind("Chat", obj);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
}
          
	