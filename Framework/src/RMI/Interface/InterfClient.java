package RMI.Interface;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import RMI.Client.Client;
import RMI.Client.ClientInterface;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.LinkedList;

public class InterfClient extends JFrame {

	private JPanel contentPane;
	private JTextField message;
	private JTextField textField_1;
	Client user = new Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfClient frame = new InterfClient();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		message = new JTextField();
		message.setColumns(10);
		
		JTextArea chat = new JTextArea();
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		
		
		
		
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField_1.getText();
				
				user.setName(name);
				user.addUser(name);
				listUsers();
				
			}
		});
		
		JButton btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msg = message.getText();
				
				user.sendmessage(msg);
				message.setText(null);;
				
			}
		});
		
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(47)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(chat, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
						.addComponent(message, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnEnvoyer)
							.addGap(30))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnConnect)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(OnlineUsers)
								.addComponent(textField_1))
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(chat, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnConnect)
							.addGap(12)
							.addComponent(OnlineUsers, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEnvoyer)
						.addComponent(message, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	JTextArea OnlineUsers = new JTextArea();
	
	
	public void listUsers(){
		LinkedList OnlineU = user.getOnlineUsers();
		System.out.println(OnlineU.getFirst());
		int taille = 0;
		Object str = "";
		while (OnlineU.size() > taille ) {
			str = OnlineU.get(taille);
			OnlineUsers.append((String) str + "\n");
			
			taille++;
			
			
		}
		//OnlineUsers.setText(OnlineU);
	}
	
	public void append(){
		
	}
}
