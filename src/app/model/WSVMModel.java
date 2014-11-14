package app.model;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import app.model.data.WSVMDataItem;
import app.model.data.WSVMDataGenerator;
import app.model.data.WSVMDataSeries;
import app.model.data.WSVMDataSet;

/**
 * Represents an WSVM Model object, it communicates with solvers, and the dataset
 * @author shifaz
 *
 */
public class WSVMModel implements DatasetChangeListener,ISubject {
	
	private ArrayList<WSVMDataItem> dataset1 = new ArrayList<WSVMDataItem>();
	private ArrayList<WSVMDataItem> dataset2 = new ArrayList<WSVMDataItem>();	

	private ArrayList<WSVMDataItem> rch1 = new ArrayList<WSVMDataItem>();
	private ArrayList<WSVMDataItem> rch2 = new ArrayList<WSVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private WSVMDataItem w = new WSVMDataItem(2);
	private double b = 0;
	private WSVMDataItem nearestPositivePoint = null;
	private WSVMDataItem nearestNegativePoint = null;	
		
	private WSVMDataItem centroid1;
	private WSVMDataItem centroid2;

	private WSVMDataSet solutionDataSet = null;
	private WSVMDataSeries positiveSeries = null;
	private WSVMDataSeries negativeSeries = null;
	
	
	private WSVMDataSet trainingData = null;
	private WSVMDataSet testData = null;

	public int numTruePositives;
	public int numFalsePositives;
	public int numTrueNegatives;
	public int numFalseNegatives;
	
	
	private boolean isSVMSolved = false;

	int numActualNegatives;
	int numPredictedPositives;
	int numPredictedNegatives;

	//observer pattern 
    private List<IObserver> observers;
    private boolean changed;
    private final Object MUTEX= new Object();
	private boolean solutionChanged;
    
    public boolean isSolutionChanged() {
		return solutionChanged;
	}


	public void setSolutionChanged(boolean solutionChanged) {
		this.solutionChanged = solutionChanged;
	}


