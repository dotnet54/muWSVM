package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYRangeInfo;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;

import app.model.algorithms.WSK;
import app.model.data.SVMDataItem;
import app.model.data.SVMDataSet;
import app.model.data.SVMModel;

public class SVMPanel extends ChartPanel implements ChartMouseListener{
//TODO BUG point is not added to click location but upper corner of popup menu
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1667095001940958696L;
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem itp = new JMenuItem("Add to Positive Class");
	private JMenuItem itn = new JMenuItem("Add to Negative Class");
	private JMenuItem itd = new JMenuItem("Duplicate Point");
	private JMenuItem itr = new JMenuItem("Remove Point");
	private JMenuItem itw = new JMenuItem("Change Weight");
		
	protected XYPlot thisPlot = null;
	protected JFreeChart thisChart = null;
	
	
	private SVMModel model = null;
	private SVMDataSet inputData = null;
	private SVMDataSet overlayData = null;
	
	private  XYShapeAnnotation anoHull1 = null;
	private  XYShapeAnnotation anoRHull1 = null;
	private  XYShapeAnnotation anoHull2 = null;
	private  XYShapeAnnotation anoRHull2 = null;
	
	private  XYLineAnnotation hyperPlane = null;
	private  XYLineAnnotation marginPos = null;
	private  XYLineAnnotation marginNeg = null;
	private  XYLineAnnotation nearestPointLine = null;
	
	private double xChart =0;
	private double yChart=0;
	private double xPanel =0;
	private double yPanel=0;
	
	private XYItemEntity selectedEntity = null;
	private XYSeries selectedSeries = null;
	private SVMDataItem selectedDataItem = null;
		
	
	
	
	
	//Rendering Options
	
	private boolean displayPositiveWeights = true;
	private boolean displayNegativeWeights = true;
	
	private boolean displayPositiveAlpha = false;
	private boolean displayNegativeAlpha = false;
	
	private boolean displayPositiveClass = true;
	private boolean displayNegativeClass = true;
	
	private boolean displayPositiveCH = false;
	private boolean displayNegativeCH = false;
	
	private boolean displayPositiveWRCH = true;
	private boolean displayNegativeWRCH = true;
	
	private boolean displayPositiveSupportVectors = false;
	private boolean displayNegativeSupportVectors = false;
	
	private boolean displayPositiveCentroid = true;
	private boolean displayNegativeCentroid = true;
	
	private boolean displayHyperplane = true;
	private boolean displayNearestPointLine = true;
	private boolean displayCentroidConnectingLine = true;
	private boolean displayMargins = true;
	
	
	private boolean enableZoom = true;
	private int pointSize = 2;
	
	
	/**
	 * Label generator for point labels of JFreeChart
	 * @author shifaz
	 *
	 */
    private class LabelGenerator implements XYItemLabelGenerator {
        @Override
        public String generateLabel(XYDataset dataset, int series, int item) {
        	XYSeriesCollection dd = (XYSeriesCollection) dataset;
            XYSeries ss =  dd.getSeries(series);
	    	XYDataItem ditem =  (XYDataItem) ss.getItems().get(item);
	    	if (ditem instanceof SVMDataItem){
	    		SVMDataItem svmItem = (SVMDataItem) ditem;
	    		//return svmItem.toFormatedString(2);
	    		return svmItem.getLabel();
	    	}
            return "";
        }
    }
	
