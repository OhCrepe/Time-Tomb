package main.Tiles;

import java.awt.Image;

import main.Game;

public class Floor extends Tile{

	public Floor(int x, int y, Game game, Image sprite) {
		super(x, y, game, sprite);
		this.solid = false;
		this.tileID = TileID.Floor;
	}

}
