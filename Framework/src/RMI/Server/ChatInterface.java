package RMI.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;

import RMI.Client.Client;

public interface ChatInterface extends Remote {

    public void send(String nom, String msg) throws RemoteException;
    public void addUser(String nom,Client client) throws RemoteException;
   // public Hashtable<String, String> getMsg();
	public Hashtable<String, String> getMsg()throws RemoteException;
    
    public void removeUser(String nom) throws RemoteException;
    public LinkedList listUsers() throws RemoteException;
}