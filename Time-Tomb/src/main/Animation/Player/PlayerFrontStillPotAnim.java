package main.Animation.Player;

import main.Game;
import main.Animation.Animation;

public class PlayerFrontStillPotAnim extends Animation{

	public PlayerFrontStillPotAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_playerPotFront);
	}	
	
	public String toString(){
		return "FrontStillPot";
	}
	
}
