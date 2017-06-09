import java.awt.Container;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.sql.Connection;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.util.ArrayList;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Date;

public class Server extends JFrame {

	Server server;
	Socket conn;
	ServerSocket serverSocket;
	java.sql.Connection connection = null;
	java.sql.Statement statement = null;
	private PreparedStatement preparedstatement = null;
	private ResultSet resultSet;
	private ResultSet resultSet1;
	private ResultSet resultSet2;

	public static int orderID;

	ItemList item;
	String totalamntstr;
	ServerGui gui;
	ObjectInputStream serverInputStream;
	ObjectOutputStream serverOutputStream;
	DataOutputStream serverOutputStreamdata;

	Message message;
	public boolean flag;
	Double total = (double) 0;

	/// java.sql.Statement stmt;
	// ResultSet rs;
	// java.sql.Connection connection;
	private final int CREATE_ACCOUNT = 0;
	private final int LOG_IN = 1;
	private final int SHOW_MENU = 2;
	private final int ADD_ITEM = 3;
	private final int UPDATE_ITEM = 4;
	private final int DELETE_ITEM = 5;
	private final int ADD_ORDER = 6;
	private final int GET_TOTAL = 7;
	private final int MAKE_PAYMENT = 8;
	private final int GET_SELECTED_ITEMS =9;
	private final int GET_STATUS =10;
	private final int UPDATED_ADD=11;
	private final int UPDATED_DELETE=12;
	private final int GET_EDITABLE_ITEMS=13;
	private final int SHOW_CHEF_ITEMS=14;
	private final int UPDATE_CHEF_STATUS = 15;
	private final int MAKE_NEW_PAYMENT = 16;
	private final int DELETE_ENTIRE = 17;
	private final int VIEW_ORDERID = 18;








	Validation validating;

	public Server(int port)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {

		viewGui();
		startListening(port);

	}

	void viewGui() {

		JFrame frame = new JFrame();
		frame.setTitle("Pizza Station Server");
		Container contentPane = frame.getContentPane();
		gui = new ServerGui(this);
		contentPane.add(gui);
		frame.pack();
		frame.setVisible(true);

	}

	public Boolean startListening(int listeningPort)
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		gui.txtDisplayResults.append("Server Is Listening ON Port:  " + listeningPort + "\n");

		try {

			serverSocket = new ServerSocket(listeningPort);
			initializeDB();

			// conn = serverSocket.accept();
			int clientNo = 1;
			while (true) {
				// Listen for a new connection request
				conn = serverSocket.accept();

				// Display the client number
				gui.txtDisplayResults.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

				// Find the client's host name, and IP address
				InetAddress inetAddress = conn.getInetAddress();
				gui.txtDisplayResults
						.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
				gui.txtDisplayResults
						.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");

				// Create a new thread for the connection
				HandleAClient task = new HandleAClient(conn);

				// Start the new thread
				new Thread(task).start();

				// Increment clientNo
				clientNo++;
			}
		} catch (IOException ex) {
			System.err.println(ex + "1");
		}

