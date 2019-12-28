package main.Animation.Skeleton;

import main.Game;
import main.Animation.Animation;

public class SkeletonAttackAnim extends Animation{

	public SkeletonAttackAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_skeletonAttack);
	}	
	
}