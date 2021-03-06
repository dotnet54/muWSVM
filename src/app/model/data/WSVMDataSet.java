package app.model.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.jfree.data.xy.XYSeriesCollection;


public class WSVMDataSet extends XYSeriesCollection {

	
	
	private int dimensions;
	private String[] dimensionLabels;

	private int positiveSeriesID = 0;
	private int negativeSeriesID = 1;
	private int xDimension = 0;
	private int yDimension = 1;
	
	static Random rand = new Random();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5941570772540357547L;

	public WSVMDataSet(int numDimensions){	
		super();
		
		dimensions = numDimensions;
		dimensionLabels = new String[dimensions];
		
		for (int i = 0; i< dimensions;i++){
			dimensionLabels[i] = "Dimension: " + i;
		}
	}
	
	@Override
	public WSVMDataSeries getSeries(int series){
		return (WSVMDataSeries) super.getSeries(series);
	}
	
	public void addSeries(WSVMDataSeries series) throws Exception {
		if (series.getDimensions() == dimensions){
			super.addSeries(series);
		}else{
			throw new Exception("WSVMDataSet::addSeries: Cannot add item, dimensions do not match");
		}
	}
	
	public void addItem(int seriesIndex, WSVMDataItem item) throws Exception{
		if (item.getDimensions() != getDimensions()){
			throw new Exception("WSVMDataSet::addItem: dimension of dataset much match dimension of item");
		}
		getSeries(seriesIndex).add(item);
	}
	
	public void addItem(int seriesIndex, WSVMDataItem item, boolean notify) throws Exception{
		if (item.getDimensions() != getDimensions()){
			throw new Exception("WSVMDataSet::addItem: dimension of dataset much match dimension of item");
		}
		getSeries(seriesIndex).add(item, notify);
	}
	
	public void removeItem(int seriesIndex, int itemIndex){
		getSeries(seriesIndex).remove(itemIndex);
	}
	
	public void clearData(){
		for (int i = 0; i < getSeriesCount(); i++){
			getSeries(i).clear();
		}
	}
	
	public double getWeight(int series, int index){
		return getSeries(series).getRawDataItem(index).getWeight();
	}
	
	public void setWeight(int series, int index, double newWeight){
		getSeries(series).getRawDataItem(index).setWeight(newWeight);
		fireDatasetChanged();
	}

