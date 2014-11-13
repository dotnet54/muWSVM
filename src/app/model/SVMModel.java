package app.model;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import app.model.data.SVMDataItem;
import app.model.data.SVMDataGenerator;
import app.model.data.SVMDataSeries;
import app.model.data.SVMDataSet;
import app.test.WRCH_old;

public class SVMModel implements DatasetChangeListener,ISubject {
	
	private ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	

	private ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private SVMDataItem w = new SVMDataItem(2);
	private double b = 0;
	private SVMDataItem nearestPositivePoint = null;
	private SVMDataItem nearestNegativePoint = null;	
		
	private SVMDataItem centroid1;
	private SVMDataItem centroid2;

	private SVMDataSet solutionDataSet = null;
	private SVMDataSeries positiveSeries = null;
	private SVMDataSeries negativeSeries = null;
	
	
	private SVMDataSet trainingData = null;
	public SVMDataSet getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(SVMDataSet trainingData) {
		this.trainingData = trainingData;
	}

	public SVMDataSet getTestData() {
		return testData;
	}

	public void setTestData(SVMDataSet testData) {
		this.testData = testData;
	}

	private SVMDataSet testData = null;

	
	
	public int numTruePositives;
	public int numFalsePositives;
	public int numTrueNegatives;
	public int numFalseNegatives;
	
	
	private boolean isSVMSolved = false;
	
	
	public boolean isSVMSolved() {
		return isSVMSolved;
	}

	public void setSVMSolved(boolean isSolved) {
		this.isSVMSolved = isSolved;
	}

	public SVMDataItem getCentroid1() {
		return centroid1;
	}

	public SVMDataItem getCentroid2() {
		return centroid2;
	}

	int numActualPositives;
	public int getNumActualPositives() {
		numActualPositives = trainingData.getItemCount(0);
		return numActualPositives;
	}

	public int getNumActualNegatives() {
		numActualNegatives = trainingData.getItemCount(1);
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
		return trainingData;
	}

