package app.model;

public class DoubleMath {
	
	public static double PRECISION = 0.00001; //provide a set precision function
	public static int DP = 6;
	
	
	public static boolean isLessThan(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return false;
		}else if(df < 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isLessThanOrEq(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return true;
		}else if(df < 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isGreaterThan(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return false;
		}else if(df > 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isGreaterThanEq(double d1, double d2){
		double df = (d1 - d2);
		if (Math.abs(df) < PRECISION){//eq
			return true;
		}else if(df > 0){//note -0 and +0
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isEqual(double d1, double d2){
		double df = Math.abs(d1 - d2);
		if (df < PRECISION){
			return true;
		}else{
			return false;
		}
	}
	
	public static double dAdd(double d1, double d2){
		double r = d1+d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	public static double dMinus(double d1, double d2){
		double r = d1-d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	public static double dMult(double d1, double d2){
		double r = d1*d2;
		double near = round(r, DP);
		double df = Math.abs(r - near);
		if (df <PRECISION){
			return (double) near;
		}
		return r;
	}
	
	//new BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue()
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	
}