package app.model.data;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

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
				t.setClassID(dataset.get(i).getDataClass());
				P.add(t);
			}
		
			DVector[] res = rhull(P, mu);
			
			SVMDataItem p;
			for (int i=0; i< res.length; i++){
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

		DVector[] Ret = null;
		try {
			DVector n = new DVector(-1,0);
			DVector l =  findExtremePoint(P, mu, n);
			n.setX(1);
			DVector r =  findExtremePoint(P, mu,  n);
			
			DVector [] A = rhull_aux(P, l ,r, mu);
			DVector [] B = rhull_aux(P, r, l, mu);
			
			Set<DVector> set = new LinkedHashSet<DVector>();
			set.addAll(Arrays.asList(A));
			set.addAll(Arrays.asList(B));

			Ret = new DVector[set.size()];
			Ret = set.toArray(Ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Ret;
	}
	
	private static DVector[] rhull_aux(ArrayList<DVector> P, 
			DVector l, DVector r, double mu) throws StackOverflowError{
		
		DVector[] Ret = null;
		try {
			DVector n = new DVector(0,0);
			DVector h = new DVector(0,0);
			
			DVector diff = r.subtractVectors(l);
			n.setX(-diff.getY());
			n.setY(diff.getX());

			h =  findExtremePoint(P, mu,  n);
			System.out.format("h:%s,l:%s,r%s \n", h,l,r);

			if (h.equals(l) || h.equals(r)){
				DVector[] res = new DVector[2];
				res[0]= l;
				res[1]= r;
				return res;
			}
			
			DVector nl = new DVector(0,0);
			DVector nr = new DVector(0,0);
			
			diff = h.subtractVectors(l);
			nl.setX(-diff.getY());
			nl.setY(diff.getX());
			
			diff = r.subtractVectors(h);
			nr.setX(-diff.getY());
			nr.setY(diff.getX());
			
			DVector[] supportPoints = new DVector[numSupportPoints];
			for (int i = 0; i < numSupportPoints ; i++){
				supportPoints[i] = Z[i];
			}
			double scalerProjections[] = new double [numSupportPoints];
			double tmp;
			
			for (int i = 0; i < numSupportPoints; i++){
				scalerProjections[i] = nl.getDotProduct(supportPoints[i]);
			}

			DVector[] TMP = new DVector[P.size()];	//too big
			for (int i = 0, k = 0; i < P.size(); i++){
				tmp = nl.getDotProduct(P.get(i));
				for (int j = 0; j < scalerProjections.length; j++){
					if (tmp >= scalerProjections[j]){
						TMP[k] = P.get(i);	//index wrong
						k++;
						break;
					}
				}
			}
			int c = 0;
			for (int i = 0; i < TMP.length; i++){
				if (TMP[i] != null){
					c++;
				}
			}
			ArrayList<DVector> L = new ArrayList<DVector>();
			for (int i = 0; i < TMP.length; i++){
				if (TMP[i] != null){
					L.add(TMP[i]);
				}
			}

			for (int i = 0; i < numSupportPoints; i++){
				scalerProjections[i] = nr.getDotProduct(supportPoints[i]);
			}
			TMP = new DVector[P.size()];
			for (int i = 0, k = 0; i < P.size(); i++){
				tmp = nr.getDotProduct(P.get(i));
				for (int j = 0; j < scalerProjections.length; j++){
					if (tmp >= scalerProjections[j]){
						TMP[k] = P.get(i);	//index wrong
						k++;
						break;
					}
				}
			}
			c = 0;
			for (int i = 0; i < TMP.length; i++){
				if (TMP[i] != null){
					c++;
				}
			}
			ArrayList<DVector> R = new ArrayList<DVector>();
			for (int i = 0; i < TMP.length; i++){
				if (TMP[i] != null){
					R.add(TMP[i]);
				}
			}

			DVector [] A = rhull_aux(L, l ,h, mu);
			DVector [] B = rhull_aux(R, h, r, mu);
			
			Set<DVector> set = new LinkedHashSet<DVector>();
			set.addAll(Arrays.asList(A));
			set.addAll(Arrays.asList(B));

			Ret = new DVector[set.size()];
			Ret = set.toArray(Ret);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		

		return Ret;
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

	private static SVMDataItem normal(SVMDataItem p1, SVMDataItem p2){
			double dx, dy;
			dx = SVMDataItem.dMinus(p1.getXValue(), p2.getXValue());
			dy = SVMDataItem.dMinus(p1.getYValue(), p2.getYValue());
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
