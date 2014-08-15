package app.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import old.QuickHull;
import old.ReducedQuickHull;
import old.ReducedQuickHull2D;
import old.ReducedQuickHull2D.Vector;

import app.model.algorithms.RCH;
import app.model.data.DataPoint;

public class ReducedQuickHullTest {

	static ArrayList<Point> vertices = new ArrayList<Point>();
	static Point[] points;
	static Random randomGenerator = new Random();
	static double weights[];
	
	public static void main(String[] args) {
		double mu = 0.5;
		
		points = new Point[3];
		points[0] = new Point(0,0);
		points[1] = new Point(0,10);
		points[2] = new Point(10,0);
		weights = new double[3];
		weights[0] = 1;
		weights[1] = 1;
		weights[2] = 1;
		
		Point normal = new Point(1,1);
		
		

		vertices.add(points[0]);
		vertices.add(points[1]);
		vertices.add(points[2]);
		
		Point resV = RCH.alg7(vertices, weights, mu, normal);
		System.out.println("weighted new v: " + resV.x + ", " + resV.y);
		
		Point A = vertices.get(1);
		Point B = vertices.get(2);
		
		testRCH2D();
		
		int[] a = RCH.sortmin(vertices);

		//Point H = ReducedQuickHull.findExtremePoint(vertices, mu, A, B);
		ArrayList<Point> result = new ArrayList<Point>();
		result = RCH.qrh(vertices, 0.5, null, null, true);
		System.out.println("Extreme  = " + result);
		
		
	}
	
	public static void testRCH2D(){
		
		
		ArrayList<ReducedQuickHull2D.Vector> points = new ArrayList<ReducedQuickHull2D.Vector>();
		ArrayList<ReducedQuickHull2D.Vector> rhull = new ArrayList<ReducedQuickHull2D.Vector>();
		
		points.add(new ReducedQuickHull2D.Vector(0,0));
		points.add(new ReducedQuickHull2D.Vector(1,0));
		points.add(new ReducedQuickHull2D.Vector(0,1));
		
		//points = ReducedQuickHull2D.order(points, new ReducedQuickHull2D.Vector(1,0));
		
		ReducedQuickHull2D.Vector p = ReducedQuickHull2D.theorem3(points, 0.5);
		System.out.println("Reduced Point  = " + p);
		
		//System.out.println("Normal  = " 
		//		+ Vector.normal(new Vector(0, 0.5), new Vector(0.5, 0))); 
		
		rhull = ReducedQuickHull2D.RQH(points, 0.5);
		for (int i = 0; i < rhull.size(); i++){
			System.out.println("RHull Point  = " + rhull.get(i));
		}
	}
	
	
	
	public static void testTheorem3(){
		ArrayList<DataPoint> points = new ArrayList<DataPoint>();
		
		points.add(new DataPoint(0,1));
		points.add(new DataPoint(0,0));
		points.add(new DataPoint(1,0));
		DataPoint p = theorem3(points, 0.5);
		System.out.println("Reduced Point  = " + p);
		
	}
	
	public static DataPoint theorem3(ArrayList<DataPoint> P, double mu){
		int m = (int) Math.ceil(1/mu);	//precision 
		
		DataPoint v = new DataPoint(0,0);
		for (int i = 0; i < (m-1); i++){		//if m1 > P.size case
			v.x += mu * P.get(i).x;  
			v.y += mu * P.get(i).y;  
		}
		
		v.x += 1 - (m-1)*mu * P.get(m).x; //if P dont have index m
		v.y += 1 - (m-1)*mu * P.get(m).y;
		
		return v;
	}

}
