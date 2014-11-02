package app.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DData implements ISubject{

	private ArrayList<DVector> data = new ArrayList<DVector>();
	private int dimensions;
	private String[] attribNames;
	
	private int positiveClassID = +1;
	private int negativeClassID = -1;
	
	
	DataChangeEvent lastChange; 
	//observer pattern 
    private List<IObserver> observers;
    private boolean changed;
    private final Object MUTEX= new Object();
    
    public enum DataChangeType{
    	DataAdded,
    	DataRemoved,
    	DatasetCleared
    }
    
    public class DataChangeEvent{
    	
    	DataChangeType type;
    	DVector item;
    	int index;
    	
    	public DataChangeEvent(DataChangeType type, DVector item, int index){
    		this.type = type;
    		this.item = item;
    		this.index = index;
    	}
    }
	
	private int xDimension = 0;
	public int getXDimension() {
		return xDimension;
	}

	public void setXDimension(int xDimension) {
		this.xDimension = xDimension;
	}

	public int getYDimension() {
		return yDimension;
	}

	public void setYDimension(int yDimension) {
		this.yDimension = yDimension;
	}

	private int yDimension = 1;
	
	public double getX(int i) throws Exception{
		return data.get(i).getVal(getXDimension());
	}
	
	public double getY(int i) throws Exception{
		return data.get(i).getVal(getYDimension());
	}
	
	static Random rand = new Random();
	
	public DData(int numDimensions) {
		dimensions = numDimensions;
		attribNames = new String[dimensions];
		
		for (int i = 0; i< dimensions;i++){
			attribNames[i] = "Dimension: " + i;
		}
		
		this.observers = new ArrayList<IObserver>();
	}
	
	public void add(DVector vec) throws Exception{
		if (vec.getDimensions() != dimensions){
			throw new Exception("DData::add: Dimensionality of given the vector does not match dimensions of dataset");
		}
		
		data.add(vec);
		lastChange = new DataChangeEvent(DataChangeType.DataAdded, vec, data.size() -1);
		changed = true;
		notifyObservers();
	}
	
	public void remove(int index){
		lastChange = new DataChangeEvent(DataChangeType.DataRemoved, data.get(index), index);
		data.remove(index);
		
		changed = true;
		notifyObservers();
	}
	
	public DVector get(int index){
		return data.get(index);
	}
	
	public void clear(){
		data.clear();
		lastChange = new DataChangeEvent(DataChangeType.DatasetCleared, null, -1);
		changed = true;
		notifyObservers();
	}
	
	public int size(){
		return data.size();
	}
	
	public int getDimensions(){
		return dimensions;
	}
	
	public String[] getAttributeNames(){
		return attribNames;
	}
	
	public int getClassCount(){
		//TODO
		return 0;
	}
	
	public int[] getClassIDs(){
		//TODO
		return null;
	}
	
	public void setPositiveClass(int classID){
		positiveClassID = classID;
	}
	
	public void setNegativeClass(int classID){
		negativeClassID = classID;
	}
	
	public ArrayList<DVector> getPositiveClass(){
		int size = data.size();
		ArrayList<DVector> list = new ArrayList<DVector>();

		for (int i = 0; i < size; i++){
			if (data.get(i).getClassID() == positiveClassID){
				list.add(data.get(i));
			}
		}
		return list;
	}
	
	public ArrayList<DVector> getNegativeClass(){
		int size = data.size();
		ArrayList<DVector> list = new ArrayList<DVector>();

		for (int i = 0; i < size; i++){
			if (data.get(i).getClassID() == negativeClassID){
				list.add(data.get(i));
			}
		}
		return list;
	}
	
	public ArrayList<DVector> getClassAsList(int classID){
		int size = data.size();
		ArrayList<DVector> list = new ArrayList<DVector>();

		for (int i = 0; i < size; i++){
			if (data.get(i).getClassID() == classID){
				list.add(data.get(i));
			}
		}
		return list;
	}
	
	
	public SVMDataSet getChartData(int xDim, int yDim){
		SVMDataSet dataset = new SVMDataSet(2);
		try{
			dataset.addSeries(new SVMDataSeries("Positive Class", dataset.getDimensions()));
			dataset.addSeries(new SVMDataSeries("Negative Class", dataset.getDimensions()));
			DVector vec = null;
		
		
			ArrayList<DVector> dataClass = getPositiveClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(0).add(
						new SVMDataItem(vec.getVal(xDim), vec.getVal(yDim), vec.getWeight()));
			}
			dataClass = getNegativeClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(1).add(
						new SVMDataItem(vec.getVal(xDim), vec.getVal(yDim), vec.getWeight()));
			}
			
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		
		return dataset;
	}
	
	
	
	public SVMDataSet getChartData(){
		SVMDataSet dataset = new SVMDataSet(2);
		try{
			dataset.addSeries(new SVMDataSeries("Positive Class", dataset.getDimensions()));
			dataset.addSeries(new SVMDataSeries("Negative Class", dataset.getDimensions()));

			ArrayList<DVector> dataClass = getPositiveClass();

			DVector vec = null;
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(0).add(new SVMDataItem(vec.getXValue(), vec.getYValue(), vec.getWeight()));
			}
			
			dataClass = getNegativeClass();
			
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(1).add(new SVMDataItem(vec.getXValue(), vec.getYValue(), vec.getWeight()));
			}
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}

		return dataset;
	}
	
	
	public void generateRandomData(int numItems){
		clear();
		
		double[] values = new double[dimensions];
		
		
		try {
			
			int count = numItems / 2;
			
			for (int i = 0; i < count; i++){
				for (int j = 0; j < dimensions; j++){
					values[j] = randDouble(0, 0.5);
				}
				
				add(new DVector(values, 1, +1));
			}
			
			for (int i = 0; i < (numItems - count); i++){
				for (int j = 0; j < dimensions; j++){
					values[j] = randDouble(0.5, 1);
				}
				
				add(new DVector(values, 1, -1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static double getGaussian(double aMean, double aVariance) {
		return aMean + rand.nextGaussian() * aVariance;
	}
	
	public static double randDouble(double min, double max) {
		double randomNum = min + (max - min) * rand.nextDouble();
	    return randomNum;
	}
	
	public static int randInt(int min, int max) {
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	
	
	
	public DataChangeEvent getLastChange(){
		return lastChange;
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
