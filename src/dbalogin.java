import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

public class dbalogin extends JFrame {
	clientfinal conn;
	JFrame f1, f2, f3;
	JTextField fusername = new JTextField(15);
	JTextField fpassword = new JTextField(15);
	JTextField fitemname = new JTextField(15);
	JTextField fitemprice = new JTextField(15);
	JButton loginbutton = new JButton("Login");

	private Socket connection;
	JTextArea txtDisplayResults = new JTextArea(20, 40);

	ObjectOutputStream toServer ;
	ObjectInputStream fromServer;
	private JButton closeButton;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	String[] viewlist;
	Message message;
	private final int SHOW_MENU = 2;
	private final int ADD_ITEM = 3;
	private final int UPDATE_ITEM =4;
	private final int DELETE_ITEM =5;

	ItemList item;



	//public JTextArea txtDisplayResults;

	public dbalogin(clientfinal conn) throws IOException {
		// System.out.println("in dba constructor");
		this.conn = conn;
		// JOptionPane.showMessageDialog(null, "in dba class");
		
		connect("localhost", 8000);
		viewgui();

	}

	public void connect(String hostAddress, int connectingPort) throws IOException {

		try {
			// Create a socket to connect to the server
			connection = new Socket(hostAddress, connectingPort);
			// Socket socket = new Socket("130.254.204.36", 8000);
			// Socket socket = new Socket("drake.Armstrong.edu", 8000);

			// Create an input stream to receive data from the server

			toServer = new ObjectOutputStream(connection.getOutputStream());
			fromServer = new ObjectInputStream(connection.getInputStream());

		} catch (IOException ex) {
			// jta.append(ex.toString() + '\n'+"in connect");
			System.out.println("error in connecting dba");
		}
	}

