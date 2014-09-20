package app.model.algorithms;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import old.DataPoint;

import app.model.data.SVMDataItem;
import app.model.data.SVMModel;

public class WSK {
	
	private static SVMDataItem finalW;
	private static double finalB;

	private static double[] pweights;	//TODO change from static
	private static double[] nweights;
	
//	public static void main(String[] args){
//		
//		ArrayList<SVMDataItem> Ppos = new ArrayList<SVMDataItem>();
//		
//		SVMDataItem d = new SVMDataItem(0,0);
//		d.setDataClass(1);
//		Ppos.add(d);
//		d = new SVMDataItem(0,0);
//		d.setDataClass(1);
//		Ppos.add(d);
//		d = new SVMDataItem(0,0);
//		d.setDataClass(1);
//		Ppos.add(d);
//		
//		ArrayList<SVMDataItem> Pneg = new ArrayList<SVMDataItem>();
//		d = new SVMDataItem(0,0);
//		d.setDataClass(-1);
//		Pneg.add(d);
//		d = new SVMDataItem(0,0);
//		d.setDataClass(-1);
//		Pneg.add(d);
//		d = new SVMDataItem(0,0);
//		d.setDataClass(-1);
//		Pneg.add(d);
//		
//		
//		
//		SVMDataItem a = null;
//		SVMDataItem[] aa = new SVMDataItem[3];
//		
//		a = new SVMDataItem(0,0);
//		d.setDataClass(1);
//		aa[0] = a;
//		a =  new SVMDataItem(1,0);
//		d.setDataClass(1);
//		aa[1] = a;
//		a =  new SVMDataItem(0,1);
//		d.setDataClass(1);
//		aa[2] = a;
//		
//		SVMDataItem[] bb = new SVMDataItem[3];
//		
//		a = new SVMDataItem(6,0);
//		d.setDataClass(-1);
//		bb[0] = a;
//		a =  new SVMDataItem(7,0);
//		d.setDataClass(-1);
//		bb[1] = a;
//		a =  new SVMDataItem(7,1);
//		d.setDataClass(-1);
//		bb[2] = a;
//		
//		
//		
//		sk(aa,bb,0.5);
//		
//	}
	
	public static void solve(SVMModel model){
		//TODO solve using different mu? mekae GUI less confusing
		
		
		SVMDataItem[] Ppos;
		SVMDataItem[] Pneg;
		
		Ppos = new SVMDataItem[model.getSeries1().size()];
		Pneg = new SVMDataItem[model.getSeries2().size()];
		pweights = new double [Ppos.length];
		nweights = new double [Pneg.length];
		
		
		
		if (Ppos.length <= 0 || Pneg.length <= 0){
			return ;
		}
		
		
		for (int i = 0; i < Ppos.length; i++){
			Ppos[i] = new SVMDataItem(model.getSeries1().get(i).getXValue(),
					model.getSeries1().get(i).getYValue());
			Ppos[i].setDataClass(1);
			pweights[i] = Ppos[i].getWeight();
		}
		
		for (int i = 0; i < Pneg.length; i++){
			Pneg[i] = new SVMDataItem(model.getSeries2().get(i).getXValue(),
					model.getSeries2().get(i).getYValue());
			Pneg[i].setDataClass(-1);
			nweights[i] = Pneg[i].getWeight();
		}
		
		sk(Ppos, Pneg, model.getMu1(), model.getMu2());
		
		model.setW(finalW);
		model.setB(finalB);
	}
	
	public static Line2D getLine(SVMDataItem w, double b){
        double yMin = -500;
        double yMax = 500;
        double xMin = ((b-(w.getYValue()*yMin))/w.getXValue());
        double xMax =   ((b-(w.getYValue()*yMax))/w.getXValue());
        
//        double mx = w.getXValue();
//        double my = w.getYValue();
//        double m = w.getMagnitude();
//        
//        double xMin = yMin / m;
//        double xMax = yMax / m;
        
        Point.Double p1= new Point.Double(xMin, yMin);
        Point.Double p2= new Point.Double(xMax, yMax);
        System.out.format("Line points:\np1:%s\np2:%s\n", p1, p2 );
        return new Line2D.Double(p1, p2);
	}
	
