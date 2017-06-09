
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class clientfinal extends JPanel {

	// Text field for receiving radius
	GridBagConstraints p = new GridBagConstraints();
	private JTextField jtf = new JTextField();

	// Text area to display contents
	private JTextArea jta = new JTextArea();
	private JTextArea itemtextarea = new JTextArea(10, 25);
	private JTextArea finalitemstextarea = new JTextArea(10, 25);
	private JTextArea finalitemstextarea1 = new JTextArea(10, 25);
	private JTextArea finalitemstextarea2 = new JTextArea(10, 25);


	ItemList item;
	boolean flag = true;
	boolean flag1 = true;
	ArrayList<ItemList> nameprice = new ArrayList<ItemList>();
	public ArrayList<ItemList> selecteditems;
	ArrayList<ItemList> finalitems = new ArrayList<ItemList>();
	
	
	ArrayList<ItemList> finalitems1 = new ArrayList<ItemList>();


	Validation validating;
	JList leftlist;
	JList rightlist;
	DefaultListModel listModel;
	Boolean logflag;
	public static double total5 =0;
	public static double newTotal=0;

	public static String namefororder;
	JFrame f1;
	JFrame f2,f9;
	JFrame f3, f4, f5,f6,f7,f8;
	dbalogin dba;
	ChefLogin chef;
	JTextField fusername = new JTextField(15);
	JTextField fpassword = new JTextField(15);
	JTextField ffname = new JTextField(15);
	JTextField flast = new JTextField(15);
	JTextField faddress = new JTextField(30);
	JTextField fcardnumber = new JTextField(20);
	JTextField fexpdate = new JTextField(10);
	JTextField newquantity = new JTextField(5);
	JTextField fphone = new JTextField(10);
	JTextField femail = new JTextField(20);
	
	
	
	JLabel totalpaymetlabel = new JLabel("New Payment");


	JButton newuserbutton = new JButton("New User");
	JButton loginbutton = new JButton("Returning User");
	JButton createnewaccount = new JButton("Create new Account");
	JButton logbutton = new JButton("Login");
	JButton dbaloginbutton = new JButton("DBA login");
	JButton movebutton = new JButton("Add");
	JButton paymentbutton = new JButton("Proceed to Payment");
	JButton closebutton = new JButton("Close");
	JButton viewstatusbutton = new JButton("View Order Status");
	JButton gettotalbutton = new JButton("Get Total");
	JButton statusaddbutton = new JButton("Add Item");
	JButton statusdeletebutton = new JButton("Delete Item");
	JButton deleteentire = new JButton("Delete Entire Order");
	JButton statusupdatebutton = new JButton("Update Order");
	JButton showselected = new JButton("Show All Orders Placed");
	JButton chefbutton = new JButton("Chef");




	JButton makepayment = new JButton("Make a Payment");
	JButton makenewpayment = new JButton("Make new Payment");


	private JComboBox c = new JComboBox();
	private JComboBox c1 = new JComboBox();
	private JComboBox c2 = new JComboBox();

	JTextField itemquantity = new JTextField(15);

	JTextField ftotal = new JTextField(10);

	String[] viewlist;
	String[] editablelist;

	Server server;

	private final int CREATE_ACCOUNT = 0;
	private final int LOG_IN = 1;
	private final int SHOW_MENU = 2;
	private final int ADD_ORDER = 6;
	private final int GET_TOTAL = 7;
	private final int MAKE_PAYMENT = 8;
	private final int GET_SELECTED_ITEMS =9;
	private final int GET_STATUS =10;
private final int UPDATED_ADD=11;
private final int UPDATED_DELETE=12;
private final int GET_EDITABLE_ITEMS=13;
private final int MAKE_NEW_PAYMENT = 16;
private final int DELETE_ENTIRE = 17;
private final int VIEW_ORDERID = 18;






	public volatile Message message;

	JButton placeorderbutton = new JButton("Place Order");

	// IO streams
	private Socket conn;

	ObjectOutputStream toServer;
	ObjectInputStream fromServer;
	DataInputStream fromServerdata;

	public static void main(String[] args) throws IOException {

		int port = 8000;
		clientfinal client = new clientfinal(port);

	}

	public void connect(String hostAddress, int connectingPort) throws IOException {

		try {
			// Create a socket to connect to the server
			conn = new Socket(hostAddress, connectingPort);
			// Socket socket = new Socket("130.254.204.36", 8000);
			// Socket socket = new Socket("drake.Armstrong.edu", 8000);

			// Create an input stream to receive data from the server

			toServer = new ObjectOutputStream(conn.getOutputStream());
			fromServer = new ObjectInputStream(conn.getInputStream());
			fromServerdata = new DataInputStream(conn.getInputStream());

		} catch (IOException ex) {
			jta.append(ex.toString() + '\n' + "in connect");
		}
	}

	public clientfinal(int port) throws IOException {
		// Panel p to hold the label and text field
		// connect("localhost", port);

		viewGui();
		connect("localhost", port);

	}

	public void viewGui() throws NumberFormatException, IOException {
		JPanel first = new JPanel();
		first.setLayout(new FlowLayout());
		first.add(newuserbutton);
		first.add(loginbutton);
		first.add(dbaloginbutton);
		first.add(chefbutton);

		f1 = new JFrame("GUI FOR LOGIN");
		f1.setSize(400, 400);
		f1.setLocationRelativeTo(null);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setVisible(true);

		Container contentPane = f1.getContentPane();
		contentPane.add(first);
		f1.pack();

		// newuserbutton.addActionListener(new ButtonListener()); // Register
		// listener

		placeorderbutton.addActionListener(new java.awt.event.ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				selecteditems = new ArrayList<ItemList>();

				try {
					sendMessage("", "", "", "", "", SHOW_MENU, "", "");
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					int i = 0;
					while (true) {
						nameprice = (ArrayList<ItemList>) fromServer.readObject();
						i = i + 1;
						viewlist = new String[nameprice.size()];
						// System.out.println("size of
						// vielist"+nameprice.size());
						break;
					}
					// listModel = new DefaultListModel();
					for (int a = 0; a < nameprice.size(); a++) {
						viewlist[a] = nameprice.get(a).itemname + " for Price: " + nameprice.get(a).itemprice;
						System.out.println("aray is: ********" + nameprice.get(a).itemname);
						// listModel.addElement(viewlist[a]);
						c.addItem(viewlist[a]);
					}

					JPanel titleline = new JPanel();
					titleline.setLayout(new FlowLayout());
					titleline.add(c);
					titleline.add(new JLabel("enter quantity"));
					itemquantity.setText("1");
					titleline.add(itemquantity);

					titleline.add(new JScrollPane(itemtextarea));
					titleline.add(movebutton);
					f9.setVisible(false);
					JScrollPane scroll = new JScrollPane(itemtextarea);
					scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					titleline.add(scroll);

					JPanel buttonline = new JPanel();
					buttonline.setLayout(new FlowLayout());
					// buttonline.add(gettotalbutton);
					buttonline.add(new JLabel("Total Payment"));
					buttonline.add(ftotal);
					buttonline.add(paymentbutton);
					buttonline.add(closebutton);

					JPanel finalmenu = new JPanel();
					finalmenu.setLayout(new BorderLayout());
					// finalmenu.add(new JLabel("Use control button to select
					// multiple items"),BorderLayout.NORTH);
					finalmenu.add(titleline, BorderLayout.CENTER);
					finalmenu.add(buttonline, BorderLayout.SOUTH);

					f4 = new JFrame("GUI FOR MENU");
					f4.setSize(400, 200);
					f4.setLocationRelativeTo(null);
					f4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f4.setVisible(true);
					// f4.setLayout(new FlowLayout());
					// f4.add(new JScrollPane(leftlist));
					// f4.add(movebutton);
					// f4.add(new JScrollPane(rightlist));

					Container contentPane = f4.getContentPane();
					contentPane.add(finalmenu);
					f4.pack();

				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		movebutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 double total=0;
				 int index = c.getSelectedIndex();
				// TODO Auto-generated method stub
				 boolean flagadd = true;
				 try{
				 ItemList selected = new ItemList(nameprice.get(index).itemname, nameprice.get(index).itemprice,
							Integer.parseInt(itemquantity.getText()));
					selecteditems.add(selected);
					flagadd= true;
					}catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(null, "Enter the quantity only in numbers");
					flagadd = false;
					}
				 
				 
				 if(flagadd){
				 itemtextarea.append(itemquantity.getText() + " of " + (c).getSelectedItem() + "\n");
				
				
				
				for (int a = 0; a < selecteditems.size(); a++) {
					System.out.println("the selected array list items are:" + selecteditems.get(a).itemname);
					Double price = Double.parseDouble(nameprice.get(index).itemprice);

					// System.out.println("quantiy: "+itemquantity.getText());
					// System.out.println("price: "+ price);
					System.out.println("price:" + selecteditems.get(a).itemprice);
					System.out.println("quant:" + selecteditems.get(a).quantity);

					// total =
					// total+(Double.parseDouble(itemquantity.getText())*price);
					total = total+ (Double.parseDouble(selecteditems.get(a).itemprice) * selecteditems.get(a).quantity);
					System.out.println("Debug: val->" + total);
					String total2 = Double.toString(total);
					ftotal.setEditable(false);
					ftotal.setText(total2);
					total5 = total;

				}
				 }
			}
		});

		createnewaccount.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				boolean flag10 = true;
				boolean telflag = true;
				int newphone = 0;
				try{
					newphone = Integer.parseInt(fphone.getText());
					telflag = true;
				}catch(NumberFormatException e4){
					JOptionPane.showMessageDialog(null, "***telephone number should be in digits");
					telflag=false;
				}
				
				if(telflag){
				if(fusername.getText().isEmpty()|fpassword.getText().isEmpty()|ffname.getText().isEmpty()|flast.getText().isEmpty()|faddress.getText().isEmpty()|fusername.getText().length()>15|fpassword.getText().length()>15|ffname.getText().length()>15|flast.getText().length()>15|faddress.getText().length()>30|fphone.getText().isEmpty()|fphone.getText().length()!=10|femail.getText().isEmpty()|femail.getText().length()>20){
					JOptionPane.showMessageDialog(null, "The data fields cannot be empty or too long. \nThe length of the fields should be as follows:\nUersername : 15 characters\nPasssword : 15 characters\nFirst Name : 15 characters\nLast Name : 15 characters\nAddress: 30 characters\nTelePhone: 10 characters exact\nemail: 30 characters");
				}else{
					try {
						// System.out.println(fusername.getText());
						sendMessage(fusername.getText(), fpassword.getText(), ffname.getText(), flast.getText(),
								faddress.getText(), CREATE_ACCOUNT, femail.getText(), newphone);

					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						 flag10 = fromServerdata.readBoolean();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					if(flag10){
						
				f2.setVisible(false);
				
				JPanel buttonline = new JPanel();
				buttonline.setLayout(new FlowLayout());
				buttonline.add(placeorderbutton);
				buttonline.add(closebutton);
				
				f9 = new JFrame("GUI FOR MENU");
				f9.setSize(400, 200);
				f9.setLocationRelativeTo(null);
				f9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f9.setVisible(true);
				
				Container contentPane = f9.getContentPane();
				contentPane.add(buttonline);
				f9.pack();
				
					}
				}
					}

			}
			

		});

		newuserbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JPanel usr = new JPanel();
				usr.setLayout(new FlowLayout());
				usr.add(new Label("Username: "));
				usr.add(fusername);
				usr.add(new Label("Password:"));
				usr.add(fpassword);

				JPanel one = new JPanel();
				one.setLayout(new FlowLayout());
				one.add(new Label("First Name: "));
				one.add(ffname);
				one.add(new Label("Last Name"));
				one.add(flast);

				JPanel addr = new JPanel();
				addr.add(new Label("Address: "));
				addr.add(faddress);

				JPanel but = new JPanel();
				but.add(createnewaccount);
				//but.add(placeorderbutton);
				
				JPanel tel = new JPanel();
				tel.setLayout(new FlowLayout());
				tel.add(new Label("TelePhone "));
				tel.add(fphone);
				tel.add(new Label("Email Address: "));
				tel.add(femail);

				JPanel newuserlayout = new JPanel();

				newuserlayout.setLayout(new GridLayout(5, 1));
				newuserlayout.add(usr);
				newuserlayout.add(one);
				newuserlayout.add(addr);
				newuserlayout.add(tel);
				newuserlayout.add(but);
				f1.setVisible(false);
				f2 = new JFrame("GUI FOR new user");
				f2.setSize(1000, 800);
				f2.setLocationRelativeTo(null);
				f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f2.setVisible(true);

				Container contentPane = f2.getContentPane();
				contentPane.add(newuserlayout);
				f2.pack();

			}

		});
		loginbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e1) {

				JPanel enter = new JPanel();
				enter.setLayout(new FlowLayout());
				enter.add(new Label("Enter User name and password"));

				JPanel us = new JPanel();
				us.setLayout(new FlowLayout());
				us.add(new Label("Username"));
				us.add(fusername);

				JPanel pass = new JPanel();
				pass.setLayout(new FlowLayout());
				pass.add(new Label("PassWord"));
				pass.add(fpassword);

				JPanel but = new JPanel();
				but.add(logbutton);
				// but.add(placeorderbutton);

				JPanel layout = new JPanel();

				layout.setLayout(new GridLayout(4, 1));
				layout.add(enter);
				layout.add(us);
				layout.add(pass);
				layout.add(but);

				f1.setVisible(false);

				f2 = new JFrame("GUI FOR EXISTING USER LOGIN");
				// f.setSize(1000, 800);
				f2.setLocationRelativeTo(null);
				f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f2.setVisible(true);

				Container contentPane = f2.getContentPane();
				contentPane.add(layout);
				f2.pack();

			}

		});

		dbaloginbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewdba();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		
		chefbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewchef();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		
		
		paymentbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(total5==0){
					JOptionPane.showMessageDialog(null,	"Payment cannot be done without ordering" );
						
					}else{
				try {
					// toServer.writeObject(selecteditems);
					sendMessage(fusername.getText(), "", "", "", "", ADD_ORDER, "", "", selecteditems);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				f4.setVisible(false);

				JPanel card = new JPanel();
				card.setLayout(new FlowLayout());
				card.add(new JLabel("Enter Card Number : "));
				card.add(fcardnumber);
				card.add(new JLabel("Enetr the Exp Date:mm/dd/yyyy "));
				card.add(fexpdate);

				JPanel but1 = new JPanel();
				but1.setLayout(new FlowLayout());
				but1.add(makepayment);
				but1.add(closebutton);

				JPanel fin = new JPanel();
				fin.setLayout(new BorderLayout());
				fin.add(card, BorderLayout.CENTER);
				fin.add(but1, BorderLayout.SOUTH);

				f5 = new JFrame("GUI FOR PAYMENT");
				// f.setSize(1000, 800);
				f5.setLocationRelativeTo(null);
				f5.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f5.setVisible(true);

				Container contentPane = f5.getContentPane();
				contentPane.add(fin);
				f5.pack();
					}
			}
		});

		logbutton.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					sendMessage(fusername.getText(), fpassword.getText(), "", "", "", LOG_IN, "", "");
				} catch (ClassNotFoundException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				try {
					// System.out.println(fusername.getText());
					logflag = fromServerdata.readBoolean();

					//System.out.println("log flag is" + logflag);

				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (logflag) {
					f2.setVisible(false);

					JPanel bttonline = new JPanel();
					bttonline.setLayout(new FlowLayout());
					bttonline.add(placeorderbutton);
					bttonline.add(viewstatusbutton);

					f9 = new JFrame("Select from options");
					// f.setSize(1000, 800);
					f9.setLocationRelativeTo(null);
					f9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f9.setVisible(true);

					Container contentPane = f9.getContentPane();
					contentPane.add(bttonline);
					f9.pack();
				} 

			}

		});

		makepayment.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				boolean payflag = true;
				validating = new Validation();

				
				if(fexpdate.getText().length()>10|fexpdate.getText().isEmpty()|fcardnumber.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Data fields cannot be too long or null");
				}else{
					if(validating.aValidDate(fexpdate.getText())){
					String[] splitstr;
					splitstr = fexpdate.getText().split("/");
					System.out.println("exp date at 3 : "+splitstr[2]);
					//Integer.parseInt(splitstr[2]);
					if(Integer.parseInt(splitstr[2])<=2015){
						JOptionPane.showMessageDialog(null,"Date invalid: exp year invalid");
						
					}
					
					
					
					else{
				
				
				try {
					sendMessage(fusername.getText(), "", "", "", "", MAKE_PAYMENT, "", "", null, total5,
							fcardnumber.getText(), fexpdate.getText());
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				try {
					payflag = fromServerdata.readBoolean();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				if(payflag){
				
				
				
				f5.setVisible(false);
				JPanel bottom = new JPanel();
				bottom.setLayout(new FlowLayout());
				bottom.add(viewstatusbutton);
				//bottom.add(statusupdatebutton);
				bottom.add(closebutton);
				
				f9 = new JFrame("Select from options");
				// f.setSize(1000, 800);
				f9.setLocationRelativeTo(null);
				f9.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f9.setVisible(true);

				Container contentPane = f9.getContentPane();
				contentPane.add(bottom);
				f9.pack();
				
				}}}
			}
			}

		});
		
		
		
		viewstatusbutton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
				boolean flagorder = true;
				try {
					sendMessage(fusername.getText(),VIEW_ORDERID);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					flagorder = fromServerdata.readBoolean();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}


				//for(int a =0; a<selecteditems.size();a++){
				//System.out.println("final list"+selecteditems.get(a).itemname+selecteditems.get(a).itemprice);
				//}
				
				if(flagorder){
				f9.setVisible(false);

				JPanel top = new JPanel();
				top.setLayout(new BorderLayout());
				top.add(new JLabel("Final selected items are :"), BorderLayout.NORTH);
				top.add(new JScrollPane(finalitemstextarea),BorderLayout.CENTER);
				top.add(movebutton);
				//f2.setVisible(false);
				JScrollPane scroll = new JScrollPane(finalitemstextarea);
				scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				top.add(scroll);
				selecteditems = new ArrayList<ItemList>();
				
				try {
					sendMessage(fusername.getText(), "", "", "", "", GET_SELECTED_ITEMS, "", "", null,
						0, "", "");
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					finalitems= (ArrayList<ItemList>) fromServer.readObject();
					editablelist = new String[finalitems.size()];

		
					for (int a = 0; a < finalitems.size(); a++) {
						editablelist[a] = finalitems.get(a).quantity+" of "+ finalitems.get(a).itemname + " for Price: " + finalitems.get(a).itemprice;
					//	System.out.println("aray is: ********" + nameprice.get(a).itemname);
						// listModel.addElement(viewlist[a]);
						finalitemstextarea.append(finalitems.get(a).quantity+finalitems.get(a).itemname + " for Price: " + finalitems.get(a).itemprice);
						finalitemstextarea.append("\n");
						//c1.addItem(editablelist[a]);
						//c2.addItem(editablelist[a]);
					}
					
					
					
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				try {
					sendMessage("", "", "", "", "", SHOW_MENU, "", "");
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					int i = 0;
					while (true) {
						nameprice = (ArrayList<ItemList>) fromServer.readObject();
						i = i + 1;
						viewlist = new String[nameprice.size()];
						// System.out.println("size of
						// vielist"+nameprice.size());
						break;
					}
					// listModel = new DefaultListModel();
					for (int a = 0; a < nameprice.size(); a++) {
						viewlist[a] = nameprice.get(a).itemname + " for Price: " + nameprice.get(a).itemprice;
						System.out.println("aray is: ********" + nameprice.get(a).itemname);
						// listModel.addElement(viewlist[a]);
						c.addItem(viewlist[a]);
					}
				}catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				
				JPanel statusbuttonline = new JPanel();
				statusbuttonline.setLayout(new FlowLayout());
				statusbuttonline.add(showselected);
				statusbuttonline.add(statusupdatebutton);
				statusbuttonline.add(closebutton);
				
				JPanel fnl1 = new JPanel();
				fnl1.setLayout(new BorderLayout());
				fnl1.add(top, BorderLayout.CENTER);
				fnl1.add(statusbuttonline, BorderLayout.SOUTH);
				
				
				f7 = new JFrame("Final window");
				// f.setSize(1000, 800);
				f7.setLocationRelativeTo(null);
				f7.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f7.setVisible(true);

				Container contentPane = f7.getContentPane();
				contentPane.add(fnl1);
				f7.pack();

				}else{
					JOptionPane.showMessageDialog(null, "No Order to show");
				}
			}
		});
		
		
		
		
		statusupdatebutton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean viewflag= true;
				
				try {
					sendMessage(fusername.getText(), "", "", "", "", GET_EDITABLE_ITEMS, "", "", null,
							0, "", "");
				} catch (ClassNotFoundException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					finalitems1= (ArrayList<ItemList>) fromServer.readObject();
				} catch (ClassNotFoundException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					viewflag = fromServerdata.readBoolean();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				editablelist = new String[finalitems1.size()];

				if(viewflag){
				for (int a = 0; a < finalitems1.size(); a++) {
					editablelist[a] = finalitems1.get(a).quantity+" of "+ finalitems1.get(a).itemname + " for Price: " + finalitems1.get(a).itemprice;
				//	System.out.println("aray is: ********" + nameprice.get(a).itemname);
					// listModel.addElement(viewlist[a]);
					finalitemstextarea1.append(finalitems1.get(a).quantity+finalitems1.get(a).itemname + " for Price: " + finalitems1.get(a).itemprice);
					finalitemstextarea1.append("\n");
					c1.addItem(editablelist[a]);
					c2.addItem(editablelist[a]);
				}
				
				
				
		
				String statusl = null;
				Boolean flag3 = null;
				
				
				
		f7.setVisible(false);
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.add(new JLabel("Final selected items are :"), BorderLayout.NORTH);
		top.add(new JScrollPane(finalitemstextarea),BorderLayout.CENTER);
		top.add(movebutton);
		//f2.setVisible(false);
		JScrollPane scroll = new JScrollPane(finalitemstextarea1);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		top.add(scroll);
	
		JPanel addline = new JPanel();
		addline.setLayout(new FlowLayout());
		addline.add(new JLabel("Select item to be added") );
		addline.add(c);
		addline.add(new JLabel("Enter Quantity"));
		newquantity.setText("1");
		addline.add(newquantity);
		
		
		JPanel addbut = new JPanel();
		addbut.setLayout(new FlowLayout());
		addbut.add(statusaddbutton);
		
		
		JPanel fnladd = new JPanel();
		fnladd.setLayout(new BorderLayout());
		fnladd.add(addline,BorderLayout.NORTH);
		fnladd.add(addbut,BorderLayout.CENTER);
		
		
		JPanel deleteline = new JPanel();
		deleteline.setLayout(new FlowLayout());
		deleteline.add(new JLabel("Select item to be deleted") );
		deleteline.add(c1);
		deleteline.add(statusdeletebutton);
		
		
		JPanel showstatus = new JPanel();
		showstatus.setLayout(new FlowLayout());
		//showstatus.add(new JLabel("Order Status is : "+statusl));
		
		
		JPanel fnl = new JPanel();
		fnl.setLayout(new BorderLayout());
		fnl.add(showstatus,BorderLayout.NORTH);

		fnl.add(fnladd,BorderLayout.CENTER);
		fnl.add(deleteline,BorderLayout.SOUTH);
		//fnl.add(updateline,BorderLayout.SOUTH);
		
		JPanel bottombutton = new JPanel();
		bottombutton.setLayout(new FlowLayout());
		bottombutton.add(totalpaymetlabel);
		bottombutton.add(ftotal);
		bottombutton.add(makenewpayment);
		bottombutton.add(deleteentire);
		bottombutton.add(closebutton);
		
		/*
		if(!flag3){
			fnladd.setVisible(false);
			deleteline.setVisible(false);
			bottombutton.setVisible(false);
		}else{
			
			*/
			
			fnladd.setVisible(true);
			deleteline.setVisible(true);
			bottombutton.setVisible(true);
		//}
		
		
		
		totalpaymetlabel.setVisible(false);
		makenewpayment.setVisible(false);
		ftotal.setVisible(false);
		closebutton.setVisible(true);
		
		
		
		JPanel fnl1 = new JPanel();
		fnl1.setLayout(new BorderLayout());
		fnl1.add(top,BorderLayout.NORTH);

		fnl1.add(fnl,BorderLayout.CENTER);
		fnl1.add(deleteline,BorderLayout.SOUTH);
		
		JPanel fnl2 = new JPanel();
		fnl2.setLayout(new BorderLayout());
		fnl2.add(fnl1,BorderLayout.CENTER);

		fnl2.add(bottombutton,BorderLayout.SOUTH);
		
		
		f8 = new JFrame("Final window");
		// f.setSize(1000, 800);
		f8.setLocationRelativeTo(null);
		f8.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f8.setVisible(true);

		Container contentPane = f8.getContentPane();
		contentPane.add(fnl2);
		f8.pack();
			}
			}
		});
		
		
		
		showselected.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				f7.setVisible(false);
				
				JPanel top = new JPanel();
				top.setLayout(new BorderLayout());
				top.add(new JLabel("ALL selected items are :"), BorderLayout.NORTH);
				top.add(new JScrollPane(finalitemstextarea2),BorderLayout.CENTER);
				top.add(closebutton,BorderLayout.SOUTH);
				// top.add(movebutton);
				//f2.setVisible(false);
				JScrollPane scroll = new JScrollPane(finalitemstextarea2);
				scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				top.add(scroll);
				editablelist = new String[finalitems.size()];
				//top.add(closebutton,BorderLayout.SOUTH);

				
				for (int a = 0; a < finalitems.size(); a++) {
					editablelist[a] = finalitems.get(a).quantity+" of "+ finalitems.get(a).itemname + " for Price: " + finalitems.get(a).itemprice;
				//	System.out.println("aray is: ********" + nameprice.get(a).itemname);
					// listModel.addElement(viewlist[a]);
					finalitemstextarea2.append(finalitems.get(a).quantity+finalitems.get(a).itemname + " for Price: " + finalitems.get(a).itemprice);
					finalitemstextarea2.append("\n");
				}
				
				f8 = new JFrame("Final window");
				// f.setSize(1000, 800);
				f8.setLocationRelativeTo(null);
				f8.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f8.setVisible(true);

				Container contentPane = f8.getContentPane();
				contentPane.add(top);
				f8.pack();
					}
				});
				

				
			
		
		
		
		statusaddbutton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				boolean newaddflag;
				ArrayList<ItemList> modifiedadditems = new ArrayList<ItemList>();

				
				int temp = 0;
				
				
				try{
					temp = Integer.parseInt(newquantity.getText());
					newaddflag = true;
				}catch(NumberFormatException e3){
					JOptionPane.showMessageDialog(null,"Enter quanity only in digits");
					newaddflag=false;
				}
				
				
				if(temp!=0 & newaddflag){
					totalpaymetlabel.setVisible(true);

					makenewpayment.setVisible(true);
					ftotal.setVisible(true);
					closebutton.setVisible(false);
					statusdeletebutton.setVisible(false);
				finalitemstextarea1.append(newquantity.getText() + " of " + (c).getSelectedItem() + "\n");
				int index = c.getSelectedIndex();
				

				ItemList selected = new ItemList(nameprice.get(index).itemname, nameprice.get(index).itemprice,
						Integer.parseInt(newquantity.getText()));
				modifiedadditems.add(selected);
				finalitems1.add(selected);
				
				newTotal= newTotal+Integer.parseInt(newquantity.getText())*Double.parseDouble(selected.itemprice);
				
				
				ftotal.setText(""+newTotal);
				
				editablelist = new String[finalitems1.size()];

			
					c1.addItem(newquantity.getText() + " of " + (c).getSelectedItem() + "\n");
				
				//System.out.println("Debug:+++++++" + finalitems.size() + ",selected " + selected.itemname + "+++++"
					//	+ selected.itemprice);
				
				
				try {
					sendMessage(fusername.getText(), "", "", "", "", UPDATED_ADD, "", "", modifiedadditems);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}else{
					JOptionPane.showMessageDialog(null, "Quantity cannot be zero");
				}
				
				
				
			}
		});
		
		
		statusdeletebutton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				ArrayList<ItemList> deleteditemslist = new ArrayList<ItemList>();

				totalpaymetlabel.setVisible(false);
				makenewpayment.setVisible(false);
				ftotal.setVisible(false);
				closebutton.setVisible(true);
				
				
				int index = c1.getSelectedIndex();

				ItemList selected = new ItemList(finalitems1.get(index).itemname, finalitems1.get(index).itemprice,
						finalitems1.get(index).quantity);
				deleteditemslist.add(selected);
				//System.out.println("Debug:+++++++" + finalitems.size() + ",selected " + selected.itemname + "+++++"
					//	+ selected.itemprice);
				
				try {
					sendMessage(fusername.getText(), "", "", "", "", UPDATED_DELETE, "", "", deleteditemslist);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					sendMessage(fusername.getText(), "", "", "", "", GET_EDITABLE_ITEMS, "", "", null,
							0, "", "");
				} catch (ClassNotFoundException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				finalitemstextarea1.setText("");
				c1.removeAllItems();
				
				try {
					finalitems1= (ArrayList<ItemList>) fromServer.readObject();
				} catch (ClassNotFoundException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				editablelist = new String[finalitems1.size()];

	
				for (int a = 0; a < finalitems1.size(); a++) {
					editablelist[a] = finalitems1.get(a).quantity+" of "+ finalitems1.get(a).itemname + " for Price: " + finalitems1.get(a).itemprice;
				//	System.out.println("aray is: ********" + nameprice.get(a).itemname);
					// listModel.addElement(viewlist[a]);
					finalitemstextarea1.append(finalitems1.get(a).quantity+finalitems1.get(a).itemname + " for Price: " + finalitems1.get(a).itemprice);
					finalitemstextarea1.append("\n");
					c1.addItem(editablelist[a]);
				}
				
				
				
				
				
				
			}
		});

		deleteentire.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				makenewpayment.setVisible(false);
				
				try {
					sendMessage(fusername.getText(),DELETE_ENTIRE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				System.exit(2);
				closebutton.setVisible(true);
				statusdeletebutton.setVisible(true);
				newTotal =0  ;
				ftotal.setText("0");

			}
		});
		
		
		makenewpayment.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				makenewpayment.setVisible(false);
				
				try {
					sendMessage(fusername.getText(), "", "", "", "",MAKE_NEW_PAYMENT,"", "",null,newTotal,"","");
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				closebutton.setVisible(true);
				statusdeletebutton.setVisible(true);
				newTotal =0  ;
				ftotal.setText("0");

			}
		});
		
		
		closebutton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});

	}

	public void sendMessage(String username, String password, String firstname, String lastname, String address,
			int op_type, String itemname, String itemprice) throws IOException, ClassNotFoundException {
		// System.out.println(username);

		message = new Message(username, password, firstname, lastname, address, op_type, itemname, itemprice);

		toServer.writeObject(message);

	}

	
	public void sendMessage(String username, String password, String firstname, String lastname, String address,
			int op_type, String email, int telephone) throws IOException, ClassNotFoundException {
		// System.out.println(username);

		message = new Message(username, password, firstname, lastname, address, op_type, email, telephone);

		toServer.writeObject(message);

	}
	public void sendMessage(String username, String password, String firstname, String lastname, String address,
			int op_type, String itemname, String itemprice, ArrayList<?> lsit)
					throws IOException, ClassNotFoundException {
		// System.out.println(username);

		message = new Message(username, password, firstname, lastname, address, op_type, itemname, itemprice, lsit);

		toServer.writeObject(message);

	}

	public void sendMessage(String username, String password, String firstname, String lastname, String address,
			int op_type, String itemname, String itemprice, ArrayList<?> lsit, double total, String cardnumber,
			String expdate) throws IOException, ClassNotFoundException {
		// System.out.println(username);

		message = new Message(username, password, firstname, lastname, address, op_type, itemname, itemprice, lsit,
				total, cardnumber, expdate);

		toServer.writeObject(message);

	}
	public void sendMessage(String username, int op_type) throws IOException{
		message = new Message(username,op_type);
		toServer.writeObject(message);
		
	}

	private void viewdba() throws IOException {
		f1.setVisible(false);
		// System.out.println("inside view bda method in client class");

		dba = new dbalogin(this);
		// dba.pack();
		// dba.setVisible(true);

	}
	private void viewchef() throws IOException {
		f1.setVisible(false);
		// System.out.println("inside view bda method in client class");

		chef = new ChefLogin(this);
		// dba.pack();
		// dba.setVisible(true);

	}
}
