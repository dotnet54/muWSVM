package app.gui;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class MinWindow 
		implements ActionListener, MouseListener, ChangeListener{

	//Model globals
	
	private static double mu1 = 0.5;
	private static double mu2 = 0.5;
	private static int max = 10;	
	private static ArrayList<Point> dataset1 = new ArrayList<Point>();
	private static ArrayList<Point> dataset2 = new ArrayList<Point>();
	
	private static Point[] points;
	private static Random randomGenerator = new Random();
	
	
	//GUI globals
	private JFrame frame;
	private DrawPanel panel;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnClear;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MinWindow window = new MinWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MinWindow() {
		initializeData();
		initialize();
		addListeners();
		
		
		
		
		
		
	}
	
	/**
	 * Do computation and  visualization
	 * 
	 */
	
	private void performComputations(){
		
		
	}
	
	/**
	 * initialize global data
	 * 
	 */
	
	private void initializeData(){
		points = new Point[max];
		points[0] = new Point(0,0);
		points[1] = new Point(100,0);
		points[2] = new Point(0,100);

		dataset1.clear();
		dataset2.clear();
		
		dataset1.add(points[0]);
		dataset1.add(points[1]);
		dataset1.add(points[2]);

		points[0] = new Point(100,100);
		points[1] = new Point(200,100);
		points[2] = new Point(100,200);
		
		dataset2.add(points[0]);
		dataset2.add(points[1]);
		dataset2.add(points[2]);

		dataset1.clear();
		dataset2.clear();
		
		for (int i = 0; i < max; i++){
			Point p = new Point();
			p.setLocation(randomGenerator.nextInt(250), randomGenerator.nextInt(200)); //randomGenerator.nextInt(400);
			dataset1.add(p);
		}
		
		for (int i = 0; i < max; i++){
			Point p = new Point();
			p.setLocation(randomGenerator.nextInt(250)+200, randomGenerator.nextInt(200)+50); //randomGenerator.nextInt(400);
			dataset2.add(p);
		}
	}
	
	
	/**
	 * Registers event listeners to components
	 * 
	 */
	
	private void addListeners(){
		btnClear.addActionListener(this);
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 721, 538);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		panel = new DrawPanel(dataset1, dataset2, mu1, mu2);
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -2, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, -199, SpringLayout.EAST, frame.getContentPane());
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel);
		
		
	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
	    labelTable.put(new Integer(100), new JLabel("1.0"));  
	    labelTable.put(new Integer(75), new JLabel("0.75"));  
	    labelTable.put(new Integer(50), new JLabel("0.50"));  
	    labelTable.put(new Integer(25), new JLabel("0.25"));  
	    labelTable.put(new Integer(0), new JLabel("0.0"));  
		
		JSlider slider = new JSlider();
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
	    slider.setLabelTable( labelTable );  
	    slider.setPaintLabels(true); 
		springLayout.putConstraint(SpringLayout.EAST, slider, -40, SpringLayout.EAST, frame.getContentPane());
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				textField.setText(String.valueOf((double) j.getValue() / 100));

				for (ActionListener a : textField.getActionListeners()) {
					a.actionPerformed(new ActionEvent(j,
							ActionEvent.ACTION_PERFORMED, " slider 1 changed") {
								private static final long serialVersionUID = 1L;
						// Nothing need go here, the actionPerformed method
						// (with the
						// above arguments) will trigger the respective listener
					});
				}

			}

		});
		frame.getContentPane().add(slider);
		
		textField = new JTextField();
		textField.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println("propertyChange");
			}
		});
		textField.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
				System.out.println("inputMethodTextChanged");
			}
			public void inputMethodTextChanged(InputMethodEvent arg0) {
				System.out.println("inputMethodTextChanged");
			}
		});
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				mu1 = (double)j.getValue()/100;
				System.out.println("actionPerformed " + mu1 + arg0.getActionCommand());
				panel.setMu(mu1,mu2);
			}
		});
		textField.setText("0.5");
		springLayout.putConstraint(SpringLayout.NORTH, slider, 0, SpringLayout.NORTH, textField);
		springLayout.putConstraint(SpringLayout.WEST, slider, 6, SpringLayout.EAST, textField);
		springLayout.putConstraint(SpringLayout.NORTH, textField, 13, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, 13, SpringLayout.NORTH, frame.getContentPane());
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"mu"}));
		frame.getContentPane().add(comboBox);
		
		JRadioButton rdbtnClass = new JRadioButton("Class 1");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnClass, 12, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, rdbtnClass, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(rdbtnClass);
		
		JRadioButton rdbtnClass_1 = new JRadioButton("Class 2");
		springLayout.putConstraint(SpringLayout.NORTH, panel, 84, SpringLayout.SOUTH, rdbtnClass_1);
		springLayout.putConstraint(SpringLayout.SOUTH, rdbtnClass_1, 27, SpringLayout.SOUTH, rdbtnClass);
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnClass_1, 4, SpringLayout.SOUTH, rdbtnClass);
		springLayout.putConstraint(SpringLayout.WEST, rdbtnClass_1, 0, SpringLayout.WEST, rdbtnClass);
		frame.getContentPane().add(rdbtnClass_1);
		
		JLabel lblNewLabel = new JLabel("Select Property");
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 8, SpringLayout.EAST, lblNewLabel);
		springLayout.putConstraint(SpringLayout.EAST, comboBox, 117, SpringLayout.EAST, lblNewLabel);
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 16, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 6, SpringLayout.EAST, rdbtnClass);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("Select Property");
		springLayout.putConstraint(SpringLayout.NORTH, label, 18, SpringLayout.SOUTH, lblNewLabel);
		springLayout.putConstraint(SpringLayout.WEST, label, 6, SpringLayout.EAST, rdbtnClass_1);
		springLayout.putConstraint(SpringLayout.SOUTH, label, 32, SpringLayout.SOUTH, lblNewLabel);
		frame.getContentPane().add(label);
		
		JComboBox comboBox_1 = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, comboBox_1, 6, SpringLayout.SOUTH, comboBox);
		springLayout.putConstraint(SpringLayout.WEST, comboBox_1, 8, SpringLayout.EAST, label);
		springLayout.putConstraint(SpringLayout.EAST, comboBox_1, 117, SpringLayout.EAST, label);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"mu"}));
		frame.getContentPane().add(comboBox_1);
		
		JLabel lblValue = new JLabel("Value");
		springLayout.putConstraint(SpringLayout.NORTH, lblValue, 16, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textField, 6, SpringLayout.EAST, lblValue);
		springLayout.putConstraint(SpringLayout.WEST, lblValue, 6, SpringLayout.EAST, comboBox);
		frame.getContentPane().add(lblValue);
		
		JLabel label_1 = new JLabel("Value");
		springLayout.putConstraint(SpringLayout.NORTH, label_1, 4, SpringLayout.NORTH, rdbtnClass_1);
		springLayout.putConstraint(SpringLayout.WEST, label_1, 6, SpringLayout.EAST, comboBox_1);
		frame.getContentPane().add(label_1);
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				mu2 = (double)j.getValue()/100;
				System.out.println("actionPerformed " + mu2 + arg0.getActionCommand());
				panel.setMu(mu1,mu2);
			}
		});
		textField_1.setText("0.5");
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 1, SpringLayout.NORTH, rdbtnClass_1);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, textField);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		btnClear = new JButton("Random");
		springLayout.putConstraint(SpringLayout.SOUTH, btnClear, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnClear, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btnClear);
		
		JSlider slider_1 = new JSlider();
		slider_1.setLabelTable( labelTable );  
		slider_1.setPaintLabels(true); 
		springLayout.putConstraint(SpringLayout.NORTH, slider_1, 72, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, slider_1, 6, SpringLayout.EAST, textField_1);
		springLayout.putConstraint(SpringLayout.SOUTH, slider_1, -6, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.EAST, slider_1, 0, SpringLayout.EAST, slider);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				textField_1.setText(String.valueOf((double) j.getValue() / 100));

				for (ActionListener a : textField_1.getActionListeners()) {
					a.actionPerformed(new ActionEvent(j,
							ActionEvent.ACTION_PERFORMED, " slider 2 changed") {
								private static final long serialVersionUID = 1L;
						// Nothing need go here, the actionPerformed method
						// (with the
						// above arguments) will trigger the respective listener
					});
				}

			}
		});
		slider_1.setPaintLabels(true);
		slider_1.setPaintTicks(true);
		frame.getContentPane().add(slider_1);
		
		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 33, SpringLayout.SOUTH, slider_1);
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 20, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -24, SpringLayout.NORTH, btnClear);
		springLayout.putConstraint(SpringLayout.EAST, panel_1, -34, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(panel_1);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open File");
		mnFile.add(mntmOpenFile);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JCheckBoxMenuItem chckbxmntmDisplayLabels = new JCheckBoxMenuItem("Display Labels");
		mnView.add(chckbxmntmDisplayLabels);
		
		JMenu mnData = new JMenu("Data");
		menuBar.add(mnData);
		
		JMenuItem mntmDataViewer = new JMenuItem("Data Viewer");
		mnData.add(mntmDataViewer);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpContent = new JMenuItem("Help Content");
		mnHelp.add(mntmHelpContent);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getSource().toString());
		if (e.getSource().equals(btnClear)){
			
			initializeData();
			
			panel.setBackground(Color.WHITE);
			//
			//panel.setForeground(Color.WHITE);
			
			//panel.getGraphics().fillRect(0, 0, panel.getWidth(), panel.getHeight());
			//panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());
			panel.getGraphics().drawRect(0, 0, 200, 200);
			panel.repaint();
		}
	}
}
