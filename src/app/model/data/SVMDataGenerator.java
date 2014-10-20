package app.model.data;

import java.util.Random;

public class SVMDataGenerator {
	
	SVMDataSet data;
	Random randGen =  null;
	
	public SVMDataSet getData() {
		return data;
	}

	public void setData(SVMDataSet data) {
		this.data = data;
	}

	public SVMDataGenerator(int numItems,
			double minVal,double maxVal) {
		
		long seed = System.currentTimeMillis();
		randGen = new Random(seed);
		
		data = new SVMDataSet();
		
		for (int i = 0; i < numItems/2; i++){
			double x = randGen.nextDouble() * maxVal;
			double y = randGen.nextDouble() * maxVal;
			double weight = 1;
			int dclass = randGen.nextInt(2);
			data.getPositiveSeries().add( new SVMDataItem(
					x, y, weight, dclass));
		}
		
		for (int i = 0; i < numItems/2; i++){
			double x = randGen.nextDouble() * maxVal;
			double y = randGen.nextDouble() * maxVal;
			double weight = 1;
			int dclass = randGen.nextInt(2);
			data.getNegativeSeries().add( new SVMDataItem(
					x, y, weight, dclass));
		}
		
	}
	
	
	 
}
