package main.Menu;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import main.Game;

public class Menu implements ImageObserver {

	protected Game game;
	protected ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	int lastHovered = -1;
	
	public Menu(Game game){
		this.game = game;
		buttons.add(new StartButton(game, 152, 238, game.img_startButton));
		buttons.add(new StatisticsButton(game, 152, 376, game.img_statisticsButton));
		buttons.add(new QuitButton(game, 152, 514, game.img_quitButton));
	}
	
	public void beClicked(int x, int y){
		
		for(MenuButton b: buttons){
			if(b.isClicked(x, y))b.function();
		}
		
	}
	
	public void tick(){
		
		boolean hovered = false;
		
		for(int i = 0; i < buttons.size(); i++){
			MenuButton button = buttons.get(i);
			Point mousePos = MouseInfo.getPointerInfo().getLocation();
			if(button.isClicked(mousePos.x - game.getLocationOnScreen().x, mousePos.y - game.getLocationOnScreen().y)){
				hovered = true;
				if(lastHovered == i)continue;
				button.playSound();
				lastHovered = i;
				break;
			}
		}
		if(!hovered)lastHovered = -1;
		
	}
	
	public void render(Graphics g){
		
		for(int x = 0; x < game.WIDTH; x+=64){
			for(int y = 0; y < game.HEIGHT; y+=64){
				g.drawImage(game.img_floor[0], x, y, this);
			}
		}
		
		for(MenuButton b: buttons){
			b.render(g);
		}
		
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		return false;
	}
	
}
