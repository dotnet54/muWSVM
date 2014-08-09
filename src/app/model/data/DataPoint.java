package app.model.data;

import java.awt.Point;

public class DataPoint extends Point{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int id;
	public double x;
	public double y;
	public int pclass;
	
	public DataPoint(double x, double y){
		this.x = x;
		this.y = y;
		id++;
		if (Math.random() < 0.5){
			pclass = +1;
		}else{
			pclass = -1;
		}
		
	}
	
	public String toString(){
		return "DataPoint[x=" + x + ",y="+ y +"]";
	}

}
