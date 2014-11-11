package app.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import app.model.data.SVMDataItem;
import app.model.data.SVMDataSeries;
import app.test.SVMDataItemOLD2;


/*
 * 
 * 
 * 
 */
public class WRCHSolver {
	
	
	private static SVMDataItem [] Z;
	private static int numSupportPoints;
	
	private static double epsilon = 0.001;
	
	private static long startTime = 0;
	private static long endTime = 0;
	private static long elapsedTime = 0;
	
	public static ArrayList<SVMDataItem> calcWeightedReducedCHull2(SVMDataSeries series, double mu) {
		ArrayList<SVMDataItem> r = new ArrayList<SVMDataItem>();
		
		try{
			
			if (series == null || series.getItemCount() == 0){
				return null;
			}
			
			ArrayList<SVMDataItem> P =  new ArrayList<SVMDataItem>();
			SVMDataItem t =null;
			int xDim = series.getXDimension();
			int yDim = series.getYDimension();
			for (int i=0; i< series.getItemCount(); i++){
				t = new SVMDataItem(0,0);
				t.setX(series.getRawDataItem(i).getVal(xDim));
				t.setY(series.getRawDataItem(i).getVal(yDim));
				t.setWeight(series.getRawDataItem(i).getWeight());
				t.setClassID(series.getRawDataItem(i).getClassID());
				P.add(t);
			}
			
			
//			if (1/mu > P.size()){
//				r.add(findCentroid(P));
//				return (r);
//			}
			
			SVMDataItem[] res = rhull(P, mu);
			
			SVMDataItem p;
			for (int i=0;res!=null && i< res.length; i++){
				p = new SVMDataItem(0,0);
				p.setX(res[i].getXValue());
				p.setY(res[i].getYValue());
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
	
	
	private static SVMDataItem[] rhull(ArrayList<SVMDataItem> P, double mu) throws StackOverflowError{

		startTime = System.nanoTime();
		
		SVMDataItem[] Ret = null;
		try {
			SVMDataItem n = new SVMDataItem(-1,0);
			SVMDataItem l =  findExtremePoint(P, mu, n);
			n.setXValue(1);
			SVMDataItem r =  findExtremePoint(P, mu,  n);
			
			Set<SVMDataItem> result = rhull_aux(P, l ,r, mu);
			Set<SVMDataItem> B = rhull_aux(P, r, l, mu);
			result.addAll(B);

			Ret = new SVMDataItem[result.size()];
			Ret = result.toArray(Ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		System.out.println("Elapsed Time for WRCH: " + elapsedTime / 1e6 +" ms");
		
		return Ret;
	}
	
	private static Set<SVMDataItem> rhull_aux(ArrayList<SVMDataItem> P, 
			SVMDataItem l, SVMDataItem r, double mu) throws StackOverflowError{
		
		Set<SVMDataItem> result = new LinkedHashSet<SVMDataItem>();
		
		try {
			SVMDataItem n = new SVMDataItem(0,0);
			SVMDataItem h = new SVMDataItem(0,0);
			
			SVMDataItem diff = r.subtractVectors(l);
			diff.setXValue(DoubleMath.dMinus(r.getXValue(), l.getXValue()));
			diff.setYValue(DoubleMath.dMinus(r.getYValue(), l.getYValue()));
			n.setXValue(-diff.getYValue());
			n.setYValue(diff.getXValue());

			h =  findExtremePoint(P, mu,  n);
			//System.out.format("h:%s,l:%s,r%s \n", h,l,r);

			SVMDataItem facetNormal = l.subtractVectors(r);
			facetNormal.setXValue(DoubleMath.dMinus(l.getXValue(), r.getXValue()));
			facetNormal.setYValue(DoubleMath.dMinus(l.getYValue(), r.getYValue()));
			facetNormal =  facetNormal.get2DAntiClockwiseNormal();
			double pointOffset = h.getDotProduct(facetNormal);
			double facetOffset = l.getDotProduct(facetNormal);
			
			if (Math.abs(pointOffset - facetOffset) < epsilon){
				result.add(l);
				result.add(r);
				return result;
			}
			
			
			
			SVMDataItem nl = new SVMDataItem(0,0);
			SVMDataItem nr = new SVMDataItem(0,0);
			
			diff = h.subtractVectors(l);
			diff.setXValue(DoubleMath.dMinus(h.getXValue(), l.getXValue()));
			diff.setYValue(DoubleMath.dMinus(h.getYValue(), l.getYValue()));
			nl.setXValue(-diff.getYValue());
			nl.setYValue(diff.getXValue());
			
			diff = r.subtractVectors(h);
			diff.setXValue(DoubleMath.dMinus(r.getXValue(), h.getXValue()));
			diff.setYValue(DoubleMath.dMinus(r.getYValue(), h.getYValue()));
			nr.setXValue(-diff.getYValue());
			nr.setYValue(diff.getXValue());
			
			SVMDataItem[] supportPoints = new SVMDataItem[numSupportPoints];
			for (int i = 0; i < numSupportPoints ; i++){
				supportPoints[i] = Z[i];
			}
			double scalerProjections[] = new double [numSupportPoints];
			double minOffset;
			ArrayList<SVMDataItem> L = new ArrayList<SVMDataItem>();
			ArrayList<SVMDataItem> R = new ArrayList<SVMDataItem>();
			
			
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
			Set<SVMDataItem> B = rhull_aux(R, h, r, mu);
			result.addAll(B);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	
//	public static DVector alg10(ArrayList<DVector> list, double mu, final DVector n){
//		DVector v = new DVector(n.getDimensions());
//		try {
//				
//			double A[] = new double[list.size()];
//			double total = 0;
//			
//			int order[] = new int[list.size()];
//			double projections[] = new double[list.size()];
//			for (int i = 0; i < list.size(); i++){
//				projections[i] = list.get(i).getDotProduct(n);
//			}
//			
//			double temp;
//			int m, min;
//			for (int i = 0; i < projections.length -1; i++){
//				min = i;
//				for (m = i+1; m < projections.length; m++){
//					if (projections[m] < projections[min]){
//						min = m;
//						order[i] = m;
//					}
//				}
//				if (min != i){
//					temp = projections[i];
//					projections[i] = projections[min];
//					projections[min] = temp;
//				}
//			}
//			
//			int i = 0;
//			for (int j = 0; j < list.size() && total < 1; j++){
//				A[i] = Math.min(list.get(i).getWeight() * mu, 1 - total);
//				total += A[i];
//				
//				if (j == list.size() && total < 1){
//					j = 0;
//				}
//			}
//			
//			
//		
//			DVector c = null;
//			int k = 0;
//			for (i = 0; i < A.length; i++) {
//				c = list.get(i).clone();
//				c.multiplyByScaler(A[i]);
//				v.add(c);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return v;
//	}
//	
	public static SVMDataItem findExtremePoint(ArrayList<SVMDataItem> list, double mu, final SVMDataItem n){
		
		if (mu == 0){ //TODO fp comparison
			return findCentroid(list);
		}
		
		if (1/mu > list.size()){
			System.out.println("centr");
			//return Dwrch.findCentroid(list);//TODO hot fix
		}
		
		//TODO if weight = 0 ??
		Collections.sort(list,Collections.reverseOrder( new Comparator<SVMDataItem>() {
			@Override
			public int compare(SVMDataItem o1, SVMDataItem o2) {
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
				System.out.println("k fixed");
				k = 0;//
				return findCentroid(list); //TODO if all A are used then return centroid
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
		numSupportPoints = count;
		Z = new SVMDataItem[list.size()];
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

	public static SVMDataItem findCentroid(SVMDataItem P[]){
		SVMDataItem cent = new SVMDataItem(0, 0);
		
		for (int i = 0; i < P.length; i++){
			cent.setX(cent.getXValue() + P[i].getXValue() * P[i].getWeight());
			cent.setY(cent.getYValue() + P[i].getYValue() * P[i].getWeight());
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
	
	public static SVMDataItem findCentroid(ArrayList<SVMDataItem> dataset){
		SVMDataItem cent = new SVMDataItem(0, 0);
		
		for (int i = 0; i < dataset.size(); i++){
			cent.setX(cent.getXValue() + dataset.get(i).getXValue() * dataset.get(i).getWeight());
			cent.setY(cent.getYValue() + dataset.get(i).getYValue() * dataset.get(i).getWeight());
		}
		
		double totalWeight = 0;
		for (int i = 0; i < dataset.size(); i++){
			totalWeight += dataset.get(i).getWeight();
		}
		//ASSERT totalWeight != 0;
		
		cent.setX(cent.getXValue() / totalWeight);
		cent.setY(cent.getYValue() / totalWeight);
		
		return cent;
	}
	
}
