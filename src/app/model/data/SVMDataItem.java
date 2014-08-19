package app.model.data;

import java.awt.Point;

import org.jfree.data.xy.XYDataItem;


public class SVMDataItem extends XYDataItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int DataClass = 1;
	private double weight = 1;//defaults
	
	
	public SVMDataItem(double x, double y) {
		super(x, y);
		setWeight(1);
	}
	
	
	public Double getX(){
		return super.getXValue();
	}
	public void setX(double x){
		 super.setX(x);
	}
	public Double getY(){
		return super.getYValue();
	}
	public void setY(double y){
		super.setY(y);
	}
	
	public double getDotProduct(SVMDataItem p2){
		return getXValue() * p2.getXValue() + 
			getYValue() * p2.getYValue();
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
	
	public boolean equals(Object obj){
		SVMDataItem d= (SVMDataItem) obj;
		if (this.getXValue() == d.getXValue() 
				&& this.getYValue() == d.getYValue()){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		return "[" + getXValue() + "," 
		+ getYValue() + "]";//  : " + getDataClass() + ": " + getWeight();
	}
//	toString
}
