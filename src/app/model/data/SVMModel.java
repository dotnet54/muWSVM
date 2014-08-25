package app.model.data;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import app.model.algorithms.RCH;
import app.model.algorithms.SK;

public class SVMModel {
	

	
	private ArrayList<SVMDataItem> dataset1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> dataset2 = new ArrayList<SVMDataItem>();	
	
//	XYSeries series1 = new XYSeries("Positive Class");
//	XYSeries series2 = new XYSeries("Negative Class");
	
	private SVMDataItem[] points;
	private Random randomGenerator = new Random();
	
	private ArrayList<SVMDataItem> ch1 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch1 = new ArrayList<SVMDataItem>();
	
	private ArrayList<SVMDataItem> ch2 = new ArrayList<SVMDataItem>();
	private ArrayList<SVMDataItem> rch2 = new ArrayList<SVMDataItem>();
	
	private double mu1 = 1;
	private double mu2 = 1;
	private SVMDataItem w = new SVMDataItem(0,1);
	private double b = 0;
		
	public SVMDataItem centroid1;
	public SVMDataItem centroid2;
	
	//TODO sync problem, run bgtask in debugger, before 1 task finishes 
	public SVMModel(){


	}
	
	
	public void compute(){
		calculateHull(0);
		solveSVM();
	}
	
	public boolean calculateHull(int series){
		
		
		
		
		if (series == 1){
			
			if (dataset1.size() == 0){
				return false;
			}else{
				ch1 = RCH.calcReducedCHull(dataset1, 1.0);
				rch1 = RCH.calcReducedCHull(dataset1, mu1);
			}
			
		}else if (series == 2){
			if (dataset2.size() == 0){
				return false;
			}else{
				ch2 = RCH.calcReducedCHull(dataset2, 1.0);
				rch2 = RCH.calcReducedCHull(dataset2, mu2);
			}
			
		}else{
			if (dataset1.size() != 0){
				//ch1 = RCH.calcReducedCHull(dataset1, 1.0);
				rch1 = RCH.calcReducedCHull(dataset1, mu1);
			}//else return false TODO
			if (dataset2.size() != 0){
				//ch2 = RCH.calcReducedCHull(dataset2, 1.0);
				rch2 = RCH.calcReducedCHull(dataset2, mu2);
			}
		}
		
		centroid1 = RCH.findCentroid(dataset1);
		centroid1.setLabel("+");
		centroid2 = RCH.findCentroid(dataset2);
		centroid2.setLabel("-");
		return true;
	}
	
	public boolean solveSVM(){
		SK.solve(this);
		return true;
	}

	
	public void setMu(double m1, double m2){
		setMu1(m1);
		setMu2(m2);
	}
	public void setMu1(double m1){
		mu1=m1;
		//compute();	//FIRE model change event TODO
	}
	public void setMu2(double m2){
		mu2=m2;
		//compute();
	}
	public double getMu1(){
		return mu1;
	}
	
	public double getMu2(){
		return mu2;
	}
	
	public void setW(SVMDataItem w){
		this.w = w;
	}
	
	public void setB(double b){
		this.b = b;
	}
	
	public SVMDataItem getW(){
		return w;	//reference or value TODO
	}
	
	public double getB(){
		return b;
	}
	
	public void clearDataSet(int series){
		if (series == 1){
			dataset1.clear();
			ch1.clear();
			rch1.clear();
		}else if (series == 2){
			dataset2.clear();
			ch2.clear();
			rch2.clear();
		}else{
			dataset1.clear();
			ch1.clear();
			rch1.clear();
			dataset2.clear();
			ch2.clear();
			rch2.clear();
		}
	}
	
	public void setSeries1(XYSeries s){
		dataset1.clear();
		
		//SVMDataItem item = null;
		SVMDataItem item =  null;
		for (int i = 0; i < s.getItemCount(); i++){
			item = (SVMDataItem) s.getItems().get(i);
			
			//double x =  item.getXValue();	//TODO cast double problem
			//double y =  item.getYValue();	//TODO cast double problem
			dataset1.add(item);
			
		}
	}
	
	public void setSeries2(XYSeries s){
		dataset2.clear();
		
		SVMDataItem item =  null;
		for (int i = 0; i < s.getItemCount(); i++){
			item = (SVMDataItem) s.getItems().get(i);
			
			//double x =  item.getXValue();	//TODO cast double problem
			//double y =  item.getYValue();	//TODO cast double problem
			dataset2.add(item);
			
		}
	}
	
	public ArrayList<SVMDataItem> getSeries1(){
		return dataset1;
	}
	
	public ArrayList<SVMDataItem> getSeries2(){
		return dataset2;
	}
	
	public ArrayList<SVMDataItem> getCH1(){
		return ch1;
	}
	
	public ArrayList<SVMDataItem> getRCH1(){
		return rch1;
	}
	
	public ArrayList<SVMDataItem> getCH2(){
		return ch2;
	}
	
	public ArrayList<SVMDataItem> getRCH2(){
		return rch2;
	}
	

}
