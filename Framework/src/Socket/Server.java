package Socket;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> al;
	// if I am in a GUI
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;

	private static String cheminServeur = "C:\\Users\\Quentin\\Desktop\\Serveur\\" ;
	/*
	 *  server constructor that receive the port to listen to for connection as parameter
	 *  in console
	 */

	public Server(int port) {

		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>();
	}

	public void start() throws ClassNotFoundException, InterruptedException {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				// format message saying we are waiting
				System.out.println("Server waiting for Clients on port " + port + ".");

				Socket socket = serverSocket.accept();  	// accept connection

				// if I was asked to stop
				if(!keepGoing)
					break;
				ClientThread t = new ClientThread(socket);  // make a thread of it
				
				
				al.add(t);									// save it in the ArrayList
				t.start();

			}
			// I was asked to stop
			try {
				System.out.println("serversocket close");
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				System.out.println("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
			String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			System.out.println(msg);
		}
	}		

	private void receiveFile(Socket socket, int tailleFichier, String nomFichier) throws IOException, InterruptedException{
		int bytesRead;
		int current = 0;
		byte [] fileByte  = new byte [tailleFichier+1];
		InputStream is = socket.getInputStream();


		bytesRead = is.read(fileByte,0,fileByte.length);
		current = bytesRead;

		do {	
			bytesRead = is.read(fileByte, current, (fileByte.length-current));
			if(bytesRead >= 0) current += bytesRead;
		} while(bytesRead > -1);

		if (current == tailleFichier){

			FileOutputStream fos = new FileOutputStream(cheminServeur+ nomFichier);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(fileByte, 0 , current);
			bos.flush();
			System.out.println("File " + cheminServeur + nomFichier
					+ " downloaded (" + current + " bytes read)");
			fos.close();
			bos.close();
		}
		else {
			System.out.println("Erreur réseau lors du transfert du fichier");
		}

		is.close();
	}
	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		System.out.println("remove");
		// scan the array list until we found the Id
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			// found it
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}
	
	private void sendFileToClient(Socket socket, File fichier) throws IOException{
		
		
		
		System.out.println("sendfiletoclient");
		//C:\Users\Quentin\Documents\test.jpg

		
		byte [] fileByte = new byte [(int) fichier.length()];
		FileInputStream fis = new FileInputStream(fichier);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(fileByte,0,fileByte.length);
		OutputStream os = socket.getOutputStream();
		System.out.println("Sending " + fichier.toString() + "(" + fileByte.length + " bytes)");
		os.write(fileByte,0,fileByte.length);
		os.flush();
		System.out.println("Done.");
		fis.close();
		bis.close();
		os.close();
		
	}
	private static File[] listerFichier(){
		File file = new File(cheminServeur);
    	File[] files = file.listFiles();
		return files;
	}
	

	/*
	 *  To run as a console application just open a console window and: 
	 * > java Server
	 * > java Server portNumber
	 * If the port number is not specified 1500 is used
	 */ 
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 53786;
		// create a server object and start it
		Server server = new Server(portNumber);
		/*
		File[] fichierServeur = listerFichier();
		for (int i=0 ; i < fichierServeur.length ; i++){				//Affichage liste fichier
			System.out.println(fichierServeur[i].getName());
		}*/
		
		server.start();
		
	}

	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the login of the Client
		String login, password;
		// the only type of message a will receive
		ChatMessage cm;
		// the date I connect
		String date;

		// Constructore
		ClientThread(Socket socket) throws ClassNotFoundException, InterruptedException {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				login = (String) sInput.readObject();
				password = (String) sInput.readObject();
				
				if (!authentification(login, password)){
					System.out.println("auth echoué");
					sOutput.writeObject(false);
					close();
					remove(id);
					
				}
				else{
					
					System.out.println("auth reussi");
					sOutput.writeObject(true);
					System.out.println(login + " just connected.");
					
					int choix = (int) sInput.readObject();				//Recois le choix de l'utilisateur (upload un fichier ou download un fichier)
					switch (choix) {
					case 0 :											//cas upload
						int tailleFichier = (int) sInput.readObject();
						System.out.println(tailleFichier);
						String nomFichier = (String) sInput.readObject();
						System.out.println(nomFichier);
						
						receiveFile(socket, tailleFichier, nomFichier);
						break;
					case 1 : 											//cas download
						System.out.println("download");
						sOutput.writeObject(listerFichier());
						File fichier = (File) sInput.readObject();
						
						sendFileToClient(socket, fichier);
					default:
						break;
					}
				}
				
				System.out.println("close");
				close();
				
			}
			catch (IOException e) {
				System.out.println("Exception creating new Input/output Streams: " + e);
				return;
			}
			date = new Date().toString() + "\n";
		}

	

		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}
		
		private boolean authentification(String login, String password){
			
			Utilisateurs user = new Utilisateurs();
			if (user.authentification(login, password)){
				return true;
			}
			else{
				return false;
			}				
		}
		
		private boolean writeMsg(String msg) {
			System.out.println("writemsg");
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				System.out.println("Error sending message to " + login);
				System.out.println(e.toString());
			}
			return true;
		}
		
	}
}


