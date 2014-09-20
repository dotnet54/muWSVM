package old;
import java.util.*;

public class Vector {
	private ArrayList<Double> vector;
	
	public Vector(){
		vector = new ArrayList<Double>();
	}
	
	public Vector(double[] values){
		vector = new ArrayList<Double>();
		
		for (int i = 0; i < values.length; i++){
			vector.add(values[i]);
		}
	}
	
	public double dot(Vector v2) throws RuntimeException{
		double sum = 0;
		if (vector.size() != v2.getSize()){
			throw new RuntimeException("Vectors size differ, cannot multiply");
		}
		for (int i = 0; i < vector.size(); i++){
			sum += vector.get(i) * v2.getElement(i);
		}
		
		return sum;
	}
	
	public void add(Vector v2) throws RuntimeException{
		if (vector.size() != v2.getSize()){
			throw new RuntimeException("Vectors size differ, cannot add");
		}
		
		for (int i = 0; i < vector.size(); i++){
			vector.set(i, vector.get(i) + v2.getElement(i));
		}
	}
	public void subtract(Vector v2) throws RuntimeException{
		if (vector.size() != v2.getSize()){
			throw new RuntimeException("Vectors size differ, cannot subtract");
		}
		
		for (int i = 0; i < vector.size(); i++){
			vector.set(i, vector.get(i) - v2.getElement(i));
		}
	}
	
	public void scalerMult(double scaler){
		for (int i = 0; i < vector.size(); i++){
			vector.set(i, vector.get(i) * scaler);
		}
	}
	
	public double getMagnitude(){
		double sum = 0;
		
		for (int i = 0; i < vector.size(); i++){
			sum += Math.pow(vector.get(i), 2);
		}
		
		return sum = Math.sqrt(sum);
	}
	
	
	
	public double getElement(int index){
		return vector.get(index);
	}
	public int getSize(){
		return vector.size();
	}
}
