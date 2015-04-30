package appli;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FilenameFilter;

import Socket.*;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class BoxInter extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private static String server;
	private static String user;
	private Client c;
	private static String  nomFich;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BoxInter frame = new BoxInter();
					//frame.setServer(getServer());
					//frame.setUser(getUser());
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
	public BoxInter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 449);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		
		textField = new JTextField();
		textField.setColumns(10);
		TextArea textArea_1 = new TextArea();
		JLabel nomFichier = new JLabel();
		
		JButton btnParcourir = new JButton("Parcourir");
		btnParcourir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getFileName();
				textField.setText(nomFich);			}
		});
		
		
		
		JButton btnNewButton = new JButton("Envoyer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				nomFichier.setName(nomFich);
				//textArea_1.append(nomFichier.getText());
				//ListFichier.add(nomFichier);
				//getContentPane().add(ListFichier);
				
			}
		});
		
		
		
		JButton btnDconnexion = new JButton("D\u00E9connexion");
		btnDconnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//c.disconnect();
				
			}
		});
		
		JButton btnActualiser = new JButton("Actualiser");
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnParcourir)
								.addComponent(btnNewButton)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnActualiser)
							.addPreferredGap(ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
							.addComponent(btnDconnexion)))
					.addGap(167))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(176, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDconnexion)
						.addComponent(btnActualiser))
					.addGap(40)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnParcourir)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/*public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public static String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public static String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}

	*/
	public static void getFileName(){
		
		    JFrame f=new JFrame();
		    FileDialog fd=new FileDialog(f,"Selectionner le fichier à inserer",FileDialog.LOAD);
		    fd.setFile("*.*");
		    /*fd.setFilenameFilter(new FilenameFilter(){
		      @Override public boolean accept(      File dir,      String name){
		        return (name.endsWith(".apk"));
		      }

			
		    }
		);*/
		    fd.setVisible(true);
		    String apkDirectory=fd.getDirectory();
		    String apkFile=fd.getFile();
		    f.dispose();
		    //return (apkDirectory != null && apkFile != null) ? apkDirectory + System.getProperty("file.separator") + apkFile : null;
		    nomFich = apkDirectory + System.getProperty("file.separator") + apkFile;
		 
		}
}
