package app.model.data;

import java.awt.Point;

import org.jfree.data.xy.XYDataItem;

public class SVMDataItem extends XYDataItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int DataClass;
	private double weight;
	
	
	public SVMDataItem(double x, double y) {
		super(x, y);
		
	}

	public double getWeight(){
		return weight;
	}
	
	public void setWeight(double w){
		weight = w;
	}
	
	public int getDataClass(){
		return DataClass;
	}
	
	public void setDataClass(int newClass){
		DataClass = newClass;
	}
	
	public Point toPoint(){
		return new Point((int)getXValue(), (int)getYValue());
	}
	
	public static SVMDataItem toDataItem(Point p){
		return new SVMDataItem(p.getX(), p.getY());
	}
	
}
