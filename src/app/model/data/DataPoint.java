package app.model.data;

public class DataPoint {
	
	public static int id;
	public int x;
	public int y;
	
	public DataPoint(int x, int y){
		this.x = x;
		this.y = y;
		id++;
	}

}
