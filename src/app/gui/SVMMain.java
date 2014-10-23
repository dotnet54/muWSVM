package app.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
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

import app.model.data.SVMDataItem;
import app.model.data.SVMModel;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Main GUI window for the application
 * 
 * @author shifaz
 *
 */
public class SVMMain implements ActionListener{

	//Model globals
	private SVMModel model = null;
	//TODO we can remove centroid by right click, disable those
	
	//GUI globals
	private JFrame frame;
	private JFreeChart chart;
	public static SVMPanel chartPanel;	//TODO change static public, only for debug
	
	//components
	private JTextField textField_class1;
	private JTextField textField_class_2;
	private JSlider slider_class1;
	private JSlider slider_class2;
	private JLabel lblmuInvVal_class1 = new JLabel("1");
	private JLabel lblmuInvVal_class2 = new JLabel("1");
	private JTable tableConfusion;
	private JTextField textField_NewWeight;
	private JTextField textField_NumDataPoints;
	private JTextField textField_PercentPos;
	private JTextField textField_NumDuplications;
	private JTextField textField_MaxIterationsWSK;
	private JTextField textField_MaxRecursionWRCH;
	private JTextField textField_FPPrecision;
	private JTextField textField_NumDP;
	private JTextField textField_SeparationDelta;
	private JTextField textField_11;
	private JTextField textField_12;
	
	private final SVMAddItemDialog addItemDialog = new SVMAddItemDialog();
	private final SVMAboutDialog aboutDialog = new SVMAboutDialog();
	private final SVMHelpDialog helpDialog = new SVMHelpDialog();
	private final JFileChooser fc = new JFileChooser(".");
	
