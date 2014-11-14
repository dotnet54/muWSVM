package app.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import app.model.data.WSVMDataItem;
import app.model.data.WSVMDataSeries;


public class WRCHSolver {
	
	private static WSVMDataItem [] Z;
	private static int numSupportPoints;
	
	private static long startTime = 0;
	private static long endTime = 0;
	private static long elapsedTime = 0;
	
	
	/**
	 * External interfaces call this method to solve WRCH
	 * 
	 * @param series SVMDataseries
	 * @param mu reduction parameter
	 * @return list of data items
	 */
	
	public static ArrayList<WSVMDataItem> calcWeightedReducedCHull2(WSVMDataSeries series, double mu) {
		ArrayList<WSVMDataItem> r = new ArrayList<WSVMDataItem>();
		
		try{
			
			if (series == null || series.getItemCount() == 0){
				return null;
			}
			
			//making a copy is slower, rhull function works with an arraylist
			//that is for testing it independent of gui package
			ArrayList<WSVMDataItem> P =  new ArrayList<WSVMDataItem>();
			WSVMDataItem t =null;
			int xDim = series.getXDimension();
			int yDim = series.getYDimension();
			for (int i=0; i< series.getItemCount(); i++){
				t = new WSVMDataItem(0,0);
				t.setX(series.getRawDataItem(i).getVal(xDim));
				t.setY(series.getRawDataItem(i).getVal(yDim));
				t.setWeight(series.getRawDataItem(i).getWeight());
				t.setClassID(series.getRawDataItem(i).getClassID());
				P.add(t);
			}
			
			WSVMDataItem[] res = rhull(P, mu);
			
			WSVMDataItem p;
			for (int i=0;res!=null && i< res.length; i++){
				p = new WSVMDataItem(0,0);
				p.setX(res[i].getXValue());
				p.setY(res[i].getYValue());
				p.setLabel("");
				r.add(p);
			}
		}catch(Exception e){
			e.printStackTrace();
		}catch (StackOverflowError e){
			System.out.println("Stack over flow in WRCH!");
			return null;
		}
		
		return r;
	}
	
