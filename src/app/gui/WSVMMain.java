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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import app.model.DoubleMath;
import app.model.IObserver;
import app.model.WSVMModel;
import app.model.WSKSolver;
import app.model.data.WSVMDataItem;
import app.model.data.WSVMDataSet;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
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
import javax.swing.JRadioButtonMenuItem;

/**
 * Main GUI window for the application
 * 
 * @author shifaz
 *
 */
public class WSVMMain implements IObserver, DatasetChangeListener{

	//Model globals
	private WSVMModel model = null;
	
	//GUI globals
	private JFrame frame;
	private WSVMSPLOM frameSPLOM;
	private JFreeChart chart;
	private WSVMPanel chartPanel;
	
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
	
	private JLabel lblSelectedPoint;
	
	private JCheckBox chckbxUsemuScale;
	private JCheckBox chckbxUsemuScale_1;
	private JCheckBox chckbxAutoUpdate;
	private JCheckBox chckbxAutoUpdateWsvm;
	private JCheckBox chckbxUseSameParameters;
	
	private final WSVMAboutDialog aboutDialog = new WSVMAboutDialog();
	private final JFileChooser fc;
	private JTextField txtNumDimensions;

	private JRadioButton rdbtnAddTool;
	private JRadioButton rdbtnSelectTool;
	private JRadioButton rdbtnRemoveTool;
	private JRadioButton rdbtnDuplicateTool;
	private JRadioButton rdbtnWeightingTool;
	private JComboBox<String> cmbNewClass;
	private JPanel panelPerformance;
	private PerformanceMatrix perfMatrix;

	private JComboBox<String> cmbXDimensionName;
	private JComboBox<String> cmbYDimensionName;
	private JTextField txtMin;
	private JTextField txtMax;

	private FileNameExtensionFilter csvFilter;

	private FileNameExtensionFilter libsvmFilter;
	private JTextField txtMinweight;
	private JTextField txtMaxweight;

	private JLabel lblStatus;

	private JComboBox<String> cmbSVMType;
	private JTextField txtWskepsilon;
	
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
					WSVMMain window = new WSVMMain();
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
	public WSVMMain() {
		//Initialize Model
		model = new WSVMModel();
		
		model.getTrainingData().addChangeListener(this);
		model.register(this);

		//Initialize GUI
		fc = new JFileChooser(".");
		//CSV read/write not implemented in this version but the filters are setup
		csvFilter = new FileNameExtensionFilter(
		        "Comma Separated Values File (CSV)", "csv", "gif");
		libsvmFilter = new FileNameExtensionFilter(
		        "LibSVM File", "libsvm", "dat");
		fc.addChoosableFileFilter(csvFilter);
		fc.addChoosableFileFilter(libsvmFilter);
		fc.setAcceptAllFileFilterUsed(true);
		fc.setFileFilter(fc.getAcceptAllFileFilter());
		
		
		
		initializeGUI();
		
		textField_FPPrecision.setText(DoubleMath.PRECISION + "");
		textField_NumDP.setText(DoubleMath.DP + "");
		textField_MaxIterationsWSK.setText(WSKSolver.maxIterations + "");
		txtWskepsilon.setText(WSKSolver.epsilon + "");
		
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
			frame.setBounds(100, 100, 845, 538);
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
					try {
						if (chckbxUseSameParameters.isSelected()){
							model.setMu(Double.parseDouble(textField_class1.getText()),
								 	Double.parseDouble(textField_class1.getText()));
						}else{
							model.setMu(Double.parseDouble(textField_class1.getText()),
								 	Double.parseDouble(textField_class_2.getText()));
						}
						
						model.solveWRCH(2);
						chartPanel.updateWRCHSolutions();
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Invalid input values",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnFindWeightedReduced.setBounds(0, 0, 125, 23);
			pSolveButtonsContainer.add(btnFindWeightedReduced);
			
			JButton btnTrainWSVM = new JButton("Train Weighted SVM");
			btnTrainWSVM.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (chckbxUseSameParameters.isSelected()){
							solveSVM(Double.parseDouble(textField_class1.getText()),
								 	Double.parseDouble(textField_class1.getText()));
						}else{
							solveSVM(Double.parseDouble(textField_class1.getText()),
								 	Double.parseDouble(textField_class_2.getText()));
						}
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Invalid input values",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
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
			slider_class1.setValue(100);
			setupSlider(slider_class1, true, 100);
			
			slider_class1.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					double m1 = (double) j.getValue();
					double m2;
					m2 = Double.parseDouble(textField_class_2.getText());

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

			textField_class1.setText("1");
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
					if (chckbxUsemuScale.isSelected()){
						setupSlider(slider_class1, false, model.getTrainingData().getSeries(0).getItemCount());
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
			slider_class2.setValue(100);
			setupSlider(slider_class2, true, 100);
			slider_class2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					JSlider j = (JSlider) arg0.getSource();
					double m1 = Double.parseDouble(textField_class1.getText());
					double m2 =  (double) j.getValue();

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
			textField_class_2.setText("1");
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
						setupSlider(slider_class2, false, model.getTrainingData().getSeries(1).getItemCount());
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
			chckbxAutoUpdate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JCheckBox chk = (JCheckBox) e.getSource();

					if (!chk.isSelected()){
						chartPanel.clearWRCH();
					}

				}
			});
			chckbxAutoUpdate.setSelected(true);
			chckbxAutoUpdate.setBounds(10, 2, 119, 23);
			pContainerParameters.add(chckbxAutoUpdate);
			
