package app.model.data;

import org.jfree.data.xy.XYDataItem;

import app.model.algorithms.DoubleMath;

public class DVector extends XYDataItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4348757494167079122L;
	private double values[];
	private double weight;
	private int classID;
	private String label = "";
	
	//TODO ArrayList<IObservers> observers = new ArrayList<IObservers>();

	public DVector(double x, double y) {
		super(x, y);
		//by default dimensions = 2, weight = 1, class = +1
		initialiseVector(2, 1.0, +1);
		values[0] = x;
		values[1] = y;
	}
	
	public DVector(double x, double y, double weight) {
		super(x, y);
		initialiseVector(2, weight, +1);
		values[0] = x;
		values[1] = y;
	}

	public DVector(double x, double y, double weight, int classID) {
		super(x, y);
		initialiseVector(2, weight, classID);
		values[0] = x;
		values[1] = y;
	}
	
	public DVector(int dimensions) {
		super(0, 0);
		initialiseVector(dimensions, 1.0, +1);
	}
	
	public DVector(double[] values, double weight, int classID) {
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
	
	public void dataChangedEvent(){
		//TODO
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
		if (obj instanceof DVector){
			DVector vec = (DVector) obj;
			
			if (vec.getDimensions() != getDimensions() ||
				vec.getClassID() != getClassID()
			){
				return false;
			}
			
			if (vec.getWeight() != getWeight()){ //TODO fp compariosn
				return false;
			}
			
			for (int i = 0; i < values.length; i++){
				try {
					if (values[i] != vec.getVal(i)){  //TODO fp compariosn
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
	
	public DVector clone(){
		DVector vec = new DVector(values, weight, classID);
		vec.setLabel(getLabel());
		return vec;
	}
	
	public void notifyObservers(){
		//TODO
	}
	
	/*
	 * 
	 * Utility methods
	 * 
	 * 
	 */
	
	
	public void subtract(DVector op2) throws Exception{
		int dim = op2.getDimensions();
		if (dim != getDimensions()){
			throw new Exception("DVector::subtractBy: Dimensions do not match, cannot subtract");
		}
		
		for (int i  = 0; i < dim; i++){
			values[i] -= op2.getVal(i);
		}
	}
	
	public DVector subtractVectors(DVector op2) throws Exception{
		int dim = op2.getDimensions();
		DVector newVec = clone();
		if (dim != getDimensions()){
			throw new Exception("DVector::subtract: Dimensions do not match, cannot subtract");
		}
		
		for (int i  = 0; i < dim; i++){
			newVec.setVal(i, getVal(i) - op2.getVal(i));
		}
		
		return newVec;
	}
	
	public void add(DVector op2) throws Exception{
		int dim = op2.getDimensions();
		if (dim != getDimensions()){
			throw new Exception("DVector::addTo: Dimensions do not match, cannot add");
		}
		
		for (int i  = 0; i < dim; i++){
			values[i] += op2.getVal(i);
		}
	}
	
	public DVector addVectors(DVector op2) throws Exception{
		int dim = op2.getDimensions();
		DVector newVec = clone();
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
	
	public double getDotProduct(DVector op2) throws Exception{
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
	
	public DVector getUnitVector() throws Exception{
		DVector newVec = clone();
		newVec.divideByScaler(getMagnitude());
		return newVec;
	}
	
	public DVector getOppositeVector(){
		DVector newVec = clone();
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
	public DVector get2DAntiClockwiseNormal() throws Exception{
//		if (getDimensions() != 2){
//			throw new Exception("DVector::get2DAntiClockwiseNormal: not a 2 dimensional dataset");
//		}
		
		return new DVector( -1 * getYValue(), getXValue());
	}
}
