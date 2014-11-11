package app.test;

import app.model.WSKSolver;
import app.model.data.SVMDataItem;

public class TestDDimensionWSK {

	public static void main(String[] args){
		try {
			
			DData datasource = new DData(2);
			
			
			//create two right angle triangles
			datasource.add(new SVMDataItem(1, 1, 1, +1));
			datasource.add(new SVMDataItem(3, 1, 1, +1));
			datasource.add(new SVMDataItem(3, 3, 1, +1));
			
			datasource.add(new SVMDataItem(5, 1, 1, -1));
			datasource.add(new SVMDataItem(6, 3, 1, -1));
			datasource.add(new SVMDataItem(7, 1, 1, -1));
			
			//datasource.generateRandomData(20);
			
			WSKSolver solver = new WSKSolver();

			solver.wsk(datasource, 1, 1);
			
			double offset = solver.getB() / solver.getW().getMagnitude();
			
			System.out.format("w:%s\nb:%.4f\noffset:%.4f", 
					solver.getW(), solver.getB(), offset);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
