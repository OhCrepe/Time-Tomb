package main.Animation.Ghost;

import main.Game;
import main.Animation.Animation;

public class GhostStandardAnim extends Animation{

	public GhostStandardAnim(Game game) {
		super(game);
		this.width = 32;
		this.height = 36;
		this.fps = 1.5;
	}

	@Override
	public void loadImages() {
		imgList.add(game.img_ghost1);
		imgList.add(game.img_ghost2);
	}

}
