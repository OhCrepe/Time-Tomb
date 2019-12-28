package main.Objects;

import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.Handler;
import main.Audio.SoundPlayer;

public class Coin extends Treasure{

	public Coin(int x, int y, Handler handler, Game game, double xVel, double yVel) {
		super(x, y, handler, game, xVel, yVel);
		this.width = 8;
		this.height = 8;
		this.value = game.treasureHandler.getTreasureValue(0);
		this.sprite = game.img_coin;
		this.collisionRect = new Rectangle(x, y, width, height);
		tileCollisions();
	}
	
	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void playCollectNoise(){
		//SoundPlayer.playSound("Resources/Sounds/Treasure/CoinJingle" + (int)Math.random()*3 + ".wav");
		if(game.coinFreq == 0){
			int coinNoise = (int)Math.random()*2 + 1;
			SoundPlayer.playSound("Resources/Sounds/Treasure/CoinJingle" + coinNoise + ".wav", -20f);
			game.coinFreq = 1;
		}
	}
	
	public String toString(){
		return "Coin";
	}

}
