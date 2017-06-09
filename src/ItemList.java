import java.io.Serializable;

public class ItemList implements Serializable {
//  serialVersionUID = -1415588960024803904;
		
	private static final long serialVersionUID = -7556377388518732169L;
	public String itemname;
	public String itemprice;
	public int op_type;
	public int quantity;
	public int orderid;

	
	

	public ItemList(String itemname, String itemprice ){
		this.itemname=itemname;
		this.itemprice=itemprice;
	//	this.op_type=op_type;
		
		
	}
	
	public ItemList(String itemname, String itemprice,int quantity ){
		this.itemname=itemname;
		this.itemprice=itemprice;
		this.quantity = quantity;
	//	this.op_type=op_type;
	}
	public ItemList(String itemname, int quantity,int orderid ){
		this.itemname=itemname;
		this.itemprice=itemprice;
		this.quantity = quantity;
		this.orderid = orderid;
	//	this.op_type=op_type;
	}
	

}
