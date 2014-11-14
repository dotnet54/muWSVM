package app.model.data;

import java.util.Random;

import app.gui.WSVMMain;
import app.gui.WSVMPanel;
import app.model.WSVMModel;

public class WSVMDataGenerator {
	
	private WSVMDataSet data;
	private Random randGen =  null;
	int numItems;
	int numDimensions;
	
	public WSVMDataGenerator(WSVMModel model, int numItems, int numDims, int numClasses) {
		
		long seed = System.currentTimeMillis();
		randGen = new Random(seed);
		
		this.numItems = numItems;
		this.numDimensions = numDims;
		
		//data = new SVMDataSet();
		

		
	}
	
	public static WSVMDataSet getDefaultSVMDataset(int numDimensions){
		WSVMDataSet dataset = new WSVMDataSet(numDimensions);
		try {
			dataset.addSeries(new WSVMDataSeries("Positive Class", dataset.getDimensions()));
			dataset.addSeries(new WSVMDataSeries("Negative Class", dataset.getDimensions()));
			
			dataset.setXDimension(0);
			dataset.setYDimension(1);
			
			dataset.setPositiveSeriesID(0);
			dataset.setNegativeSeriesID(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
	}
	
	public void setPredefinedDataset(WSVMDataSet data, String name){
		data.clearData();
		
		try {
			if (name == "Two points 1"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(3, 1, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(3, 4, 1, -1));
			}else if (name == "Collinear Points 1"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(3, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(6, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(4, 1, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 1, 1, -1));
				data.addItem(1, new WSVMDataItem(2, 2, 1, -1));
				data.addItem(1, new WSVMDataItem(3, 3, 1, -1));
				data.addItem(1, new WSVMDataItem(4, 4, 1, -1));
			}else if (name == "Collinear Points 2"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(3, 1.001, 1, 1));
				data.addItem(0, new WSVMDataItem(6, 1, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(3, 4, 1, -1));
			}else if (name == "Triangle 1"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(1, 3, 1, 1));
				data.addItem(0, new WSVMDataItem(3, 1, 1, 1));
				
				data.addItem(1, new WSVMDataItem(4, 1, 1, -1));
			}else if (name == "Triangle 2"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(4, 4, 1, 1));
				data.addItem(0, new WSVMDataItem(4, 1, 1, 1));
				
				data.addItem(1, new WSVMDataItem(6, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(6, 1, 1, -1));
				data.addItem(1, new WSVMDataItem(9, 1, 1, -1));
			}else if (name == "Parallelogram"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(4, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(2, 4, 1, 1));
				data.addItem(0, new WSVMDataItem(5, 4, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
			}else if (name == "Trapezium"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(5, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(2, 3, 1, 1));
				data.addItem(0, new WSVMDataItem(4, 3, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
			}else if (name == "T points"){
				data.addItem(0, new WSVMDataItem(1, 1, 1, 1));
				data.addItem(0, new WSVMDataItem(1, 4, 2, 1));
				data.addItem(0, new WSVMDataItem(1, 5, 1, 1));
				data.addItem(0, new WSVMDataItem(1, 3, 1, 1));
				data.addItem(0, new WSVMDataItem(3, 3, 1, 1));
				data.addItem(0, new WSVMDataItem(5, 3, 1, 1));
				
				data.addItem(1, new WSVMDataItem(1, 4, 1, -1));
			}else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void generateDataSeries(WSVMDataSet data, int seriesIndex, int numDataPoints, 
			int percentPos, String svmType, 
			double minVal, double maxVal, double minWeight, double maxWeight){
		try {
			if (minWeight < 0){
				minWeight = 0;
			}		
			WSVMDataItem newVec = null;
			double values[] = new double[numDimensions];
			int i = 0;
			for (i = 0;  i < numDataPoints; i++){
				for (int d = 0; d < numDimensions; d++){
					values[d] = getRandom(minVal, maxVal);
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), seriesIndex); 
				data.getSeries(seriesIndex).add(newVec, false);
			}
			
			data.notifyDataChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	
	public void generateDataHardMargin(WSVMDataSet data, int numDataPoints, 
			int percentPos, double minVal, double maxVal, double minWeight, double maxWeight){
		
		try {
			double mid = ((maxVal - minVal) / 2.0);;
			if (minWeight < 0){
				minWeight = 0;
			}
			WSVMDataItem newVec = null;
			double values[] = new double[numDimensions];
			int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
			int numNeg = numDataPoints - numPos;			
			for (int i = 0;  i < numPos; i++){
				for (int d = 0; d < numDimensions; d++){
					if (d == 0){
						values[d] = getRandom(minVal, mid);
					}else{
						values[d] = getRandom(minVal, maxVal);
					}
					
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), +1); 
				data.getSeries(data.getPositiveSeriesID()).add(newVec, false);
			}
			
			for (int i = 0;  i < numNeg; i++){
				for (int d = 0; d < numDimensions; d++){
					if (d == 0){
						values[d] = getRandom(mid, maxVal);
					}else{
						values[d] = getRandom(minVal, maxVal);
					}
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), -1); 
				data.getSeries(data.getNegativeSeriesID()).add(newVec, false);
			}
			
			data.notifyDataChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void generateDataSoftMargin(WSVMDataSet data, int numDataPoints, 
				int percentPos, int softnessDelta, 
				double minVal, double maxVal, double minWeight, double maxWeight){

		try {
			
			if (minWeight < 0){
				minWeight = 0;
			}
			
			WSVMDataItem newVec = null;
			double values[] = new double[numDimensions];
			for (int d = 0; d < numDimensions; d++){
				values[d] = getRandom(minVal, maxVal);
			}
			
			WSVMDataItem normal = new WSVMDataItem(values, 1, 1);
			normal.multiplyByScaler(maxVal);
			
			
			int numPos = (int) ((percentPos / 100.0) *  numDataPoints);
			int numNeg = numDataPoints - numPos;
			
			int softPos = (int) ((softnessDelta / 100.0) *  numPos);
			int softNeg = (int) ((softnessDelta / 100.0) *  numNeg);
			
			numPos -= softPos;
			numNeg -= softNeg;
			

			
			for (int k = 0; k < softPos; k++){
				for (int d = 0; d < numDimensions; d++){
					values[d] = getRandom(minVal, maxVal);
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), +1); 
				data.getSeries(data.getPositiveSeriesID()).add(newVec, false);
			}
			for (int k = 0; k < softNeg; k++){
				for (int d = 0; d < numDimensions; d++){
					values[d] = getRandom(minVal, maxVal);
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), +1); 
				data.getSeries(data.getNegativeSeriesID()).add(newVec, false);
			}
			
			int i = 0, j = 0;
			double proj;
			int total = numPos + numNeg;
			
			for (i = 0;  i < total; i++){
				for (int d = 0; d < numDimensions; d++){
					values[d] = getRandom(minVal, maxVal);
				}
				newVec = new WSVMDataItem(values, getRandom(minWeight, maxWeight), +1); 
				proj = normal.getDotProduct(newVec);
				
				if (j < numPos && proj > 0){
					newVec.setClassID(1);
					data.getSeries(data.getPositiveSeriesID()).add(newVec, false);
					j++;
				}else{
					newVec.setClassID(-1);
					data.getSeries(data.getNegativeSeriesID()).add(newVec, false);
				}
			}
			
			data.notifyDataChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double getRandom(double min, double max){
		return min + (randGen.nextDouble() * (max - min));
	}
	public int getRandom(int min, int max){
		return randGen.nextInt((max - min) + 1) + min;
	}

}
