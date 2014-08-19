package app.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import app.model.algorithms.RCH;
import app.model.data.SVMModel;

public class BGTask extends SwingWorker<SVMModel , Void>{
	SVMModel model = null;
	DrawPanel dp = null;
	
	public BGTask(DrawPanel dp, SVMModel model){
		this.dp = dp;
		this.model = model;
	}
	
	@Override
	protected SVMModel doInBackground() throws Exception {

		//Thread.currentThread();\
		System.out.println("bg sleeping....");
		Thread.sleep(50 );
		
		model.compute();
			 
		return model;
	}
	
	@Override
	protected void done(){
		
		try {
			SVMModel  m = get();
			System.out.println("bg task done" + m.mu1);
			
			
			dp.ch1 = model.ch1;
			dp.rch1 = model.rch1;
			dp.ch2 = model.ch2;
			dp.rch2 = model.rch2;
			dp.repaint();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	

}
