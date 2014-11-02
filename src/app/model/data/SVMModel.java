package app.model.data;

import java.util.ArrayList;
import app.model.algorithms.WRCH;

public class SVMModel {
	
	private ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	

	private ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private DVector w = new DVector(2);
	private double b = 0;
	private DVector nearestPositivePoint = null;
	private DVector nearestNegativePoint = null;	
		
	private SVMDataItem centroid1;
	private SVMDataItem centroid2;

	private SVMDataSet solutionDataSet = null;
	private SVMDataSeries positiveSeries = null;
	private SVMDataSeries negativeSeries = null;
	
	
	private DData trainingData = null;
	public DData getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(DData trainingData) {
		this.trainingData = trainingData;
	}

	public DData getTestData() {
		return testData;
	}

	public void setTestData(DData testData) {
		this.testData = testData;
	}

	private DData testData = null;
	private SVMDataSet chartData = null;

	
	
	
	public int numTruePositives;
	public int numFalsePositives;
	public int numTrueNegatives;
	public int numFalseNegatives;
	
	
	
	
	public SVMDataItem getCentroid1() {
		return centroid1;
	}

	public SVMDataItem getCentroid2() {
		return centroid2;
	}

	int numActualPositives;
	public int getNumActualPositives() {
		numActualPositives = chartData.getItemCount(0);
		return numActualPositives;
	}

	public int getNumActualNegatives() {
		numActualNegatives = chartData.getItemCount(1);
		return numActualNegatives;
	}

	public int getNumPredictedPositives() {
		return numPredictedPositives;
	}

	public int getNumPredictedNegatives() {
		return numPredictedNegatives;
	}

	int numActualNegatives;
	int numPredictedPositives;
	int numPredictedNegatives;
	
	
	public int getNumTruePositives(){
		return 0;
	}
	
	public int getNumTrueNegatives(){
		return 0;
	}
	
	public int getNumFalsePositives(){
		return 0;
	}
	
	public int getNumFalseNegatives(){
		return 0;
	}
	
	public int getAccuracy(){
		return 0;
	}
	
	public int getPrecision(){
		return 0;
	}
	
	public int getSensitivity(){
		return 0;
	}
	
	public int getSpecificity(){
		return 0;
	}
	
	public int getNumPositiveSupportVectors(){
		return 0;
	}
	
	public int getNumNegativeSupportVectors(){
		return 0;
	}
	
	public long getTimeElapsedForWSK(){
		return 0;
	}
	
	public long getTimeElapsedForWRCH(){
		return 0;
	}

	public SVMDataSet getSolutionDataSet() {
		return solutionDataSet;
	}

	public void setSolutionDataSet(SVMDataSet solutionDataSet) {
		this.solutionDataSet = solutionDataSet;
	}

	public SVMDataSet getChartDataset() {
		return chartData;
	}

	public void setChartDataset(SVMDataSet chartData) {
		this.chartData = chartData;
	}

	public SVMDataSeries getPositiveSeries() {
		return positiveSeries;
	}

	public void setPositiveSeries(SVMDataSeries positiveSeries) {
		this.positiveSeries = positiveSeries;
	}
	public SVMDataSeries getNegativeSeries() {
		return negativeSeries;
	}

	public void setNegativeSeries(SVMDataSeries negativeSeries) {
		this.negativeSeries = negativeSeries;
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

	public DVector getW(){
		return w;	//reference or value TODO
	}

	public double getB(){
		return b;
	}

	public void clearDataSet(int series){
		chartData.getSeries(0).clear();
		chartData.getSeries(1).clear();
		solutionDataSet.getSeries(0).clear();
		solutionDataSet.getSeries(1).clear();
		solutionDataSet.getSeries(2).clear();
		
		if (series == 1){
			dataset1.clear();
			rch1.clear();
		}else if (series == 2){
			dataset2.clear();
			rch2.clear();
		}else{
			dataset1.clear();
			rch1.clear();
			dataset2.clear();
			rch2.clear();
		}
	}

	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		
		trainingData = new DData(2);		
		testData = new DData(2);
		
		trainingData.setPositiveClass(+1);
		trainingData.setNegativeClass(-1);
		trainingData.setXDimension(0);
		trainingData.setYDimension(1);
		
		chartData = trainingData.getChartData();
		
		solutionDataSet = new SVMDataSet();
		SVMDataSeries series3 = new SVMDataSeries("Positive WRCH");
		SVMDataSeries series4 = new SVMDataSeries("Negative WRCH");
		SVMDataSeries series5 = new SVMDataSeries("Centroids");
		solutionDataSet.addSeries(series3);
		solutionDataSet.addSeries(series4);
		solutionDataSet.addSeries(series5);

		//initializeData();
	}
	
