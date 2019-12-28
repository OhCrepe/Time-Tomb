package main.Menu;

import java.awt.Image;

import main.Game;

public class QuitButton extends MenuButton{

	public QuitButton(Game game, int x, int y, Image image) {
		super(game, x, y, image);
		soundFile = "Resources/Sounds/Entities/GhostSpawn.wav";
	}

	@Override
	public void function() {
		System.exit(0);		
	}

}
