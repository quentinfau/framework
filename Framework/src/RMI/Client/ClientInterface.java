package RMI.Client;

public interface ClientInterface {
	public void receive(String sender, String msg);
	//public void sendmessage(String msg);

	public void setName(String name);

	public void addUser(String name);
	
}
