package RMI.Client;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.LinkedList;



public class Client implements ClientInterface, Serializable {
	private String nom;
	private String serveur="130.190.29.49";
	//private ChatInterface chat;
	private Remote chat;
	
	public Client(){
		try {
			//this.nom=name;
			startTextChatSession();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* RECEIVE */
	public synchronized void  receive(String sender, String msg) {
		System.out.println(sender+" : "+msg);			
	}
	
	
	
	
	public void startTextChatSession() throws RemoteException, NotBoundException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			//System.setProperty("java.rmi.server.hostname","192.168.1.2");
			//System.out.println("Enter the ip server");
			//serveur = in.readLine();
			/*System.out.println("Enter your name");
			nom = in.readLine();*/
			
            Registry registry = LocateRegistry.getRegistry(serveur,1090);
            
			//chat = (ChatInterface) registry.lookup(serveur+"/Chat");
			chat = registry.lookup(serveur);
			System.out.println("USER ADD");
			
			//chat.addUser(nom,this);
			
			
			/*System.out.println("You can now send message.\nHit q or Q to exit");
			String toSend = in.readLine();
			
			while (toSend.length() != 1 && (toSend.charAt(0) != 'q' || toSend.charAt(0) != 'Q'))
			{
				chat.send(nom, toSend);
				//System.out.println("Sent !!");
				toSend = in.readLine();
			}
			
			chat.removeUser(nom);
			System.exit(0);*/

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void sendmessage(String msg){
		try {
			chat.send(nom, msg);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void setName(String name) {
		// TODO Auto-generated method stub
		this.nom=name;
	}


	public void addUser(String name) {
		// TODO Auto-generated method stub
		try {
			chat.addUser(nom,this);
			System.out.println("user added");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return this.nom;
	}
	
	/*
	 * Lister les utilisateurs connectés
	 */
	public LinkedList getOnlineUsers(){
		LinkedList onlineuser = null;
		try {
			onlineuser = chat.listUsers();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return onlineuser;
	}
	
	
}