	public static void sk(SVMDataItem[] Ppos, SVMDataItem[] Pneg
			,double mu1,double mu2){

		SVMDataItem p = null;
		SVMDataItem n = null;
		SVMDataItem w = null;
		
		double eps = 0.005;
		int MAXIT = 50;
		int it = 0;
		
		
//		p = RCH.theorem32(Ppos, new SVMDataItem(-1, 0), mu);
//		n = RCH.theorem32(Pneg, new SVMDataItem(1, 0), mu);
		p = WRCH.alg9(Ppos,pweights , mu1,new SVMDataItem(-1, 0));
		n = WRCH.alg9(Pneg,nweights , mu2,new SVMDataItem(1, 0));
		
		while(it < MAXIT){
			w = new SVMDataItem(p.getXValue() - n.getXValue(),
					p.getYValue() - n.getYValue());
			
			SVMDataItem wprime = new SVMDataItem(w.getXValue(), w.getYValue());
			
			wprime.setX(w.getXValue() -1);
			wprime.setY(w.getYValue() -1);
//			SVMDataItem vp = RCH.theorem32(Ppos, w, mu);
			SVMDataItem vp = WRCH.alg9(Ppos,pweights , mu1, w);
			
			wprime = new SVMDataItem(w.getXValue(), w.getYValue());
			wprime.setX(w.getXValue());
			wprime.setY(w.getYValue());
//			SVMDataItem vn = RCH.theorem32(Pneg, w, mu);
			SVMDataItem vn = WRCH.alg9(Pneg,nweights , mu2, w);
			
			SVMDataItem dvp = new SVMDataItem(vp.getXValue() - n.getXValue(), 
					vp.getYValue() - n.getYValue());
			SVMDataItem dpv = new SVMDataItem(p.getXValue() - vn.getXValue(), 
					p.getYValue() - vn.getYValue());
			
			double w1 =(w.getDotProduct(dvp));
			double w2 = (w.getDotProduct(dpv));
			if ( w1 < w2){
				
				double ws = Math.sqrt(Math.pow(w.getXValue(), 2)  
						+ Math.pow(w.getXValue(), 2));
				ws = Math.pow(ws, 2);
				
				
				if ((1-w1/ws) < eps){
					break;
				}
				
				double temp;
				
				double x1 = n.getXValue() - p.getXValue();
				double x2 = p.getXValue() - vp.getXValue();
				
				double y1 = n.getYValue() - p.getYValue();
				double y2 = p.getYValue() - vp.getYValue();
				
				double top = (x1 * x2) + (y1 * y2);
				double bottom = (x2 * x2) + (y2 * y2);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				p.setX((1-delta) * p.getXValue() + delta * vp.getXValue());
				p.setY((1-delta) * p.getYValue() + delta * vp.getYValue());
				int f=0;
				
			}else{
				double ws = Math.sqrt(Math.pow(w.getXValue(), 2) 
						+ Math.pow(w.getYValue(), 2));
				ws = Math.pow(ws, 2);
				
				if ((1-w2/ws) < eps){
					break;
				}
				
				double temp;
				
				double x1 = p.getXValue() - n.getXValue();
				double x2 = n.getXValue() - vn.getXValue();
				
				double y1 = p.getYValue() - n.getYValue();
				double y2 = n.getYValue() - vn.getYValue();
				
				double top = (x1 * x2) + (y1 * y2);
				double bottom = (x2 * x2) + (y2 * y2);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				n.setX((1-delta) * n.getXValue() + delta * vn.getXValue());
				n.setY((1-delta) * n.getYValue() + delta * vn.getYValue());
				int f=0;
			}
			
			it++;
		}
		

		double b = 0.5 * ((w.getDotProduct(p)) + 
						(w.getDotProduct(n)));
		
		finalW = w;
		finalB = b;
		System.out.println("w = " + finalW.getXValue() + ", "+finalW.getYValue());
		System.out.println("b = " + finalB);
		System.out.println("it = " + it);
		
		
	}
	
	
	public static double clamp(double c, double cmin, double cmax){
		
		if (c <= cmin){
			return cmin;
		}else if (cmin < c && c < cmax){
			return c;
		}else if (c >= cmax){
			return cmax;
		}else{
			//assert error: should never reach this
			System.out.println("logic error: assertion failed");
			return Double.NaN;
		}
	}

}
