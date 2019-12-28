package main.Entities;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;

public class Indicator extends GameObject{

	GameObject mark;
	int diameter = 128;
	Color color;
	
	public Indicator(int x, int y, ID id, Handler handler, Game game, GameObject mark, Color color){
		super(x, y, id, handler, game);
		this.mark = mark;
		this.color = color;	
	}

	@Override
	public void tick() {

		diameter -= 4;
		if(diameter <= 0){
			handler.removeObject(this);
		}
		
	}

	@Override
	public void render(Graphics g) {
		
		g.setColor(color);
		g.drawOval(mark.getX() + mark.getWidth()/2 - diameter/2, mark.getY() + mark.getHeight()/2 - diameter/2, diameter, diameter);
		
	}

	
	
}
