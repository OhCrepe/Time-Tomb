package main.Entities;

import java.awt.Image;
import java.awt.Rectangle;

import main.Direction;
import main.Game;
import main.Handler;
import main.ID;
import main.Animation.Player.PlayerDamageAnim;
import main.Animation.Player.PlayerFrontStillAnim;
import main.Animation.Player.PlayerFrontStillPotAnim;
import main.Animation.Player.PlayerFrontWalkAnim;
import main.Animation.Player.PlayerFrontWalkPotAnim;
import main.Objects.Pot;
import main.Objects.Sarcophagus;
import main.Tiles.Tile;
import main.Tiles.TileID;
import main.Audio.SoundPlayer;

public class Player extends Entity{
	
	boolean pot;
	
	public Player(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id, handler, game);
		this.width = 32;
		this.height = 32;
		this.kbSpeed = 10;
		this.pot = false;
		this.collisionRect = new Rectangle(x, y, width, height);
		this.animation = new PlayerFrontStillAnim(game);
	}

	@Override
	public void tick() {
		
		super.tick();
		if(state == EntityState.Normal)normalTick();
		else if(state == EntityState.Knockback)knockbackTick();
		fixCollisionRect();
		checkTileCollisions();
		checkPotCollisions();
		checkSarcophagusCollisions();
		
	}

	private void normalTick() {
		x += velX;
		y += velY;
		if(this.velX == 0 && this.velY == 0){
			if(game.pickUp && !animation.toString().equals("FrontStillPot")){
				this.animation = new PlayerFrontStillPotAnim(game);
			}else if(!game.pickUp && !animation.toString().equals("FrontStill")){
				this.animation = new PlayerFrontStillAnim(game);
			}
		}else{
			if(game.pickUp && !animation.toString().equals("FrontWalkPot")){
				this.animation = new PlayerFrontWalkPotAnim(game);
			}else if(!game.pickUp && !animation.toString().equals("FrontWalk")){
				this.animation = new PlayerFrontWalkAnim(game);
			}
		}
	}

	@Override
	void collideWith(Tile tile) {
		
		if(tile.getTileID() == TileID.Exit){
			if(collisionRect().intersects(tile.collisionRect())){
				game.createLevel();
				game.treasureHandler.roomBeat();
				SoundPlayer.playSound("Resources/Sounds/Other/TombFinish.wav", -10f);
				return;
			}
		}
		
		Direction direction = calculateDirection(tile);	
		if(direction == Direction.Down){
			if(tile.isUpSolid())this.y = tile.getY() - this.height;
		}else if(direction == Direction.Right){
			if(tile.isLeftSolid())this.x = tile.getX() - this.width;
		}else if(direction == Direction.Left){
			if(tile.isRightSolid())this.x = tile.getX() + tile.getWidth();
		}else if(direction == Direction.Up){
			if(tile.isDownSolid())this.y = tile.getY() + tile.getHeight();
		}
		fixCollisionRect();
	}

	@Override
	void collideWith(Pot pot) {
		
		Direction direction = calculateDirection(pot);
		if(direction == Direction.Down){
			if(pot.isUpSolid())this.y = pot.getY() - this.height;
		}else if(direction == Direction.Right){
			if(pot.isLeftSolid())this.x = pot.getX() - this.width;
		}else if(direction == Direction.Left){
			if(pot.isRightSolid())this.x = pot.getX() + pot.getWidth();
		}else if(direction == Direction.Up){
			if(pot.isDownSolid())this.y = pot.getY() + pot.getHeight();
		}
		fixCollisionRect();
		
	}

	@Override
	public Rectangle collisionRect() {
		return collisionRect;
	}
	
	@Override
	public void knockbackTick(){
		
		super.knockbackTick();
		if(!animation.toString().equals("Damage")){
			this.animation = new PlayerDamageAnim(game);
		}
		
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5) {
		return false;
	}

	@Override
	void collideWith(Player p) {
		return;		
	}

	public void setPot(boolean pot) {
		this.pot = pot;		
	}

	@Override
	void collideWith(Sarcophagus sarc) {
		
		Direction direction = calculateDirection(sarc);
		if(direction == Direction.Down){
			if(sarc.isUpSolid())this.y = sarc.getY() - this.height;
		}else if(direction == Direction.Right){
			if(sarc.isLeftSolid())this.x = sarc.getX() - this.width;
		}else if(direction == Direction.Left){
			if(sarc.isRightSolid())this.x = sarc.getX() + sarc.getWidth();
		}else if(direction == Direction.Up){
			if(sarc.isDownSolid())this.y = sarc.getY() + sarc.getHeight();
		}
		fixCollisionRect();
		
	}

	
}
