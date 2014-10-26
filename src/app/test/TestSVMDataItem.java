package app.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.model.data.SVMDataGenerator;
import app.model.data.SVMDataItem;
import app.model.data.SVMDataSet;

public class TestSVMDataItem {

	public static void main(String[] args) {
		
		SVMDataItem o1 = new SVMDataItem(0.0027, 0);
		SVMDataItem o2 = new SVMDataItem(0.002199999, 0);
		SVMDataItem o3 = new SVMDataItem(0.0022, 0);
		
		SVMDataItem normal = new SVMDataItem(1, 0);
		
		o1.project(normal);
		o2.project(normal);
		o3.project(normal);
		
		System.out.format("%f : % f \n", o1.getScProj(), o2.getScProj());
		System.out.format("o1.sc < o2.sc = %b\n", 
				SVMDataItem.isLessThan(o1.getScProj(), o2.getScProj()));
		System.out.format("o1.sc == o2.sc = %b\n", 
				SVMDataItem.isEqual(o1.getScProj(), o2.getScProj()));
		System.out.format("o1.sc > o2.sc = %b\n", 
				SVMDataItem.isGreaterThan(o1.getScProj(), o2.getScProj()));
		
		
		ArrayList<SVMDataItem> X = new ArrayList<SVMDataItem>();
		X.add(o1);
		X.add(o2);
		X.add(o3);
		
		
		Collections.sort(X,Collections.reverseOrder( new Comparator<SVMDataItem>() {

			@Override
			public int compare(SVMDataItem o1, SVMDataItem o2) {
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
