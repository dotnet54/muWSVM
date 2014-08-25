package app.gui;

import java.awt.BasicStroke;
import java.awt.EventQueue;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;

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
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MinWindow 
		implements ActionListener, MouseListener, ChangeListener{

	//Model globals
	private static SVMModel model = null;
	XYSeriesCollection dataset = new XYSeriesCollection();
	XYSeries series1 = new XYSeries("Positive Class");
	XYSeries series2 = new XYSeries("Negative Class");	
	XYSeries series3 = new XYSeries("Positive WRCH");
	

	
	//GUI globals
	private JFrame frame;
	private JFCPanel panel;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnClear;
	JLabel lblNewLabel_1 = new JLabel("1");
	JLabel lblNewLabel_2 = new JLabel("1");
	
	

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
		
		initializeData();
		
		//init GUI
		initialize();
		btnClear.addActionListener(this);
	}
	
	public void addRandomData(){
		clearPlot();
		
		//TODO use generator and set seed
		Random r = new Random(System.currentTimeMillis());
		
		int max  = (int) (r.nextDouble() * 10);
		
		for (int i = 0; i < max; i++ ){
			double randX  =  (r.nextDouble() * 10);
			double randY  =  (r.nextDouble() * 10);
			series1.add(new SVMDataItem(randX, randY));
		}
		model.setSeries1(series1);
	}
	
	private void initializeData(){
		
		series1.add(new SVMDataItem(1, 1));
		series1.add(new SVMDataItem(2, 4));
		series1.add(new SVMDataItem(7.5, 7.5));
		series1.add(new SVMDataItem(8, 4, 2));
		
//		series1.add(new SVMDataItem(4, 4));
//		series1.add(new SVMDataItem(8, 8));
//		series1.add(new SVMDataItem(8, 1));
		
		
//		series1.add(new SVMDataItem(4, 4));
//		series1.add(new SVMDataItem(8, 8));
//		series1.add(new SVMDataItem(8, 1));
		
		
		

		series2.add(new SVMDataItem(1.0, 5.0));
		series2.add(new SVMDataItem(3.0, 5.0));
		series2.add(new SVMDataItem(5.0, 5.0));
		series2.add(new SVMDataItem(4.0, 8.0));
		
		model.setSeries1(series1);
		model.setSeries2(series2);
	}
	
    private static class LabelGenerator implements XYItemLabelGenerator {

        @Override
        public String generateLabel(XYDataset dataset, int series, int item) {
        	XYSeriesCollection dd = (XYSeriesCollection) dataset;
            XYSeries ss =  dd.getSeries(series);
	    	XYDataItem ditem =  (XYDataItem) ss.getItems().get(item);
	    	if (ditem instanceof SVMDataItem){
	    		SVMDataItem svmItem = (SVMDataItem) ditem;
	    		//return svmItem.toFormatedString(2);
	    		return svmItem.getWeight() + "";
	    	}
            return "";
        }


    }
	
	private JFCPanel createChartPanel(){

		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		

		JFreeChart chart = ChartFactory.createScatterPlot
		("XY Scatter Plot", "X", "Y", dataset);
		
		XYPlot plot = chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.getRangeAxis().setAutoRange(false);
        plot.getDomainAxis().setAutoRange(false);
        plot.getRangeAxis().setRange(0, 10);
        plot.getDomainAxis().setRange(0, 20);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new LabelGenerator());
        renderer.setBaseItemLabelPaint(Color.black);
        renderer.setBasePositiveItemLabelPosition(
            new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_LEFT));
        renderer.setBaseItemLabelFont(
            renderer.getBaseItemLabelFont().deriveFont(8f));
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        
        renderer.setSeriesShape(2, ShapeUtilities.createDiagonalCross(1, 1));
        renderer.setSeriesPaint(2, Color.RED.brighter());
        return new JFCPanel(chart, model);
	}

	public void clearPlot(){
		XYPlot p = panel.getChart().getXYPlot();
		p.clearAnnotations();
		series1.clear();
		series2.clear();
		
		
		model.clearDataSet(0);
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
		
		JPanel pContainer1 = new JPanel();
        

        pContainer1.setLayout(new BorderLayout(0, 0));
        
//        rr.setSeriesLinesVisible(0, true);
//        rr.setDrawSeriesLineAsPath(true);
        

        
        panel = createChartPanel();
        pContainer1.add(panel);
        panel.setBackground(Color.WHITE);
		
		JPanel pContainer2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pContainer2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		JPanel pClass1 = new JPanel();
		pContainer2.add(pClass1);
		
		JSlider slider = new JSlider();
		slider.setValue(73);
		slider.setFont(new Font("Tahoma", Font.PLAIN, 8));
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		slider.setLabelTable( labelTable );  
		slider.setPaintLabels(true);
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
		
		JLabel lblValue = new JLabel("mu");
		
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
				Object src = arg0.getSource();
				if (src instanceof JSlider){
					JSlider j = (JSlider) src;
					model.setMu1((double)j.getValue()/100) ;
					System.out.println("actionPerformed " + model.getMu1() + arg0.getActionCommand());
				}
			}
		});
		
		
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
			  	  public void changedUpdate(DocumentEvent e) {
				   // warn();
				  }
				  public void removeUpdate(DocumentEvent e) {
				    //warn();
				  }
				  public void insertUpdate(DocumentEvent e) {
				    //warn();
				  }

				  public void warn() {
					System.out.println(textField.getText());  
//				     if (Integer.parseInt(textField.getText())<=0){
//				       JOptionPane.showMessageDialog(null,
//				          "Error: Please enter number bigger than 0", "Error Massage",
//				          JOptionPane.ERROR_MESSAGE);
//				     }
				  }
				});
		textField.setText("1");
		textField.setColumns(10);
		
		
		
		JLabel lblNewLabel = new JLabel("1/mu");
		
		JLabel lblNewLabel_4 = new JLabel("Class1");
		GroupLayout gl_pClass1 = new GroupLayout(pClass1);
		gl_pClass1.setHorizontalGroup(
			gl_pClass1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pClass1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pClass1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_4)
						.addComponent(lblValue))
					.addGap(13)
					.addGroup(gl_pClass1.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_pClass1.createSequentialGroup()
							.addGap(38)
							.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 0, Short.MAX_VALUE))
						.addGroup(gl_pClass1.createSequentialGroup()
							.addGap(48)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
							.addGap(38)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)))
					.addGap(31))
		);
		gl_pClass1.setVerticalGroup(
			gl_pClass1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pClass1.createSequentialGroup()
					.addGroup(gl_pClass1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pClass1.createSequentialGroup()
							.addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_pClass1.createParallelGroup(Alignment.BASELINE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1)))
						.addGroup(gl_pClass1.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_4)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblValue, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(65, Short.MAX_VALUE))
		);
		pClass1.setLayout(gl_pClass1);
		
		JPanel pClass2 = new JPanel();
		pContainer2.add(pClass2);
		
		JSlider slider_1 = new JSlider();
		slider_1.setValue(100);
		slider_1.setMajorTickSpacing(25);
		slider_1.setFont(new Font("Tahoma", Font.PLAIN, 8));
		slider_1.setLabelTable( labelTable );  
		slider_1.setPaintLabels(true);
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				JSlider j = (JSlider) arg0.getSource();
				textField_1.setText(String.valueOf((double) j.getValue() / 100));
				lblNewLabel_2.setText((1 / Double.parseDouble(textField_1.getText()))  + "");
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
		
		JLabel lblMu = new JLabel("mu");
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object src = arg0.getSource();
				if (src instanceof JSlider){
					JSlider j = (JSlider) src;
					model.setMu2((double)j.getValue()/100) ;
					
				}
				
				System.out.println("actionPerformed " + model.getMu2() + arg0.getActionCommand());
			}
		});
		textField_1.setText("1");
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("1/mu");
		
		JLabel lblNewLabel_5 = new JLabel("Class2");
		GroupLayout gl_pClass2 = new GroupLayout(pClass2);
		gl_pClass2.setHorizontalGroup(
			gl_pClass2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pClass2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pClass2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pClass2.createSequentialGroup()
							.addComponent(lblMu)
							.addGap(18)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
							.addGap(41)
							.addComponent(lblNewLabel_3)
							.addGap(18)
							.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
						.addGroup(gl_pClass2.createSequentialGroup()
							.addComponent(lblNewLabel_5)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(slider_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_pClass2.setVerticalGroup(
			gl_pClass2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pClass2.createSequentialGroup()
					.addGroup(gl_pClass2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pClass2.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_5))
						.addComponent(slider_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_pClass2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pClass2.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_3)
							.addComponent(lblNewLabel_2))
						.addGroup(gl_pClass2.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblMu)
							.addComponent(textField_1, 0, 0, Short.MAX_VALUE)))
					.addGap(67))
		);
		pClass2.setLayout(gl_pClass2);
		
		JPanel pContainer3 = new JPanel();
		
		btnClear = new JButton("Random Data");
		pContainer3.add(btnClear);
		
		JButton btnNewButton = new JButton("find CH");
		pContainer3.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("find RCH");
		pContainer3.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Solve");
		btnNewButton_2.setEnabled(false);
		pContainer3.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Clear");
		pContainer3.add(btnNewButton_3);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(pContainer1, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
					.addGap(28)
					.addComponent(pContainer3, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(pContainer2, GroupLayout.PREFERRED_SIZE, 687, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(pContainer2, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(206)
							.addComponent(pContainer3, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addGap(35))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(pContainer1, 0, 0, Short.MAX_VALUE)
							.addGap(23))))
		);
		frame.getContentPane().setLayout(groupLayout);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
			}
		});
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.solveSVM();
			}
		});
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO debug code3
				//model.setMu1(0.91);
				//model.setMu1(0.49);
				//model.setMu1(1);
				
				model.setMu1(Double.parseDouble(textField.getText()));
				
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
		
		JMenuItem mntmAddPoint = new JMenuItem("Add Point");
		mnFile.add(mntmAddPoint);
		
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
		
		JMenuItem mntmTwoPoints = new JMenuItem("Two points 1");
		mntmTwoPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(4, 1));
				model.setSeries1(series1);
			}
		});
		mnData.add(mntmTwoPoints);
		
		JMenuItem mntmCollinearPoints = new JMenuItem("Collinear Points 1");
		mntmCollinearPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(4, 1));
				series1.add(new SVMDataItem(7, 1));
				series1.add(new SVMDataItem(11, 1));
				
				model.setSeries1(series1);
			}
		});
		mnData.add(mntmCollinearPoints);
		
		JMenuItem mntmTriangle = new JMenuItem("Triangle 1");
		mntmTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				//series1.add(new SVMDataItem(1, 1));
				
				series1.add(new SVMDataItem(8, 1,2));
				
				//series1.add(new SVMDataItem(8, 1));
				//series1.add(new SVMDataItem(8, 1));
				
				series1.add(new SVMDataItem(1, 8));
				//series1.add(new SVMDataItem(1, 8));
				
				//series1.add(new SVMDataItem(4.5, 1));
				//series1.add(new SVMDataItem(1, 1));
				model.setSeries1(series1);
			}
		});
		
		JMenuItem mntmCollinearPoints_1 = new JMenuItem("Collinear Points 2");
		mntmCollinearPoints_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(4, 1));
				series1.add(new SVMDataItem(7, 1.01));
				series1.add(new SVMDataItem(11, 1));
				
				model.setSeries1(series1);
				
			}
		});
		mnData.add(mntmCollinearPoints_1);
		mnData.add(mntmTriangle);
		
		JMenuItem mntmRombus = new JMenuItem("Parallelogram");
		mntmRombus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(4, 4));
				series1.add(new SVMDataItem(4, 1));
				series1.add(new SVMDataItem(7, 4));
				model.setSeries1(series1);
			}
		});
		mnData.add(mntmRombus);
		
		JMenuItem mntmTrapezium = new JMenuItem("Trapezium");
		mntmTrapezium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(4, 4));
				series1.add(new SVMDataItem(8, 4));
				series1.add(new SVMDataItem(11, 1));
				model.setSeries1(series1);
			}
		});
		mnData.add(mntmTrapezium);
		
		JMenuItem mntmRandom = new JMenuItem("Random");
		mntmRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRandomData();
			}
		});
		
		JMenuItem mntmTPoints = new JMenuItem("T points");
		mntmTPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearPlot();
				//TODO answer the question is point adding order is dependent or independent of RCH produced
				series1.add(new SVMDataItem(1, 1));
				series1.add(new SVMDataItem(1, 3));
				series1.add(new SVMDataItem(1, 5));
				series1.add(new SVMDataItem(1, 7));
				series1.add(new SVMDataItem(1, 9));
//				series1.add(new SVMDataItem(2, 5));
//				series1.add(new SVMDataItem(4, 5));
				series1.add(new SVMDataItem(6, 5));
				model.setSeries1(series1);
			}
		});
		mnData.add(mntmTPoints);
		mnData.add(mntmRandom);
		
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
			addRandomData();
			//initializeData();
			
			//panel.setBackground(Color.WHITE);
			//
			//panel.setForeground(Color.WHITE);
			
			//panel.getGraphics().fillRect(0, 0, panel.getWidth(), panel.getHeight());
			//panel.getGraphics().clearRect(0, 0, panel.getWidth(), panel.getHeight());
			//panel.getGraphics().drawRect(0, 0, 200, 200);
			//panel.repaint();
		}
	}
	
	
	
	
	
	
	
	//TODO helper
	private void print(String str){
		System.out.println(str);
	}
}
