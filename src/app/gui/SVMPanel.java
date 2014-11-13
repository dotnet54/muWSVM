package app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;

import app.model.IObserver;
import app.model.ISubject;
import app.model.SVMModel;
import app.model.data.SVMDataItem;
import app.model.data.SVMDataSeries;
import app.model.data.SVMDataSet;
import app.test.SVMDataItemOLD2;

public class SVMPanel extends ChartPanel 
						implements ChartMouseListener, ISubject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1667095001940958696L;
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem itp = new JMenuItem("Add to Positive Class");
	private JMenuItem itn = new JMenuItem("Add to Negative Class");
	private JMenuItem itu = new JMenuItem("Add Unlabelled Data");
	private JMenuItem itd = new JMenuItem("Duplicate Point");
	private JMenuItem itr = new JMenuItem("Remove Point");
	private JMenuItem itw = new JMenuItem("Change Weight");
		
	protected XYPlot thisPlot = null;
	protected JFreeChart thisChart = null;
	
	
	private SVMModel model = null;
	private SVMDataSet chartData = null;
	private  XYShapeAnnotation annotationWRCH1 = null;
	private  XYShapeAnnotation annotationWRCH2 = null;
	
	private  XYLineAnnotation hyperPlane = null;
	private  XYLineAnnotation marginPos = null;
	private  XYLineAnnotation marginNeg = null;
	private  XYLineAnnotation nearestPointLine = null;
	
	private double xChart =0;
	private double yChart=0;
	
	private XYItemEntity selectedEntity = null;
	private SVMDataItem selectedDataItem = null;

	private XYLineAndShapeRenderer trainingDatasetRenderer;
	private XYLineAndShapeRenderer solutionRenderer;
	
	//Rendering Options
	

	public void setDisplayWeights(boolean displayWeights) {
		this.displayWeights = displayWeights;
		trainingDatasetRenderer.setBaseItemLabelsVisible(displayWeights);
	}

	public boolean isDisplayWeights() {
		return displayWeights;
		
	}

	public boolean isDisplayPositiveClass() {
		return displayPositiveClass;
	}

	public void setDisplayPositiveClass(boolean displayPositiveClass) {
		this.displayPositiveClass = displayPositiveClass;
		updateChart();
	}

	public boolean isDisplayNegativeClass() {
		return displayNegativeClass;
	}

	public void setDisplayNegativeClass(boolean displayNegativeClass) {
		this.displayNegativeClass = displayNegativeClass;
		updateChart();
	}

	public boolean isDisplayPositiveWRCH() {
		return displayPositiveWRCH;
	}

	public void setDisplayPositiveWRCH(boolean displayPositiveWRCH) {
		this.displayPositiveWRCH = displayPositiveWRCH;
		solutionRenderer.setSeriesVisible(0,displayPositiveWRCH);
		updateWRCHSolutions();
	}

	public boolean isDisplayNegativeWRCH() {
		return displayNegativeWRCH;
	}

	public void setDisplayNegativeWRCH(boolean displayNegativeWRCH) {
		this.displayNegativeWRCH = displayNegativeWRCH;
		solutionRenderer.setSeriesVisible(1,displayNegativeWRCH);
		updateWRCHSolutions();
	}

	public boolean isDisplayPositiveSupportVectors() {
		return displayPositiveSupportVectors;
	}

	public void setDisplayPositiveSupportVectors(
			boolean displayPositiveSupportVectors) {
		this.displayPositiveSupportVectors = displayPositiveSupportVectors;
	}

	public boolean isDisplayNegativeSupportVectors() {
		return displayNegativeSupportVectors;
	}

	public void setDisplayNegativeSupportVectors(
			boolean displayNegativeSupportVectors) {
		this.displayNegativeSupportVectors = displayNegativeSupportVectors;
	}

	public boolean isDisplayGeometricBoundary() {
		return displayGeometricBoundary;
	}

	public void setDisplayGeometricBoundary(boolean displayGeometricBoundary) {
		this.displayGeometricBoundary = displayGeometricBoundary;
		updateWSVMSolutions();
	}

	public boolean isDisplayKKTBoundary() {
		return displayKKTBoundary;
	}

	public void setDisplayKKTBoundary(boolean displayKKTBoundary) {
		this.displayKKTBoundary = displayKKTBoundary;
	}

	public boolean isDisplayNearestPointLine() {
		return displayNearestPointLine;
	}

	public void setDisplayNearestPointLine(boolean displayNearestPointLine) {
		this.displayNearestPointLine = displayNearestPointLine;
	}

	public boolean isDisplayCentroidConnectingLine() {
		return displayCentroidConnectingLine;
	}

	public void setDisplayCentroidConnectingLine(
			boolean displayCentroidConnectingLine) {
		this.displayCentroidConnectingLine = displayCentroidConnectingLine;
	}

	public boolean isDisplayMargins() {
		return displayMargins;
	}

	public void setDisplayMargins(boolean displayMargins) {
		this.displayMargins = displayMargins;
		updateWSVMSolutions();
	}

	public boolean isDisplaySlackVariables() {
		return displaySlackVariables;
	}

	public void setDisplaySlackVariables(boolean displaySlackVariables) {
		this.displaySlackVariables = displaySlackVariables;
	}

	public boolean isDisplayslackAmount() {
		return displayslackAmount;
	}

	public void setDisplayslackAmount(boolean displayslackAmount) {
		this.displayslackAmount = displayslackAmount;
	}

	public boolean isDisplayAlphaValues() {
		return displayAlphaValues;
	}

	public void setDisplayAlphaValues(boolean displayAlphaValues) {
		this.displayAlphaValues = displayAlphaValues;
	}

	public boolean isDisplayCentroids() {
		return displayCentroids;
	}

	public void setDisplayCentroids(boolean displayCentroids) {
		this.displayCentroids = displayCentroids;
		updateChart();
	}

	public boolean isEnableZoom() {
		return enableZoom;
	}

	public void setEnableZoom(boolean enableZoom) {
		this.enableZoom = enableZoom;
	}

	private boolean displayWeights = true;	
	private boolean displayAlphaValues = false;
	private boolean displayCentroids = true;
	
	private boolean displayPositiveClass = true;
	private boolean displayNegativeClass = true;

	private boolean displayPositiveWRCH = true;
	private boolean displayNegativeWRCH = true;
	
	private boolean displayPositiveSupportVectors = false;
	private boolean displayNegativeSupportVectors = false;

	private boolean displayGeometricBoundary = true;
	private boolean displayKKTBoundary = false;
	private boolean displayNearestPointLine = true;
	private boolean displayCentroidConnectingLine = true;
	private boolean displayMargins = true;
	
	private boolean displaySlackVariables = false;
	private boolean displayslackAmount = false;
	
	private boolean enableZoom = true;
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
            if (dataset instanceof SVMDataSet){
            	SVMDataSet svmData = (SVMDataSet) dataset;
            	SVMDataSeries svmSeries =  svmData.getSeries(series);
            	SVMDataItem svmItem = svmSeries.getDataItem(item);
            	//return svmItem.toFormatedString(2);
            	return  String.format("%.2f", svmItem.getWeight());
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
		
		thisPlot.getDomainAxis().setAutoRangeMinimumSize(1, true);
		thisPlot.getRangeAxis().setAutoRangeMinimumSize(1, true);
		thisPlot.getRangeAxis().setRange(0, 10);
		thisPlot.getDomainAxis().setRange(0, 10);
        

		
		

		//Dataset settings
		chartData = model.getChartDataset();
		thisPlot.setDataset(1, model.getSolutionDataSet());
		thisPlot.setRenderer(1, new XYLineAndShapeRenderer(false, true));
		thisPlot.setDataset(2, model.getTestData());
		thisPlot.setRenderer(2, new XYLineAndShapeRenderer(false, true));
		
		thisPlot.getRenderer(2).setSeriesShape(0, ShapeUtilities.createUpTriangle(2));
		thisPlot.getRenderer(2).setSeriesPaint(0, Color.GREEN.darker());
		
        //Renderer settings
		//currently not using dot render -> but can set this up if necessary
//		XYDotRenderer rawDataRenderer = new XYDotRenderer();
//		rawDataRenderer.setDotWidth(4);
//		rawDataRenderer.setDotHeight(4);
//		thisPlot.setRenderer(rawDataRenderer);
		
        trainingDatasetRenderer = (XYLineAndShapeRenderer) thisPlot.getRenderer();
        solutionRenderer = (XYLineAndShapeRenderer) thisPlot.getRenderer(1);

        trainingDatasetRenderer.setBaseItemLabelGenerator(new LabelGenerator());
        trainingDatasetRenderer.setBaseItemLabelPaint(Color.black);
        trainingDatasetRenderer.setBaseItemLabelsVisible(true);
        trainingDatasetRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());        
        trainingDatasetRenderer.setBasePositiveItemLabelPosition(
            new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BOTTOM_LEFT));
        trainingDatasetRenderer.setBaseItemLabelFont(
        		trainingDatasetRenderer.getBaseItemLabelFont().deriveFont(8f));
        
        solutionRenderer.setSeriesShape(0, ShapeUtilities.createDiagonalCross(1, 1));
        solutionRenderer.setSeriesPaint(0, Color.RED.brighter());
        
        solutionRenderer.setSeriesShape(1, ShapeUtilities.createDiagonalCross(1, 1));
        solutionRenderer.setSeriesPaint(1, Color.BLUE.brighter());
        
        solutionRenderer.setSeriesShape(2, ShapeUtilities.createDiagonalCross(2, 2));
        solutionRenderer.setSeriesPaint(2, Color.DARK_GRAY.brighter());
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
					popup.add(itu);
					popup.pack();
					popup.show(this, event.getX(), event.getY());
				} else {
					popup.removeAll();
					popup.add(itp);
					popup.add(itn);
					popup.add(itu);
					popup.addSeparator();
					popup.add(itd);
					popup.add(itr);
					popup.add(itw);
					popup.pack();
					popup.show(this, event.getX(), event.getY());
				}					
			}else{
				popup.setVisible(false);
			}
		}
		
		
		thisPlot.getRangeAxis().setAutoRange(false);
		thisPlot.getDomainAxis().setAutoRange(false);
	}
	
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent event) {
	}
	
	public void clearPlot(){
			XYPlot p = getChart().getXYPlot();
			model.clearDataSet(0);
			p.clearAnnotations();
	}
	
	public void updateChart(){
		
		
		trainingDatasetRenderer.setSeriesVisible(
				model.getTrainingData().getPositiveSeriesID(), isDisplayPositiveClass());
	
		trainingDatasetRenderer.setSeriesVisible(
				model.getTrainingData().getNegativeSeriesID(), isDisplayNegativeClass());
		
		
		solutionRenderer.setSeriesVisible(2,isDisplayCentroids());
		//updateWRCHSolutions();
		//updateWSVMSolutions();
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

			if (annotationWRCH1 != null) {
				thisPlot.removeAnnotation(annotationWRCH1);
			}
	
			annotationWRCH1 = new XYShapeAnnotation(shapes[0], dashed, Color.blue);
			thisPlot.addAnnotation(annotationWRCH1);
		}

		if (rch2 != null && !rch2.isEmpty() && displayNegativeWRCH) {
			path = new Path2D.Double();
			path.moveTo(rch2.get(0).getXValue(), rch2.get(0).getYValue());
			for (int i = 1; i < rch2.size(); i++) {
				path.lineTo(rch2.get(i).getXValue(), rch2.get(i).getYValue());
			}
			path.closePath();
			shapes[1] = path;
	
			if (annotationWRCH2 != null) {
				thisPlot.removeAnnotation(annotationWRCH2);
			}
	
			annotationWRCH2 = new XYShapeAnnotation(shapes[1], dashed, Color.blue);
			thisPlot.addAnnotation(annotationWRCH2);
		}
	
	}


	public void updateWSVMSolutions(){
		try {
			
			if (!model.isSVMSolved()){
				return;
			}
			
			SVMDataItem w = model.getW();
			double b = model.getB();
			SVMDataItem p =  model.getNearestPositivePoint();
			SVMDataItem n = model.getNearestNegativePoint();			
			
			double y1 = -10000;
			double y2 = 10000;
			double x1 = (b - (w.getYValue() * y1))/ w.getXValue();
			double x2 = (b - (w.getYValue() * y2))/ w.getXValue();
			
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
			x1 = (b - (w.getYValue() * y1))/ w.getXValue();
			x2 = (b - (w.getYValue() * y2))/ w.getXValue();
			marginPos = new XYLineAnnotation(x1, y1, x2, y2);
			b = n.getDotProduct(w);
			x1 = (b - (w.getYValue() * y1))/ w.getXValue();
			x2 = (b - (w.getYValue() * y2))/ w.getXValue();
			marginNeg = new XYLineAnnotation(x1, y1, x2, y2);
			
			if (nearestPointLine != null){
				thisPlot.removeAnnotation(nearestPointLine);
			}
			nearestPointLine = new XYLineAnnotation(
					p.getXValue(), p.getYValue(),
					n.getXValue(), n.getYValue());
			
			
			
			if (displayGeometricBoundary){
				thisPlot.addAnnotation(hyperPlane);
			}
			if (displayMargins){
				thisPlot.addAnnotation(marginPos);
				thisPlot.addAnnotation(marginNeg);
			}
			if (displayNearestPointLine){
				//uncomment for debugging
			   // thisPlot.addAnnotation(nearestPointLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.format("SVM found w:%s b:%s \n", model.getW(), model.getB() );
	}



	public void drawLine(SVMDataItem vector){
		try {
			thisPlot.clearAnnotations();
			
			double length = 500;
			
			
			XYLineAnnotation line = new XYLineAnnotation(
					vector.getXValue() * length, vector.getYValue() * length,
					vector.getXValue() * -length, vector.getYValue() * -length);
			//thisPlot.addAnnotation(line);
			
			vector = vector.get2DAntiClockwiseNormal();
			final float dash1[] = { 2.0f, 2.0f };
			final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			XYLineAnnotation line2 = new XYLineAnnotation(
					vector.getXValue() * length, vector.getYValue() * length,
					vector.getXValue() * -length, vector.getYValue() * -length,
					dashed, Color.blue);
			thisPlot.addAnnotation(line2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initPopUpMenu(){
		itp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addPoint(0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		itn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					addPoint(1);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		itu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SVMDataItem newPoint = new SVMDataItem(model.getTestData().getDimensions());
					int xDim = model.getTestData().getXDimension();
					int yDim = model.getTestData().getYDimension();
					
					newPoint.setVal(xDim, xChart);
					newPoint.setVal(yDim, yChart);
					model.getTestData().addItem(0, newPoint);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		itd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					duplicateSelection(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	            
	            XYDataset dataset = e.getDataset();
	            if (dataset instanceof SVMDataSet){
	            	SVMDataSet svmData = (SVMDataSet) dataset;
	            	SVMDataSeries series =  svmData.getSeries(e.getSeriesIndex());
	            	SVMDataItem item = series.getDataItem(e.getItem());
		            double weight = item.getWeight();
					String input = JOptionPane.showInputDialog("Enter weight of the point"  ,weight);
					if (input != null){
						weight = Double.parseDouble(input); 
						item.setWeight(weight);
						item.setLabel(weight + "");
						model.getTrainingData().setWeight(e.getSeriesIndex(), e.getItem(), weight);
						//thisChart.fireChartChanged(); //implementation moved to setWeight function
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
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); // your plot
		return  plot.getDomainAxis().java2DToValue(x,
				plotArea, plot.getDomainAxisEdge());
	}
	
	private Point2D panelToChart(Point p){
		Point2D pp = this.translateScreenToJava2D(p);
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); 
		double chartX = plot.getDomainAxis().java2DToValue(pp.getX(),
				plotArea, plot.getDomainAxisEdge());
		double chartY = plot.getRangeAxis().java2DToValue(pp.getY(),
				plotArea, plot.getRangeAxisEdge());
		return new Point2D.Double(chartX, chartY);	
	}
	
	private Point chartToPanel (Point2D pp){
		Rectangle2D plotArea = this.getScreenDataArea();
		XYPlot plot = (XYPlot) getChart().getPlot(); 
		double panelX = plot.getDomainAxis().valueToJava2D(pp.getX(),
				plotArea, plot.getDomainAxisEdge());
		double panelY = plot.getRangeAxis().valueToJava2D(pp.getY(),
				plotArea, plot.getRangeAxisEdge());
		Point2D p2 =new Point2D.Double(panelX, panelY);
		return this.translateJava2DToScreen(p2);
	}
	
	public void addPoint(int series, SVMDataItem newPoint) throws Exception{
		int xDim = model.getTrainingData().getXDimension();
		int yDim = model.getTrainingData().getYDimension();
		
		newPoint.setVal(xDim, xChart);
		newPoint.setVal(yDim, yChart);
		chartData.addItem(series, newPoint);
	}
	
	
	public void addPoint(int series) throws Exception{
		SVMDataItem newPoint = new SVMDataItem(model.getTrainingData().getDimensions());
		int xDim = model.getTrainingData().getXDimension();
		int yDim = model.getTrainingData().getYDimension();
		
		newPoint.setVal(xDim, xChart);
		newPoint.setVal(yDim, yChart);
		chartData.addItem(series, newPoint);
	}
	
	public void duplicateSelection(int numDuplicates) throws Exception{
		if (hasSelectedItem()){
			XYItemEntity e = (XYItemEntity) selectedEntity;
            XYDataset dataset = e.getDataset();
            if (dataset instanceof SVMDataSet){
            	SVMDataSet svmData = (SVMDataSet) dataset;
            	SVMDataSeries series =  svmData.getSeries(e.getSeriesIndex());
            	SVMDataItem item = series.getDataItem(e.getItem());
            	
                for (int k = 0; k < numDuplicates; k++){
                	series.add(item);
                }
            }
		}
	}
	
	public void removeSelection(){
		if (hasSelectedItem()){
			XYItemEntity e = (XYItemEntity) selectedEntity;
            XYDataset dataset = e.getDataset();
            if (dataset instanceof SVMDataSet){
            	SVMDataSet svmData = (SVMDataSet) dataset;
            	SVMDataSeries series =  svmData.getSeries(e.getSeriesIndex());
                series.remove(e.getItem());
            }
            
            clearSelection();
		}
	}
	
	public void changeSelectionWeight(double newWeight){
		if (hasSelectedItem()){
			XYItemEntity e = (XYItemEntity) selectedEntity;
            XYDataset dataset = e.getDataset();
            if (dataset instanceof SVMDataSet){
            	//use set weight function in dataset to notify observers
            	model.getTrainingData().setWeight(e.getSeriesIndex(), e.getItem(), newWeight);
            }
            
            clearSelection();
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
    	
    	this.changed = true;
    	notifyObservers();
    }
    
    /**
     * reference
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
     * reference
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
     * reference
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
