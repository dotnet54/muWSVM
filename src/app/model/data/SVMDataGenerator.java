package app.model.data;

import java.util.Random;

import app.gui.SVMMain;
import app.gui.SVMPanel;

public class SVMDataGenerator {
	
	private SVMDataSet data;
	private Random randGen =  null;
	int numItems;
	
	public SVMDataGenerator(SVMModel model, int numItems, int numClasses) {
		
		long seed = System.currentTimeMillis();
		randGen = new Random(seed);
		
		//data = new SVMDataSet();
		

		
	}
	
	
	public void generateData(SVMDataSet data, int numDataPoints, 
				int percentPos, int softnessDelta){

		double minVal = 0;
		double maxVal = 10;
		double separationDelta = 0;
		
		
		SVMDataItem normal = new SVMDataItem(
				(randGen.nextDouble() * 2.0) - 1, 
				(randGen.nextDouble() * 2.0) - 1);
		normal.scale(maxVal);
		if (SVMMain.chartPanel != null){
			SVMMain.chartPanel.drawLine(normal);
		}
		
		System.out.println(normal);
		int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
		int numNeg = numDataPoints - numPos;
		
		int i = 0, j = 0;
		double x,y, proj;
		while(i < numPos ||  j < numNeg){
			
			x = (randGen.nextDouble() * 2.0) - 1;
			y = (randGen.nextDouble() * 2.0) - 1;
			double weight = 1;
			
			SVMDataItem point = new SVMDataItem(x, y, weight);
			
			proj = normal.getDotProduct(point);
			point.setX(point.getXValue() * maxVal);
			point.setY(point.getYValue() * maxVal);
			if ( proj> 0 && i < numPos){
				point.setDataClass(1);
				data.getSeries(0).add(point);
				i++;
			}else if (proj < 0 && j < numNeg){
				point.setDataClass(-1);
				data.getSeries(1).add(point);
				j++;
			}else{
				//TODO possible infinite loop - use separation delta
			}
		}
	}
	
	 
}
