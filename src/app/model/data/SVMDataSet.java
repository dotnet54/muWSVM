package app.model.data;

import java.util.ArrayList;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeriesCollection;

public class SVMDataSet extends XYSeriesCollection{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5941570772540357547L;

	public SVMDataSet(){	
		super();
	}
	
	public void addItem(int seriesIndex, SVMDataItem item){
		getSeries(seriesIndex).add(item);
	}
	
	public void removeItem(int seriesIndex, int itemIndex){
		getSeries(seriesIndex).remove(itemIndex);
	}

	
}
