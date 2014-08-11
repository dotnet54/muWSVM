package app.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class JFCPanel extends ChartPanel{

	public JFCPanel(JFreeChart chart) {
		super(chart);
		setMouseWheelEnabled(true);

	}

}
