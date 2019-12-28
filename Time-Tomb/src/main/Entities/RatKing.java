package main.Entities;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Direction;
import main.Game;
import main.GameObject;
import main.Handler;
import main.ID;
import main.Animation.AnimationState;
import main.Animation.RatKing.RatKingAttackAnim;
import main.Animation.RatKing.RatKingDeathAnim;
import main.Animation.RatKing.RatKingStandardAnim;
import main.Display.TreasureHandler;
import main.Objects.Pot;
import main.Objects.RatKingCrown;
import main.Objects.Sarcophagus;
import main.Objects.Treasure;
import main.Pathfinder.Pathfinder;
import main.Tiles.Tile;

public class RatKing extends Entity{

	int health, slotTimer, slotTimeLimit = 18, deathTimer = 19;
	Pathfinder pathFinder;
	ArrayList<Treasure> treasure = new ArrayList<Treasure>();
	
	public RatKing(int x, int y, ID id, Handler handler, Game game) {
		
		super(x, y, id, handler, game);
		this.width = 32;
		this.height = 32;
		this.animation = new RatKingStandardAnim(game);
		this.collisionRect = new Rectangle(x, y, 32, 32);
		this.transparency = 0f;
		this.transparent = false;
		this.health = 1;
		this.speed = 4;
		this.slotTimer = slotTimeLimit;
		this.kbSpeed = 10; //knockback speed
		this.pathFinder = new Pathfinder(game);
		
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Player){
				player = (Player)handler.object.get(i);
			}
		}
		
		this.handler.addObject(new Indicator(x, y, ID.Indicator, handler, game, this, Color.RED));
		
	}
	
	public void tick(){
		super.tick();
		
		if(state == EntityState.Normal)normalTick();
		else if(state == EntityState.Knockback)knockbackTick();
		else if(state == EntityState.Recovering)recoverTick();
		else if(state == EntityState.Death)deathTick();
		
		collisionRect.x = (int)x;
		collisionRect.y = (int)y;

		checkTileCollisions();
		checkPotCollisions();
		checkSarcophagusCollisions();
		
	}
	
	public void collect(Treasure t){
		this.treasure.add(t);
	}

	private void normalTick(){
		
		if(!this.animation.toString().equals("RatKingWalk"))setAnim(new RatKingStandardAnim(game), AnimationState.Normal);
		
		if(slotted()){
			slotTimer = slotTimeLimit;
			System.out.println("Slotted");
			this.xVel = 0;
			this.yVel = 0;
			Direction direction = pathFinder.findNextStep(this, findTarget(handler));
			if(direction == Direction.Up){
				//System.out.println("MOVE UP");
				this.yVel = -speed;
			}
			if(direction == Direction.Left){
				//System.out.println("MOVE LEFT");
				this.xVel = -speed;
			}
			if(direction == Direction.Right){
				//System.out.println("MOVE RIGHT");
				this.xVel = speed;
			}
			if(direction == Direction.Down){
				//System.out.println("MOVE DOWN");
				this.yVel = speed;
			}
		}else if(slotTimer == 0){
			this.state = EntityState.Recovering;
			this.xVel = 0;
			this.yVel = 0;
		}
		
		slotTimer--;
		
		this.x += xVel;
		this.y += yVel;
		
		checkPlayerCollision();
		
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
	
	private GameObject findTarget(Handler handler) {
		
		Treasure t = null;
		for(int i = 0; i < handler.object.size(); i++){
			if(handler.object.get(i).getId() == ID.Treasure){
				Treasure tempTreasure = (Treasure)handler.object.get(i);
				if(t == null){
					t = tempTreasure;
				}
				if(t.treasureValue() < tempTreasure.treasureValue() && tempTreasure.treasureValue() <= TreasureHandler.treasureValues[3]){
					t = tempTreasure;
				}				
			}
		}
		
		return t;
	}

	//Checks to see if locked onto tile grid
	private boolean slotted() {
		if((this.x - game.offSet)%32 == 0 && this.y%32 == 0)return true;
		return false;
	}
	
	private void deathTick(){
		deathTimer--;
		if(deathTimer <= 0)handler.removeObject(this);
	}
	
	private void recoverTick() {
		
		//Select point to align with
		int targetX, targetY;
		targetX = ((int)((this.x-game.offSet)/32))*32 + game.offSet;
		targetY = ((int)(this.y/32))*32;
		
		if((targetX-game.offSet-this.x)%32 > 16)targetX += 32;
		if((targetY-this.y)%32 > 16)targetY += 32;
		
		//Begin moving towards that point
		int dX = (int)(targetX - this.x), dY = (int)(targetY - this.y);
		
		double dist = Math.sqrt(dX*dX + dY*dY);
		
		//If close enough snap onto the point
		if(dist <= speed){
			this.x = targetX;
			this.y = targetY;
			state = EntityState.Normal;
			return;
		}
		
		double a = Math.asin(dY/dist);
		
		this.yVel = speed*Math.sin(a);
		
		if(targetX < this.x){
			this.xVel = -speed*Math.cos(a);
		}else{
			this.xVel = speed*Math.cos(a);
		}
		
		if(Math.abs(xVel) > speed)xVel = 0;
		if(Math.abs(yVel) > speed)yVel = 0;
		
		this.x += xVel;
		this.y += yVel;
		
		checkPlayerCollision();
		
	}


	@Override
	void collideWith(Tile tile) {

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
	
	public void getHit(Pot pot){

		die(pot);
		return;
		
	}
	
	@Override
	void collideWith(Player p) {
		setKnockback(p);
		game.timer.penalty(1);
		this.state = EntityState.Knockback;
		setAnim(new RatKingAttackAnim(game), AnimationState.Attack);
		p.setKnockback(this);
		p.state = EntityState.Knockback;
	}
	
	void die(Pot pot){		
		
		handler.addObject(new RatKingCrown((int)this.x + 10, (int)this.y, handler, game, pot.getxVel(), pot.getyVel()));
		game.treasureHandler.ratKill();
		this.state = EntityState.Death;
		dropTreasure();
		setAnim(new RatKingDeathAnim(game), AnimationState.Death);
		
	}
	
	private void dropTreasure(){
		int spread = 8;
		for(Treasure t: treasure){
			t.setX((int)(this.x + Math.random()*(42-t.getWidth()-8)));
			t.setVelX(Math.random()*(2*spread)-spread);
			t.setY((int)(this.y + Math.random()*(42-t.getHeight()-8)));
			t.setVelY((Math.random()*(2*spread))-spread);
			handler.addObject(t);
		}
		treasure.clear();
	}

	@Override
	public Rectangle collisionRect() {
		return collisionRect;
	}


	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
