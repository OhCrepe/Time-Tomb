package main.Animation.Player;

import main.Game;
import main.Animation.Animation;

public class PlayerFrontWalkPotAnim extends Animation{

	public PlayerFrontWalkPotAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 2.5;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_playerPotWalk1);
		imgList.add(game.img_playerPotWalk2);
	}
	
	public String toString(){
		return "FrontWalkPot";		
	}
	
}
