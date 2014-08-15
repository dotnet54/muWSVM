package old;

import java.awt.Point;
import java.util.ArrayList;

public class QuickHull {

	
	public static ArrayList<Point> QH(ArrayList<Point> P){
		ArrayList<Point> result = null;
		ArrayList<Point> left = null;
		ArrayList<Point> right = null;
		Point n  = null;
		Point l = findMinX(P);
		Point r = findMaxX(P);
		
		
		left = QH_aux(P, l, r );
		right  = QH_aux(P, r, l);
		if (left.addAll(right) == false){
			throw new RuntimeException("cannot append left and right QH");
		}
		return left;
	}
	
	public static ArrayList<Point> QH_aux(ArrayList<Point> P, Point l, Point r){
		ArrayList<Point> result1,result2 = null;
		ArrayList<Point> L = new ArrayList<Point>();
		ArrayList<Point> R = new ArrayList<Point>();
		Point n  = null;
		Point h = null;
		Point nl = null;
		Point nr = null;
		
		
		if (P.size() < 2 ){ //|| P.contains(l) && P.contains(r)
			P.add(l);
			P.add(r);
			return P;// incorrect
		}else{

			h = maxPerpendicularPoint(l, r, P);

			if (h == null){
				return P;
			}
			
			L.add(h); L.add(l);
			L = getRight(l, h, P);
			
			R.add(h); R.add(r);
			R = getRight(h, r, P);
			
			
			result1 = QH_aux(L, l, h);
			result2  = QH_aux(R, h, r);
			if (result1.addAll(result2) == false){
				throw new RuntimeException("cannot append left and right AUX");
			}
			return result1;
		}
	}
	
	public static ArrayList<Point> getLeft(Point A, Point B, ArrayList<Point> list){
		ArrayList<Point> result = new ArrayList<Point>();
		Point p;
		for (int i = 0; i < list.size(); i++){
			p = list.get(i);
			
			if (p.equals(A) || p.equals(B)){
				continue;
			}
			if (checkOrientation(A, B, p) < 0){
				result.add(p);
			}
		}
		return result;	
	}
	
	public static ArrayList<Point> getRight(Point A, Point B, ArrayList<Point> list){
		ArrayList<Point> result = new ArrayList<Point>();
		Point p;
		for (int i = 0; i < list.size(); i++){
			p = list.get(i);
			
			if (p.equals(A) || p.equals(B)){
				continue;
			}
			if (checkOrientation(A, B, p) > 0){
				result.add(p);
			}
		}
		return result;	
	}
	
	
	//finds the cross product to determine is a point is below a line or above a line
	// position = sign( (Bx-Ax)*(Y-Ay) - (By-Ay)*(X-Ax) )
	/**
	 * checks orientation of a point with respect to a line
	 * 
	 * @return +1 if point is right of line
	 * 		   -1 if point is left of line
	 * 		   0 is its colinear
	 * 		"If the line is horizontal, 
	 * 		then this returns true if the point is above the line."
	 */
	public static int checkOrientation(Point A, Point B, Point P){
		return (int) (((B.getX() - A.getX()) * (P.getY() - A.getY()))
			- ((B.getY() - A.getY()) * (P.getX() - A.getX())));
	}
	
	public static double distanceFromPointToLine(Point A, Point B, Point P) {
		double normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y)
				* (B.y - A.y));
		return Math.abs((P.x - A.x) * (B.y - A.y) - (P.y - A.y) * (B.x - A.x))
				/ normalLength;
	}

	public static Point maxPerpendicularPoint(Point A, Point B, ArrayList<Point> list){
		Point p, maxP = null;
		double distance = -1,max = -1;
		
		for (int i = 0; i < list.size(); i++){
			p = list.get(i);
			
			if (p.equals(A) || p.equals(B)){
				continue;
			}
			if (checkOrientation(A, B, p) > 0){	//if above line
				distance = distanceFromPointToLine(A, B, p);
				if (distance > max){
					max = distance;
					maxP = p;
				}
			}
		}
		
		return maxP;		//may cause problems, degenerate case, p=a, =b
	}

	public static Point findMinX(ArrayList<Point> list){
		Point p, min = null;
		
		min = list.get(0); //make sure list != null, and size > 0
		for (int i = 0; i < list.size(); i++){
			p = list.get(i);
			if (p.x < min.x){
				min = p;
			}
		}
		return min;	
	}
	
	public static Point findMaxX(ArrayList<Point> list){
		Point p, max = null;
		
		max = list.get(0); //make sure list != null, and size > 0
		for (int i = 0; i < list.size(); i++){
			p = list.get(i);
			if (p.x > max.x){
				max = p;
			}
		}
		return max;	
	}
	
}