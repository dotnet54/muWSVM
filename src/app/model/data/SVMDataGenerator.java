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
	
	public void setPredefinedDataset(SVMDataSet data, String name){
		data.clear();
		
		if (name == "Two points 1"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(3, 1, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(3, 4, 1, -1));
		}else if (name == "Collinear Points 1"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(3, 1, 1, 1));
			data.addItem(0, new SVMDataItem(6, 1, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(3, 4, 1, -1));
		}else if (name == "Collinear Points 2"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(3, 1.001, 1, 1));
			data.addItem(0, new SVMDataItem(6, 1, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(3, 4, 1, -1));
		}else if (name == "Triangle 1"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(1, 3, 1, 1));
			data.addItem(0, new SVMDataItem(3, 1, 1, 1));
			
			data.addItem(1, new SVMDataItem(4, 1, 1, -1));
		}else if (name == "Triangle 2"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(4, 4, 1, 1));
			data.addItem(0, new SVMDataItem(4, 1, 1, 1));
			
			data.addItem(1, new SVMDataItem(6, 4, 1, -1));
			data.addItem(1, new SVMDataItem(6, 1, 1, -1));
			data.addItem(1, new SVMDataItem(9, 1, 1, -1));
		}else if (name == "Parallelogram"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(4, 1, 1, 1));
			data.addItem(0, new SVMDataItem(2, 4, 1, 1));
			data.addItem(0, new SVMDataItem(5, 4, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
		}else if (name == "Trapezium"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(5, 1, 1, 1));
			data.addItem(0, new SVMDataItem(2, 3, 1, 1));
			data.addItem(0, new SVMDataItem(4, 3, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
		}else if (name == "T points"){
			data.addItem(0, new SVMDataItem(1, 1, 1, 1));
			data.addItem(0, new SVMDataItem(1, 3, 1, 1));
			data.addItem(0, new SVMDataItem(1, 5, 1, 1));
			data.addItem(0, new SVMDataItem(1, 3, 1, 1));
			data.addItem(0, new SVMDataItem(3, 3, 1, 1));
			data.addItem(0, new SVMDataItem(5, 3, 1, 1));
			
			data.addItem(1, new SVMDataItem(1, 4, 1, -1));
		}else {
			
		}
		
		
	}
	
	public void generateData(SVMDataSet data, int numDataPoints, 
				int percentPos, int softnessDelta){

		double minVal = 0;
		double maxVal = 10;
		
		
		SVMDataItem normal = new SVMDataItem(
				(randGen.nextDouble() * 2.0) - 1, 
				(randGen.nextDouble() * 2.0) - 1);
		normal.scale(maxVal);
		if (SVMMain.chartPanel != null){
			SVMMain.chartPanel.drawLine(normal); //TODO temporary
		}
		
		System.out.println(normal);
		int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
		int numNeg = numDataPoints - numPos;
		
		int softPos = (int) ((softnessDelta / 100.0) *  numPos);
		int softNeg = (int) ((softnessDelta / 100.0) *  numNeg);
		
		numPos -= softPos;
		numNeg -= softNeg;
		//TODO 100000 point scaling problem
		for (int k = 0; k < softPos; k++){
			data.addItem(0, new SVMDataItem(
					randGen.nextDouble()* maxVal, 
					randGen.nextDouble()* maxVal, 1, +1));
		}
		for (int k = 0; k < softNeg; k++){
			data.addItem(1, new SVMDataItem(
					randGen.nextDouble()* maxVal, 
					randGen.nextDouble()* maxVal, 1, -1));
		}
		
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
				//TODO possible infinite loop - use separation delta SERIOUS try do for loop
			}
		}
	}
	
	 
}
