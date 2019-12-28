package main.Objects;

import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.Handler;

public class RatKingCrown extends Treasure{

	public RatKingCrown(int x, int y, Handler handler, Game game, double xVel, double yVel) {
		super(x, y, handler, game, xVel, yVel);
		this.width = 12;
		this.height = 6;
		this.value = game.treasureHandler.getTreasureValue(5);
		this.sprite = game.img_ratKingCrown;
		this.collisionRect = new Rectangle(x, y, width, height);
		tileCollisions();
	}
	
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void playCollectNoise() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		return "RatCrown";
	}

}