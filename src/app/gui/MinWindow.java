package app.gui;

import java.awt.BasicStroke;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Polygon;
import java.awt.Stroke;

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
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.algorithms.RHull;
import app.model.algorithms.RHull.DP;
import app.model.data.SVMModel;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;

public class MinWindow 
		implements ActionListener, MouseListener, ChangeListener{

	//Model globals
	private static SVMModel model = null;
	
	//GUI globals
	private JFrame frame;
	private JFCPanel panel;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnClear;
	private JFCPanel pan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("start main");
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MinWindow window = new MinWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("exit main");
	}

	/**
	 * Create the application.
	 */
	public MinWindow() {
		//init Model
		
		model = new SVMModel();
		
		//init GUI
		initialize();
		btnClear.addActionListener(this);
	}
	
	private JFCPanel createChartPanel(){
		
		
		model.compute();
		
		
		XYSeries series1 = new XYSeries("Positive Class");
		series1.add(100.0, 100.0);
		series1.add(200.0, 400.0);
		series1.add(3.0, 3.0);
		series1.add(3.0, 3.0);
		series1.add(5.0, 5.0);
		series1.add(6.0, 7.0);
		series1.add(7.0, 7.0);
		series1.add(8.0, 8.0);
		XYSeries series2 = new XYSeries("Negative Class");
		series2.add(1.0, 5.0);
		series2.add(2.0, 7.0);
		series2.add(3.0, 6.0);
		series2.add(4.0, 8.0);
		series2.add(5.0, 4.0);
		series2.add(6.0, 4.0);
		series2.add(7.0, 2.0);
		series2.add(8.0, 1.0);
		series2.add(1.0, 5.0);

		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		JFreeChart chart = ChartFactory.createScatterPlot
		("XY Scatter Plot", "X", "Y", dataset);
		
		XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
 
		
		pan = new JFCPanel(chart);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(pan, popupMenu);
		
		JMenuItem mntmAddpoint = new JMenuItem("addPoint");
		mntmAddpoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.print("a");
			}
		});
		popupMenu.add(mntmAddpoint);
		
		JMenuItem mntmRemovepoint = new JMenuItem("removePoint");
		mntmRemovepoint.setEnabled(false);
		mntmRemovepoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.print("r");
			}
		});
		popupMenu.add(mntmRemovepoint);
		
		JMenuItem mntmChangeweight = new JMenuItem("changeWeight");
		mntmChangeweight.setEnabled(false);
		mntmChangeweight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.print("w");
			}
		});
		popupMenu.add(mntmChangeweight);
		return pan;
	}
	static XYShapeAnnotation anoHull = null;
	static XYShapeAnnotation anoRHull = null;
	
	private void findCH(){
		System.out.println("finding CH...");
		
		XYSeriesCollection sc = (XYSeriesCollection) pan.getChart().getXYPlot().getDataset();
		XYSeries s = sc.getSeries(0);
		

		
		model.setSeries1(s);
		model.compute();
		
		
		  final Shape[] shapes = new Shape[3];
		  
		  
	        int[] xPoints = new int[model.ch1.size()];
	        int[] yPoints =  new int[model.ch1.size()];
	        
	        for (int i = 0; i < model.ch1.size(); i++){
	        	xPoints[i] = (int) model.ch1.get(i).x;
	        	yPoints[i] = (int) model.ch1.get(i).y;
	      
	        }
	        shapes[0] = new Polygon(xPoints, yPoints, model.ch1.size());
	        XYPlot p = pan.getChart().getXYPlot();
	        if (anoHull != null){
	        	p.removeAnnotation(anoHull);
	        }
	        
	        anoHull = new XYShapeAnnotation(shapes[0]);
	        p.addAnnotation(anoHull);
    
    
		//DP[] res = RHull.rhull(points, 1.0);
		
		System.out.println("CH found");
	}
	
	private void findRCH(){
		System.out.println("finding RCH...");
		
		XYSeriesCollection sc = (XYSeriesCollection) pan.getChart().getXYPlot().getDataset();
		XYSeries s = sc.getSeries(0);
		

		
		model.setSeries1(s);
		double m1 = Double.parseDouble(textField.getText());
		model.setMu(m1, 0.5);
		model.compute();
		
		
		  final Shape[] shapes = new Shape[3];
		  
		  
	        int[] xPoints = new int[model.rch1.size()];
	        int[] yPoints =  new int[model.rch1.size()];
	        
	        for (int i = 0; i < model.rch1.size(); i++){
	        	xPoints[i] = (int) model.rch1.get(i).x;
	        	yPoints[i] = (int) model.rch1.get(i).y;
	      
	        }
	        shapes[0] = new Polygon(xPoints, yPoints, model.rch1.size());
	        XYPlot p = pan.getChart().getXYPlot();
	        if (anoRHull != null){
	        	p.removeAnnotation(anoRHull);
	        }
	        anoRHull = new XYShapeAnnotation(shapes[0],
	        		new BasicStroke(2.0f), Color.blue);
	        //anoRHull.
	        p.addAnnotation(anoRHull);
    
    
		//DP[] res = RHull.rhull(points, 1.0);
		
		System.out.println("RCH found");
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
		
		panel = createChartPanel();
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
				model.mu1 = (double)j.getValue()/100;
				System.out.println("actionPerformed " + model.mu1 + arg0.getActionCommand());
				//panel.setMu(mu1,mu2);
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
				model.mu2 = (double)j.getValue()/100;
				System.out.println("actionPerformed " + model.mu2 + arg0.getActionCommand());
				//panel.setMu(mu1,mu2);
			}
		});
		textField_1.setText("0.5");
		springLayout.putConstraint(SpringLayout.NORTH, textField_1, 1, SpringLayout.NORTH, rdbtnClass_1);
		springLayout.putConstraint(SpringLayout.WEST, textField_1, 0, SpringLayout.WEST, textField);
		textField_1.setColumns(10);
		frame.getContentPane().add(textField_1);
		
		btnClear = new JButton("Random Data");
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
		springLayout.putConstraint(SpringLayout.WEST, btnClear, 0, SpringLayout.WEST, panel_1);
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 33, SpringLayout.SOUTH, slider_1);
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 20, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.EAST, panel_1, -34, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(panel_1);
		
		JButton btnNewButton = new JButton("find CH");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				findCH();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 374, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -6, SpringLayout.NORTH, btnNewButton);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 17, SpringLayout.EAST, pan);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("find RCH");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findRCH();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_1, 6, SpringLayout.SOUTH, panel_1);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_1, 6, SpringLayout.EAST, btnNewButton);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Solve");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_2, 6, SpringLayout.SOUTH, btnNewButton);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton_2, 0, SpringLayout.WEST, panel_1);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Clear");
		springLayout.putConstraint(SpringLayout.NORTH, btnClear, 0, SpringLayout.NORTH, btnNewButton_3);
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton_3, 35, SpringLayout.SOUTH, btnNewButton_1);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton_3, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btnNewButton_3);
		
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
			
			//initializeData();
			
			panel.setBackground(Color.WHITE);
			//
			//panel.setForeground(Color.WHITE);
			
			//panel.getGraphics().fillRect(0, 0, panel.getWidth(), panel.getHeight());
			//panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());
			panel.getGraphics().drawRect(0, 0, 200, 200);
			panel.repaint();
		}
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
