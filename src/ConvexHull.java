
import java.awt.Point;
import java.util.Arrays;

import app.model.algorithms.*;
import app.model.data.DataPoint;

// Sem 1 Week 5

public class ConvexHull {

	/**
	 * @param args
	 */
	
	public static DataPoint dp[];
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		dp = new DataPoint[4];
		
		dp[0] = new DataPoint(10, 10);
		dp[1] = new DataPoint(20, 20);
		dp[2] = new DataPoint(10, 20);
		dp[3] = new DataPoint(10, 60);
		
		
		//DataPoint[] temp =  getHull(dp);
		
		convexHull(dp);
		
		//for (int i = 0; i < temp.length; i++){
		//	System.out.println("Point ID = " + temp[i].id);
		//}
		
	}
	
	
	private static boolean CCW(DataPoint p, DataPoint q, DataPoint r)
    {
        int val = (int) ((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y));
 
         if (val >= 0)
             return false;
         return true;
    }
	
	public static void convexHull(DataPoint[] points)
    {
        int n = points.length;
        /** if less than 3 points return **/        
        if (n < 3) 
            return;     
        int[] next = new int[n];
        Arrays.fill(next, -1);
 
        /** find the leftmost point **/
        int leftMost = 0;
        for (int i = 1; i < n; i++)
            if (points[i].x < points[leftMost].x)
                leftMost = i;
        int p = leftMost, q;
        /** iterate till p becomes leftMost **/
        do
        {
            /** wrapping **/
            q = (p + 1) % n;
            for (int i = 0; i < n; i++)
              if (CCW(points[p], points[i], points[q]))
                 q = i;
 
            next[p] = q;  
            p = q; 
        } while (p != leftMost);
 
        /** Display result **/
        display(points, next);
    }
	public static void display(DataPoint[] points, int[] next)
    {
        System.out.println("\nConvex Hull points : ");
        for (int i = 0; i < next.length; i++)
            if (next[i] != -1)
               System.out.println("("+ points[i].x +", "+ points[i].y +")");
    }
}
