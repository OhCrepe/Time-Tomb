package main.Menu;

import java.awt.Image;

import main.Game;
import main.GameState;

public class StatisticsButton extends MenuButton {

	public StatisticsButton(Game game, int x, int y, Image image) {
		super(game, x, y, image);
		soundFile = "Resources/Sounds/Entities/SkeletonSpawn.wav";
	}

	@Override
	public void function() {
		game.gameState = GameState.Statistics;
	}

}
