import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;




public class ObjectViewFrame extends JFrame {
	
	

	private int objName,cellType=0,continNum;
	
	private String username,cusername,type,remark,name1,name2,forMap,continType;
	private Date cdate,date;
	
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel idLabel = new JLabel();
	private JLabel typeLabel = new JLabel();
	private JTextField typeTextField = new JTextField();
	
	private JLabel continNumLabel = new JLabel();
	private JButton changeContinButton = new JButton();
	
	private JLabel continName1Label = new JLabel();
	private JTextField continName1TextField= new JTextField(10);
	
	private JLabel continName2Label = new JLabel();
	private JTextField continName2TextField= new JTextField(10);
	

	
	private JLabel remarkLabel = new JLabel();
	private JTextField remarkTextField = new JTextField();
	
	private JLabel mapLabel = new JLabel();
	String[] endList = {"normal","to be continued","end","out of series","commissure R","commissure L","end?" };
	private JComboBox mapComboBox = new JComboBox(endList);
	

	private JLabel continTypeLabel = new JLabel();
	String[] continTypeList = {"neuron","muscle","gonad","hyp","unknown","socket","intestine"};
	private JComboBox continTypeComboBox = new JComboBox(continTypeList);
	
	private JLabel cellBodyLabel = new JLabel();
	private ButtonGroup cellBodyGroup = new ButtonGroup();
	private JLabel cellBodyYesLabel = new JLabel();
	private JRadioButton yesButton = new JRadioButton();
	private JLabel cellBodyNoLabel = new JLabel();
	private JRadioButton noButton = new JRadioButton();