			chckbxAutoUpdateWsvm = new JCheckBox("Auto update hyperplane");
			chckbxAutoUpdateWsvm.setBounds(144, 2, 158, 23);
			pContainerParameters.add(chckbxAutoUpdateWsvm);
			
			JPanel pContainerLoadData = new JPanel();
			tabbedPane.addTab("Load Data", null, pContainerLoadData, null);
			pContainerLoadData.setLayout(null);
			
			final JComboBox<String> cmbDataType = new JComboBox<String>();
			cmbDataType.setBounds(100, 7, 156, 23);
			cmbDataType.setModel(new DefaultComboBoxModel<String>(new String[] {"Training Data", "Test Data"}));
			pContainerLoadData.add(cmbDataType);
			
			JButton btnGenerateData = new JButton("Generate Random Data");
			btnGenerateData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int numDims = Integer.parseInt(txtNumDimensions.getText());
						int numPoints = Integer.parseInt(textField_NumDataPoints.getText());
						int percentPos = Integer.parseInt(textField_PercentPos.getText());
						double min = Double.parseDouble(txtMin.getText());
						double max = Double.parseDouble(txtMax.getText());
						double minW = Double.parseDouble(txtMinweight.getText());
						double maxW = Double.parseDouble(txtMaxweight.getText());
						
						if (numDims < 2 ){
							JOptionPane.showMessageDialog(frame,
								    "Dimension is too low or invalid",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (numDims != 2){
							
							int reply = JOptionPane.showConfirmDialog(null, 
									"If dimensions are greater than 2, some features might not work in this version \nAre you sure you want to change the number of dimensions?", "Close?",  JOptionPane.YES_NO_OPTION);
							if (reply == JOptionPane.NO_OPTION)
							{
							   return;
							}

						}
						
						if (min > max){
							JOptionPane.showMessageDialog(frame,
								    "Min Value should be less than Max Value",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (minW > maxW){
							JOptionPane.showMessageDialog(frame,
								    "Min Weight should be less than Max Weight",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (minW < 0){
							JOptionPane.showMessageDialog(frame,
								    "Weight should be positive",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						String svmType = cmbSVMType.getSelectedItem().toString();
						
						if (cmbDataType.getSelectedIndex() == 0){
							model.generateRandomTrainingData(numDims,
								numPoints, percentPos, svmType, min, max, minW, maxW);
							//dataset 0 for training set
							chart.getXYPlot().setDataset(0, model.getTrainingData()); 
						}else if(cmbDataType.getSelectedIndex() == 1){
							model.generateRandomTestData
							(numDims, numPoints, percentPos, svmType, min, max, minW, maxW);
							//dataset 2 for training set
							chart.getXYPlot().setDataset(2, model.getTestData());
						}
						
						chartPanel.thisPlot.getDomainAxis().setAutoRange(true);
						chartPanel.thisPlot.getRangeAxis().setAutoRange(true);
						
						cmbXDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getDimensionLabels()));
						cmbYDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getDimensionLabels()));
						cmbYDimensionName.setSelectedIndex(1);
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Invalid input values",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					} 
					
				}
			});
			btnGenerateData.setBounds(10, 294, 145, 23);
			pContainerLoadData.add(btnGenerateData);
			
			final JButton btnLoadData = new JButton("Load From File");
			btnLoadData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						
						WSVMDataSet dataset= null;
						
						if (cmbDataType.getSelectedItem().toString().equals("Test Data")){
							dataset = model.getTestData();
						}else{
							dataset = model.getTrainingData();
						}
						
						
				        int returnVal = fc.showOpenDialog(frame);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				        	
				        	File file = fc.getSelectedFile();
				        	String file_name = file.toString();
				        	if (file_name.endsWith("libsvm") || file_name.endsWith("dat")){
				        		dataset.loadFromFile(file_name, "libsvm");
				        	}else{
				        		dataset.loadFromFile(file_name, "libsvm"); //default
				        	}
				        } 
				        
