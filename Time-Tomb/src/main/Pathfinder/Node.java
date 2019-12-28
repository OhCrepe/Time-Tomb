package main.Pathfinder;

public class Node implements Comparable<Node>{

	int x, y;
	int hValue, gValue, fValue;
	Node parent;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
		this.hValue = 0;
		this.gValue = 0;
		this.fValue = 0;
		this.parent = null;
	}
	
	public Node(int x, int y, Node node){
		this.x = x;
		this.y = y;
		this.hValue = 0;
		this.gValue = 0;
		this.parent = node;
		calcF();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setParent(Node node){
		this.parent = node;
		calcF();
	}
	
	public void calcH(Node node){
		this.fValue = Math.abs(this.x - node.getX()) + Math.abs(this.y - node.getY());
		calcF();
	}
	
	public void calcG(Node node){
		this.gValue = node.gValue + 1;
		calcF();
	}
	
	public void calcF(){
		this.fValue = this.gValue + this.hValue;
	}

	@Override
	public int compareTo(Node n) {
		return fValue - n.fValue;
	}	
	
	
	
}
