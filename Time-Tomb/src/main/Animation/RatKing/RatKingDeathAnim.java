package main.Animation.RatKing;

import main.Game;
import main.Animation.Animation;

public class RatKingDeathAnim extends Animation{

	public RatKingDeathAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 32;
		this.fps = 12;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_ratKingDeath1);
		imgList.add(game.img_ratKingDeath2);
		imgList.add(game.img_ratKingDeath3);
		imgList.add(game.img_ratKingDeath4);
	}

	
	
}
