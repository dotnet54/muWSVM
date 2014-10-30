package app.test;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class PerformanceMatrix extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1987160885537524491L;
	private JLabel lbl00;
	private JLabel lbl01;
	private JLabel lbl10;
	private JLabel lbl11;

	/**
	 * Create the panel.
	 */
	public PerformanceMatrix() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 97, 83, 69, 0};
		gridBagLayout.rowHeights = new int[] {0, 35, 30, 36, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTotal = new JLabel("Total");
		GridBagConstraints gbc_lblTotal = new GridBagConstraints();
		gbc_lblTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotal.gridx = 0;
		gbc_lblTotal.gridy = 0;
		add(lblTotal, gbc_lblTotal);
		
		JLabel lblPredictedClass = new JLabel("Predicted Class");
		GridBagConstraints gbc_lblPredictedClass = new GridBagConstraints();
		gbc_lblPredictedClass.insets = new Insets(0, 0, 5, 5);
		gbc_lblPredictedClass.gridx = 2;
		gbc_lblPredictedClass.gridy = 0;
		add(lblPredictedClass, gbc_lblPredictedClass);
		
		JLabel lblNewLabel_2 = new JLabel("Positive");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 2;
		gbc_lblNewLabel_2.gridy = 1;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		JLabel lblNewLabel = new JLabel("Negative");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblActualClass = new JLabel("Actual Class");
		GridBagConstraints gbc_lblActualClass = new GridBagConstraints();
		gbc_lblActualClass.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualClass.gridx = 0;
		gbc_lblActualClass.gridy = 2;
		add(lblActualClass, gbc_lblActualClass);
		
		JLabel lblNewLabel_1 = new JLabel("Positive");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 2;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		lbl00 = new JLabel("0");
		GridBagConstraints gbc_lbl00 = new GridBagConstraints();
		gbc_lbl00.insets = new Insets(0, 0, 5, 5);
		gbc_lbl00.gridx = 2;
		gbc_lbl00.gridy = 2;
		add(lbl00, gbc_lbl00);
		
		lbl01 = new JLabel("0");
		GridBagConstraints gbc_lbl01 = new GridBagConstraints();
		gbc_lbl01.insets = new Insets(0, 0, 5, 0);
		gbc_lbl01.gridx = 3;
		gbc_lbl01.gridy = 2;
		add(lbl01, gbc_lbl01);
		
		JLabel lblNewLabel_3 = new JLabel("Negative");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 3;
		add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		lbl10 = new JLabel("0");
		GridBagConstraints gbc_lbl10 = new GridBagConstraints();
		gbc_lbl10.insets = new Insets(0, 0, 5, 5);
		gbc_lbl10.gridx = 2;
		gbc_lbl10.gridy = 3;
		add(lbl10, gbc_lbl10);
		
		lbl11 = new JLabel("0");
		GridBagConstraints gbc_lbl11 = new GridBagConstraints();
		gbc_lbl11.insets = new Insets(0, 0, 5, 0);
		gbc_lbl11.gridx = 3;
		gbc_lbl11.gridy = 3;
		add(lbl11, gbc_lbl11);
		
		JLabel lblSensitivity = new JLabel("Sensitivity");
		GridBagConstraints gbc_lblSensitivity = new GridBagConstraints();
		gbc_lblSensitivity.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensitivity.gridx = 0;
		gbc_lblSensitivity.gridy = 5;
		add(lblSensitivity, gbc_lblSensitivity);
		
		JLabel lblSpecificity = new JLabel("Specificity");
		GridBagConstraints gbc_lblSpecificity = new GridBagConstraints();
		gbc_lblSpecificity.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpecificity.gridx = 0;
		gbc_lblSpecificity.gridy = 6;
		add(lblSpecificity, gbc_lblSpecificity);
		
		JLabel lblRecall = new JLabel("Accuracy");
		GridBagConstraints gbc_lblRecall = new GridBagConstraints();
		gbc_lblRecall.insets = new Insets(0, 0, 5, 5);
		gbc_lblRecall.gridx = 0;
		gbc_lblRecall.gridy = 7;
		add(lblRecall, gbc_lblRecall);
		
		JLabel lblPrecision = new JLabel("Precision");
		GridBagConstraints gbc_lblPrecision = new GridBagConstraints();
		gbc_lblPrecision.insets = new Insets(0, 0, 0, 5);
		gbc_lblPrecision.gridx = 0;
		gbc_lblPrecision.gridy = 8;
		add(lblPrecision, gbc_lblPrecision);

	}
	
	public void setValue(int x, int y, int value){
		if ( x == 0 && y == 0){
			lbl00.setText(value + "");
		}else if ( x == 0 && y == 1){
			lbl01.setText(value + "");
		}else if ( x == 1 && y == 0){
			lbl10.setText(value + "");
		}else if ( x == 1 && y == 1){
			lbl11.setText(value + "");
		}
	}
}
