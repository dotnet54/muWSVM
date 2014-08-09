package app.model.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.model.data.DataPoint;


public class RCH {
	
	public static double [] weights;
	public static Point normal;

	//function vert = qrh(X, mu, l, r)
	public static ArrayList<Point> qrh(ArrayList<Point> X, double mu, 
			ArrayList<Point> l, ArrayList<Point> r,
			boolean first){

		//nfl = floor(1/mu);
		//n = ceil(1/mu);
		
		int nfl = (int) Math.floor(1/mu);
		int n = (int) Math.ceil(1/mu);
		
		if (X.size() == nfl){
			System.out.println("Hull reduced to center " + findCentroid(X));
			ArrayList<Point> res = new ArrayList<Point>();
			res.add(findCentroid(X));
			return  res;
		}
		
		
		ArrayList<Point> h = new ArrayList<Point>();
		
		if (first == true){
			//[npts ndims] = size(X);
			int npts = X.size();
			int ndims = 2;
			
			//[abscissa i] = sort(X(:,1));
			int[] i = sortmin(X);
			
		    //l = X(i(1:n),:);
		    //h = X(i(npts:-1:(npts-nfl)),:);
		    //r = l; 
			l = new ArrayList<Point>();
			for (int a = 0; a < n; a++){
				l.add(X.get(i[a]));
			}
			for (int a = npts - 1; a >= (npts - nfl - 1); a--){
				h.add(X.get(i[a]));
			}
			r = (ArrayList<Point>) l.clone();

			
			normal = new Point(-1,0);
			Point hpt = new Point(0,0);
			hpt = alg7(X, weights, mu, normal);
			
			normal = new Point(1,0);
			Point lpt = new Point(0,0);
			lpt = alg7(X, weights, mu, normal);
			
			normal = new Point(-1,0);
			Point rpt = new Point(0,0);
			rpt = alg7(X, weights, mu, normal);
			
			
		}else{
			 //h = X(1:n,:);
			for (int a = 0; a < n; a++){
				h.add(X.get(a));
			}
		}
		
		
		
		//hpt = mu*sum(h(1:nfl,:),1) + (1-mu*nfl)*h(n,:);
		//lpt = mu*sum(l(1:nfl,:),1) + (1-mu*nfl)*l(n,:);
		//rpt = mu*sum(r(1:nfl,:),1) + (1-mu*nfl)*r(n,:);
		
		///Point lh = new Point(hpt.x-lpt.x, hpt.y-lpt.y);
		//Point hr = new Point(rpt.x-hpt.x, rpt.y-hpt.y);
		//Point wlh = new Point(-lh.y, lh.x);
		//Point whr = new Point(-hr.y, hr.x);
		
		
		normal = new Point(-1,0);
		Point hpt = new Point(0,0);
		hpt = alg7(X, weights, mu, normal);
		
		normal = new Point(1,0);
		Point lpt = new Point(0,0);
		lpt = alg7(X, weights, mu, normal);
		
		normal = new Point(-1,0);
		Point rpt = new Point(0,0);
		rpt = alg7(X, weights, mu, normal);
		
		/*
		
		Point hpt = new Point(0,0);
		double sumX = 0, sumY = 0;
		for (int a = 0; a < nfl; a++){
			sumX += h.get(a).x;
		}
		for (int a = 0; a < nfl; a++){
			sumY += h.get(a).y;
		}
		hpt.x = (int) (mu * sumX);	//use double Point
		hpt.y = (int) (mu * sumY);
		
		hpt.x += (1-mu*nfl) * h.get(n - 1).x;
		hpt.y += (1-mu*nfl) * h.get(n - 1).y;
		
		
		Point lpt = new Point(0,0);
		sumX = 0; sumY = 0;
		for (int a = 0; a < nfl; a++){
			sumX += l.get(a).x;
		}
		for (int a = 0; a < nfl; a++){
			sumY += l.get(a).y;
		}
		lpt.x = (int) (mu * sumX);	//use double Point
		lpt.y = (int) (mu * sumY);
		
		lpt.x += (1-mu*nfl) * l.get(n - 1).x;
		lpt.y += (1-mu*nfl) * l.get(n - 1).y;
		
		
		Point rpt = new Point(0,0);
		sumX = 0; sumY = 0;
		for (int a = 0; a < nfl; a++){
			sumX += r.get(a).x;
		}
		for (int a = 0; a < nfl; a++){
			sumY += r.get(a).y;
		}
		rpt.x = (int) (mu * sumX);	//use double Point
		rpt.y = (int) (mu * sumY);
		
		rpt.x += (1-mu*nfl) * r.get(n - 1).x;
		rpt.y += (1-mu*nfl) * r.get(n - 1).y;	
		*/
		
		
		
		//if(norm(hpt - lpt) < 1e-12 || norm(hpt - rpt) < 1e-12)
		//   vert = [lpt; rpt];
		//    return;
		//end
		ArrayList<Point> result = new ArrayList<Point>();
		if ( Math.abs((hpt.x - lpt.x)) < 1e-12 &&  Math.abs((hpt.y - lpt.y)) < 1e-12
		  || Math.abs((hpt.x - rpt.x)) < 1e-12 &&  Math.abs((hpt.y - rpt.y)) < 1e-12){
			result.add(lpt);
			result.add(rpt);
			return result;
		}
		
		//lh = hpt - lpt;
		//hr = rpt - hpt;
		Point lh = new Point(hpt.x-lpt.x, hpt.y-lpt.y);
		Point hr = new Point(rpt.x-hpt.x, rpt.y-hpt.y);
		
		//wlh = [-lh(2); lh(1)];
		//whr = [-hr(2); hr(1)];
		Point wlh = new Point(-lh.y, lh.x);
		Point whr = new Point(-hr.y, hr.x);
		
		
		
		//projlh = [l; h] * wlh;
		//projhr = [h; r] * whr;
		
		//lthresh = min(projlh);
		//rthresh = min(projhr);

		//projlh = X * wlh;
		//projhr = X * whr;
		
		ArrayList<Integer> projlh = new ArrayList<Integer>();
		ArrayList<Integer> projhr = new ArrayList<Integer>();
		for (int a = 0; a < X.size(); a++){
			Integer e = new Integer (X.get(a).x * wlh.x + X.get(a).y *  wlh.y);
			projlh.add(e);
		}
		for (int a = 0; a < X.size(); a++){
			Integer e = new Integer (X.get(a).x * whr.x + X.get(a).y *  whr.y);
			projhr.add(e);
		}
		
		//[sprojlh iprojlh] = sort(projlh, 'descend');
		//[sprojhr iprojhr] = sort(projhr, 'descend');
		
		//Collections.sort(projlh,Collections.reverseOrder());
		//Collections.sort(projhr,Collections.reverseOrder());
		
		int[] iprojlh = sortmax(projlh);
		int[] iprojhr = sortmax(projhr);
		
		//A = X(iprojlh,:);
		//B = X(iprojhr,:);
		ArrayList<Point> A = new ArrayList<Point>();
		ArrayList<Point> B = new ArrayList<Point>();
		for (int a = 0; a < iprojlh.length; a++){
			A.add(X.get(iprojlh[a]));
		}
		for (int a = 0; a < iprojhr.length; a++){
			B.add(X.get(iprojhr[a]));
		}
		
		//vl = qrh(A, mu, l, h);
		//vh = qrh(B, mu, h, r);
		ArrayList<Point> vl = new ArrayList<Point>();
		ArrayList<Point> vh = new ArrayList<Point>();
		
		vl = RCH.qrh(A, mu, l, h, false);
		vh = RCH.qrh(B, mu, h, r, false);
		
		if (vl.addAll(vh) == false){
			throw new RuntimeException("add all failed qrh");
		}
		
		return vl;
	}
	
