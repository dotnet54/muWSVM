package app.model.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.data.DData.DataChangeEvent;
import app.model.data.DData.DataChangeType;

public class SVMDataSet extends XYSeriesCollection {

	
	
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

	public SVMDataSet(int numDimensions){	
		super();
		
		dimensions = numDimensions;
		dimensionLabels = new String[dimensions];
		
		for (int i = 0; i< dimensions;i++){
			dimensionLabels[i] = "Dimension: " + i;
		}
	}
	
	@Override
	public SVMDataSeries getSeries(int series){
		return (SVMDataSeries) super.getSeries(series);
	}
	
	public void addSeries(SVMDataSeries series) throws Exception {
		if (series.getDimensions() == dimensions){
			super.addSeries(series);
		}else{
			throw new Exception("SVMDataSeries::add: Cannot add item, dimensions do not match");
		}
	}
	
	public void addItem(int seriesIndex, SVMDataItem item) throws Exception{
		//TODO check if dimensions match, add to right class
		getSeries(seriesIndex).add(item);
	}
	
	public void removeItem(int seriesIndex, int itemIndex){
		//TODO check if dimensions match, add to right class
		getSeries(seriesIndex).remove(itemIndex);
	}
	
	public void clearData(){
		for (int i = 0; i < getSeriesCount(); i++){
			getSeries(i).clear();
		}
	}
	
	public ArrayList<SVMDataItem> getPositiveClass(){
		return getSeries(getPositiveSeriesID()).toArrayList();
	}
	
	public ArrayList<SVMDataItem> getNegativeClass(){
		return getSeries(getNegativeSeriesID()).toArrayList();
	}
	

	public void loadFromFile(File fin) throws Exception{
		
		FileInputStream fis = new FileInputStream(fin);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		SVMDataItem item = null;
		
		clearData();
		
		while ((line = br.readLine()) != null) {
			
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");
			
			int className = Integer.parseInt(st.nextToken());
			st.nextToken();// skip attrib id
			double x = Double.parseDouble(st.nextToken());
			st.nextToken();// skip attrib id
			double y = Double.parseDouble(st.nextToken());
				
			item = new SVMDataItem(x, y, 1,className);
			System.out.println(item);
			if (className >= 0){	//TODO add class 0 for positive
				addItem(0, item);
			}else{
				addItem(1, item);
			}
			
		}
	 
		br.close();
	}
	
	public void saveToFile(File fout){
	       String text = "";
	        try {
	          File file = new File("example.txt");
	          BufferedWriter output = new BufferedWriter(new FileWriter(fout));
	          
	          SVMDataItem item = null;
	          //TODO hard coded dataclass, series index
	          for (int i = 0; i < getItemCount(0); i++){
	        	  item = getSeries(0).getDataItem(i);
	        	  text = "+1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          for (int i = 0; i < getItemCount(1); i++){
	        	  item = getSeries(1).getDataItem(i);
	        	  text =  "-1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          
	          output.close();
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
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
			getSeries(i).setYDimension(yDimension);
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
