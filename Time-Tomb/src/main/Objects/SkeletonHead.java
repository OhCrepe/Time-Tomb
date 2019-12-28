package main.Objects;

import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.Handler;

public class SkeletonHead extends Treasure{

	public SkeletonHead(int x, int y, Handler handler, Game game, double xVel, double yVel) {
		super(x, y, handler, game, xVel, yVel);
		this.width = 14;
		this.height = 12;
		this.value = game.treasureHandler.getTreasureValue(4);
		this.sprite = game.img_skeleHead;
		this.collisionRect = new Rectangle(x, y, width, height);
		tileCollisions();
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void playCollectNoise() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		return "SkeletonHead";
	}

}
