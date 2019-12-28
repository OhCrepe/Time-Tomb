package main.Tiles;

import java.awt.Image;
import java.awt.Rectangle;

import main.Direction;
import main.Game;

public class Exit extends Tile{
	
	public Exit(int x, int y, Game game, Image sprite, Direction d) {
		
		super(x, y, game, sprite);
		solid = true;
		this.tileID = TileID.Exit;
		//Decides on the collision rectangle (don't want us getting to the next level through a wall do we?)
		if(d == Direction.Up)collisionRect = new Rectangle(x, y + 32, 64, 32);
		if(d == Direction.Left)collisionRect = new Rectangle(x + 32, y, 32, 64);
		if(d == Direction.Right)collisionRect = new Rectangle(x, y, 32, 64);
		if(d == Direction.Down)collisionRect = new Rectangle(x, y, 64, 32);
		
	}

}
