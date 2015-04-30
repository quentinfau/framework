package RMI.Client;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerCloneException;
import java.util.Scanner;


public class testClient {
	 public static void main(String[] args) {

	        //String host = "localhost";
	        try {
//	    	            Registry registry = LocateRegistry.getRegistry(host,Constant.RMI_PORT);
//        Hello stub = (Hello) registry.lookup(Constant.RMI_ID);
//	            String response = stub.sayHello();
//	            Scanner sc = new Scanner(System.in);
//	            System.out.println("response: " + response);
//	            while(true){
//	            	stub.setmessage(sc.next());
//	            	
//	            }
	            new Client();
	            
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	    }
	
}
