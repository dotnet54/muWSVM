package app.model.algorithms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import app.model.data.DData;
import app.model.data.DVector;
import app.model.data.Dwrch;
import app.model.data.Dwsk;
import app.model.data.SVMDataItem;

public class BBY {

	ArrayList<DVector> vertexList = null;
	ArrayList<Facet> facetList = null;
	List<List<Integer>> adjacencyList = null;
	
	private class Facet{
		public DVector left = null;
		public DVector right = null;
		public boolean isVisible = false;
		public boolean isFinal = false;
		
		public Facet(DVector v1, DVector v2){
			this.left = v1;
			this.right = v2;
		}
		
		//normal away from hull, follow same convention always
		public DVector getNormal() throws Exception{
			DVector w = right.subtractVectors(left);
			return w.get2DAntiClockwiseNormal();
		}
		
		public boolean equals(Object obj){
			if(obj instanceof Facet){
				Facet f = (Facet) obj;
				if (f.left.equals(left) && f.right.equals(right)){
					return true;
				}
			}
			return false;
		}
		
		public boolean contains(DVector v){
			
			if (left.equals(v)){
				return true;
			}
			
			if (right.equals(v)){
				return true;
			}
			
			return false;
		}
	}
	
	public BBY(){
		vertexList = new ArrayList<DVector>();
		facetList = new ArrayList<Facet>();
		adjacencyList = new ArrayList<List<Integer>>();
	}
	