		return true;
	}

	private void receivingMessage(Message message2)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// System.out.println("in receibing msg :" + message2.toString());
		// while (true) {

		// message = (Message )serverInputStream.readObject();

		switch (message2.op_type) {

		case CREATE_ACCOUNT:
			insertnewaccount(message2);
			break;

		case LOG_IN:
			existinglogin(message);
			break;

		case SHOW_MENU:
			readfromdba();
			break;
		case ADD_ITEM:
			additemtodba(message2);
			break;
		case UPDATE_ITEM:
			updateitemfromdba(message2);
			break;
		case DELETE_ITEM:
			deleteitemfromdba(message2);
			break;
		case ADD_ORDER:
			addorder(message2);
			break;
		case GET_TOTAL:
			calculateTotal(message2);
			break;
		case MAKE_PAYMENT:
			makepayment(message2);
			break;
		case GET_SELECTED_ITEMS:
			getselecteditems(message2);
			break;
		case GET_STATUS:
			getstatus(message2);
			break;
		case UPDATED_ADD:
			updatedadd(message2);
			break;
		case UPDATED_DELETE:
			updated_delete(message2);
			break;
		case GET_EDITABLE_ITEMS:
			get_editable_items(message2);
			break;
		case SHOW_CHEF_ITEMS:
			show_chef_items(message2);
			break;
		case UPDATE_CHEF_STATUS:
			update_chef_status(message2);
			break;
		case MAKE_NEW_PAYMENT:
			make_new_payment(message2);
			break;
		case DELETE_ENTIRE:
			delete_entire_order(message2);
			break;
		case VIEW_ORDERID:
			view_orerderid(message2);
			break;
			
			
		}
		

		// sendMessage(message);
		// }

	}

	private void view_orerderid(Message message19) throws SQLException, IOException {
		// TODO Auto-generated method stub
		boolean orderflag1 = true;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message19.username
				+ "';");
		resultSet = statement.executeQuery(query1);
		if (resultSet.next()) {
			// od = (resultSet.getInt(1));
			orderflag1 = true;
		}else{
			orderflag1 = false;
		}
		
		serverOutputStreamdata.writeBoolean(orderflag1);
		
		
	}

	private void delete_entire_order(Message message18) throws SQLException {
		// TODO Auto-generated method stub
		int od =0;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message18.username
				+ "';");
		resultSet = statement.executeQuery(query1);
		if (resultSet.next()) {
			 od = (resultSet.getInt(1));
		}
		String query = "DELETE FROM `Order` WHERE OrderId = "+ od+";";
		PreparedStatement preparedstatement = connection.prepareStatement(query);
		 preparedstatement.execute();
		JOptionPane.showMessageDialog(null, "Order record "+od+" is deleted");
		
		
		String query2 = "DELETE FROM `ItemList` WHERE OrderId = "+ od+";";
		PreparedStatement preparedstatement1 = connection.prepareStatement(query2);
		 preparedstatement1.execute();
		JOptionPane.showMessageDialog(null, "List with Order record "+od+" is deleted");
		
		
		
	}

	private void make_new_payment(Message message17) throws SQLException {
		
		// TODO Auto-generated method stub
		int od = 0;
		double amnt = 0;
		double totalamount= 0;
		//String totalamntstr;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message17.username
				+ "' AND Status = 'Order Placed';");
		//System.out.println("user name "+message10.username );
		resultSet = statement.executeQuery(query1);
		if (resultSet.next()) {
			 od = (resultSet.getInt(1));
		}
		String query2 = ("SELECT * FROM `Payment` WHERE OrderID = '" + od
				+ "';");
		
		resultSet1 = statement.executeQuery(query2);
		if (resultSet1.next()) {
			String  amntstr = (resultSet1.getString(3));
			amnt = Double.parseDouble(amntstr);
			totalamount = message17.total + amnt;
			totalamntstr = Double.toString(totalamount);
			PreparedStatement preparedstatement3 = connection.prepareStatement(
					"UPDATE `Payment` SET Amount = ?  WHERE OrderID = "
							+ od + ";");
			preparedstatement3.setDouble(1, totalamount);
			preparedstatement3.execute();
		}
		
		
		
		
	}

	private void update_chef_status(Message message16) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement preparedstatement3 = connection.prepareStatement(
				"UPDATE `Order` SET Status = ?  WHERE OrderID = "
						+ message16.orderid + ";");
		preparedstatement3.setString(1, "Order InProcess");
		preparedstatement3.execute();
		
		
		
	}

	private void show_chef_items(Message message15) throws SQLException, IOException {
		
		// TODO Auto-generated method stub
		ArrayList<Integer> orderids = new ArrayList<Integer>();
		ArrayList<Integer> itemids = new ArrayList<Integer>();
		ArrayList<ItemList> finalitems = new ArrayList<ItemList>();
		int itemQuantity = 0 ;
		
		String nestedQuery= "SELECT `ListID`, `OrderID`, `ItemID`, `ItemQuantity`, (select itemName from dbaDatabase where itemId=ItemList.itemID) as ItemName FROM ItemList WHERE orderID in (SELECT OrderID FROM `Order` WHERE `Status`='Order Placed')";
		resultSet=statement.executeQuery(nestedQuery);
		
			while(resultSet.next()){
				int cheforderid = resultSet.getInt(2);
				int chefitemquantity=resultSet.getInt(4);
				String chefitemname = resultSet.getString(5);
				
				
				ItemList chefitem = new ItemList(chefitemname,chefitemquantity,cheforderid);
				finalitems.add(chefitem);
				
			}
		
		//statement = connection.createStatement();
		
		serverOutputStream.writeObject(finalitems);
		
		
		
	}

	private void get_editable_items(Message message14) throws SQLException, IOException {
		// TODO Auto-generated method stub
		boolean flag99 = true;
		
		ArrayList<Integer> orderids = new ArrayList<Integer>();
		ArrayList<Integer> itemids = new ArrayList<Integer>();
		ArrayList<ItemList> finalitems = new ArrayList<ItemList>();
		int itemQuantity = 0 ;

		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message14.username
				+ "' AND Status = 'Order Placed';");
		//System.out.println("user name "+message10.username );
		resultSet = statement.executeQuery(query1);
		if (resultSet.next()) {
			int OrderID = (resultSet.getInt(1));
			orderids.add(OrderID);
			//String Status = (resultSet1.getString(3));
			//System.out.println("orderid "+ OrderID+"Status : "+Status);
			flag99 = true;
		}else{
			JOptionPane.showMessageDialog(null, "There are no items that can be updated");
			flag99 = false;
		}
		
		for(int x=0; x<orderids.size();x++){
			String query2= ("SELECT `ListID`, `OrderID`, `ItemID`, `ItemQuantity` FROM `ItemList` WHERE OrderID = '" + orderids.get(x)
					+ "';");
			System.out.println("ordeid  "+orderids.get(x));
			resultSet1 = statement.executeQuery(query2);
			while (resultSet1.next()) {
				int ListID = (resultSet1.getInt(1));
				
				//orderids.add(OrderID);
				int itemId = (resultSet1.getInt(3));
				itemids.add(itemId);
				 itemQuantity = (resultSet1.getInt(4));
				
				System.out.println("orderid "+ ListID+"Itemid : "+itemId);
			}
		}
		
			for (int y = 0;y<itemids.size();y++){
				String query4= ("SELECT `ListID`, `OrderID`, `ItemID`, `ItemQuantity` FROM `ItemList` WHERE (OrderID = "+orderids.get(0) +") AND (ItemID = " + itemids.get(y)
				+ ");");
		System.out.println("itemid  "+itemids.get(y));
		resultSet2 = statement.executeQuery(query4);
		if(resultSet2.next()){
			itemQuantity = resultSet2.getInt(4);
		}
		
		//statement = connection.createStatement();
		
				
				String query3= ("SELECT `ItemID`, `itemName`, `Price` FROM `dbaDatabase` WHERE ItemID = '" + itemids.get(y)
				+ "';");
		System.out.println("itemid  "+itemids.get(y));
		resultSet1 = statement.executeQuery(query3);
		while (resultSet1.next()) {
			//int ItemID = (resultSet1.getInt(1));
			//listids.add(ListID);
			//orderids.add(OrderID);
			String item_n = (resultSet1.getString(2));
			int item_p= (resultSet1.getInt(3));
			String itemp = Integer.toString(item_p);
			ItemList tempitem = new ItemList(item_n,itemp,itemQuantity);
			finalitems.add(tempitem);
			
			
			System.out.println("item name "+ item_n+"price : "+item_p +"qiuantity"+itemQuantity);
				
				
			}
		}
			
			
		if(finalitems!=null){
		serverOutputStream.writeObject(finalitems);
		}
		serverOutputStreamdata.writeBoolean(flag99);
		
		
		
	}

	private void updated_delete(Message message13) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<ItemList> selectednew = new ArrayList<ItemList>();
		ArrayList<Integer> orderids = new ArrayList<Integer>();
		for(int b = 0; b<message13.itemlist.size();b++){
			selectednew.add((ItemList) message13.itemlist.get(b));
			
		}
		int OrderId = 0 ;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userNAme = '" + message13.username
				+ "';");
		//System.out.println(query1);
		resultSet1 = statement.executeQuery(query1);
		while (resultSet1.next()) {
			OrderId = (resultSet1.getInt(1));
			System.out.println("order id of new added id"+OrderId);
			orderids.add(OrderId);
			
		}

			for (int a = 0; a < selectednew.size(); a++) {
				String iname = selectednew.get(a).itemname;
				int iquantity = 0;
				int i_id;
				String query4 = ("SELECT `ItemID`, `itemName`, `Price` FROM `dbaDatabase` WHERE itemName = '" + iname
						+ "';");
				System.out.println(query4);
				resultSet2 = statement.executeQuery(query4);
				if (resultSet2.next()) {
					i_id = (resultSet2.getInt(1));
					iquantity = selectednew.get(a).quantity;
					String query2 = "DELETE FROM `ItemList` WHERE (OrderID = " + OrderId + ") AND (ItemID = " + i_id + ") AND (ItemQuantity = " + iquantity + ");";
					PreparedStatement preparedstatement1 = connection.prepareStatement(query2,
							Statement.RETURN_GENERATED_KEYS);
					preparedstatement1.execute();
					
					
				
			}
		}
		
		
	}

	private void updatedadd(Message message12) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<ItemList> selectednew = new ArrayList<ItemList>();
		for(int b = 0; b<message12.itemlist.size();b++){
			selectednew.add((ItemList) message12.itemlist.get(b));
		}
		
		int OrderId ;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userNAme = '" + message12.username
				+ "';");
		//System.out.println(query1);
		resultSet1 = statement.executeQuery(query1);
		if (resultSet1.next()) {
			OrderId = (resultSet1.getInt(1));
			System.out.println("order id of new added id"+OrderId);
			
			
			for (int a = 0; a < selectednew.size(); a++) {
				String iname = selectednew.get(a).itemname;
				int iquantity = 0;
				int i_id;
				String query4 = ("SELECT `ItemID`, `itemName`, `Price` FROM `dbaDatabase` WHERE itemName = '" + iname
						+ "';");
				System.out.println(query4);
				resultSet1 = statement.executeQuery(query4);
				if (resultSet1.next()) {
					i_id = (resultSet1.getInt(1));
					iquantity = selectednew.get(a).quantity;

					String query2 = "INSERT INTO `ItemList` (`OrderID`,`ItemId`,`ItemQuantity`) VALUES ('" + OrderId + "','"
							+ i_id + "','" + iquantity + "')";
					PreparedStatement preparedstatement1 = connection.prepareStatement(query2,
							Statement.RETURN_GENERATED_KEYS);
					preparedstatement1.execute();

					//resultSet = preparedstatement1.getGeneratedKeys();
					//resultSet.next();
					//int listid = resultSet.getInt(1);
				}
			}
					
		}
		
		
	}

	private void getstatus(Message message11) throws SQLException, IOException {
		// TODO Auto-generated method stub
	 boolean flag1 = true;
		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message11.username
				+ "';");
		//System.out.println("user name "+message10.username );
		resultSet1 = statement.executeQuery(query1);
		while (resultSet1.next()) {
			int OrderID = (resultSet1.getInt(1));
		//	orderids.add(OrderID);
			String Status = (resultSet1.getString(3));
			
			if(Status.equals("Order InProcess")){
				flag1 = false;
				
			}
		}
		System.out.println("ststus flag is : "+ flag1);
		serverOutputStreamdata.writeBoolean(flag1);
		
	}

	private void getselecteditems(Message message10) throws SQLException, IOException {
		
		// TODO Auto-generated method stub
		ArrayList<Integer> orderids = new ArrayList<Integer>();
		ArrayList<Integer> itemids = new ArrayList<Integer>();
		ArrayList<ItemList> finalitems = new ArrayList<ItemList>();
		int itemQuantity = 0 ;

		String query1 = ("SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '" + message10.username
				+ "';");
		//System.out.println("user name "+message10.username );
		resultSet1 = statement.executeQuery(query1);
		while (resultSet1.next()) {
			int OrderID = (resultSet1.getInt(1));
			orderids.add(OrderID);
			String Status = (resultSet1.getString(3));
			//System.out.println("orderid "+ OrderID+"Status : "+Status);
		}
		for(int x=0; x<orderids.size();x++){
			String query2= ("SELECT `ListID`, `OrderID`, `ItemID`, `ItemQuantity` FROM `ItemList` WHERE OrderID = '" + orderids.get(x)
					+ "';");
			System.out.println("ordeid  "+orderids.get(x));
			resultSet1 = statement.executeQuery(query2);
			while (resultSet1.next()) {
				int ListID = (resultSet1.getInt(1));
				
				//orderids.add(OrderID);
				int itemId = (resultSet1.getInt(3));
				itemids.add(itemId);
				 itemQuantity = (resultSet1.getInt(4));
				
				System.out.println("orderid "+ ListID+"Itemid : "+itemId);
			}
		}
		
			for (int y = 0;y<itemids.size();y++){
				String query4= ("SELECT `ListID`, `OrderID`, `ItemID`, `ItemQuantity` FROM `ItemList` WHERE (OrderID = "+orderids.get(0) +") AND (ItemID = " + itemids.get(y)
				+ ");");
		System.out.println("itemid  "+itemids.get(y));
		resultSet2 = statement.executeQuery(query4);
		if(resultSet2.next()){
			itemQuantity = resultSet2.getInt(4);
		}
		
		//statement = connection.createStatement();
		
				
				String query3= ("SELECT `ItemID`, `itemName`, `Price` FROM `dbaDatabase` WHERE ItemID = '" + itemids.get(y)
				+ "';");
		System.out.println("itemid  "+itemids.get(y));
		resultSet1 = statement.executeQuery(query3);
		while (resultSet1.next()) {
			//int ItemID = (resultSet1.getInt(1));
			//listids.add(ListID);
			//orderids.add(OrderID);
			String item_n = (resultSet1.getString(2));
			int item_p= (resultSet1.getInt(3));
			String itemp = Integer.toString(item_p);
			ItemList tempitem = new ItemList(item_n,itemp,itemQuantity);
			finalitems.add(tempitem);
			
			
			System.out.println("item name "+ item_n+"price : "+item_p +"qiuantity"+itemQuantity);
				
				
			}
		}
			
			
		serverOutputStream.writeObject(finalitems);

		
		
	}

	private void makepayment(Message message9) throws SQLException, IOException {
		boolean flag11 = true;
		boolean flag12 = true;
		boolean flag13 = true;

		validating = new Validation();
		// TODO Auto-generated method stub
		System.out.println("card number in server"+message9.cardnumber + "exp date : "+message9.expdate);
		try{
			if(validating.aValidNumber(message9.cardnumber) & validating.aValidDate(message9.expdate)){
		
		String query3 = "INSERT INTO `Payment` (`OrderID`,`Amount`,`CreditCard`,`ExpDate`) VALUES ('" + orderID + "','"
				+ message9.total + "','" + message9.cardnumber + "','"+message9.expdate+"')";
		PreparedStatement preparedstatement2 = connection.prepareStatement(query3,
				Statement.RETURN_GENERATED_KEYS);
		preparedstatement2.execute();
		PreparedStatement preparedstatement3 = connection.prepareStatement(
				"UPDATE `Order` SET Status = ?  WHERE OrderID = "
						+ orderID + ";");
		preparedstatement3.setString(1, "Order Placed");
		preparedstatement3.execute();
		flag11 = true;
		
		}else{
			JOptionPane.showMessageDialog(null, "Card Number or exp date is incorrect");
			flag11 = false;
		}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Card Number and Date should be in digits only");
			flag12 = false;
		}
		if(flag11&flag12){
			flag13 = true;
		}else{
			flag13 = false;
		}
		serverOutputStreamdata.writeBoolean(flag13);
	}

	private void calculateTotal(Message message8) throws IOException {
		// TODO Auto-generated method stub
		// System.out.println("Server Debugger size:"+message8.password);
		ArrayList<ItemList> selecteditems = new ArrayList<ItemList>();
		System.out.println("size of aara in calculate" + message8.itemlist.size());
		// ArrayList<ItemList> temp = (ArrayList<ItemList>) message8.itemlist;
		// System.out.println("Debug1:"+temp.size());

		for (int i = 0; i < message8.itemlist.size(); i++) {
			selecteditems.add((ItemList) message8.itemlist.get(i));
			System.out.println(
					"price is :" + selecteditems.get(i).itemprice + "quantity is" + selecteditems.get(i).quantity);
		}
		for (int a = 0; a < selecteditems.size(); a++) {
			total = total + ((selecteditems.get(a).quantity) * Double.parseDouble(selecteditems.get(a).itemprice));
			System.out.println("total is " + total);
		}
		// serverOutputStreamdata.writeDouble(total);

	}

	private void addorder(Message message7) throws ClassNotFoundException, IOException, SQLException {
		System.out.println("in add order class");
		int orderid = 0;
		ArrayList<ItemList> selecteditems = new ArrayList<ItemList>();
		for (int i = 0; i < message7.itemlist.size(); i++) {
			selecteditems.add((ItemList) message7.itemlist.get(i));
			System.out.println("in server add order" + selecteditems.get(i).itemname);
		}
		
		String query5  = "SELECT `OrderID`, `userName`, `Status` FROM `Order` WHERE userName = '"
				+ message7.username +"' AND Status = "+ "'Order Placed'"+";";
		resultSet1 = statement.executeQuery(query5);
		if(!resultSet1.next()){
			// orderid = resultSet1.getInt(1);
			String query = "INSERT INTO `Order` (`userName`,`Status`) VALUES ('" + message7.username + "','"
					+ "Order Selected" + "')";
			PreparedStatement preparedstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedstatement.execute();

			// resultSet = statement.executeQuery("select * from Order Where
			// userName = '" + message7.username + "';");

			resultSet1 = preparedstatement.getGeneratedKeys();
			resultSet1.next();
			orderID = resultSet1.getInt(1);
			
			
		}else{
			orderID = resultSet1.getInt(1);
		
		}
		
	System.out.println("order id is : " + orderID);
		for (int a = 0; a < selecteditems.size(); a++) {
			String iname = selecteditems.get(a).itemname;
			int iquantity = Integer.parseInt(selecteditems.get(a).itemprice);
			int i_id;
			String query1 = ("SELECT `ItemID`, `itemName`, `Price` FROM `dbaDatabase` WHERE itemName = '" + iname
					+ "';");
			System.out.println(query1);
			resultSet1 = statement.executeQuery(query1);
			if (resultSet1.next()) {
				i_id = (resultSet1.getInt(1));
				iquantity = selecteditems.get(a).quantity;

				String query2 = "INSERT INTO `ItemList` (`OrderID`,`ItemId`,`ItemQuantity`) VALUES ('" + orderID + "','"
						+ i_id + "','" + iquantity + "')";
				PreparedStatement preparedstatement1 = connection.prepareStatement(query2,
						Statement.RETURN_GENERATED_KEYS);
				preparedstatement1.execute();

				resultSet = preparedstatement1.getGeneratedKeys();
				resultSet.next();
				int listid = resultSet.getInt(1);
				System.out.println("item name is : " + iname + " quantity is " + iquantity + "order id is:" + orderID);
			}
		}

		// TODO Auto-generated method stub

	}

	private void updateitemfromdba(Message message4) throws SQLException, IOException {
		// TODO Auto-generated method stub
		resultSet = statement.executeQuery("select * from dbaDatabase Where itemName = '" + message4.itemname + "';");

		if (resultSet.next()) {
			String l = resultSet.getString("itemName");
			String f = resultSet.getString("Price");

			int selectedoption = JOptionPane
					.showConfirmDialog(null,
							" The Previous item details are: \n " + "\nItemNAme = " + l + " Price = " + f
									+ "\nDo you Still want to update with data from GUI?",
							"Choose", JOptionPane.YES_NO_OPTION);
			if (selectedoption == JOptionPane.YES_OPTION) {
				PreparedStatement preparedstatement = connection.prepareStatement(
						"UPDATE `dbaDatabase` SET Price = ? WHERE itemName = '" + message4.itemname + "';");
				preparedstatement.setString(1, message4.itemprice);

				preparedstatement.executeUpdate();
				JOptionPane.showMessageDialog(null, " Item " + message4.itemname + " is updated");

			}
		} else {
			JOptionPane.showMessageDialog(null, "Item with this name does not exist");

		}
		// readfromdba();

	}

	private void deleteitemfromdba(Message message5) throws SQLException, IOException {
		// TODO Auto-generated method stub

		String query1 = ("SELECT `itemName`, `Price` FROM `dbaDatabase` WHERE itemName = '" + message5.itemname + "';");
		resultSet = statement.executeQuery(query1);

		if (resultSet.next()) {
			String query = "DELETE FROM `dbaDatabase` WHERE itemName = '" + message5.itemname + "';";
			PreparedStatement preparedstatement = connection.prepareStatement(query);
			preparedstatement.execute();
			JOptionPane.showMessageDialog(null, "Item" + message5.itemname + " is deleted from the menu list");
		} else {

			JOptionPane.showMessageDialog(null, "item " + message5.itemname + " does not exist");

		}
		// readfromdba();

	}

	private void additemtodba(Message message3) throws SQLException, IOException {
		// TODO Auto-generated method stub
		// System.out.println("inside insert item to dbba" +
		// message3.toString());
		// - statement = connection.createStatement();

		resultSet = statement.executeQuery("select * from dbaDatabase Where itemName = '" + message3.itemname + "';");

		if (resultSet.next()) {
			JOptionPane.showMessageDialog(null, "Cannot insert because the menu item already is present "
					+ "\n Choose to update if you want to replace the information");
		} else {
			try {
				String query2 = "INSERT INTO `dbaDatabase` (`itemName`,`Price`) VALUES ('" + message3.itemname+ "','"
						 + message3.itemprice + "')";
				PreparedStatement preparedstatement1 = connection.prepareStatement(query2,
						Statement.RETURN_GENERATED_KEYS);
				preparedstatement1.execute();
				JOptionPane.showMessageDialog(null, "Menu record for item " + message3.itemname + " is inserted");

			} catch (Exception e) {
				System.out.println("e");
			}

		}
		// readfromdba();

	}

	private void existinglogin(Message message6) throws SQLException, IOException {
		// TODO Auto-generated method stub
		boolean flag111 = true;
		resultSet = statement.executeQuery("select * from details Where username = '" + message.username + "';");

		if (resultSet.next()) {
			if (resultSet.getString(2).equals(message6.password)) {
				flag111 = true;
			}else {
				JOptionPane.showMessageDialog(null, "The username " + message6.username + " is invalid");
				flag111 = false;
		} 
		}

		serverOutputStreamdata.writeBoolean(flag111);
	}

	private void readfromdba() throws SQLException, IOException {
		System.out.println("inside read dba++++++++++++++++++++++++++");
		ArrayList<ItemList> nameprice = new ArrayList<ItemList>();

		resultSet = statement.executeQuery("select * from dbaDatabase ");
		while (resultSet.next()) {
			item = new ItemList(resultSet.getString(2), resultSet.getString(3));
			nameprice.add(item);
			// System.out.println("list inside read from dba");
			// for(int i=0; i<nameprice.size();i++)
			// System.out.println(nameprice.get(i).itemname+nameprice.get(i).itemprice);

		}
		serverOutputStream.writeObject(nameprice);

	}

	private void insertnewaccount(Message message1) throws SQLException, IOException {
		// TODO Auto-generated method stub
		// System.out.println("inside insert" + message1.toString());
		// - statement = connection.createStatement();
		boolean flag4=true;

		resultSet = statement.executeQuery("select * from details Where username = '" + message.username + "';");

		if (resultSet.next()) {
			JOptionPane.showMessageDialog(null,
					"Cannot insert because username " + message1.username
							+ "already exists");
			flag4=false;
		} else {
			try {
				preparedstatement = connection.prepareStatement("INSERT INTO `Details` values (?,?,?,?,?,?,?)");
				preparedstatement.setString(1, message1.username);
				preparedstatement.setString(2, message1.password);
				preparedstatement.setString(3, message1.FirstName);
				preparedstatement.setString(4, message1.Lastname);
				preparedstatement.setString(5, message1.address);
				preparedstatement.setString(6, message1.email);
				preparedstatement.setInt(7, message1.telephone);


				preparedstatement.executeUpdate();
				JOptionPane.showMessageDialog(null,
						"Staff record for employee with ID " + message1.username + " is inserted");
				flag4=true;
			} catch (Exception e) {
				System.out.println("e");
			}
		}
		serverOutputStreamdata.writeBoolean(flag4);
	}

	public void initializeDB() {
		// loading drivers
		try {
			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("driverLoaded");

			// establish connection
			connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/pizzadatabase", "", "");
			System.out.println("Database connected\n");

			statement = connection.createStatement();

			System.out.println("database created");

			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS Details(username varchar(15) not null, password varchar(15), firstname varchar(15), lastname varchar(15),address varchar(30),primary key (username))");
			System.out.println("table created");

		} catch (SQLException e) {
			System.out.println("an error occured on table createion");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("mysql drivers were not found");
		}
	}

	// Inner class
	// Define the thread class for handling new connection
	class HandleAClient implements Runnable {
		private Socket sock; // A connected socket

		/** Construct a thread */
		public HandleAClient(Socket socket) {
			this.sock = socket;
		}

		/** Run a thread */
		public void run() {
			try {
				// Create data input and output streams
				serverInputStream = new ObjectInputStream(conn.getInputStream());
				serverOutputStream = new ObjectOutputStream(conn.getOutputStream());
				serverOutputStreamdata = new DataOutputStream(conn.getOutputStream());

				// Continuously serve the client
				while (true) {
					// Receive radius from the client
					// if(serverInputStream.readObject() instanceof Message)
					// {
					message = (Message) serverInputStream.readObject();
					// item = (ItemList) serverInputStream.readObject();

					// System.out.println(message.username.toString());
					receivingMessage(message);
					// System.out.println("in receiving
					// message"+message.itemlist.size());

					/*
					 * } else { ArrayList<ItemList> itmp = (ArrayList<ItemList>)
					 * serverInputStream.readObject(); }
					 */
					// message = new Message(username.toString(),"","","","");

				}
			} catch (IOException e) {
				System.err.println(e);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*
		 * public void closeAll() throws IOException, SQLException {
		 * serverInputStream.close(); serverOutputStream.close();
		 * resultSet.close(); statement.close(); connection.close();
		 * System.exit(0);
		 * 
		 * }
		 */
	}

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		int port = 8000;
		Server server = new Server(port);
	}

}
