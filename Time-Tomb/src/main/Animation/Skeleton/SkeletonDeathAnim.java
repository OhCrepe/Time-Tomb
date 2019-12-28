package main.Animation.Skeleton;

import main.Game;
import main.Animation.Animation;

public class SkeletonDeathAnim extends Animation{

	public SkeletonDeathAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 10;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_skeletonDeath1);
		imgList.add(game.img_skeletonDeath2);
		imgList.add(game.img_skeletonDeath3);
	}
	
}
