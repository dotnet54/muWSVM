package old;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


import app.model.algorithms.WRCH;
import app.model.data.SVMDataItem;
import app.model.data.SVMModel;

public class DrawPanel extends JPanel 
	implements MouseListener, MouseMotionListener{
	
	private static SVMModel model = null;
	private BGTask bg = null;
	
	
	public  ArrayList<Point> ch1 = new ArrayList<Point>();
    public  ArrayList<Point> rch1 = new ArrayList<Point>();
    
	public  ArrayList<Point> ch2 = new ArrayList<Point>();
    public  ArrayList<Point> rch2 = new ArrayList<Point>();
	
	public DrawPanel(SVMModel model){
		
		this.model = model;
		
		addMouseListener(this);
		
		//TODO
//		addMouseListener(new PopUpListner(){
//			
//		});
		
        bg = new BGTask(this, model);
        bg.execute();
	}
	
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //System.out.println("redawing panel " + mu1 + ", " + mu2);
        
        if (model.dataset1.isEmpty())
        	return;
        if (model.dataset2.isEmpty())
        	return;
        

        Graphics2D g2 = (Graphics2D) g;
        g2.scale(1,1);
        paintSet1(g);
        paintSet2(g);
        paintHyperPlanes(g2);
  
    }
    
    public void paintHyperPlanes(Graphics2D g2){
    	SVMDataItem w = model.w; //	new RHull.DP(-1000, -2000);//TODO w is Point
        double b =model.b; //-180;
        

        
        int yMin = -500;
        int yMax = 500;
        
//        RHull.DP h = new RHull.DP(-w.y, w.x);
        
        double xMin = ((b-(w.getYValue()*yMin))/w.getXValue());
        double xMax =   ((b-(w.getYValue()*yMax))/w.getXValue());
        
        System.out.println("---------drawing with-----------");
		System.out.println("w = " + w.getXValue() + ", "+w.getYValue());
		System.out.println("b = " + b);
		System.out.format("Line: [%s, %s] to [%s, %s]\n", xMin, yMin, xMax, yMax);
		System.out.println("---------drawing with-----------");        
        Point to = new Point((int)xMin, yMin);
        Point bo = new Point((int)xMax, yMin);
        
//        g2.translate(getWidth()/2, getHeight()/2);
        
        g2.drawLine((int)xMin, yMin,(int) xMax, yMax);
//        
//        g2.setColor(Color.GRAY);
//         xMin = (int) ((b-10-(h.y*yMin))/h.x);
//         xMax = (int) ((b-10-(h.y*yMax))/h.x);
//        g2.drawLine(xMin, yMin, xMax, yMax);
//        
//        g2.setColor(Color.GRAY);
//        xMin = (int) ((b+10-(h.y*yMin))/h.x);
//        xMax = (int) ((b+10-(h.y*yMax))/h.x);
//       g2.drawLine(xMin, yMin, xMax, yMax);
//       
//       g2.setColor(Color.DARK_GRAY);
//       xMin = (int) ((b-(w.y*yMin))/w.x);
//       xMax = (int) ((b-(w.y*yMax))/w.x);
//      g2.drawLine(xMin, yMin, xMax, yMax);
    }

    
    
    public void paintSet1(Graphics g) {
        
        Point p = new Point();
    	for (int i = 0; i < model.dataset1.size(); i++){
    		p = model.dataset1.get(i);
    		g.setColor(Color.BLACK);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.RED);
    		g.setFont(new Font("Arial", Font.PLAIN, 10));
    		//g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        //ch = WRCH.WRCH(dataset1);
    	//ch = RCH.qrh(dataset1, 1.0, null, null, true);
    	//ch = RHull.rhull(model.dataset1, 1.0);
        

        int[] xPoints = new int[ch1.size()];
        int[] yPoints =  new int[ch1.size()];
        
        for (int i = 0; i < ch1.size(); i++){
        	xPoints[i] = (int) ch1.get(i).x;
        	yPoints[i] = (int) ch1.get(i).y;
      
        }

        
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch1.size());
        Point c = RCH_old.findCentroid(model.dataset1);
    	g.drawOval(c.x, c.y, 3, 3);
    	g.drawString("c", c.x -4, c.y-4);
        

