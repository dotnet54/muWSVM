package app.model.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.algorithms.WRCH;
import app.model.algorithms.WSK;

public class SVMModel {
	
	private ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	
	
//	XYSeries series1 = new XYSeries("Positive Class");
//	XYSeries series2 = new XYSeries("Negative Class");
	
	private SVMDataItem[] points;
	private Random randomGenerator = new Random();
	
	private ArrayList<SVMDataItem> ch1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	
	private ArrayList<SVMDataItem> ch2 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private SVMDataItem w = new SVMDataItem(0,1);
	private double b = 0;
		
	private SVMDataItem centroid1;
	public SVMDataItem getCentroid1() {
		return centroid1;
	}

	public SVMDataItem getCentroid2() {
		return centroid2;
	}

	private SVMDataItem centroid2;
	
	public class inParam{
		private double mu[];	//mu per class
		private boolean useSameMu = false;
		
		private int maxIterationWSK = 50;
		private int maxRecursionWRCH = 100;
		private double epsilon = 0.001;
		private int roundOff = 2;
	}
	
	public class outParam{
		private int numSV[];	//num SV per class
		private double alphas[][];	//alphas per class
	}
		
	private SVMDataSet modelDataSet = null;
	
	public SVMDataSet getModelDataSet() {
		return modelDataSet;
	}

	public void setModelDataSet(SVMDataSet modelDataSet) {
		this.modelDataSet = modelDataSet;
	}

	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		
		modelDataSet = new SVMDataSet();
		SVMDataSeries series3 = new SVMDataSeries("Positive WRCH");
		SVMDataSeries series4 = new SVMDataSeries("Negative WRCH");
		SVMDataSeries series5 = new SVMDataSeries("Centroids");
		modelDataSet.addSeries(series3);
		modelDataSet.addSeries(series4);
		modelDataSet.addSeries(series5);
		
		initializeData();
	}
	
	public void generateRandomData(){
		SVMDataGenerator dataGen = new SVMDataGenerator(10, 0, 10);
		modelDataSet = dataGen.getData();
	}
	
	
	private void initializeData(){
		
		
		SVMDataGenerator dataGen = new SVMDataGenerator(3, 0, 10);

		modelDataSet = dataGen.getData();
		
//		modelDataSet.getSeries(0).clear();
//		modelDataSet.getSeries(1).clear();
		
		modelDataSet.getSeries(0).add(new SVMDataItem(6, 2));
		modelDataSet.getSeries(0).add(new SVMDataItem(5, 3));
		modelDataSet.getSeries(0).add(new SVMDataItem(6, 6));
		//modelDataSet.getSeries(0).add(new SVMDataItem(8, 4, 2));
		
//		dataset.getSeries(0).add(new SVMDataItem(4, 4));
//		dataset.getSeries(0).add(new SVMDataItem(8, 8));
//		dataset.getSeries(0).add(new SVMDataItem(8, 1));
		
		
//		dataset.getSeries(0).add(new SVMDataItem(4, 4));
//		dataset.getSeries(0).add(new SVMDataItem(8, 8));
//		dataset.getSeries(0).add(new SVMDataItem(8, 1));
		
		
		
////
		modelDataSet.getSeries(1).add(new SVMDataItem(1.0, 5.0));
		modelDataSet.getSeries(1).add(new SVMDataItem(3.0, 5.0));
		modelDataSet.getSeries(1).add(new SVMDataItem(5.0, 5.0));
		modelDataSet.getSeries(1).add(new SVMDataItem(4.0, 8.0));
		
	}
	
