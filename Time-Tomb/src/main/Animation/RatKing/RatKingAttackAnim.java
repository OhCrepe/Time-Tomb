package main.Animation.RatKing;

import main.Game;
import main.Animation.Animation;

public class RatKingAttackAnim extends Animation{

	public RatKingAttackAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 32;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_ratKingAttack);	
	}

	public String toString(){
		return "RatKingAttack";
	}
	
}
