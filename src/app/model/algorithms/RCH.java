package app.model.algorithms;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ode.EquationsMapper;

import app.model.data.DataPoint;
import app.model.data.SVMDataItem;

//

/*
 * 
 * RECURSION TOO DEEP IF P IS IN STACK, USE CLASS STATIC
 * 
 */
public class RCH {
	
	
	static SVMDataItem [] Z;
	static double weights[];
	static ArrayList<SVMDataItem> PP;

	public static void main(String[] args) {
		int np = 5;
		SVMDataItem[] points = new SVMDataItem[np];
		SVMDataItem[] wh;
		
//		points[0] = new DP(0,0);
//		points[1] =  new DP(1,0);
//		points[2] = new DP(0, 1);
//		points[3] = new DP(1, 1);
//		points[4] = new DP(1, 2);
		
		
		points[0] = new SVMDataItem(0,0);
		points[1] =  new SVMDataItem(100, 0);
		points[2] = new SVMDataItem(184, 40);
		points[3] = new SVMDataItem(109, 103);
		points[4] = new SVMDataItem(0, 100);
		
		PP = new ArrayList<SVMDataItem>();
		for(int i = 0; i < points.length; i++){
			PP.add(i,points[i]);
		}
		
		weights = new double[points.length];
		for(int i = 0; i < points.length; i++){
			weights[i] = 1;
		}
		weights[0] = 0.5;
		wh = rhull(points, (double)1);
		for (int i = 0; i < wh.length; i++){
			System.out.println("wh Point  = " + wh[i]);
		}
	}
	

	
	public static ArrayList<SVMDataItem> rhull(ArrayList<SVMDataItem> dataset1, double mu) {
		
		SVMDataItem[] P =  new SVMDataItem[dataset1.size()];
		SVMDataItem t =null;
		PP = new ArrayList<SVMDataItem>();
		weights = new double[dataset1.size()];
		for (int i=0; i< dataset1.size(); i++){
			t = new SVMDataItem(0,0);
			t.setX(dataset1.get(i).getX());
			t.setY(dataset1.get(i).getY());
			P[i] = t;
			PP.add(i,t);
			weights[i] = 1;
		}
		//weights[0]=2;
		
		
		SVMDataItem[] res = rhull(P, mu);
		
		ArrayList<SVMDataItem> r = new ArrayList<SVMDataItem>();
		SVMDataItem p;
		for (int i=0; i< res.length; i++){
			p = new SVMDataItem(0,0);
			p.setX(res[i].getXValue());
			p.setY(res[i].getYValue());
			r.add(p);
		}
		//r.addAll(Arrays.asList(res));
		
		return r;
	}
	
	
	public static SVMDataItem[] rhull(SVMDataItem[] P, double mu){
		SVMDataItem n = new SVMDataItem(-1,0);
		//DP l = theorem32(P, n, mu);
		SVMDataItem l =  alg8(PP, weights, mu, n);
		n.setX(1);
		//DP r = theorem32(P, n, mu);
		SVMDataItem r =  alg8(PP, weights, mu,  n);
		
		SVMDataItem [] A = rhull_h(P, l ,r, mu);
		SVMDataItem [] B = rhull_h(P, r, l, mu);
		
		Set<SVMDataItem> set = new LinkedHashSet<SVMDataItem>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		SVMDataItem[] Ret = new SVMDataItem[set.size()];
		Ret = set.toArray(Ret);
		
		return Ret;
	}
	
