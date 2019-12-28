package main.Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;

public class HUD extends GameObject{

	Font font = new Font("Verdana", 0, 12);
	
	public HUD(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id, handler, game);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {

		g.setColor(new Color(194, 145, 78));
		g.fillRect(0, 0, 128, game.HEIGHT);
		g.fillRect(game.WIDTH-128, 0, 128, game.HEIGHT);
		g.setColor(new Color(158, 106, 54));
		g.fillRect(128, 0, 4, game.HEIGHT);
		g.fillRect(game.WIDTH-132, 0, 4, game.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		game.treasureHandler.render(g);
		game.timer.render(g);
			
		
	}

}
