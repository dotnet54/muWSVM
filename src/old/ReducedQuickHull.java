package old;

import java.awt.Point;
import java.util.ArrayList;


public class ReducedQuickHull {

	
	public static ArrayList<Point> RQH(ArrayList<Point> P, double mu){
		ArrayList<Point> result = null;
		ArrayList<Point> left = null;
		ArrayList<Point> right = null;
		Point n = new Point(-1,0);
		Point l = findExtremePoint(P, mu, new Point(0,0), new Point(1000,0));
		n.x = 1;
		Point r = findExtremePoint(P, mu, new Point(1000,0), new Point(0,0));
		
		
		left = RQH_aux(P,mu, l, r );
		right  = RQH_aux(P,mu, r, l);
		if (left.addAll(right) == false){
			throw new RuntimeException("cannot append left and right QH");
		}
		return left;
	}
	
	
	public static ArrayList<Point> RQH_aux(ArrayList<Point> P, double mu,
			Point l, Point r){
		ArrayList<Point> result = null;
		ArrayList<Point> left = null;
		ArrayList<Point> right = null;
		
		Point n  = null; //findnormal
		
		Point h = findExtremePoint(P, mu, l, r);
		
		if ( h == l || h == r){
			return P;
		}
		
		//left = 
		//right = 
	 
		return left;
	}
	
	
	public static Point findExtremePoint(ArrayList<Point> P, double mu, Point A, Point B){
		ArrayList<Point> sorted = orderOnDistance(P, A, B);
		Point vertex = theorem3(sorted, mu);
		
		return vertex;
	}
	
	
	public static ArrayList<Point> orderOnDistance(ArrayList<Point> P, 
		Point A, Point B){
		
		ArrayList<Point> result = new ArrayList<Point>();
		double dist = 0, max = 0;
		int max_index = 0;

		while (P.size() > 0){
			for (int j = 0; j < P.size(); j++){
				dist = QuickHull.distanceFromPointToLine(A, B, P.get(j));
				if (dist > max){
					max = dist;
					max_index  = j;
				}
			}
			
			result.add(P.get(max_index));
			P.remove(max_index);
		}

		

		return result;
	}
	
	
	
	public static Point theorem3(ArrayList<Point> P, double mu){
		int m = (int) Math.ceil(1/mu);	//precision 
		int mf = (int) Math.floor(1/mu);	//precision
		int m1 = m - 1;
		
		Point v = new Point(0,0);
		for (int i = 0; i < m1; i++){		//if m1 > P.size case
			v.x = (int) (v.x + mu * P.get(i).x);  
			v.y = (int) (v.y + mu * P.get(i).y);  
		}
		
		v.x += 1 - m1*mu * P.get(m).x; //if P dont have index m
		v.y += 1 - m1*mu * P.get(m).y;
		
		return v;
	}
	
	public static Point[] getNormal(Point p1, Point p2){
		int dx, dy;
		Point[] n = new Point[2];
		
		dx = p2.x - p1.x;
		dy = p2.y - p1.y;
		
		n[0].x = dx;
		n[0].y = -dy;
		
		n[1].x = -dx;
		n[1].y = dy;
		return null;
	}
	
	
	public static DataPoint argMax(ArrayList<DataPoint> P,
			DataPoint n){
		ArrayList<DataPoint>points = getPoints(P);
		double r = 0;
		double max = 0;
		DataPoint maxPoint = null;
		for (int i = 0; i < points.size(); i++){
			r = n.x * points.get(i).x + n.y * points.get(i).y;
			if (max < r){
				max = r;
				maxPoint = points.get(i);
			}
		}
		return maxPoint;
	}


	private static ArrayList<DataPoint> 
	getPoints(ArrayList<DataPoint> p) {
		return p;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}



