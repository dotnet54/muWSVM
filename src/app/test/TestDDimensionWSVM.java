package app.test;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.gui.SVMPanel;
import app.model.WSKSolver;
import app.model.data.SVMDataItem;

public class TestDDimensionWSVM extends JFrame{
	
	public static void main(String[] args){
		try {
			TestDDimensionWSVM app = new TestDDimensionWSVM();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public TestDDimensionWSVM() {
		try {
			DData datasource = new DData(2);
			//create two right angle triangles
//			datasource.add(new DVector(1, 1, 1, -1));
//			datasource.add(new DVector(3, 1, 1, -1));
//			datasource.add(new DVector(1, 3, 1, -1));
//			
//			datasource.add(new DVector(5, 1, 1, +1));
//			datasource.add(new DVector(4, 3, 1, +1));
//			datasource.add(new DVector(7, 1, 1, +1));
			
			datasource.generateRandomData(20);
			
//			datasource.add(new DVector(1, 4, 1, -1));
//			datasource.add(new DVector(2, 5, 1, -1));
//			datasource.add(new DVector(7, 4, 1, +1));
//			datasource.add(new DVector(6, 3, 1, +1));
			
			XYSeriesCollection dataset =  datasource.getChartData();
			dataset.addSeries(new XYSeries("Anno"));
			dataset.getSeries(2).add(0, 0);
			
			JFreeChart chart = ChartFactory.createScatterPlot
			("Multidimensional WSVM", "X", "Y", dataset);
			ChartPanel panel = new ChartPanel(chart);
			
			WSKSolver solver = new WSKSolver();
			solver.wsk(datasource, 1, 1);
			
			SVMDataItem w = solver.getW();
			double b = solver.getB();
			SVMDataItem p =  solver.getNearestPositivePoint();
			SVMDataItem n = solver.getNearestNegativePoint();			
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
			
			SVMDataItem neww = w.clone();
			SVMDataItem h = w.clone();

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
			
			dataset.getSeries(2).add(offset, offset);
			
			chart.getXYPlot().clearAnnotations();
			chart.getXYPlot().addAnnotation(hyperplane);
			//chart.getXYPlot().addAnnotation(nearest);
			chart.getXYPlot().addAnnotation(posMargin);
			chart.getXYPlot().addAnnotation(negMargin);
			
			panel.setMouseWheelEnabled(true);
			panel.setMouseZoomable(true);
			chart.getXYPlot().getDomainAxis().setRange(-2, 10);
			chart.getXYPlot().getRangeAxis().setRange(-2, 10);
			
			getContentPane().add(panel);
			setPreferredSize(new Dimension(600, 600));
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			pack();
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
