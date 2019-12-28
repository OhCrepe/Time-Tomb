package main.Animation.Skeleton;

import main.Game;
import main.Animation.Animation;

public class SkeletonDamageAnim extends Animation{

	public SkeletonDamageAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 52;
		this.fps = 0.1;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_skeletonDamage);
	}

}
