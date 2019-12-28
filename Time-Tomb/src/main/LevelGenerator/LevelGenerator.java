package main.LevelGenerator;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import main.Direction;
import main.Game;
import main.Handler;
import main.ID;
import main.Animation.AnimationState;
import main.Animation.Player.PlayerFrontStillAnim;
import main.Decor.Decor;
import main.Entities.Ghost;
import main.Entities.Indicator;
import main.Entities.Player;
import main.Entities.RatKing;
import main.Entities.Skeleton;
import main.Objects.Pot;
import main.Objects.Sarcophagus;
import main.Tiles.Exit;
import main.Tiles.Floor;
import main.Tiles.Tile;
import main.Tiles.Wall;

public class LevelGenerator {

	int[][] tileMap = new int[12][12];
	int[][] potMap = new int[24][24];
	public int level = 0;
	ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
	double potWallChance = 0.15;
	double potCentreChance = 0.1;
	Game game;
	Handler handler;
	int mainChamber = 0;
	int potCount;
	Player player = null;
	
	//TILES CODES
	/*
	 * 0 - Wall
	 * 1 - Floor
	 * 2 - Pot
	 * 3 - PlayerSpawn
	 * 4 - Sarcophagus
	 * 11 - Exit UP
	 * 12 - Exit LEFT
	 * 13 - Exit RIGHT
	 * 14 - Exit DOWN
	 */
	
	public LevelGenerator(Handler handler, Game game){
		this.handler = handler;
		this.game = game;
	}
	
	public void generateLevel(){
		
		game.pickUp = false;
		game.heldPot = null;
		level++;
		clearObjects();
		potCount = 0;
		boolean validTile = false;
		while(!validTile){
			initiateTileMap();
			createTiles();
			validTile = checkValidTiles();
		}
		createExit();
		createTileDecors();		
		initiatePotMap();
		createPots();
		setPlayerPosition();
		if(level > 4)createEnemies();
	}
	
	private void createSarcophagus() {

		System.out.println("Starting");
		if(Math.random()*2.5 < 1.8)return;
		System.out.println("Chancing");
		//Finds possible locations for the sarcophagus
		ArrayList<Point> spawns = new ArrayList<Point>();
		for(int x = 0; x < 24; x++){
			for(int y = 0; y < 24; y++){
				if(potMap[x][y] != 2)continue;
				System.out.println("Maybe");
				if(potMap[x][y-1] == 0 && potMap[x-1][y+1] == 1 && potMap[x][y+1] == 1 && potMap[x+1][y+1] == 1){
					System.out.println("Yeah");
					if(potMap[x-1][y] == 2)potMap[x-1][y] = 1;
					if(potMap[x+1][y] == 2)potMap[x+1][y] = 1;
					spawns.add(new Point(x, y));
				}
			}
		}
		
		if(spawns.isEmpty())return;
		else{
			Point spawn = spawns.get((int)(Math.random()*spawns.size()));
			potMap[(int)spawn.getX()][(int)spawn.getY()] = 4;
		}
		
	}

	private void createTileDecors() {
		
		for(int x = 1; x < 11; x++){
			for(int y = 0; y < 11; y++){
				if(tileMap[x][y] != 0 || tileMap[x][y+1] != 0){
					
					if(tileMap[x+1][y] == 0 && tileMap[x+1][y+1] == 0){
						handler.addObject(new Decor(x*64 + game.offSet + 64, y*64, 8, 72, handler, game, game.img_verWallDec));
					}
					if(tileMap[x-1][y] == 0 && tileMap[x-1][y+1] == 0){
						handler.addObject(new Decor(x*64 + game.offSet - 8, y*64, 8, 72, handler, game, game.img_horWallDec));
					}
					if(tileMap[x][y+1] == 0){
						if(y == 10){
							handler.addObject(new Decor(x*64 + game.offSet, y*64 + 64, 64, 8, handler, game, game.img_verWallDec));
						}else if(tileMap[x][y+2] == 0){
							handler.addObject(new Decor(x*64 + game.offSet, y*64 + 64, 64, 8, handler, game, game.img_verWallDec));
						}
					}
					
				}
			}
		}
		
	}

