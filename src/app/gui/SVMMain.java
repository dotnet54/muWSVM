package app.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import app.model.data.IObserver;
import app.model.data.SVMModel;
import app.test.PerformanceMatrix;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.awt.Panel;
import java.awt.FlowLayout;
import java.awt.Dimension;

/**
 * Main GUI window for the application
 * 
 * @author shifaz
 *
 */
public class SVMMain implements ActionListener, IObserver{

	//Model globals
	private SVMModel model = null;
	
	//GUI globals
	private JFrame frame;
	private SVMSPLOM frameSPLOM;
	private JFreeChart chart;
	public static SVMPanel chartPanel;	//TODO change static public, only for debug
	
	//components
	private JTextField textField_class1;
	private JTextField textField_class_2;
	private JSlider slider_class1;
	private JSlider slider_class2;
	private JLabel lblmuInvVal_class1 = new JLabel("1");
	private JLabel lblmuInvVal_class2 = new JLabel("1");
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
	
	private JLabel lblSelectedPoint;
	
	private JCheckBox chckbxUsemuScale;
	private JCheckBox chckbxUsemuScale_1;
	private JCheckBox chckbxAutoUpdate;
	private JCheckBox chckbxAutoUpdateWsvm;
	private JCheckBox chckbxUseSameParameters;
	
	private final SVMAddItemDialog addItemDialog = new SVMAddItemDialog();
	private final SVMAboutDialog aboutDialog = new SVMAboutDialog();
	private final SVMHelpDialog helpDialog = new SVMHelpDialog();
	private final JFileChooser fc = new JFileChooser(".");
	private JTextField numDimensions;

	private JRadioButton rdbtnAddTool;

	private JRadioButton rdbtnSelectTool;

	private JRadioButton rdbtnRemoveTool;

	private JRadioButton rdbtnDuplicateTool;

	private JRadioButton rdbtnWeightingTool;

	private JComboBox<String> cmbNewClass;

	private JPanel panelPerformance;

	private PerformanceMatrix perfMatrix;

	private JComboBox<String> xDimensionName;
	private JComboBox<String> yDimensionName;
	
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

			JPanel pChartContainer = new JPanel();
			pChartContainer.setBounds(10, 11, 450, 454);
	        
	        chartPanel = createChartPanel();
	        chartPanel.register(this);
	        pChartContainer.setLayout(new BorderLayout(0, 0));
	        pChartContainer.add(chartPanel, BorderLayout.CENTER);
	        chartPanel.setBackground(Color.WHITE);
			
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.setBounds(514, 11, 313, 372);
			
			JPanel pSolveButtonsContainer = new JPanel();
			pSolveButtonsContainer.setBounds(514, 390, 313, 52);
			pSolveButtonsContainer.setLayout(null);
			
			JButton btnFindWeightedReduced = new JButton("Find Weighted RCH");
			btnFindWeightedReduced.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO try catch parse integer
					if (chckbxUseSameParameters.isSelected()){
						model.setMu(Double.parseDouble(textField_class1.getText()),
							 	Double.parseDouble(textField_class1.getText()));
					}else{
						model.setMu(Double.parseDouble(textField_class1.getText()),
							 	Double.parseDouble(textField_class_2.getText()));
					}
					
