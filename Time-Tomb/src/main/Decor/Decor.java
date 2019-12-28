package main.Decor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;

public class Decor extends GameObject implements ImageObserver{

	Image sprite;
	
	public Decor(int x, int y, int width, int height, Handler handler, Game game, Image sprite){
		super(x, y, ID.Decor, handler, game);
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite, (int)x, (int)y, width, height, this);
		
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

}