//	public void addRandomData(){
//		clearPlot();
//		
//		//TODO use generator and set seed
//		Random r = new Random(System.currentTimeMillis());
//		
//		int max  = (int) (r.nextDouble() * 50) + 0;
//		
//		for (int i = 0; i < max; i++ ){
//			double randX  =  (r.nextDouble() * 20);
//			double randY  =  (r.nextDouble() * 20);
//			series1.add(new SVMDataItem(randX, randY));
//		}
//		for (int i = 0; i < max/8; i++ ){
//			double randX  =  (r.nextDouble() * 20);
//			double randY  =  (r.nextDouble() * 20);
//			series2.add(new SVMDataItem(randX, randY));
//		}
//		
//		model.setSeries1(series1);
//		model.setSeries2(series2);
//        chartPanel.thisPlot.getRangeAxis().setRange(0, 20);
//        chartPanel.thisPlot.getDomainAxis().setRange(0, 20);
//	}
	
	public void compute(){
		calculateHull(0);
		solveSVM();
	}
	
	public boolean calculateHull(int series){
		if (series == 1){
			
			if (dataset1.size() == 0){
				return false;
			}else{
				ch1 = WRCH.calcWeightedReducedCHull(dataset1, 1.0);
				rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
			}
			
		}else if (series == 2){
			if (dataset2.size() == 0){
				return false;
			}else{
				ch2 = WRCH.calcWeightedReducedCHull(dataset2, 1.0);
				rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
			}
			
		}else{
			if (dataset1.size() != 0){
				//ch1 = RCH.calcReducedCHull(dataset1, 1.0);
				rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
			}//else return false TODO
			if (dataset2.size() != 0){
				//ch2 = RCH.calcReducedCHull(dataset2, 1.0);
				rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
			}
		}
		
		centroid1 = WRCH.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = WRCH.findCentroid(dataset2);
		centroid2.setLabel("-");
		return true;
	}
	
	public boolean solveSVM(){
		WSK.solve(this);
		return true;
	}

	
	public void setMu(double m1, double m2){
		setMu1(m1);
		setMu2(m2);
	}
	public void setMu1(double m1){
		mu1=m1;
		//compute();	//FIRE model change event TODO
	}
	public void setMu2(double m2){
		mu2=m2;
		//compute();
	}
	public double getMu1(){
		return mu1;
	}
	
	public double getMu2(){
		return mu2;
	}
	
	public void setW(SVMDataItem w){
		this.w = w;
	}
	
	public void setB(double b){
		this.b = b;
	}
	
	public SVMDataItem getW(){
		return w;	//reference or value TODO
	}
	
	public double getB(){
		return b;
	}
	
	public void clearDataSet(int series){
		if (series == 1){
			dataset1.clear();
			ch1.clear();
			rch1.clear();
		}else if (series == 2){
			dataset2.clear();
			ch2.clear();
			rch2.clear();
		}else{
			dataset1.clear();
			ch1.clear();
			rch1.clear();
			dataset2.clear();
			ch2.clear();
			rch2.clear();
		}
	}
	public Line2D getLine(SVMDataItem w, double b){
        double yMin = -500;
        double yMax = 500;
        double xMin = ((b-(w.getYValue()*yMin))/w.getXValue());
        double xMax = ((b-(w.getYValue()*yMax))/w.getXValue());
        
//        double mx = w.getXValue();
//        double my = w.getYValue();
//        double m = w.getMagnitude();
//        
//        double xMin = yMin / m;
//        double xMax = yMax / m;
		SVMDataItem p =  WSK.getNearestPositivePoint();
		SVMDataItem n = WSK.getNearestNegativePoint();
		SVMDataItem mid = new SVMDataItem(
				(p.getXValue() + n.getXValue()) / 2,
				(p.getYValue() + n.getYValue()) / 2);
		
        xMin = -500;
        xMax = 500;
        //yMix = w.getXValue() *  
        
        double f = 5000.0;
        SVMDataItem normal = new SVMDataItem(-w.getYValue(), w.getXValue());
       // SVMDataItem normal = new SVMDataItem(w.getXValue(), w.getYValue());
        
        getModelDataSet().getSeries(4).add(mid);
        
        
        Point.Double p1= new Point.Double(
        		(normal.getXValue() * f) + mid.getXValue(),
        		(normal.getYValue() * f) + mid.getYValue());
        Point.Double p2= new Point.Double(
        		(normal.getXValue() * -f) + mid.getXValue(),
        		(normal.getYValue() * -f) + mid.getYValue());
        System.out.format("Line points:\np1:%s\np2:%s\n", p1, p2 );
        return new Line2D.Double(p1, p2);
	}
	
	public static Line2D getPerpendicularBisector(){
		SVMDataItem p =  WSK.getNearestPositivePoint();
		SVMDataItem n = WSK.getNearestNegativePoint();
		
		SVMDataItem mid = new SVMDataItem(
			(p.getXValue() + p.getYValue()) / 2,
			(n.getXValue() + n.getYValue()) / 2);
		
		double m =(p.getYValue() - n.getYValue())/
				(p.getXValue() - n.getXValue());
		double mp = -1.0/m;
		
		double b = mid.getYValue() / (mp * mid.getXValue());
		
		
		double xMin = -500;
        double xMax =   500;
        double yMin = mp * xMin + b;
        double yMax = mp * xMax + b;
        
		
        Point.Double p1= new Point.Double(xMin, yMin);
        Point.Double p2= new Point.Double(xMax, yMax);
        return new Line2D.Double(p1, p2);
	}
	
	
	public void setSeries1(XYSeries s){
		dataset1.clear();
		
		//SVMDataItem item = null;
		SVMDataItem item =  null;
		for (int i = 0; i < s.getItemCount(); i++){
			item = (SVMDataItem) s.getItems().get(i);
			
			//double x =  item.getXValue();	//TODO cast double problem
			//double y =  item.getYValue();	//TODO cast double problem
			dataset1.add(item);
			
		}
	}
	
	public void setSeries2(XYSeries s){
		dataset2.clear();
		
		SVMDataItem item =  null;
		for (int i = 0; i < s.getItemCount(); i++){
			item = (SVMDataItem) s.getItems().get(i);
			
			//double x =  item.getXValue();	//TODO cast double problem
			//double y =  item.getYValue();	//TODO cast double problem
			dataset2.add(item);
			
		}
	}
	
	public ArrayList<SVMDataItem> getSeries1(){
		return dataset1;
	}
	
	public ArrayList<SVMDataItem> getSeries2(){
		return dataset2;
	}
	
	public ArrayList<SVMDataItem> getCH1(){
		return ch1;
	}
	
	public ArrayList<SVMDataItem> getRCH1(){
		return rch1;
	}
	
	public ArrayList<SVMDataItem> getCH2(){
		return ch2;
	}
	
	public ArrayList<SVMDataItem> getRCH2(){
		return rch2;
	}
	

}
