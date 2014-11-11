package app.test;

import java.awt.Point;
import java.awt.geom.Line2D;

import app.model.SVMModel;

public class WSK_old {
	
	
	//TODO CHANGE from static class to instance class, smae for RCH
	
	private static SVMDataItemOLD2 finalW;
	private static double finalB;

	
	public static SVMDataItemOLD2 getFinalW() {
		return finalW;
	}
	public static double getFinalB() {
		return finalB;
	}
	
	private static SVMDataItemOLD2 nearestPositivePoint = null;
	private static SVMDataItemOLD2 nearestNegativePoint = null;
	public static SVMDataItemOLD2 getNearestPositivePoint() {
		return nearestPositivePoint;
	}


	public static SVMDataItemOLD2 getNearestNegativePoint() {
		return nearestNegativePoint;
	}


	
	public static void wsk(SVMDataItemOLD2[] Ppos, SVMDataItemOLD2[] Pneg, double[] pweights, double[] nweights
			,double mu1,double mu2){

		SVMDataItemOLD2 p = null;
		SVMDataItemOLD2 n = null;
		SVMDataItemOLD2 w = null;
		
		double eps = 0.005;
		int MAXIT = 500;
		int it = 0;
		
		
//		p = RCH.theorem32(Ppos, new SVMDataItem(-1, 0), mu);
//		n = RCH.theorem32(Pneg, new SVMDataItem(1, 0), mu);
		p = WRCH_old.alg9(Ppos,pweights , mu1,new SVMDataItemOLD2(-1, 0));
		n = WRCH_old.alg9(Pneg,nweights , mu2,new SVMDataItemOLD2(1, 0));
		
		while(it < MAXIT){
			w = new SVMDataItemOLD2(
					p.getXValue() - n.getXValue(),
					p.getYValue() - n.getYValue());
			
			SVMDataItemOLD2 wprime = new SVMDataItemOLD2(w.getXValue(), w.getYValue());
			
			wprime.setX(w.getXValue() *-1);
			wprime.setY(w.getYValue() *-1);
//			SVMDataItem vp = RCH.theorem32(Ppos, w, mu);
			SVMDataItemOLD2 vp = WRCH_old.alg9(Ppos,pweights , mu1, wprime);
			
			wprime = new SVMDataItemOLD2(w.getXValue(), w.getYValue());
			wprime.setX(w.getXValue());
			wprime.setY(w.getYValue());
//			SVMDataItem vn = RCH.theorem32(Pneg, w, mu);
			SVMDataItemOLD2 vn = WRCH_old.alg9(Pneg,nweights , mu2, w);
			
			SVMDataItemOLD2 dvp = new SVMDataItemOLD2(vp.getXValue() - n.getXValue(), 
					vp.getYValue() - n.getYValue());
			SVMDataItemOLD2 dpv = new SVMDataItemOLD2(p.getXValue() - vn.getXValue(), 
					p.getYValue() - vn.getYValue());
			
			double w1 =(w.getDotProduct(dvp));
			double w2 = (w.getDotProduct(dpv));
			if ( w1 < w2){
				
				double ws = Math.sqrt(Math.pow(w.getXValue(), 2)  
						+ Math.pow(w.getYValue(), 2));
				ws = Math.pow(ws, 2);
				
				
				if ((1-(w1/ws)) < eps){
					break;
				}
				
				double temp;
				
//				double x1 = n.getXValue() - p.getXValue();
//				double x2 = p.getXValue() - vp.getXValue();
//				
//				double y1 = n.getYValue() - p.getYValue();
//				double y2 = p.getYValue() - vp.getYValue();
//				
//				double top = (x1 * x2) + (y1 * y2);
//				double bottom = (x2 * x2) + (y2 * y2);
				
				
				SVMDataItemOLD2 dnp = new SVMDataItemOLD2(
						p.getXValue() - n.getXValue(),
						p.getYValue() - n.getYValue());
				
				SVMDataItemOLD2 dpvp = new SVMDataItemOLD2(
						p.getXValue() - vp.getXValue(),
						p.getYValue() - vp.getYValue());
				
				double top = dnp.getDotProduct(dpvp);
				double bottom = dpvp.getDotProduct(dpvp);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				p.setX((1-delta) * p.getXValue() + delta * vp.getXValue());
				p.setY((1-delta) * p.getYValue() + delta * vp.getYValue());
				int f=0;
				
			}else{
				double ws = Math.sqrt(Math.pow(w.getXValue(), 2) 
						+ Math.pow(w.getYValue(), 2));
				ws = Math.pow(ws, 2);
				
				if ((1-(w2/ws)) < eps){
					break;
				}
				
				double temp;
				
//				double x1 = p.getXValue() - n.getXValue();
//				double x2 = n.getXValue() - vn.getXValue();
//				
//				double y1 = p.getYValue() - n.getYValue();
//				double y2 = n.getYValue() - vn.getYValue();
//				
//				double top = (x1 * x2) + (y1 * y2);
//				double bottom = (x2 * x2) + (y2 * y2);
				
				
				SVMDataItemOLD2 dpn = new SVMDataItemOLD2(
						n.getXValue() - p.getXValue(),
						n.getYValue() - p.getYValue());
				
				SVMDataItemOLD2 dpvn = new SVMDataItemOLD2(
						n.getXValue() - vn.getXValue(),
						n.getYValue() - vn.getYValue());
				
				double top = dpn.getDotProduct(dpvn);
				double bottom = dpvn.getDotProduct(dpvn);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				n.setX((1-delta) * n.getXValue() + delta * vn.getXValue());
				n.setY((1-delta) * n.getYValue() + delta * vn.getYValue());
				int f=0;
			}
			
			it++;
		}
		

		double b = 0.5 * ((w.getDotProduct(p)) + (w.getDotProduct(n)));
		
		finalW = w;
		finalB = b;
		nearestPositivePoint = p;
		nearestNegativePoint = n;
		
		//System.out.println("w = " + finalW.getXValue() + ", "+finalW.getYValue());
		//System.out.println("b = " + finalB);
		System.out.println("it = " + it);
		//System.out.println("p = " + p + ", n = " + n);
		
	}
	
	
	private static double clamp(double c, double cmin, double cmax){
		
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
