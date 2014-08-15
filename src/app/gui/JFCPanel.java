package app.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class JFCPanel extends ChartPanel implements ChartMouseListener{
//TODO BUG point is not added to click location but upper corner of popup menu
		JPopupMenu popup = new JPopupMenu();
		JMenuItem it = new JMenuItem("Add Point");
		JMenuItem itr = new JMenuItem("Remove Point");
		JMenuItem itw = new JMenuItem("Change Weight");
	
	public JFCPanel(JFreeChart chart) {
		super(chart);
		setMouseWheelEnabled(true);
		addChartMouseListener(this);
		popup.add(it);
		popup.add(itr);
		popup.add(itw);
		setPopupMenu(popup);
		
		it.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//e.
				 
				addPoint(clx, cly);
			}
		});
		
		itr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				// TODO Auto-generated method stub
	            XYItemEntity e = (XYItemEntity) selection;
	            XYDataset d = e.getDataset(); //TODO check selection for null
	            int s = e.getSeriesIndex();
	            int i = e.getItem();
	            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
	            
	            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
	            XYSeries ss =  dd.getSeries(s);
	            
	            System.out.println("rem:" + ss.getItems().get(i));
	            ss.remove(i);
			}
		});
		
		itw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
	            XYItemEntity e = (XYItemEntity) selection;
	            XYDataset d = e.getDataset(); //TODO check selection for null
	            int s = e.getSeriesIndex();
	            int i = e.getItem();
	            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
	            
	            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
	            XYSeries ss =  dd.getSeries(0);
	            
	            System.out.println("rem:" + ss.getItems().get(i));
	            double weight = 1;
				String input = JOptionPane.showInputDialog(this  ,weight);
				weight = Double.parseDouble(input); // try catch TODO parse error handle
			}
		});
	}
	double clx =0;
	double cly=0;
	XYItemEntity selection = null;
	
	
	@Override
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);
		 System.out.println("rel:[" + event.getX() + "," + event.getY() + "]");
//		 Point2D p = this.translateScreenToJava2D(event.getTrigger().getPoint());
			Rectangle2D plotArea = this.getScreenDataArea();
			XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
			double chartX = plot.getDomainAxis().java2DToValue(event.getX(), plotArea, plot.getDomainAxisEdge());
			double chartY = plot.getRangeAxis().java2DToValue(event.getY(), plotArea, plot.getRangeAxisEdge());
			
	        ChartEntity entity = null;
	        List list = null;
	        if (getChartRenderingInfo() != null) {
	            EntityCollection entities = getChartRenderingInfo().getEntityCollection();
	            if (entities != null) {
	                entity = entities.getEntity(event.getX(), event.getY());
	                //@shifaz 11/8/2014
	                StandardEntityCollection sec = (StandardEntityCollection)
	                getChartRenderingInfo().getEntityCollection();
	                list = sec.getEntities(event.getX(), event.getY());
	            }
	        }
			ChartMouseEvent chartEvent = new ChartMouseEvent(getChart(), event,
	        		list,event.getX(),event.getY());
			report(chartEvent);
			 System.out.println("rel:[" + chartX + "," + chartX + "]");
			 //addPoint((double)chartX, (double)chartY);
			  clx = chartX;
			  cly = chartY;
			  
			  
			  
			  
			  
			  
			  
			  selection = getSelection(chartEvent,chartX,chartY);
//			  System.out.println("releasedb on:" + selection.toString());
			  if (selection == null){
				  popup.show(this, event.getX(), event.getY());
			  }else{
		            XYItemEntity e = selection;
		            XYDataset d = e.getDataset();
		            int s = e.getSeriesIndex();
		            int i = e.getItem();
		            System.out.println("Selection:" + i +" : "+ d.getX(s, i) + ", Y:" + d.getY(s, i));
		            //d.g
		            
		            popup.show(this, event.getX(), event.getY());
			  }
			  
			  
	}
	
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
		// TODO Auto-generated method stub
		Point2D p = this.translateScreenToJava2D(event.getTrigger().getPoint());
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		double chartX = plot.getDomainAxis().java2DToValue(p.getX(), plotArea, plot.getDomainAxisEdge());
		double chartY = plot.getRangeAxis().java2DToValue(p.getY(), plotArea, plot.getRangeAxisEdge());
		report(event);
		 System.out.println("Pos:[" + chartX + "," + chartX + "]");
		 //addPoint((double)chartX, (double)chartY);
		  clx = chartX;
		  cly = chartY;
		  
		  
//		  selection = getSelection(event,chartX,chartY);
//		  if (selection == null){
//			  popup.show(this, event.getX(), event.getY());
//		  }else{
//	            XYItemEntity e = selection;
//	            XYDataset d = e.getDataset();
//	            int s = e.getSeriesIndex();
//	            int i = e.getItem();
//	            System.out.println("Selection:" + i +" : "+ d.getX(s, i) + ", Y:" + d.getY(s, i));
//	            //d.g
//	            
//	            popup.show(this, event.getX(), event.getY());
//		  }
		 
		 
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent event) {
		// TODO Auto-generated method stub
	List l = event.getEntities();
		// System.out.println("#:" + l.size());
		report(event);
	}
	
	
	private XYItemEntity getSelection(ChartMouseEvent event, double x, double y){
		ChartEntity ce = event.getEntity();
		List list = event.getEntities();
		
		
		if (list == null){
			selection = null;
		}
		selection = null;
		for (int j = 0; j < list.size(); j++){
			ce = (ChartEntity) list.get(j);
			if (ce instanceof XYItemEntity) {
	            XYItemEntity e = (XYItemEntity) ce;
	            Insets insets = getInsets();
	            x = (int) ((event.getX() - insets.left) / this.getScaleX());
	            y = (int) ((event.getY() - insets.top) / this.getScaleY());
	            
	            if (e.getArea().contains(x, y)){
	           	    XYDataset d = e.getDataset();
		            int s = e.getSeriesIndex();
		            int i = e.getItem();
		            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
		            
		            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
		            XYSeries ss =  dd.getSeries(0);
		            
		            System.out.println("SS:" + ss.getItems().get(i));
		            //ss.remove(i);
		            selection = e; 	
		            break;
	            }
			}
		}
        
		return selection;
		
	}
	
	
	private void addPoint(double x, double y){
		
		XYSeriesCollection d = (XYSeriesCollection) getChart().getXYPlot().getDataset();
		XYSeries s =  d.getSeries(0);
		
		XYDataItem i = new XYDataItem(x, y);
		s.add(i);
	}
	
    private void report(ChartMouseEvent cme) {
        ChartEntity ce = cme.getEntity();
        
        if (ce instanceof XYItemEntity) {
            XYItemEntity e = (XYItemEntity) ce;
            XYDataset d = e.getDataset();
            int s = e.getSeriesIndex();
            int i = e.getItem();
            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
        }
    }

}
