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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.algorithms.RCH;
import app.model.data.SVMDataItem;
import app.model.data.SVMModel;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

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
	JLabel lblNewLabel_1 = new JLabel("New label");
	JLabel lblNewLabel_2 = new JLabel("New label");
	
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
		series1.add(1.0, 1.0);
		series1.add(2.0, 4.0);
//		series1.add(3.0, 3.0);
//		series1.add(3.0, 3.0);
//		series1.add(5.0, 5.0);
//		series1.add(6.0, 7.0);
//		series1.add(1.0, 7.0);
//		series1.add(4.0, 2.0);
		
		series1.add(new SVMDataItem(7.5, 7.5));
		series1.add(new SVMDataItem(8, 4));
		XYSeries series2 = new XYSeries("Negative Class");
		series2.add(1.0, 5.0);
		series2.add(2.0, 7.0);
		series2.add(3.0, 6.0);
		series2.add(4.0, 8.0);
//		series2.add(5.0, 4.0);
//		series2.add(6.0, 4.0);
//		series2.add(7.0, 2.0);
//		series2.add(8.0, 1.0);
//		series2.add(1.0, 5.0);

		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(series1);
		dataset.addSeries(series2);

		JFreeChart chart = ChartFactory.createScatterPlot
		("XY Scatter Plot", "X", "Y", dataset);
		
		XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) plot.getRenderer();
        return new JFCPanel(chart, model);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 721, 538);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
	    labelTable.put(new Integer(100), new JLabel("1.0"));  
	    labelTable.put(new Integer(75), new JLabel("0.75"));  
	    labelTable.put(new Integer(50), new JLabel("0.50"));  
	    labelTable.put(new Integer(25), new JLabel("0.25"));  
	    labelTable.put(new Integer(0), new JLabel("0.0"));  
		frame.getContentPane().setLayout(null);
		
		JPanel pContainer1 = new JPanel();
		pContainer1.setBounds(10, 130, 555, 326);
		frame.getContentPane().add(pContainer1);
        

        pContainer1.setLayout(new BorderLayout(0, 0));
        
//        rr.setSeriesLinesVisible(0, true);
//        rr.setDrawSeriesLineAsPath(true);
        

        
        panel = createChartPanel();
        pContainer1.add(panel);
        panel.setBackground(Color.WHITE);
		
		JPanel pContainer2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pContainer2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pContainer2.setBounds(0, 0, 687, 119);
		frame.getContentPane().add(pContainer2);
		
		JPanel pClass1 = new JPanel();
		pContainer2.add(pClass1);
		
		JRadioButton rdbtnClass = new JRadioButton("Class 1");
		pClass1.add(rdbtnClass);
		
		JLabel label = new JLabel("Select Property");
		pClass1.add(label);
		
		JComboBox comboBox = new JComboBox();
		pClass1.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"mu"}));
		
		JSlider slider = new JSlider();
		pClass1.add(slider);
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setLabelTable( labelTable );  
		slider.setPaintLabels(true);
		
		JLabel lblValue = new JLabel("Value");
		pClass1.add(lblValue);
		
		textField = new JTextField();
		pClass1.add(textField);
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
		textField.setColumns(10);
		pClass1.add(lblNewLabel_1);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				textField.setText(String.valueOf((double) j.getValue() / 100));
				lblNewLabel_1.setText((1 / Double.parseDouble(textField.getText()))  + "");
				panel.findRCH();

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
		
		JPanel pClass2 = new JPanel();
		pContainer2.add(pClass2);
		
		JRadioButton rdbtnClass_1 = new JRadioButton("Class 2");
		pClass2.add(rdbtnClass_1);
		
		JLabel lblNewLabel = new JLabel("Select Property");
		pClass2.add(lblNewLabel);
		
		JComboBox comboBox_1 = new JComboBox();
		pClass2.add(comboBox_1);
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"mu"}));
		
		JSlider slider_1 = new JSlider();
		pClass2.add(slider_1);
		slider_1.setLabelTable( labelTable );  
		slider_1.setPaintLabels(true);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				textField_1.setText(String.valueOf((double) j.getValue() / 100));
				lblNewLabel_2.setText((1 / Double.parseDouble(textField.getText()))  + "");
				panel.findRCH();
				
				
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
		
		JLabel label_1 = new JLabel("Value");
		pClass2.add(label_1);
		
		textField_1 = new JTextField();
		pClass2.add(textField_1);
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				model.mu2 = (double)j.getValue()/100;
				System.out.println("actionPerformed " + model.mu2 + arg0.getActionCommand());
				//panel.setMu(mu1,mu2);
			}
		});
		textField_1.setText("0.5");
		textField_1.setColumns(10);
		pClass2.add(lblNewLabel_2);
		
		JPanel pContainer3 = new JPanel();
		pContainer3.setBounds(575, 299, 112, 157);
		frame.getContentPane().add(pContainer3);
		
		btnClear = new JButton("Random Data");
		pContainer3.add(btnClear);
		
		JButton btnNewButton = new JButton("find CH");
		pContainer3.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("find RCH");
		pContainer3.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Solve");
		pContainer3.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Clear");
		pContainer3.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.dataset1.clear();
			}
		});
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.solveSVM();
			}
		});
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.findRCH();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				panel.findCH();
			}
		});
		
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
}
