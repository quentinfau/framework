package RMI.Server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		private Hashtable<String, String> Histo = new Hashtable<String,String>();
		
		
		//private Enumeration<Client> OnlineUsers;
	//protected Hashtable<String, Client> users;
	public ChatImpl() throws RemoteException {
		users = new Hashtable<String,Client>();
		Histo = new Hashtable<String,String>();
	}

	
	/*Ajouter user*/
	public void addUser(String nom,Client client){
		users.put(nom,client);
		
	}
	
	public void removeUser(String nom){
		users.remove(nom);
	}
	
	
	public synchronized void send(String nom, String msg) {
		Set<String> set = users.keySet();
		Iterator<String> itr = set.iterator();
		  while(itr.hasNext()){
		 //for (int i = 0; i < users.size(); i++) {
		 //nomOnline = itr.next();
			 //System.out.println(users.get(i).getName());
		    Client client = users.get(itr.next());
			client.receive(nom,msg);
			Histo.put(nom, msg);
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


	public Hashtable<String, String> getMsg() {
		return Histo;
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