	public double getXValue(int series, int item) {
		try {
			return getSeries(series).getRawDataItem(item).getVal(getXDimension());
		} catch (Exception e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}

	public double getYValue(int series, int item) {
		try {
			return getSeries(series).getRawDataItem(item).getVal(getYDimension());
		} catch (Exception e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}
	
	
	
	public ArrayList<WSVMDataItem> getPositiveClass(){
		return getSeries(getPositiveSeriesID()).toArrayList();
	}
	
	public ArrayList<WSVMDataItem> getNegativeClass(){
		return getSeries(getNegativeSeriesID()).toArrayList();
	}
	
	
	public void loadFromFile(String fname, String format){
		File fin = new File(fname);
		
		if (format.equals("csv")){
			
		}else if (format.equals("libsvm") || format.equals("dat")){
			loadFromLIBSVMFile(fin);
		}else {
			loadFromLIBSVMFile(fin);
		}
		
	}

	public void loadFromLIBSVMFile(File fin) {
		
		try {
			FileInputStream fis = new FileInputStream(fin);
			 
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
 
			String line = null;
			WSVMDataItem item = null;
			
			clearData();
			
			
			
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");
				
				int className = (int) Double.parseDouble(st.nextToken());
				st.nextToken();// skip attrib id
				double x = Double.parseDouble(st.nextToken());
				st.nextToken();// skip attrib id
				double y = Double.parseDouble(st.nextToken());
					
				item = new WSVMDataItem(x, y, 1,className);
				if (className > 0){	
					addItem(getPositiveSeriesID(), item, false);
				}else{
					addItem(getNegativeSeriesID(), item, false);
				}
			}
			
			fireDatasetChanged();
 
			br.close();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
				    "Error reading from file, make sure it is formatted correctly",
				    "File IO error",
				    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "File IO error",
				    JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "File IO error",
				    JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
				    e.getMessage(),
				    "File IO error",
				    JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		}
	}
	
	public void saveToFile(String fname, String format){
		File fout = new File(fname);
		
		if (format.equals("csv")){
			//to be implemented
		}else if (format.equals("libsvm") || format.equals("dat")){
			saveToLIBSVMFile(fout);
		}else {
			saveToLIBSVMFile(fout);
		}
		
	}
	
	public void notifyDataChange(){
		fireDatasetChanged();
	}
	
	public void saveToLIBSVMFile(File fout){
	       String text = "";
	        try {
	          BufferedWriter output = new BufferedWriter(new FileWriter(fout));
	          
	          WSVMDataItem item = null;
	          //hard coded dataclass
	          for (int i = 0; i < getItemCount(0); i++){
	        	  item = getSeries(getPositiveSeriesID()).getDataItem(i);
	        	  text = "+1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          for (int i = 0; i < getItemCount(1); i++){
	        	  item = getSeries(getNegativeSeriesID()).getDataItem(i);
	        	  text =  "-1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          
	          output.close();
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
	}
	
	public void resetWeights(){
		for (int j = 0; j <getSeriesCount(); j++){
			for (int i = 0; i < getSeries(j).getItemCount(); i++){
				setWeight(j, i, 1);
			}			
		}
		fireDatasetChanged();
	}
	
	public void reduceWeightofOldData(int percentage){
		double newWeight = 1;
		
		int classID;
		
		for (int j = 0; j <getSeriesCount(); j++){
			classID = j;
			for (int i = 0; i < getSeries(classID).getItemCount(); i++){
				newWeight = getWeight(classID, i);
				
				newWeight = newWeight - (percentage / 100.0 * newWeight);
				
				if (newWeight < 0.01){
					newWeight = 0.01;
				}
				
				setWeight(classID, i, newWeight);
			}			
		}
		fireDatasetChanged();
	}
	
	
	
	
	public WSVMDataSet getChartData(int xDim, int yDim){
		WSVMDataSet dataset = new WSVMDataSet(getDimensions());
		try{
			dataset.addSeries(new WSVMDataSeries("Positive Class", dataset.getDimensions()));
			dataset.addSeries(new WSVMDataSeries("Negative Class", dataset.getDimensions()));
			WSVMDataItem vec = null;
		
		
			ArrayList<WSVMDataItem> dataClass = getPositiveClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(0).add(
						new WSVMDataItem(vec.getVal(xDim), vec.getVal(yDim), vec.getWeight()));
			}
			dataClass = getNegativeClass();
			for (int i= 0; i < dataClass.size(); i++){
				vec = dataClass.get(i);
				dataset.getSeries(1).add(
						new WSVMDataItem(vec.getVal(xDim), vec.getVal(yDim), vec.getWeight()));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataset;
	}
	
	
	
	
	
	
	
	
	
	
	/*
	 * Getters and Setters
	 */
	
	public int getPositiveSeriesID() {
		return positiveSeriesID;
	}

	public void setPositiveSeriesID(int positiveSeriesID) {
		this.positiveSeriesID = positiveSeriesID;
	}

	public int getNegativeSeriesID() {
		return negativeSeriesID;
	}

	public void setNegativeSeriesID(int negativeSeriesID) {
		this.negativeSeriesID = negativeSeriesID;
	}

	public int getXDimension() {
		return xDimension;
	}

	public void setXDimension(int xDimension) {
		this.xDimension = xDimension;
		for (int i = 0; i < getSeriesCount(); i++){
			getSeries(i).setXDimension(xDimension);
		}
		
		fireDatasetChanged();
	}

	public int getYDimension() {
		return yDimension;
	}

	public void setYDimension(int yDimension) {
		this.yDimension = yDimension;
		for (int i = 0; i < getSeriesCount(); i++){
			getSeries(i).setYDimension(yDimension);
		}
		
		fireDatasetChanged();
	}

	public int getDimensions() {
		return dimensions;
	}
	

	public String[] getDimensionLabels() {
		return dimensionLabels;
	}

	public void setDimensionLabels(String[] dimensionLabels) {
		this.dimensionLabels = dimensionLabels;
	}
	
}
