package app.model;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import app.model.data.SVMDataItem;
import app.model.data.SVMDataSet;



public class WSKSolver {

	
	public static double epsilon = 0.001;
	public static int maxIterations = 500;
	
	private SVMDataItem w;
	private double b;
	
	private SVMDataItem nearestPositivePoint = null;
	private SVMDataItem nearestNegativePoint = null;	
	
	private int iterations = 0;
	
	private long startTime = 0;
	private long endTime = 0;
	private long elapsedTime = 0;
	
	public SVMDataItem getW() {
		return w;
	}
	public double getB() {
		return b;
	}

	public SVMDataItem getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public SVMDataItem getNearestNegativePoint() {
		return nearestNegativePoint;
	}

	//TODO handle n-dimension alg9 while wrch use 2 d a;g9
	
	public void wsk(SVMDataSet Dataset, double mu1,double mu2) throws Exception{

		startTime = System.nanoTime();
		double breakCondition = Double.NaN;
		
		SVMDataItem p = null;
		SVMDataItem n = null;
		SVMDataItem w = null;
		
		ArrayList<SVMDataItem> positiveClass = Dataset.getPositiveClass();
		ArrayList<SVMDataItem> negativeClass = Dataset.getNegativeClass();
		
		SVMDataItem startDirection = new SVMDataItem(Dataset.getDimensions());
		startDirection.setXValue(+1);
		SVMDataItem oppositeDirection = new SVMDataItem(Dataset.getDimensions());
		oppositeDirection.setXValue(-1);

		p = findExtremePoint(positiveClass , mu1, startDirection);
		n = findExtremePoint(negativeClass , mu2, oppositeDirection);
		
		
		while(iterations < maxIterations){
			w = p.subtractVectors(n);
			
			//TODO hot fix centroid
			if (w.equals(new SVMDataItem(w.getDimensions()))){
				//centroids on top of each other-> no solution
			
			}
			
			SVMDataItem wprime = w.clone();
			wprime.multiplyByScaler(-1);

			SVMDataItem vp = findExtremePoint(positiveClass , mu1, wprime);
			SVMDataItem vn = findExtremePoint(negativeClass , mu2, w);
			
			SVMDataItem dvp = vp.subtractVectors(n);
			SVMDataItem dpv = p.subtractVectors(vn);
			
			double w1 =(w.getDotProduct(dvp));
			double w2 = (w.getDotProduct(dpv));
			double ws = w.getMagnitude();
			ws = Math.pow(ws, 2);
			double temp;
			if ( w1 < w2){
				if ((1-(w1/ws)) < epsilon){
					breakCondition = (1-(w1/ws));
					break;
				}
				SVMDataItem dnp = p.subtractVectors(n);
				SVMDataItem dpvp = p.subtractVectors(vp);
				double top = dnp.getDotProduct(dpvp);
				double bottom = dpvp.getDotProduct(dpvp);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				
				if (delta == Double.NaN){
					break;
				}
				
				SVMDataItem newP = p.clone();
				newP.multiplyByScaler((1-delta));
				SVMDataItem newVP = vp.clone();
				newVP.multiplyByScaler(delta);
				newP.add(newVP);
				p = newP;
			}else{
				if ((1-(w2/ws)) < epsilon){
					breakCondition = (1-(w1/ws));
					break;
				}
				SVMDataItem dpn = n.subtractVectors(p);
				SVMDataItem dpvn = n.subtractVectors(vn);
				double top = dpn.getDotProduct(dpvn);
				double bottom = dpvn.getDotProduct(dpvn);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				
				if (delta == Double.NaN){
					break;
				}
				
				SVMDataItem newN = n.clone();
				newN.multiplyByScaler((1-delta));
				SVMDataItem newVN = vn.clone();
				newVN.multiplyByScaler(delta);
				newN.add(newVN);
				n = newN;
			}
			iterations++;
		}
		double b = 0.5 * ((w.getDotProduct(p)) + (w.getDotProduct(n)));
		
		this.w = w;
		this.b = b;
		nearestPositivePoint = p;
		nearestNegativePoint = n;
		
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		
		//System.out.println("w = " + finalW.getXValue() + ", "+finalW.getYValue());
		//System.out.println("b = " + finalB);
		System.out.println("it = " + iterations + ", break: " + breakCondition);
		System.out.println("Elapsed Time for WSK: " + elapsedTime / 1e6 +" ms");
		//System.out.println("p = " + p + ", n = " + n);
		
	}
	
	
	//TODO NOTE WSK is n dimension whole WRCH is 2 dimension
	
	
	public static SVMDataItem findExtremePoint(ArrayList<SVMDataItem> list, double mu, final SVMDataItem n){
		
		if (mu == 0){ //TODO fp comparison
			return WRCHSolver.findCentroid(list);	//TODO hot fix
		}
		
//		if (1/mu > list.size()){
//			System.out.println("centr");
//			return Dwrch.findCentroid(list);//TODO hot fix
//		}
		
		testSorting(list, n);
		
		//TODO if weight = 0 ??
//		Collections.sort(list,Collections.reverseOrder( new Comparator<DVector>() {
//			@Override
//			public int compare(DVector o1, DVector o2) {
//				
//				//TODO fp comparisons
//				try {
//					double p1 = o1.getDotProduct(n);
//					double p2 = o2.getDotProduct(n);
//					if (p1 < p2){
//						return -1;
//					}else if (p2 > p1){
//						return 1;
//					}else{
//						return 0;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					return 0;
//				}
//			}
//		}));
		
		
		double [] A = new double[list.size()];
		double s = 0;
		int k = 0;
		
		int count = 0;
		
		while (s < 1.0){ //TODO fp comparison
			if (k >= A.length){
				System.out.println("k fixed");
				k = 0;//
				return WRCHSolver.findCentroid(list); // TODO if all A are used then return centroid
			}
				if (A[k] == 0){
					count++;
				}
			A[k] = A[k] + Math.min(list.get(k).getWeight() * mu, 1 - s);
			s += A[k];
			k++;
		}
		
		SVMDataItem v = new SVMDataItem(n.getDimensions());
		int i = 0;
		for (i = 0; i < count; i++){
			if (i >=  list.size()){
				System.out.println("index out of bounds");
			}
			SVMDataItem c = list.get(i).clone();
			c.multiplyByScaler(A[i]);
			try {
				v.add(c);
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		

//		//copy support points
//		numSupportPoints = count;
//		Z = new SVMDataItem[X.size()];
//		for (int j = 0; j < X.size(); j++){
//			Z[j] = X.get(j); 
//		}
		
		return v;
	}
	
	private static void testSorting(ArrayList<SVMDataItem> list,final SVMDataItem normal){
		Collections.sort(list,Collections.reverseOrder( new Comparator<SVMDataItem>() {
			@Override
			public int compare(SVMDataItem o1, SVMDataItem o2) {
				
				//TODO fp comparisons
				try {
					double p1 = o1.getDotProduct(normal);
					double p2 = o2.getDotProduct(normal);
					double diff = p1 - p2;
					//System.out.format("p1:%f p2:%f\n" , p1, p2);					
					if (p1 < p2){
						return -1;
					}else if (p2 > p1){
						return 1;
					}else{
						return 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}));
	}
	
	private static double clamp(double c, double cmin, double cmax){
		
		if (c <= cmin){
			return cmin;
		}else if (cmin < c && c < cmax){
			return c;
		}else if (c >= cmax){
			return cmax;
		}else{
			//assert error: will happen if doubles contain NaN
			return Double.NaN; 
		}
	}

}
