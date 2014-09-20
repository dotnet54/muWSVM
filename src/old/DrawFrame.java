package old;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;


import app.model.data.SVMModel;

public class DrawFrame extends JFrame{
	
	private DrawPanel panel;
	private SVMModel model;
	
	public DrawFrame(){
		
		model = new SVMModel();
		panel = new DrawPanel(model);
		
		getContentPane().add(panel);
		setPreferredSize(new Dimension(600,400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
	}
	
	
	
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DrawFrame window = new DrawFrame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
