package app.test;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.data.DData;
import app.model.data.DVector;
import app.model.data.Dwsk;

public class TestSPLOM extends JFrame{


		public static void main(String[] args){
			try {
				TestSPLOM app = new TestSPLOM();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		public TestSPLOM() {
			try {
				DData datasource = new DData(4);	
				datasource.generateRandomData(4);
				
				JPanel jpanel = new JPanel();
				jpanel.setLayout(new GridLayout(2, 2));
				
				
				JFreeChart chart = ChartFactory.createScatterPlot
				("SPLOM TEST 1", "0", "0", datasource.getChartData(0, 0));
				ChartPanel panel = new ChartPanel(chart);
				jpanel.add(panel);
				
				JFreeChart chart2 = ChartFactory.createScatterPlot
				("SPLOM TEST 2", "0", "1", datasource.getChartData(0, 1));
				ChartPanel panel2 = new ChartPanel(chart2);
				jpanel.add(panel2);
				
				JFreeChart chart3 = ChartFactory.createScatterPlot
				("SPLOM TEST 3", "1", "0", datasource.getChartData(1, 0));
				ChartPanel panel3 = new ChartPanel(chart3);
				jpanel.add(panel3);
				
				JFreeChart chart4 = ChartFactory.createScatterPlot
				("SPLOM TEST 4", "1", "1", datasource.getChartData(0, 1));
				ChartPanel panel4 = new ChartPanel(chart4);
				jpanel.add(panel4);
				
				Dwsk solver = new Dwsk();
				solver.wsk(datasource, 1, 1);
				
				DVector w = solver.getW();
				double b = solver.getB();
				DVector p =  solver.getNearestPositivePoint();
				DVector n = solver.getNearestNegativePoint();			
				double offset = b / w.getMagnitude();
			
				System.out.format("p:%s\nn:%s\n", 
						solver.getNearestPositivePoint(), 
						solver.getNearestNegativePoint());
				System.out.format("w:%s\nb:%.4f\noffset:%.4f", w, b, offset);

				double x1 = 0.0;
				double x2 = 10.0;
				double y1 = 0.0;
				double y2 = 10.0;			
				double scale = 20.0;
				
				DVector neww = w.clone();
				DVector h = w.clone();

				h.setXValue(-w.getYValue());
				h.setYValue(w.getXValue());
				
				
				x1 = h.getXValue() * scale + offset;
				y1 = h.getYValue() * scale + offset;
				x2 = h.getXValue() * -scale + offset;
				y2 = h.getYValue() * -scale + offset;
				
				y1 = -500;
				y2 = 500;
				x1 = (b - (w.getYValue() * y1))/ w.getXValue();
				x2 = (b - (w.getYValue() * y2))/ w.getXValue();
				
				XYLineAnnotation hyperplane = new XYLineAnnotation(x1, y1, x2, y2);
				b = p.getDotProduct(w);
				x1 = (b - (w.getYValue() * y1))/ w.getXValue();
				x2 = (b - (w.getYValue() * y2))/ w.getXValue();
				XYLineAnnotation posMargin = new XYLineAnnotation(x1, y1, x2, y2);
				b = n.getDotProduct(w);
				x1 = (b - (w.getYValue() * y1))/ w.getXValue();
				x2 = (b - (w.getYValue() * y2))/ w.getXValue();
				XYLineAnnotation negMargin = new XYLineAnnotation(x1, y1, x2, y2);
				
				XYLineAnnotation nearest = new XYLineAnnotation(
						solver.getNearestPositivePoint().getXValue(),
						solver.getNearestPositivePoint().getYValue(), 
						solver.getNearestNegativePoint().getXValue(),
						solver.getNearestNegativePoint().getYValue());
				
				chart.getXYPlot().clearAnnotations();
				chart.getXYPlot().addAnnotation(hyperplane);
				//chart.getXYPlot().addAnnotation(nearest);
				chart.getXYPlot().addAnnotation(posMargin);
				chart.getXYPlot().addAnnotation(negMargin);
				
				panel.setMouseWheelEnabled(true);
				panel.setMouseZoomable(true);
				chart.getXYPlot().getDomainAxis().setRange(-2, 10);
				chart.getXYPlot().getRangeAxis().setRange(-2, 10);
				
				getContentPane().add(jpanel);
				setPreferredSize(new Dimension(600, 600));
				setDefaultCloseOperation(EXIT_ON_CLOSE);
				pack();
				setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

}
