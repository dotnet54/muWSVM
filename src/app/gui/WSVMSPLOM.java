package app.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import app.model.WSVMModel;
import app.model.data.WSVMDataSet;

import java.awt.GridLayout;

public class WSVMSPLOM extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5085575827721920635L;

	private JPanel contentPane;

	WSVMDataSet datasource;
	int numDimensions;
	
	JFreeChart charts[];
	ChartPanel panels[];
	
	WSVMModel model;
	XYItemRenderer renderer;
	JPanel panel;
	
	/**
	 * generates a 2d scatter plot matrix
	 */
	public WSVMSPLOM(WSVMModel model, XYItemRenderer renderer) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.model = model;
		this.renderer = renderer;
		
		updateSPLOM();

		getContentPane().add(panel);
		
	}
	

	public void updateSPLOM(){
		getContentPane().removeAll();
		
		datasource = model.getTrainingData();
		numDimensions = datasource.getDimensions();
		
		int numCharts = numDimensions * numDimensions;
		charts = new JFreeChart[numCharts];
		panels = new ChartPanel[numCharts];	
		
		
		panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(numDimensions, numDimensions));
		
		int chartID = 0;
		for (int i = 0; i < numDimensions; i++){
			for (int j = 0; j < numDimensions; j++){

				chartID = i * numDimensions + j;
				charts[chartID] = ChartFactory.createScatterPlot
					("Chart " + chartID, "Dimension: " + i,
					"Dimension: " + j, datasource.getChartData(i, j));
				
				
				
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
			
				plot.setRenderer(renderer);
				plot.setDomainPannable(true);
				plot.setRangePannable(true);
				
				plot.getDomainAxis().setAutoRangeMinimumSize(1, true);
				plot.getRangeAxis().setAutoRangeMinimumSize(1, true);
				plot.getDomainAxis().setAutoRange(true);
				plot.getRangeAxis().setAutoRange(true);
			}
		}
		
		
		this.validate();
	}

}
