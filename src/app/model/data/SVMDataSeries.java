package app.model.data;

import java.util.ArrayList;

import org.jfree.data.xy.XYSeries;

public class SVMDataSeries extends XYSeries {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6695560134203970982L;

	ArrayList<SVMDataItem> items = null;
	
	public SVMDataSeries(Comparable key) {
		super(key);
		
		items = new ArrayList<SVMDataItem>();
	}

}
