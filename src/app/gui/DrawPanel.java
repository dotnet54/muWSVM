package app.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import app.model.algorithms.RCH;

public class DrawPanel extends JPanel 
	implements MouseListener, MouseMotionListener{
	
	private static ArrayList<Point> dataset1 = new ArrayList<Point>();
	private static ArrayList<Point> dataset2 = new ArrayList<Point>();
	private static double mu1 = 0.5;
	private static double mu2 = 0.5;
	
	public DrawPanel(ArrayList<Point> set1, ArrayList<Point> set2,
			double m1, double m2){
		
		dataset1 = set1;
		dataset2 = set2;
		mu1=m1;
		mu2=m2;
		
		addMouseListener(this);
		
		//TODO
//		addMouseListener(new PopUpListner(){
//			
//		});
	}
	
	
	public void setMu(double m1, double m2){
		mu1=m1;
		mu2=m2;
		repaint();
	}
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("redawing panel " + mu1 + ", " + mu2);
        
        if (dataset1.isEmpty())
        	return;
        if (dataset2.isEmpty())
        	return;
        
        paintSet1(g);
        paintSet2(g);
  
    }
    
    public void paintSet1(Graphics g) {
        
        ArrayList<Point> ch = new ArrayList<Point>();	//mem leak with new
        ArrayList<Point> rch = new ArrayList<Point>();
        
        Point p = new Point();
    	for (int i = 0; i < dataset1.size(); i++){
    		p = dataset1.get(i);
    		g.setColor(Color.BLACK);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.RED);
    		g.setFont(new Font("Arial", Font.PLAIN, 10));
    		//g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        //ch = QuickHull.QH(vertices);
    	ch = RCH.qrh(dataset1, 1.0, null, null, true);
        
        int[] xPoints = new int[ch.size()];
        int[] yPoints =  new int[ch.size()];
        
        for (int i = 0; i < ch.size(); i++){
        	xPoints[i] = (int) ch.get(i).x;
        	yPoints[i] = (int) ch.get(i).y;
      
        }
        
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch.size());
        Point c = RCH.findCentroid(dataset1);
    	g.drawOval(c.x, c.y, 3, 3);
    	g.drawString("c", c.x -4, c.y-4);
        

        rch = RCH.qrh(dataset1, mu1, null, null, true);
        
        xPoints = new int[rch.size()];
        yPoints =  new int[rch.size()];
        
        for (int i = 0; i < rch.size(); i++){
        	xPoints[i] = (int) rch.get(i).x;
        	yPoints[i] = (int) rch.get(i).y;
      
        }
        g.setColor(Color.RED);
        g.drawPolygon(xPoints, yPoints, rch.size());

    	for (int i = 0; i < rch.size(); i++){
    		p = rch.get(i);
    		g.setColor(Color.RED);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    	}

        c = RCH.findCentroid(rch);
    	g.drawOval(c.x, c.y, 3, 3);
    	g.drawString("c", c.x +4, c.y+4);

    }

    public void paintSet2(Graphics g) {
        
        ArrayList<Point> ch = new ArrayList<Point>();	//mem leak with new
        ArrayList<Point> rch = new ArrayList<Point>();
        
        Point p = new Point();
    	for (int i = 0; i < dataset2.size(); i++){
    		p = dataset2.get(i);
    		g.setColor(Color.BLACK);
    		g.drawOval((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.BLUE);
    		//g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        //ch = QuickHull.QH(vertices);
    	ch = RCH.qrh(dataset2, 1.0, null, null, true);
        
        int[] xPoints = new int[ch.size()];
        int[] yPoints =  new int[ch.size()];
        
        for (int i = 0; i < ch.size(); i++){
        	xPoints[i] = (int) ch.get(i).x;
        	yPoints[i] = (int) ch.get(i).y;
      
        }
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch.size());
        
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch.size());
        Point c = RCH.findCentroid(dataset2);
    	g.fillOval(c.x, c.y, 6, 6);
    	g.drawString("c", c.x -4, c.y-4);

        rch = RCH.qrh(dataset2, mu2, null, null, true);
        
        xPoints = new int[rch.size()];
        yPoints =  new int[rch.size()];
        
        for (int i = 0; i < rch.size(); i++){
        	xPoints[i] = (int) rch.get(i).x;
        	yPoints[i] = (int) rch.get(i).y;
      
        }
        g.setColor(Color.BLUE);
        g.drawPolygon(xPoints, yPoints, rch.size());
        
        if (rch.size() == 1){
        	g.drawOval(rch.get(0).x, rch.get(0).y, 3, 3);
        	g.drawString("c", rch.get(0).x +4, rch.get(0).y+4);
        }
        
    	for (int i = 0; i < rch.size(); i++){
    		p = rch.get(i);
    		g.setColor(Color.BLUE);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    	}
        
        c = RCH.findCentroid(rch);
    	g.fillOval(c.x, c.y, 6, 6);
    	g.drawString("c", c.x +4, c.y+4);
    }


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getX() + ", " + e.getY());
		if (SwingUtilities.isLeftMouseButton(e)){
			dataset1.add(new Point(e.getX(), e.getY()));
			
		}else{
			dataset2.add(new Point(e.getX(), e.getY()));
		}
		repaint();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
    
}
