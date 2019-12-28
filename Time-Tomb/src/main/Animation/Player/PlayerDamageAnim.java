package main.Animation.Player;

import main.Game;
import main.Animation.Animation;

public class PlayerDamageAnim extends Animation{

	public PlayerDamageAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_playerDamage);
	}	
	
	public String toString(){
		return "Damage";
	}
	
}