	public void updateTrainingSet(SVMDataSet chartData){
		//update positive class
		for (int i = 0; i < chartData.getItemCount(0); i++){
			
		}
		//update negative class
		for (int i = 0; i < chartData.getItemCount(1); i++){
			
		}
	}
	
	public void generateRandomData(int numPoints, int percentPos, int softnessDelta){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(chartData, numPoints, percentPos, softnessDelta);
	}
	
	public void loadPredefinedDataset(String name){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.setPredefinedDataset(chartData, name);
	}
	
	private void initializeData(){

		trainingData.generateRandomData(10);
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(chartData, 10, 50, 0);
		//dataGen.setPredefinedDataset(rawDataSet, "Triangle 1");

	}
	
	public void solveWRCH(int series){
		dataset1 = chartData.getSeries(0).toArrayList();
		dataset2 = chartData.getSeries(1).toArrayList();
		
//		if (series == 0){
//			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
//		}else if (series == 1){
//			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
//		}else{
//			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
//			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
//		}
		
		
		if (series == 0){
			rch1 = Dwrch.calcWeightedReducedCHull2(dataset1, mu1);
		}else if (series == 1){
			rch2 = Dwrch.calcWeightedReducedCHull2(dataset2, mu2);
		}else{
			rch1 = Dwrch.calcWeightedReducedCHull2(dataset1, mu1);
			rch2 = Dwrch.calcWeightedReducedCHull2(dataset2, mu2);
		}
		
		
		
//		BBY wrchSolver = new BBY();
//		if (series == 0){
//			rch1 = wrchSolver.calcWRCH(dataset1, mu1);
//			rch1 = wrchSolver.getWRCH();
//		}else if (series == 1){
//			rch2 = wrchSolver.calcWRCH(dataset2, mu2);
//			rch2 = wrchSolver.getWRCH();
//		}else{
//			rch1 = wrchSolver.calcWRCH(dataset1, mu1);
//			rch1 = wrchSolver.getWRCH();
//			rch2 = wrchSolver.calcWRCH(dataset2, mu2);
//			rch2 = wrchSolver.getWRCH();
//		}

		centroid1 = WRCH.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = WRCH.findCentroid(dataset2);
		centroid2.setLabel("-");
		SVMDataSeries s5= getSolutionDataSet().getSeries(2);
		s5.clear();
		s5.add(getCentroid1());
		s5.add(getCentroid2());
		
		
		SVMDataSeries s3 = getSolutionDataSet().getSeries(0);
		s3.clear();
		for (int i = 0; i < rch1.size(); i++){//TODO null exception 23/10 handle stackoverflow
			s3.add(rch1.get(i));
		}
		
		SVMDataSeries s4= getSolutionDataSet().getSeries(1);
		s4.clear();
		for (int i = 0; i < rch2.size(); i++){
			s4.add(rch2.get(i));
		}
	}
	
	public void solveSVM(){
		try {
			Dwsk wskSolver = new Dwsk();
			wskSolver.wsk(trainingData, getMu1(), getMu2());
			
			b = wskSolver.getB();
			w = wskSolver.getW();
			nearestPositivePoint = wskSolver.getNearestPositivePoint();
			nearestNegativePoint = wskSolver.getNearestNegativePoint();
			
			
			int count = 0;
			double proj = 0.0;
			DVector item = null;
			
			ArrayList<DVector> positiveClass = trainingData.getPositiveClass();
			numActualPositives = positiveClass.size();
			for (int i = 0; i < numActualPositives; i++){
				item = positiveClass.get(i);
				proj = getW().getDotProduct(item);
				if (proj > 0){ //TODO fp comparison
					count++;
				}
			}
			numPredictedPositives = count;
			
			count = 0;
			proj = 0.0;
			ArrayList<DVector> negativeClass = trainingData.getPositiveClass();
			numActualNegatives = negativeClass.size();
			for (int i = 0; i < numActualNegatives; i++){
				item = positiveClass.get(i);
				proj = getW().getDotProduct(item);
				if (proj > 0){ //TODO fp comparison
					count++;
				}
			}
			numPredictedNegatives = count;
			
			
			numTruePositives = numActualPositives;
			numTrueNegatives = numActualNegatives;
			numFalsePositives = numPredictedPositives;
			numFalseNegatives = numPredictedNegatives;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	public DVector getHyperplane() throws Exception{
		return getW().get2DAntiClockwiseNormal(); //TODO sure?
	}
	
	public ArrayList<SVMDataItem> getRCH1(){
		return rch1;
	}
	
	public ArrayList<SVMDataItem> getRCH2(){
		return rch2;
	}

	public DVector getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public DVector getNearestNegativePoint() {
		return nearestNegativePoint;
	}

}