	private void createEnemies() {

		int enemies = 0;
		int levelCount = level - 5;
		while(levelCount > 0){
			enemies += Math.random()*2;
			levelCount -= 10;
		}
		if(enemies > 3)enemies = 3;
		
		if (enemies <= 0) return;
		ArrayList<Point> spawns = new ArrayList<Point>();
		boolean[] pChambers = {false, false, false, false};
		int pX = (player.getX()-game.offSet)/32;
		int pY = player.getY()/32;
		
		pChambers[pointInChamber(new Point(pX, pY))-1] = true;
			
		for(int x = 2; x < 22; x++){
			for(int y = 2; y < 22; y++){
				if(potMap[x][y] == 1){
					int dX = pX - x;
					int dY = pY - y;
					if(Math.sqrt((dX*dX)+(dY*dY)) >= 6){
						if(!pChambers[pointInChamber(new Point(x, y)) - 1])spawns.add(new Point(x, y));
					}
				}
			}
		}
		
		if(potCount >= 13 && level >= 15){
			if(Math.random()*3 < 1.3){
				if(spawns.size() == 0)return;
				int spawn = (int)(Math.random()*spawns.size());
				handler.addObject(new RatKing((int)spawns.get(spawn).getX()*32 + game.offSet, (int)spawns.get(spawn).getY()*32, ID.RatKing, handler, game));
				spawns.remove(spawn);
				enemies--;
			}
		}
		
		while(enemies > 0){
			if(spawns.size() == 0)return;
			int spawn = (int)(Math.random()*spawns.size());
			if(Math.random()*3 > 1.8){
				handler.addObject(new Ghost((int)spawns.get(spawn).getX()*32 + game.offSet, (int)spawns.get(spawn).getY()*32, ID.Ghost, handler, game));
			}else{
				handler.addObject(new Skeleton((int)spawns.get(spawn).getX()*32 + game.offSet, (int)spawns.get(spawn).getY()*32, ID.Skeleton, handler, game));
			}
			spawns.remove(spawn);
			enemies--;
		}
		System.out.println(enemies);	
		
	}
	
	int pointInChamber(Point point){
		int x = (int)point.getX(), y = (int)point.getY();
		if(x <= 11){
			if(y <= 11){
				return 1;
			}else{
				return 3;
			}
		}else{
			if(y <= 11){
				return 2;
			}else{
				return 4;
			}
		}
	}

	boolean checkValidTiles(){
		
		boolean allFloor = true;
		
		for(int x = 1; x < 11; x++){
			for(int y = 1; y < 11; y++){
				if(tileMap[x][y] == tileMap[x+1][y+1] &&
						tileMap[x+1][y] != tileMap[x][y] && tileMap[x][y+1] != tileMap[x][y]){
					return false;
				}
				if(allFloor){
					if(tileMap[x][y] != 1)allFloor = false;
				}
			}
		}
		if(allFloor)return false;
		return true;
	}
	
	void clearObjects(){
		handler.reset();
	}
	
