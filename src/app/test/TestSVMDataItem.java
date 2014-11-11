package app.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.model.data.SVMDataGenerator;
import app.model.data.SVMDataSet;

public class TestSVMDataItem {

	public static void main(String[] args) {
		
		SVMDataItemOLD2 o1 = new SVMDataItemOLD2(0.0027, 0);
		SVMDataItemOLD2 o2 = new SVMDataItemOLD2(0.002199999, 0);
		SVMDataItemOLD2 o3 = new SVMDataItemOLD2(0.0022, 0);
		
		SVMDataItemOLD2 normal = new SVMDataItemOLD2(1, 0);
		
		o1.project(normal);
		o2.project(normal);
		o3.project(normal);
		
		System.out.format("%f : % f \n", o1.getScProj(), o2.getScProj());
		System.out.format("o1.sc < o2.sc = %b\n", 
				SVMDataItemOLD2.isLessThan(o1.getScProj(), o2.getScProj()));
		System.out.format("o1.sc == o2.sc = %b\n", 
				SVMDataItemOLD2.isEqual(o1.getScProj(), o2.getScProj()));
		System.out.format("o1.sc > o2.sc = %b\n", 
				SVMDataItemOLD2.isGreaterThan(o1.getScProj(), o2.getScProj()));
		
		
		ArrayList<SVMDataItemOLD2> X = new ArrayList<SVMDataItemOLD2>();
		X.add(o1);
		X.add(o2);
		X.add(o3);
		
		
		Collections.sort(X,Collections.reverseOrder( new Comparator<SVMDataItemOLD2>() {

			@Override
			public int compare(SVMDataItemOLD2 o1, SVMDataItemOLD2 o2) {
				if (o1.getScProj() < o2.getScProj()){
					return -1;
				}else if (o1.getScProj() == o2.getScProj()){
					if (o1.getWeight() < o2.getWeight()){
						return -1;
					}else if(o1.getWeight() == o2.getWeight()){
						return 0;
					}else{
						return +1;
					}
				}else{
					return +1;
				}
			}
		}));
		
		
		
		
		
	}
}
