package app.model.data;

import java.util.ArrayList;
import org.jfree.data.xy.XYSeriesCollection;

public class SVMDataSet extends XYSeriesCollection{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5941570772540357547L;
	private ArrayList<SVMDataSeries> dataSet;
	
	public SVMDataSet(){	
		super();
		dataSet = new ArrayList<SVMDataSeries>();
	}
	
	public ArrayList<SVMDataSeries> getDataSet(){
		return dataSet;
	}
	
	public void setDataSet(ArrayList<SVMDataSeries> list){
		dataSet = list;
	}
	
	public void addSeries(SVMDataSeries series){
		dataSet.add(series);
	}
	
	public void removeSeries(SVMDataSeries series){
		dataSet.remove(series);
	}
	
}
