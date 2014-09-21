package app.model.data;

public class SVMParameters {
	
	private int numClass;
	
	
	public class inputs{
		private double mu[];	//mu per class
		private boolean useSameMu = false;
		
		private int maxIterationWSK = 50;
		private int maxRecursionWRCH = 100;
		private double epsilon = 0.001;
		private int roundOff = 2;
		
	}
	
	public class outputs{
		private int numSV[];	//num SV per class
		private double alphas[][];	//alphas per class
	}
}
