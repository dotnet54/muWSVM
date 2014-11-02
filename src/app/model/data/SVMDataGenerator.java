package app.model.data;

import java.util.Random;

import app.gui.SVMPanel;

public class SVMDataGenerator {
	
	private SVMDataSet data;
	private Random randGen =  null;
	int numItems;
	int numDimensions;
	
	public SVMDataGenerator(SVMModel model, int numItems, int numDims, int numClasses) {
		
		long seed = System.currentTimeMillis();
		randGen = new Random(seed);
		
		this.numItems = numItems;
		this.numDimensions = numDims;
		
		//data = new SVMDataSet();
		

		
	}
	
	public static SVMDataSet getDefaultSVMDataset(){
		SVMDataSet dataset = new SVMDataSet(2);
		try {
			dataset.addSeries(new SVMDataSeries("Positive Class", dataset.getDimensions()));
			dataset.addSeries(new SVMDataSeries("Negative Class", dataset.getDimensions()));
			
			dataset.setXDimension(0);
			dataset.setYDimension(1);
			
			dataset.setPositiveSeriesID(0);
			dataset.setNegativeSeriesID(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}
	
	public void setPredefinedDataset(SVMDataSet data, String name){
		data.clearData();
		
		try {
			if (name == "Two points 1"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(3, 1, 1, 1));
				
				data.addItem(1, new DVector(1, 4, 1, -1));
				data.addItem(1, new DVector(3, 4, 1, -1));
			}else if (name == "Collinear Points 1"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(3, 1, 1, 1));
				data.addItem(0, new DVector(6, 1, 1, 1));
				data.addItem(0, new DVector(4, 1, 1, 1));
				
				data.addItem(1, new DVector(1, 1, 1, -1));
				data.addItem(1, new DVector(2, 2, 1, -1));
				data.addItem(1, new DVector(3, 3, 1, -1));
				data.addItem(1, new DVector(4, 4, 1, -1));
			}else if (name == "Collinear Points 2"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(3, 1.001, 1, 1));
				data.addItem(0, new DVector(6, 1, 1, 1));
				
				data.addItem(1, new DVector(1, 4, 1, -1));
				data.addItem(1, new DVector(3, 4, 1, -1));
			}else if (name == "Triangle 1"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(1, 3, 1, 1));
				data.addItem(0, new DVector(3, 1, 1, 1));
				
				data.addItem(1, new DVector(4, 1, 1, -1));
			}else if (name == "Triangle 2"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(4, 4, 1, 1));
				data.addItem(0, new DVector(4, 1, 1, 1));
				
