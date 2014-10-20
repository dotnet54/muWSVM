package app.model.data;

import java.util.ArrayList;

import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeriesCollection;

public class SVMDataSet extends XYSeriesCollection{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5941570772540357547L;

	private SVMDataSeries positiveSeries = null;
	public SVMDataSeries getPositiveSeries() {
		return positiveSeries;
	}

	public void setPositiveSeries(SVMDataSeries positiveSeries) {
		this.positiveSeries = positiveSeries;
	}

	public SVMDataSeries getNegativeSeries() {
		return negativeSeries;
	}

	public void setNegativeSeries(SVMDataSeries negativeSeries) {
		this.negativeSeries = negativeSeries;
	}

	private SVMDataSeries negativeSeries = null;
	
	public SVMDataSet(){	
		super();
		
		positiveSeries = new SVMDataSeries("Positive Class");
		negativeSeries = new SVMDataSeries("Negative Class");
	}
	
	public void addItem(int seriesIndex, SVMDataItem item){
		getSeries(seriesIndex).add(item);
	}
	
	public void removeItem(int seriesIndex, int itemIndex){
		getSeries(seriesIndex).remove(itemIndex);
	}

	
}
