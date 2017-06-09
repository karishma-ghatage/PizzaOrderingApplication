

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
import javax.swing.JComboBox;
import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTextArea;
	import javax.swing.JTextField;
	import javax.swing.ScrollPaneConstants;
	import javax.swing.text.BadLocationException;

	public class ChefLogin extends JFrame {
		clientfinal conn;
		JFrame f1, f2, f3;
		JTextField fusername = new JTextField(15);
		JTextField fpassword = new JTextField(15);
		JTextField fitemname = new JTextField(15);
		JTextField fitemprice = new JTextField(15);
		JButton loginbutton = new JButton("Login");
		private JComboBox c = new JComboBox();


		private Socket connection;
		JTextArea txtDisplayResults = new JTextArea(20, 40);
		ArrayList<Integer> orderids;

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
		private final int SHOW_CHEF_ITEMS=14;
		private final int UPDATE_CHEF_STATUS = 15;


		ItemList item;



		//public JTextArea txtDisplayResults;

		public ChefLogin(clientfinal conn) throws IOException {
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
		public void sendMessage(int orderid, int op_type) throws IOException{
			message = new Message(orderid,op_type);
			toServer.writeObject(message);
			
		}
		private void printafteradd() throws BadLocationException {
			// TODO Auto-generated method stub
			ArrayList<ItemList> nameprice = new ArrayList<ItemList>();
			 orderids = new ArrayList<Integer>();
			 c.removeAllItems();
			try {
				int i = 0;
					nameprice = (ArrayList<ItemList>) fromServer.readObject();
					i = i + 1;
					viewlist = new String[nameprice.size()];
					//System.out.println("size of vielist" + nameprice.size());
					
				
				//int Start = 0;
					txtDisplayResults.setText("");
				// listModel = new DefaultListModel();
				for (int a = 0; a < nameprice.size(); a++) {
					viewlist[a] = "OrderID : "+nameprice.get(a).orderid + " , " + nameprice.get(a).quantity +" of, "+nameprice.get(a).itemname;
					System.out.println("aray is:" + viewlist[a]);
					if(!orderids.contains(nameprice.get(a).orderid)){
						orderids.add(nameprice.get(a).orderid);
					}
					
					txtDisplayResults.append(viewlist[a]);
					txtDisplayResults.append("\n");
					//c.removeAllItems();
				}
				for(int x=0; x<orderids.size(); x++){
					c.addItem(orderids.get(x));
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
			sec.add(new Label("Please Enter Username and password to access Chef"), BorderLayout.NORTH);
			sec.add(first, BorderLayout.CENTER);
			sec.add(but, BorderLayout.SOUTH);

			f1 = new JFrame("GUI FOR CHEF LOGIN");
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
					if (fusername.getText().equals("chef") & fpassword.getText().equals("chef")) {
						f1.setVisible(false);
						try {
							sendMessage("", "", "", "", "", SHOW_CHEF_ITEMS,"","");
						} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						closeButton = new JButton("CLOSE");
						txtDisplayResults.setEditable(false);
						//addButton = new JButton("ADD");
						updateButton = new JButton("UPDATE");
						//deleteButton = new JButton("DELETE");

						
						
						
						try {
							printafteradd();
						} catch (BadLocationException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						JPanel displayPanel = new JPanel();
						JPanel buttonPanel = new JPanel();
						JPanel middle = new JPanel();
						JPanel below = new JPanel();
						JPanel fin = new JPanel();

						displayPanel.setLayout(new BorderLayout());
						displayPanel.add(new JLabel(" List of Ordered Items:"), BorderLayout.NORTH);
						displayPanel.add(txtDisplayResults, BorderLayout.CENTER);

						buttonPanel.setLayout(new FlowLayout());
						buttonPanel.add(c);
						buttonPanel.add(updateButton);
						//buttonPanel.add(deleteButton);
						buttonPanel.add(closeButton);

						

						JScrollPane scroll = new JScrollPane(txtDisplayResults);
						scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						displayPanel.add(scroll);

						middle.setLayout(new BorderLayout());
						middle.add(displayPanel, BorderLayout.CENTER);
						middle.add(buttonPanel, BorderLayout.SOUTH);

						//middle.add(below, BorderLayout.SOUTH);

						//fin.setLayout(new BorderLayout());
						//fin.add(middle, BorderLayout.CENTER);
					
	
						f2 = new JFrame("GUI FOR DBA LOGIN");
							f2.setSize(400, 400);
							f2.setLocationRelativeTo(null);
							f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							f2.setVisible(true);

							Container contentPane = f2.getContentPane();
							contentPane.add(middle);
							f2.pack();

						//} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
						//	e1.printStackTrace();
						//}
					
				
				updateButton.addActionListener(new java.awt.event.ActionListener() {

					@SuppressWarnings("unchecked")
					public void actionPerformed(ActionEvent e) {
						
						int oid = 0;
						int index = c.getSelectedIndex();
						oid = orderids.get(index);
						//orderid

						  try {
							//  System.out.println(fusername.getText());
							sendMessage(oid, UPDATE_CHEF_STATUS);
							
							
							sendMessage("", "", "", "", "", SHOW_CHEF_ITEMS,"","");
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