					model.solveWRCH(2);
					chartPanel.updateWRCHSolutions();
				}
			});
			btnFindWeightedReduced.setBounds(0, 0, 125, 23);
			pSolveButtonsContainer.add(btnFindWeightedReduced);
			
			JButton btnTrainWSVM = new JButton("Train Weighted SVM");
			btnTrainWSVM.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (chckbxUseSameParameters.isSelected()){
						solveSVM(Double.parseDouble(textField_class1.getText()),
							 	Double.parseDouble(textField_class1.getText()));
					}else{
						solveSVM(Double.parseDouble(textField_class1.getText()),
							 	Double.parseDouble(textField_class_2.getText()));
					}
				}
			});
			btnTrainWSVM.setBounds(0, 29, 129, 23);
			pSolveButtonsContainer.add(btnTrainWSVM);
			
			JPanel pContainerParameters = new JPanel();
			tabbedPane.addTab("Parameters", null, pContainerParameters, null);
			pContainerParameters.setLayout(null);
			
			JPanel pClass1 = new JPanel();
			pClass1.setBounds(10, 58, 259, 120);
			pContainerParameters.add(pClass1);
			
			slider_class1 = new JSlider();
			setupSlider(slider_class1, true, 100);
			
			slider_class1.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					double m1 = (double) j.getValue();
					double m2 = Double.parseDouble(textField_class_2.getText());
					//TODO try catch parse double, format label string
					
					if (chckbxUsemuScale.isSelected()){
						textField_class1.setText(format(1 / m1));
						lblmuInvVal_class1.setText(format(m1));
						if (chckbxAutoUpdate.isSelected()){
							if (chckbxUseSameParameters.isSelected()){
								findWRCH(1 / m1, 1 / m1);
							}else{
								findWRCH(1 / m1, m2);
							}
						}
						if(chckbxAutoUpdateWsvm.isSelected()){
							if (chckbxUseSameParameters.isSelected()){
								solveSVM(1 / m1, 1 / m1);
							}else{
								solveSVM(1 / m1, m2);
							}
						}
					}else{
						m1 = m1 / 100.0;
						textField_class1.setText(format(m1));
						lblmuInvVal_class1.setText(format(1 / m1));
						if (chckbxAutoUpdate.isSelected()){
							if (chckbxUseSameParameters.isSelected()){
								findWRCH(m1, m1);
							}else{
								findWRCH(m1, m2);
							}
						}
						if(chckbxAutoUpdateWsvm.isSelected()){
							if (chckbxUseSameParameters.isSelected()){
								solveSVM(m1, m1);
							}else{
								solveSVM(m1, m2);
							}
						}
					}

					for (ActionListener a : textField_class1.getActionListeners()) {
						a.actionPerformed(new ActionEvent(j,
								ActionEvent.ACTION_PERFORMED, " slider 1 changed") {
									private static final long serialVersionUID = 1L;
							// Nothing need go here, the actionPerformed method
							// (with the  above arguments) will trigger the respective listener
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
			textField_class1.setBounds(58, 72, 82, 16);
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
						//System.out.println("actionPerformed " + model.getMu1() + arg0.getActionCommand());
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
			lblmuInvVal_class1.setBounds(58, 95, 82, 14);
			pClass1.add(lblmuInvVal_class1);
			pClass1.add(slider_class1);
			
			
			chckbxUsemuScale = new JCheckBox("Use 1/mu scale");
			chckbxUsemuScale.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO bug fix, when #points change, scale is not updated, implement observer
					if (chckbxUsemuScale.isSelected()){
						setupSlider(slider_class1, false, model.getChartDataset().getSeries(0).getItemCount());
					}else{
						setupSlider(slider_class1, true, 100);
					}
				}
			});
			chckbxUsemuScale.setBounds(156, 91, 97, 23);
			pClass1.add(chckbxUsemuScale);
			
			JPanel pClass2 = new JPanel();
			pClass2.setBounds(10, 189, 259, 120);
			pContainerParameters.add(pClass2);
			
			slider_class2 = new JSlider();
			setupSlider(slider_class2, true, 100);
			slider_class2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					double m1 = Double.parseDouble(textField_class1.getText());
					double m2 =  (double) j.getValue();
					//TODO try catch parse double, format label string
					
					if (chckbxUsemuScale_1.isSelected()){
						textField_class_2.setText(format(1 / m2));
						lblmuInvVal_class2.setText(format(m2));
						if (chckbxAutoUpdate.isSelected()){
							findWRCH(m1, 1/m2);
						}
						if(chckbxAutoUpdateWsvm.isSelected()){
							solveSVM(m1, 1/m2);
						}
					}else{
						m2 = m2 / 100.0;
						textField_class_2.setText(format(m2));
						lblmuInvVal_class2.setText(format(1 / m2));
						if (chckbxAutoUpdate.isSelected()){
							findWRCH(m1, m2);
						}
						if(chckbxAutoUpdateWsvm.isSelected()){
							solveSVM(m1, m2);
						}
					}
					
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

			
			JLabel lblmu_class2 = new JLabel("mu");
			lblmu_class2.setBounds(10, 70, 14, 14);
			
			textField_class_2 = new JTextField();
			textField_class_2.setBounds(55, 70, 86, 14);
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
			lblmuInvVal_class2.setBounds(55, 95, 86, 14);
			pClass2.add(lblmuInvVal_class2);
			pClass2.add(lblNewLabel_5);
			pClass2.add(slider_class2);
			
			chckbxUsemuScale_1 = new JCheckBox("Use 1/mu scale");
			chckbxUsemuScale_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (chckbxUsemuScale_1.isSelected()){
						setupSlider(slider_class2, false, model.getChartDataset().getSeries(1).getItemCount());
					}else{
						setupSlider(slider_class2, true, 100);
					}
				}
			});
			chckbxUsemuScale_1.setBounds(156, 91, 97, 23);
			pClass2.add(chckbxUsemuScale_1);
			
			chckbxUseSameParameters = new JCheckBox("Use same parameters for two classes");
			chckbxUseSameParameters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					slider_class2.setEnabled(!slider_class2.isEnabled());
					textField_class_2.setEnabled(!textField_class_2.isEnabled());
					chckbxUsemuScale_1.setEnabled(!chckbxUsemuScale_1.isEnabled());
				}
			});
			chckbxUseSameParameters.setBounds(10, 28, 231, 23);
			pContainerParameters.add(chckbxUseSameParameters);
			
			chckbxAutoUpdate = new JCheckBox("Auto update WRCH");
			chckbxAutoUpdate.setSelected(true);
			chckbxAutoUpdate.setBounds(10, 2, 119, 23);
			pContainerParameters.add(chckbxAutoUpdate);
			
			chckbxAutoUpdateWsvm = new JCheckBox("Auto update hyperplane");
			chckbxAutoUpdateWsvm.setBounds(144, 2, 158, 23);
			pContainerParameters.add(chckbxAutoUpdateWsvm);
			
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
					chart.getXYPlot().setDataset(0, model.getChartDataset()); 
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
				        	model.getChartDataset().loadFromFile(fc.getSelectedFile());
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
			lblPercentPos.setBounds(10, 140, 157, 14);
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
			
			JButton btnSaveData = new JButton("Save Data");
			btnSaveData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File("wsvm_out.txt");
					model.getChartDataset().saveToFile(file);
				}
			});
			btnSaveData.setBounds(209, 294, 89, 23);
			pContainerTrainingData.add(btnSaveData);
			
			numDimensions = new JTextField();
			numDimensions.setBounds(165, 109, 104, 20);
			pContainerTrainingData.add(numDimensions);
			numDimensions.setColumns(10);
			
			JLabel lblNumberOfDimensions = new JLabel("Number of dimensions");
			lblNumberOfDimensions.setBounds(164, 92, 105, 14);
			pContainerTrainingData.add(lblNumberOfDimensions);
			
			JPanel pContainerDataEditing = new JPanel();
			tabbedPane.addTab("Data Editing", null, pContainerDataEditing, null);
			pContainerDataEditing.setLayout(null);
			
			textField_NewWeight = new JTextField();
			textField_NewWeight.setBounds(242, 210, 46, 20);
			pContainerDataEditing.add(textField_NewWeight);
			textField_NewWeight.setText("1");
			textField_NewWeight.setColumns(10);
			
			JLabel lblNewWeight = new JLabel("New Weight:");
			lblNewWeight.setBounds(155, 213, 77, 14);
			pContainerDataEditing.add(lblNewWeight);
			
			JLabel lblCurrentSelection = new JLabel("Current Selection:");
			lblCurrentSelection.setBounds(10, 11, 94, 14);
			pContainerDataEditing.add(lblCurrentSelection);
			
			lblSelectedPoint = new JLabel("None");
			lblSelectedPoint.setBounds(10, 36, 259, 40);
			pContainerDataEditing.add(lblSelectedPoint);
			
			JLabel lblNewLabel_10 = new JLabel("Select option and click chart/points to edit");
			lblNewLabel_10.setBounds(10, 87, 259, 14);
			pContainerDataEditing.add(lblNewLabel_10);
			
			textField_NumDuplications = new JTextField();
			textField_NumDuplications.setText("1");
			textField_NumDuplications.setBounds(242, 179, 46, 20);
			pContainerDataEditing.add(textField_NumDuplications);
			textField_NumDuplications.setColumns(10);
			
			JLabel lblNumDuplications = new JLabel("Number of Duplications:");
			lblNumDuplications.setBounds(125, 182, 122, 14);
			pContainerDataEditing.add(lblNumDuplications);
			
			cmbNewClass = new JComboBox<String>();
			cmbNewClass.setModel(new DefaultComboBoxModel<String>(new String[] {"Positive Class", "Negative Class"}));
			cmbNewClass.setBounds(198, 241, 90, 20);
			pContainerDataEditing.add(cmbNewClass);
			
			JLabel lblNewClass = new JLabel("New Class:");
			lblNewClass.setBounds(128, 244, 60, 14);
			pContainerDataEditing.add(lblNewClass);
			
			rdbtnSelectTool = new JRadioButton("Select Point");
			rdbtnSelectTool.setSelected(true);
			rdbtnSelectTool.setBounds(10, 119, 109, 23);
			pContainerDataEditing.add(rdbtnSelectTool);
			
			rdbtnAddTool = new JRadioButton("Add Point");
			rdbtnAddTool.setBounds(10, 233, 77, 23);
			pContainerDataEditing.add(rdbtnAddTool);
			
			rdbtnRemoveTool = new JRadioButton("Remove Point");
			rdbtnRemoveTool.setBounds(10, 145, 109, 23);
			pContainerDataEditing.add(rdbtnRemoveTool);
			
			rdbtnDuplicateTool = new JRadioButton("Duplicate Point");
			rdbtnDuplicateTool.setBounds(10, 178, 109, 23);
			pContainerDataEditing.add(rdbtnDuplicateTool);
			
			rdbtnWeightingTool = new JRadioButton("Change Weight");
			rdbtnWeightingTool.setBounds(10, 204, 109, 23);
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
			btnAddPoint.setBounds(199, 299, 89, 23);
			pContainerDataEditing.add(btnAddPoint);
			
			JLabel lblDefineExactPoint = new JLabel("Add to an exact location:");
			lblDefineExactPoint.setBounds(20, 278, 129, 14);
			pContainerDataEditing.add(lblDefineExactPoint);
			
			JPanel pContainerPerformance = new JPanel();
			tabbedPane.addTab("Performance", null, pContainerPerformance, null);
			pContainerPerformance.setLayout(null);
			
			JLabel lblConfusionMatrix = new JLabel("Confusion Matrix");
			lblConfusionMatrix.setBounds(10, 11, 102, 14);
			pContainerPerformance.add(lblConfusionMatrix);
			
			JLabel lblNumberOfSupport = new JLabel("<html>Num. Pos.\r\n<br> Support Vectors");
			lblNumberOfSupport.setBounds(20, 273, 77, 28);
			pContainerPerformance.add(lblNumberOfSupport);
			
			panelPerformance = new JPanel();
			panelPerformance.setBounds(10, 36, 288, 225);
			pContainerPerformance.add(panelPerformance);
			perfMatrix = new PerformanceMatrix();
			panelPerformance.add(perfMatrix);
			
			JLabel lblTimemsForWsk = new JLabel("Time(ms) for WSK");
			lblTimemsForWsk.setBounds(113, 273, 109, 14);
			pContainerPerformance.add(lblTimemsForWsk);
			
			JLabel lblTimemsForWrch = new JLabel("Time(ms) for WRCH");
			lblTimemsForWrch.setBounds(107, 298, 99, 14);
			pContainerPerformance.add(lblTimemsForWrch);
			
			JLabel lblNewLabel = new JLabel("numIterations");
			lblNewLabel.setBounds(220, 273, 78, 14);
			pContainerPerformance.add(lblNewLabel);
			frame.getContentPane().setLayout(null);
			frame.getContentPane().add(pChartContainer);
			
			Panel panel = new Panel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			pChartContainer.add(panel, BorderLayout.NORTH);
			
			JLabel lblXaxisAttribute = new JLabel("X-axis Attribute");
			panel.add(lblXaxisAttribute);
			
			xDimensionName = new JComboBox<String>();
			xDimensionName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JComboBox<String> combo = (JComboBox<String>) e.getSource();
					chart.getXYPlot().getDomainAxis().setLabel(
							model.getTestData().getAttributeNames()[combo.getSelectedIndex()]);
					changeChartData();
				}
			});
			xDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getAttributeNames()));
			xDimensionName.setMinimumSize(new Dimension(40, 20));
			xDimensionName.setSelectedIndex(0);
			panel.add(xDimensionName);
			
			JLabel lblYaxisAttribute = new JLabel("Y-axis Attribute");
			panel.add(lblYaxisAttribute);
			
			yDimensionName = new JComboBox<String>();
			yDimensionName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox<String> combo = (JComboBox<String>) e.getSource();
					chart.getXYPlot().getRangeAxis().setLabel(
							model.getTestData().getAttributeNames()[combo.getSelectedIndex()]);	
					changeChartData();
					
					}
					
			});
			yDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getAttributeNames()));
			yDimensionName.setMinimumSize(new Dimension(40, 20));
			yDimensionName.setSelectedIndex(1); //TODO assumption index 1 exist
			panel.add(yDimensionName);
			frame.getContentPane().add(pSolveButtonsContainer);
			
			JButton btnClassify = new JButton("Classify");
			btnClassify.setBounds(214, 29, 89, 23);
			pSolveButtonsContainer.add(btnClassify);
			
			JButton btndScatterMatrix = new JButton("2D Scatter Matrix");
			btndScatterMatrix.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showScatterPlotMatrix();
				}
			});
			btndScatterMatrix.setBounds(196, 0, 117, 23);
			pSolveButtonsContainer.add(btndScatterMatrix);
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
			pContainerTestingData.setLayout(null);
			
			JButton btnLoadTestingData = new JButton("Load Testing Data");
			btnLoadTestingData.setBounds(10, 11, 119, 23);
			pContainerTestingData.add(btnLoadTestingData);
			
			JButton btnPerformCrossValidation = new JButton("Perform Cross Validation");
			btnPerformCrossValidation.setBounds(10, 55, 159, 23);
			pContainerTestingData.add(btnPerformCrossValidation);
			
			Panel pContainerWeighting = new Panel();
			tabbedPane.addTab("Weighting", null, pContainerWeighting, null);
			pContainerWeighting.setLayout(null);
			
			JButton btnApplyAutoWeighting = new JButton("Apply Auto Weighting");
			btnApplyAutoWeighting.setBounds(10, 80, 145, 23);
			pContainerWeighting.add(btnApplyAutoWeighting);
			
			JComboBox cmbWeightingSchemes = new JComboBox();
			cmbWeightingSchemes.setModel(new DefaultComboBoxModel(new String[] {"Reduce weight of old data", "Reduce weight of uncertain data"}));
			cmbWeightingSchemes.setBounds(10, 37, 186, 20);
			pContainerWeighting.add(cmbWeightingSchemes);
			
			JButton btnNewButton = new JButton("Weight based on clusters");
			btnNewButton.setBounds(10, 114, 168, 23);
			pContainerWeighting.add(btnNewButton);
			
			JCheckBox chckbxEnableAutoWeighting = new JCheckBox("Enable Auto weighting");
			chckbxEnableAutoWeighting.setBounds(10, 7, 168, 23);
			pContainerWeighting.add(chckbxEnableAutoWeighting);
			
			JButton btnRandomUndersample = new JButton("Random Undersample");
			btnRandomUndersample.setBounds(10, 260, 145, 23);
			pContainerWeighting.add(btnRandomUndersample);
			
			JButton btnRandomOversample = new JButton("Random Oversample");
			btnRandomOversample.setBounds(10, 294, 145, 23);
			pContainerWeighting.add(btnRandomOversample);
			
			JButton btnSystematicUndersample = new JButton("Systematic Undersample");
			btnSystematicUndersample.setBounds(10, 194, 168, 23);
			pContainerWeighting.add(btnSystematicUndersample);
			
			JButton btnSystematicOversample = new JButton("Systematic Oversample");
			btnSystematicOversample.setBounds(10, 228, 168, 23);
			pContainerWeighting.add(btnSystematicOversample);
			
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
					model.loadPredefinedDataset("Two points 1");
				}
			});
			mnData.add(mntmTwoPoints);
			
			JMenuItem mntmCollinearPoints = new JMenuItem("Collinear Points 1");
			mntmCollinearPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Collinear Points 1");
				}
			});
			mnData.add(mntmCollinearPoints);
			
			JMenuItem mntmTriangle = new JMenuItem("Triangle 1");
			mntmTriangle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Triangle 1");
				}
			});
			
			JMenuItem mntmCollinearPoints_1 = new JMenuItem("Collinear Points 2");
			mntmCollinearPoints_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Collinear Points 2");
					
				}
			});
			mnData.add(mntmCollinearPoints_1);
			mnData.add(mntmTriangle);
			
			JMenuItem mntmRombus = new JMenuItem("Parallelogram");
			mntmRombus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Parallelogram");
				}
			});
			mnData.add(mntmRombus);
			
			JMenuItem mntmTrapezium = new JMenuItem("Trapezium");
			mntmTrapezium.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Trapezium");
				}
			});
			mnData.add(mntmTrapezium);
			
			JMenuItem mntmRandom = new JMenuItem("Random");
			mntmRandom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.getChartDataset().clear();
					model.generateRandomData(//TODO make this completely random
							10,
							50, 
							0);
				}
			});
			
			JMenuItem mntmTPoints = new JMenuItem("T points");
			mntmTPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("T points");
				}
			});
			mnData.add(mntmTPoints);
			mnData.add(mntmRandom);
			
			JMenuItem mntmNewMenuItem = new JMenuItem("Triangle 2");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Triangle 2");
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







	
    /**
     * Creates a customized JFreechart panel to draw the chart
     * 
     * @return
     */
	private SVMPanel createChartPanel(){

		chart = ChartFactory.createScatterPlot
		("Weighted Support Vector Machine", "X", "Y", model.getChartDataset());

        return new SVMPanel(chart, model);
	}
	
	private void changeChartData(){
		int xDim = 0;
		int yDim = 0; 
		if (xDimensionName != null){
			xDim = xDimensionName.getSelectedIndex();
		}
		if (yDimensionName != null){
			xDim = yDimensionName.getSelectedIndex();
		}
		//use model
		//chart.getXYPlot().setDataset(model.getTrainingData().getChartData(xDim, yDim));
	}

	private void solveSVM(double mu1, double mu2){
		model.setMu(mu1, mu2);
		model.solveSVM();
		chartPanel.updateWSVMSolutions();
		
		perfMatrix.setValue(0, 0, model.numTruePositives);
		perfMatrix.setValue(0, 1, model.numFalseNegatives);
		perfMatrix.setValue(1, 0, model.numFalsePositives);
		perfMatrix.setValue(1, 1, model.numTrueNegatives);
	}
	
	private void findWRCH(double mu1, double mu2){
		model.setMu(mu1, mu2);
		model.solveWRCH(0);
		model.solveWRCH(1);
		chartPanel.updateWRCHSolutions();
	}
	
	private void showScatterPlotMatrix(){

		frameSPLOM = new SVMSPLOM(model);
		
		frameSPLOM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameSPLOM.setPreferredSize(new Dimension(600,400));
		frameSPLOM.pack();
		frameSPLOM.setVisible(true);
	}

	private JSlider setupSlider(JSlider slider, boolean useMuScale, int maxValue){
	    java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();  
	    labelTable.put(new Integer(100), new JLabel("1.0"));  
	    labelTable.put(new Integer(75), new JLabel("0.75"));  
	    labelTable.put(new Integer(50), new JLabel("0.50"));  
	    labelTable.put(new Integer(25), new JLabel("0.25"));  
	    labelTable.put(new Integer(0), new JLabel("0.0"));
	    
	    slider.setBounds(0, 26, 200, 40);
	    slider.setFont(new Font("Tahoma", Font.PLAIN, 8));	    
	    
	    slider.setLabelTable(null);
	    
	    if (useMuScale){
	    	slider.setMajorTickSpacing(25);
	    	slider.setLabelTable(labelTable);
	    	slider.setMinimum(0);
	    	slider.setMaximum(100);
	    }else{
	    	slider.setMajorTickSpacing(maxValue/2);
	    	//slider.setLabelTable(slider.createStandardLabels(10));
	    	slider.setMinimum(0);
	    	slider.setMaximum(maxValue);
	    }
	    
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		return slider;
	}
	

	public String format(double d){
		return String.format("%.4f", d);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source instanceof JMenuItem){
			JMenuItem mItem = (JMenuItem) source;
			//TODO if (mItem.equals(obj)
		}
		
	}





	@Override
	public void update() {
		if (chartPanel.getSelectedDataItem() != null){
			lblSelectedPoint.setText(chartPanel.getSelectedDataItem().toFormatedString(2));
		}else{
			lblSelectedPoint.setText("None");
		}
		
		if (rdbtnRemoveTool.isSelected()){
			chartPanel.removeSelection();
		}else if (rdbtnDuplicateTool.isSelected()){
			
			int duplications = Integer.parseInt(textField_NumDuplications.getText());
			chartPanel.duplicateSelection(duplications);
		}else if (rdbtnAddTool.isSelected()){
			if (cmbNewClass.getSelectedIndex() == 0){ //asumption 0 is positive class
				chartPanel.addPoint(0);
			}else if (cmbNewClass.getSelectedIndex() == 1){
				chartPanel.addPoint(1);
			}
		}else if (rdbtnWeightingTool.isSelected()){
			
		}
	}
}
