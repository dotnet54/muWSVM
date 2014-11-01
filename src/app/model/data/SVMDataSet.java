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
import java.util.StringTokenizer;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeriesCollection;

import app.model.data.DData.DataChangeEvent;
import app.model.data.DData.DataChangeType;

public class SVMDataSet extends XYSeriesCollection implements IObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5941570772540357547L;

	DData datasource = null;

	public SVMDataSet(){	
		super();
	}
	
	@Override
	public SVMDataSeries getSeries(int series){
		return (SVMDataSeries) super.getSeries(series);
	}
	
	public void addItem(int seriesIndex, SVMDataItem item){
		getSeries(seriesIndex).add(item);
	}
	
	public void removeItem(int seriesIndex, int itemIndex){
		getSeries(seriesIndex).remove(itemIndex);
	}
	
	public void clear(){
		getSeries(0).clear();	//TODO 0 and 1 must exist
		getSeries(1).clear();
	}

	public void loadFromFile(File fin) throws IOException{
		
		FileInputStream fis = new FileInputStream(fin);
		 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		SVMDataItem item = null;
		
		clear();
		
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
	        	  item = getSeries(0).getRawDataItem(i);
	        	  text = "+1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          for (int i = 0; i < getItemCount(1); i++){
	        	  item = getSeries(1).getRawDataItem(i);
	        	  text =  "-1  1:" 
	        	  		+ item.getXValue() + " 2:" + item.getYValue();
	        	  output.write(text + "\n");
	          }
	          
	          output.close();
	        } catch ( IOException e ) {
	           e.printStackTrace();
	        }
	}
	
	public void attach(DData datasource){
		this.datasource = datasource;
		datasource.register(this);
	}

	@Override
	public void update() {
		// TODO 
		try {
			DataChangeEvent event = datasource.getLastChange();
			DVector item = event.item;
			if (event.type == DataChangeType.DataAdded){
				SVMDataItem newItem = new SVMDataItem(item.getXValue(), item.getYValue(), 
						item.getWeight(), item.getClassID());
				
				if (item.getClassID() == +1){ //TODO if positive class changed??
					addItem(0,  newItem);
				}else if(item.getClassID() == -1){
					addItem(1,  newItem);
				}
			}else if (event.type == DataChangeType.DataRemoved){
				if (item.getClassID() == +1){ //TODO if positive class changed??
					removeItem(0,  event.index); //TODO index missmatch
				}else if(item.getClassID() == -1){
					removeItem(1,  event.index);
				}
			}else if (event.type == DataChangeType.DatasetCleared){
				clear();
			}
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
	
}