	public void sendMessage(String username, String password, String firstname, String lastname, String address,
			int op_type,String itemname,String itemprice) throws IOException, ClassNotFoundException {
		System.out.println(username);

		message = new Message(username, password, firstname, lastname, address, op_type,itemname,itemprice);

		toServer.writeObject(message);
		return;

	}
	private void printafteradd() throws BadLocationException {
		// TODO Auto-generated method stub
		ArrayList<ItemList> nameprice = new ArrayList<ItemList>();

		/*try {
			sendMessage("", "", "", "", "", SHOW_MENU,"","");
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		System.out.println("size of vielist" );

		try {
			int i = 0;
				nameprice = (ArrayList<ItemList>) fromServer.readObject();
				// nameprice.add(item);
				// System.out.println("in client price is :
				// "+item.itemname+item.itemprice);
				//System.out.println("araylist is:" + nameprice.get(i).itemname);
				i = i + 1;
				viewlist = new String[nameprice.size()];
				
				
			
			//int Start = 0;
				txtDisplayResults.setText("");
			// listModel = new DefaultListModel();
			for (int a = 0; a < nameprice.size(); a++) {
				viewlist[a] = nameprice.get(a).itemname + " for Price: " + nameprice.get(a).itemprice;
				System.out.println("aray is:" + viewlist[a]);
				txtDisplayResults.append(viewlist[a]);
				txtDisplayResults.append("\n");
			}
			//int end = txtDisplayResults.getCaretPosition();

	
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
	}
	
	
	

	private void viewgui() {
		// TODO Auto-generated method stub
		System.out.println("inside view bda method in dbs class");

		JPanel first = new JPanel();
		first.setLayout(new FlowLayout());
		first.add(new Label("UserName: "));
		first.add(fusername);
		first.add(new Label("PassWord: "));
		first.add(fpassword);

		JPanel but = new JPanel();
		but.setLayout(new FlowLayout());
		but.add(loginbutton);

		JPanel sec = new JPanel();
		sec.setLayout(new BorderLayout());
		sec.add(new Label("Please Enter Username and password to access DBA"), BorderLayout.NORTH);
		sec.add(first, BorderLayout.CENTER);
		sec.add(but, BorderLayout.SOUTH);

		f1 = new JFrame("GUI FOR DBA LOGIN");
		// f1.setSize(400, 400);
		f1.setLocationRelativeTo(null);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setVisible(true);

		Container contentPane = f1.getContentPane();

		contentPane.add(sec);
		// contentPane.add(loginbutton);
		f1.pack();

		loginbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("inside login button");
				if (fusername.getText().equals("root") & fpassword.getText().equals("root")) {
					f1.setVisible(false);
					try {
						sendMessage("", "", "", "", "", SHOW_MENU,"","");
					} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					closeButton = new JButton("CLOSE");
					txtDisplayResults.setEditable(false);
					addButton = new JButton("ADD");
					updateButton = new JButton("UPDATE");
					deleteButton = new JButton("DELETE");

					JPanel displayPanel = new JPanel();
					JPanel buttonPanel = new JPanel();
					JPanel middle = new JPanel();
					JPanel below = new JPanel();
					JPanel fin = new JPanel();

					displayPanel.setLayout(new BorderLayout());
					displayPanel.add(new JLabel("Existing List of menu"), BorderLayout.NORTH);
					displayPanel.add(txtDisplayResults, BorderLayout.CENTER);

					buttonPanel.setLayout(new FlowLayout());
					buttonPanel.add(addButton);
					buttonPanel.add(updateButton);
					buttonPanel.add(deleteButton);
					buttonPanel.add(closeButton);

					below.setLayout(new FlowLayout());
					below.add(new JLabel("Item Name: "));
					below.add(fitemname);
					below.add(new JLabel("Item Price: "));
					below.add(fitemprice);

					JScrollPane scroll = new JScrollPane(txtDisplayResults);
					scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					displayPanel.add(scroll);

					middle.setLayout(new BorderLayout());
					middle.add(displayPanel, BorderLayout.CENTER);
					middle.add(below, BorderLayout.SOUTH);

					fin.setLayout(new BorderLayout());
					fin.add(middle, BorderLayout.CENTER);
					fin.add(buttonPanel, BorderLayout.SOUTH);
				/*	try {
						int i = 0;
						while (true) {
							nameprice = (ArrayList<ItemList>) fromServer.readObject();
							// nameprice.add(item);
							// System.out.println("in client price is :
							// "+item.itemname+item.itemprice);
							//System.out.println("araylist is:" + nameprice.get(i).itemname);
							i = i + 1;
							viewlist = new String[nameprice.size()];
							//System.out.println("size of vielist" + nameprice.size());
							break;
						}
						// listModel = new DefaultListModel();
						for (int a = 0; a < nameprice.size(); a++) {
							viewlist[a] = nameprice.get(a).itemname + " for Price: " + nameprice.get(a).itemprice;
							//System.out.println("aray is:" + viewlist[a]);
							txtDisplayResults.append(viewlist[a]);
							txtDisplayResults.append("\n");
						}
*/
					try {
						
						printafteradd();
					} catch (BadLocationException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
						f2 = new JFrame("GUI FOR DBA LOGIN");
						f2.setSize(400, 400);
						f2.setLocationRelativeTo(null);
						f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						f2.setVisible(true);

						Container contentPane = f2.getContentPane();
						contentPane.add(fin);
						f2.pack();

					//} catch (ClassNotFoundException | IOException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				
			addButton.addActionListener(new java.awt.event.ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					boolean flag3 = true;
					int price=0;
					try{
						price=Integer.parseInt(fitemprice.getText());
						flag3 = true;
					}catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Price has to be in integers");
						flag3 = false;
					}

					if(flag3){
					  try {
						//  System.out.println(fusername.getText());
						sendMessage("","","","","",ADD_ITEM,fitemname.getText(),fitemprice.getText());
						//txtDisplayResults.append("new added");
						sendMessage("", "", "", "", "", SHOW_MENU,"","");
						System.out.println("after send ms+++++++++++++++++++++++++++");
						
						printafteradd();
					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
				}

			
				
			});
			updateButton.addActionListener(new java.awt.event.ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {
					boolean flag3 = true;
					int price=0;
					try{
						price=Integer.parseInt(fitemprice.getText());
						flag3 = true;
					}catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Price has to be in integers");
						flag3 = false;
					}

					if(flag3){

					  try {
						//  System.out.println(fusername.getText());
						sendMessage("","","","","",UPDATE_ITEM,fitemname.getText(),fitemprice.getText());
						
						
						sendMessage("", "", "", "", "", SHOW_MENU,"","");
						//txtDisplayResults.append("new added");
					//	System.out.println("after send ms");
						printafteradd();
					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				}
				
			});
			deleteButton.addActionListener(new java.awt.event.ActionListener() {

				@SuppressWarnings("unchecked")
				public void actionPerformed(ActionEvent e) {


					  try {
						//  System.out.println(fusername.getText());
						sendMessage("","","","","",DELETE_ITEM,fitemname.getText(),fitemprice.getText());
						sendMessage("", "", "", "", "", SHOW_MENU,"","");
						//txtDisplayResults.append("new added");
					//	System.out.println("after send ms");
						printafteradd();
					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			
				
			});
			closeButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent e) {

					System.exit(0);
				}
			});
				}else{
					JOptionPane.showMessageDialog(null, "invalid name or passwprd");
				}
				}
		});
		
		
	}

}
