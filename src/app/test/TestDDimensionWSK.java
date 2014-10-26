package app.test;

import app.model.data.DData;
import app.model.data.DVector;
import app.model.data.Dwsk;

public class TestDDimensionWSK {

	public static void main(String[] args){
		try {
			
			DData datasource = new DData(2);
			
			
			//create two right angle triangles
			datasource.add(new DVector(1, 1, 1, +1));
			datasource.add(new DVector(3, 1, 1, +1));
			datasource.add(new DVector(3, 3, 1, +1));
			
			datasource.add(new DVector(5, 1, 1, -1));
			datasource.add(new DVector(6, 3, 1, -1));
			datasource.add(new DVector(7, 1, 1, -1));
			
			//datasource.generateRandomData(20);
			
			Dwsk solver = new Dwsk();

			solver.wsk(datasource, 1, 1);
			
			double offset = solver.getB() / solver.getW().getMagnitude();
			
			System.out.format("w:%s\nb:%.4f\noffset:%.4f", 
					solver.getW(), solver.getB(), offset);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
