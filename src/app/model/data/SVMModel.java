package app.model.data;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;

import app.model.algorithms.WRCH;

public class SVMModel implements DatasetChangeListener,ISubject {
	
	private ArrayList<DVector> dataset1 = new ArrayList<DVector>();
	private ArrayList<DVector> dataset2 = new ArrayList<DVector>();	

	private ArrayList<DVector> rch1 = new ArrayList<DVector>();
	private ArrayList<DVector> rch2 = new ArrayList<DVector>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private DVector w = new DVector(2);
	private double b = 0;
	private DVector nearestPositivePoint = null;
	private DVector nearestNegativePoint = null;	
		
	private DVector centroid1;
	private DVector centroid2;

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
	
	
	
	
	public DVector getCentroid1() {
		return centroid1;
	}

	public DVector getCentroid2() {
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

	public DVector getW(){
		return w;	//reference or value TODO
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
	
	
	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		try {
			
			this.observers = new ArrayList<IObserver>();
		
			int numDimensions = 2;
			
			trainingData = SVMDataGenerator.getDefaultSVMDataset();		
			testData = SVMDataGenerator.getDefaultSVMDataset();

			solutionDataSet = new SVMDataSet(numDimensions);
			SVMDataSeries series3 = new SVMDataSeries("Positive WRCH", numDimensions);
			SVMDataSeries series4 = new SVMDataSeries("Negative WRCH", numDimensions);
			SVMDataSeries series5 = new SVMDataSeries("Centroids", numDimensions);
			solutionDataSet.addSeries(series3);
			solutionDataSet.addSeries(series4);
			solutionDataSet.addSeries(series5);
			
			//add change listeners
			trainingData.addChangeListener(this);
			testData.addChangeListener(this);
			solutionDataSet.addChangeListener(this);
			
		} catch (Exception e) {
			// TODO
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
	
	public void generateRandomData(int numPoints, int percentPos, int softnessDelta){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(trainingData, numPoints, percentPos, softnessDelta);
	}
	
	public void loadPredefinedDataset(String name){
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.setPredefinedDataset(trainingData, name);
	}
	
	private void initializeData(){

		
		
		
		
		
		
		//trainingData.generateRandomData(10);
		SVMDataGenerator dataGen = new SVMDataGenerator(this, 3, 2);
		dataGen.generateData(trainingData, 10, 50, 0);
		//dataGen.setPredefinedDataset(rawDataSet, "Triangle 1");

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
			rch1 = Dwrch.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
		}else if (series == 1){
			rch2 = Dwrch.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
		}else{
			rch1 = Dwrch.calcWeightedReducedCHull2(trainingData.getSeries(0), mu1);
			rch2 = Dwrch.calcWeightedReducedCHull2(trainingData.getSeries(1), mu2);
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

		centroid1 = Dwrch.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = Dwrch.findCentroid(dataset2);
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
			
			SVMDataSeries positiveClass = trainingData.getSeries(trainingData.getPositiveSeriesID());
			numActualPositives = positiveClass.getItemCount();
			for (int i = 0; i < numActualPositives; i++){
				item = positiveClass.getDataItem(i);
				proj = getW().getDotProduct(item);
				if (proj > 0){ //TODO fp comparison
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
				proj = getW().getDotProduct(item);
				if (proj < 0){ //TODO fp comparison
					count++;
				}
			}
			numPredictedNegatives = count;
			
			
			numTruePositives = numActualPositives;
			numTrueNegatives = numActualNegatives;
			numFalsePositives = numActualPositives - numPredictedPositives ;
			numFalseNegatives = numActualNegatives - numPredictedNegatives;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	public DVector getHyperplane() throws Exception{
		return getW().get2DAntiClockwiseNormal(); //TODO sure?
	}
	
	public ArrayList<DVector> getRCH1(){
		return rch1;
	}
	
	public ArrayList<DVector> getRCH2(){
		return rch2;
	}

	public DVector getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public DVector getNearestNegativePoint() {
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
			// TODO
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
			
			//TODO note not using equals function, only testing for reference equality
			if (svmDataset == trainingData){
				System.out.println("training data changed");
				//updateModel();
			}else if(svmDataset == testData){
				System.out.println("test data changed");
				
			}else if (svmDataset == solutionDataSet){
				System.out.println("solution data changed");
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