	/**
	 * Entry point - Launch the application.
	 */
	public static void main(String[] args) {
		
		//set up the theme
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					SVMMain window = new SVMMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("Unknown Error: Terminating");
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	

	/**
	 * Create the application.
	 */
	public SVMMain() {
		//Initialize Model
		model = new SVMModel();

		//Initialize GUI
		initializeGUI();
	}
	

	
		/**
		 * Initialize the contents of the frame.
		 * NOTE: Most parts of this function is auto-generated using a GUI Builder,
		 * which is why it is very lengthy and non-modular
		 * 
		 */
		private void initializeGUI() {
			frame = new JFrame();
			frame.setResizable(false);
			frame.setBounds(100, 100, 845, 518);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
		    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
		    labelTable.put(new Integer(100), new JLabel("1.0"));  
		    labelTable.put(new Integer(75), new JLabel("0.75"));  
		    labelTable.put(new Integer(50), new JLabel("0.50"));  
		    labelTable.put(new Integer(25), new JLabel("0.25"));  
		    labelTable.put(new Integer(0), new JLabel("0.0"));
			
			JPanel pChartContainer = new JPanel();
			pChartContainer.setBounds(10, 11, 450, 454);
	        
	
	        pChartContainer.setLayout(new BorderLayout(0, 0));
	        
	        chartPanel = createChartPanel();
	        pChartContainer.add(chartPanel, BorderLayout.NORTH);
	        chartPanel.setBackground(Color.WHITE);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(514, 11, 313, 372);
			
			JPanel pSolveButtonsContainer = new JPanel();
			pSolveButtonsContainer.setBounds(514, 390, 313, 52);
			pSolveButtonsContainer.setLayout(null);
			
			JButton btnFindWeightedReduced = new JButton("Find Weighted RCH");
			btnFindWeightedReduced.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.setMu1(Double.parseDouble(textField_class1.getText()));
					model.setMu2(Double.parseDouble(textField_class_2.getText()));
					chartPanel.findRCH();
					chartPanel.solveSVM();
				}
			});
			btnFindWeightedReduced.setBounds(0, 0, 125, 23);
			pSolveButtonsContainer.add(btnFindWeightedReduced);
			
			JButton btnTrainWSVM = new JButton("Train Weighted SVM");
			btnTrainWSVM.setBounds(0, 29, 129, 23);
			pSolveButtonsContainer.add(btnTrainWSVM);
			
			JPanel pContainerParameters = new JPanel();
			tabbedPane.addTab("Parameters", null, pContainerParameters, null);
			pContainerParameters.setLayout(null);
			
			JPanel pClass1 = new JPanel();
			pClass1.setBounds(10, 58, 259, 120);
			pContainerParameters.add(pClass1);
			
			slider_class1 = new JSlider();
			slider_class1.setBounds(0, 26, 200, 40);
			slider_class1.setFont(new Font("Tahoma", Font.PLAIN, 8));
			slider_class1.setMajorTickSpacing(25);
			slider_class1.setPaintTicks(true);
			slider_class1.setLabelTable( labelTable );  
			slider_class1.setPaintLabels(true);
			slider_class1.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					textField_class1.setText(String.valueOf((double) j.getValue() / 100));
					lblmuInvVal_class1.setText((1 / Double.parseDouble(textField_class1.getText()))  + "");
					//TODO
					model.setMu1(Double.parseDouble(textField_class1.getText()));
					model.setMu2(Double.parseDouble(textField_class_2.getText()));
					chartPanel.findRCH();
					chartPanel.solveSVM();
					
	
					for (ActionListener a : textField_class1.getActionListeners()) {
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
			
			JLabel lblmu_class1 = new JLabel("mu");
			lblmu_class1.setBounds(10, 72, 14, 16);
			
			
			textField_class1 = new JTextField();
			textField_class1.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					System.out.println("textField Lost Focus: " + textField_class1.getText());
		            try{
		            	String input = textField_class1.getText();
		            	input = input.replaceAll("[^-\\d.]","");
		            	textField_class1.setText(input);
	            		double db = Double.parseDouble(textField_class1.getText());
	            		
	            		if (db < 0){
	            			textField_class1.setText("0");
	            		}else if(db > 1){
	            			textField_class1.setText("1");
	            		}
	            		
	            		
		            }catch (NumberFormatException nf) {
		            	JOptionPane.showMessageDialog(null, "Invalid input for mu parameter", 
		            			"Error: Invalid Input" , JOptionPane.INFORMATION_MESSAGE);
		            	textField_class1.setText("1");
		            }
					slider_class1.setValue((int) (Double.parseDouble(textField_class1.getText()) * 100));
				}
				@Override
				public void focusGained(FocusEvent e) {
					System.out.println("textField Gained Focus: " + textField_class1.getText());
				}
			});
			textField_class1.setBounds(58, 72, 49, 16);
			textField_class1.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent arg0) {
					System.out.println("propertyChange");
				}
			});
			textField_class1.addInputMethodListener(new InputMethodListener() {
				public void caretPositionChanged(InputMethodEvent arg0) {
					System.out.println("inputMethodTextChanged");
				}
				public void inputMethodTextChanged(InputMethodEvent arg0) {
					System.out.println("inputMethodTextChanged");
				}
			});
			textField_class1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object src = arg0.getSource();
					if (src instanceof JSlider){
						JSlider j = (JSlider) src;
						model.setMu1((double)j.getValue()/100) ;
						System.out.println("actionPerformed " + model.getMu1() + arg0.getActionCommand());
					}
				}
			});
			
			
			
			textField_class1.getDocument().addDocumentListener(new DocumentListener() {
				  	  public void changedUpdate(DocumentEvent e) {
				  		//Plain text components do not fire these events
					  }
					  public void removeUpdate(DocumentEvent e) {
					    //warn();
					  }
					  public void insertUpdate(DocumentEvent e) {
					   // warn();
					  }
	
					  public void warn() {
						System.out.println(textField_class1.getText());  
						
						
			            try{
		            		Integer.parseInt(textField_class1.getText());
			            }catch (NumberFormatException nf) {
			            	//JOptionPane.showMessageDialog(null, "sdf", 
			            	//		"InfoBox: " , JOptionPane.INFORMATION_MESSAGE);
			            }
						
						
	//				     if (Integer.parseInt(textField.getText())<=0){
	//				       JOptionPane.showMessageDialog(null,
	//				          "Error: Please enter number bigger than 0", "Error Massage",
	//				          JOptionPane.ERROR_MESSAGE);
	//				     }
					  }
					});
			textField_class1.setText("0.5");
			textField_class1.setColumns(10);
			
			
			
			JLabel lblmuInv_class1 = new JLabel("1/mu");
			lblmuInv_class1.setBounds(10, 95, 24, 14);
			pClass1.setLayout(null);
			
			JLabel lblNewLabel_4 = new JLabel("Positive \r\nClass");
			lblNewLabel_4.setBounds(10, -1, 82, 16);
			pClass1.add(lblNewLabel_4);
			pClass1.add(lblmu_class1);
			pClass1.add(textField_class1);
			pClass1.add(lblmuInv_class1);
			lblmuInvVal_class1.setBounds(58, 95, 69, 14);
			pClass1.add(lblmuInvVal_class1);
			pClass1.add(slider_class1);
			
			JPanel pClass2 = new JPanel();
			pClass2.setBounds(10, 189, 259, 120);
			pContainerParameters.add(pClass2);
			
			slider_class2 = new JSlider();
			slider_class2.setBounds(0, 19, 200, 40);
			slider_class2.setMajorTickSpacing(25);
			slider_class2.setFont(new Font("Tahoma", Font.PLAIN, 8));
			slider_class2.setLabelTable( labelTable );  
			slider_class2.setPaintLabels(true);
			slider_class2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					textField_class_2.setText(String.valueOf((double) j.getValue() / 100));
					lblmuInvVal_class2.setText((1 / Double.parseDouble(textField_class_2.getText()))  + "");
					
					//TODO temp do same for other slider
					model.setMu1(Double.parseDouble(textField_class1.getText()));
					model.setMu2(Double.parseDouble(textField_class_2.getText()));
					chartPanel.findRCH();
					chartPanel.solveSVM();
					
					
					
					for (ActionListener a : textField_class_2.getActionListeners()) {
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
			slider_class2.setPaintLabels(true);
			slider_class2.setPaintTicks(true);
			
			JLabel lblmu_class2 = new JLabel("mu");
			lblmu_class2.setBounds(10, 70, 14, 14);
			
			textField_class_2 = new JTextField();
			textField_class_2.setBounds(55, 70, 54, 14);
			textField_class_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object src = arg0.getSource();
					if (src instanceof JSlider){
						JSlider j = (JSlider) src;
						model.setMu2((double)j.getValue()/100) ;
						
					}
					
					System.out.println("actionPerformed " + model.getMu2() + arg0.getActionCommand());
				}
			});
			textField_class_2.setText("0.5");
			textField_class_2.setColumns(10);
			textField_class_2.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					System.out.println("textField_1 Lost Focus: " + textField_class_2.getText());
		            try{
		            	String input = textField_class_2.getText();
		            	input = input.replaceAll("[^-\\d.]","");
		            	textField_class_2.setText(input);
	            		double db = Double.parseDouble(textField_class_2.getText());
	            		
	            		if (db < 0){
	            			textField_class_2.setText("0");
	            		}else if(db > 1){
	            			textField_class_2.setText("1");
	            		}
	            		
	            		
		            }catch (NumberFormatException nf) {
		            	JOptionPane.showMessageDialog(null, "Invalid input for mu parameter", 
		            			"Error: Invalid Input" , JOptionPane.INFORMATION_MESSAGE);
		            	textField_class_2.setText("1");
		            }
					slider_class2.setValue((int) (Double.parseDouble(textField_class_2.getText()) * 100));
				}
				@Override
				public void focusGained(FocusEvent e) {
					System.out.println("textField_1 Gained Focus: " + textField_class_2.getText());
				}
			});
			
			JLabel lblmuInv_class2 = new JLabel("1/mu");
			lblmuInv_class2.setBounds(10, 95, 24, 14);
			
			JLabel lblNewLabel_5 = new JLabel("Negative Class");
			lblNewLabel_5.setBounds(10, 0, 71, 14);
			pClass2.setLayout(null);
			pClass2.add(lblmu_class2);
			pClass2.add(textField_class_2);
			pClass2.add(lblmuInv_class2);
			lblmuInvVal_class2.setBounds(55, 95, 66, 14);
			pClass2.add(lblmuInvVal_class2);
			pClass2.add(lblNewLabel_5);
			pClass2.add(slider_class2);
			
			JCheckBox chckbxUseSameParameters = new JCheckBox("Use same parameters for two classes");
			chckbxUseSameParameters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					slider_class2.setEnabled(!slider_class2.isEnabled());
					textField_class_2.setEnabled(!textField_class_2.isEnabled());
				}
			});
			chckbxUseSameParameters.setBounds(6, 7, 231, 23);
			pContainerParameters.add(chckbxUseSameParameters);
			
			JPanel pContainerTrainingData = new JPanel();
			tabbedPane.addTab("Training Data", null, pContainerTrainingData, null);
			pContainerTrainingData.setLayout(null);
			
			JComboBox<String> cmbDataDistType = new JComboBox<String>();
			cmbDataDistType.setBounds(11, 221, 156, 23);
			cmbDataDistType.setModel(new DefaultComboBoxModel<String>(new String[] {"Hard Margin SVM", "Soft Margin SVM"}));
			pContainerTrainingData.add(cmbDataDistType);
			
			JButton btnGenerateData = new JButton("Generate Random Data");
			btnGenerateData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.clearDataSet(2);
					
					
					int numPoints = Integer.parseInt(textField_NumDataPoints.getText());
					int percentPos = Integer.parseInt(textField_PercentPos.getText());
					int separationDelta = Integer.parseInt(textField_SeparationDelta.getText());
					
					model.generateRandomData(numPoints, percentPos, separationDelta);
					chart.getXYPlot().setDataset(0, model.getRawDataSet()); 
				}
			});
			btnGenerateData.setBounds(10, 294, 145, 23);
			pContainerTrainingData.add(btnGenerateData);
			
			final JButton btnLoadData = new JButton("Load Data ");
			btnLoadData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
				        int returnVal = fc.showOpenDialog(frame);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				        	model.getRawDataSet().loadFromFile(fc.getSelectedFile());
				        } 
						
					} catch (IOException e1) {
						// TODO
						e1.printStackTrace();
					}
				}
			});
			btnLoadData.setBounds(11, 11, 92, 23);
			pContainerTrainingData.add(btnLoadData);
			
			JLabel lblNumDataPoints = new JLabel("Number of Data Points:");
			lblNumDataPoints.setBounds(10, 92, 155, 14);
			pContainerTrainingData.add(lblNumDataPoints);
			
			textField_NumDataPoints = new JTextField();
			textField_NumDataPoints.setText("10");
			textField_NumDataPoints.setBounds(10, 109, 105, 20);
			pContainerTrainingData.add(textField_NumDataPoints);
			textField_NumDataPoints.setColumns(10);
			
			JLabel lblPercentPos = new JLabel("Percentage of Positive Data:");
			lblPercentPos.setBounds(10, 140, 175, 14);
			pContainerTrainingData.add(lblPercentPos);
			
			textField_PercentPos = new JTextField();
			textField_PercentPos.setText("50");
			textField_PercentPos.setBounds(10, 165, 105, 20);
			pContainerTrainingData.add(textField_PercentPos);
			textField_PercentPos.setColumns(10);
			
			JLabel lblDataDistType = new JLabel("Data Distribution Type");
			lblDataDistType.setBounds(10, 196, 145, 14);
			pContainerTrainingData.add(lblDataDistType);
			
			JButton btnClearPlot = new JButton("Clear Plot");
			btnClearPlot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.clearDataSet(2);
				}
			});
			btnClearPlot.setBounds(113, 11, 79, 23);
			pContainerTrainingData.add(btnClearPlot);
			
			JComboBox<String> cmbPredefinedData = new JComboBox<String>();
			cmbPredefinedData.setModel(new DefaultComboBoxModel<String>(new String[] {"Triangle"}));
			cmbPredefinedData.setBounds(164, 45, 134, 20);
			pContainerTrainingData.add(cmbPredefinedData);
			
			JButton btnPredefinedDatasets = new JButton("Predefined Datasets");
			btnPredefinedDatasets.setBounds(11, 45, 143, 23);
			pContainerTrainingData.add(btnPredefinedDatasets);
			
			JLabel lblOverlapDelta = new JLabel("Separation Delta");
			lblOverlapDelta.setBounds(11, 253, 104, 14);
			pContainerTrainingData.add(lblOverlapDelta);
			
			textField_SeparationDelta = new JTextField();
			textField_SeparationDelta.setText("0");
			textField_SeparationDelta.setBounds(113, 255, 55, 20);
			pContainerTrainingData.add(textField_SeparationDelta);
			textField_SeparationDelta.setColumns(10);
			
			JPanel pContainerDataEditing = new JPanel();
			tabbedPane.addTab("Data Editing", null, pContainerDataEditing, null);
			pContainerDataEditing.setLayout(null);
			
			textField_NewWeight = new JTextField();
			textField_NewWeight.setBounds(142, 263, 46, 20);
			pContainerDataEditing.add(textField_NewWeight);
			textField_NewWeight.setText("1");
			textField_NewWeight.setColumns(10);
			
			JLabel lblNewWeight = new JLabel("New Weight:");
			lblNewWeight.setBounds(10, 263, 77, 14);
			pContainerDataEditing.add(lblNewWeight);
			
			JLabel lblCurrentSelection = new JLabel("Current Selection:");
			lblCurrentSelection.setBounds(10, 11, 94, 14);
			pContainerDataEditing.add(lblCurrentSelection);
			
			JLabel lblNone = new JLabel("None");
			lblNone.setBounds(114, 11, 46, 14);
			pContainerDataEditing.add(lblNone);
			
			JLabel lblNewLabel_10 = new JLabel("Select tool and click points to edit");
			lblNewLabel_10.setBounds(10, 87, 259, 14);
			pContainerDataEditing.add(lblNewLabel_10);
			
			textField_NumDuplications = new JTextField();
			textField_NumDuplications.setText("1");
			textField_NumDuplications.setBounds(142, 198, 46, 20);
			pContainerDataEditing.add(textField_NumDuplications);
			textField_NumDuplications.setColumns(10);
			
			JLabel lblNumDuplications = new JLabel("Number of Duplications");
			lblNumDuplications.setBounds(10, 201, 122, 14);
			pContainerDataEditing.add(lblNumDuplications);
			
			JComboBox<String> cmbNewClass = new JComboBox<String>();
			cmbNewClass.setModel(new DefaultComboBoxModel<String>(new String[] {"Positive Class", "Negative Class"}));
			cmbNewClass.setBounds(142, 229, 90, 20);
			pContainerDataEditing.add(cmbNewClass);
			
			JLabel lblNewClass = new JLabel("New Class:");
			lblNewClass.setBounds(10, 226, 77, 14);
			pContainerDataEditing.add(lblNewClass);
			
			JRadioButton rdbtnSelectTool = new JRadioButton("Select Tool");
			rdbtnSelectTool.setSelected(true);
			rdbtnSelectTool.setBounds(10, 119, 109, 23);
			pContainerDataEditing.add(rdbtnSelectTool);
			
			JRadioButton rdbtnAddTool = new JRadioButton("Add Tool");
			rdbtnAddTool.setBounds(123, 145, 109, 23);
			pContainerDataEditing.add(rdbtnAddTool);
			
			JRadioButton rdbtnRemoveTool = new JRadioButton("Remove Tool");
			rdbtnRemoveTool.setBounds(123, 119, 109, 23);
			pContainerDataEditing.add(rdbtnRemoveTool);
			
			JRadioButton rdbtnDuplicateTool = new JRadioButton("Duplicate Tool");
			rdbtnDuplicateTool.setBounds(10, 145, 109, 23);
			pContainerDataEditing.add(rdbtnDuplicateTool);
			
			JRadioButton rdbtnWeightingTool = new JRadioButton("Weight Tool");
			rdbtnWeightingTool.setBounds(10, 171, 109, 23);
			pContainerDataEditing.add(rdbtnWeightingTool);
			
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(rdbtnSelectTool);
			bg.add(rdbtnAddTool);
			bg.add(rdbtnRemoveTool);
			bg.add(rdbtnDuplicateTool);
			bg.add(rdbtnWeightingTool);
			
			JLabel lblX = new JLabel("X:");
			lblX.setBounds(10, 303, 18, 14);
			pContainerDataEditing.add(lblX);
			
			JLabel lblY = new JLabel("Y:");
			lblY.setBounds(86, 303, 18, 14);
			pContainerDataEditing.add(lblY);
			
			textField_11 = new JTextField();
			textField_11.setBounds(20, 300, 46, 20);
			pContainerDataEditing.add(textField_11);
			textField_11.setColumns(10);
			
			textField_12 = new JTextField();
			textField_12.setBounds(96, 300, 46, 20);
			pContainerDataEditing.add(textField_12);
			textField_12.setColumns(10);
			
			final JButton btnAddPoint = new JButton("Add Point");
			btnAddPoint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addItemDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					addItemDialog.setVisible(true);
				}
			});
			btnAddPoint.setBounds(152, 294, 89, 23);
			pContainerDataEditing.add(btnAddPoint);
			
			JPanel pContainerPerformance = new JPanel();
			tabbedPane.addTab("Performance", null, pContainerPerformance, null);
			
			tableConfusion = new JTable();
			tableConfusion.setBounds(47, 57, 227, 142);
			tableConfusion.setEnabled(false);
			tableConfusion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableConfusion.setRowSelectionAllowed(false);
			tableConfusion.setModel(new DefaultTableModel(
				new Object[][] {
					{"Total:", "Positive", "Negative"},
					{"Positive", null, null},
					{"Negative", null, null},
					{null, null, ""},
					{"Sensitivity", null, null},
					{"Specificity", null, null},
					{"Accuracy", null, null},
					{"Precision", null, null},
				},
				new String[] {
					"New column", "New column", "New column"
				}
			) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 3148195956415476019L;
				Class[] columnTypes = new Class[] {
					String.class, String.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});
			tableConfusion.getColumnModel().getColumn(0).setResizable(false);
			tableConfusion.getColumnModel().getColumn(1).setResizable(false);
			tableConfusion.getColumnModel().getColumn(2).setResizable(false);
			pContainerPerformance.setLayout(null);
			pContainerPerformance.add(tableConfusion);
			
			JLabel lblConfusionMatrix = new JLabel("Confusion Matrix");
			lblConfusionMatrix.setBounds(10, 11, 102, 14);
			pContainerPerformance.add(lblConfusionMatrix);
			
			JLabel lblPredictedClass = new JLabel("<html>Predicted Class");
			lblPredictedClass.setBounds(122, 29, 89, 22);
			pContainerPerformance.add(lblPredictedClass);
			
			JLabel lblActualClass = new JLabel("<html>Actual<br> Class");
			lblActualClass.setBounds(10, 82, 30, 22);
			pContainerPerformance.add(lblActualClass);
			
			JLabel lblNumberOfSupport = new JLabel("<html>Number of\r\n<br> Support Vectors");
			lblNumberOfSupport.setBounds(47, 210, 77, 28);
			pContainerPerformance.add(lblNumberOfSupport);
			frame.getContentPane().setLayout(null);
			frame.getContentPane().add(pChartContainer);
			frame.getContentPane().add(pSolveButtonsContainer);
			
			JButton btnClassify = new JButton("Classify");
			btnClassify.setBounds(188, 29, 89, 23);
			pSolveButtonsContainer.add(btnClassify);
			
			JButton btnPlotTestData = new JButton("Plot Test Data");
			btnPlotTestData.setBounds(188, 0, 125, 23);
			pSolveButtonsContainer.add(btnPlotTestData);
			frame.getContentPane().add(tabbedPane);
			
			JPanel pContainerOptions = new JPanel();
			tabbedPane.addTab("Options", null, pContainerOptions, null);
			pContainerOptions.setLayout(null);
			
			JLabel lblNumberOfIterations = new JLabel("Number of Maximum Iterations for WSK");
			lblNumberOfIterations.setBounds(10, 11, 191, 14);
			pContainerOptions.add(lblNumberOfIterations);
			
			textField_MaxIterationsWSK = new JTextField();
			textField_MaxIterationsWSK.setText("500");
			textField_MaxIterationsWSK.setBounds(10, 26, 86, 20);
			pContainerOptions.add(textField_MaxIterationsWSK);
			textField_MaxIterationsWSK.setColumns(10);
			
			JLabel lblMaximumRecursionDepth = new JLabel("Maximum Recursion Depth for WRCH");
			lblMaximumRecursionDepth.setBounds(10, 58, 191, 14);
			pContainerOptions.add(lblMaximumRecursionDepth);
			
			textField_MaxRecursionWRCH = new JTextField();
			textField_MaxRecursionWRCH.setText("100");
			textField_MaxRecursionWRCH.setBounds(10, 90, 86, 20);
			pContainerOptions.add(textField_MaxRecursionWRCH);
			textField_MaxRecursionWRCH.setColumns(10);
			
			JLabel lblOrToo = new JLabel("If 0 or too large, this setting depends on Java stack size");
			lblOrToo.setBounds(10, 71, 288, 14);
			pContainerOptions.add(lblOrToo);
			
			JLabel lblFloatingPointThreshold = new JLabel("Floating Point Comparison Threshold");
			lblFloatingPointThreshold.setBounds(10, 138, 191, 14);
			pContainerOptions.add(lblFloatingPointThreshold);
			
			textField_FPPrecision = new JTextField();
			textField_FPPrecision.setText("0.001");
			textField_FPPrecision.setBounds(10, 163, 86, 20);
			pContainerOptions.add(textField_FPPrecision);
			textField_FPPrecision.setColumns(10);
			
			JLabel lblDecimalRounding = new JLabel("Number of Decimal Places");
			lblDecimalRounding.setBounds(10, 199, 225, 14);
			pContainerOptions.add(lblDecimalRounding);
			
			textField_NumDP = new JTextField();
			textField_NumDP.setText("2");
			textField_NumDP.setBounds(10, 221, 86, 20);
			pContainerOptions.add(textField_NumDP);
			textField_NumDP.setColumns(10);
			
			JPanel pContainerTestingData = new JPanel();
			tabbedPane.addTab("Testing Data", null, pContainerTestingData, null);
			
			JMenuBar menuBar = new JMenuBar();
			frame.setJMenuBar(menuBar);
			
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			
			JMenuItem mntmAddPoint = new JMenuItem("Add Point");
			mntmAddPoint.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnAddPoint.doClick();
				}
			});
			mnFile.add(mntmAddPoint);
			
			JMenuItem mntmOpenFile = new JMenuItem("Open File");
			mntmOpenFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnLoadData.doClick();
				}
			});
			mnFile.add(mntmOpenFile);
			
			JSeparator separator = new JSeparator();
			mnFile.add(separator);
			
			JMenuItem mntmExit = new JMenuItem("Exit");
			mntmExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			mnFile.add(mntmExit);
			
			JMenu mnView = new JMenu("View");
			menuBar.add(mnView);
			
			JMenu mnDisplayLabels = new JMenu("Display Labels");
			mnView.add(mnDisplayLabels);
			
			JCheckBoxMenuItem chckbxmntmDisplayWeights = new JCheckBoxMenuItem("Weights");
			chckbxmntmDisplayWeights.setSelected(true);
			mnDisplayLabels.add(chckbxmntmDisplayWeights);
			
			JCheckBoxMenuItem chckbxmntmAlphaValues = new JCheckBoxMenuItem("Alpha Values");
			mnDisplayLabels.add(chckbxmntmAlphaValues);
			
			JMenu mnDataPoints = new JMenu("Data Points");
			mnView.add(mnDataPoints);
			
			JCheckBoxMenuItem chckbxmntmDisplayDataPositiveClass = new JCheckBoxMenuItem("Positive Class");
			chckbxmntmDisplayDataPositiveClass.setSelected(true);
			mnDataPoints.add(chckbxmntmDisplayDataPositiveClass);
			
			JCheckBoxMenuItem chckbxmntmDisplayDataNegativeClass = new JCheckBoxMenuItem("Negative Class");
			chckbxmntmDisplayDataNegativeClass.setSelected(true);
			mnDataPoints.add(chckbxmntmDisplayDataNegativeClass);
			
			JMenu mnConvexHulls = new JMenu("Convex Hulls");
			mnView.add(mnConvexHulls);
			
			JCheckBoxMenuItem mntmConvexHullpositive = new JCheckBoxMenuItem("Positive Class");
			mnConvexHulls.add(mntmConvexHullpositive);
			
			JCheckBoxMenuItem mntmConvexHullnegative = new JCheckBoxMenuItem("Negative Class");
			mnConvexHulls.add(mntmConvexHullnegative);
			
			JMenu mnWeightedRch = new JMenu("Weighted RCH");
			mnView.add(mnWeightedRch);
			
			JCheckBoxMenuItem mntmWrchpositive = new JCheckBoxMenuItem("Positive Class");
			mntmWrchpositive.setSelected(true);
			mnWeightedRch.add(mntmWrchpositive);
			
			JCheckBoxMenuItem mntmWrchnegative = new JCheckBoxMenuItem("Negative Class");
			mntmWrchnegative.setSelected(true);
			mnWeightedRch.add(mntmWrchnegative);
			
			JMenu mnSupportVectors = new JMenu("Support Vectors");
			mnView.add(mnSupportVectors);
			
			JCheckBoxMenuItem mntmDisplaySVPC = new JCheckBoxMenuItem("Positive Class");
			mnSupportVectors.add(mntmDisplaySVPC);
			
			JCheckBoxMenuItem mntmDisplaySVNC = new JCheckBoxMenuItem("Negative Class");
			mnSupportVectors.add(mntmDisplaySVNC);
			
			JMenu mnCentroids = new JMenu("Centroids");
			mnView.add(mnCentroids);
			
			JCheckBoxMenuItem mntmDisplayCentroidPositiveClass = new JCheckBoxMenuItem("Positive Class");
			mntmDisplayCentroidPositiveClass.setSelected(true);
			mnCentroids.add(mntmDisplayCentroidPositiveClass);
			
			JCheckBoxMenuItem mntmDisplayCentroidNegativeClass = new JCheckBoxMenuItem("Negative Class");
			mntmDisplayCentroidNegativeClass.setSelected(true);
			mnCentroids.add(mntmDisplayCentroidNegativeClass);
			
			JCheckBoxMenuItem mntmHyperplane = new JCheckBoxMenuItem("Hyperplane");
			mntmHyperplane.setSelected(true);
			mnView.add(mntmHyperplane);
			
			JCheckBoxMenuItem mntmMargins = new JCheckBoxMenuItem("Margins");
			mnView.add(mntmMargins);
			
			JMenu mnData = new JMenu("Data");
			menuBar.add(mnData);
			
			JMenuItem mntmTwoPoints = new JMenuItem("Two points 1");
			mntmTwoPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(4, 1));
	//				model.setSeries1(series1);
				}
			});
			mnData.add(mntmTwoPoints);
			
			JMenuItem mntmCollinearPoints = new JMenuItem("Collinear Points 1");
			mntmCollinearPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(4, 1));
	//				series1.add(new SVMDataItem(7, 1));
	//				series1.add(new SVMDataItem(11, 1));
	//				
	//				model.setSeries1(series1);
				}
			});
			mnData.add(mntmCollinearPoints);
			
			JMenuItem mntmTriangle = new JMenuItem("Triangle 1");
			mntmTriangle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	//				chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				//series1.add(new SVMDataItem(1, 1));
	//				
	//				series1.add(new SVMDataItem(8, 1,2));
	//				
	//				//series1.add(new SVMDataItem(8, 1));
	//				//series1.add(new SVMDataItem(8, 1));
	//				
	//				series1.add(new SVMDataItem(1, 8));
	//				//series1.add(new SVMDataItem(1, 8));
	//				
	//				//series1.add(new SVMDataItem(4.5, 1));
	//				//series1.add(new SVMDataItem(1, 1));
	//				model.setSeries1(series1);
				}
			});
			
			JMenuItem mntmCollinearPoints_1 = new JMenuItem("Collinear Points 2");
			mntmCollinearPoints_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(4, 1));
	//				series1.add(new SVMDataItem(7, 1.01));
	//				series1.add(new SVMDataItem(11, 1));
	//				
	//				model.setSeries1(series1);
					
				}
			});
			mnData.add(mntmCollinearPoints_1);
			mnData.add(mntmTriangle);
			
			JMenuItem mntmRombus = new JMenuItem("Parallelogram");
			mntmRombus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(4, 4));
	//				series1.add(new SVMDataItem(4, 1));
	//				series1.add(new SVMDataItem(7, 4));
	//				model.setSeries1(series1);
				}
			});
			mnData.add(mntmRombus);
			
			JMenuItem mntmTrapezium = new JMenuItem("Trapezium");
			mntmTrapezium.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(4, 4));
	//				series1.add(new SVMDataItem(8, 4));
	//				series1.add(new SVMDataItem(11, 1));
	//				model.setSeries1(series1);
				}
			});
			mnData.add(mntmTrapezium);
			
			JMenuItem mntmRandom = new JMenuItem("Random");
			mntmRandom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	//				addRandomData();
				}
			});
			
			JMenuItem mntmTPoints = new JMenuItem("T points");
			mntmTPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
					//TODO answer the question is point adding order is dependent or independent of RCH produced
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(1, 3));
	//				series1.add(new SVMDataItem(1, 5));
	//				series1.add(new SVMDataItem(1, 7));
	//				series1.add(new SVMDataItem(1, 9));
	////				series1.add(new SVMDataItem(2, 5));
	////				series1.add(new SVMDataItem(4, 5));
	//				series1.add(new SVMDataItem(6, 5));
	//				model.setSeries1(series1);
				}
			});
			mnData.add(mntmTPoints);
			mnData.add(mntmRandom);
			
			JMenuItem mntmNewMenuItem = new JMenuItem("Triangle 2");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
					//TODO answer the question is point adding order is dependent or independent of RCH produced
	//				series1.add(new SVMDataItem(1, 1));
	//				series1.add(new SVMDataItem(6, 1));
	//				series1.add(new SVMDataItem(1, 6));
	//				series2.add(new SVMDataItem(8, 1));
	//				series2.add(new SVMDataItem(13, 1));
	//				series2.add(new SVMDataItem(13, 6));
	//				model.setSeries1(series1);
	//				model.setSeries2(series2);
				}
			});
			mnData.add(mntmNewMenuItem);
			
			JMenu mnHelp = new JMenu("Help");
			menuBar.add(mnHelp);
			
			JMenuItem mntmHelpContent = new JMenuItem("Help Content");
			mntmHelpContent.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					helpDialog.setVisible(true);
				}
			});
			mnHelp.add(mntmHelpContent);
			
			JMenuItem mntmAbout = new JMenuItem("About");
			mntmAbout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					aboutDialog.setVisible(true);
				}
			});
			mnHelp.add(mntmAbout);
		}





	private void updateMatrix(){
		tableConfusion.getCellEditor(0, 0);
	}


	
    /**
     * Creates a customized JFreechart panel to draw the chart
     * 
     * @return
     */
	private SVMPanel createChartPanel(){

		chart = ChartFactory.createScatterPlot
		("Weighted Support Vector Machine", "X", "Y", model.getRawDataSet());

        return new SVMPanel(chart, model);
	}





	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source instanceof JMenuItem){
			JMenuItem mItem = (JMenuItem) source;
			//TODO if (mItem.equals(obj)
		}
		
	}
}