//        xPoints = new int[model.dataset1.size()];
//        yPoints =  new int[model.dataset1.size()];
//        
//        
//        for (int i = 0; i < model.dataset1.size(); i++){
//        	xPoints[i] = (int) model.dataset1.get(i).x;
//        	yPoints[i] = (int) model.dataset1.get(i).y;
//      
//        }
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setColor(Color.DARK_GRAY);
//        g2.setStroke(new BasicStroke(3));
//        g2.drawPolygon(xPoints, yPoints, model.dataset1.size());
//        g2.setStroke(new BasicStroke(1));
    	
    	 //rch = WRCH.WRCH(dataset1);
        //rch = RCH.qrh(dataset1, mu1, null, null, true);
        //rch = RHull.rhull(model.dataset1, 0.5);
        
    	
        xPoints = new int[rch1.size()];
        yPoints =  new int[rch1.size()];
        
        for (int i = 0; i < rch1.size(); i++){
        	xPoints[i] = (int) rch1.get(i).x;
        	yPoints[i] = (int) rch1.get(i).y;
      
        }
        g.setColor(Color.RED);
        g.drawPolygon(xPoints, yPoints, rch1.size());

    	for (int i = 0; i < rch1.size(); i++){
    		p = rch1.get(i);
    		g.setColor(Color.RED);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    	}

        c = RCH_old.findCentroid(rch1);
        if (c != null){//TODO
        	g.drawOval(c.x, c.y, 3, 3);
        	g.drawString("c", c.x +4, c.y+4);
        }
    	

    }

    public void paintSet2(Graphics g) {
        
        Point p = new Point();
    	for (int i = 0; i < model.dataset2.size(); i++){
    		p = model.dataset2.get(i);
    		g.setColor(Color.BLACK);
    		g.drawOval((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.BLUE);
    		//g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

    	 //ch2 = WRCH.WRCH(dataset1);
    	//ch2 = RCH.qrh(dataset2, 1.0, null, null, true);
    	ch2 = WRCH.calcWeightedReducedCHull(model.dataset2, 1.0);
        
        int[] xPoints = new int[ch2.size()];
        int[] yPoints =  new int[ch2.size()];
        
        for (int i = 0; i < ch2.size(); i++){
        	xPoints[i] = (int) ch2.get(i).x;
        	yPoints[i] = (int) ch2.get(i).y;
      
        }
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch2.size());
        
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch2.size());
        Point c = RCH_old.findCentroid(model.dataset2);
    	g.fillOval(c.x, c.y, 6, 6);
    	g.drawString("c", c.x -4, c.y-4);

    	//rch2 = WRCH.WRCH(dataset1);
        //rch2 = RCH.qrh(dataset2, mu2, null, null, true);
        rch2 = WRCH.calcWeightedReducedCHull(model.dataset2, 0.5);
        
        xPoints = new int[rch2.size()];
        yPoints =  new int[rch2.size()];
        
        for (int i = 0; i < rch2.size(); i++){
        	xPoints[i] = (int) rch2.get(i).x;
        	yPoints[i] = (int) rch2.get(i).y;
      
        }
        g.setColor(Color.BLUE);
        g.drawPolygon(xPoints, yPoints, rch2.size());
        
        if (rch2.size() == 1){
        	g.drawOval(rch2.get(0).x, rch2.get(0).y, 3, 3);
        	g.drawString("c", rch2.get(0).x +4, rch2.get(0).y+4);
        }
        
    	for (int i = 0; i < rch2.size(); i++){
    		p = rch2.get(i);
    		g.setColor(Color.BLUE);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    	}
        
        c = RCH_old.findCentroid(rch2);
    	g.fillOval(c.x, c.y, 6, 6);
    	g.drawString("c", c.x +4, c.y+4);
    }

    //TODO IMPORTANT mouse clicks dont work if u move/drag mouse
    //even by a few pixels which meakes jpanel miss mose licks
    //use mouse released event
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

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
		System.out.println(e.getX() + ", " + e.getY());
		if (SwingUtilities.isLeftMouseButton(e)){
			model.dataset1.add(new Point(e.getX(), e.getY()));
			
		}else{
			model.dataset2.add(new Point(e.getX(), e.getY()));
		}
		
        bg = new BGTask(this, model);
        bg.execute();
		repaint();
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
