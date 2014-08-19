package app.test;

import app.model.algorithms.RCH;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[] s = new double [5];
		int[] ind = new int[5];
		
		s[0]=0;
		s[1]=0;
		s[2]=1;
		s[3]=2;
		s[4]=0;
		ind = sorti(s);
		
		for (int i = 0; i < s.length; i++){
			System.out.println("s " + s[i]);
		}
		for (int i = 0; i < s.length; i++){
			System.out.println("ind " + ind[i]);
		}
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
	

}
