package app.model.algorithms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import app.model.data.SVMDataItem;

//

/*
 * 
 * RECURSION TOO DEEP IF P IS IN STACK, USE CLASS STATIC
 * 
 */
public class WRCH {
	
	
	private static SVMDataItem [] Z;
	private static int numSupportPoints;	//varies depending on weight
	private static double weights[];
	
	
	public static ArrayList<SVMDataItem> calcWeightedReducedCHull(ArrayList<SVMDataItem> dataset, double mu) {
		
		ArrayList<SVMDataItem> r = new ArrayList<SVMDataItem>();
		
		double m = 1.0/mu;
//		if (m >= dataset.size()){//TODO double comparison error if m=4.000001 ??check
//			System.out.println("calcReducedCHull: m (or ceil(1/mu)) must be greater than number of points");
//			r.add(findCentroid(dataset));
//			return r;
//		}
//		
		
		SVMDataItem[] P =  new SVMDataItem[dataset.size()];
		SVMDataItem t =null;
		//PP = new ArrayList<SVMDataItem>();
		weights = new double[dataset.size()];
		for (int i=0; i< dataset.size(); i++){
			t = new SVMDataItem(0,0);
			t.setX(dataset.get(i).getX());
			t.setY(dataset.get(i).getY());
			t.setWeight(dataset.get(i).getWeight());
			t.setClassID(dataset.get(i).getClassID());
			P[i] = t;
			//PP.add(i,t);
			weights[i] = dataset.get(i).getWeight();
		}
		//weights[0]=2;
		
		try{
			SVMDataItem[] res = rhull(P, mu);

		
		
		
		SVMDataItem p;
		for (int i=0; i< res.length; i++){
			p = new SVMDataItem(0,0);
			p.setX(res[i].getXValue());
			p.setY(res[i].getYValue());
			p.setLabel("");
			r.add(p);
		}
		//r.addAll(Arrays.asList(res));
		
		}catch (StackOverflowError e){
			System.out.println("Stack over flow in RCH!");
			return null;
		}
		
		return r;
	}
	
	
	private static SVMDataItem[] rhull(SVMDataItem[] P, double mu) throws StackOverflowError{
		
//		int m = (int) Math.ceil(1.0/mu);
//		if (m > P.length){
//			SVMDataItem c = findCentroid(P);
//			SVMDataItem res[] = new SVMDataItem[1];
//			res[0] = c;
//			return res;
//		}
		
		SVMDataItem n = new SVMDataItem(-1,0);
		//DP l = theorem32(P, n, mu);
		SVMDataItem l =  alg9(P, weights, mu, n);
		n.setX(1);
		//DP r = theorem32(P, n, mu);
		SVMDataItem r =  alg9(P, weights, mu,  n);
		
		SVMDataItem [] A = rhull_aux(P, l ,r, mu);
		SVMDataItem [] B = rhull_aux(P, r, l, mu);
		
		Set<SVMDataItem> set = new LinkedHashSet<SVMDataItem>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		SVMDataItem[] Ret = new SVMDataItem[set.size()];
		Ret = set.toArray(Ret);
		
		return Ret;
	}
	
