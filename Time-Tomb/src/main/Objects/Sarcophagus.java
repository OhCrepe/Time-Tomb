package main.Objects;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;
import main.Display.TreasureHandler;

public class Sarcophagus extends GameObject{

	private Rectangle collisionRect;
	Image sprite;
	boolean upSolid, rightSolid, leftSolid, downSolid;
	boolean open;
	int minValue = 1000, maxValue = 2500;
	
	public Sarcophagus(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id, handler, game);
		this.id = ID.Sarcophagus;
		this.width = 32;
		this.height = 32;
		this.collisionRect = new Rectangle(x, y, width, height);
		this.upSolid = false;
		this.rightSolid = true;
		this.leftSolid = true;
		this.downSolid = true;
		this.open = false;
		this.sprite = game.img_sarcophagusClosed;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite, (int)x, (int)y-16, width, 48, game);
		
	}

	public Rectangle collisionRect(){
		return collisionRect;
	}
	
	public boolean isUpSolid(){
		return upSolid;
	}
	
	public boolean isLeftSolid(){
		return leftSolid;
	}
	
	public boolean isRightSolid(){
		return rightSolid;
	}
	
	public boolean isDownSolid(){
		return leftSolid;
	}
	
	public void open(){
		if(this.open)return;
		this.open = true;
		this.sprite = game.img_sarcophagusOpen;
		int value = (int) ((int)(Math.random()*(maxValue/10-minValue/10))*10) + minValue;
		while(value > 0){
			System.out.println(value);
			if(value >= TreasureHandler.treasureValues[3]){
				Ruby r = new Ruby((int)(this.x + (int)Math.random()*16 + 8), (int)(this.y + 16 + (int)Math.random()*16), 
						this.handler, this.game, Math.random()*6-3, Math.random()*2+3);
				value -= TreasureHandler.treasureValues[3];
				handler.addObject(r);
			}
			if(value >= TreasureHandler.treasureValues[2]){
				Emerald e = new Emerald((int)(this.x + (int)Math.random()*16 + 8), (int)(this.y + 16 + (int)Math.random()*16), 
						this.handler, this.game, Math.random()*6-3, Math.random()*2+3);
				value -= TreasureHandler.treasureValues[2];
				handler.addObject(e);
			}
			if(value >= TreasureHandler.treasureValues[1]){
				Sapphire s = new Sapphire((int)(this.x + (int)Math.random()*16 + 8), (int)(this.y + 16 + (int)Math.random()*16), 
						this.handler, this.game, Math.random()*6-3, Math.random()*2+3);
				value -= TreasureHandler.treasureValues[1];
				handler.addObject(s);
			}
			if(value >= TreasureHandler.treasureValues[0]){
				Coin c = new Coin((int)(this.x + (int)Math.random()*16 + 8), (int)(this.y + 16 + (int)Math.random()*16), 
						this.handler, this.game, Math.random()*6-3, Math.random()*2+3);
				value -= TreasureHandler.treasureValues[0];
				handler.addObject(c);
			}
		}
	}
	
}
