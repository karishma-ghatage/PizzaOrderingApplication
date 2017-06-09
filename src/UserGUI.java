

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class UserGUI extends JPanel {
	

	
	 

	  private JButton jubCustomer;
	  private JButton jubChef;
	  private JButton jubDBA;
	  
	
	  
	 
	  
	  
	  public UserGUI() throws IOException{
		  
	

	    initGUI();
	    doTheLayout();
	    
	 
	    jubCustomer.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	        	
	        		
	        	}
	        	
	            }
	    );
	    
	    jubChef.addActionListener( new java.awt.event.ActionListener() {
	    	  public void actionPerformed(ActionEvent e){
	    		  int year=0, month=0, day=0;
	        	
	        JOptionPane.showMessageDialog(null, "still there");
		        		
		        	}
	                }
	        	
	            
	    );
	    
	    jubDBA.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	            close();
	            }
	    });
	    
	    
			
	  

	  } // end of constructor

	

	 
	private void initGUI(){
		  
		
	      
	      jubCustomer = new JButton("Customer");
	      jubChef = new JButton("Chef");
	      jubDBA = new JButton("DBA");
	   
	      
	     // field2.setEditable(false);

	  }// end of creating objects method

	  private void doTheLayout(){
		  
	     
	      JPanel header1 = new JPanel();
	      JPanel West = new JPanel();
	      JPanel bottom = new JPanel();
	      JPanel Center = new JPanel();
	      JPanel W1 = new JPanel();
	     
	      JPanel East = new JPanel();
	      
	      JPanel but = new JPanel();
	    //  JPanel bottom = new JPanel();
	     
	   
	     
	      
	     
	    
	      but.setLayout( new FlowLayout());
	      but.add(jubChef);
	      but.add(jubCustomer);
	      but.add(jubDBA);
	      
	    
	  
	      bottom.setLayout( new BorderLayout());
	      bottom.add(but, BorderLayout.NORTH);
	      
	      
	      setLayout( new BorderLayout());
	      
	      add(header1, BorderLayout.NORTH);
	      add(West, BorderLayout.WEST);
	      add(East, BorderLayout.EAST);
	    add(Center, BorderLayout.CENTER);
	    add(bottom, BorderLayout.SOUTH);

	  }// end of Layout method

	
	 

	  void close(){
	      System.exit(0);
	  };
	
  

	public static void main(String[] args) throws IOException {
		JFrame f = new JFrame("GUI FOR LOGIN");
		f.setSize(1000, 800);
	    f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		
		Container contentPane = f.getContentPane();
		contentPane.add(new UserGUI());
		f.pack();
	
		
		

		
	}  }


	  
