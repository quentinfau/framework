package Socket;


import java.net.*;
import java.io.*;
import java.util.*;

import Socket.ChatMessage;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;

	// if I use a GUI or not

	// the server, the port and the login
	private String server, login, password;
	private int port;

	/*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  login: the login
	 */

	/*
	 * Constructor call when used from a GUI
	 * in console mode the ClienGUI parameter is null
	 */
	Client(String server, int port, String login, String password) {
		this.server = server;
		this.port = port;
		this.login = login;
		this.password = password;
		// save if we are in GUI mode or not
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			System.out.println("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		System.out.println(msg);

		/* Creating both Data Stream */
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		//new ListenFromServer().start();
		// Send our login to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		
		if (!(authentification(login, password)))
		{
			System.out.println("Erreur lors de la connexion ,mdp ou login incorrect");
			disconnect();
			return false;
		}
		else{
			System.out.println("Authentification réussi");
			return true;
		}
		// success we inform the caller that it worked 
		//E:\Musique\Pink Floyd\Meddle\Echoes - Pink Floyd.mp3
		//E:\Musique\Pink Floyd\Meddle\One Of These Days - Pink Floyd.mp3
		//C:\Users\Quentin\Documents\test.jpg
		
	}

	private boolean authentification(String login, String password){
		
		try {
			sOutput.writeObject(login);
			sOutput.writeObject(password);
			return (boolean) (sInput.readObject());
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
		
	}
	void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}

	private void sendFileToServer(File file) {
		try {
			
			
			sOutput.writeObject(0);
			sOutput.writeObject((int) file.length());
			sOutput.writeObject(file.getName());
			

			//C:\Users\Quentin\Documents\test.jpg

			
			byte [] fileByte = new byte [(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(fileByte,0,fileByte.length);
			OutputStream os = socket.getOutputStream();
			System.out.println("Sending " + file.toString() + "(" + fileByte.length + " bytes)");
			os.write(fileByte,0,fileByte.length);
			os.flush();
			System.out.println("Done.");
			fis.close();
			bis.close();
			os.close();
			
		}
		catch(IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}
	public File[] listerFichierServeur(){
		
		File[] fichierServeur = null;
		try {
			fichierServeur = (File[]) sInput.readObject();		//On récupère la liste des fichier du serveur
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=0 ; i < fichierServeur.length ; i++){				//On affiche la liste
			System.out.println(fichierServeur[i].getName());
		}
		
		return fichierServeur;
		
	}
	
	public File choixFichier(File[] files, String nomFichier){
		for (int i =0 ; i < files.length ; i++){
			if (files[i].getName().equals(nomFichier)){
				return files[i];
			}
		}
		return null;
	}
	public void downloadFileFromServer() throws ClassNotFoundException{
		try {
			sOutput.writeObject(1);						//envoie d'un 1 au serveur pour lui dire que le client veut download
			
			File[] fichierServeur = listerFichierServeur();
			
			System.out.println("choisissez un fichier en rentrant son nom");
			Scanner scan = new Scanner(System.in);
			String nomFichier = scan.nextLine() ;
			
			File fichier = choixFichier(fichierServeur, nomFichier);		//On récupère les infos du fichier voulu
			
			System.out.println("Ou souhaitez vous l'enregistrer ?");
			String chemin = scan.nextLine();							//chemin ou on enregistre le fichier sur le client
			sOutput.writeObject(fichier);								//On envoie les infos du fichier choisi au serveur
			scan.close();
			int tailleFichier = (int) fichier.length();
			//On envoie le fichier
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

				FileOutputStream fos = new FileOutputStream(chemin+ nomFichier);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(fileByte, 0 , current);
				bos.flush();
				System.out.println("File " + chemin + nomFichier
						+ " downloaded (" + current + " bytes read)");
				fos.close();
				bos.close();
			}
			else {
				System.out.println("Erreur réseau lors du transfert du fichier");
			}

			is.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("download");
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} // not much else I can do
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} // not much else I can do
		try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} // not much else I can do
		System.out.println("disconnected");
	}
	/*
	 * To start the Client in console mode use one of the following command
	 * > java Client
	 * > java Client login
	 * > java Client login portNumber
	 * > java Client login portNumber serverAddress
	 * at the console prompt
	 * If the portNumber is not specified 1500 is used
	 * If the serverAddress is not specified "localHost" is used
	 * If the login is not specified "Anonymous" is used
	 * > java Client 
	 * is equivalent to
	 * > java Client Anonymous 1500 localhost 
	 * are eqquivalent
	 * 
	 * In console mode, if an error occurs the program simply stops
	 * when a GUI id used, the GUI is informed of the disconnection
	 */
	public static void main(String[] args) throws FileNotFoundException, UnknownHostException, ClassNotFoundException {
		// default values
		int portNumber = 53786;
		String serverAddress = InetAddress.getLocalHost().getHostAddress(); 
		System.out.println(serverAddress);
		String login = "login1";
		String password = "password1";
		// create the Client object
		Client client = new Client(serverAddress, portNumber, login, password);
		// test if we can start the connection to the Server
		// if it failed nothing we can do
		if(!client.start())
			return;

		// wait for messages from user
		Scanner scan = new Scanner(System.in);
		// loop forever for message from the user

		System.out.print("> ");
		// read message from user
		
		System.out.println("Voulez vous upload ou download un fichier ? (u/d)");
		if (scan.nextLine().equals("u")){	//upload
			
			System.out.println("Indiquez le chemin du fichier a upload");
			File file;
			file = new File(scan.nextLine());
			
			
			client.sendFileToServer(file);
		}
		else if (scan.nextLine().equals("d")){		//download
			System.out.println("download");
			client.downloadFileFromServer();
		}
		else
		{
			//traitement par defaut
			System.out.println("rien");
		}
		
		scan.close();
		System.out.println("disconnected");
		client.disconnect();
		
		
		// done disconnect

	}

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a GUI or simply System.out.println() it in console mode
	 */
	class ListenFromServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
					// if console mode print the message and add back the prompt

					System.out.println(msg);
					System.out.print("> ");
				}
				catch(IOException e) {
					System.out.println("Server has close the connection: " + e);
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