	ImageDisplayPanel iframe;
	
	
	private JButton modButton = new JButton();
	private JButton cancelButton = new JButton();
	
	
	public ObjectViewFrame(int objN, String userN, ImageDisplayPanel iframe) {
		
		this.objName = objN;
		this.cusername = userN;
		this.iframe = iframe;
		cdate = new Date(System.currentTimeMillis());
		date = cdate;
		

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setTitle("View The Object Data");
		setSize(550, 500);
		JPanel p = new JPanel();
		p.setSize(400,400);
		p.setLayout(gridBagLayout1);
		JScrollPane scrollpane = new JScrollPane(p);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		Connection con = null;
		ResultSet rs = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			Statement stm = con.createStatement();
		
			rs = stm.executeQuery("SELECT type,CON_Number,cellType, username,DateEntered,OBJ_Remarks,forMap FROM object where OBJ_Name=" + objName);
			if (rs.next()) {
	            type = rs.getString("type");
	            continNum = rs.getInt("CON_Number");
	           	date = rs.getDate("DateEntered");
				username = rs.getString("username");
				cellType = rs.getInt("cellType");
				remark = rs.getString("OBJ_Remarks");
				forMap = rs.getString("forMap");
				if ((forMap==null) && ("".equals(forMap)) ) forMap="normal";
				
			}
			rs.close();
			
			rs = stm.executeQuery("SELECT CON_AlternateName,CON_AlternateName2,type FROM contin where CON_Number=" + continNum);
			if (rs.next()) {
	            name1 = rs.getString(1);
	            name2 = rs.getString(2);
	            continType = rs.getString(3);
	       
			}
			rs.close();
			
			
			
			
			
			
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
		
		idLabel.setText("Object ID: " + objName + "  by "+username+"  "+date);
		typeLabel.setText("type:");
	    typeTextField.setText(type);
	    continNumLabel.setText("Contin Number:"+continNum);
	    continName1Label.setText("AlternateName1");
	    continName2Label.setText("AlternateName2");
	    continName1TextField.setText(name1);
	    continName2TextField.setText(name2);
	    remarkLabel.setText("Remark:");
	    remarkTextField.setText(remark);
	    mapLabel.setText("For the Map:");
	    mapComboBox.setSelectedItem(forMap);
	    continTypeLabel.setText("contin type:");
	    continTypeComboBox.setSelectedItem(continType);
		cellBodyLabel.setText("Is CellBody?");
		cellBodyYesLabel.setText("Yes");
		cellBodyNoLabel.setText("No");

		idLabel.setBackground(new Color(206, 206, 230));

		modButton.setText("Modify");
		modButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modButton_actionPerformed(e);
			}
		});
		
		changeContinButton.setText("new contin number");
		changeContinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeContinButton_actionPerformed(e);
			}
		});
		
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButton_actionPerformed(e);
			}
		});
		
		

		final ItemListener cellBodyListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (yesButton.isSelected()) {
					cellType = 1;

				}
				if (noButton.isSelected()) {
					cellType = 0;

				}
				
			}

		};
		
		
		
		
		
		yesButton.addItemListener(cellBodyListener);
		noButton.addItemListener(cellBodyListener);
		
		if (cellType==1) {
			yesButton.setSelected(true);
		}
		
		if (cellType==0) {
			noButton.setSelected(true);
		}
		
		

		cellBodyGroup.add(yesButton);
		cellBodyGroup.add(noButton);
		
		
		p.add(
				idLabel,
				new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		
		p.add(
				typeLabel,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				typeTextField,
				new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));

		p.add(
				continNumLabel,
				new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				changeContinButton,
				new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		
		p.add(
				mapLabel,
				new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				mapComboBox,
				new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		
		p.add(
				remarkLabel,
				new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				remarkTextField,
				new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
		
		p.add(
				cellBodyLabel,
				new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				cellBodyYesLabel,
				new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				yesButton,
				new GridBagConstraints(2, 5, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		p.add(
				cellBodyNoLabel,
				new GridBagConstraints(3, 5, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				noButton,
				new GridBagConstraints(4, 5, 1, 1, 1.0, 1.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		
		p.add(
				continName1Label,
				new GridBagConstraints(0, 6, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				continName1TextField,
				new GridBagConstraints(2, 6, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		
		
		p.add(
				continName2Label,
				new GridBagConstraints(0, 7, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				continName2TextField,
				new GridBagConstraints(2, 7, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		
		p.add(
				continTypeLabel,
				new GridBagConstraints(0, 8, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				continTypeComboBox,
				new GridBagConstraints(2, 8, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));
		
	
		
		p.add(
				modButton,
				new GridBagConstraints(0, 9, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		p.add(
				cancelButton,
				new GridBagConstraints(2, 9, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 20, 0, 0), 0, 0));
		
		
		
		

	}
	
	



	
	
	private void cancelButton_actionPerformed(ActionEvent e) {
		this.dispose();
	}
	

	private void modButton_actionPerformed(ActionEvent e) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			PreparedStatement pst = null;
			
			pst = con.prepareStatement("UPDATE object set type = ?, "
							+ "CellType = ?, username = ?, "
							+ "DateEntered = ?, OBJ_Remarks=?, forMap=? WHERE OBJ_Name = ?");
			
			
			
			pst.setString(1, typeTextField.getText());
			
			pst.setInt(2, cellType);
			pst.setString(3, cusername);
			pst.setDate(4, cdate);
			pst.setInt(7, objName);
			pst.setString(5, remarkTextField.getText());
			pst.setString(6, (String) mapComboBox.getSelectedItem());
			if ( pst.executeUpdate()< 0) {
				JOptionPane.showMessageDialog(null, "Object Modify failed");
			    }
			pst = con.prepareStatement("UPDATE contin set CON_Alternatename = ?, "
					+ "CON_Alternatename2 = ?, type=? WHERE CON_Number = ?");
	
	
	
	        pst.setString(1, continName1TextField.getText());
	        pst.setString(2, continName2TextField.getText());
	        pst.setString(3, (String) continTypeComboBox.getSelectedItem());
	        pst.setInt(4, continNum);
	
	        if ( pst.executeUpdate()< 0) {JOptionPane.showMessageDialog(null, "Contin Alternatename Modify failed");}
			
			//JOptionPane.showMessageDialog(null, "Data Modify successfully");
			this.dispose();

			
			
			  
			
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
	}
	
	private void changeContinButton_actionPerformed(ActionEvent e) {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection(
					
					);

			PreparedStatement pst = null;
			
			int continN = Utilities.getNewContinNumber();
	           
            pst = con.prepareStatement ("insert into contin (CON_Number,CON_AlternateName) values("+continN+",'contin"+continN+"')");
	        pst.executeUpdate();
			pst.close();
	        pst = con.prepareStatement ("update object set CON_number="+continN+" where OBJ_Name = "+ objName);
	          
	
	        if ( pst.executeUpdate()< 0) {JOptionPane.showMessageDialog(null, "Contin Number Modify failed");}
			pst.close();
			//JOptionPane.showMessageDialog(null, "Data Modify successfully");
			this.dispose();
			

			
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			EDatabase.returnConnection(con);
		}

		
	}
	
	

}