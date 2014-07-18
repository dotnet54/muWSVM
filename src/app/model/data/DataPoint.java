package app.model.data;

public class DataPoint {
	
	public static int id;
	public double x;
	public double y;
	
	public DataPoint(double x, double y){
		this.x = x;
		this.y = y;
		id++;
	}
	
	public String toString(){
		return "DataPoint[x=" + x + ",y="+ y +"]";
	}

}
