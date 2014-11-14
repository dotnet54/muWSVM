package app.model.data;

import java.util.ArrayList;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

public class WSVMDataSeries extends XYSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6695560134203970982L;

	private int dimensions;
	private int xDimension = 0;
	private int yDimension = 1;
	
	public WSVMDataSeries(Comparable key, int numDimensins) {
		super(key, false, true);	
		this.dimensions = numDimensins;
	}

	public void add(WSVMDataItem item, boolean notify) throws Exception{
		if (item.getDimensions() == dimensions){
			super.add(item, notify);
		}else{
			throw new Exception("SVMDataSeries::add: Cannot add item, dimensions do not match");
		}
	}
	
	public WSVMDataItem remove(int index){
		return (WSVMDataItem) super.remove(index);
	}
	
	//returns internal data structure to public
	//this does not override the inherited method from XYSeries since it is private to a different package
	public WSVMDataItem getRawDataItem(int index) {
		return (WSVMDataItem) this.data.get(index);
	}

	//returns internal data structure to public - same as above for convenience
    public WSVMDataItem getDataItem(int index) {
        return (WSVMDataItem) this.data.get(index);
    }
	
	
	public ArrayList<WSVMDataItem> toArrayList(){
		ArrayList<WSVMDataItem> list = new ArrayList<WSVMDataItem>();
		
		WSVMDataItem item =  null;
		for (int i = 0; i < getItemCount(); i++){
			item = (WSVMDataItem) getItems().get(i);
			list.add(item);
		}
		return list;
	}
	
	
	//
	//getters and setters
	//
	
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

}
