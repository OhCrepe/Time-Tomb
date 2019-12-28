package main.Pathfinder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import main.Direction;
import main.Game;
import main.GameObject;
import main.Entities.Entity;

public class Pathfinder {

	LinkedList<Node> open = new LinkedList<Node>();
	LinkedList<Node> closed = new LinkedList<Node>();
	Game game;
	int dimension;
	public boolean[][] solids;
	
	public Pathfinder(Game game){
		
		this.game = game;
		this.dimension = game.tiles.length;
		this.solids = null;
		
	}
	
	Node getNode(List<Node> list, int x, int y){
		for (Node node: list){
			if (node.getX() == x && node.getY() == y) return node;
		}return null;
	}
	
	public Direction findNextStep(Entity entity, GameObject goalObject){
		
		if(goalObject == null) return randomStep();
		if(solids == null)solids = game.solids;
		open.clear();
		closed.clear();
		int startX = ((entity.getX() + entity.getWidth()/2) - game.offSet)/32, startY = (entity.getY() + entity.getHeight()/2)/32, 
				goalX = ((goalObject.getX() + goalObject.getWidth()/2) - game.offSet)/32, goalY = (goalObject.getY() + goalObject.getHeight()/2)/32;
		Node current = new Node(startX, startY);
		Node goal = new Node(goalX, goalY);
		
		//System.out.println(current.getX() + ", " + current.getY());
		//System.out.println(goal.getX() + ", " + goal.getY());
		
		printSolids();
		
		while(!(current.getX() == goal.getX() && current.getY() == goal.getY())){
			if(contains(open, current))open.remove(current);
			closed.add(current);
			
			//Above node
			if(!solids[current.getX()][current.getY()-1]){
				Node tempNode = new Node(current.getX(), current.getY() - 1);
				//System.out.println("Above Not Solid");
				if(!contains(closed, tempNode)){
					//System.out.println("Above Not Closed");
					if(contains(open, tempNode)){
						tempNode = getNode(open, tempNode);
						if(tempNode.fValue > current.fValue + 1){
							tempNode.setParent(current);
						}
					}else{
						tempNode = new Node(tempNode.getX(), tempNode.getY(), current);
						tempNode.calcG(goal);
						open.add(tempNode);
					}
				}
			}
			
			//Left node
			if(!solids[current.getX()-1][current.getY()]){
				Node tempNode = new Node(current.getX() - 1, current.getY());
				//System.out.println("Left Not Solid");
				if(!contains(closed, tempNode)){
					//System.out.println("Left Not Closed");
					if(contains(open, tempNode)){
						tempNode = getNode(open, tempNode);
						if(tempNode.fValue > current.fValue + 1){
							tempNode.setParent(current);
						}
					}else{
						tempNode = new Node(tempNode.getX(), tempNode.getY(), current);
						tempNode.calcG(goal);
						open.add(tempNode);
					}
				}
			}
			
			//Right node
			if(!solids[current.getX() + 1][current.getY()]){
				Node tempNode = new Node(current.getX() + 1, current.getY());
				//System.out.println("Right Not Solid");
				if(!contains(closed, tempNode)){
					//System.out.println("Right Not Closed");
					if(contains(open, tempNode)){
						tempNode = getNode(open, tempNode);
						if(tempNode.fValue > current.fValue + 1){
							tempNode.setParent(current);
						}
					}else{
						tempNode = new Node(tempNode.getX(), tempNode.getY(), current);
						tempNode.calcG(goal);
						open.add(tempNode);
					}
				}
			}
			
			//Down node
			if(!solids[current.getX()][current.getY()+1]){
				Node tempNode = new Node(current.getX(), current.getY() + 1);
				//System.out.println("Down Not Solid");
				if(!contains(closed, tempNode)){
					//System.out.println("Down Not Closed");
					if(contains(open, tempNode)){
						tempNode = getNode(open, tempNode);
						if(tempNode.fValue > current.fValue + 1){
							tempNode.setParent(current);
						}
					}else{
						tempNode = new Node(tempNode.getX(), tempNode.getY(), current);
						tempNode.calcG(goal);
						open.add(tempNode);
					}
				}
			}
			
			//System.out.println(open.size());
			Collections.sort(open);
			if(open.size() > 1)current = open.get(0);
			else return null;
			
		}
		
		//Goes back through the path
		if(current.parent != null){
			while(current.parent.parent != null){
				if(current.getX() > current.parent.getX())System.out.println("RIGHT");
				if(current.getX() < current.parent.getX())System.out.println("LEFT");
				if(current.getY() > current.parent.getY())System.out.println("DOWN");
				if(current.getY() < current.parent.getY())System.out.println("UP");
				current = current.parent;
			}
		}
		
		if(current.getX() < startX)return Direction.Left;
		if(current.getY() < startY)return Direction.Up;
		if(current.getY() > startY)return Direction.Down;
		return Direction.Right;

		
	}
	
	boolean contains(List<Node> list, Node searchNode){
		for(Node node: list){
			if(searchNode.getX() == node.getX() && searchNode.getY() == node.getY())return true;
		}
		return false;
	}
	
	Node getNode(List<Node> list, Node searchNode){
		for(Node node: list){
			if(searchNode.getX() == node.getX() && searchNode.getY() == node.getY())return node;
		}
		return null;
	}
	
	Direction randomStep(){
		int dir = (int) (Math.random()*4);
		if(dir == 0)return Direction.Up;
		if(dir == 1)return Direction.Right;
		if(dir == 2)return Direction.Down;
		if(dir == 3)return Direction.Left;
		return null;
	}
	
	void printSolids(){
		for(int y = 0; y < 24; y++){
			for(int x = 0; x < 24; x++){
				if(solids[x][y])System.out.print("#");
				else System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	
}
