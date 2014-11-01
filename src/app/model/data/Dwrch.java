package app.model.data;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import app.model.algorithms.DoubleMath;
import app.model.data.SVMDataItem;

//

/*
 * 
 * RECURSION TOO DEEP IF P IS IN STACK, USE CLASS STATIC
 * 
 */
public class Dwrch {
	
	
	private static DVector [] Z;
	private static int numSupportPoints;
	
	private static double epsilon = 0.001;
	
	private static long startTime = 0;
	private static long endTime = 0;
	private static long elapsedTime = 0;
	
	public static ArrayList<SVMDataItem> calcWeightedReducedCHull2(ArrayList<SVMDataItem> dataset, double mu) {
		ArrayList<SVMDataItem> r = new ArrayList<SVMDataItem>();
		
		try{
			ArrayList<DVector> P =  new ArrayList<DVector>();
			DVector t =null;
			for (int i=0; i< dataset.size(); i++){
				t = new DVector(0,0);
				t.setX(dataset.get(i).getX());
				t.setY(dataset.get(i).getY());
				t.setWeight(dataset.get(i).getWeight());
				t.setClassID(dataset.get(i).getClassID());
				P.add(t);
			}
		
			DVector[] res = rhull(P, mu);
			
			SVMDataItem p;
			for (int i=0; i< res.length; i++){ //TODO assume res != null
				p = new SVMDataItem(0,0);
				p.setX(res[i].getX());
				p.setY(res[i].getY());
				p.setLabel("");
				r.add(p);
			}
		}catch(Exception e){
			e.printStackTrace();
		}catch (StackOverflowError e){
			System.out.println("Stack over flow in RCH!");
			return null;
		}
		
		return r;
	}
	
	
	private static DVector[] rhull(ArrayList<DVector> P, double mu) throws StackOverflowError{

		startTime = System.nanoTime();
		
		DVector[] Ret = null;
		try {
			DVector n = new DVector(-1,0);
			DVector l =  findExtremePoint(P, mu, n);
			n.setX(1);
			DVector r =  findExtremePoint(P, mu,  n);
			
			Set<DVector> result = rhull_aux(P, l ,r, mu);
			Set<DVector> B = rhull_aux(P, r, l, mu);
			result.addAll(B);

			Ret = new DVector[result.size()];
			Ret = result.toArray(Ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		System.out.println("Elapsed Time for WRCH: " + elapsedTime / 1e6 +" ms");
		
		return Ret;
	}
	
	private static Set<DVector> rhull_aux(ArrayList<DVector> P, 
			DVector l, DVector r, double mu) throws StackOverflowError{
		
		Set<DVector> result = new LinkedHashSet<DVector>();
		
		try {
			DVector n = new DVector(0,0);
			DVector h = new DVector(0,0);
			
			DVector diff = r.subtractVectors(l);
			diff.setX(DoubleMath.dMinus(r.getX(), l.getX()));
			diff.setY(DoubleMath.dMinus(r.getY(), l.getY()));
			n.setX(-diff.getY());
			n.setY(diff.getX());

			h =  findExtremePoint(P, mu,  n);
			//System.out.format("h:%s,l:%s,r%s \n", h,l,r);

			DVector facetNormal = l.subtractVectors(r);
			facetNormal.setX(DoubleMath.dMinus(l.getX(), r.getX()));
			facetNormal.setY(DoubleMath.dMinus(l.getY(), r.getY()));
			facetNormal =  facetNormal.get2DAntiClockwiseNormal();
			double pointOffset = h.getDotProduct(facetNormal);
			double facetOffset = l.getDotProduct(facetNormal);
			
			if (Math.abs(pointOffset - facetOffset) < epsilon){
				result.add(l);
				result.add(r);
				return result;
			}
			
			
			
			DVector nl = new DVector(0,0);
			DVector nr = new DVector(0,0);
			
			diff = h.subtractVectors(l);
			diff.setX(DoubleMath.dMinus(h.getX(), l.getX()));
			diff.setY(DoubleMath.dMinus(h.getY(), l.getY()));
			nl.setX(-diff.getY());
			nl.setY(diff.getX());
			
			diff = r.subtractVectors(h);
			diff.setX(DoubleMath.dMinus(r.getX(), h.getX()));
			diff.setY(DoubleMath.dMinus(r.getY(), h.getY()));
			nr.setX(-diff.getY());
			nr.setY(diff.getX());
			
			DVector[] supportPoints = new DVector[numSupportPoints];
			for (int i = 0; i < numSupportPoints ; i++){
				supportPoints[i] = Z[i];
			}
			double scalerProjections[] = new double [numSupportPoints];
			double minOffset;
			ArrayList<DVector> L = new ArrayList<DVector>();
			ArrayList<DVector> R = new ArrayList<DVector>();
			
			
			for (int i = 0; i < numSupportPoints; i++){
				scalerProjections[i] = nl.getDotProduct(supportPoints[i]);
			}
			minOffset = minValue(scalerProjections);
			int size = P.size();
			double projection = 0.0;
			for (int i = 0; i < size; i++){
				projection = P.get(i).getDotProduct(nl);
				if (Math.abs(projection - minOffset) < epsilon ||
						projection > minOffset){
					L.add(P.get(i));
				}
			}

			for (int i = 0; i < numSupportPoints; i++){
				scalerProjections[i] = nr.getDotProduct(supportPoints[i]);
			}
			minOffset = minValue(scalerProjections);
			for (int i = 0; i < size; i++){
				projection = P.get(i).getDotProduct(nr);
				if (Math.abs(projection - minOffset) < epsilon || 
						projection > minOffset){
					R.add(P.get(i));
				}
			}
			
			
			result = rhull_aux(L, l ,h, mu);
			Set<DVector> B = rhull_aux(R, h, r, mu);
			result.addAll(B);
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static DVector findExtremePoint(ArrayList<DVector> list, double mu, final DVector n){
		
		if (mu == 0){ //TODO fp comparison
			//return findCentroid(P);
		}
		
		//TODO if weight = 0 ??
		Collections.sort(list,Collections.reverseOrder( new Comparator<DVector>() {
			@Override
			public int compare(DVector o1, DVector o2) {
				//TODO fp comparisons
				try {
					double p1 = o1.getDotProduct(n);
					double p2 = o2.getDotProduct(n);
					//double diff = Math.abs(DoubleMath.dMinus(p1, p2));
					double diff = Math.abs(p1 - p2);
					
					double d1 = o1.getWeight();
					double d2 = o2.getWeight();
					//double diffWeight =  Math.abs(DoubleMath.dMinus(d1, d2));
					double diffWeight =  Math.abs(d1 - d2);
					
					//TODO Exception: Comparison violates general contract
					if ((diff - epsilon) < 0){
//						if ((diffWeight - epsilon) < 0){
//							return 0;
//						}else if(d1 > d2){
//							return 1;
//						}else{
//							return -1;
//						}
						//System.out.format("0: %f : %f = %f\n", p1, p2, diff);
						return 0;
					}else if (p1 > p2){
						//System.out.format("1: %f : %f = %f\n", p1, p2, diff);
						return 1;
					}else{
						//System.out.format("-1: %f : %f = %f\n", p1, p2, diff);
						return -1;
					}
					
				} catch (Exception e) {
					System.out.println("Exception In Comparison");
					e.printStackTrace();
					
					return 0;
				}
			}
		}));
		
		
		double [] A = new double[list.size()];
		double s = 0;
		int k = 0;
		
		int count = 0;
		
		while (s < 1.0){ //TODO fp comparison
			if (k >= A.length){
				//System.out.println("k fixed");
				k = 0;
				//return findCentroid(P); //TODO if all A are used then return centroid
			}
				if (A[k] == 0){
					count++;
				}
			A[k] = A[k] + Math.min(list.get(k).getWeight() * mu, 1 - s);
			s += A[k];
			k++;
		}
		
		DVector v = new DVector(n.getDimensions());
		int i = 0;
		for (i = 0; i < count; i++){
			if (i >=  list.size()){
				System.out.println("index out of bounds");
			}
			DVector c = list.get(i).clone();
			c.multiplyByScaler(A[i]);
			try {
				v.add(c);
			} catch (Exception e) {
				// TODO
				e.printStackTrace();
			}
		}
		

//		//copy support points
		numSupportPoints = count;
		Z = new DVector[list.size()];
		for (int j = 0; j < list.size(); j++){
			Z[j] = list.get(j); 
		}
		
		return v;
	}

	private static double minValue(double[] array){
		double min = Double.NaN;
		if (array != null && array.length > 0){
			min = array[0];
			for (int i = 1; i < array.length; i++){
				if (array[i] < min){ //TODO fp comparison
					min = array[i];
				}
			}
		}
		return min;
	}
	
	private static SVMDataItem normal(SVMDataItem p1, SVMDataItem p2){
			double dx, dy;
			dx = DoubleMath.dMinus(p1.getXValue(), p2.getXValue());
			dy = DoubleMath.dMinus(p1.getYValue(), p2.getYValue());
			SVMDataItem n = new SVMDataItem(0,0);
			n.setX(-dy);
			n.setY(dx);
			return n;
		}

	private static SVMDataItem findCentroid(SVMDataItem P[]){
		SVMDataItem cent = new SVMDataItem(0, 0);
		
		for (int i = 0; i < P.length; i++){
			cent.setX(cent.getX() + P[i].getXValue() * P[i].getWeight());
			cent.setY(cent.getY() + P[i].getYValue() * P[i].getWeight());
		}
		
		double totalWeight = 0;
		for (int i = 0; i < P.length; i++){
			totalWeight += P[i].getWeight();
		}
		//ASSERT totalWeight != 0;
		
		cent.setX(cent.getXValue() / totalWeight);
		cent.setY(cent.getYValue() / totalWeight);
		
		return cent;
	}
	
	
	
}
