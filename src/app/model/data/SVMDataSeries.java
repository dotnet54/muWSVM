package app.model.data;

import java.util.ArrayList;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

public class SVMDataSeries extends XYSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6695560134203970982L;

	private int dimensions;
	private int xDimension = 0;
	private int yDimension = 1;
	
	public SVMDataSeries(Comparable key, int numDimensins) {
		super(key, false, true);	
		this.dimensions = numDimensins;
	}
	
	
	//override these methods
	
	public void add(SVMDataItem item, boolean notify) throws Exception{
		if (item.getDimensions() == dimensions){
			super.add(item, notify);
		}else{
			throw new Exception("SVMDataSeries::add: Cannot add item, dimensions do not match");
		}
	}
	
	public SVMDataItem remove(int index){
		return (SVMDataItem) super.remove(index);
	}
	
	//TODO returns internal data NOTE -> notification issue
	public SVMDataItem getRawDataItem(int index) {
		return (SVMDataItem) this.data.get(index);
	}
	
//	public double getVal(int dim, int index){
//		return this.data.get(dim).getVal(index);
//	}
//	
//	public void setVal(int dim, int index){
//		return this.data.get(dim).setVal(index);
//	}
//	
	
	//getters and setters
	
	
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

	public int getDimensions() {
		return dimensions;
	}
	
	
	
	public ArrayList<SVMDataItem> toArrayList(){
		ArrayList<SVMDataItem> list = new ArrayList<SVMDataItem>();
		
		SVMDataItem item =  null;
		for (int i = 0; i < getItemCount(); i++){
			item = (SVMDataItem) getItems().get(i);
			list.add(item);
		}
		return list;
	}
	
	//TODO note jfreechart modified to make this public 
    public SVMDataItem getDataItem(int index) {
        return (SVMDataItem) this.data.get(index);
    }
}
