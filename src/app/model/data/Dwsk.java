package app.model.data;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;



public class Dwsk {

	
	private double epsilon = 0.001;
	private int maxIterations = 50;
	
	private DVector w;
	private double b;
	
	private DVector nearestPositivePoint = null;
	private DVector nearestNegativePoint = null;	
	
	public DVector getW() {
		return w;
	}
	public double getB() {
		return b;
	}

	public DVector getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public DVector getNearestNegativePoint() {
		return nearestNegativePoint;
	}


	
	public void wsk(DData Dataset, double mu1,double mu2) throws Exception{

		DVector p = null;
		DVector n = null;
		DVector w = null;
		int iterations = 0;
		
		DVector startDirection = new DVector(Dataset.getDimensions());
		startDirection.setX(+1);
		DVector oppositeDirection = new DVector(Dataset.getDimensions());
		oppositeDirection.setX(-1);

		p = Dwrch.findExtremePoint(Dataset.getPositiveClass() , mu1, startDirection);
		n = Dwrch.findExtremePoint(Dataset.getNegativeClass() , mu2, oppositeDirection);
		
		while(iterations < maxIterations){
			w = p.subtractVectors(n);
			DVector wprime = w.clone();
			wprime.multiplyByScaler(-1);

			DVector vp = Dwrch.findExtremePoint(Dataset.getPositiveClass() , mu1, wprime);
			DVector vn = Dwrch.findExtremePoint(Dataset.getNegativeClass() , mu2, w);
			
			DVector dvp = vp.subtractVectors(n);
			DVector dpv = p.subtractVectors(vn);
			
			double w1 =(w.getDotProduct(dvp));
			double w2 = (w.getDotProduct(dpv));
			double ws = w.getMagnitude();
			ws = Math.pow(ws, 2);
			double temp;
			if ( w1 < w2){
				if ((1-(w1/ws)) < epsilon){
					break;
				}
				DVector dnp = p.subtractVectors(n);
				DVector dpvp = p.subtractVectors(vp);
				double top = dnp.getDotProduct(dpvp);
				double bottom = dpvp.getDotProduct(dpvp);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				DVector newP = p.clone();
				newP.multiplyByScaler((1-delta));
				DVector newVP = vp.clone();
				newVP.multiplyByScaler(delta);
				newP.add(newVP);
				p = newP;
			}else{
				if ((1-(w2/ws)) < epsilon){
					break;
				}
				DVector dpn = n.subtractVectors(p);
				DVector dpvn = n.subtractVectors(vn);
				double top = dpn.getDotProduct(dpvn);
				double bottom = dpvn.getDotProduct(dpvn);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				DVector newN = n.clone();
				newN.multiplyByScaler((1-delta));
				DVector newVN = vn.clone();
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
		
		//System.out.println("w = " + finalW.getXValue() + ", "+finalW.getYValue());
		//System.out.println("b = " + finalB);
		System.out.println("it = " + iterations);
		//System.out.println("p = " + p + ", n = " + n);
		
	}
	
	
//	public static DVector findExtremePoint(ArrayList<DVector> list, double mu, final DVector n){
//		
//		if (mu == 0){ //TODO fp comparison
//			//return findCentroid(P);
//		}
//		
//		//TODO if weight = 0 ??
//		Collections.sort(list,Collections.reverseOrder( new Comparator<DVector>() {
//			@Override
//			public int compare(DVector o1, DVector o2) {
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
//		
//		
//		double [] A = new double[list.size()];
//		double s = 0;
//		int k = 0;
//		
//		int count = 0;
//		
//		while (s < 1.0){ //TODO fp comparison
//			if (k >= A.length){
//				//System.out.println("k fixed");
//				k = 0;
//				//return findCentroid(P); //TODO if all A are used then return centroid
//			}
//				if (A[k] == 0){
//					count++;
//				}
//			A[k] = A[k] + Math.min(list.get(k).getWeight() * mu, 1 - s);
//			s += A[k];
//			k++;
//		}
//		
//		DVector v = new DVector(n.getDimensions());
//		int i = 0;
//		for (i = 0; i < count; i++){
//			if (i >=  list.size()){
//				System.out.println("index out of bounds");
//			}
//			DVector c = list.get(i).clone();
//			c.multiplyByScaler(A[i]);
//			try {
//				v.add(c);
//			} catch (Exception e) {
//				// TODO
//				e.printStackTrace();
//			}
//		}
//		
//
////		//copy support points
////		numSupportPoints = count;
////		Z = new SVMDataItem[X.size()];
////		for (int j = 0; j < X.size(); j++){
////			Z[j] = X.get(j); 
////		}
//		
//		return v;
//	}
	
	
	private static double clamp(double c, double cmin, double cmax){
		
		if (c <= cmin){
			return cmin;
		}else if (cmin < c && c < cmax){
			return c;
		}else if (c >= cmax){
			return cmax;
		}else{
			//assert error: should never reach this
			System.out.println("logic error: assertion failed");
			return Double.NaN;
		}
	}

}
