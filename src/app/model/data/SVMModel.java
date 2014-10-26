package app.model.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.algorithms.WRCH;
import app.model.algorithms.WSK;

public class SVMModel {
	
	private ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	

	
	private ArrayList<SVMDataItem> ch1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	
	private ArrayList<SVMDataItem> ch2 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private SVMDataItem w = new SVMDataItem(0,1);
	private double b = 0;
		
	private SVMDataItem centroid1;
	private SVMDataItem centroid2;
	
	public SVMDataItem getCentroid1() {
		return centroid1;
	}

	public SVMDataItem getCentroid2() {
		return centroid2;
	}

	
	
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
		
	private SVMDataSet rawDataSet = null;
	private SVMDataSet solutionDataSet = null;
	
	public SVMDataSet getSolutionDataSet() {
		return solutionDataSet;
	}

	public void setSolutionDataSet(SVMDataSet solutionDataSet) {
		this.solutionDataSet = solutionDataSet;
	}

	public SVMDataSet getRawDataSet() {
		return rawDataSet;
	}

	public void setRawDataSet(SVMDataSet rawDataSet) {
		this.rawDataSet = rawDataSet;
	}

	private SVMDataSeries positiveSeries = null;
	public SVMDataSeries getPositiveSeries() {
		return positiveSeries;
	}

	public void setPositiveSeries(SVMDataSeries positiveSeries) {
		this.positiveSeries = positiveSeries;
	}
	private SVMDataSeries negativeSeries = null;
	public SVMDataSeries getNegativeSeries() {
		return negativeSeries;
	}

	public void setNegativeSeries(SVMDataSeries negativeSeries) {
		this.negativeSeries = negativeSeries;
	}
	
	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		
		rawDataSet = new SVMDataSet();
		positiveSeries = new SVMDataSeries("Positive Class");
		negativeSeries = new SVMDataSeries("Negative Class");
		rawDataSet.addSeries(positiveSeries);
		rawDataSet.addSeries(negativeSeries);
		
		solutionDataSet = new SVMDataSet();
		SVMDataSeries series3 = new SVMDataSeries("Positive WRCH");
		SVMDataSeries series4 = new SVMDataSeries("Negative WRCH");
		SVMDataSeries series5 = new SVMDataSeries("Centroids");
		solutionDataSet.addSeries(series3);
		solutionDataSet.addSeries(series4);
		solutionDataSet.addSeries(series5);

