package main.Menu;

import java.awt.Image;

import main.Game;
import main.GameState;

public class BackButton extends MenuButton{

	public BackButton(Game game, int x, int y, Image image) {
		super(game, x, y, image);
	}

	@Override
	public void function() {
		game.gameState = GameState.Menu;
	}

}