	void initiateTileMap(){
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				tileMap[x][y] = 0;
			}
		}
	}
	
	public Tile giveTile(int x, int y){
		if(tileMap[x][y] == 0){
			if(y < 11){
				if(tileMap[x][y+1] != 0){
					if(x != 0 && x != 11){
						if((tileMap[x-1][y] == 0 && tileMap[x+1][y] == 0 && tileMap[x-1][y+1] != 0 && tileMap[x+1][y+1] != 0) ||
								(tileMap[x-1][y] != 0 && tileMap[x+1][y] != 0)){
							if(Math.random()*5 <= 1)return new Wall(x*64 + game.offSet, y*64, game, game.img_wall[2]);
						}
					}
					return new Wall(x*64 + game.offSet, y*64, game, game.img_wall[1]);
				}
			}
			return new Wall(x*64 + game.offSet, y*64, game, game.img_wall[0]);
		}
		if(tileMap[x][y] == 1){
			int flrsprite = (int)(Math.random()*60);
			if(flrsprite <= 2){
				return new Floor(x*64 + game.offSet, y*64, game, game.img_floor[flrsprite + 1]);
			}else{
				return new Floor(x*64 + game.offSet, y*64, game, game.img_floor[0]);
			}
		}
		if(tileMap[x][y] == 11)return new Exit(x*64 + game.offSet, y*64, game, game.img_uExit, Direction.Up);
		if(tileMap[x][y] == 12)return new Exit(x*64 + game.offSet, y*64, game, game.img_lExit, Direction.Left);
		if(tileMap[x][y] == 13)return new Exit(x*64 + game.offSet, y*64, game, game.img_rExit, Direction.Right);
		if(tileMap[x][y] == 14)return new Exit(x*64 + game.offSet, y*64, game, game.img_dExit, Direction.Down);
		return null;
	}
	
	void createExit(){
		
		ArrayList<vExit> vExits = new ArrayList<vExit>();
		for(int x = 1; x < 11; x++){
			for(int y = 1; y < 11; y++){
				
				if(tileMap[x][y] == 0)
					//Decides which walls could be viable exits
					if(tileMap[x-1][y] == 1 && tileMap[x+1][y] == 0 && tileMap[x][y-1] == 0 && tileMap[x][y+1] == 0){
						vExits.add(new vExit(x, y, 12));
					}
					else if(tileMap[x-1][y] == 0 && tileMap[x+1][y] == 1 && tileMap[x][y-1] == 0 && tileMap[x][y+1] == 0){
						vExits.add(new vExit(x, y, 13));
					}
					else if(tileMap[x-1][y] == 0 && tileMap[x+1][y] == 0 && tileMap[x][y-1] == 1 && tileMap[x][y+1] == 0){
						vExits.add(new vExit(x, y, 11));
					}
					else if(tileMap[x-1][y] == 0 && tileMap[x+1][y] == 0 && tileMap[x][y-1] == 0 && tileMap[x][y+1] == 1){
						vExits.add(new vExit(x, y, 14));
					}
				
			}
		}
		
		//Randomly decides on an exit to use
		if(vExits.size() == 0){
			generateLevel();
			return;
		}
		
		vExit exit = vExits.get((int)Math.random()*vExits.size());
		tileMap[exit.getX()][exit.getY()] = exit.getTileCode();
		
	}
	
	void createTiles(){
		
		mainChamber = (int)(Math.random()*4);
		if(mainChamber == 0){
			createChamber(1, 1, 4, 4, 5, 5);
		}else{
			createChamber(1, 1, 0, 0, 5, 5);
		}
		if(mainChamber == 1){
			createChamber(1, 6, 4, 4, 5, 5);
		}else{
			createChamber(1, 6, 0, 0, 5, 5);
		}
		if(mainChamber == 2){
			createChamber(6, 1, 4, 4, 5, 5);
		}else{
			createChamber(6, 1, 0, 0, 5, 5);
		}
		if(mainChamber == 3){
			createChamber(6, 6, 4, 4, 5, 5);
		}else{
			createChamber(6, 6, 0, 0, 5, 5);
		}
		createPaths();
		addFloors();
		
	}
	
	public boolean[][] getSolids(){
		boolean[][] solidMap = new boolean[24][24];
		for(int x = 0; x < 24; x++){
			for(int y = 0; y < 24; y++){
				if(potMap[x][y] == 0 || potMap[x][y] == 2){
					solidMap[x][y] = true;
				}else solidMap[x][y] = false;
			}
		}		
		return solidMap;		
	}
	
	void setPlayerPosition(){
		
		ArrayList<Point> spawns = new ArrayList<Point>();
		for(int x = 2; x < 22; x++){
			for(int y = 2; y < 22; y++){
				if(potMap[x][y] == 1)spawns.add(new Point(x, y));
			}
		}
		if(player == null){
			for(int i = 0; i < handler.object.size(); i++){
				if(handler.object.get(i).getId() == ID.Player){
					player = (Player)handler.object.get(i);
					break;
				}
			}
		}
		Point spawn = spawns.get((int)(Math.random()*spawns.size()));
		player.setX((int)(spawn.getX()*32) + game.offSet);
		player.setY((int)(spawn.getY()*32));
		player.setAnim(new PlayerFrontStillAnim(game), AnimationState.Normal);
		handler.addObject(new Indicator(0, 0, ID.Indicator, handler, game, handler.object.get(0), Color.WHITE));
		
	}
	
	//Creates a chamber for the level where x is the min x, y is the min y, w is the min width, h is the min height
	void createChamber(int mX, int mY, int minW, int minH, int maxW, int maxH){
		
		//Generates the width and height of the chambers, in the range of w||h -> 5
		int rectWidth = (int)(Math.random()*(maxW + 1 - minW)) + minW;
		int rectHeight = (int)(Math.random()*(maxW + 1 - minH)) + minH;
		
		//A chamber is not created if the room is too small
		if(rectWidth <= 1 || rectHeight <= 1){
			if((int)(Math.random()*4) == 0)createChamber(mX, mY, minW, minH, maxW, maxH);
			return;
		}
		
		int rectX = (int)(Math.random()*(6 - rectWidth)) + mX;
		int rectY = (int)(Math.random()*(6 - rectHeight)) + mY;
		
		System.out.println(rectX + " " + rectY + " " + rectWidth + " " + rectHeight);
		
		rects.add(new Rectangle(rectX, rectY, rectWidth, rectHeight));
		
	}
	
	void createPaths(){
		
		Rectangle[] r = rects.toArray(new Rectangle[rects.size()]);
		
		for(int i = 0; i < r.length - 1; i++){
			if(Math.random() * 2 < 1){
				Rectangle temp = r[i];
				r[i] = r[i + 1];
				r[i + 1] = temp;
			}
		}
		
		for(int i = 0; i < r.length - 1; i++){
			connectRooms(r[i], r[i + 1]);
		}
		
	}
	
	void connectRooms(Rectangle r1, Rectangle r2){
		
		int pathX = (int)(Math.random()*(r1.width - 1) + r1.x);
		int pathY = (int)(Math.random()*(r2.height - 1) + r2.y);
		
		System.out.println(pathX + " " + pathY);
		
		if(r2.x < pathX){
			rects.add(new Rectangle(r2.x, pathY, pathX - r2.x + 2, 2));
		}else if(pathX < r2.x){
			rects.add(new Rectangle(pathX, pathY, r2.x - pathX + 2, 2));
		}
		
		if(r1.y < pathY){
			rects.add(new Rectangle(pathX, r1.y, 2, pathY - r1.y));
		}else if(pathY < r1.y){
			rects.add(new Rectangle(pathX, pathY, 2, r1.y - pathY + 2));
		}
		
	}
	
	void addFloors(){
		while(rects.size() >= 1){
			Rectangle rect = rects.get(0);
			System.out.println(rect.x + " " + rect.y + " " + rect.width + " " + rect.height);
			for(int x = rect.x; x < rect.x + rect.width; x++){
				for(int y = rect.y; y < rect.y + rect.height; y++){
					tileMap[x][y] = 1;
				}
			}
			rects.remove(0);
		}
	}
	
	public void drawMap(){
		for(int y = 0; y < 10; y++){
			//String line = "";
			for(int x = 0; x < 10; x++){
				System.out.print(tileMap[x][y]);
			}
			System.out.print("\n");
		}
	}
	
	void initiatePotMap(){
		
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				potMap[x*2][y*2] = potMap[x*2 + 1][y*2] = potMap[x*2 + 1][y*2 + 1] = potMap[x*2][y*2 + 1] = tileMap[x][y];
			}
		}
		
	}
	
	void createPots(){
		
		for(int x = 2; x < 22; x++){
			for(int y = 2; y < 22; y++){
				
				//Makes sure the pots are not within range of the exit
				if(potMap[x][y-2] == 14)continue;
				if(potMap[x-2][y] == 13)continue;
				if(potMap[x+2][y] == 12)continue;
				if(potMap[x][y+2] == 11)continue;
				
				//Decides where to place pots
				if(potMap[x][y] == 1){
					
					//Wall pots
					if(potMap[x-1][y] == 0){
						if(Math.random() < potWallChance){
							if(potMap[x][y] != 2)potCount++;
							potMap[x][y] = 2;
						}
					}
					if(potMap[x][y-1] == 0){
						if(Math.random() < potWallChance){
							if(potMap[x][y] != 2)potCount++;
							potMap[x][y] = 2;
						}
					}
					if(potMap[x+1][y] == 0){
						if(Math.random() < potWallChance){
							if(potMap[x][y] != 2)potCount++;
							potMap[x][y] = 2;
						}
					}
					if(potMap[x][y+1] == 0){
						if(Math.random() < potWallChance){
							if(potMap[x][y] != 2)potCount++;
							potMap[x][y] = 2;
						}
					}
					
					//Centre pots
					if(potMap[x - 2][y - 2] == 1 && potMap[x][y - 2] == 1 && potMap[x + 2][y - 2] == 1 &&
							potMap[x - 2][y] == 1 && potMap[x + 2][y] == 1 && potMap[x - 2][y + 2] == 1 &&
							potMap[x][y + 2] == 1 && potMap[x + 2][y + 2] == 1){
						if(Math.random() < potCentreChance){
							if(potMap[x][y] != 2)potCount++;
							potMap[x][y] = 2;
						}
						
					}
				}				
				
			}
		}
		
		if(level >= 18){
			createSarcophagus();
		}
		
		//Puts the pots in the handler and decides which parts of solids
		for(int x = 2; x < 22; x++){
			for(int y = 2; y < 22; y++){
				if(potMap[x][y] == 2){
					Pot tempPot = new Pot(x*32 + game.offSet, y*32, handler, game, game.img_potRegular);
					if(potMap[x-1][y] == 1)tempPot.setLeftSolid(true);
					if(potMap[x+1][y] == 1)tempPot.setRightSolid(true);
					if(potMap[x][y-1] == 1)tempPot.setUpSolid(true);
					if(potMap[x][y+1] == 1)tempPot.setDownSolid(true);
					handler.addObject(tempPot);
				}
				if(potMap[x][y] == 4){
					handler.addObject(new Sarcophagus(x*32 + game.offSet, y*32, ID.Pot, handler, game));
				}
			}
		}
		
	}
	
	
}
