package app.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import app.model.data.DData;
import app.model.data.SVMModel;

import java.awt.GridLayout;
import java.awt.FlowLayout;

public class SVMSPLOM extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public SVMSPLOM(SVMModel model) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		int numDimensions = 2;
		
		DData datasource = new DData(numDimensions);	
		datasource.generateRandomData(numDimensions);
		
		int numCharts = numDimensions * numDimensions;
		JFreeChart charts[] = new JFreeChart[numCharts];
		ChartPanel panels[] = new ChartPanel[numCharts];	
		
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(numDimensions, numDimensions));
		
		int chartID = 0;
		for (int i = 0; i < numDimensions; i++){
			for (int j = 0; j < numDimensions; j++){

				chartID = i * numDimensions + j;
				charts[chartID] = ChartFactory.createScatterPlot
					("Chart " + chartID, "Dimension: " + i, "Dimension: " + j, datasource.getChartData(i, j));
				panels[chartID] = new ChartPanel(charts[chartID]);
				panel.add(panels[chartID]);
				
				panels[chartID].setMouseWheelEnabled(true);
				panels[chartID].setMouseZoomable(true);
				charts[chartID].getXYPlot().getDomainAxis().setRange(-2, 10);
				charts[chartID].getXYPlot().getRangeAxis().setRange(-2, 10);
				charts[chartID].removeLegend();
				charts[chartID].setTitle((String)null);
				
				Font font3 = new Font("Dialog", Font.BOLD, 12); 
				XYPlot plot = charts[chartID].getXYPlot();
				plot.getDomainAxis().setLabelFont(font3);
				plot.getRangeAxis().setLabelFont(font3);
			}
		}

		getContentPane().add(panel);
	}

}