				data.addItem(1, new DVector(6, 4, 1, -1));
				data.addItem(1, new DVector(6, 1, 1, -1));
				data.addItem(1, new DVector(9, 1, 1, -1));
			}else if (name == "Parallelogram"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(4, 1, 1, 1));
				data.addItem(0, new DVector(2, 4, 1, 1));
				data.addItem(0, new DVector(5, 4, 1, 1));
				
				data.addItem(1, new DVector(1, 4, 1, -1));
				data.addItem(1, new DVector(1, 4, 1, -1));
				data.addItem(1, new DVector(1, 4, 1, -1));
				data.addItem(1, new DVector(1, 4, 1, -1));
			}else if (name == "Trapezium"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(5, 1, 1, 1));
				data.addItem(0, new DVector(2, 3, 1, 1));
				data.addItem(0, new DVector(4, 3, 1, 1));
				
				data.addItem(1, new DVector(1, 4, 1, -1));
			}else if (name == "T points"){
				data.addItem(0, new DVector(1, 1, 1, 1));
				data.addItem(0, new DVector(1, 4, 2, 1));
				data.addItem(0, new DVector(1, 5, 1, 1));
				data.addItem(0, new DVector(1, 3, 1, 1));
				data.addItem(0, new DVector(3, 3, 1, 1));
				data.addItem(0, new DVector(5, 3, 1, 1));
				
				data.addItem(1, new DVector(1, 4, 1, -1));
			}else {
				
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		
		
	}
	
	
	public void generateDataSeries(SVMDataSet data, int seriesIndex, int numDataPoints, 
			int percentPos, int softnessDelta, double minVal, double maxVal){

	try {

		
		DVector normal = new DVector(
				(randGen.nextDouble() * 1.0) - 1, 
				(randGen.nextDouble() * 1.0) - 1);
		normal.multiplyByScaler(maxVal);
		
//		if (SVMMain.chartPanel != null){
//			SVMMain.chartPanel.drawLine(normal); //TODO temporary
//		}
		
		//System.out.println(normal);
		int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
		int numNeg = numDataPoints - numPos;
		
		int softPos = (int) ((softnessDelta / 100.0) *  numPos);
		int softNeg = (int) ((softnessDelta / 100.0) *  numNeg);
		
		numPos -= softPos;
		numNeg -= softNeg;
		//TODO 100000 point scaling problem
		for (int k = 0; k < softPos; k++){
			data.addItem(0, new DVector(
					randGen.nextDouble()* maxVal, 
					randGen.nextDouble()* maxVal, 1, +1));
		}
		for (int k = 0; k < softNeg; k++){
			data.addItem(1, new DVector(
					randGen.nextDouble()* maxVal, 
					randGen.nextDouble()* maxVal, 1, -1));
		}
		
		int i = 0, j = 0;
		double x,y, proj;
		while(i < numPos ||  j < numNeg){
			
			x = (randGen.nextDouble() * 2.0) - 1;
			y = (randGen.nextDouble() * 2.0) - 1;
			double weight = 1;
			
			DVector point = new DVector(x, y, weight);
			
			proj = normal.getDotProduct(point);
			point.setX(point.getXValue() * maxVal);
			point.setY(point.getYValue() * maxVal);
			if ( proj> 0 && i < numPos){
				point.setClassID(1);
				data.getSeries(seriesIndex).add(point);
				i++;
			}else if (proj < 0 && j < numNeg){
				point.setClassID(-1);
				data.getSeries(seriesIndex).add(point);
				j++;
			}else{
				//TODO possible infinite loop - use separation delta SERIOUS try do for loop
			}
		}
	} catch (Exception e) {
		// TODO
		e.printStackTrace();
	}
}
	
	
	
	
	
	
	
	
	public void generateData(SVMDataSet data, int numDataPoints, 
				int percentPos, int softnessDelta, double minVal, double maxVal){

		try {

			
			DVector normal = new DVector(
					(randGen.nextDouble() * 1.0) - 1, 
					(randGen.nextDouble() * 1.0) - 1);
			normal.multiplyByScaler(maxVal);
			
//			if (SVMMain.chartPanel != null){
//				SVMMain.chartPanel.drawLine(normal); //TODO temporary
//			}
			
			//System.out.println(normal);
			int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
			int numNeg = numDataPoints - numPos;
			
			int softPos = (int) ((softnessDelta / 100.0) *  numPos);
			int softNeg = (int) ((softnessDelta / 100.0) *  numNeg);
			
			numPos -= softPos;
			numNeg -= softNeg;
			//TODO 100000 point scaling problem
			for (int k = 0; k < softPos; k++){
				data.addItem(0, new DVector(
						randGen.nextDouble()* maxVal, 
						randGen.nextDouble()* maxVal, 1, +1));
			}
			for (int k = 0; k < softNeg; k++){
				data.addItem(1, new DVector(
						randGen.nextDouble()* maxVal, 
						randGen.nextDouble()* maxVal, 1, -1));
			}
			
			int i = 0, j = 0;
			double x,y, proj;
			while(i < numPos ||  j < numNeg){
				
				x = (randGen.nextDouble() * 2.0) - 1;
				y = (randGen.nextDouble() * 2.0) - 1;
				double weight = 1;
				
				DVector point = new DVector(x, y, weight);
				
				proj = normal.getDotProduct(point);
				point.setX(point.getXValue() * maxVal);
				point.setY(point.getYValue() * maxVal);
				if ( proj> 0 && i < numPos){
					point.setClassID(1);
					data.getSeries(0).add(point);
					i++;
				}else if (proj < 0 && j < numNeg){
					point.setClassID(-1);
					data.getSeries(1).add(point);
					j++;
				}else{
					//TODO possible infinite loop - use separation delta SERIOUS try do for loop
				}
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
	
	 
}
