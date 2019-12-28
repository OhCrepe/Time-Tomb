package main.Tiles;

import java.awt.Image;
import java.awt.Rectangle;

import main.Game;

public class Wall extends Tile{

	public Wall(int x, int y, Game game, Image sprite){
		super(x, y, game, sprite);
		this.tileID = TileID.Wall;
		this.solid = true;
		this.leftSolid = false;
		this.rightSolid = false;
		this.upSolid = false;
		this.downSolid = false;
		this.collisionRect = new Rectangle(x, y, width, height);
	}
	
}
