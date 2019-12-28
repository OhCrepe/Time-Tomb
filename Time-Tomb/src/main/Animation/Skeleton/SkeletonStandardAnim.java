package main.Animation.Skeleton;

import main.Game;
import main.Animation.Animation;

public class SkeletonStandardAnim extends Animation{

	public SkeletonStandardAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 6;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_skeleton1);
		imgList.add(game.img_skeleton2);
	}	
	
}
