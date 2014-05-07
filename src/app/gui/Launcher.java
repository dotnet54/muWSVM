package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.model.algorithms.QuickHull;
import app.model.algorithms.RCH;
import app.model.algorithms.ReducedQuickHull;

public class Launcher extends JPanel
					  implements ChangeListener{
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15;    //initial frames per second
 
	private double mu = 0.5;

	public static void main(String[] args) {
		JFrame app = new JFrame();
		Launcher panel = new Launcher();
		
		app.setContentPane(panel);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(new Dimension(600, 400));
		app.setVisible(true);
	}

	ArrayList<Point> vertices = new ArrayList<Point>();
	Point[] points;
	Random randomGenerator = new Random();
	
	
	Launcher(){
		int max = 20;		
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 1,
				max, 1);
		slider.addChangeListener(this);

		// Turn on labels at major tick marks.
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		
		
		add(slider);
		
		
		
		
		
		
		
		setBackground(new Color(255,255,255));
		


		points = new Point[5];
		points[0] = new Point(100,50);
		points[1] = new Point(100,100);
		points[2] = new Point(200,200);
		points[3] = new Point(70,150);
		points[4] = new Point(300,50);

		vertices.add(points[0]);
		vertices.add(points[1]);
		vertices.add(points[2]);
		vertices.add(points[3]);
		vertices.add(points[4]);
		
//		vertices.clear();
//		points[0] = new Point(300,200);
//		points[1] = new Point(100,100);
//		points[2] = new Point(500,100);
//		
//
//		vertices.add(points[0]);
//		vertices.add(points[1]);
//		vertices.add(points[2]);

		for (int i = 0; i < max; i++){
			Point p = new Point();
			p.setLocation(randomGenerator.nextInt(400)+50, randomGenerator.nextInt(200)+50); //randomGenerator.nextInt(400);
			vertices.add(p);
		}
		
	}
	
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        ArrayList<Point> ch = new ArrayList<Point>();	//mem leak with new
        ArrayList<Point> rch = new ArrayList<Point>();
        
        Point p = new Point();
    	for (int i = 0; i < vertices.size(); i++){
    		p = vertices.get(i);
    		g.setColor(Color.BLACK);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.RED);
    		g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        ch = QuickHull.QH(vertices);
        
        int[] xPoints = new int[ch.size()];
        int[] yPoints =  new int[ch.size()];
        
        for (int i = 0; i < ch.size(); i++){
        	xPoints[i] = (int) ch.get(i).x;
        	yPoints[i] = (int) ch.get(i).y;
      
        }
        //g.drawPolygon(xPoints, yPoints, ch.size());
        
        g.setColor(Color.GREEN);
        //rch = ReducedQuickHull.RQH(vertices, .5);
        rch = RCH.qrh(vertices, mu, null, null, true);
        
        xPoints = new int[rch.size()];
        yPoints =  new int[rch.size()];
        
        for (int i = 0; i < rch.size(); i++){
        	xPoints[i] = (int) rch.get(i).x;
        	yPoints[i] = (int) rch.get(i).y;
      
        }
        g.setColor(Color.BLUE);
        g.drawPolygon(xPoints, yPoints, rch.size());
    }


	@Override
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
             System.out.println(" slider -> m = " + source.getValue() 
            		 + ", 1/mu = " + 1.0/source.getValue());
             mu = 1.0/source.getValue();
             repaint();
        }
	}
}














