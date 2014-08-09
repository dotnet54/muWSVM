package app.model.algorithms;

import java.awt.Point;
import java.util.ArrayList;

import app.model.data.DataPoint;

public class SK {

	public static void main(String[] args){
		
		ArrayList<RHull.DP> Ppos = new ArrayList<RHull.DP>();
		
		RHull.DP d = new RHull.DP(0,0);
		d.pclass = +1;
		Ppos.add(d);
		d = new RHull.DP(0,0);
		d.pclass = +1;
		Ppos.add(d);
		d = new RHull.DP(0,0);
		d.pclass = +1;
		Ppos.add(d);
		
		ArrayList<RHull.DP> Pneg = new ArrayList<RHull.DP>();
		d = new RHull.DP(0,0);
		d.pclass = -1;
		Pneg.add(d);
		d = new RHull.DP(0,0);
		d.pclass = -1;
		Pneg.add(d);
		d = new RHull.DP(0,0);
		d.pclass = -1;
		Pneg.add(d);
		
		
		
		RHull.DP a = null;
		RHull.DP[] aa = new RHull.DP[3];
		
		a = new RHull.DP(0,0);
		a.pclass = +1;
		aa[0] = a;
		a =  new RHull.DP(1,0);
		a.pclass = +1;
		aa[1] = a;
		a =  new RHull.DP(0,1);
		a.pclass = +1;
		aa[2] = a;
		
		RHull.DP[] bb = new RHull.DP[3];
		
		a = new RHull.DP(6,0);
		a.pclass = -1;
		bb[0] = a;
		a =  new RHull.DP(7,0);
		a.pclass = -1;
		bb[1] = a;
		a =  new RHull.DP(7,1);
		a.pclass = -1;
		bb[2] = a;
		
		
		
		sk(aa,bb,0.5);
		
	}
	
	public static void sk(RHull.DP[] Ppos, RHull.DP[] Pneg
			,double mu){

		RHull.DP p = Ppos[0];
		RHull.DP n = Pneg[0];
		RHull.DP w = null;
		
		double eps = 0.005;
		int MAXIT = 500;
		int it = 0;
		
		while(it < MAXIT){
			w = new RHull.DP(p.x - n.x, p.y - n.y);
			
			w.x *= -1;
			w.y *= -1;
			RHull.DP vp = RHull.theorem32(Ppos, w, mu);
			w.x *= -1;
			w.y *= -1;
			RHull.DP vn = RHull.theorem32(Pneg, w, mu);
			
			RHull.DP dvp = new RHull.DP(vp.x - n.x, vp.y - n.y);
			RHull.DP dpv = new RHull.DP(p.x - vn.x, p.y - vn.y);
			
			double w1 =(w.x * dvp.x + w.y * dvp.y);
			double w2 = (w.x * dpv.x + w.y * dpv.y);
			if ( w1 < w2){
				
				double ws = Math.sqrt(Math.pow(w.x, 2)  + Math.pow(w.y, 2));
				ws = Math.pow(ws, 2);
				
				
				if ((1-w1/ws) < eps){
					break;
				}
				
				double temp;
				
				double x1 = n.x - p.x;
				double x2 = p.x - vp.x;
				
				double y1 = n.y - p.y;
				double y2 = p.y - vp.y;
				
				double top = (x1 * x2) + (y1 * y2);
				double bottom = (x2 * x2) + (y2 * y2);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				p.x = (1-delta) * p.x + delta * vp.x;
				p.y = (1-delta) * p.y + delta * vp.y;
				int f=0;
				
			}else{
				double ws = Math.sqrt(Math.pow(w.x, 2)  + Math.pow(w.y, 2));
				ws = Math.pow(ws, 2);
				
				if ((1-w2/ws) < eps){
					break;
				}
				
				double temp;
				
				double x1 = p.x - n.x;
				double x2 = n.x - vn.x;
				
				double y1 = p.y - n.y;
				double y2 = n.y - vn.y;
				
				double top = (x1 * x2) + (y1 * y2);
				double bottom = (x2 * x2) + (y2 * y2);
				
				temp = top / bottom;
				
				double delta = clamp(temp,0,1);
				n.x = (1-delta) * n.x + delta * vn.x;
				n.y = (1-delta) * n.y + delta * vn.y;
				int f=0;
			}
			
			it++;
		}
		

		double b = 0.5 * ((w.x * p.x + w.y * p.y) + 
						(w.x * n.x + w.y * n.y));
		
		System.out.println("w = " + w.x + ", "+w.y);
		System.out.println("b = " + b);
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
