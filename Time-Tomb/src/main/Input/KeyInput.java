package main.Input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.Game;
import main.GameObject;
import main.GameState;
import main.Handler;
import main.ID;

public class KeyInput extends KeyAdapter{

	private Handler handler;
	private Game game;
	private boolean right, up, down;
	private boolean left = right = up = down = false;
	private double playerSpeed = 4.0;
	
	public KeyInput(Handler handler, Game game){
		this.handler = handler;
		this.game = game;
	}
	
	public void keyPressed(KeyEvent e){
		
		int key = e.getKeyCode();
		if(game.gameState == GameState.Playing)playingKeyPressed(key);
		
	}
	
	public void keyReleased(KeyEvent e){
		
		int key = e.getKeyCode();
		if(game.gameState == GameState.Playing)playingKeyReleased(key);
		
	}
	
	private void playingKeyPressed(int key){
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			//PLAYER CONTROL
			if(tempObject.getId() == ID.Player){
				
				if(key == KeyEvent.VK_W) up = true;
				if(key == KeyEvent.VK_S) down = true;
				if(key == KeyEvent.VK_A) left = true;
				if(key == KeyEvent.VK_D) right = true;
				
				if(up)tempObject.setVelY(-playerSpeed);
				if(down)tempObject.setVelY(playerSpeed);
				if(left)tempObject.setVelX(-playerSpeed);
				if(right)tempObject.setVelX(playerSpeed);
				
			}
		}
		
	}
	
	private void playingKeyReleased(int key){
		
		if(game.playing){
			for(int i = 0; i < handler.object.size(); i++){
				GameObject tempObject = handler.object.get(i);
				//PLAYER CONTROL
				if(tempObject.getId() == ID.Player){
					
					if(key == KeyEvent.VK_W) up = false;
					if(key == KeyEvent.VK_S) down = false;
					if(key == KeyEvent.VK_A) left = false;
					if(key == KeyEvent.VK_D) right = false;
					if(key == KeyEvent.VK_L) game.createLevel();
					
					if(up)tempObject.setVelY(-playerSpeed);
					if(down)tempObject.setVelY(playerSpeed);
					if(left)tempObject.setVelX(-playerSpeed);
					if(right)tempObject.setVelX(playerSpeed);
					
					if(!(up || down))tempObject.setVelY(0);
					if(!(right || left))tempObject.setVelX(0);
					
				}
			}
		}else{
			up = false;
			down = false;
			left = false;
			right = false;
			game.gameState = GameState.Menu;
		}
		
	}
	
}