	public void setChartDataset(SVMDataSet chartData) {
		this.trainingData = chartData;
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

	public SVMDataItem getW(){
		return w;	
	}

	public double getB(){
		return b;
	}

	public void clearDataSet(int series){
		trainingData.clearData();
		testData.clearData();
		solutionDataSet.getSeries(0).clear();
		solutionDataSet.getSeries(1).clear();
		solutionDataSet.getSeries(2).clear();
		solutionDataSet.getSeries(3).clear();
		solutionDataSet.getSeries(4).clear();
		
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

	//observer pattern 
    private List<IObserver> observers;
    private boolean changed;
    private final Object MUTEX= new Object();
	
	public SVMModel(){
		try {
			
			this.observers = new ArrayList<IObserver>();
		
			int numDimensions = 2;
			
			trainingData = SVMDataGenerator.getDefaultSVMDataset();		
			testData = new SVMDataSet(numDimensions);
			SVMDataSeries series6 = new SVMDataSeries("Unlabelled Data", numDimensions);
			testData.setPositiveSeriesID(0);
			testData.setNegativeSeriesID(0);

			testData.addSeries(series6);

			solutionDataSet = new SVMDataSet(numDimensions);
			SVMDataSeries series3 = new SVMDataSeries("Positive WRCH", numDimensions);
			SVMDataSeries series4 = new SVMDataSeries("Negative WRCH", numDimensions);
			SVMDataSeries series5 = new SVMDataSeries("Centroids", numDimensions);
			solutionDataSet.addSeries(series3);
			solutionDataSet.addSeries(series4);
			solutionDataSet.addSeries(series5);
			
			
			solutionDataSet.addSeries(new SVMDataSeries("Test Positive", trainingData.getDimensions()));
			solutionDataSet.addSeries(new SVMDataSeries("Test Negative", trainingData.getDimensions()));
						
			
			//add change listeners
			trainingData.addChangeListener(this);
			testData.addChangeListener(this);
			solutionDataSet.addChangeListener(this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

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
	
	public void generateRandomTrainingData(int numDims,
			int numPoints, int percentPos, String svmType, 
			double min, double max, double minW, double maxW){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2, 2);
		
		if (svmType.equals("Hard Margin")){
			dataGen.generateDataHardMargin(trainingData, 
					numPoints, percentPos, min, max, minW, maxW);
		}else{
			dataGen.generateDataSoftMargin(trainingData, 
					numPoints, percentPos, 0, min, max, minW, maxW);
		}
		
		
	}
	
	public void generateRandomTestData(int numDims,
			int numPoints, int percentPos, String svmType, 
			double min, double max, double minW, double maxW){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2, 2);
		dataGen.generateDataSeries(testData, 0, numPoints, percentPos, 
				svmType, min, max, minW, maxW);
	}
	
	public void loadPredefinedDataset(String name){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2, 2);
		dataGen.setPredefinedDataset(trainingData, name);
	}
	
	public void solveWRCH(int series) throws Exception{
		dataset1 = trainingData.getSeries(0).toArrayList();
		dataset2 = trainingData.getSeries(1).toArrayList();
		
		if (dataset1 == null || dataset1.size() == 0){
			return;
		}
		if (dataset2 == null || dataset2.size() == 0){
			return;
		}
		
//		if (series == 0){
//			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
//		}else if (series == 1){
//			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
//		}else{
//			rch1 = WRCH.calcWeightedReducedCHull(dataset1, mu1);
//			rch2 = WRCH.calcWeightedReducedCHull(dataset2, mu2);
//		}
		
		
		if (series == 0){
			rch1 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
		}else if (series == 1){
			rch2 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
		}else{
			rch1 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
			rch2 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
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

		centroid1 = WRCHSolver.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = WRCHSolver.findCentroid(dataset2);
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
			WSKSolver wskSolver = new WSKSolver();
			wskSolver.wsk(trainingData, getMu1(), getMu2());
			
			b = wskSolver.getB();
			w = wskSolver.getW();
			nearestPositivePoint = wskSolver.getNearestPositivePoint();
			nearestNegativePoint = wskSolver.getNearestNegativePoint();
			
			if (!w.isZeroOrValid()){
				setSVMSolved(false);
				return;
			}else{
				setSVMSolved(true);
			}
			
			
			int count = 0;
			double proj = 0.0;
			SVMDataItem item = null;
			
			SVMDataSeries positiveClass = trainingData.getSeries(trainingData.getPositiveSeriesID());
			numActualPositives = positiveClass.getItemCount();
			for (int i = 0; i < numActualPositives; i++){
				item = positiveClass.getDataItem(i);
				proj = getW().getDotProduct(item) - getB();
				if (proj > 0){ //fp comparison
					count++;
				}
			}
			numPredictedPositives = count;
			
			count = 0;
			proj = 0.0;
			SVMDataSeries negativeClass = 
				trainingData.getSeries(trainingData.getNegativeSeriesID());
			numActualNegatives = negativeClass.getItemCount();
			for (int i = 0; i < numActualNegatives; i++){
				item = negativeClass.getDataItem(i);
				proj = getW().getDotProduct(item)- getB();
				if (proj < 0){ //fp comparison
					count++;
				}
			}
			numPredictedNegatives = count;
			
			
			numTruePositives = numActualPositives;
			numTrueNegatives = numActualNegatives;
			numFalsePositives = numActualPositives - numPredictedPositives ;
			numFalseNegatives = numActualNegatives - numPredictedNegatives;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void classifyTestData(){
		try {
			for (int i = 0; i < getTestData().getItemCount(0); i++){
				if (testPoint(testData.getSeries(0).getDataItem(i)) == 1){
					//add to positive class
					
					solutionDataSet.addItem(3, testData.getSeries(0).getDataItem(i));
				}else{
					//add to negative class
					solutionDataSet.addItem(4, testData.getSeries(0).getDataItem(i));
				}
			}
			
			testData.clearData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int testPoint(SVMDataItem point){
		try {
			double proj = point.getDotProduct(getW()) - getB();
			if (proj > 0){
				return +1;
			}else{
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public SVMDataItem getHyperplane() throws Exception{
		return getW().get2DAntiClockwiseNormal(); //TODO 2d only
	}
	
	public ArrayList<SVMDataItem> getRCH1(){
		return rch1;
	}
	
	public ArrayList<SVMDataItem> getRCH2(){
		return rch2;
	}

	public SVMDataItem getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public SVMDataItem getNearestNegativePoint() {
		return nearestNegativePoint;
	}

	
	private boolean autoUpdateModel = true;
	
	public boolean isAutoUpdateModel() {
		return autoUpdateModel;
	}

	public void setAutoUpdateModel(boolean autoUpdateModel) {
		this.autoUpdateModel = autoUpdateModel;
	}

	public void updateModel(){
		try {
			if (isAutoUpdateModel()){
				//solveWRCH(2);
				//solveSVM();
				
				changed = true;
				notifyObservers();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fireModelChangedEvent(){
		
	}
	
	
	@Override
	public void datasetChanged(DatasetChangeEvent event) {
		Dataset data = event.getDataset();
		
		if (data !=null && data instanceof SVMDataSet){
			SVMDataSet svmDataset = (SVMDataSet) data;
			
			if (svmDataset == trainingData){
				//System.out.println("training data changed");
				//updateModel();
			}else if(svmDataset == testData){
				System.out.println("test data changed");
				
			}else if (svmDataset == solutionDataSet){
				//System.out.println("solution data changed");
			}
		}

		
		//fireModelChangedEvent();
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
