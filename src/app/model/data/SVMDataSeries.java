package app.model.data;

import java.util.ArrayList;

import org.jfree.data.xy.XYSeries;

public class SVMDataSeries extends XYSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6695560134203970982L;

	
	public SVMDataSeries(Comparable key) {
		super(key);		
	}
	
	public ArrayList<SVMDataItem> toArrayList(){
		ArrayList<SVMDataItem> list = new ArrayList<SVMDataItem>();
		
		SVMDataItem item =  null;
		for (int i = 0; i < getItemCount(); i++){
			item = (SVMDataItem) getItems().get(i);
			list.add(item);
		}
		return list;
	}

}
