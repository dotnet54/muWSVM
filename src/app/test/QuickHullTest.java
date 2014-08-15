package app.test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import old.QuickHull;


public class QuickHullTest {

	static ArrayList<Point> vertices = new ArrayList<Point>();
	static Point[] points;
	static Random randomGenerator = new Random();
	
	
	public static void main(String[] args) {
		points = new Point[5];
		points[0] = new Point(100,50);
		points[1] = new Point(100,100);
		points[2] = new Point(200,200);
		points[3] = new Point(70,150);
		points[4] = new Point(300,50);

		vertices.add(points[0]);
		vertices.add(points[1]);
		vertices.add(points[2]);
		vertices.add(points[3]);
		vertices.add(points[4]);
		
		
//		Point l = QuickHull.findMinX(vertices);
//		Point r = QuickHull.findMaxX(vertices);
		
		Point l = vertices.get(0);
		Point r = vertices.get(3);
		
		Point nl = QuickHull.maxPerpendicularPoint(l, r, vertices);
		Point nr = QuickHull.maxPerpendicularPoint(r, l, vertices);
		
		System.out.println("Left MinX  = " + l.x);
		System.out.println("Right MaxX  = " + r.x);
		
		System.out.println("Max Above Normal left  = " + nl);
		System.out.println("Max Below Normal right  = " + nr);
		
		ArrayList<Point> result = new ArrayList<Point>();
		result = QuickHull.getRight(l, r, vertices);
		System.out.println("Vertices above line");
		for (int i = 0; i < result.size(); i++){
			System.out.println(result.get(i));
		}
		
		result = QuickHull.getRight(r, l, vertices);
		System.out.println("Vertices below line");
		for (int i = 0; i < result.size(); i++){
			System.out.println(result.get(i));
		}
	}

}
