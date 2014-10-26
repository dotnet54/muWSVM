package app.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DData {

	private ArrayList<DVector> data = new ArrayList<DVector>();
	private int dimensions;
	
	private int positiveClassID = +1;
	private int negativeClassID = -1;
	
	static Random rand = new Random();
	
	public DData(int numDimensions) {
		dimensions = numDimensions;
	}
	
	public void add(DVector vec) throws Exception{
		if (vec.getDimensions() != dimensions){
			throw new Exception("DData::add: Dimensionality of given the vector does not match dimensions of dataset");
		}
		
		data.add(vec);
		//TODO notify observers
	}
	
	public void remove(int index){
		data.remove(index);
		//TODO notify observers
	}
	
	public DVector get(int index){
		return data.get(index);
	}
	
	public void clear(){
		data.clear();
		//TODO notify observers
	}
	
	public int getDimensions(){
		return dimensions;
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
	
	
	public XYSeriesCollection getChartData(int xDim, int yDim){
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(new XYSeries("Positive Class"));
		dataset.addSeries(new XYSeries("Negative Class"));
		DVector vec = null;
		
		try{
			ArrayList<DVector> dataClass = getPositiveClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(0).add(new XYDataItem(vec.getVal(xDim), vec.getVal(yDim)));
			}
			dataClass = getNegativeClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(1).add(new XYDataItem(vec.getVal(xDim), vec.getVal(yDim)));
			}
			
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		
		return dataset;
	}
	
	
	
	public XYSeriesCollection getChartData(){
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(new XYSeries("Positive Class"));
		dataset.addSeries(new XYSeries("Negative Class"));
		
		
		
		try {
			ArrayList<DVector> dataClass = getPositiveClass();

			DVector vec = null;
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(0).add(new XYDataItem(vec.getX(), vec.getY()));
			}
			
			dataClass = getNegativeClass();
			
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(1).add(new XYDataItem(vec.getX(), vec.getY()));
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
}
