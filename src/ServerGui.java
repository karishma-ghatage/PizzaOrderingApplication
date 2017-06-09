
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class ServerGui extends JPanel{
	

		private JButton btnCloseButton;
		public JTextArea txtDisplayResults;
		Server connection;
		
		public ServerGui(Server connection) 
		{
	        this.connection = connection;
			initializeComponents();
			initializeLayout();
			
			btnCloseButton.addActionListener( new java.awt.event.ActionListener() {
		        public void actionPerformed(ActionEvent e){
		        	try {
						close_actionPerformed(e);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		            }
		    });
			
		}

		
		private void initializeComponents()
		{
			txtDisplayResults = new JTextArea(20, 40);
			btnCloseButton = new JButton("Close");
			txtDisplayResults.setEditable(false);
		}

		
		private void initializeLayout() {
			
			JPanel displayPanel = new JPanel();
			JPanel buttonPanel = new JPanel();

			displayPanel.setLayout( new BorderLayout());
			displayPanel.add( new JLabel( "Client Request:" ), BorderLayout.NORTH );
			displayPanel.add(txtDisplayResults, BorderLayout.CENTER);
			
			buttonPanel.setLayout( new FlowLayout());
			buttonPanel.add(btnCloseButton);
			
			JScrollPane scroll = new JScrollPane ( txtDisplayResults );
		    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		    displayPanel.add(scroll);

			setLayout(new BorderLayout());
			add(displayPanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);

		}

		
		void close_actionPerformed(ActionEvent e) throws IOException{
		    
			System.exit(0);
			  
		  }
		
	}


