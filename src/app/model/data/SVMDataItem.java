package app.model.data;

import org.jfree.data.xy.XYDataItem;

import app.model.DoubleMath;

public class SVMDataItem extends XYDataItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4348757494167079122L;
	private double values[];
	private double weight;
	private int classID;
	private String label = "";
	
	public SVMDataItem(double x, double y) {
		super(x, y);
		//by default dimensions = 2, weight = 1, class = +1
		initialiseVector(2, 1.0, +1);
		values[0] = x;
		values[1] = y;
	}
	
	public SVMDataItem(double x, double y, double weight) {
		super(x, y);
		initialiseVector(2, weight, +1);
		values[0] = x;
		values[1] = y;
	}

	public SVMDataItem(double x, double y, double weight, int classID) {
		super(x, y);
		initialiseVector(2, weight, classID);
		values[0] = x;
		values[1] = y;
	}
	
	public SVMDataItem(int dimensions) {
		super(0, 0);
		initialiseVector(dimensions, 1.0, +1);
	}
	
	public SVMDataItem(double[] values, double weight, int classID) {
		super(0, 0);
		initialiseVector(values.length, weight, classID);
		//deep copy
		for (int i = 0; i < values.length; i++){
			this.values[i] = values[i];
		}
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		if (weight < 0){
			weight  =0;
		}
		this.weight = weight;
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDimensions() {
		return  values.length;
	}

	private void initialiseVector(int dimensions, double weight, int classID){
		values = new double[dimensions];
		this.weight = weight;
		this.classID = classID;
	}
	
	
	public double getVal(int index) throws Exception{
		if (index > values.length - 1){
			throw new Exception("DVector::getVal: Index greater than dimensions in vector");
		}
		return values[index];
	}
	
	public void setVal(int index, double newValue) throws Exception{
		if (index > values.length - 1){
			throw new Exception("DVector::setVal: Index greater than dimensions in vector");
		}
		values[index] = newValue;
	}
	
	
	public Double getX(){
		return getXValue();
	}
	
	public Double getY(){
		return getYValue();
	}
	
	public void setX(Number x){
		setX(x.doubleValue());
	}
	public void setY(Number y){
		setY(y.doubleValue());
	}
	
	public void setX(double x){
		try {
			setXValue(x);
		} catch (Exception e) {
			System.out.println("Error: set value x, dimension 0 do not exist in vector");
			e.printStackTrace();
		}
	}
	
	public void setY(double y){
		try {
			setYValue(y);
		} catch (Exception e) {
			System.out.println("Error: set value y, dimension 1 do not exist in vector");
			e.printStackTrace();
		}
	}
	
	
	public double getXValue(){
		try {
			return getVal(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.NaN;
	}
	
	public double getYValue(){
		try {
			return getVal(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.NaN;
	}
	
	public void setXValue(double x) throws Exception{
		setVal(0, x);
	}
	
	public void setYValue(double y) throws Exception{
		setVal(1, y);
	}
	
	public boolean equals(Object obj){
		if (obj instanceof SVMDataItem){
			SVMDataItem vec = (SVMDataItem) obj;
			
			if (vec.getDimensions() != getDimensions() ||
				vec.getClassID() != getClassID()
			){
				return false;
			}
			
			if (vec.getWeight() != getWeight()){ //fp compariosn
				return false;
			}
			
			for (int i = 0; i < values.length; i++){
				try {
					if (values[i] != vec.getVal(i)){  //fp compariosn
						return false;
					}
				} catch (Exception e) {
					System.out.println("DVector::equals: Assertion failed during comparison, vector dimensions do not match");
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public String toString(){
//		return new String(String.format("[%.4f,%.4f]:w=%.2f :c=%d :d=%d", 
//				values[0], values[1], getWeight(), getClassID(), getDimensions()));
		return new String(String.format("[%.4f,%.4f]", values[0], values[1]));
	}
	
	public SVMDataItem clone(){
		SVMDataItem vec = new SVMDataItem(values, weight, classID);
		vec.setLabel(getLabel());
		return vec;
	}
	
	public boolean isZeroOrValid(){
		boolean valid = false;
		try {
			double val;
			
			for (int i = 0; i < getDimensions(); i++){
				val = getVal(i);
				
				if (Double.isNaN(val) || Double.isInfinite(val)){
					return false;
				}
				
				if (!DoubleMath.isEqual(val, 0)){
					valid = true;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}
	
	
	/*
	 * 
	 * Utility methods
	 * 
	 * 
	 */
	
	
	public void subtract(SVMDataItem op2) throws Exception{
		int dim = op2.getDimensions();
		if (dim != getDimensions()){
			throw new Exception("DVector::subtractBy: Dimensions do not match, cannot subtract");
		}
		
		for (int i  = 0; i < dim; i++){
			values[i] -= op2.getVal(i);
		}
	}
	
	public SVMDataItem subtractVectors(SVMDataItem op2) throws Exception{
		int dim = op2.getDimensions();
		SVMDataItem newVec = clone();
		if (dim != getDimensions()){
			throw new Exception("DVector::subtract: Dimensions do not match, cannot subtract");
		}
		
		for (int i  = 0; i < dim; i++){
			newVec.setVal(i, getVal(i) - op2.getVal(i));
		}
		
		return newVec;
	}
	
	public void add(SVMDataItem op2) throws Exception{
		int dim = op2.getDimensions();
		if (dim != getDimensions()){
			throw new Exception("DVector::addTo: Dimensions do not match, cannot add");
		}
		
		for (int i  = 0; i < dim; i++){
			values[i] += op2.getVal(i);
		}
	}
	
	public SVMDataItem addVectors(SVMDataItem op2) throws Exception{
		int dim = op2.getDimensions();
		SVMDataItem newVec = clone();
		if (dim != getDimensions()){
			throw new Exception("DVector::add: Dimensions do not match, cannot add");
		}
		
		for (int i  = 0; i < dim; i++){
			newVec.setVal(i, getVal(i) + op2.getVal(i));
		}
		
		return newVec;
	}
	
	public void multiplyByScaler(double scaler){
		int dim = getDimensions();
		
		for (int i  = 0; i < dim; i++){
			values[i] *= scaler;
		}
	}
	
	public void divideByScaler(double scaler) throws Exception{
		
		if (scaler == 0){
			throw new Exception("DVector::divideByScaler: scaler value cannot be 0");
		}
		int dim = getDimensions();
		
		for (int i  = 0; i < dim; i++){
			values[i] /= scaler;
		}
	}
	
	public double getDotProduct(SVMDataItem op2) throws Exception{
		int dim = op2.getDimensions();
		if (dim != getDimensions()){
			throw new Exception("DVector::addTo: Dimensions do not match, cannot find dot product");
		}
		
		double product = 0.0;
		
		for (int i = 0; i < getDimensions(); i++){
			product = DoubleMath.dAdd(product, DoubleMath.dMult(values[i], op2.getVal(i)));
			//product += (values[i] * op2.getVal(i));
		}
		
		return product;
	}
	
	public double getMagnitude(){
		double magnitude = 0;
		int dim = getDimensions();
		for (int i = 0; i < dim; i++){
			try {
				magnitude += Math.pow(getVal(i), 2);
			} catch (Exception e) {
				System.out.println("DVector::getMagnitude: Vector dimension assertion failed");
				e.printStackTrace();
			}
		}
		
		return Math.sqrt(magnitude);
	}
	
	public SVMDataItem getUnitVector() throws Exception{
		SVMDataItem newVec = clone();
		newVec.divideByScaler(getMagnitude());
		return newVec;
	}
	
	public SVMDataItem getOppositeVector(){
		SVMDataItem newVec = clone();
		try {
			for (int i = 0; i < getDimensions(); i++) {
				newVec.setVal(i, -1 * newVec.getVal(i));
			}
		} catch (Exception e) {
			System.out
					.println("DVector::getOppositeVector: Vector dimension assertion failed");
			e.printStackTrace();
		}
		return newVec;
	}
	
	//TODO verify clockwise or anticlockwise
	public SVMDataItem get2DAntiClockwiseNormal() throws Exception{
//		if (getDimensions() != 2){
//			throw new Exception("DVector::get2DAntiClockwiseNormal: not a 2 dimensional dataset");
//		}
		
		return new SVMDataItem( -1 * getYValue(), getXValue());
	}
	
	public String toFormatedString(int dp){
		String sdp = dp + "";
		
		
		String str1 = String.format("%." + sdp +"f", getXValue());
		String str2 = String.format("%." + sdp +"f", getYValue());
		String str3 = String.format("%." + sdp +"f", getWeight());
		return "[" + str1+ "," 
		+ str2 + "]"+ ":w =" + str3 ;//  : " + getDataClass() ;
	}
	
}
