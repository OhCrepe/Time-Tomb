package main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import main.Objects.Pot;

public class Handler {

	public LinkedList<GameObject> object = new LinkedList<GameObject>();
	LinkedList<GameObject> addQ = new LinkedList<GameObject>();
	LinkedList<GameObject> remQ = new LinkedList<GameObject>();
	Game game;
	boolean levelStart;
	
	public Handler(Game game){
		this.game = game;
	}
	
	public void reset(){
		for(int i = 0; i < object.size(); i++){
			if(object.get(i).getId() != ID.Player){
				object.remove(i);
				i--;
			}
		}
		System.out.println("Handler size: " + object.size());
	}
	
	public void tick(){
		
		while(remQ.size() > 0){
			boolean found = false;
			for(int i = 0; i < object.size(); i++){
				if(remQ.get(0) == object.get(i)){
					object.remove(i);
					remQ.remove(0);
					found = true;
					break;
				}
			}
			if(!found)remQ.remove(0);			
		}
		
		while(addQ.size() > 0){
			object.add(addQ.get(0));
			addQ.remove(0);
		}
		
		levelStart = true;
		for(int i = 0; i < object.size(); i++){
			
			GameObject tempObject = object.get(i);
			if(tempObject.getId() == ID.Indicator){
				tempObject.tick();
				levelStart = false;			
			}
			
		}
		
		if(levelStart){
			for(int i = 0; i < object.size(); i++){
				GameObject tempObject = object.get(i);
				tempObject.tick();
			}
		}
		
	}
	
	public void render(Graphics g){
		
		ArrayList<GameObject> highLayer = new ArrayList<GameObject>();
		ArrayList<GameObject> ghostLayer = new ArrayList<GameObject>();
		
		for(int i = 0; i < object.size(); i++){
			
			GameObject tempObject = object.get(i);
			if(tempObject.getId() == ID.Pot){
				Pot tempPot = (Pot)tempObject;
				if(tempPot.getxVel() != 0 || tempPot.getyVel() != 0){
					highLayer.add(tempPot);
				}else{
					tempObject.render(g);
				}
			}else if(tempObject.getId() == ID.Ghost){
				ghostLayer.add(tempObject);
			}else{
				tempObject.render(g);
			}
			
		}
		
		for(int i = 0; i < highLayer.size(); i++){			
			highLayer.get(i).render(g);			
		}
		
		for(int i = 0; i < ghostLayer.size(); i++){
			ghostLayer.get(i).render(g);
		}
		
	}
	
	public void addObject(GameObject object){
		this.addQ.add(object);
	}
	
	public void removeObject(GameObject object){
		this.remQ.add(object);
	}
	
}