	public ArrayList<SVMDataItem> calcWRCH(ArrayList<SVMDataItem> points, double mu){
		ArrayList<DVector> list = new ArrayList<DVector>();
		ArrayList<SVMDataItem> res = new ArrayList<SVMDataItem>();
		
		try {
			for (int i =  0; i < points.size(); i++){
				list.add(new DVector(
						points.get(i).getXValue(), 
						points.get(i).getYValue(),
						points.get(i).getWeight()));
			}
			System.out.println("----------------");
			ArrayList<DVector> ans = findWRCH(list ,mu);
			
			for (int i =  0; i < ans.size(); i++){
				res.add(new SVMDataItem(
						ans.get(i).getX(),
						ans.get(i).getY(), 
						ans.get(i).getWeight()));
			}
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	
	public ArrayList<DVector> findWRCH(ArrayList<DVector> points, double mu){
		try {
			DVector leftEdge = null;
			DVector rightEdge = null;
			
			DVector startDirection = new DVector(2);
			startDirection.setX(-1);
			DVector oppositeDirection = new DVector(2);
			oppositeDirection.setX(+1);

			DVector leftMostVertex = Dwrch.findExtremePoint(points , mu, startDirection);
			DVector rightMostVertex = Dwrch.findExtremePoint(points , mu, oppositeDirection);
			
			vertexList.add(leftMostVertex);
			vertexList.add(rightMostVertex);
			
			facetList.add(new Facet(leftMostVertex, rightMostVertex));
			facetList.add(new Facet(rightMostVertex, leftMostVertex));
			
			Facet facet = getNonFinal();
			while (facet != null){
				
				DVector n = facet.getNormal();
				DVector h = Dwrch.findExtremePoint(points, mu, n);
				System.out.println(h + " : " + facetList.size()
						+" : " + vertexList.size());

				//TODO contain uses equals
				if (!vertexList.contains(h)){
					
					//TODO add h to vertexList
					vertexList.add(h);
					
					updateVisibleFacets(h);
					for (int i =  0; i < facetList.size(); i++){
						if (facetList.get(i).isVisible){
							DVector v = facetList.get(i).left;
							for (int j = 0; j < facetList.size(); j++){
								Facet f = facetList.get(j);
								if (f.contains(v) && f.isVisible == false){
									leftEdge = v;
								}
							}
							v = facetList.get(i).right;
							for (int j = 0; j < facetList.size(); j++){
								Facet f = facetList.get(j);
								if (f.contains(v) && f.isVisible == false){
									rightEdge = v;
								}
							}
							
						}
					}

					facetList.add(new Facet(leftEdge, h));
					facetList.add(new Facet(h, rightEdge));
					
					for (int i =  0; i < facetList.size(); i++){
						if (facetList.get(i).isVisible){
							facetList.remove(i);
							i = 0;
						}
					}
					
				}else{
					facet.isFinal = true;
				}
				
				
				facet = getNonFinal();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vertexList;
	}
	
	
	public ArrayList<SVMDataItem> getWRCH(){
		ArrayList<DVector> ans =  new ArrayList<DVector>();
		ArrayList<SVMDataItem> res = new ArrayList<SVMDataItem>();

		
		try {
			//TODO assume index 0 exist
			Facet currentFacet = null;
			if (facetList.size() > 1){
				//currentFacet = facetList.get(0);
				
			}else{
				return null;
			}
			
			Facet nn = null;
			for (int i = 0; i < facetList.size(); i++){
				if (i == 0){
					currentFacet = facetList.get(i);
					ans.add(currentFacet.left);
					ans.add(currentFacet.right);
				}else{
					nn = getRightNeighbour(currentFacet);
					if (nn != null){
						if (ans.get(ans.size() - 1).equals(nn.left)){ //start == end
							ans.add(nn.right);
							currentFacet = nn;
						}else if(ans.get(ans.size() - 1).equals(nn.right)){
							ans.add(nn.left); 
							currentFacet = nn;
						}else{
							//assert error??
						}
					} else {
						System.out.println("Error: disjoint facet found:" + nn);
					}					
				}
				//facetList.get(i).isFinal = false;	//use isFinal for tagging
			}
			
			
//			int count = 1;
//			int i = 1;
//			while(count < facetList.size()){
//				for (i = 1; i < facetList.size(); i++){
//					currentFacet = facetList.get(i);
//					if (!currentFacet.isFinal){
//						if (ans.get(ans.size() - 1).equals(currentFacet.left)){ //start == end
//							ans.add(currentFacet.right); 
//							currentFacet.isFinal = true;
//							count++;
//						}else if(ans.get(ans.size() - 1).equals(currentFacet.right)){
//							ans.add(currentFacet.left); 
//							currentFacet.isFinal = true;
//							count++;
//						}else{
//							//we will visit this facet next round
//						}
//					}
//				}
//				if (count  < facetList.size()){
//					i = 1;
//					if (getNonFinal() != null){
//						System.out.println("non final exist round");
//					}
//					System.out.println("next round");
//				}
//			}

			for (int j =  0; j < ans.size(); j++){
				res.add(new SVMDataItem(
						ans.get(j).getX(),
						ans.get(j).getY(), 
						ans.get(j).getWeight()));
			}
			
			
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		return res;
	}
	
	
	private void updateVisibleFacets(DVector h){
		try {
			int size = facetList.size();
			double pointoffset = 0.0;
			double facetOffset = 0.0;
			
			Facet facet = null;

			for (int i = 0; i < size; i++){
				facet = facetList.get(i);
				facetOffset = facet.getNormal().getDotProduct(facet.left);
				pointoffset = facet.getNormal().getDotProduct(h);
				if (pointoffset > facetOffset){	//TODO fp comparison, use epsilon
					facetList.get(i).isVisible = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private Facet getNonFinal(){
		int start = facetList.size() -1;
		for (int i = start; i >= 0; i--){
			if (!facetList.get(i).isFinal){
				return facetList.get(i); //returns first found in order
			}
		}
		return null;
	}
	
	private Facet findNeighnour(Facet facet){
		Facet currentFacet = null; 
		for (int i = 0; i < facetList.size(); i++){
			currentFacet = facetList.get(i);
			if (facet.right.equals(currentFacet.left)
					&& !currentFacet.equals(facet)){
				return facetList.get(i);
			}
		}
		
		
		
		return null;
	}
	
	private Facet getLeftNeighbour(Facet facet){
		Facet currentFacet = null; 
		for (int i = 0; i < facetList.size(); i++){
			currentFacet = facetList.get(i);
			if (currentFacet.right.equals(facet.left) 
					|| currentFacet.left.equals(facet.left)
					&& !currentFacet.equals(facet)){
				return facetList.get(i);
			}
		}
		return null;
	}
	private Facet getRightNeighbour(Facet facet){
		Facet currentFacet = null; 
		for (int i = 0; i < facetList.size(); i++){
			currentFacet = facetList.get(i);
			if (currentFacet.left.equals(facet.right)
					|| currentFacet.right.equals(facet.right)
					&& !currentFacet.equals(facet)){
				return facetList.get(i);
			}
		}
		return null;
	}
	

	private ArrayList<DVector> getBoundaryVertices(){
		int size = facetList.size();
		Facet facet = null;
		LinkedHashSet<DVector> listVisible = new LinkedHashSet<DVector>();
		ArrayList<DVector> boundary = new ArrayList<DVector>();
		
		boolean foundVisibleFacet = false;
		boolean foundInVisibleFacet = false;
		
		for (int i = 0; i < size; i++){
			facet = facetList.get(i);
			
			if (facet.isVisible){
				listVisible.add(facet.left);
				listVisible.add(facet.right);
			}
		}
		DVector vert = null;
		Iterator<DVector> it = listVisible.iterator();
		int j = 0;
		while(it.hasNext()){
			vert = it.next();
			
			for (int i = 0; i < size; i++){
				facet = facetList.get(i);
				if (facet.left.equals(vert) || facet.right.equals(vert)){
					if (facet.isVisible){
						foundVisibleFacet = true;
					}else{
						foundInVisibleFacet = true;
					}
				}
			}	
			
			if (foundVisibleFacet && foundInVisibleFacet){
				boundary.add(vert);
			}
			j++;
		}
	
		
		return boundary;
	}

	private void updateAdjacencyList(Facet newFacet) {
		int sizeF = facetList.size();
		Facet facet = null;
		boolean adjacentFaceExists = false;
		ArrayList<Integer> adList = new ArrayList<Integer>();;
		
		for (int i = 0; i < sizeF; i++){
			facet = facetList.get(i);
			if (facet.equals(newFacet)){
				//skip
			}else if (facet.left.equals(newFacet.left) || facet.right.equals(newFacet.right)){
				adjacentFaceExists = true;
				adList.add(i);
			}else{
				//skip
			}
		}
		
		if (!adjacentFaceExists){
			adjacencyList.add(null);
		}else{
			adjacencyList.add(adList);
		}
		
	}

	private void updateBoundaryEdges(){
		try {
			int sizeV = vertexList.size();
			int sizeF = facetList.size();
			DVector vert = null;
			Facet facet = null;
			boolean isV1Boundary = false;
			boolean isV2Boundary = false;
			
			for (int i = 0; i < sizeV; i++){
				vert = vertexList.get(i);
				for (int j = 0; j < sizeF; j++){
					facet = facetList.get(i);
					if (facet.left.equals(vert) || facet.right.equals(vert)){
						
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private ArrayList<DVector> getBoundaryEdges(ArrayList<Facet> facets){
		ArrayList<DVector> edges = new ArrayList<DVector>();
		
		
		return edges;
	}
	
	
	private ArrayList<Facet> getVisibleFacets(DVector h){
		ArrayList<Facet> X = new ArrayList<BBY.Facet>();
		
		try {
			int size = facetList.size();
			double proj = 0.0;
			
			
			for (int i = 0; i < size; i++){
				proj = facetList.get(i).getNormal().getDotProduct(h);
				if (proj > 0){	//TODO fp comparison, use epsilon
					X.add(facetList.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return X;
	}
}
