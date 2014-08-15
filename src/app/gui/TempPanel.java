package app.gui;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public class TempPanel extends ChartPanel implements ChartMouseListener,MouseWheelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFreeChart chart = null;
	
	public TempPanel(JFreeChart chart) {
		super(chart);
		this.chart = chart;
		this.addChartMouseListener(this);
		this.addMouseWheelListener(this);
		setMouseWheelEnabled(true);
//		
//		this.addChartMouseListener(new ChartMouseListener() {
//		    @Override
//		    public void chartMouseClicked(ChartMouseEvent event) {
//		        System.out.print("mouse clicked");
//		    }
//
//		    @Override
//		    public void chartMouseMoved(ChartMouseEvent event) {
//		        Plot p = event.getChart().getPlot();
//
//		        //process the xyplot
//		        if (p instanceof XYPlot) {
//		            if (event.getEntity() instanceof XYItemEntity) {
//		                XYItemEntity e = (XYItemEntity) (event.getEntity());
//		                XYPlot plot = event.getChart().getXYPlot();
//		                XYDataset dataset = e.getDataset();
//
//		                //find which data set matches the one provided by the event
//		                for (int i = 0; i < plot.getDatasetCount(); i++) {
//		                    XYDataset test = plot.getDataset(i);
//		                    if (test == dataset) {
//		                        XYItemRenderer r = plot.getRenderer(i);
//
//		                        //set the selection
//		                        if (r instanceof XYSelectionRenderer) {
//		                            XYSelectionRenderer sel = (XYSelectionRenderer) r;
//		                            if (sel.isSelectionActive()) {
//		                                sel.setSelectedSeries(-1);
//		                                sel.setSelectedItem(-1);
//		                            } else {
//		                                sel.setSelectedSeries(e.getSeriesIndex());
//		                                sel.setSelectedItem(e.getItem());
//		                            }
//		                        }
//		                    }
//		                }
//		                System.out.println("Series index: " + e.getSeriesIndex() + ", item index " + e.getItem());
//		            }
//
//		        }
//		    }
//		});
		
		
		
	}

	
	@Override
	public void paintComponent(Graphics g){
		//paintComponent(g);
		super.paintComponent(g);
		
		XYPlot plot = chart.getXYPlot();
		XYDataset dt =  plot.getDataset();
		
		int x1 = (int) dt.getXValue(0, 0);
		int y1 = (int) dt.getXValue(0, 0);
		int x2 = (int) dt.getXValue(0, 1);
		int y2 = (int) dt.getXValue(0, 1);
		
		Point2D p = translateScreenToJava2D(new Point(x1,y1));
		
		
		g.drawLine(0, 0,(int) p.getX(),(int) p.getY());
		
		

	}
	
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) return;
       // if (e.getWheelRotation()< 0) increaseZoom(getFocusedChart(), true);
       // else                          decreaseZoom(getFocusedChart(), true);
    }
    
    public synchronized void increaseZoom(JComponent chart, boolean saveAction){
        ChartPanel ch = (ChartPanel)chart;
        zoomChartAxis(ch, true);
    }  
    
    public synchronized void decreaseZoom(JComponent chart, boolean saveAction){
        ChartPanel ch = (ChartPanel)chart;
        zoomChartAxis(ch, false);
    }  
    
    private void zoomChartAxis(ChartPanel chartP, boolean increase){              
        int width = chartP.getMaximumDrawWidth() - chartP.getMinimumDrawWidth();
        int height = chartP.getMaximumDrawHeight() - chartP.getMinimumDrawWidth();        
        if(increase){
           chartP.zoomInBoth(width/2, height/2);
        }else{
           chartP.zoomOutBoth(width/2, height/2);
        }

    }
	
    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
    Point2D p = translateScreenToJava2D(event.getTrigger().getPoint());
    Rectangle2D plotArea = getScreenDataArea();
    XYPlot plot = (XYPlot) chart.getPlot(); // your plot
    double chartX = plot.getDomainAxis().java2DToValue(p.getX(), plotArea, plot.getDomainAxisEdge());
    double chartY = plot.getRangeAxis().java2DToValue(p.getY(), plotArea, plot.getRangeAxisEdge());
    
    System.out.println(chartX);
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.getScreenDataArea();
        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, 
                RectangleEdge.BOTTOM);
        // make the crosshairs disappear if the mouse is out of range
        if (!xAxis.getRange().contains(x)) { 
            x = Double.NaN;                  
        }
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        //xCrosshair.setValue(x);
        //yCrosshair.setValue(y);
    }  
}
