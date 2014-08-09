import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class AppMain extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static Point points[];
	Random randomGenerator = new Random();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame app = new JFrame();
		AppMain panel = new AppMain();
		
		app.setContentPane(panel);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(new Dimension(600, 400));
		app.setVisible(true);
		
	}
	
	AppMain(){
		setBackground(new Color(255,255,255));
		
		int max = 20;
		
//		points = new Point[max];
//		
//		for (int i = 0; i < max; i++){
//			//points[i].x = randomGenerator.nextInt(600);
//			Point p = new Point();
//			p.setLocation(randomGenerator.nextInt(400)+50, randomGenerator.nextInt(200)+50); //randomGenerator.nextInt(400);
//			points[i] = p;
//		}
		
		points = new Point[5];
		points[0] = new Point(100,50);
		points[1] = new Point(100,100);
		points[2] = new Point(200,200);
		points[3] = new Point(70,150);
		points[4] = new Point(300,50);
	}
	
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        
        Point p = new Point();
    	for (int i = 0; i < points.length; i++){
    		p = points[i];
    		g.setColor(Color.BLACK);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.RED);
    		g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}
        
        convexHull(points,g);
        
    }

	public void convexHull(Point[] points, Graphics  g)
    {
		ArrayList<Integer> tt = new ArrayList<Integer>();
        int n = points.length;
     
        //basics
        if (n < 3) 
            return;     
        int[] next = new int[n];
        Arrays.fill(next, -1);
 
        
        //find the left most point
        int leftMost = 0;
        for (int i = 1; i < n; i++){
        	if (points[i].x < points[leftMost].x){
            	leftMost = i;
            }
        }  
        int p = leftMost, q;
        do
        {
            q = (p + 1) % n;
            for (int i = 0; i < n; i++){
              if (CCW(points[p], points[i], points[q])){
                 q = i;
              }
            }
 
            next[p] = q;
            tt.add(q);
            p = q; 
        } while (p != leftMost);
 
   

        
        ArrayList<Integer> nn = new ArrayList<Integer>();
        System.out.println("next :");
    	for (int j = 0; j < next.length; j++){
    		System.out.print(" " + next[j]);
    		if (next[j] != -1){
    			nn.add(next[j]);
    		}
    	}
    	
    	 System.out.println("\nnn :");
        for (int i = 0; i < nn.size(); i++){
        	g.setColor(Color.BLUE);
        	int k = nn.get(i);
        	System.out.print(" " + k + "("+ points[k].x +", "+ points[k].y +")");
        	
        	g.fillRect((int) points[k].getX(), (int) points[k].getY(), 5, 5);
        	g.setColor(Color.RED);
            //g.drawString(i + "", (int) points[k].getX(), (int) points[k].getY());
        }
       
        Point currentPoint, nextPoint;
        g.setColor(Color.GREEN);
        
        currentPoint = points[tt.get(0)];
        
        
        int[] xPoints = new int[tt.size()];
        int[] yPoints =  new int[tt.size()];
        
        for (int i = 0; i < tt.size(); i++){
        	xPoints[i] = (int) points[tt.get(i)].getX();
        	yPoints[i] = (int) points[tt.get(i)].getY();
      
        }
        
        g.drawPolygon(xPoints,yPoints,tt.size());
        
        
        
//        for (int i = 0; i < tt.size(); i++){
//        	//g.drawLine((int)points[leftMost].getX(),
//        	//		(int) points[leftMost].getY(),
//        	//		(int)currentPoint.getX(), (int)currentPoint.getY());
//        	
//        	nextPoint = points[tt.get(i)];
//        	g.drawLine((int)currentPoint.getX(), (int)currentPoint.getY(), 
//        			(int)nextPoint.getX(),(int) nextPoint.getY());
//            
//        	currentPoint = nextPoint;
//        }
        


    }


	private boolean CCW(Point p, Point q, Point r)
    {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
 
         if (val >= 0)
             return false;
         return true;
    }
	
	public void quickHull(){
		
		
	}
	
	
	public Point[] qrh(Point[] P, double miu){
		return P;
		
	}
	
	public Point[] qrhHelpher(Point[] P, Point l, Point r){
		return P;
		
		
	}

}
