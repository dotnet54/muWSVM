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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ode.EquationsMapper;

import app.model.data.DataPoint;

//

/*
 * 
 * RECURSION TOO DEEP IF P IS IN STACK, USE CLASS STATIC
 * 
 */
public class RHull {
	
	public static class DP extends DataPoint {
		double sp = 0;
		
		public DP(double x, double y) {
			super(x, y);
		}
		public boolean equals(Object obj){
			DP d= (DP) obj;
			if (this.x == d.x && this.y == d.y){
				return true;
			}else{
				return false;
			}
		}
	}
	
	static DP [] Z;
	static double weights[];
	static ArrayList<DP> PP;

	public static void main(String[] args) {
		int np = 3;
		DP[] points = new DP[np];
		DP[] wh;
		
		points[0] = new DP(0,0);
		points[1] =  new DP(1,0);
		points[2] = new DP(0, 1);
		
		PP = new ArrayList<DP>();
		for(int i = 0; i < points.length; i++){
			PP.add(i,points[i]);
		}
		
		weights = new double[points.length];
		for(int i = 0; i < points.length; i++){
			weights[i] = 1;
		}
		weights[0] = 2;
		wh = rhull(points, (double)0.5);
		for (int i = 0; i < wh.length; i++){
			System.out.println("wh Point  = " + wh[i]);
		}
	}
	

	
	public static ArrayList<Point> rhull(ArrayList<Point> dataset1, double mu) {
		
		DP[] P =  new DP[dataset1.size()];
		DP t =null;
		PP = new ArrayList<DP>();
		weights = new double[dataset1.size()];
		for (int i=0; i< dataset1.size(); i++){
			t = new DP(0,0);
			t.x =  dataset1.get(i).x;
			t.y =  dataset1.get(i).y;
			P[i] = t;
			PP.add(i,t);
			weights[i] = 1;
		}
		weights[0]=2;
		
		
		DP[] res = rhull(P, mu);
		
		ArrayList<Point> r = new ArrayList<Point>();
		Point p;
		for (int i=0; i< res.length; i++){
			p = new Point(0,0);
			p.x =  (int) res[i].x;
			p.y =  (int) res[i].y;
			r.add(p);
		}
		//r.addAll(Arrays.asList(res));
		
		return r;
	}
	
	
	public static DP[] rhull(DP[] P, double mu){
		DP n = new DP(-1,0);
		//DP l = theorem32(P, n, mu);
		DP l =  alg8(PP, weights, mu, n);
		n.x = 1;
		//DP r = theorem32(P, n, mu);
		DP r =  alg8(PP, weights, mu,  n);
		
		DP [] A = rhull_h(P, l ,r, mu);
		DP [] B = rhull_h(P, r, l, mu);
		
		Set<DP> set = new HashSet<DP>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		DP[] Ret = new DP[set.size()];
		Ret = set.toArray(Ret);
		
		return Ret;
	}
	
