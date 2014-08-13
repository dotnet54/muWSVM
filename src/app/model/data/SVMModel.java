package app.model.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

import app.model.algorithms.RHull;
import app.model.algorithms.RHull.DP;
import app.model.algorithms.SK;

public class SVMModel {
	
	public double mu1 = 0.5;
	public double mu2 = 0.5;
	public int max = 5;	
	public ArrayList<Point> dataset1 = new ArrayList<Point>();
	public ArrayList<Point> dataset2 = new ArrayList<Point>();
	public ArrayList<Point> weights1 = new ArrayList<Point>();
	public ArrayList<Point> weights2 = new ArrayList<Point>();
	public ArrayList<Point> labels1 = new ArrayList<Point>();
	public ArrayList<Point> labels2 = new ArrayList<Point>();	
	
	private Point[] points;
	private Random randomGenerator = new Random();
	
	public ArrayList<Point> ch1 = new ArrayList<Point>();
	public ArrayList<Point> rch1 = new ArrayList<Point>();
	
	public ArrayList<Point> ch2 = new ArrayList<Point>();
	public ArrayList<Point> rch2 = new ArrayList<Point>();
	
	public RHull.DP w = new RHull.DP(0,1);
	public double b = 0;
	
	
	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		
		//dataset1 = data1;
		//dataset2 = data2;
		
		initializeData();
		//compute();
	}
	
	
	public void compute(){
//		ch1 = RHull.rhull(dataset1, 1.0);
//		rch1 = RHull.rhull(dataset1, mu1);
//		
//		ch2 = RHull.rhull(dataset2, 1.0);
//		rch2 = RHull.rhull(dataset2, mu2);
		
		SK.solve(this);
	}

	
	public void setMu(double m1, double m2){
		mu1=m1;
		mu2=m2;
	}
	
	public double getMu1(){
		return mu1;
	}
	
	public double getMu2(){
		return mu2;
	}
	
	public void setSeries1(XYSeries s){
		dataset1.clear();
		
		for (int i = 0; i < s.getItemCount(); i++){
			
			double x =  s.getX(i).doubleValue();	//TODO cast double problem
			double y =  s.getY(i).doubleValue();	//TODO cast double problem
			dataset1.add(new Point((int)x,(int) y));
			
		}
	}
	
	
	private void initializeData(){
		points = new Point[max];
		points[0] = new Point(0,0);
		points[1] = new Point(100,0);
		points[2] = new Point(0,100);
		
		
		dataset1.add(points[0]);
		dataset1.add(points[1]);
		dataset1.add(points[2]);

//		dataset1.add(new Point(0, 0));
//		dataset1.add(new Point(100, 0));
//		dataset1.add(new Point(184, 40));
//		dataset1.add(new Point(109, 103));
//		dataset1.add(new Point(0, 100));
//		
		
		
		
		

		points[0] = new Point(500,0);
		points[1] = new Point(400,0);
		points[2] = new Point(500,100);
		
		dataset2.add(points[0]);
		dataset2.add(points[1]);
		dataset2.add(points[2]);

//		dataset1.clear();
//		dataset2.clear();
//		
//		for (int i = 0; i < max; i++){
//			Point p = new Point();
//			p.setLocation(randomGenerator.nextInt(250), randomGenerator.nextInt(200)); //randomGenerator.nextInt(400);
//			dataset1.add(p);
//		}
//		
//		for (int i = 0; i < max; i++){
//			Point p = new Point();
//			p.setLocation(randomGenerator.nextInt(250)+200, randomGenerator.nextInt(200)+50); //randomGenerator.nextInt(400);
//			dataset2.add(p);
//		}
	}
}
