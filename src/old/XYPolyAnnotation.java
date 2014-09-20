package old;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.RectangleEdge;

public class XYPolyAnnotation extends XYShapeAnnotation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public double xa,xb, ya,yb = 0;
	
	public XYPolyAnnotation(Shape shape) {
		super(shape);
	}

	
    /**
     * Draws the annotation.  This method is usually called by the
     * {@link XYPlot} class, you shouldn't need to call it directly.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param rendererIndex  the renderer index.
     * @param info  the plot rendering info.
     */
    @Override
    public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
                     ValueAxis domainAxis, ValueAxis rangeAxis,
                     int rendererIndex,
                     PlotRenderingInfo info) {

        PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                plot.getRangeAxisLocation(), orientation);

        // compute transform matrix elements via sample points. Assume no
        // rotation or shear.
        Rectangle2D bounds = this.shape.getBounds2D();
        double x0 = bounds.getMinX();
        double x1 = bounds.getMaxX();
        double xx0 = domainAxis.valueToJava2D(x0, dataArea, domainEdge);
        double xx1 = domainAxis.valueToJava2D(x1, dataArea, domainEdge);
//        double xx0 = xa;
//        double xx1 = xb;
        double m00 = (xx1 - xx0) / (x1 - x0);
        double m02 = xx0 - x0 * m00;

        double y0 = bounds.getMaxY();
        double y1 = bounds.getMinY();
        double yy0 =rangeAxis.valueToJava2D(y0, dataArea, rangeEdge);
        double yy1 = rangeAxis.valueToJava2D(y1, dataArea, rangeEdge);
//        double yy0 = yb;
//        double yy1 = ya;
        double m11 = (yy1 - yy0) / (y1 - y0);
        double m12 = yy0 - m11 * y0;

        //  create transform & transform shape
        Shape s = null;
        if (orientation == PlotOrientation.HORIZONTAL) {
            AffineTransform t1 = new AffineTransform(0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f);
            AffineTransform t2 = new AffineTransform(m11, 0.0f, 0.0f, m00,
                    m12, m02);
            s = t1.createTransformedShape(this.shape);
            s = t2.createTransformedShape(s);
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            AffineTransform t = new AffineTransform(m00, 0, 0, m11, m02, m12);
            s = t.createTransformedShape(this.shape);
        }
//s=this.shape;

        if (this.fillPaint != null) {
            g2.setPaint(this.fillPaint);
            g2.fill(s);
        }

        if (this.stroke != null && this.outlinePaint != null) {
            g2.setPaint(this.outlinePaint);
            g2.setStroke(this.stroke);
            g2.draw(s);
        }
        addEntity(info, s, rendererIndex, getToolTipText(), getURL());

    }
	
}