	public static DP[] rhull_h(DP[] P, DP l, DP r, double mu){
		DP n = new DP(0,0);
		DP h = new DP(0,0);
		
		n = normal(r, l);
		//h = theorem32(P, n, mu);
		h =  alg8(PP, weights, mu,  n);
		
		if (h.x == l.x && h.y == l.y || 
				h.x == r.x && h.y == r.y ){
			DP[] res = new DP[2];
			res[0]= l;
			res[1]= r;
			return res;
		}
		
		DP nl = new DP(0,0);
		DP nr = new DP(0,0);
		nl = normal(h, l);
		nr = normal(r, h);
		
		int m = (int) Math.ceil(1.0/mu);
		DP[] S = new DP[m];
		for (int i = 0; i < m ; i++){
			S[i] = Z[i];
		}
		double sc[] = new double [m];
		double tmp;
		
		for (int i = 0; i < m; i++){
			sc[i] = nl.x * S[i].x + nl.y * S[i].y;
		}

		DP[] TMP = new DP[P.length];	//too big
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nl.x * P[i].x + nl.y * P[i].y;
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
		DP[] L = new DP [c];
		for (int i = 0; i < TMP.length; i++){
			if (TMP[i] != null){
				L[i] = TMP[i];
			}
		}

		for (int i = 0; i < m; i++){
			sc[i] = nr.x * S[i].x + nr.y * S[i].y;
		}
		TMP = new DP[P.length];
		for (int i = 0, k = 0; i < P.length; i++){
			tmp = nr.x * P[i].x + nr.y * P[i].y;
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
		DP[] R = new DP [c];
		for (int i = 0; i < TMP.length; i++){
			if (TMP[i] != null){
				R[i] = TMP[i];
			}
		}


		
		DP [] A = rhull_h(L, l ,h, mu);
		DP [] B = rhull_h(R, h, r, mu);
		
		Set<DP> set = new HashSet<DP>();
		set.addAll(Arrays.asList(A));
		set.addAll(Arrays.asList(B));

		DP[] Ret = new DP[set.size()];
		Ret = set.toArray(Ret);
		

		return Ret;
	}
	
	public static DP normal(DP p1, DP p2){
		double dx, dy;
		dx = p1.x - p2.x;
		dy = p1.y - p2.y;
		
		DP n = new DP(0,0);
		n.x = -dy;
		n.y = dx;

		return n;
	}
	
	
	public static DP theorem32(DP[] P, DP n, double mu){
		DP v = new DP(0,0);
		Z = new DP[P.length];
		double[] s = new double[P.length];	//scaler projection
		int[] ind = new int[P.length];
		double max;
		int maxi;
		double tmp;
		int i, j, k;

		for (i = 0; i < P.length; i++){
			s[i] = P[i].x * n.x + P[i].y * n.y;
		}
		
		ind = sorti(s);
		
		for (i = 0; i < P.length; i++){
			Z[i] = P[ind[i]];
		}
		
		int m = (int) Math.ceil(1.0/mu);
		
		for (i = 0; i <= (m-1)-1; i++){
			v.x += mu * Z[i].x;
		}
		v.x += (1 - (m-1)*mu)* Z[(m-1)].x;
		
		for (i = 0; i <= (m-1)-1; i++){
			v.y += mu * Z[i].y;
		}
		v.y += (1 - (m-1)*mu)* Z[(m-1)].y;
		
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
	
	
	
	
	
	private static Map sortByComparator(Map unsortMap) {
		 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	public static DP argmax(DP[] P, DP n, double mu){
		DP[] points = getpoints(P);
		double r = 0;
		double max = 0;
		DP maxPoint = null;
		for (int i = 0; i < points.length; i++){
			r = n.x * points[i].x + n.y * points[i].y;
			if (max < r){
				max = r;
				maxPoint = points[i];
			}
		}
		return maxPoint;
	}
	
	private static DP[] getpoints(DP[] p) {
		return p;
	}
	
	
	
	
	public static DP alg8(ArrayList<DP> Pl, double [] S,
			double mu, DP n){
		
		ArrayList<DP> P = new ArrayList<RHull.DP>(Pl);
		
		DP v = new DP(0,0);
		double [] A = new double[P.size()];
		double [] dots = new double[P.size()];
		int [] sortedInd = new int[P.size()];
		ArrayList<Double> sc = new ArrayList<Double>();
		
		Z = new DP[P.size()];
		int ind[] = new int[P.size()]; 	//larger than need
		
		
		double s = 0.0;
		int i = 0;
		
		for (i = 0; i < A.length; i++){
			A[i] = 0;	//redundant java initializes to 0
		}
		
		for (i = 0; i < P.size(); i++){
			dots[i] = n.x * P.get(i).x + n.y * P.get(i).y;
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
			v.x +=  (A[sortedInd[i]] * P.get(sortedInd[i]).x);
			//System.out.println(A[sortedInd[i]] * P.get(sortedInd[i]).y);
			v.y += (A[sortedInd[i]] * P.get(sortedInd[i]).y);
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
