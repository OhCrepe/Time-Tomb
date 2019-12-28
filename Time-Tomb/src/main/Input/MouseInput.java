package main.Input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import main.Game;
import main.GameState;
import main.Handler;
import main.ID;
import main.Entities.Player;
import main.Objects.Pot;
import main.Objects.vPot;

public class MouseInput extends MouseAdapter{

	Game game;
	Handler handler;
	Player player;
	
	public MouseInput(Game game, Handler handler){
		this.game = game;
		this.handler = handler;
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Player){
				player = (Player)handler.object.get(i);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		
		if(game.gameState == GameState.Playing){
			gameClick(e);
		}else if(game.gameState == GameState.Menu){
			game.menu.beClicked(e.getX(), e.getY());
		}else if(game.gameState == GameState.Statistics){
			game.stats.beClicked(e.getX(), e.getY());
		}
		
		
		
	}
	
	private void gameClick(MouseEvent e){
		
		int pX = player.getX() + player.getWidth()/2;
		int pY = player.getY() + player.getHeight()/2;
		int eX = e.getX();
		int eY = e.getY();

		if(!game.pickUp){
					
			ArrayList<vPot> pots = new ArrayList<vPot>();
					
			for(int i = 0; i < handler.object.size(); i++){
						
				if(handler.object.get(i).getId() == ID.Pot){
							
					//Gets the closest part of the pot to the player
					Pot p = (Pot)handler.object.get(i);
					int poX = Game.clamp(pX, p.getX(), p.getX() + 32);
					int poY = Game.clamp(pY, p.getY(), p.getY() + 32);
					double dist = Math.sqrt((poX-pX)*(poX-pX) + (poY-pY)*(poY-pY));
					double mouseDist = Math.sqrt((poX-eX)*(poX-eX) + (poY-eY)*(poY-eY));
							
					if(dist <= 32 && mouseDist <= 64){
						pots.add(new vPot(i, dist, mouseDist));
					}
							
				}
						
			}
					
			//Chooses the closest pot
			if(pots.size() > 0){
						
				int index = 0;
				for(int i = 1; i < pots.size(); i++){
					if(pots.get(index).getMouseD() > pots.get(i).getMouseD()){
						index = i;
					}
				}
				System.out.println(index);
				Pot p = (Pot)handler.object.get(pots.get(index).getIndex());
				p.bePicked(player);
						
			}
				
		}else{
					
			Pot p = null;
					
			for(int i = 0; i < handler.object.size(); i++){
				if(handler.object.get(i).getId() == ID.Pot){
					p = (Pot)handler.object.get(i);
					if(p.isHeld()){
						break;
					}
				}	
			}
					
			if(p != null){
						
				int poX = p.getX() + p.getWidth()/2;
				int poY = p.getY() + p.getHeight()/2;

				double mouseDist = Math.sqrt((poX-eX)*(poX-eX) + (poY-eY)*(poY-eY));
				double a = Math.asin((eY - poY)/mouseDist);
						
				p.beThrown(eX < poX, a);
						
			}
					
		}	
		
	}
	
}
