package app.model;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import app.model.data.WSVMDataItem;
import app.model.data.WSVMDataSet;



public class WSKSolver {

	
	public static double epsilon = 0.001;
	public static int maxIterations = 500;
	
	private WSVMDataItem w;
	private double b;
	
	private WSVMDataItem nearestPositivePoint = null;
	private WSVMDataItem nearestNegativePoint = null;	
	
	private int iterations = 0;
	
	private long startTime = 0;
	private long endTime = 0;
	private long elapsedTime = 0;
	
	public boolean wsk(WSVMDataSet Dataset, double mu1,double mu2) throws Exception{
	
		startTime = System.nanoTime();
		double breakCondition = Double.NaN;
		
		WSVMDataItem p = null;
		WSVMDataItem n = null;
		WSVMDataItem w = null;
		
		ArrayList<WSVMDataItem> positiveClass = Dataset.getPositiveClass();
		ArrayList<WSVMDataItem> negativeClass = Dataset.getNegativeClass();
		
		if (positiveClass !=null && positiveClass.isEmpty()){
			return false;
		}
		
		if (negativeClass !=null && negativeClass.isEmpty()){
			return false;
		}
		
		WSVMDataItem startDirection = new WSVMDataItem(Dataset.getDimensions());
		startDirection.setXValue(+1);
		WSVMDataItem oppositeDirection = new WSVMDataItem(Dataset.getDimensions());
		oppositeDirection.setXValue(-1);
	
		p = findExtremePoint(positiveClass , mu1, startDirection);
		n = findExtremePoint(negativeClass , mu2, oppositeDirection);
		
		
		while(iterations < maxIterations){
			w = p.subtractVectors(n);
			WSVMDataItem wprime = w.clone();
			wprime.multiplyByScaler(-1);
	
			WSVMDataItem vp = findExtremePoint(positiveClass , mu1, wprime);
			WSVMDataItem vn = findExtremePoint(negativeClass , mu2, w);
			
			WSVMDataItem dvp = vp.subtractVectors(n);
			WSVMDataItem dpv = p.subtractVectors(vn);
			
			double w1 =(w.getDotProduct(dvp));
			double w2 = (w.getDotProduct(dpv));
			double ws = w.getMagnitude();
			ws = Math.pow(ws, 2);
			double temp;
			if ( w1 < w2){
				breakCondition = (1-(w1/ws));
				if (Double.isNaN(breakCondition)){
					return false;
				}
				if (breakCondition < epsilon){
					break;
				}
				WSVMDataItem dnp = p.subtractVectors(n);
				WSVMDataItem dpvp = p.subtractVectors(vp);
				double top = dnp.getDotProduct(dpvp);
				double bottom = dpvp.getDotProduct(dpvp);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				
				if (delta == Double.NaN){
					break;
				}
				
				WSVMDataItem newP = p.clone();
				newP.multiplyByScaler((1-delta));
				WSVMDataItem newVP = vp.clone();
				newVP.multiplyByScaler(delta);
				newP.add(newVP);
				p = newP;
			}else{
				breakCondition = (1-(w2/ws));
				if (Double.isNaN(breakCondition)){
					return false;
				}
				if (breakCondition < epsilon){
					break;
				}
				WSVMDataItem dpn = n.subtractVectors(p);
				WSVMDataItem dpvn = n.subtractVectors(vn);
				double top = dpn.getDotProduct(dpvn);
				double bottom = dpvn.getDotProduct(dpvn);
				temp = top / bottom;
				double delta = clamp(temp,0,1);
				
				if (delta == Double.NaN){
					break;
				}
				
				WSVMDataItem newN = n.clone();
				newN.multiplyByScaler((1-delta));
				WSVMDataItem newVN = vn.clone();
				newVN.multiplyByScaler(delta);
				newN.add(newVN);
				n = newN;
			}
			iterations++;
		}
		double b = 0.5 * ((w.getDotProduct(p)) + (w.getDotProduct(n)));
		
		this.w = w;
		this.b = b;
		nearestPositivePoint = p;
		nearestNegativePoint = n;
		
		endTime = System.nanoTime();
		elapsedTime = endTime - startTime;
		
		if (!isZeroOrValid(w)){
			return false;
		}
		
		
		//System.out.println("w = " + finalW.getXValue() + ", "+finalW.getYValue());
		//System.out.println("b = " + finalB);
		System.out.println("it = " + iterations + ", break: " + breakCondition);
		System.out.println("Elapsed Time for WSK: " + elapsedTime / 1e6 +" ms");
		//System.out.println("p = " + p + ", n = " + n);

		return true;
		
	}
	//NOTE WSK is n dimension whole WRCH is 2 dimension
	public static WSVMDataItem findExtremePoint(ArrayList<WSVMDataItem> list, double mu, final WSVMDataItem n){
		
		if (mu == 0){ 
			return WRCHSolver.findCentroid(list);	
		}
	
		try {
			reverseSortOnProjections(list, n);
		} catch (Exception e) {
			//System.out.println("Comparator exception");
			//e.printStackTrace();
		}
	
		double [] A = new double[list.size()];
		double s = 0;
		int k = 0;
		
		int count = 0;
		
		while (DoubleMath.isLessThan(s, 1)){
			if (k >= A.length){
				k = 0;//
				return WRCHSolver.findCentroid(list); 
			}
				if (A[k] == 0){
					count++;
				}
			A[k] = A[k] + Math.min(list.get(k).getWeight() * mu, 1 - s);
			s += A[k];
			k++;
		}
		
		WSVMDataItem v = new WSVMDataItem(n.getDimensions());
		int i = 0;
		for (i = 0; i < count; i++){
			WSVMDataItem c = list.get(i).clone();
			c.multiplyByScaler(A[i]);
			try {
				v.add(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return v;
	}
	private static void reverseSortOnProjections(ArrayList<WSVMDataItem> list,final WSVMDataItem normal){
		Collections.sort(list,Collections.reverseOrder( new Comparator<WSVMDataItem>() {
			@Override
			public int compare(WSVMDataItem o1, WSVMDataItem o2) {
				//fp comparisons
				try {
					double p1 = o1.getDotProduct(normal);
					double p2 = o2.getDotProduct(normal);
					double diff = Math.abs(p1 - p2);
					
					//Known Exception: Comparison violates general contract
					//rare cases when this comparator may not give a total ordering
					//as java expects. this is due to precision erros
					if ((diff - DoubleMath.PRECISION) < 0){
						return 0;
					}else if (p1 > p2){
						return 1;
					}else{
						return -1;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		}));
	}
	
	
	private static double clamp(double c, double cmin, double cmax){
		
		if (c <= cmin){
			return cmin;
		}else if (cmin < c && c < cmax){
			return c;
		}else if (c >= cmax){
			return cmax;
		}else{
			//assert error: this will happen if doubles contain NaN, catch it in caller
			return Double.NaN; 
		}
	}
	
	public boolean isZeroOrValid(WSVMDataItem w){
		boolean valid = false;
		try {
			double val;
			
			for (int i = 0; i < w.getDimensions(); i++){
				val = w.getVal(i);
				
				if (Double.isNaN(val) || Double.isInfinite(val)){
					return false;
				}
				//adjust the value of 0.01 to give a min bound on nearest points for valid solutions
				double diff = Math.abs(val - 0);
				if (!(diff < 0.005)){
					valid = true;
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}
	
	
	public WSVMDataItem getW() {
		return w;
	}
	public double getB() {
		return b;
	}

	public WSVMDataItem getNearestPositivePoint() {
		return nearestPositivePoint;
	}

	public WSVMDataItem getNearestNegativePoint() {
		return nearestNegativePoint;
	}

}
