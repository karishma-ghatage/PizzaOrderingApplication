

	import java.io.Serializable;
import java.util.ArrayList;


	public class Message implements Serializable {
		
		public String username;
		public String password;
		public String  address;
		public String FirstName;
		public String Lastname;
		public int op_type;
		public String itemname;
		public String itemprice;
		public ArrayList<?> itemlist;
		public double total ;
		public String cardnumber;
		public String expdate;
		public int orderid;
		public String email;
		public int telephone;
		
		@Override
		public String toString() {
			return "Message [username=" + username + ", password=" + password + ", address=" + address + ", FirstName="
					+ FirstName + ", Lastname=" + Lastname + ", op_type=" + op_type + "]";
		}

		public Message (String username, String password, String  firstname, String lastname, String address ,int op_type,String itemname, String itemprice){
			this.username = username;
		    this.password = password;
		    this.address = address;
			this.FirstName = firstname;
			this.Lastname = lastname;
			this.op_type = op_type;
			this.itemname = itemname;
			this.itemprice = itemprice;
		}
		
		public Message (String username, String password, String  firstname, String lastname, String address ,int op_type,String itemname, String itemprice, ArrayList<?> arrayList){
			this.username = username;
		    this.password = password;
		    this.address = address;
			this.FirstName = firstname;
			this.Lastname = lastname;
			this.op_type = op_type;
			this.itemname = itemname;
			this.itemprice = itemprice;
			this.itemlist = arrayList;
		}
		public Message (String username, String password, String  firstname, String lastname, String address ,int op_type,String itemname, String itemprice,ArrayList<?> arrayList,double total,String cardnumber,String expdate){
			this.username = username;
		    this.password = password;
		    this.address = address;
			this.FirstName = firstname;
			this.Lastname = lastname;
			this.op_type = op_type;
			this.itemname = itemname;
			this.itemprice = itemprice;
			this.total = total;
			this.cardnumber = cardnumber;
			this.expdate = expdate;
		}
		public Message (int orderid, int op_type){
			this.orderid = orderid;
			this.op_type = op_type;
		}
		
		public Message (String username, int op_type){
			this.username = username;
			this.op_type = op_type;
		}
		
		public Message (String username, String password, String  firstname, String lastname, String address ,int op_type,String email, int telephone){
			this.username = username;
		    this.password = password;
		    this.address = address;
			this.FirstName = firstname;
			this.Lastname = lastname;
			this.op_type = op_type;
			this.email = email;
			this.telephone = telephone;
		}
		
		
		
		
		

	}


