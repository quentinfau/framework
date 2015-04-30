package RMI.Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import RMI.Client.Client;


public class ChatImpl  extends UnicastRemoteObject implements ChatInterface,Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 606137891279137844L;
		private Hashtable<String, Client> users;
		//private Enumeration<Client> OnlineUsers;
	
	public ChatImpl() throws RemoteException {
		users = new Hashtable<String, Client>();
	}

	
	/*Ajouter user*/
	public void addUser(String nom,Client client){
		users.put(nom,client);
		
	}
	
	public void removeUser(String nom){
		users.remove(nom);
	}
	
	
	public synchronized void send(String nom, String msg) {
		
		 //OnlineUsers=users.elements();
		//System.out.println(users.elements().toString());
		//while(OnlineUsers.hasMoreElements()){
		Set<String> set=users.keySet();
		Iterator<String> itr = set.iterator();
		 String nomOnline;
		 while(itr.hasNext()){
			 nomOnline = itr.next();
		 Client client = get(nomOnline);
			client.receive(nom,msg);
			enregistrerEchange(nom,msg);
		}
	}
	
	public void enregistrerEchange(String nom, String data) {
		
		  SimpleDateFormat formater = null;
		  Date aujourdhui = new Date();

		  formater = new SimpleDateFormat("dd-MM-yy");
		  //System.out.println(formater.format(aujourdhui));
		 String FileName = "History_"+formater.format(aujourdhui)+".txt";
		  
		  File FileData = new File(FileName);
		  try {
			  if (!FileData.exists()) {
				  FileData.createNewFile();
			  } 
			  
			  FileWriter MyHistory= new FileWriter(FileData,true);
			  MyHistory.write(nom + " : " + data + "\n");
			  MyHistory.close();
			
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	}
	
	
	public LinkedList listUsers() {
		LinkedList list = new LinkedList();
		
		Set<String> set = users.keySet();
		Iterator<String> itr = set.iterator();
		while (itr.hasNext()) {
			list.add(itr.next());
		}
		/*OnlineUsers=users.elements();
		System.out.println(users.);*/
		return list;
	}

	public Client get(String nom){
		return users.get(nom);
}
	
}
