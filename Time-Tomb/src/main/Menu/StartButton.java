package main.Menu;

import java.awt.Image;

import main.Game;

public class StartButton extends MenuButton {

	public StartButton(Game game, int x, int y, Image image) {
		super(game, x, y, image);
	}

	@Override
	public void function() {
		game.startGame();
	}

	

}
