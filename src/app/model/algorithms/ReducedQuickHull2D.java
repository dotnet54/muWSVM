package app.model.algorithms;

import java.awt.Point;
import java.util.ArrayList;

import app.model.data.DataPoint;

public class ReducedQuickHull2D {
	
	public static class Vector{
		public double x;
		public double y;
		
		public Vector(double x, double y){
			this.x = x;
			this.y = y;
		}
		
		public String toString(){
			return "Vector [x=" + x + ",y="+ y +"]";
		}
		
		public void add(Vector v2){
			x += v2.x;
			y += v2.y;
		}
		
		public Vector minus(Vector v2){
			Vector v = new Vector(x, y);
			v.x = x - v2.x;
			v.y = y - v2.y;
			return v;
		}
		
		public double dot(Vector v2){
			return x*v2.x + y * v2.y;
		}
		
		public double magnitude(){
			double a =Math.pow(x, 2) + Math.pow(y, 2);
			double b =Math.sqrt(a);
			return b;
		}
		
		public double scalerproj(Vector v2){
			double dot = dot(v2);
			double mag = v2.magnitude();
			if (mag == 0)
				return 0;
			return dot / mag;	//TODO div by 0 if mag is = 0
		}
		
		public static Vector normal(Vector v1, Vector v2){	//above normal
			double dx = v2.x - v1.x;
			double dy = v2.y - v1.y;
			
			Vector n = new Vector(-dy, dx);
			return n;
		}
		public Vector normal(Vector v1){	//above normal
			double dx = v1.x - x;
			double dy = v1.y - y;
			
			Vector n = new Vector(-dy, dx);
			return n;
		}
		
		public boolean equals(Vector v1){
			double THRESHOLD = 1E-5;
			if ((v1.x - x) < THRESHOLD && (v1.y - y) < THRESHOLD){
				return true;
			}else{
				return false;
			}
		}
		
		
	}

	public static ArrayList<Vector> order(ArrayList<Vector> P, Vector normal){
		ArrayList<Vector> ordered = new ArrayList<ReducedQuickHull2D.Vector>();
		double[] projections = new double[P.size()];
		int [] indices = new int[P.size()];
		double max = 0;
		int maxi = 0;
		double temp;
		
		for (int i = 0; i < P.size(); i++){
			projections[i] = P.get(i).scalerproj(normal);
		}
		int j;
		max = Integer.MIN_VALUE;
		
		for (int i = 0; i < P.size(); i++){
			for (j = 0; j < projections.length; j++){
				if (projections[j] != Integer.MIN_VALUE){	//TODO magic number
					//max = projections[i];
					//maxi = i;
					if (max < projections[j]){		//TODO <= ??
						max = projections[j];
						maxi = j;
					}
				}
			}
			projections[maxi] = Integer.MIN_VALUE;
			indices[i] = maxi;
			max = Integer.MIN_VALUE;
		}
		
		for (int i = 0; i < P.size(); i++){
			ordered.add(P.get(indices[i]));
		}
		
		return ordered;
	}
	
	
	public static Vector theorem3(ArrayList<Vector> P, double mu){
		int m = (int) Math.ceil(1/mu);	//precision 
		
		ReducedQuickHull2D.Vector v = new ReducedQuickHull2D.Vector(0,0);
		for (int i = 0; i < (m-1); i++){		//if m1 > P.size case
			v.x += mu * P.get(i).x;  
			v.y += mu * P.get(i).y;  
		}
		
		v.x += ((1 - (m-1)*mu) * P.get(m-1).x); //if P dont have index m
		v.y += ((1 - (m-1)*mu) * P.get(m-1).y);
		
		return v;
	}
	
	
	
	
	
	public static ArrayList<Vector> RQH(ArrayList<Vector> P, double mu){
		ArrayList<Vector> left = null;
		ArrayList<Vector> right = null;
		ArrayList<Vector> orderedList = null;
		Vector normal = new Vector(-1,0);
		orderedList = order(P, normal);
		Vector l = theorem3(orderedList, mu);
		normal.x = 1;
		orderedList = order(P, normal);
		Vector r = theorem3(orderedList, mu);

		left = RQH_aux(P,mu, l, r );
		right  = RQH_aux(P,mu, r, l);
		if (left.addAll(right) == false){
			throw new RuntimeException("cannot append left and right QH");
		}
		return left;
	}
	
	public static ArrayList<Vector> RQH_aux(ArrayList<Vector> P, double mu,
			Vector l, Vector r){
		ArrayList<Vector> A = null;
		ArrayList<Vector> B = null;
		ArrayList<Vector> left = new ArrayList<Vector>();
		ArrayList<Vector> right = new ArrayList<Vector>();
		ArrayList<Vector> orderedList = null;
		
		if (P.isEmpty()){
			return new ArrayList<ReducedQuickHull2D.Vector>();
		}
		
		Vector n  = Vector.normal(r, l);
		orderedList = order(P, n);
		Vector h = theorem3(orderedList, mu);
		
		if ( h.equals(l) || h.equals(r)){
			ArrayList<Vector> r2 = new ArrayList<Vector>();
			r2.add(l);
			r2.add(r);
			return r2;
		}
		
		Vector nl  = Vector.normal(h, r);
		Vector nr  = Vector.normal(r, h);
		
		
		
		int s = (int) Math.ceil(1/mu);
		//int s = 1 / m;
		
		orderedList = order(orderedList, nl);
		for (int i = 0; i < s; i++){
			left.add(orderedList.get(i));
		}
		orderedList = order(orderedList, nr);
		for (int i = 0; i < s; i++){
			right.add(orderedList.get(i));
		}
	 
		A = RQH_aux(left,mu, l, h );
		B  = RQH_aux(right,mu, h, r);
		if (A.addAll(right) == false){
			throw new RuntimeException("cannot append left and right QH");
		}
		return A;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
