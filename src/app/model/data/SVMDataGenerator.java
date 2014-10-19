package app.model.data;

import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

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
		
		SVMDataSeries series1 = new SVMDataSeries("Positive Class");
		SVMDataSeries series2 = new SVMDataSeries("Negative Class");	
		
		for (int i = 0; i < numItems/2; i++){
			double x = randGen.nextDouble() * maxVal;
			double y = randGen.nextDouble() * maxVal;
			double weight = 1;
			int dclass = randGen.nextInt(2);
			series1.add( new SVMDataItem(
					x, y, weight, dclass));
		}
		
		for (int i = 0; i < numItems/2; i++){
			double x = randGen.nextDouble() * maxVal;
			double y = randGen.nextDouble() * maxVal;
			double weight = 1;
			int dclass = randGen.nextInt(2);
			series2.add( new SVMDataItem(
					x, y, weight, dclass));
		}
		
		data.addSeries(series1);
		data.addSeries(series2);
	}
	
	
	 
}
