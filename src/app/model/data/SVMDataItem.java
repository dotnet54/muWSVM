package app.model.data;

import java.awt.Point;
import java.util.Vector;

import org.jfree.data.xy.XYDataItem;

import app.model.algorithms.DoubleMath;


public class SVMDataItem extends XYDataItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int classID = 1;
	private double weight = 1;//defaults
	private double scProjection = 0;
	private String label = "";
	
	private DVector vector = null; //TODO THINK ABOUT THIS
	
	public DVector getVector() {
		return vector;
	}

	public void setVector(DVector vec) {
		try {
			setX(vec.getX());
			setY(vec.getY());
			setWeight(vec.getWeight());
			setClassID(vec.getClassID());
			this.vector = vec;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public SVMDataItem(DVector vec){
		super(0, 0);
		try {
			setX(vec.getX());
			setY(vec.getY());
			setWeight(vec.getWeight());
			setClassID(vec.getClassID());
			vector = vec;

		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	public SVMDataItem(double x, double y) {
		super(x, y);
		initialise(x,y, 1, 1);
	}
	
	public SVMDataItem(double x, double y, double weight) {
		super(x, y);
		initialise(x,y, weight, 1);
	}
	
	public SVMDataItem(double x, double y, double weight, int classID) {
		super(x, y);
		initialise(x,y, weight, classID);
	}
	
	
	private void initialise(double x, double y, double weight, int classID){
		setX(x);
		setY(y);
		setWeight(weight);
		setClassID(classID);
		setLabel(getWeight() + "");
		vector = null;
	}
	
	public Double getX(){
		return super.getXValue();
	}
	public void setX(double x){
		 super.setX(DoubleMath.round(x, DoubleMath.DP));
	}
	public Double getY(){
		return super.getYValue();
	}
	public void setY(double y){
		super.setY(DoubleMath.round(y, DoubleMath.DP));
	}
	
	
	public double getDotProduct(SVMDataItem p2){

		double dot1 = DoubleMath.dMult(getXValue(), p2.getXValue()); 
		double dot2 = DoubleMath.dMult(getYValue(), p2.getYValue());
		double dot = DoubleMath.dAdd(dot1, dot2); 
		return  dot;
	}
	
	public double getMagnitude(){
		return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2)) ;
	}
	
	public SVMDataItem getAntiClockWiseNormal(){
		return new SVMDataItem(-getYValue(), getXValue());
	}
	
	public void scale(double scaleFactor){
		this.setX(getXValue() * scaleFactor);
		this.setY(getYValue() * scaleFactor);
	}

	public double getWeight(){
		return weight;
	}
	
	public void setWeight(double w){
		weight = w;
	}
	
	public int getClassID(){
		return classID;
	}
	
	public void setClassID(int classID){
		this.classID = classID;
	}
	
	public Point toPoint(){
		return new Point((int)getXValue(), (int)getYValue());
	}
	
	public static SVMDataItem toDataItem(Point p){
		return new SVMDataItem(p.getX(), p.getY());
	}
	
	//TODO check weight, classs
	public boolean equals(Object obj){
		
		if (obj instanceof SVMDataItem){
			SVMDataItem d= (SVMDataItem) obj;
			if (DoubleMath.isEqual(this.getXValue(), d.getXValue())
			&& DoubleMath.isEqual(this.getYValue(), d.getYValue()) 
			&& DoubleMath.isEqual(this.getWeight(), d.getWeight())
			&& getClassID() == d.getClassID()){ 	
				return true;
			}
		}
		
		return false;
	}
	
	public int custCompareTo(SVMDataItem o2){
		if ((scProjection < o2.getScProj())){
			return -1;
		}else if ((scProjection > o2.getScProj())){
			return +1;
		}else{
			if (getWeight() < o2.getWeight()){
				return -1;
			}else if(getWeight() > o2.getWeight()){
				return +1;
			}else{
				return 0;
			}
			
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
	
	
	public double project(SVMDataItem normal){
		scProjection = this.getDotProduct(normal);
		return scProjection;
	}
	
	//TODO bad idea but used for sorting
	public double getScProj(){
		return scProjection;
	}
	
//    @Override
//    public int compareTo(Object o1) {
//		return DataClass;
//    }

	
}
