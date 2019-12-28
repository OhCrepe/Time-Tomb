package main.Animation.Player;

import main.Game;
import main.Animation.Animation;

public class PlayerFrontStillAnim extends Animation{

	public PlayerFrontStillAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_playerStillFront);
	}	
	
	public String toString(){
		return "FrontStill";
	}
	
}