	private static SVMDataItem[] rhull_aux(SVMDataItem[] P, 
			SVMDataItem l, SVMDataItem r, double mu) throws StackOverflowError{
		SVMDataItem n = new SVMDataItem(0,0);
		SVMDataItem h = new SVMDataItem(0,0);
		
		n = normal(r, l);
		//h = theorem32(P, n, mu);
		h =  alg9(P, weights, mu,  n);
		
		//System.out.format("h:%s \n", h.toFormatedString(2));
		
		
		if (h.equals(l) || h.equals(r)){//TODO double op
			SVMDataItem[] res = new SVMDataItem[2];
			res[0]= l;
			res[1]= r;
			return res;
		}
		
		SVMDataItem nl = new SVMDataItem(0,0);
		SVMDataItem nr = new SVMDataItem(0,0);
		nl = normal(h, l);		//left partition normal
		nr = normal(r, h);		//right partition normal
		
		SVMDataItem[] supportPoints = new SVMDataItem[numSupportPoints];
		for (int i = 0; i < numSupportPoints ; i++){
			supportPoints[i] = Z[i];
		}
		double scalerProjections[] = new double [numSupportPoints];
		double tmp;
		
		for (int i = 0; i < numSupportPoints; i++){
			scalerProjections[i] = nl.getDotProduct(supportPoints[i]);//TODO double op
		}

		SVMDataItem[] TMP = new SVMDataItem[P.length];	//too big
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nl.getDotProduct(P[i]);
			for (int j = 0; j < scalerProjections.length; j++){
//				if (tmp >= scalerpro[j]){//TODO double op
				if (DoubleMath.isGreaterThanEq(tmp, scalerProjections[j])){
					TMP[k] = P[i];	//index wrong
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
		SVMDataItem[] L = new SVMDataItem [c];
		for (int i = 0; i < TMP.length; i++){
			if (TMP[i] != null){
				L[i] = TMP[i];
			}
		}

		for (int i = 0; i < numSupportPoints; i++){
			scalerProjections[i] = nr.getDotProduct(supportPoints[i]);
		}
		TMP = new SVMDataItem[P.length];
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nr.getDotProduct(P[i]);
			for (int j = 0; j < scalerProjections.length; j++){
//				if (tmp >= scalerpro[j]){ //TODO double op
				if (DoubleMath.isGreaterThanEq(tmp, scalerProjections[j])){
					TMP[k] = P[i];	//index wrong
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
		SVMDataItem[] R = new SVMDataItem [c];
		for (int i = 0; i < TMP.length; i++){
			if (TMP[i] != null){
				R[i] = TMP[i];
			}
		}


		
		SVMDataItem [] A = rhull_aux(L, l ,h, mu);
		SVMDataItem [] B = rhull_aux(R, h, r, mu);
		
		Set<SVMDataItem> set = new LinkedHashSet<SVMDataItem>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		SVMDataItem[] Ret = new SVMDataItem[set.size()];
		Ret = set.toArray(Ret);
		

		return Ret;
	}
	
	public static SVMDataItem alg9(SVMDataItem[] P, double[] S, double mu, SVMDataItem n){
		//TODO S not used
		
		//TODO if mu is zero return centroid of P
		if (DoubleMath.isEqual(mu, 0)){
			return findCentroid(P);
		}
		
		//TODO if weight = 0 ??
		ArrayList<SVMDataItem> X = new ArrayList<SVMDataItem>(Arrays.asList(P));
		
		//TODO debug code ->connect n with comparator in a dirty way
		double [] dots = new double[X.size()];
		for (int i = 0; i < X.size(); i++){
			dots[i] = X.get(i).project(n);
		}
		
		Collections.sort(X,Collections.reverseOrder( new Comparator<SVMDataItem>() {
			@Override
			public int compare(SVMDataItem o1, SVMDataItem o2) {
				//USE normal to compare between two objects
				return (o1.custCompareTo(o2));
			}
		}));
		
		
		double [] A = new double[X.size()];
		double s = 0;
		int k = 0;
		
		int count = 0;
		
		while (DoubleMath.isLessThan(s, 1.0)){
			if (k >= A.length){
				System.out.println("k fixed");
				k = 0;
				return findCentroid(P); //TODO if all A are used then return centroid
			}
//			for (k = 0; k < X.size(); k++){
				if (A[k] == 0){
//					
					count++;
//					break;
				}
//			}		
			//last k will be index out of bounds so

			
			
			A[k] = A[k] + Math.min(X.get(k).getWeight() * mu, 1 - s);
			s += A[k];
			k++;
		}
		
		SVMDataItem v = new SVMDataItem(0,0);
		int i = 0;
		for (i = 0; i < count; i++){
			if (i >=  X.size()){
				System.out.println("index out of bounds");
			}

			v.setX(DoubleMath.dAdd(v.getXValue(), DoubleMath.dMult(A[i], X.get(i).getXValue())));
			v.setY(DoubleMath.dAdd(v.getYValue(), DoubleMath.dMult(A[i], X.get(i).getYValue())));
	
		}
		
	
		numSupportPoints = count;
		
		//TODO redundant
		Z = new SVMDataItem[X.size()];
		for (int j = 0; j < X.size(); j++){
			Z[j] = X.get(j); //copy support points
		}
		
		return v;
	}
	
	
	
	
	public static SVMDataItem findCentroid(ArrayList<SVMDataItem> P){
		SVMDataItem cent = new SVMDataItem(0, 0);
		
		for (int i = 0; i < P.size(); i++){
			cent.setX(cent.getX() + P.get(i).getXValue() * P.get(i).getWeight());
			cent.setY(cent.getY() + P.get(i).getYValue() * P.get(i).getWeight());
		}
		
		double totalWeight = 0;
		for (int i = 0; i < P.size(); i++){
			totalWeight += P.get(i).getWeight();
		}
		//ASSERT totalWeight != 0;
		
		cent.setX(cent.getXValue() / totalWeight);
		cent.setY(cent.getYValue() / totalWeight);
		
		return cent;
	}
	
	private static SVMDataItem normal(SVMDataItem p1, SVMDataItem p2){
			double dx, dy;
	//		dx = p1.getXValue() - p2.getXValue(); //TODO double op
	//		dy = p1.getYValue() - p2.getYValue(); //TODO double op
			dx = DoubleMath.dMinus(p1.getXValue(), p2.getXValue());
			dy = DoubleMath.dMinus(p1.getYValue(), p2.getYValue());
			
			SVMDataItem n = new SVMDataItem(0,0);
			n.setX(-dy);
			n.setY(dx);
	
			return n;
		}


	//TODO FIND GEOMETRIC centroid using weights
	// use double op functions
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
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */


	private static SVMDataItem alg8(SVMDataItem[] Pl, double [] S,
				double mu, SVMDataItem n){
			
	//		int m = (int) Math.ceil(1.0/mu);
	//		if (m > Pl.length){
	//			System.out.println("alg8: m (or ceil(1/mu)) must be greater than number of points");
	//			return null;
	//		}
			
			
			ArrayList<SVMDataItem> P = new ArrayList<SVMDataItem>(Arrays.asList(Pl));
			//TODO NULL
			SVMDataItem v = new SVMDataItem(0,0);
			double [] A = new double[P.size()];
			double [] dots = new double[P.size()];
			int [] sortedInd = new int[P.size()];
			Z = new SVMDataItem[P.size()];
			double s = 0.0;
			int i = 0;
			
			for (i = 0; i < P.size(); i++){
				dots[i] = n.getDotProduct(P.get(i));
			}
			
	
			sortedInd = sorti(dots);	
			for (i = 0; i < P.size(); i++){
				Z[i] = P.get(sortedInd[i]);
			}
			
			i = 0;
			//int k = 0;//kth maximum
			int count = 0;
			while ((1 - s) >0.000001 ){//TODO double op
				for (int k = 0; k < sortedInd.length; k++){
					if (A[sortedInd[k]] == 0){
						i = sortedInd[k];
						count++;
						break;
					}
				}
				A[i] = Math.min(S[i] * mu, 1 - s); //if s[i] = 0?? TEST
				s = s + A[i];
			}
			int gg = 4;
			
			for (i = 0; i < count; i++){
				
				if (i >= P.size()){
					System.out.println("index out of bounds");
				}
				//TODO double op
	//			v.setX(v.getXValue() + (A[sortedInd[i]] * P.get(sortedInd[i]).getXValue()));
	//			v.setY(v.getYValue() + (A[sortedInd[i]] * P.get(sortedInd[i]).getYValue()));
				v.setX(DoubleMath.dAdd(v.getXValue(),
						DoubleMath.dMult(A[sortedInd[i]], 
								P.get(sortedInd[i]).getXValue())));
				
				v.setY(DoubleMath.dAdd(v.getYValue(),
						DoubleMath.dMult(A[sortedInd[i]], 
								P.get(sortedInd[i]).getYValue())));
		
			}
			
			numSupportPoints = count;
			return v;
		}


	private static int[] sorti(double[] s){
		double max;
		int maxi;
		double tmp;
		int i, j, k;
		int[] ind = new int[s.length];
		
		
		max = s[0];
		maxi  = 0;
		int n = s.length;
		for (j = 0,k = 0; j < n; j++,k++){
			max =-1e8;
			for (i = 0; i < n; i++){
				System.out.println((max - s[i]));
				if (!Double.isNaN(s[i]) && ((max - s[i]) < -0.001)){//TODO double op
					max = s[i];
					maxi = i;
				}
			}
			s[maxi] = Double.NaN;
			ind[k] = maxi;
		}
		
		return ind;
	}


	private static SVMDataItem theorem32(SVMDataItem[] P, SVMDataItem n, double mu){
		SVMDataItem v = new SVMDataItem(0,0);
		Z = new SVMDataItem[P.length];
		double[] s = new double[P.length];	//scaler projection
		int[] ind = new int[P.length];
		double max;
		int maxi;
		double tmp;
		int i, j, k;
	
		for (i = 0; i < P.length; i++){
			s[i] = P[i].getDotProduct(n);
		}
		
		ind = sorti(s);
		
		for (i = 0; i < P.length; i++){
			Z[i] = P[ind[i]];
		}
		
		int m = (int) Math.ceil(1.0/mu);
		
		for (i = 0; i <= (m-1)-1; i++){
			v.setX(v.getXValue() + mu * Z[i].getXValue());
		}
		v.setX(v.getXValue() + (1 - (m-1)*mu)* Z[(m-1)].getXValue());
		
		for (i = 0; i <= (m-1)-1; i++){
			v.setY(v.getYValue() + mu * Z[i].getYValue());
		}
		v.setY(v.getYValue() + (1 - (m-1)*mu)* Z[(m-1)].getYValue());
		
		//S = 
		return v;
	}


	
	
	
	
	
	
	
	
	
}