	/**
	 * Algorithm to find WRCH
	 * 
	 * @param P an array list of data points
	 * @param mu reduction parameter
	 * @return an array of points in WRCH
	 * 
	 * @throws StackOverflowError
	 */
	private static WSVMDataItem[] rhull(ArrayList<WSVMDataItem> P, double mu) throws StackOverflowError{

		startTime = System.nanoTime();
		
		WSVMDataItem[] Ret = null;
		try {
			WSVMDataItem n = new WSVMDataItem(-1,0);
			WSVMDataItem l =  findExtremePoint(P, mu, n);
			n.setXValue(1);
			WSVMDataItem r =  findExtremePoint(P, mu,  n);
			
			Set<WSVMDataItem> result = rhull_aux(P, l ,r, mu);
			Set<WSVMDataItem> B = rhull_aux(P, r, l, mu);
			result.addAll(B);

			Ret = new WSVMDataItem[result.size()];
			Ret = result.toArray(Ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		System.out.println("Elapsed Time for WRCH: " + elapsedTime / 1e6 +" ms");
		
		return Ret;
	}
	
	/**
	 * private helper method used by rhull
	 * 
	 * @param P
	 * @param l
	 * @param r
	 * @param mu
	 * @return
	 * @throws StackOverflowError
	 */
	private static Set<WSVMDataItem> rhull_aux(ArrayList<WSVMDataItem> P, 
			WSVMDataItem l, WSVMDataItem r, double mu) throws StackOverflowError{
		
		Set<WSVMDataItem> result = new LinkedHashSet<WSVMDataItem>();
		
		try {
			WSVMDataItem n = new WSVMDataItem(0,0);
			WSVMDataItem h = new WSVMDataItem(0,0);
			
			WSVMDataItem diff = r.subtractVectors(l);
			diff.setXValue(DoubleMath.dMinus(r.getXValue(), l.getXValue()));
			diff.setYValue(DoubleMath.dMinus(r.getYValue(), l.getYValue()));
			n.setXValue(-diff.getYValue());
			n.setYValue(diff.getXValue());

			h =  findExtremePoint(P, mu,  n);

			WSVMDataItem facetNormal = l.subtractVectors(r);
			facetNormal.setXValue(DoubleMath.dMinus(l.getXValue(), r.getXValue()));
			facetNormal.setYValue(DoubleMath.dMinus(l.getYValue(), r.getYValue()));
			facetNormal =  facetNormal.get2DAntiClockwiseNormal();
			double pointOffset = h.getDotProduct(facetNormal);
			double facetOffset = l.getDotProduct(facetNormal);
			
			if (Math.abs(pointOffset - facetOffset) < DoubleMath.PRECISION){
				result.add(l);
				result.add(r);
				return result;
			}
			
			
			
			WSVMDataItem nl = new WSVMDataItem(0,0);
			WSVMDataItem nr = new WSVMDataItem(0,0);
			
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
			
			WSVMDataItem[] supportPoints = new WSVMDataItem[numSupportPoints];
			for (int i = 0; i < numSupportPoints ; i++){
				supportPoints[i] = Z[i];
			}
			double scalerProjections[] = new double [numSupportPoints];
			double minOffset;
			ArrayList<WSVMDataItem> L = new ArrayList<WSVMDataItem>();
			ArrayList<WSVMDataItem> R = new ArrayList<WSVMDataItem>();
			
			
			for (int i = 0; i < numSupportPoints; i++){
				scalerProjections[i] = nl.getDotProduct(supportPoints[i]);
			}
			minOffset = minValue(scalerProjections);
			int size = P.size();
			double projection = 0.0;
			for (int i = 0; i < size; i++){
				projection = P.get(i).getDotProduct(nl);
				if (Math.abs(projection - minOffset) < DoubleMath.PRECISION ||
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
				if (Math.abs(projection - minOffset) < DoubleMath.PRECISION || 
						projection > minOffset){
					R.add(P.get(i));
				}
			}
			
			
			result = rhull_aux(L, l ,h, mu);
			Set<WSVMDataItem> B = rhull_aux(R, h, r, mu);
			result.addAll(B);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static WSVMDataItem findExtremePoint(ArrayList<WSVMDataItem> list, double mu, final WSVMDataItem n){
		
		if (mu == 0){ //fp comparison
			return findCentroid(list);
		}

		try {
			reverseSortOnProjections(list, n);
		} catch (Exception e) {
			System.out.println("Comparator exception");
			//e.printStackTrace();
		}

		double [] A = new double[list.size()];
		double s = 0;
		int k = 0;
		
		int count = 0;
		
		while (DoubleMath.isLessThan(s, 1)){
			if (k >= A.length){
				k = 0;
				return findCentroid(list);
			}
				if (A[k] == 0){
					count++;
				}
			A[k] = A[k] + Math.min(list.get(k).getWeight() * mu, 1 - s);
			s += A[k];
			k++;
		}
		
		WSVMDataItem v = new WSVMDataItem(n.getDimensions());
		int i = 0;
		for (i = 0; i < count; i++){
			WSVMDataItem c = list.get(i).clone();
			c.multiplyByScaler(A[i]);
			try {
				v.add(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		numSupportPoints = count;
		Z = new WSVMDataItem[list.size()];
		for (int j = 0; j < list.size(); j++){
			Z[j] = list.get(j); 
		}
		
		return v;
	}

	private static void reverseSortOnProjections(ArrayList<WSVMDataItem> list,final WSVMDataItem normal){
		Collections.sort(list,Collections.reverseOrder( new Comparator<WSVMDataItem>() {
			@Override
			public int compare(WSVMDataItem o1, WSVMDataItem o2) {
				try {
					double p1 = o1.getDotProduct(normal);
					double p2 = o2.getDotProduct(normal);
					double diff = Math.abs(p1 - p2);
					
					double d1 = o1.getWeight();
					double d2 = o2.getWeight();
					double diffWeight =  Math.abs(d1 - d2);
					
					//Known Exception: Comparison violates general contract
					//rare cases when this comparator may not give a total ordering
					//as java expects. this is due to precision erros
					if ((diff - DoubleMath.PRECISION) < 0){
						return 0;
					}else if (p1 > p2){
						return 1;
					}else{
						return -1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}));
	}
	
	private static double minValue(double[] array){
		double min = Double.NaN;
		if (array != null && array.length > 0){
			min = array[0];
			for (int i = 1; i < array.length; i++){
				if (array[i] < min){ //fp comparison
					min = array[i];
				}
			}
		}
		return min;
	}
	

	public static WSVMDataItem findCentroid(WSVMDataItem P[]){
		WSVMDataItem cent = new WSVMDataItem(0, 0);
		
		for (int i = 0; i < P.length; i++){
			cent.setX(cent.getXValue() + P[i].getXValue() * P[i].getWeight());
			cent.setY(cent.getYValue() + P[i].getYValue() * P[i].getWeight());
		}
		
		double totalWeight = 0;
		for (int i = 0; i < P.length; i++){
			totalWeight += P[i].getWeight();
		}
		if (totalWeight == 0){
			totalWeight = 1;
		}
		
		cent.setX(cent.getXValue() / totalWeight);
		cent.setY(cent.getYValue() / totalWeight);
		
		return cent;
	}
	
	public static WSVMDataItem findCentroid(ArrayList<WSVMDataItem> dataset){
		WSVMDataItem cent = new WSVMDataItem(0, 0);
		
		for (int i = 0; i < dataset.size(); i++){
			cent.setX(cent.getXValue() + dataset.get(i).getXValue() * dataset.get(i).getWeight());
			cent.setY(cent.getYValue() + dataset.get(i).getYValue() * dataset.get(i).getWeight());
		}
		
		double totalWeight = 0;
		for (int i = 0; i < dataset.size(); i++){
			totalWeight += dataset.get(i).getWeight();
		}
		if (totalWeight == 0){
			totalWeight = 1;
		}
		
		cent.setX(cent.getXValue() / totalWeight);
		cent.setY(cent.getYValue() / totalWeight);
		
		return cent;
	}
	
}
