package app.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import app.model.algorithms.QuickHull;
import app.model.algorithms.RCH;
import app.model.algorithms.ReducedQuickHull;

public class ReducedQuickHullTest {

	static ArrayList<Point> vertices = new ArrayList<Point>();
	static Point[] points;
	static Random randomGenerator = new Random();
	
	
	public static void main(String[] args) {
		double mu = 0.5;
		
		points = new Point[3];
		points[0] = new Point(300,200);
		points[1] = new Point(100,100);
		points[2] = new Point(500,100);
		

		vertices.add(points[0]);
		vertices.add(points[1]);
		vertices.add(points[2]);
		
		Point A = vertices.get(1);
		Point B = vertices.get(2);
		
		
		int[] a = RCH.sortmin(vertices);

		//Point H = ReducedQuickHull.findExtremePoint(vertices, mu, A, B);
		ArrayList<Point> result = new ArrayList<Point>();
		result = RCH.qrh(vertices, 0.5, null, null, true);
		System.out.println("Extreme  = " + result);
	}

}
