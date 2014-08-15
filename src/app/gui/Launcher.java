package app.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import old.QuickHull;
import old.ReducedQuickHull;

import app.model.algorithms.RCH;
import app.model.algorithms.RHull;

public class Launcher extends JPanel
					  implements ChangeListener{
 
	private static double mu = 0.5;
	private static int max = 4;	
	private static Launcher panel;
	private static ArrayList<Point> dataset1 = new ArrayList<Point>();
	private static ArrayList<Point> dataset2 = new ArrayList<Point>();
	
	private static Point[] points;
	private static Random randomGenerator = new Random();
	final static JTextField text = new JTextField();
	
	
	public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        		JFrame app = new JFrame();

        		panel = new Launcher();
        		
        		app.setContentPane(panel);
        		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        		app.setSize(new Dimension(600, 400));
        		app.setVisible(true);
        		//app.revalidate();
            }
        });
		
	}



	
	
	Launcher(){
		
		CreateUI();
		
		points = new Point[max];
		points[0] = new Point(0,0);
		points[1] = new Point(100,0);
		points[2] = new Point(0,100);


		dataset1.add(points[0]);
		dataset1.add(points[1]);
		dataset1.add(points[2]);

		dataset2.add(points[0]);
		dataset2.add(points[1]);
		dataset2.add(points[2]);

		dataset1.clear();
		dataset2.clear();
		
		for (int i = 0; i < max; i++){
			Point p = new Point();
			p.setLocation(randomGenerator.nextInt(400), randomGenerator.nextInt(200)); //randomGenerator.nextInt(400);
			dataset1.add(p);
		}
		
		for (int i = 0; i < max; i++){
			Point p = new Point();
			p.setLocation(randomGenerator.nextInt(400)+50, randomGenerator.nextInt(200)+50); //randomGenerator.nextInt(400);
			dataset2.add(p);
		}
	}
	
	public void CreateUI(){
		setBackground(new Color(255,255,255));
		
		int maxLabel = (int) (20 / (double)max);
		System.out.println("maxLabel = " + maxLabel);
		
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 1,
				20,1);
		slider.addChangeListener(this);

		// Turn on labels at major tick marks.
		slider.setMajorTickSpacing(2);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		//slider.setPaintLabels(true);
		
		
		//add(slider);
		
		
		text.setColumns(5);
		text.setText("0.5");
		add(text);
		
		JButton btn = new JButton();
		btn.setText("Draw Hull");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				mu = Double.parseDouble(text.getText());
				
				System.out.println("mu = " + mu);
				
				if ( Math.floor(1/mu) > dataset1.size()){
					System.out.println("mu = " + mu 
							+ ", Invalid mu Hull Undefined, " + Math.ceil(1/mu));
				}else{
					repaint();
				}
				
			}
		});
		add(btn);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (SwingUtilities.isLeftMouseButton(e)){
					dataset1.add(new Point(e.getX(), e.getY()));
					
				}else{
					dataset2.add(new Point(e.getX(), e.getY()));
				}
				panel.repaint();
			}
		});
		
		
		JButton btnClear = new JButton();
		btnClear.setText("Clear");
		btnClear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dataset1.clear();
				dataset2.clear();
				getGraphics().clearRect(0, 0,
						getWidth(), getHeight());
				repaint();
			}
		});
		
		//add(btnClear);
		
		
	}

	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintSet1(g);
        //paintSet2(g);
  
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
    		g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        //ch = QuickHull.QH(vertices);
    	//ch = RCH.qrh(dataset1, 1.0, null, null, true);
    	ch = RHull.rhull(dataset1, 1.0);
        
        int[] xPoints = new int[ch.size()];
        int[] yPoints =  new int[ch.size()];
        
        for (int i = 0; i < ch.size(); i++){
        	xPoints[i] = (int) ch.get(i).x;
        	yPoints[i] = (int) ch.get(i).y;
      
        }
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch.size());
        
        
        rch = RHull.rhull(dataset1, 0.5);
        //rch = RCH.qrh(dataset1, mu, null, null, true);
        
        xPoints = new int[rch.size()];
        yPoints =  new int[rch.size()];
        
        for (int i = 0; i < rch.size(); i++){
        	xPoints[i] = (int) rch.get(i).x;
        	yPoints[i] = (int) rch.get(i).y;
      
        }
        g.setColor(Color.RED);
        g.drawPolygon(xPoints, yPoints, rch.size());
        
        if (rch.size() == 1){
        	g.drawOval(rch.get(0).x, rch.get(0).y, 2, 2);
        	g.drawString("c", rch.get(0).x +4, rch.get(0).y+4);
        }
    }

    public void paintSet2(Graphics g) {
        
        ArrayList<Point> ch = new ArrayList<Point>();	//mem leak with new
        ArrayList<Point> rch = new ArrayList<Point>();
        
        Point p = new Point();
    	for (int i = 0; i < dataset2.size(); i++){
    		p = dataset2.get(i);
    		g.setColor(Color.BLACK);
    		g.fillRect((int) p.getX(), (int) p.getY(), 3, 3);
    		g.setColor(Color.BLUE);
    		g.drawString(i + "", (int) p.getX(), (int) p.getY());
    	}

        //ch = QuickHull.QH(vertices);
    	//ch = RCH.qrh(dataset2, 1.0, null, null, true);
    	ch = RHull.rhull(dataset2, 1.0);
    	
    	
        int[] xPoints = new int[ch.size()];
        int[] yPoints =  new int[ch.size()];
        
        for (int i = 0; i < ch.size(); i++){
        	xPoints[i] = (int) ch.get(i).x;
        	yPoints[i] = (int) ch.get(i).y;
      
        }
        g.setColor(Color.GREEN);
        g.drawPolygon(xPoints, yPoints, ch.size());
        
        
        rch = RHull.rhull(dataset2, 0.5);
        //rch = RCH.qrh(dataset2, mu, null, null, true);
        
        xPoints = new int[rch.size()];
        yPoints =  new int[rch.size()];
        
        for (int i = 0; i < rch.size(); i++){
        	xPoints[i] = (int) rch.get(i).x;
        	yPoints[i] = (int) rch.get(i).y;
      
        }
        g.setColor(Color.BLUE);
        g.drawPolygon(xPoints, yPoints, rch.size());
        
        if (rch.size() == 1){
        	g.drawOval(rch.get(0).x, rch.get(0).y, 2, 2);
        	g.drawString("c", rch.get(0).x +4, rch.get(0).y+4);
        }
    }
    
    
	@Override
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
             
             double d = (double)source.getValue() / (20 / (double)max);
             mu = 1.0/d;
             
             System.out.println(" slider  -> " + source.getValue() 
            		 + ",  mu =  " + mu
            		 + ", 1/mu = " + 1.0/mu);
             
             text.setText(String.valueOf(mu));
             repaint();
        }
	}
}