	/**
     * constructor for model
     * 
     */
	public WSVMModel(){
		try {
			
			this.observers = new ArrayList<IObserver>();
			initDataModel(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	private void initDataModel(int numDimensions) throws Exception{
		
		trainingData = WSVMDataGenerator.getDefaultSVMDataset(numDimensions);		
		testData = new WSVMDataSet(numDimensions);
		WSVMDataSeries series6 = new WSVMDataSeries("Unlabelled Data", numDimensions);
		testData.setPositiveSeriesID(0);
		testData.setNegativeSeriesID(0);

		testData.addSeries(series6);

		solutionDataSet = new WSVMDataSet(numDimensions);
		WSVMDataSeries series3 = new WSVMDataSeries("Positive WRCH", numDimensions);
		WSVMDataSeries series4 = new WSVMDataSeries("Negative WRCH", numDimensions);
		WSVMDataSeries series5 = new WSVMDataSeries("Centroids", numDimensions);
		solutionDataSet.addSeries(series3);
		solutionDataSet.addSeries(series4);
		solutionDataSet.addSeries(series5);
		
		
		solutionDataSet.addSeries(new WSVMDataSeries("Test Positive", trainingData.getDimensions()));
		solutionDataSet.addSeries(new WSVMDataSeries("Test Negative", trainingData.getDimensions()));
					
		
		//add change listeners
		trainingData.addChangeListener(this);
		testData.addChangeListener(this);
		solutionDataSet.addChangeListener(this);
		
		trainingData.notifyDataChange();
		testData.notifyDataChange();
		solutionDataSet.notifyDataChange();
		changed = true;
		notifyObservers();
	}
	

	
	public void generateRandomTrainingData(int numDims,
			int numPoints, int percentPos, String svmType, 
			double min, double max, double minW, double maxW){
		
		try {
			if (trainingData.getDimensions() != numDims){
				initDataModel(numDims);
			}

			WSVMDataGenerator dataGen = new WSVMDataGenerator(
					this, 3, trainingData.getDimensions(), 2);
			
			if (svmType.equals("Hard Margin")){
				dataGen.generateDataHardMargin(trainingData, 
						numPoints, percentPos, min, max, minW, maxW);
			}else{
				dataGen.generateDataSoftMargin(trainingData, 
						numPoints, percentPos, 0, min, max, minW, maxW);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void generateRandomTestData(int numDims,
			int numPoints, int percentPos, String svmType, 
			double min, double max, double minW, double maxW){
		WSVMDataGenerator dataGen = new WSVMDataGenerator(this, 3, numDims, 2);
		dataGen.generateDataSeries(testData, 0, numPoints, percentPos, 
				svmType, min, max, minW, maxW);
	}
	
	public void loadPredefinedDataset(String name){
		WSVMDataGenerator dataGen = new WSVMDataGenerator(this, 3, 2, 2);
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

		if (series == 0){
			rch1 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
		}else if (series == 1){
			rch2 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
		}else{
			rch1 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
			rch2 = WRCHSolver.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
		}
		

		centroid1 = WRCHSolver.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = WRCHSolver.findCentroid(dataset2);
		centroid2.setLabel("-");
		WSVMDataSeries s5= getSolutionDataSet().getSeries(2);
		s5.clear();
		s5.add(getCentroid1());
		s5.add(getCentroid2());
		
		
		WSVMDataSeries s3 = getSolutionDataSet().getSeries(0);
		
		
		s3.clear();
		for (int i = 0;rch1 != null && i < rch1.size(); i++){
			s3.add(rch1.get(i));
		}
		
		WSVMDataSeries s4= getSolutionDataSet().getSeries(1);
		s4.clear();
		for (int i = 0;rch2 != null &&  i < rch2.size(); i++){
			s4.add(rch2.get(i));
		}
	}
	
	public void solveSVM(){
		try {
			WSKSolver wskSolver = new WSKSolver();
			boolean result = wskSolver.wsk(trainingData, getMu1(), getMu2());

			b = wskSolver.getB();
			w = wskSolver.getW();
			nearestPositivePoint = wskSolver.getNearestPositivePoint();
			nearestNegativePoint = wskSolver.getNearestNegativePoint();

			if (!result){
				setSVMSolved(false);
				return;
			}else{
				setSVMSolved(true);
			}
			
			int count = 0;
			double proj = 0.0;
			WSVMDataItem item = null;
			
			WSVMDataSeries positiveClass = trainingData.getSeries(trainingData.getPositiveSeriesID());
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
			WSVMDataSeries negativeClass = 
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
	
	public int testPoint(WSVMDataItem point){
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
	
	public void clearDataSet(int series){
		trainingData.clearData();
		testData.clearData();
		solutionDataSet.getSeries(0).clear();
		solutionDataSet.getSeries(1).clear();
		solutionDataSet.getSeries(2).clear();
		solutionDataSet.getSeries(3).clear();
		solutionDataSet.getSeries(4).clear();
		
		if (series == 1 && dataset1 != null && rch1 != null){
			dataset1.clear();
			rch1.clear();
		}else if (series == 2 && dataset2 != null && rch2 != null){
			dataset2.clear();
			rch2.clear();
		}else{
			
			if (dataset1 != null) dataset1.clear();
			if (rch1 != null) rch1.clear();
			if (dataset2 != null) dataset2.clear();
			if (rch2 != null) rch2.clear();
		}
	}

	public WSVMDataItem getHyperplane() throws Exception{
		return getW().get2DAntiClockwiseNormal(); //2d normal only
	}
	
	public ArrayList<WSVMDataItem> getRCH1(){
		return rch1;
	}
	
	public ArrayList<WSVMDataItem> getRCH2(){
		return rch2;
	}

	public WSVMDataItem getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public WSVMDataItem getNearestNegativePoint() {
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
		//future work
	}
	
	
	@Override
	public void datasetChanged(DatasetChangeEvent event) {
		Dataset data = event.getDataset();
		
		if (data !=null && data instanceof WSVMDataSet){
			WSVMDataSet svmDataset = (WSVMDataSet) data;
			
			if (svmDataset == trainingData){
				//System.out.println("training data changed");
				//updateModel();
			}else if(svmDataset == testData){
				System.out.println("test data changed");
				
			}else if (svmDataset == solutionDataSet){
				//System.out.println("solution data changed");
			}
		}

		//future work
		//fireModelChangedEvent();
	}

	
	
	/*
	 * 
	 * Getters and setters
	 * 
	 */
	


	public WSVMDataSet getSolutionDataSet() {
		return solutionDataSet;
	}

	public void setSolutionDataSet(WSVMDataSet solutionDataSet) {
		this.solutionDataSet = solutionDataSet;
	}

	public void setChartDataset(WSVMDataSet chartData) {
		this.trainingData = chartData;
	}

	public WSVMDataSeries getPositiveSeries() {
		return positiveSeries;
	}

	public void setPositiveSeries(WSVMDataSeries positiveSeries) {
		this.positiveSeries = positiveSeries;
	}
	public WSVMDataSeries getNegativeSeries() {
		return negativeSeries;
	}

	public void setNegativeSeries(WSVMDataSeries negativeSeries) {
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

	public WSVMDataItem getW(){
		return w;	
	}

	public double getB(){
		return b;
	}
	
	public WSVMDataSet getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(WSVMDataSet trainingData) {
		this.trainingData = trainingData;
	}

	public WSVMDataSet getTestData() {
		return testData;
	}

	public void setTestData(WSVMDataSet testData) {
		this.testData = testData;
	}
	
	
	public boolean isSVMSolved() {
		return isSVMSolved;
	}

	public void setSVMSolved(boolean isSolved) {
		this.isSVMSolved = isSolved;
	}

	public WSVMDataItem getCentroid1() {
		return centroid1;
	}

	public WSVMDataItem getCentroid2() {
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
	
	
	
	/*
	 * future work, implement performance measuring as part of model
	 * 
	 * 
	 */
//	public int getNumTruePositives(){
//		return 0;
//	}
//	
//	public int getNumTrueNegatives(){
//		return 0;
//	}
//	
//	public int getNumFalsePositives(){
//		return 0;
//	}
//	
//	public int getNumFalseNegatives(){
//		return 0;
//	}
//	
//	public int getAccuracy(){
//		return 0;
//	}
//	
//	public int getPrecision(){
//		return 0;
//	}
//	
//	public int getSensitivity(){
//		return 0;
//	}
//	
//	public int getSpecificity(){
//		return 0;
//	}
//	
//	public int getNumPositiveSupportVectors(){
//		return 0;
//	}
//	
//	public int getNumNegativeSupportVectors(){
//		return 0;
//	}
//	
//	public long getTimeElapsedForWSK(){
//		return 0;
//	}
//	
//	public long getTimeElapsedForWRCH(){
//		return 0;
//	}
	
	
	
	
	
	
	
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