	public SVMPanel(JFreeChart chart,SVMModel model ) {
		super(chart);
		
		
		this.model = model;
		thisChart = chart;
		thisPlot = chart.getXYPlot();
		
		
		//Panel settings
		setMouseWheelEnabled(true);
		setZoomTriggerDistance(20);
		addChartMouseListener(this);
		
//		popup.add(it);
//		popup.add(itr);
//		popup.add(itw);		
		setPopupMenu(popup);
		initPopUpMenu();
		
		
		
		//Plot settings
		thisPlot.setDomainPannable(true);
		thisPlot.setRangePannable(true);
		thisPlot.getRangeAxis().setAutoRange(false);
		thisPlot.getDomainAxis().setAutoRange(false);
		thisPlot.getRangeAxis().setRange(0, 10);
		thisPlot.getDomainAxis().setRange(0, 10);
        

		
		

		//Dataset settings
		inputData = model.getRawDataSet();
		thisPlot.setDataset(1, model.getSolutionDataSet());
		thisPlot.setRenderer(1, new XYLineAndShapeRenderer(false, true));
		
        //Renderer settings
		XYDotRenderer rawDataRenderer = new XYDotRenderer();
		rawDataRenderer.setDotWidth(4);
		rawDataRenderer.setDotHeight(4);
		thisPlot.setRenderer(rawDataRenderer);
		
        //XYLineAndShapeRenderer rawDataRenderer = (XYLineAndShapeRenderer) thisPlot.getRenderer();
        XYLineAndShapeRenderer solutionRenderer = (XYLineAndShapeRenderer) thisPlot.getRenderer(1);

        rawDataRenderer.setBaseItemLabelGenerator(new LabelGenerator());
        rawDataRenderer.setBaseItemLabelPaint(Color.black);
        rawDataRenderer.setBaseItemLabelsVisible(true);
        rawDataRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());        
        rawDataRenderer.setBasePositiveItemLabelPosition(
            new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_LEFT));
        rawDataRenderer.setBaseItemLabelFont(
        		rawDataRenderer.getBaseItemLabelFont().deriveFont(8f));
        
        solutionRenderer.setSeriesShape(0, ShapeUtilities.createDiagonalCross(1, 1));
        solutionRenderer.setSeriesPaint(0, Color.RED.brighter());
        
        solutionRenderer.setSeriesShape(1, ShapeUtilities.createDiagonalCross(1, 1));
        solutionRenderer.setSeriesPaint(1, Color.BLUE.brighter());
        
        solutionRenderer.setSeriesShape(2, ShapeUtilities.createDiagonalCross(2, 2));
        solutionRenderer.setSeriesPaint(2, Color.GREEN.brighter());
	}

	
	
	@Override
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);

		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		double chartX = plot.getDomainAxis().java2DToValue(event.getX(),
				plotArea, plot.getDomainAxisEdge());
		double chartY = plot.getRangeAxis().java2DToValue(event.getY(),
				plotArea, plot.getRangeAxisEdge());

		ChartEntity entity = null;
		List list = null;
		if (getChartRenderingInfo() != null) {
			EntityCollection entities = getChartRenderingInfo()
					.getEntityCollection();
			if (entities != null) {
				entity = entities.getEntity(event.getX(), event.getY());
				// @shifaz 11/8/2014

				StandardEntityCollection sec = (StandardEntityCollection) getChartRenderingInfo()
						.getEntityCollection();
				list = sec.getEntities(event.getX(), event.getY());
			}
		}
		ChartMouseEvent chartEvent = new ChartMouseEvent(getChart(), event,
				list, event.getX(), event.getY());
		report(chartEvent);
		// addPoint((double)chartX, (double)chartY);
		xChart = chartX;
		yChart = chartY;

		// System.out.println("Mouse Released [Panel]: "
		// + event.getX() + "," + event.getY());
		// System.out.println("Mouse Released [Chart]: "
		// + xChart + "," + yChart);
		Point2D p = chartToPanel(new Point2D.Double(chartX, chartY));
		//System.out.println("Point: " + p);

		Point2D p2 = panelToChart(new Point(event.getX(), event.getY()));
		//System.out.println("Point2: " + p2);

		selectedEntity = getSelectedEntity(chartEvent, chartX, chartY);
		// System.out.println("releasedb on:" + selection.toString());
		if (selectedEntity == null) {

			if (event.isPopupTrigger()) {
				popup.removeAll();
				popup.add(itp);
				popup.add(itn);
				popup.pack();
				popup.show(this, event.getX(), event.getY());
			}

		} else {
			popup.removeAll();
			popup.add(itd);// TODO duplicate point
			popup.add(itr);
			popup.add(itw);
			popup.pack();
			XYItemEntity e = selectedEntity;
			XYDataset d = e.getDataset();
			int s = e.getSeriesIndex();
			int i = e.getItem();
			// System.out.println("Mouse Released : " + i +" : "+ d.getX(s, i) +
			// "," + d.getY(s, i));
			// d.g

			if (event.isPopupTrigger()) {
				popup.show(this, event.getX(), event.getY());
			}

		}
		//TODO 
        plot.getRangeAxis().setAutoRange(false);
        plot.getDomainAxis().setAutoRange(false);
       // plot.getRangeAxis().setRange(0, 10);
        //plot.getDomainAxis().setRange(0, 20);
	}
	
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
		//clicks

		 
		 
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent event) {
		// TODO Auto-generated method stub
		List l = event.getEntities();
		// System.out.println("#:" + l.size());
		report(event);
	}
	
	public void clearPlot(){
			XYPlot p = getChart().getXYPlot();
			p.clearAnnotations();
			//TODO
	//		series1.clear();
	//		series2.clear();
	//		series3.clear();
	//		series4.clear();
	//		series5.clear();
			
			model.clearDataSet(0);
	    }



	public void drawBisector(){
		
		
	}



	public void solveSVM(){
		System.out.println("solving SVM...");
 
		model.compute();
			    
	    if (hyperPlane != null){
	    	thisPlot.removeAnnotation(hyperPlane);
	    }
	    Line2D line = model.getLine(model.getW(), model.getB());
	   // Line2D line = SVMModel.getPerpendicularBisector();
	    hyperPlane = new XYLineAnnotation(line.getX1(), line.getY1(),
	    		line.getX2(), line.getY2());
	    thisPlot.addAnnotation(hyperPlane);
		
		
	    if (nearestPointLine != null){
	    	thisPlot.removeAnnotation(nearestPointLine);
	    }
	    nearestPointLine = new XYLineAnnotation(
	    		WSK.getNearestPositivePoint().getXValue(), 
	    		WSK.getNearestPositivePoint().getYValue(),
	    		WSK.getNearestNegativePoint().getXValue(),
	    		WSK.getNearestNegativePoint().getYValue());
	    thisPlot.addAnnotation(nearestPointLine);
		
		System.out.format("SVM found w:%s b:%s ", model.getW(), model.getB() );
	}



	public void findRCH(){
		System.out.println("finding RCH...");
	
		model.compute();
		
		
		final float dash1[] = { 2.0f, 2.0f };
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
	
		Path2D path;
		
	
		final Shape[] shapes = new Shape[3];
		thisPlot.clearAnnotations();
	
		ArrayList<SVMDataItem> rch1 = model.getRCH1();
	
		if (!rch1.isEmpty()) {
	
			double[] xPoints = new double[rch1.size()];
			double[] yPoints = new double[rch1.size()];
	
			for (int i = 0; i < rch1.size(); i++) {
				xPoints[i] = rch1.get(i).getXValue();
				yPoints[i] = rch1.get(i).getYValue();
			}
			path = new Path2D.Double();
	
			path.moveTo(xPoints[0], yPoints[0]);
			for (int i = 1; i < xPoints.length; ++i) {
				path.lineTo(xPoints[i], yPoints[i]);
			}
			path.closePath();
			shapes[0] = path;
	
			
			if (anoRHull1 != null) {
				thisPlot.removeAnnotation(anoRHull1);
			}
	
			anoRHull1 = new XYShapeAnnotation(shapes[0], dashed, Color.blue);
			thisPlot.addAnnotation(anoRHull1);
	
	
			
			
		}
	
		ArrayList<SVMDataItem> rch2 = model.getRCH2();
	
		if (!rch2.isEmpty()) {
			double[] xPoints3 = new double[rch2.size()];
			double[] yPoints3 = new double[rch2.size()];
	
			for (int i = 0; i < rch2.size(); i++) {
				xPoints3[i] = rch2.get(i).getXValue();
				yPoints3[i] = rch2.get(i).getYValue();
			}
			path = new Path2D.Double();
	
			path.moveTo(xPoints3[0], yPoints3[0]);
			for (int i = 1; i < xPoints3.length; ++i) {
				path.lineTo(xPoints3[i], yPoints3[i]);
			}
			path.closePath();
			shapes[1] = path;
	
			if (anoRHull2 != null) {
				thisPlot.removeAnnotation(anoRHull2);
			}
	
			anoRHull2 = new XYShapeAnnotation(shapes[1], dashed, Color.blue);
			thisPlot.addAnnotation(anoRHull2);
			
	
		}
	
	
		System.out.println("RCH found");
	}



	public void findCH(){
		System.out.println("finding CH...");
	
		Path2D path;
		model.compute();
	
		final Shape[] shapes = new Shape[4];
		
		thisPlot.clearAnnotations();
	
		ArrayList<SVMDataItem> ch1 = model.getCH1();
	
		if (!ch1.isEmpty()) {
			int[] xPoints = new int[ch1.size()];
			int[] yPoints = new int[ch1.size()];
	
			int[] xPoints2 = new int[ch1.size()];
			int[] yPoints2 = new int[ch1.size()];
	
			double[] xPoints3 = new double[ch1.size()];
			double[] yPoints3 = new double[ch1.size()];
	
			for (int i = 0; i < ch1.size(); i++) {
				xPoints[i] = (int) ch1.get(i).getXValue();
				yPoints[i] = (int) ch1.get(i).getYValue();
				xPoints2[i] = toPanelX(ch1.get(i).getXValue());
				yPoints2[i] = toPanelY(ch1.get(i).getYValue());
				xPoints3[i] = ch1.get(i).getXValue();
				yPoints3[i] = ch1.get(i).getYValue();
			}
			shapes[0] = new Polygon(xPoints, yPoints, ch1.size());
			shapes[1] = new Polygon(xPoints2, yPoints2, ch1.size());
	
			path = new Path2D.Double();
	
			path.moveTo(xPoints3[0], yPoints3[0]);
			for (int i = 1; i < xPoints3.length; ++i) {
				path.lineTo(xPoints3[i], yPoints3[i]);
			}
			path.closePath();
			shapes[2] = path;
	
			if (anoHull1 != null) {
				thisPlot.removeAnnotation(anoHull1);
			}
	
			anoHull1 = new XYShapeAnnotation(shapes[2]);
			thisPlot.addAnnotation(anoHull1);
		}
	
		ArrayList<SVMDataItem> ch2 = model.getCH2();
		if (!ch2.isEmpty()) {
			double[] xPoints3 = new double[ch1.size()];
			double[] yPoints3 = new double[ch1.size()];
	
			for (int i = 0; i < ch2.size(); i++) {
				xPoints3[i] = ch2.get(i).getXValue();
				yPoints3[i] = ch2.get(i).getYValue();
			}
			path = new Path2D.Double();
	
			path.moveTo(xPoints3[0], yPoints3[0]);
			for (int i = 1; i < xPoints3.length; ++i) {
				path.lineTo(xPoints3[i], yPoints3[i]);
			}
			path.closePath();
			shapes[3] = path;
	
			if (anoHull2 != null) {
				thisPlot.removeAnnotation(anoHull2);
			}
	
			anoHull2 = new XYShapeAnnotation(shapes[3]);
			thisPlot.addAnnotation(anoHull2);
	
		}
	        
		System.out.println("CH found");
	}


	public void drawLine(SVMDataItem vector){
		thisPlot.clearAnnotations();
		
		double length = 500;
		
		
		XYLineAnnotation line = new XYLineAnnotation(
				vector.getXValue() * length, vector.getYValue() * length,
				vector.getXValue() * -length, vector.getYValue() * -length);
	    //thisPlot.addAnnotation(line);
	    
	    vector = vector.getAntiClockWiseNormal();
		final float dash1[] = { 2.0f, 2.0f };
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		XYLineAnnotation line2 = new XYLineAnnotation(
				vector.getXValue() * length, vector.getYValue() * length,
				vector.getXValue() * -length, vector.getYValue() * -length,
				dashed, Color.blue);
	    thisPlot.addAnnotation(line2);
	}

	private void initPopUpMenu(){
		//popup menu
		itp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPoint(0,xChart, yChart);
			}
		});
		
		itn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPoint(1,xChart, yChart);
			}
		});
		
		itd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (selectedEntity != null){
					XYItemEntity e = (XYItemEntity) selectedEntity;
		            XYDataset d = e.getDataset(); //TODO check selection for null
		            int s = e.getSeriesIndex();
		            int i = e.getItem();
		            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
		            
		            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
		            XYSeries ss =  dd.getSeries(s);
		            
		            System.out.println("duplicated:" + ss.getItems().get(i));
		            ss.add((XYDataItem) ss.getItems().get(i));
				}
			}
		});
		
		itr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				if (selectedEntity != null){
					XYItemEntity e = (XYItemEntity) selectedEntity;
		            XYDataset d = e.getDataset(); //TODO check selection for null
		            int s = e.getSeriesIndex();
		            int i = e.getItem();
		            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
		            
		            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
		            XYSeries ss =  dd.getSeries(s);
		            
		            System.out.println("rem:" + ss.getItems().get(i));
		            ss.remove(i);
				}
	            
			}
		});
		
		itw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
	            XYItemEntity e = (XYItemEntity) selectedEntity;
	            XYDataset d = e.getDataset(); //TODO check selection for null
	            int s = e.getSeriesIndex();
	            int i = e.getItem();
	            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
	            
	            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
	            XYSeries ss =  dd.getSeries(s);
	            
	            
	            XYDataItem item =  (XYDataItem) ss.getItems().get(i);
	            System.out.println("rem:" + item);
	           
				if (item instanceof SVMDataItem){
					SVMDataItem svmItem = (SVMDataItem) item;
		            double weight = svmItem.getWeight();
					String input = JOptionPane.showInputDialog("Enter weight of the point"  ,weight);
					if (input != null){
						weight = Double.parseDouble(input); // try catch TODO parse error handle
						svmItem.setWeight(weight);
						svmItem.setLabel(weight + "");
						thisChart.fireChartChanged();
						System.out.println( svmItem);
					}
					
					
				}
			}
		});
		
	}

	private SVMDataItem getSelectedDataItem(ChartMouseEvent event, double x, double y){
		XYItemEntity ent = getSelectedEntity(event, x, y);
		return getDataItem(ent);
	}
	
	private XYItemEntity getSelectedEntity(ChartMouseEvent event, double x, double y){
		ChartEntity ce = event.getEntity();
		List list = event.getEntities();
		
		//TODO selectable layer?? cant select and delete centroid
		
		if (list == null){
			selectedEntity = null;
		}else{
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
			            XYSeries ss =  dd.getSeries(s);
			            
			            
			            //ss.remove(i);
			            selectedSeries = ss;
			            selectedEntity = e;
			            System.out.format("Selected Item:%s:%s\n",i, ss.getItems().get(i));
			            break;
		            }
				}
			}			
		}
		

        
		return selectedEntity;
		
	}
	
	private double toChartX(int x){
		//TODO translate function not used *ScaleFactor not done
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		return  plot.getDomainAxis().java2DToValue(x,
				plotArea, plot.getDomainAxisEdge());
	}
	
	private double toChartY(int y){
		//TODO translate function not used *ScaleFactor not done
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		return  plot.getRangeAxis().java2DToValue(y,
				plotArea, plot.getRangeAxisEdge());
	}
	
	
	private int toPanelX(double x){
		//TODO translate function not used *ScaleFactor not done
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		return  (int) plot.getDomainAxis().valueToJava2D(x,
				plotArea, plot.getDomainAxisEdge());
	}
	
	private int toPanelY(double y){
		//TODO translate function not used *ScaleFactor not done
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		return  (int) plot.getRangeAxis().valueToJava2D(y,
				plotArea, plot.getRangeAxisEdge());
	}
	
	private Point2D panelToChart(Point p){
		Point2D pp = this.translateScreenToJava2D(p);
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		double chartX = plot.getDomainAxis().java2DToValue(pp.getX(),
				plotArea, plot.getDomainAxisEdge());
		double chartY = plot.getRangeAxis().java2DToValue(pp.getY(),
				plotArea, plot.getRangeAxisEdge());
		return new Point2D.Double(chartX, chartY);	
	}
	
	private Point chartToPanel (Point2D pp){
		//Point2D pp = this.translateScreenToJava2D(p);
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		double panelX = plot.getDomainAxis().valueToJava2D(pp.getX(),
				plotArea, plot.getDomainAxisEdge());
		double panelY = plot.getRangeAxis().valueToJava2D(pp.getY(),
				plotArea, plot.getRangeAxisEdge());
		Point2D p2 =new Point2D.Double(panelX, panelY);
		return this.translateJava2DToScreen(p2);
	}
	
	private SVMDataItem getDataItem(XYItemEntity entity){
	
	    if (entity != null){
	        XYSeriesCollection sc = (XYSeriesCollection) entity.getDataset();
	        XYSeries s =  sc.getSeries(entity.getSeriesIndex());
	        int i = entity.getItem();
	        
	    	XYDataItem item =  (XYDataItem) s.getItems().get(i);
	    	if (item instanceof SVMDataItem){
	    		SVMDataItem svmItem = (SVMDataItem) item;
	    		return svmItem;
	    	}
	    }
	    
		return null;
	}


	private void addPoint(int series, double x, double y){
		
		XYSeriesCollection d = (XYSeriesCollection) getChart().getXYPlot().getDataset();
		XYSeries s =  d.getSeries(series);
		
		SVMDataItem i = new SVMDataItem(x, y);
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