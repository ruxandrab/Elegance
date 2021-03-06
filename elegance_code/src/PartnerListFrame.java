import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;



public class PartnerListFrame
	extends JFrame
{
	
	private GridBagLayout gridBagLayout1 = new GridBagLayout(  );
	private JLabel        nameLabel = new JLabel(  );
	private JTextField    nameBox       = new JTextField(  );
	private JButton       ok          = new JButton(  );
	private JButton       close       = new JButton(  );
	private JButton       save       = new JButton(  );
	private JScrollPane   spane;
	private JTable        table       = new JTable(  );
	private Vector  tableData   = new Vector(  );
	private Vector tableHeaders = new Vector(  );
	private String name = "";

	/**
	 * Creates a new ImageSearchFrame object.
	 */
	public PartnerListFrame (  )
	{
		try
		{
			jbInit (  );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}

	private void jbInit (  )
		throws Exception
	{
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		this.setDefaultCloseOperation ( JFrame.HIDE_ON_CLOSE );
		this.setSize ( new Dimension( 300, 100 ) );
		this.setTitle ( "Partner List" );
		nameLabel.setText("Input the neuron name or contin#:");
		ok.setText ( "go" );
		ok.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					ok_actionPerformed ( e );
				}
			}
		 );
		
		save.setText ( "save as CSV file (opened by Excel)" );
		save.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					save_actionPerformed ( e );
				}
			}
		 );
		
		close.setText ( "Close" );
		close.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					close_actionPerformed ( e );
				}
			}
		 );
		
		this.getContentPane (  ).add ( 
		    nameLabel,
		    new GridBagConstraints( 
		        0,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		
		this.getContentPane (  ).add ( 
		    nameBox,
		    new GridBagConstraints( 
		        1,
		        0,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.HORIZONTAL,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
	
		this.getContentPane (  ).add ( 
		    ok,
		    new GridBagConstraints( 
		        0,
		        3,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		this.getContentPane (  ).add ( 
		    close,
		    new GridBagConstraints( 
		        1,
		        3,
		        1,
		        1,
		        0.0,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        15,
		        0
		     )
		 );
	}

	
	public int performSearch (  )
	{
		
		String  jsql   = "",jsql1="";
		
		String pre="",post="",type="",number="",sections="";
		

		if ( ( nameBox.getText (  ) == null ) || ( nameBox.getText (  ).length (  ) == 0 ) )
		{
			JOptionPane.showMessageDialog ( 
			    null,
			    "Please enter the name or contin#",
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			return -1;
		}
		
		Connection con = null;
		PreparedStatement pst=null;
		ResultSet         rs=null;
		name = Utilities.getNameFromWhatever(nameBox.getText (  ));

		try
		{
			
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );
			
			jsql= "select type,pre,post,count(*),sum(sections) as sects, concat(pre,post) as name " +
					"from synapsenomultiple where ( pre = ? or pre=concat('[',?,']') " +
					"or post=concat('[',?,']') or post=? ) " +
					"and type = 'electrical' group by name order by sects desc";

			pst = con.prepareStatement ( jsql );
			pst.setString(1, name);
			pst.setString(2, name);
			pst.setString(3, name);
			pst.setString(4, name);
			rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				type = rs.getString(1);
				pre = name;
				post = rs.getString(3);
			    if(post.equals(name)) post=rs.getString(2);
			    number =  rs.getString(4);
			    sections = rs.getString(5);

				
				
				
				Vector v = new Vector(  );
				
				
				v.addElement ( type );
				v.addElement ( pre );
				v.addElement ( post );
				v.addElement(number);
				v.addElement ( sections );
              
				tableData.addElement ( v );
			}
			rs.close();
			pst.close();
			
			jsql= "select type,post,count(*),sum(sections) as sects " +
					"from synapsenomultiple where ( pre = ? " +
					"or pre=concat('[',?,']') )  " +
					"and type = 'chemical' group by post order by sects desc";

			pst = con.prepareStatement ( jsql );
			pst.setString(1, name);
			pst.setString(2, name);
			rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				type = rs.getString(1);
				pre = name;
				post = rs.getString(2);
			    number =  rs.getString(3);
			    sections = rs.getString(4);

				
				
				
				Vector v = new Vector(  );
				
				
				v.addElement ( type );
				v.addElement ( pre );
				v.addElement ( post );
				v.addElement(number);
				v.addElement ( sections );
                
				tableData.addElement ( v );
			}
			rs.close();
			pst.close();
			
			
			jsql= "select type,pre,count(*),sum(sections) as sects " +
			"from synapsenomultiple where ( post = ? " +
			"or post=concat('[',?,']') )  " +
			"and type = 'chemical' group by pre order by sects desc";

	pst = con.prepareStatement ( jsql );
	pst.setString(1, name);
	pst.setString(2, name);
	rs = pst.executeQuery (  );

	while ( rs.next (  ) )
	{
		type = rs.getString(1);
		post = name;
		pre = rs.getString(2);
	    number =  rs.getString(3);
	    sections = rs.getString(4);

		
		
		
		Vector v = new Vector(  );
		
		
		v.addElement ( type );
		v.addElement ( pre );
		v.addElement ( post );
		v.addElement(number);
		v.addElement ( sections );
                
				tableData.addElement ( v );
				
			}
			rs.close();
			pst.close();
			

		
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );

			JOptionPane.showMessageDialog ( 
			    null,
			    ex.getMessage (  ),
			    "Error",
			    JOptionPane.ERROR_MESSAGE
			 );

			

			return -1;
		}finally {
			EDatabase.returnConnection(con);
		}

		
		

		tableHeaders.addElement ( "type" );
		tableHeaders.addElement ( "presynaptical" );
		tableHeaders.addElement ( "postsynaptical" );
		tableHeaders.addElement ( "synapses#" );
        tableHeaders.addElement ( "sections#" );
       
       

        MyTableModel model = new MyTableModel( tableData, tableHeaders );

		table.setModel ( model );
		spane =
			new JScrollPane( 
			    table,
			    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			 );

		this.getContentPane (  ).removeAll (  );
		this.setSize ( new Dimension( 800, 600 ) );
		this.getContentPane (  ).setLayout ( new GridBagLayout(  ) );
		this.getContentPane (  ).add ( 
		    spane,
		    new GridBagConstraints( 
		        0,
		        0,
		        3,
		        1,
		        1.0,
		        1.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.BOTH,
		        new Insets( 0, 0, 0, 0 ),
		        0,
		        0
		     )
		 );
		
		this.getContentPane (  ).add ( 
		    close,
		    new GridBagConstraints( 
		        2,
		        1,
		        1,
		        1,
		        0.1,
		        0.0,
		        GridBagConstraints.CENTER,
		        GridBagConstraints.NONE,
		        new Insets( 0, 0, 0, 0 ),
		        15,
		        0
		     )
		 );
		
		
		this.getContentPane (  ).add ( 
			    save,
			    new GridBagConstraints( 
			        1,
			        1,
			        1,
			        1,
			        0.1,
			        0.0,
			        GridBagConstraints.CENTER,
			        GridBagConstraints.NONE,
			        new Insets( 0, 0, 0, 0 ),
			        15,
			        0
			     )
			 );

		
		repaint (  );
		setVisible ( true );

		return 1;
	}

	private void close_actionPerformed ( ActionEvent e )
	{
		this.hide (  );
	}

	private void ok_actionPerformed ( ActionEvent e )
	{
		performSearch (  );
	}
	
	private void save_actionPerformed ( ActionEvent e )
	{
		saveExcel();
	}
	
	public class CSVFileFilter extends javax.swing.filechooser.FileFilter
	{
	
	  public boolean accept(File file)
	  {
	   
	      if (file.getName().toLowerCase().endsWith("csv"))
	      {
	        return true;
	      }
	   return false;
	  }

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	}



	private void saveExcel()
	{
		try
		{
			
	     
			JFileChooser chooser = new JFileChooser(".");
			
		    chooser.addChoosableFileFilter(new CSVFileFilter());
			
			chooser.showSaveDialog(this); 
			
			File file = chooser.getSelectedFile(); 
			
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			PrintWriter fileWriter = new PrintWriter(bufferedWriter);

			for(int i=0; i<tableHeaders.size(); ++i)
			{
				String s = '"'+tableHeaders.elementAt(i).toString()+'"'+",";
				fileWriter.print(s);
			}
			fileWriter.println("\n");
			for(int i=0; i<table.getRowCount(); ++i)
			{
				for(int j=0; j<table.getColumnCount(); ++j)
				{
					String s = '"'+table.getValueAt(i,j).toString()+'"'+",";
					fileWriter.print(s);
				}
				fileWriter.println("\n");
				fileWriter.flush();
			}	
			fileWriter.close();
			JOptionPane.showMessageDialog(null, "Success");
		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Failure");
		}

	}
	
	
	public JTable getTable (  )
	{
		return table;
	}
	
	 public static void main(String[] args) 
	  {
	    try
	    {
	      (new PartnerListFrame()).setVisible(true);
	      
	      
	         
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	   
	  }

	

}
