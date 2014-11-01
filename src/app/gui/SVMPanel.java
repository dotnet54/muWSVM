package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.MenuItem;
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
import app.model.data.DVector;
import app.model.data.IObserver;
import app.model.data.ISubject;
import app.model.data.SVMDataItem;
import app.model.data.SVMDataSet;
import app.model.data.SVMModel;

public class SVMPanel extends ChartPanel 
						implements ChartMouseListener, ISubject{
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
	private SVMDataSet chartData = null;
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
	
	private boolean displayGeometricBoundary = true;
	private boolean displayKKTBoundary = false;
	private boolean displayNearestPointLine = true;
	private boolean displayCentroidConnectingLine = true;
	private boolean displayMargins = true;
	
	private boolean displaySlackVariables = false;
	private boolean displayslackAmount = false;
	
	private boolean enableZoom = true;
	private int pointSize = 2;
	
	
	//observer pattern 
    private List<IObserver> observers;
    private boolean changed;
    private final Object MUTEX= new Object();
	
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
		
		this.observers = new ArrayList<IObserver>();
		
		this.model = model;
		thisChart = chart;
		thisPlot = chart.getXYPlot();
		
		
		//Panel settings
		setMouseWheelEnabled(true);
		setZoomTriggerDistance(20);
		addChartMouseListener(this);
		//setMouseZoomable(false);
		//setDomainZoomable(false);
		//setRangeZoomable(false);
			
		//popup menu
		initPopUpMenu();
		setPopupMenu(popup);
		
		
		//Plot settings
		thisPlot.setDomainPannable(true);
		thisPlot.setRangePannable(true);
		thisPlot.getRangeAxis().setAutoRange(false);
		thisPlot.getDomainAxis().setAutoRange(false);
		thisPlot.getRangeAxis().setRange(0, 10);
		thisPlot.getDomainAxis().setRange(0, 10);
        

		
		

		//Dataset settings
		chartData = model.getChartDataset();
		thisPlot.setDataset(1, model.getSolutionDataSet());
		thisPlot.setRenderer(1, new XYLineAndShapeRenderer(false, true));
		
        //Renderer settings
//		XYDotRenderer rawDataRenderer = new XYDotRenderer();
//		rawDataRenderer.setDotWidth(4);
//		rawDataRenderer.setDotHeight(4);
//		thisPlot.setRenderer(rawDataRenderer);
		
        XYLineAndShapeRenderer rawDataRenderer = (XYLineAndShapeRenderer) thisPlot.getRenderer();
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

	public void mousePressed(MouseEvent event) {
		super.mousePressed(event);
		
		System.out.format("mPressed: %s\n", event.getSource());
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
		super.mouseReleased(event);

		Rectangle2D plotArea = this.getScreenDataArea();
		double chartX = thisPlot.getDomainAxis().java2DToValue(event.getX(),
				plotArea, thisPlot.getDomainAxisEdge());
		double chartY = thisPlot.getRangeAxis().java2DToValue(event.getY(),
				plotArea, thisPlot.getRangeAxisEdge());

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
		ChartMouseEvent chartEvent = new ChartMouseEvent(getChart(), event, list, event.getX(), event.getY());
		xChart = chartX;
		yChart = chartY;

		Point2D p = chartToPanel(new Point2D.Double(chartX, chartY));
		Point2D p2 = panelToChart(new Point(event.getX(), event.getY()));

		selectedEntity = getSelectedEntity(chartEvent, chartX, chartY);
		// System.out.println("releasedb on:" + selection.toString());
		
		if (event.isPopupTrigger()){
			if (plotArea.contains(event.getX(), event.getY())){
				if (selectedEntity == null) {
					popup.removeAll();
					popup.add(itp);
					popup.add(itn);
					popup.pack();
					popup.show(this, event.getX(), event.getY());
				} else {
					popup.removeAll();
					popup.add(itp);
					popup.add(itn);
					popup.addSeparator();
					popup.add(itd);// TODO duplicate point
					popup.add(itr);
					popup.add(itw);
					popup.pack();
					popup.show(this, event.getX(), event.getY());
				}					
			}else{
				popup.setVisible(false);
			}
		}
		//TODO 
		thisPlot.getRangeAxis().setAutoRange(false);
		thisPlot.getDomainAxis().setAutoRange(false);
       // plot.getRangeAxis().setRange(0, 10);
        //plot.getDomainAxis().setRange(0, 20);
	}
	
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
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
			model.clearDataSet(0);
	}
	
	public void updateChart(){
		updateWRCHSolutions();
		updateWSVMSolutions();
	}

	public void updateWRCHSolutions(){
		final float dash1[] = { 2.0f, 2.0f };
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		Path2D path;
		final Shape[] shapes = new Shape[3];
		thisPlot.clearAnnotations();
	
		ArrayList<SVMDataItem> rch1 = model.getRCH1();
		ArrayList<SVMDataItem> rch2 = model.getRCH2();
		
		if (rch1 != null && !rch1.isEmpty() && displayPositiveWRCH) {
			path = new Path2D.Double();
			path.moveTo(rch1.get(0).getXValue(), rch1.get(0).getYValue());
			for (int i = 1; i < rch1.size(); i++) {
				path.lineTo(rch1.get(i).getXValue(), rch1.get(i).getYValue());
			}
			path.closePath();
			shapes[0] = path;

			if (anoRHull1 != null) {
				thisPlot.removeAnnotation(anoRHull1);
			}
	
			anoRHull1 = new XYShapeAnnotation(shapes[0], dashed, Color.blue);
			thisPlot.addAnnotation(anoRHull1);
		}

		if (rch2 != null && !rch2.isEmpty() && displayNegativeWRCH) {
			path = new Path2D.Double();
			path.moveTo(rch2.get(0).getXValue(), rch2.get(0).getYValue());
			for (int i = 1; i < rch2.size(); i++) {
				path.lineTo(rch2.get(i).getXValue(), rch2.get(i).getYValue());
			}
			path.closePath();
			shapes[1] = path;
	
			if (anoRHull2 != null) {
				thisPlot.removeAnnotation(anoRHull2);
			}
	
			anoRHull2 = new XYShapeAnnotation(shapes[1], dashed, Color.blue);
			thisPlot.addAnnotation(anoRHull2);
		}
	
	}

	public void updateWSVMSolutions(){
		try {
			DVector w = model.getW();
			double b = model.getB();
			DVector p =  model.getNearestPositivePoint();
			DVector n = model.getNearestNegativePoint();			
			
			double y1 = -10000;
			double y2 = 10000;
			double x1 = (b - (w.getY() * y1))/ w.getX();
			double x2 = (b - (w.getY() * y2))/ w.getX();
			
			if (hyperPlane != null){
				thisPlot.removeAnnotation(hyperPlane);
			}
			if (marginPos != null){
				thisPlot.removeAnnotation(marginPos);
			}
			if (marginNeg != null){
				thisPlot.removeAnnotation(marginNeg);
			}
			
			hyperPlane = new XYLineAnnotation(x1, y1, x2, y2);
			b = p.getDotProduct(w);
			x1 = (b - (w.getY() * y1))/ w.getX();
			x2 = (b - (w.getY() * y2))/ w.getX();
			marginPos = new XYLineAnnotation(x1, y1, x2, y2);
			b = n.getDotProduct(w);
			x1 = (b - (w.getY() * y1))/ w.getX();
			x2 = (b - (w.getY() * y2))/ w.getX();
			marginNeg = new XYLineAnnotation(x1, y1, x2, y2);
			
			if (nearestPointLine != null){
				thisPlot.removeAnnotation(nearestPointLine);
			}
			nearestPointLine = new XYLineAnnotation(
					p.getX(), p.getY(),
					n.getX(), n.getY());
			
			
			
			if (displayGeometricBoundary){
				thisPlot.addAnnotation(hyperPlane);
			}
			if (displayMargins){
				thisPlot.addAnnotation(marginPos);
				thisPlot.addAnnotation(marginNeg);
			}
			if (displayNearestPointLine){
			    thisPlot.addAnnotation(nearestPointLine);
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}

		System.out.format("SVM found w:%s b:%s \n", model.getW(), model.getB() );
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
				addPoint(0);
			}
		});
		
		itn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPoint(1);
			}
		});
		
		itd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				duplicateSelection(1);
			}
		});
		
		itr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				removeSelection();
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

	public SVMDataItem getSelectedDataItem(){
		return selectedDataItem;
	}
	
	private XYItemEntity getSelectedEntity(ChartMouseEvent event, double x, double y){
		ChartEntity ce = event.getEntity();
		List list = event.getEntities();
		selectedEntity = null;
		selectedDataItem = null;
		
		//TODO selectable layer?? cant select and delete centroid
		
		if (list != null){
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
			            
			            SVMDataItem item  = (SVMDataItem) ss.getItems().get(i);
			            
			            if (d.equals(model.getChartDataset())
			            	&&	s == 0 
			            	|| s == 1){ //assume index 0 and 1 = pos/neg 
				            //ss.remove(i);
				            selectedSeries = ss;
				            selectedEntity = e;
				            selectedDataItem = item;
				            System.out.format("Selected Item:%s:%s\n",i, item);
				            break;			            	
			            }
		            }
				}
			}			
		}
		

        this.changed = true;
        notifyObservers();
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
	
	public void addPoint(int series){
		
		XYSeriesCollection d = (XYSeriesCollection) getChart().getXYPlot().getDataset();
		XYSeries s =  d.getSeries(series);
		
		SVMDataItem i = new SVMDataItem(xChart, yChart);
		s.add(i);
	}
	
	public void duplicateSelection(int numDuplicates){
		if (hasSelectedItem()){
			XYItemEntity e = (XYItemEntity) selectedEntity;
            XYDataset d = e.getDataset(); //TODO check selection for null
            int s = e.getSeriesIndex();
            int i = e.getItem();
            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
            
            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
            XYSeries ss =  dd.getSeries(s);
            
            System.out.println("duplicated:" + ss.getItems().get(i));
            for (int k = 0; k < numDuplicates; k++){
            	ss.add((XYDataItem) ss.getItems().get(i));
            }
            
		}
	}
	
	public void removeSelection(){
		if (hasSelectedItem()){
			XYItemEntity e = (XYItemEntity) selectedEntity;
            XYDataset d = e.getDataset(); //TODO check selection for null
            int s = e.getSeriesIndex();
            int i = e.getItem();
            //System.out.println("X:" + d.getX(s, i) + ", Y:" + d.getY(s, i));
            
            XYSeriesCollection dd = (XYSeriesCollection) e.getDataset();
            XYSeries ss =  dd.getSeries(s);
            
            System.out.println("rem:" + ss.getItems().get(i));
            ss.remove(i);
            
            clearSelection();
		}
	}
	
	public void changeSelectionWeight(){
		
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
    
    public boolean hasSelectedItem(){
    	if (selectedEntity != null && selectedDataItem != null){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    private void clearSelection(){
    	selectedDataItem = null;
    	selectedEntity = null;
    	selectedSeries = null;
    	
    	this.changed = true;
    	notifyObservers();
    }
    
    /**
     * based on 
     * http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
     * @param obj
     */
    @Override
    public void register(IObserver obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
        if(!observers.contains(obj)) observers.add(obj);
        }
    }
 
    /**
     * http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
     * @param obj
     */
    @Override
    public void unregister(IObserver obj) {
        synchronized (MUTEX) {
        observers.remove(obj);
        }
    }
 
    /**
     * http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
     */
    @Override
    public void notifyObservers() {
        List<IObserver> observersLocal = null;
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX) {
            if (!changed)
                return;
            observersLocal = new ArrayList<IObserver>(this.observers);
            this.changed=false;
        }
        for (IObserver obj : observersLocal) {
            obj.update();
        }
 
    }
}
