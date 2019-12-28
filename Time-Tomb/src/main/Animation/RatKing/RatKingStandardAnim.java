package main.Animation.RatKing;

import main.Game;
import main.Animation.Animation;

public class RatKingStandardAnim extends Animation {

	public RatKingStandardAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 32;
		this.fps = 5;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_ratKingWalk1);
		imgList.add(game.img_ratKingWalk2);
	}
	
	public String toString(){
		return "RatKingWalk";
	}

}
