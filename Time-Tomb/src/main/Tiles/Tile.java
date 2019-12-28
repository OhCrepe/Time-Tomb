package main.Tiles;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Game;

public abstract class Tile implements ImageObserver {

	protected int x, y;
	protected boolean solid;
	protected Image sprite;
	protected int width = 64, height = 64;
	protected boolean leftSolid, rightSolid, upSolid, downSolid;
	protected Rectangle collisionRect;
	protected Game game;
	protected TileID tileID;
	
	protected Tile(int x, int y, Game game, Image sprite){
		this.x = x;
		this.y = y;
		this.game = game;
		this.sprite = sprite;
	}
	
	public TileID getTileID(){
		return tileID;
	}
	
	public Rectangle collisionRect(){
		return collisionRect;
	}
	
	public boolean isLeftSolid() {
		return leftSolid;
	}

	public void setLeftSolid(boolean leftSolid) {
		this.leftSolid = leftSolid;
	}

	public boolean isRightSolid() {
		return rightSolid;
	}

	public void setRightSolid(boolean rightSolid) {
		this.rightSolid = rightSolid;
	}

	public boolean isUpSolid() {
		return upSolid;
	}

	public void setUpSolid(boolean upSolid) {
		this.upSolid = upSolid;
	}

	public boolean isDownSolid() {
		return downSolid;
	}

	public void setDownSolid(boolean downSolid) {
		this.downSolid = downSolid;
	}

	public void render(Graphics g){
		g.drawImage(sprite, x, y, 64, 64, this);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		return false;
	}
	
}