	public static SVMDataItem[] rhull_h(SVMDataItem[] P, SVMDataItem l, SVMDataItem r, double mu){
		SVMDataItem n = new SVMDataItem(0,0);
		SVMDataItem h = new SVMDataItem(0,0);
		
		n = normal(r, l);
		//h = theorem32(P, n, mu);
		h =  alg8(PP, weights, mu,  n);
		
		if (h.equals(l) || h.equals(r)){
			SVMDataItem[] res = new SVMDataItem[2];
			res[0]= l;
			res[1]= r;
			return res;
		}
		
		SVMDataItem nl = new SVMDataItem(0,0);
		SVMDataItem nr = new SVMDataItem(0,0);
		nl = normal(h, l);
		nr = normal(r, h);
		
		int m = (int) Math.ceil(1.0/mu);
		SVMDataItem[] S = new SVMDataItem[m];
		for (int i = 0; i < m ; i++){
			S[i] = Z[i];
		}
		double sc[] = new double [m];
		double tmp;
		
		for (int i = 0; i < m; i++){
			sc[i] = nl.getXValue() * S[i].getXValue() 
			+ nl.getYValue() * S[i].getYValue();
		}

		SVMDataItem[] TMP = new SVMDataItem[P.length];	//too big
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nl.getXValue() * P[i].getXValue() 
			+ nl.getYValue() * P[i].getYValue();
			for (int j = 0; j < sc.length; j++){
				if (tmp >= sc[j]){
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

		for (int i = 0; i < m; i++){
			sc[i] = nr.getXValue() * S[i].getXValue() 
			+ nr.getYValue() * S[i].getYValue();
		}
		TMP = new SVMDataItem[P.length];
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nr.getDotProduct(P[i]);
			for (int j = 0; j < sc.length; j++){
				if (tmp >= sc[j]){
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


		
		SVMDataItem [] A = rhull_h(L, l ,h, mu);
		SVMDataItem [] B = rhull_h(R, h, r, mu);
		
		Set<SVMDataItem> set = new LinkedHashSet<SVMDataItem>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		SVMDataItem[] Ret = new SVMDataItem[set.size()];
		Ret = set.toArray(Ret);
		

		return Ret;
	}
	
	public static SVMDataItem normal(SVMDataItem p1, SVMDataItem p2){
		double dx, dy;
		dx = p1.getXValue() - p2.getXValue();
		dy = p1.getYValue() - p2.getYValue();
		
		SVMDataItem n = new SVMDataItem(0,0);
		n.setX(-dy);
		n.setY(dx);

		return n;
	}
	
	
	public static SVMDataItem theorem32(SVMDataItem[] P, SVMDataItem n, double mu){
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
	
	
	
	
	public static int[] sorti(double[] s){
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
				if (!Double.isNaN(s[i]) && max < s[i]){
					max = s[i];
					maxi = i;
				}
			}
			s[maxi] = Double.NaN;
			ind[k] = maxi;
		}
		
		return ind;
	}
	
	
	
	

	
	
	public static SVMDataItem argmax(SVMDataItem[] P, SVMDataItem n, double mu){
		SVMDataItem[] points = getpoints(P);
		double r = 0;
		double max = 0;
		SVMDataItem maxPoint = null;
		for (int i = 0; i < points.length; i++){
			r = n.getDotProduct(points[i]);
			if (max < r){
				max = r;
				maxPoint = points[i];
			}
		}
		return maxPoint;
	}
	
	private static SVMDataItem[] getpoints(SVMDataItem[] p) {
		return p;
	}
	
	
	
	
	public static SVMDataItem alg8(ArrayList<SVMDataItem> Pl, double [] S,
			double mu, SVMDataItem n){
		
		ArrayList<SVMDataItem> P = new ArrayList<SVMDataItem>(Pl);
		
		SVMDataItem v = new SVMDataItem(0,0);
		double [] A = new double[P.size()];
		double [] dots = new double[P.size()];
		int [] sortedInd = new int[P.size()];
		ArrayList<Double> sc = new ArrayList<Double>();
		
		Z = new SVMDataItem[P.size()];
		int ind[] = new int[P.size()]; 	//larger than need
		
		
		double s = 0.0;
		int i = 0;
		
		for (i = 0; i < A.length; i++){
			A[i] = 0;	//redundant java initializes to 0
		}
		
		for (i = 0; i < P.size(); i++){
			dots[i] = n.getDotProduct(P.get(i));
			//sc.add(i, (double) (n.x * P.get(i).x + n.y * P.get(i).y));
		}
		
		//Collections.sort(sc);
		sortedInd = sorti(dots);	
		for (i = 0; i < P.size(); i++){
			Z[i] = P.get(sortedInd[i]);
		}
		
		i = 0;
		int k = 0;
		while ((1 - s) >0.000001 ){
			for (; k < sortedInd.length; k++){
				if (A[sortedInd[k]] == 0){
					i = sortedInd[k];
					break;
				}
			}
			A[i] = Math.min(S[i] * mu, 1 - s); //if s[i] = 0?? TEST
			s = s + A[i];
		}
		
		for (i = 0; i <= k; i++){
			v.setX(v.getXValue() +
				(A[sortedInd[i]] * P.get(sortedInd[i]).getXValue()));
			//System.out.println(A[sortedInd[i]] * P.get(sortedInd[i]).y);
			v.setY(v.getYValue() + 
				(A[sortedInd[i]] * P.get(sortedInd[i]).getYValue()));
		}
		return v;
	}
	
	
	public static int extractMax(ArrayList<Double> list){
		
		int maxi = 0;
		Double max = null; // = list.get(maxi);
		
		for (int i = 0; i < list.size(); i++){
			System.out.println(max + " " +(max < list.get(i)) + " " + list.get(i));
			
				if (max == null && Double.isNaN(list.get(i))){
					max = list.get(i);
				}
				
				if (max < list.get(i)){
					max = list.get(i);
					maxi = i;
				}
			
		}
		
		//list.remove(maxi);
		list.set(maxi, Double.NaN);
		return maxi;
	}


	
	
	
	
	
	
	
	
	
}