	public static Point findCentroid(ArrayList<Point> P){
		Point c = new Point();
		
		for (int i = 0; i < P.size(); i++){
			c.x += P.get(i).x;
		}
		c.x /= P.size();	//if p.size = 0 division by 0 throw

		for (int i = 0; i < P.size(); i++){
			c.y += P.get(i).y;
		}
		c.y /= P.size();
		
		return c;
	}
	
	public static int[] sortmin(ArrayList<Point> X){
		int [] result = new int[X.size()];
		int mini = 0;
		double min = 0;
		double temp;
		int i = 0;
		int j = 0;
		boolean found = false;
		
		for (int a = 0; a < result.length; a++) {
			result[a] = -1;
		}
		

		for (i = 0; i < result.length; i++) {
			min  = Integer.MAX_VALUE;
			for (j = 0; j < X.size(); j++) {
				temp = X.get(j).x;

				found = false;
				for (int a = 0; a < result.length && !found; a++) {
					if (result[a] == j) {
						found = true;
					}
				}
				if (!found) {
					if (temp <= min) {
						min = temp;
						mini = j;
					}
				}
			}
			result[i] = mini;
		}
		return result;
	}
	
	
	
	public static int[] sortmax(ArrayList<Integer> X){
		int [] result = new int[X.size()];
		int maxi = 0;
		double max = 0;
		double temp;
		int i = 0;
		int j = 0;
		boolean found = false;
		
		for (int a = 0; a < result.length; a++) {
			result[a] = -1;
		}
		

		for (i = 0; i < result.length; i++) {
			max  = Integer.MIN_VALUE;
			for (j = 0; j < X.size(); j++) {
				temp = X.get(j);

				found = false;
				for (int a = 0; a < result.length && !found; a++) {
					if (result[a] == j) {
						found = true;
					}
				}
				if (!found) {
					if (temp > max) {
						max = temp;
						maxi = j;
					}
				}
			}
			result[i] = maxi;
		}
		return result;
	}
	
	
	public static Point alg7(ArrayList<Point> P, double [] S,
			double mu, Point n){
		
		Point v = new Point();
		double [] A = new double[P.size()];
		double [] dots = new double[P.size()];
		ArrayList<Double> sc = new ArrayList<Double>();
		
		double s = 0.0;
		int i = 0;
		
		for (i = 0; i < A.length; i++){
			A[i] = 0;	//redundant java initializes to 0
		}
		
		for (i = 0; i < P.size(); i++){
			//dots[i] = n.x * P.get(i).x + n.y * P.get(i).y;
			sc.add(i, (double) (n.x * P.get(i).x + n.y * P.get(i).y));
		}
		
		
		i = 0;
		while ((1 - s) >0.000001 ){
			if (A[i] == 0){
				i = extractMax(sc);
			}
			A[i] = Math.min(S[i] * mu, 1 - s);
			s = s + A[i];
		}
		
		for (i = 0; i < P.size(); i++){
			v.x += (int) (A[i] * P.get(i).x);
			v.y += (int) (A[i] * P.get(i).y);
		}
		return v;
	}
	
	
	public static int extractMax(ArrayList<Double> list){
		
		int maxi = 0;
		Double max = list.get(maxi);
		
		for (int i = 1; i < list.size(); i++){
			if (max < list.get(i)){
				max = list.get(i);
				maxi = i;
			}
		}
		
		//list.remove(maxi);
		list.set(maxi, Double.MIN_VALUE);
		return maxi;
	}
	
	
	
	
	
}








/*
 * 
 * 
	public static int[] sortmax(ArrayList<Point> X){
		int [] result = new int[X.size()];
		int maxi = 0;
		double max = 0;
		double temp;
		int i = 0;
		int j = 0;
		boolean found = false;
		
		for (int a = 0; a < result.length; a++) {
			result[a] = -1;
		}
		

		for (i = 0; i < result.length; i++) {
			max  = 0;
			for (j = 0; j < X.size(); j++) {
				temp = X.get(j).x;

				found = false;
				for (int a = 0; a < result.length && !found; a++) {
					if (result[a] == j) {
						found = true;
					}
				}
				if (!found) {
					if (temp > max) {
						max = temp;
						maxi = j;
					}
				}
			}
			result[i] = maxi;
		}
		return result;
	}
	
 * 
 * 
*/