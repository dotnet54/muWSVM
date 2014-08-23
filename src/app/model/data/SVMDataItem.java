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
	
	public static double PRECISION = 0.001; //provide a set precision function
	public static int DP = 2;
	
	public SVMDataItem(double x, double y) {
		super(x, y);
		setWeight(1);
	}
	
	public SVMDataItem(double x, double y, double weight) {
		super(x, y);
		setWeight(weight);
	}
	
	public SVMDataItem(double x, double y, double weight, int dclass) {
		super(x, y);
		setWeight(weight);
		setDataClass(dclass);
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
//		double dot1 = getXValue() * p2.getXValue(); //TODO double op
//		double dot2 = getYValue() * p2.getYValue(); //TODO double op
//		double dot = dot1+ dot2; //TODO double op
		
		double dot1 = dMult(getXValue(), p2.getXValue()); 
		double dot2 = dMult(getYValue(), p2.getYValue());
		double dot = dAdd(dot1, dot2); 
		return  dot;
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
	
	public void setDataClass(int dClass){
		
		//TODO two class
		if (dClass < 0){
			this.DataClass = -1;
		}else{
			this.DataClass = +1;
		}
		//DataClass = dClass;
	}
	
	public Point toPoint(){
		return new Point((int)getXValue(), (int)getYValue());
	}
	
	public static SVMDataItem toDataItem(Point p){
		return new SVMDataItem(p.getX(), p.getY());
	}
	
	public boolean equals(Object obj){
		SVMDataItem d= (SVMDataItem) obj;
//		if (this.getXValue() == d.getXValue()  //TODO double op
//		&& this.getYValue() == d.getYValue()){ //TODO double op
		if (isEqual(this.getXValue(), d.getXValue())
		&& isEqual(this.getYValue(), d.getYValue())){ 	
			return true;
		}else{
			return false;
		}
	}
	
	public String toFormatedString(int dp){
		String sdp = dp + "";
		
		
		String str1 = String.format("%." + sdp +"f", getXValue());
		String str2 = String.format("%." + sdp +"f", getYValue());
		String str3 = String.format("%." + sdp +"f", getWeight());
		return "[" + str1+ "," 
		+ str2 + "]"+ ": " + str3 ;//  : " + getDataClass() ;
	}
	
	public String toString(){
		return "[" + getXValue() + "," 
		+ getYValue() + "]"+ ": " + getWeight();//  : " + getDataClass() ;
	}
//	toString
	
	
	public static boolean isLessThan(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return false;
		}else if(df < 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isLessThanOrEq(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return true;
		}else if(df < 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isGreaterThan(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return false;
		}else if(df > 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isGreaterThanEq(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return true;
		}else if(df > 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isEqual(double d1, double d2){
		double df = Math.abs(d1 - d2);
		if (df < PRECISION){
			return true;
		}else{
			return false;
		}
	}
	
	public static double dAdd(double d1, double d2){
		double r = d1+d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	public static double dMinus(double d1, double d2){
		double r = d1-d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	public static double dMult(double d1, double d2){
		double r = d1*d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	//new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue()
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	
}
