package main.Animation.Player;

import main.Game;
import main.Animation.Animation;

public class PlayerFrontWalkAnim extends Animation{

	public PlayerFrontWalkAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 2.5;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_playerWalk1);
		imgList.add(game.img_playerWalk2);
	}
	
	public String toString(){
		return "FrontWalk";		
	}
	
}