		initializeData();
	}
	
	public void generateRandomData(int numPoints, int percentPos, int softnessDelta){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(rawDataSet, numPoints, percentPos, softnessDelta);
	}
	
	public void setDataset(String name){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.setPredefinedDataset(rawDataSet, name);
	}
	
	private void initializeData(){
		
		
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(rawDataSet, 10, 50, 0);
		
//		modelDataSet.getSeries(0).clear();
//		modelDataSet.getSeries(1).clear();
		
		rawDataSet.getSeries(0).add(new SVMDataItem(6, 2));
		rawDataSet.getSeries(0).add(new SVMDataItem(5, 3));
		rawDataSet.getSeries(0).add(new SVMDataItem(6, 6));
		//modelDataSet.getSeries(0).add(new SVMDataItem(8, 4, 2));
		
//		dataset.getSeries(0).add(new SVMDataItem(4, 4));
//		dataset.getSeries(0).add(new SVMDataItem(8, 8));
//		dataset.getSeries(0).add(new SVMDataItem(8, 1));
		
		
//		dataset.getSeries(0).add(new SVMDataItem(4, 4));
//		dataset.getSeries(0).add(new SVMDataItem(8, 8));
//		dataset.getSeries(0).add(new SVMDataItem(8, 1));
		
		
		
////
		rawDataSet.getSeries(1).add(new SVMDataItem(1.0, 5.0));
		rawDataSet.getSeries(1).add(new SVMDataItem(3.0, 5.0));
		rawDataSet.getSeries(1).add(new SVMDataItem(5.0, 5.0));
		rawDataSet.getSeries(1).add(new SVMDataItem(4.0, 8.0));
		
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
		dataset1 = rawDataSet.getSeries(0).toArrayList();
		dataset2 = rawDataSet.getSeries(1).toArrayList();

		findWRCH(2);
		solveSVM();
		
		centroid1 = WRCH.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = WRCH.findCentroid(dataset2);
		centroid2.setLabel("-");
		SVMDataSeries s5= getSolutionDataSet().getSeries(2);
		s5.clear();
		s5.add(getCentroid1());
		s5.add(getCentroid2());
		
	}
	
	public void findWRCH(int series){

		if (series == 0){
			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
		}else if (series == 1){
			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
		}else{
			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
		}
		
		SVMDataSeries s3 = getSolutionDataSet().getSeries(0);
		s3.clear();
		for (int i = 0; i < rch1.size(); i++){//TODO null exception 23/10
			s3.add(rch1.get(i));
		}
		
		SVMDataSeries s4= getSolutionDataSet().getSeries(1);
		s4.clear();
		for (int i = 0; i < rch2.size(); i++){
			s4.add(rch2.get(i));
		}
	}
	
	public boolean solveSVM(){
		SVMDataItem[] Ppos;
		SVMDataItem[] Pneg;
		
		Ppos = new SVMDataItem[dataset1.size()];
		Pneg = new SVMDataItem[dataset2.size()];
		double[] pweights = new double [Ppos.length];
		double[] nweights = new double [Pneg.length];
		
		
		
		if (Ppos.length <= 0 || Pneg.length <= 0){
			return false;
		}
		
		
		for (int i = 0; i < Ppos.length; i++){
			Ppos[i] = new SVMDataItem(dataset1.get(i).getXValue(),
					dataset1.get(i).getYValue(),
					dataset1.get(i).getWeight());
			Ppos[i].setDataClass(1);	//TODO
			pweights[i] = dataset1.get(i).getWeight();
		}
		
		for (int i = 0; i < Pneg.length; i++){
			Pneg[i] = new SVMDataItem(dataset2.get(i).getXValue(),
					dataset2.get(i).getYValue(),
					dataset2.get(i).getWeight());
			Pneg[i].setDataClass(-1);
			nweights[i] = dataset2.get(i).getWeight();
		}
		
		WSK.wsk(Ppos, Pneg,pweights,nweights,  getMu1(), getMu2());
		
		b = WSK.getFinalB();
		w = WSK.getFinalW();
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
	
	public SVMDataItem getW(){
		return w;	//reference or value TODO
	}
	
	public double getB(){
		return b;
	}
	
	public void clearDataSet(int series){
		rawDataSet.getSeries(0).clear();
		rawDataSet.getSeries(1).clear();
		solutionDataSet.getSeries(0).clear();
		solutionDataSet.getSeries(1).clear();
		solutionDataSet.getSeries(2).clear();
		
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
		SVMDataItem p =  WSK.getNearestPositivePoint();
		SVMDataItem n = WSK.getNearestNegativePoint();
		
		SVMDataItem mid = new SVMDataItem(
				(p.getXValue() + n.getXValue()) / 2,
				(p.getYValue() + n.getYValue()) / 2);

        
        double f = 5000.0;
        double delta = b / w.getMagnitude();
      
        SVMDataItem normal = new SVMDataItem(-w.getYValue(), w.getXValue());
        //normal.setX(normal.getXValue() + delta);
        //normal.setY(normal.getYValue() + delta);
        
//        Point.Double p1= new Point.Double(
//        		(normal.getXValue() * f) + mid.getXValue(),
//        		(normal.getYValue() * f) + mid.getYValue());
//        Point.Double p2= new Point.Double(
//        		(normal.getXValue() * -f) + mid.getXValue(),
//        		(normal.getYValue() * -f) + mid.getYValue());
        
        
        
        
      Point.Double p1= new Point.Double(
		(normal.getXValue() * f + delta) ,
		(normal.getYValue() * f + delta) );
Point.Double p2= new Point.Double(
		(normal.getXValue() * -f + delta) ,
		(normal.getYValue() * -f + delta) );
        //System.out.format("Line points:\np1:%s\np2:%s -- %f\n", p1, p2, delta );
        return new Line2D.Double(p1, p2);
        
//        double yMin = -500;
//        double yMax = 500;
//        double xMin = ((b-(w.getYValue()*yMin))/w.getXValue());
//        double xMax =   ((b-(w.getYValue()*yMax))/w.getXValue());
//        
//        Point.Double p1= new Point.Double(xMin, yMin);
//        Point.Double p2= new Point.Double(xMax, yMax);
//        return new Line2D.Double(p1, p2);
	}
	
	public SVMDataItem getPositiveMargin(){
		SVMDataSeries positives = getPositiveSeries();
		SVMDataItem item = null;
		SVMDataItem max = null;
		double prod = 0.0;
		double maxProd = 0.0;
	
		for (int i = 0; i < positives.getItemCount(); i++){
			item = positives.getRawDataItem(i);
			prod = item.getDotProduct(getW());
			
			if (max == null){
				max = item;
				maxProd = prod;
			}
			
			if (prod < maxProd){
				
			}
		}
		
		return null;
	}
	
	public SVMDataItem getNegativeMargin(){
		return null;
	}
	
	public SVMDataItem getHyperplane(){
		return getW().getAntiClockWiseNormal(); //TODO sure?
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
