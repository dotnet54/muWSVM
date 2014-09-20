package old;

import java.awt.EventQueue;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class InputTest {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InputTest window = new InputTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private class IntegerInputVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            String text = tf.getText();
            try{
            	Integer.parseInt(text);
            }catch (NumberFormatException nf) {
            	JOptionPane.showMessageDialog(null, "sdf", 
            			"InfoBox: " , JOptionPane.INFORMATION_MESSAGE);
            	return false;
            }
			return true;
		}
		
//		@Override
//		public boolean shouldYieldFocus(JComponent input){
//			return true;
//			
//		}
    }

	/**
	 * Create the application.
	 */
	public InputTest() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(21, 46, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setInputVerifier(new IntegerInputVerifier());
		
		textField_1 = new JTextField();
		textField_1.setBounds(21, 80, 86, 20);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(21, 134, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		chckbxNewCheckBox.setBounds(10, 164, 97, 23);
		frame.getContentPane().add(chckbxNewCheckBox);
		
		JSlider slider = new JSlider();
		slider.setBounds(10, 11, 200, 23);
		frame.getContentPane().add(slider);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(184, 72, 172, 115);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("New check box");
		panel.add(chckbxNewCheckBox_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("New toggle button");
		panel_1.add(tglbtnNewToggleButton);
	}
}