						chartPanel.thisPlot.getDomainAxis().setAutoRange(true);
						chartPanel.thisPlot.getRangeAxis().setAutoRange(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnLoadData.setBounds(10, 41, 118, 23);
			pContainerLoadData.add(btnLoadData);
			
			JLabel lblNumDataPoints = new JLabel("Number of Data Points:");
			lblNumDataPoints.setBounds(10, 175, 155, 14);
			pContainerLoadData.add(lblNumDataPoints);
			
			textField_NumDataPoints = new JTextField();
			textField_NumDataPoints.setText("10");
			textField_NumDataPoints.setBounds(10, 200, 105, 20);
			pContainerLoadData.add(textField_NumDataPoints);
			textField_NumDataPoints.setColumns(6);
			
			JLabel lblPercentPos = new JLabel("Percentage of Positive Data:");
			lblPercentPos.setBounds(10, 231, 157, 14);
			pContainerLoadData.add(lblPercentPos);
			
			textField_PercentPos = new JTextField();
			textField_PercentPos.setText("50");
			textField_PercentPos.setBounds(177, 228, 46, 20);
			pContainerLoadData.add(textField_PercentPos);
			textField_PercentPos.setColumns(3);
			
			JLabel lblDatasetType = new JLabel("Dataset Type");
			lblDatasetType.setBounds(11, 11, 79, 14);
			pContainerLoadData.add(lblDatasetType);
			
			JButton btnClearPlot = new JButton("Clear Plot");
			btnClearPlot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.clearPlot();
				}
			});
			btnClearPlot.setBounds(219, 294, 79, 23);
			pContainerLoadData.add(btnClearPlot);
			
			JLabel lblOverlapDelta = new JLabel("SVM Type:");
			lblOverlapDelta.setBounds(10, 256, 104, 14);
			pContainerLoadData.add(lblOverlapDelta);
			
			JButton btnSaveData = new JButton("Save To File");
			btnSaveData.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
				        int returnVal = fc.showSaveDialog(frame);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				        	
				        	File file = fc.getSelectedFile();
				        	String file_name = file.toString();
				        	//future work: note only libsvm format is supported atm, but this is added here to support csv in future
				        	if (fc.getFileFilter() == libsvmFilter 
				        			&& (!file_name.endsWith("libsvm") || !file_name.endsWith("dat"))){
				        		// file_name += ".libsvm";
				        		model.getTrainingData().saveToFile(
				        				file_name, "libsvm");
				        	}else{
				        		model.getTrainingData().saveToFile(
				        				file_name, "libsvm"); //default
				        	}
				        	
				        } 
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			btnSaveData.setBounds(164, 41, 132, 23);
			pContainerLoadData.add(btnSaveData);
			
			txtNumDimensions = new JTextField();
			txtNumDimensions.setEnabled(false);
			txtNumDimensions.setText("2");
			txtNumDimensions.setBounds(128, 141, 27, 20);
			pContainerLoadData.add(txtNumDimensions);
			txtNumDimensions.setColumns(2);
			
			JLabel lblNumberOfDimensions = new JLabel("Number of dimensions");
			lblNumberOfDimensions.setBounds(10, 144, 105, 14);
			pContainerLoadData.add(lblNumberOfDimensions);
			
			JLabel lblMin = new JLabel("Min Value:");
			lblMin.setBounds(10, 82, 64, 14);
			pContainerLoadData.add(lblMin);
			
			JLabel lblMax = new JLabel("Max Value:");
			lblMax.setBounds(10, 113, 64, 14);
			pContainerLoadData.add(lblMax);
			
			txtMin = new JTextField();
			txtMin.setText("0");
			txtMin.setBounds(128, 79, 86, 20);
			pContainerLoadData.add(txtMin);
			txtMin.setColumns(6);
			
			txtMax = new JTextField();
			txtMax.setText("10");
			txtMax.setBounds(128, 110, 86, 20);
			pContainerLoadData.add(txtMax);
			txtMax.setColumns(6);
			
			JLabel lblMinWeight = new JLabel("Min Weight:");
			lblMinWeight.setBounds(175, 164, 64, 14);
			pContainerLoadData.add(lblMinWeight);
			
			JLabel lblMaxWeight = new JLabel("Max Weight:");
			lblMaxWeight.setBounds(177, 189, 62, 14);
			pContainerLoadData.add(lblMaxWeight);
			
			txtMinweight = new JTextField();
			txtMinweight.setText("1");
			txtMinweight.setBounds(252, 164, 46, 20);
			pContainerLoadData.add(txtMinweight);
			txtMinweight.setColumns(10);
			
			txtMaxweight = new JTextField();
			txtMaxweight.setText("1");
			txtMaxweight.setBounds(252, 183, 46, 20);
			pContainerLoadData.add(txtMaxweight);
			txtMaxweight.setColumns(10);
			
			cmbSVMType = new JComboBox<String>();
			cmbSVMType.setModel(new DefaultComboBoxModel<String>(new String[] {"Hard Margin", "Soft Margin"}));
			cmbSVMType.setSelectedIndex(1);
			cmbSVMType.setBounds(124, 256, 99, 20);
			pContainerLoadData.add(cmbSVMType);
			
			JLabel lblIsExperimental = new JLabel(">3 is experimental");
			lblIsExperimental.setForeground(Color.RED);
			lblIsExperimental.setBounds(177, 144, 119, 14);
			pContainerLoadData.add(lblIsExperimental);
			
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
			cmbNewClass.setBounds(198, 146, 90, 20);
			pContainerDataEditing.add(cmbNewClass);
			
			JLabel lblNewClass = new JLabel("New Class:");
			lblNewClass.setBounds(128, 149, 60, 14);
			pContainerDataEditing.add(lblNewClass);
			
			rdbtnSelectTool = new JRadioButton("Select Point");
			rdbtnSelectTool.setSelected(true);
			rdbtnSelectTool.setBounds(10, 119, 109, 23);
			pContainerDataEditing.add(rdbtnSelectTool);
			
			rdbtnAddTool = new JRadioButton("Add Point");
			rdbtnAddTool.setBounds(10, 145, 77, 23);
			pContainerDataEditing.add(rdbtnAddTool);
			
			rdbtnRemoveTool = new JRadioButton("Remove Point");
			rdbtnRemoveTool.setBounds(10, 231, 109, 23);
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
			
			JPanel pContainerPerformance = new JPanel();
			tabbedPane.addTab("Performance", null, pContainerPerformance, null);
			pContainerPerformance.setLayout(null);
			
			JLabel lblConfusionMatrix = new JLabel("Confusion Matrix");
			lblConfusionMatrix.setBounds(10, 11, 102, 14);
			pContainerPerformance.add(lblConfusionMatrix);
			
			panelPerformance = new JPanel();
			panelPerformance.setBounds(10, 36, 288, 225);
			pContainerPerformance.add(panelPerformance);
			perfMatrix = new PerformanceMatrix();
			panelPerformance.add(perfMatrix);
			frame.getContentPane().setLayout(null);
			frame.getContentPane().add(pChartContainer);
			
			Panel panel = new Panel();
			FlowLayout flowLayout = (FlowLayout) panel.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			pChartContainer.add(panel, BorderLayout.NORTH);
			
			JLabel lblXaxisAttribute = new JLabel("X-axis Attribute");
			panel.add(lblXaxisAttribute);
			
			cmbXDimensionName = new JComboBox<String>();
			cmbXDimensionName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JComboBox<String> combo = (JComboBox<String>) e.getSource();
					chart.getXYPlot().getDomainAxis().setLabel(
							model.getTestData().getDimensionLabels()[combo.getSelectedIndex()]);
					changeChartData();
				}
			});
			cmbXDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getDimensionLabels()));
			cmbXDimensionName.setMinimumSize(new Dimension(40, 20));
			cmbXDimensionName.setSelectedIndex(0);
			panel.add(cmbXDimensionName);
			
			JLabel lblYaxisAttribute = new JLabel("Y-axis Attribute");
			panel.add(lblYaxisAttribute);
			
			cmbYDimensionName = new JComboBox<String>();
			cmbYDimensionName.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox<String> combo = (JComboBox<String>) e.getSource();
					chart.getXYPlot().getRangeAxis().setLabel(
							model.getTestData().getDimensionLabels()[combo.getSelectedIndex()]);	
					changeChartData();
					
					}
					
			});
			cmbYDimensionName.setModel(new DefaultComboBoxModel<String>(model.getTrainingData().getDimensionLabels()));
			cmbYDimensionName.setMinimumSize(new Dimension(40, 20));
			cmbYDimensionName.setSelectedIndex(1); // assumption index 1 exist
			panel.add(cmbYDimensionName);
			frame.getContentPane().add(pSolveButtonsContainer);
			
			JButton btnClassify = new JButton("Classify Test Data");
			btnClassify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.classifyTestData();	
				}
			});
			btnClassify.setBounds(188, 29, 125, 23);
			pSolveButtonsContainer.add(btnClassify);
			
			JButton btndScatterMatrix = new JButton("2D Scatter Matrix");
			btndScatterMatrix.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					if (model.getTrainingData().getDimensions() > 5){
						JOptionPane.showMessageDialog(frame,
							    "Too many dimensions to display SPLOM, limit dimensions to 5",
							    "Error",
							    JOptionPane.WARNING_MESSAGE);
					}else{
						showScatterPlotMatrix();
					}
					
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
			lblMaximumRecursionDepth.setBounds(10, 117, 191, 14);
			pContainerOptions.add(lblMaximumRecursionDepth);
			
			textField_MaxRecursionWRCH = new JTextField();
			textField_MaxRecursionWRCH.setEnabled(false);
			textField_MaxRecursionWRCH.setText("100");
			textField_MaxRecursionWRCH.setBounds(10, 149, 86, 20);
			pContainerOptions.add(textField_MaxRecursionWRCH);
			textField_MaxRecursionWRCH.setColumns(10);
			
			JLabel lblOrToo = new JLabel("If 0 or too large, this setting depends on Java stack size");
			lblOrToo.setBounds(10, 130, 288, 14);
			pContainerOptions.add(lblOrToo);
			
			JLabel lblFloatingPointThreshold = new JLabel("Floating Point Comparison Threshold");
			lblFloatingPointThreshold.setBounds(10, 180, 191, 14);
			pContainerOptions.add(lblFloatingPointThreshold);
			
			textField_FPPrecision = new JTextField();
			textField_FPPrecision.setText("0.001");
			textField_FPPrecision.setBounds(10, 205, 86, 20);
			pContainerOptions.add(textField_FPPrecision);
			textField_FPPrecision.setColumns(10);
			
			JLabel lblDecimalRounding = new JLabel("Number of Decimal Places");
			lblDecimalRounding.setBounds(10, 241, 225, 14);
			pContainerOptions.add(lblDecimalRounding);
			
			textField_NumDP = new JTextField();
			textField_NumDP.setText("2");
			textField_NumDP.setBounds(10, 263, 86, 20);
			pContainerOptions.add(textField_NumDP);
			textField_NumDP.setColumns(10);
			
			JButton btnSaveChanges = new JButton("Save Changes");
			btnSaveChanges.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
	
					try {
						DoubleMath.PRECISION = Double.parseDouble(textField_FPPrecision.getText());
						DoubleMath.DP = Integer.parseInt(textField_NumDP.getText());
						
						int temp = Integer.parseInt(textField_MaxIterationsWSK.getText());
						WSKSolver.maxIterations = temp; 
						if (temp > 0){
							WSKSolver.maxIterations = temp;
						}else{
							JOptionPane.showMessageDialog(frame,
								    "Invalid range for max iteration",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
						}
						
						WSKSolver.epsilon = Double.parseDouble(txtWskepsilon.getText());
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame,
							    "Invalid input values",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}

				}
			});
			btnSaveChanges.setBounds(184, 294, 114, 23);
			pContainerOptions.add(btnSaveChanges);
			
			txtWskepsilon = new JTextField();
			txtWskepsilon.setText("0.001");
			txtWskepsilon.setBounds(10, 86, 86, 20);
			pContainerOptions.add(txtWskepsilon);
			txtWskepsilon.setColumns(10);
			
			JLabel lblEpsilonstoppingDistance = new JLabel("Epsilon (Stopping distance for WSK)");
			lblEpsilonstoppingDistance.setBounds(10, 57, 180, 14);
			pContainerOptions.add(lblEpsilonstoppingDistance);
			
			Panel pContainerWeighting = new Panel();
			tabbedPane.addTab("Weighting", null, pContainerWeighting, null);
			pContainerWeighting.setLayout(null);
			
			JButton btnApplyAutoWeighting = new JButton("Apply Selected Weighting Schemes");
			btnApplyAutoWeighting.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.getTrainingData().reduceWeightofOldData(5);
				}
			});
			btnApplyAutoWeighting.setBounds(6, 276, 200, 29);
			pContainerWeighting.add(btnApplyAutoWeighting);
			
			JCheckBox chckbxEnableAutoWeighting = new JCheckBox("Enable Auto weighting");
			chckbxEnableAutoWeighting.setEnabled(false);
			chckbxEnableAutoWeighting.setBounds(6, 142, 168, 23);
			pContainerWeighting.add(chckbxEnableAutoWeighting);
			
			JCheckBox chckbxReduceWeightOf = new JCheckBox("Reduce all weights by a constant percentage");
			chckbxReduceWeightOf.setSelected(true);
			chckbxReduceWeightOf.setBounds(6, 193, 288, 23);
			pContainerWeighting.add(chckbxReduceWeightOf);
			
			JLabel lblAutoWeightingSchemes = new JLabel("Auto Weighting Schemes");
			lblAutoWeightingSchemes.setBounds(6, 172, 168, 14);
			pContainerWeighting.add(lblAutoWeightingSchemes);
			
			JCheckBox chckbxModifyWeightsBased = new JCheckBox("Modify weights based on distance to hyperplane");
			chckbxModifyWeightsBased.setEnabled(false);
			chckbxModifyWeightsBased.setBounds(6, 220, 288, 23);
			pContainerWeighting.add(chckbxModifyWeightsBased);
			
			JCheckBox chckbxModifyBasedOn = new JCheckBox("Modify based on accuracy results");
			chckbxModifyBasedOn.setEnabled(false);
			chckbxModifyBasedOn.setBounds(6, 246, 191, 23);
			pContainerWeighting.add(chckbxModifyBasedOn);
			
			JButton btnResetWeights = new JButton("Reset Weights");
			btnResetWeights.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.getTrainingData().resetWeights();
				}
			});
			btnResetWeights.setBounds(6, 11, 113, 23);
			pContainerWeighting.add(btnResetWeights);
			
			JComboBox<String> cmbWeightingClass = new JComboBox<String>();
			cmbWeightingClass.setModel(new DefaultComboBoxModel<String>(new String[] {"Positive", "Negative", "Both"}));
			cmbWeightingClass.setBounds(230, 168, 68, 20);
			pContainerWeighting.add(cmbWeightingClass);
			
			JLabel lblApplyWeightsTo = new JLabel("Apply weights to: ");
			lblApplyWeightsTo.setBounds(210, 146, 88, 14);
			pContainerWeighting.add(lblApplyWeightsTo);
			
			JLabel lblExperimentialFutureWork = new JLabel("<html>Experimential: For future work:\r\n<br /> Automatic weighting schemes such as, adjusting the weight of data points as they are being added to the dataset. This is to reduce weight of old data, which might be useful for timeseries data");
			lblExperimentialFutureWork.setForeground(Color.RED);
			lblExperimentialFutureWork.setBounds(10, 45, 226, 90);
			pContainerWeighting.add(lblExperimentialFutureWork);
			
			Panel pStatus = new Panel();
			pStatus.setBounds(10, 469, 817, 20);
			frame.getContentPane().add(pStatus);
			pStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			
			lblStatus = new JLabel("");
			lblStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblStatus.setForeground(Color.RED);
			pStatus.add(lblStatus);
			
			JMenuBar menuBar = new JMenuBar();
			frame.setJMenuBar(menuBar);
			
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			
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
			
			JRadioButtonMenuItem rdbtnmntmWeights = new JRadioButtonMenuItem("Weights");
			rdbtnmntmWeights.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayWeights(!chartPanel.isDisplayWeights());
		
				}
			});
			rdbtnmntmWeights.setSelected(true);
			mnDisplayLabels.add(rdbtnmntmWeights);
			
			JMenu mnDataPoints = new JMenu("Data Points");
			mnView.add(mnDataPoints);
			
			JCheckBoxMenuItem chckbxmntmDisplayDataPositiveClass = new JCheckBoxMenuItem("Positive Class");
			chckbxmntmDisplayDataPositiveClass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayPositiveClass(!chartPanel.isDisplayPositiveClass());
				}
			});
			chckbxmntmDisplayDataPositiveClass.setSelected(true);
			mnDataPoints.add(chckbxmntmDisplayDataPositiveClass);
			
			JCheckBoxMenuItem chckbxmntmDisplayDataNegativeClass = new JCheckBoxMenuItem("Negative Class");
			chckbxmntmDisplayDataNegativeClass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayNegativeClass(!chartPanel.isDisplayNegativeClass());
				}
			});
			chckbxmntmDisplayDataNegativeClass.setSelected(true);
			mnDataPoints.add(chckbxmntmDisplayDataNegativeClass);
			
			JMenu mnWeightedRch = new JMenu("Weighted RCH");
			mnView.add(mnWeightedRch);
			
			JCheckBoxMenuItem mntmWrchpositive = new JCheckBoxMenuItem("Positive Class");
			mntmWrchpositive.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayPositiveWRCH(!chartPanel.isDisplayPositiveWRCH());
				}
			});
			mntmWrchpositive.setSelected(true);
			mnWeightedRch.add(mntmWrchpositive);
			
			JCheckBoxMenuItem mntmWrchnegative = new JCheckBoxMenuItem("Negative Class");
			mntmWrchnegative.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayNegativeWRCH(!chartPanel.isDisplayNegativeWRCH());
				}
			});
			mntmWrchnegative.setSelected(true);
			mnWeightedRch.add(mntmWrchnegative);
			
			JMenu mnSupportVectors = new JMenu("Support Vectors");
			mnView.add(mnSupportVectors);
			
			JCheckBoxMenuItem mntmDisplaySVPC = new JCheckBoxMenuItem("Positive Class");
			mnSupportVectors.add(mntmDisplaySVPC);
			
			JCheckBoxMenuItem mntmDisplaySVNC = new JCheckBoxMenuItem("Negative Class");
			mnSupportVectors.add(mntmDisplaySVNC);
			
			JCheckBoxMenuItem chckbxmntmCentroids = new JCheckBoxMenuItem("Centroids");
			chckbxmntmCentroids.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayCentroids(!chartPanel.isDisplayCentroids());
				}
			});
			chckbxmntmCentroids.setSelected(true);
			mnView.add(chckbxmntmCentroids);
			
			JCheckBoxMenuItem mntmHyperplane = new JCheckBoxMenuItem("Hyperplane");
			mntmHyperplane.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayGeometricBoundary(!chartPanel.isDisplayGeometricBoundary());
				}
			});
			mntmHyperplane.setSelected(true);
			mnView.add(mntmHyperplane);
			
			JCheckBoxMenuItem mntmMargins = new JCheckBoxMenuItem("Margins");
			mntmMargins.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					chartPanel.setDisplayMargins(!chartPanel.isDisplayMargins());
				}
			});
			mntmMargins.setSelected(true);
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
			
			JMenuItem mntmTPoints = new JMenuItem("T points");
			mntmTPoints.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("T points");
				}
			});
			mnData.add(mntmTPoints);
			
			JMenuItem mntmNewMenuItem = new JMenuItem("Triangle 2");
			mntmNewMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					model.loadPredefinedDataset("Triangle 2");
				}
			});
			mnData.add(mntmNewMenuItem);
			
			JMenu mnHelp = new JMenu("Help");
			menuBar.add(mnHelp);
			
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
	private WSVMPanel createChartPanel(){

		chart = ChartFactory.createScatterPlot
		("Weighted Support Vector Machine", "X", "Y", model.getTrainingData());

        return new WSVMPanel(chart, model);
	}
	
	private void changeChartData(){
		int xDim = 0;
		int yDim = 0; 
		if (cmbXDimensionName != null){
			xDim = cmbXDimensionName.getSelectedIndex();
		}
		if (cmbYDimensionName != null){
			yDim = cmbYDimensionName.getSelectedIndex();
		}
		model.getTrainingData().setXDimension(xDim);
		model.getTrainingData().setYDimension(yDim);
		
		//model.getTrainingData().
		//use model
		//chart.getXYPlot().setDataset(model.getTrainingData().getChartData(xDim, yDim));
	}

	private void solveSVM(double mu1, double mu2){
		model.setMu(mu1, mu2);
		model.solveSVM();
		chartPanel.updateWSVMSolutions();
		
		if (!model.isSVMSolved()){
			setStatus("WSVM solution not found, change mu values until WRCHs do not overlap");
		}else{
			setStatus("");
		}
		
		perfMatrix.setValue(0, 0, model.numTruePositives);
		perfMatrix.setValue(0, 1, model.numFalseNegatives);
		perfMatrix.setValue(1, 0, model.numFalsePositives);
		perfMatrix.setValue(1, 1, model.numTrueNegatives);
		
		
	}
	
	private void findWRCH(double mu1, double mu2){
		try {
			model.setMu(mu1, mu2);
			model.solveWRCH(0);
			model.solveWRCH(1);
			chartPanel.updateWRCHSolutions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showScatterPlotMatrix(){

		frameSPLOM = new WSVMSPLOM(model, chart.getXYPlot().getRenderer());
		
		frameSPLOM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameSPLOM.setPreferredSize(new Dimension(600,400));
		frameSPLOM.pack();
		frameSPLOM.setVisible(true);
	}

	private JSlider setupSlider(JSlider slider, boolean useMuScale, int maxValue){
	    try {
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
		} catch (Exception e) {

		}
		
		
		return slider;
	}
	

	public String format(double d){
		return String.format("%.4f", d);
	}


	public void setStatus(String status){
		lblStatus.setText(status);
	}
	
	public String getStatus(){
		return lblStatus.getText();
	}


	@Override
	public void update() {
		try {
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
				
				WSVMDataItem newPoint = new WSVMDataItem(model.getTrainingData().getDimensions());
				newPoint.setWeight(Double.parseDouble(textField_NewWeight.getText()));
				
				if (cmbNewClass.getSelectedIndex() == 0){ //asumption 0 is positive class
					chartPanel.addPoint(model.getTrainingData().getPositiveSeriesID(), newPoint);
				}else if (cmbNewClass.getSelectedIndex() == 1){
					chartPanel.addPoint(model.getTrainingData().getNegativeSeriesID(), newPoint);
				}
			}else if (rdbtnWeightingTool.isSelected()){
				double newWeight = Double.parseDouble(textField_NewWeight.getText());
				
				chartPanel.changeSelectionWeight(newWeight);
				//TODO future update view-solution
			}
			

			// if model updated //TODO future improve design
			
//			if (model.isSolutionChanged()){
//				chartPanel.updateWRCHSolutions();
//				chartPanel.updateWSVMSolutions();
//			}
			
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame,
				    "Invalid range of input values",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	@Override
	public void datasetChanged(DatasetChangeEvent event) {
		Dataset data = event.getDataset();
		
		if (data !=null && data instanceof WSVMDataSet){
			WSVMDataSet svmDataset = (WSVMDataSet) data;
			if (svmDataset == model.getTrainingData()){
				
				if (frameSPLOM != null){
					frameSPLOM.updateSPLOM();
				}
				
				System.out.println("training data changed");
				if (chckbxAutoUpdate.isSelected()){
					findWRCH(model.getMu1(), model.getMu2());
				}
				if (chckbxAutoUpdateWsvm.isSelected()){
					solveSVM(model.getMu1(), model.getMu2());
				}
				
				setupSlider(slider_class1, !chckbxUsemuScale.isSelected(), 
						model.getTrainingData().getSeries(model.getTrainingData().getPositiveSeriesID()).getItemCount());
				
				setupSlider(slider_class2, !chckbxUsemuScale_1.isSelected(), 
						model.getTrainingData().getSeries(model.getTrainingData().getNegativeSeriesID()).getItemCount());
				
			}
		}
	}
}
