package app.model.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

import app.model.algorithms.RCH;
import app.model.algorithms.SK;

public class SVMModel {
	
	public double mu1 = 1;
	public double mu2 = 1;
	public int max = 5;	
	public ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	public ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	
	
	private SVMDataItem[] points;
	private Random randomGenerator = new Random();
	
	public ArrayList<SVMDataItem> ch1 = new ArrayList<SVMDataItem>();
	public ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	
	public ArrayList<SVMDataItem> ch2 = new ArrayList<SVMDataItem>();
	public ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	public SVMDataItem w = new SVMDataItem(0,1);
	public double b = 0;
	
	
	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){
		
		//dataset1 = data1;
		//dataset2 = data2;
		
		initializeData();
		//compute();
	}
	
	
	public void compute(){
		ch1 = RCH.rhull(dataset1, 1.0);
		rch1 = RCH.rhull(dataset1, mu1);
		
		ch2 = RCH.rhull(dataset2, 1.0);
		rch2 = RCH.rhull(dataset2, mu2);
		
		SK.solve(this);
	}

	
	public void setMu(double m1, double m2){
		setMu1(m1);
		setMu2(m2);
	}
	public void setMu1(double m1){
		mu1=m1;
		compute();
	}
	public void setMu2(double m2){
		mu2=m2;
		compute();
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
			dataset1.add(new SVMDataItem(x,y));
			
		}
	}
	
	public void setSeries2(XYSeries s){
		dataset2.clear();
		
		for (int i = 0; i < s.getItemCount(); i++){
			double x =  s.getX(i).doubleValue();	//TODO cast double problem
			double y =  s.getY(i).doubleValue();	//TODO cast double problem
			dataset2.add(new SVMDataItem(x,y));
			
		}
	}
	private void initializeData(){
		points = new SVMDataItem[max];
		points[0] = new SVMDataItem(0,0);
		points[1] = new SVMDataItem(100,0);
		points[2] = new SVMDataItem(0,100);
		
		
		dataset1.add(points[0]);
		dataset1.add(points[1]);
		dataset1.add(points[2]);

//		dataset1.add(new Point(0, 0));
//		dataset1.add(new Point(100, 0));
//		dataset1.add(new Point(184, 40));
//		dataset1.add(new Point(109, 103));
//		dataset1.add(new Point(0, 100));
//		
		
		
		
		

		points[0] = new SVMDataItem(500,0);
		points[1] = new SVMDataItem(400,0);
		points[2] = new SVMDataItem(500,100);
		
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
