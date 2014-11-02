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
		super(key);	
		this.dimensions = numDimensins;
	}
	
	
	//override these methods
	
	public void add(DVector item) throws Exception{
		if (item.getDimensions() == dimensions){
			super.add(item);
		}else{
			throw new Exception("SVMDataSeries::add: Cannot add item, dimensions do not match");
		}
	}
	
	public DVector remove(int index){
		return (DVector) super.remove(index);
	}
	
	//TODO returns internal data NOTE -> notification issue
	public DVector getRawDataItem(int index) {
		return (DVector) this.data.get(index);
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
	
	
	
	public ArrayList<DVector> toArrayList(){
		ArrayList<DVector> list = new ArrayList<DVector>();
		
		DVector item =  null;
		for (int i = 0; i < getItemCount(); i++){
			item = (DVector) getItems().get(i);
			list.add(item);
		}
		return list;
	}
	
	//TODO note jfreechart modified to make this public 
    public DVector getDataItem(int index) {
        return (DVector) this.data.get(index);
    }
}
