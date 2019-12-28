package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import main.Audio.SoundPlayer;
import main.Display.HUD;
import main.Display.TreasureHandler;
import main.Display.Window;
import main.Entities.Player;
import main.Input.KeyInput;
import main.Input.MouseInput;
import main.LevelGenerator.LevelGenerator;
import main.Menu.Menu;
import main.Menu.Statistics.Statistics;
import main.Menu.Statistics.StatisticsMenu;
import main.Objects.Pot;
import main.Tiles.Tile;
import main.Tiles.TileID;
import main.Timer.Timer;

public class Game extends Canvas implements Runnable{
	
	//Code + Graphical Assets by Tyler Thorn
	//SoundFX by Travis Rogers
	//Skeleton Spawn by FacadeGaikan
	
	private static final long serialVersionUID = 6691247796639148462L;

	public final int WIDTH = 1024;

	public final int HEIGHT = 768;
	
	private Thread thread;
	private boolean running = false;
	LevelGenerator gen;
	public int offSet;
	public boolean playing = false;
	public GameState gameState = GameState.Menu;
	
	private Handler handler;
	
	public boolean pickUp = false;
	
	public BufferedImage img_lExit, img_rExit, img_uExit, img_dExit, img_pot, img_coin, img_sapphire, img_emerald, img_ruby, img_skeleHead,
							img_horWallDec, img_verWallDec, img_startButton, img_statisticsButton, img_quitButton, img_backButton, img_ratKingCrown,
							img_potOutline;
	public BufferedImage img_ghost1 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_ghost2 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_ghostAttack = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeleton1 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeleton2 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeletonDeath1 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeletonDeath2 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeletonDeath3 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_skeletonDamage = new BufferedImage(32, 52, BufferedImage.TYPE_INT_ARGB),
			img_skeletonAttack = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerStillFront = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerWalk1 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerWalk2 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerPotFront = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerPotWalk1 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerPotWalk2 = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_playerDamage = new BufferedImage(32, 36, BufferedImage.TYPE_INT_ARGB),
			img_ratKingWalk1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_ratKingWalk2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_ratKingAttack = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_ratKingDeath1 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_ratKingDeath2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),		
			img_ratKingDeath3 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_ratKingDeath4 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_potRegular = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_potHeld = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB),
			img_sarcophagusClosed = new BufferedImage(32, 48, BufferedImage.TYPE_INT_ARGB),
			img_sarcophagusOpen = new BufferedImage(32, 48, BufferedImage.TYPE_INT_ARGB),
			img_maskObject = new BufferedImage(16, 18, BufferedImage.TYPE_INT_ARGB),
			img_maskPossessed = new BufferedImage(16, 18, BufferedImage.TYPE_INT_ARGB);
	public int[] treasureValues = {10};
	public BufferedImage[] img_floor = new BufferedImage[4], img_wall = new BufferedImage[3], img_timer, img_timerLow;
	
	public Tile[][] tiles = new Tile[12][12];
	public boolean[][] solids = new boolean[24][24];
	public Pot heldPot = null;
	public int money = 0;
	public HUD hud;
	public Timer timer;
	public TreasureHandler treasureHandler;
	public int coinFreq = 0;
	SoundPlayer soundPlayer = new SoundPlayer();
	
	public Menu menu;
	public StatisticsMenu stats;
	
	public Game(){
		
		Statistics.loadStats();
		loadSprites();
		offSet = (WIDTH - (12*64))/2;
		handler = new Handler(this);
		hud = new HUD(0, 0, ID.HUD, handler, this);
		menu = new Menu(this);
		stats = new StatisticsMenu(this);
		new Window(WIDTH, HEIGHT, "Time Tomb", this);			
		
		gen = new LevelGenerator(handler, this);
		
		handler.object.add(new Player(0, 0, ID.Player, handler, this));
		
		this.addKeyListener(new KeyInput(handler, this));
		this.addMouseListener(new MouseInput(this, handler));
		SoundPlayer.playMusic("Resources/Sounds/Music/Background.wav", -20f);
		
		//startGame();
		
		playing = true;
		
		//SoundPlayer.playSound("Resources/Sounds/Treasure/CoinJingle2.wav");
		
	}
	
	public void startGame(){
		
		this.timer = new Timer(30, this);
		this.treasureHandler = new TreasureHandler(this);
		this.gameState = GameState.Playing;
		gen.level = 0;
		createLevel();
		money = 0;
		this.playing = true;
		
	}
	
	public void endGame(){
		
		handler.reset();
		this.playing = false;
		updateStatistics();
		Statistics.saveStats();
		
	}
	
	public void updateStatistics(){
		
		Statistics.treasureCollected += (treasureHandler.totalValue());
		Statistics.coinsCollected += treasureHandler.getTreasureAmount(0);
		Statistics.sapphiresCollected += treasureHandler.getTreasureAmount(1);
		Statistics.emeraldsCollected += treasureHandler.getTreasureAmount(2);
		Statistics.rubiesCollected += treasureHandler.getTreasureAmount(3);
		Statistics.skeleHeadsCollected += treasureHandler.getTreasureAmount(4);
		Statistics.trappedGhostsCollected += treasureHandler.getTreasureAmount(6);
		Statistics.ratCrownsCollected += treasureHandler.getTreasureAmount(5);
		Statistics.skeletonsDefeated += treasureHandler.skeleKilled();
		Statistics.ghostsTrapped += treasureHandler.ghostKilled();
		Statistics.ratKingsDefeated += treasureHandler.ratKilled();
		Statistics.potsSmashed += treasureHandler.potsSmashed();
		Statistics.roomsCompleted += treasureHandler.roomsBeaten();
		Statistics.gamesPlayed++;
		
	}
	
	public void loadSprites(){
		
		img_horWallDec = loadImage("Resources/Sprites/Tiles/wallHor.png");
		img_verWallDec = loadImage("Resources/Sprites/Tiles/wallVer.png");
		img_lExit = loadImage("Resources/Sprites/Tiles/exitLeft.png");
		img_rExit = loadImage("Resources/Sprites/Tiles/exitRight.png");
		img_uExit = loadImage("Resources/Sprites/Tiles/exitUp.png");
		img_dExit = loadImage("Resources/Sprites/Tiles/exitDown.png");
		img_pot = loadImage("Resources/Sprites/Objects/pot.png");
		img_coin = loadImage("Resources/Sprites/Objects/coin.png");
		img_sapphire = loadImage("Resources/Sprites/Objects/sapphire.png");
		img_emerald = loadImage("Resources/Sprites/Objects/emerald.png");
		img_ruby = loadImage("Resources/Sprites/Objects/ruby.png");
		img_skeleHead = loadImage("Resources/Sprites/Objects/SkeletonHead.png");
		img_ratKingCrown = loadImage("Resources/Sprites/Objects/RatKingCrown.png");
		img_potOutline = loadImage("Resources/Sprites/Objects/PotOutline.png");
		
		//Load Menu Buttons
		img_startButton = loadImage("Resources/Sprites/Menu/StartButton.png");
		img_statisticsButton = loadImage("Resources/Sprites/Menu/StatisticsButton.png");
		img_quitButton = loadImage("Resources/Sprites/Menu/QuitButton.png");
		img_backButton = loadImage("Resources/Sprites/Menu/BackButton.png");
		
		//Load Player Sprites		
		BufferedImage sprsht_player = (BufferedImage) loadImage("Resources/Sprites/Entities/Player/PlayerSpriteSheet.png");
		img_playerStillFront = sprsht_player.getSubimage(0, 0, 32, 36);
		img_playerWalk1 = sprsht_player.getSubimage(32, 0, 32, 36);
		img_playerWalk2 = sprsht_player.getSubimage(64, 0, 32, 36);
		img_playerPotFront = sprsht_player.getSubimage(0, 36, 32, 36);
		img_playerPotWalk1 = sprsht_player.getSubimage(32, 36, 32, 36);
		img_playerPotWalk2 = sprsht_player.getSubimage(64, 36, 32, 36);
		img_playerDamage = sprsht_player.getSubimage(96, 0, 32, 36);
		
		//Load Pot Sprites
		BufferedImage sprsht_pots = (BufferedImage) loadImage("Resources/Sprites/Objects/pot.png");
		img_potRegular = sprsht_pots.getSubimage(0, 0, 32, 32);
		img_potHeld = sprsht_pots.getSubimage(32, 0, 32, 32);
		
		//Load Sarcophagus Sprites
		BufferedImage sprsht_sarcophagus = (BufferedImage) loadImage("Resources/Sprites/Objects/Sarcophagus.png");
		img_sarcophagusClosed = sprsht_sarcophagus.getSubimage(0, 0, 32, 48);
		img_sarcophagusOpen = sprsht_sarcophagus.getSubimage(32, 0, 32, 48);
		
		//Loads ghost sprites	
		BufferedImage sprsht_ghostStandard = (BufferedImage) loadImage("Resources/Sprites/Entities/Ghost/GhostSprSheet.png");	
		img_ghost1 = sprsht_ghostStandard.getSubimage(0,0,32,36);
		img_ghost2 = sprsht_ghostStandard.getSubimage(32,0,32,36);
		img_ghostAttack = sprsht_ghostStandard.getSubimage(0, 36, 32, 36);
		
		//Load skeleton sprites
		
		BufferedImage sprsht_skeletonStandard = (BufferedImage) loadImage("Resources/Sprites/Entities/Skeleton/SkeletonSprSheet.png");
		img_skeleton1 = sprsht_skeletonStandard.getSubimage(0, 0, 32, 36);
		img_skeleton2 = sprsht_skeletonStandard.getSubimage(32, 0, 32, 36);
		img_skeletonDeath1 = sprsht_skeletonStandard.getSubimage(0, 36, 32, 36);
		img_skeletonDeath2 = sprsht_skeletonStandard.getSubimage(32, 36, 32, 36);
		img_skeletonDeath3 = sprsht_skeletonStandard.getSubimage(64, 36, 32, 36);
		img_skeletonDamage = sprsht_skeletonStandard.getSubimage(96, 0, 32, 52);
		img_skeletonAttack = sprsht_skeletonStandard.getSubimage(64, 0, 32, 36);
		
		//Load RatKing sprites
		
		BufferedImage sprsht_ratKingStandard = (BufferedImage) loadImage("Resources/Sprites/Entities/RatKing/RatKingSprSht.png");
		img_ratKingWalk1 = sprsht_ratKingStandard.getSubimage(0, 0, 32, 32);
		img_ratKingWalk2 = sprsht_ratKingStandard.getSubimage(32, 0, 32, 32);
		img_ratKingAttack = sprsht_ratKingStandard.getSubimage(64, 0, 32, 32);
		img_ratKingDeath1 = sprsht_ratKingStandard.getSubimage(0, 32, 32, 32);
		img_ratKingDeath2 = sprsht_ratKingStandard.getSubimage(32, 32, 32, 32);
		img_ratKingDeath3 = sprsht_ratKingStandard.getSubimage(64, 32, 32, 32);
		img_ratKingDeath4 = sprsht_ratKingStandard.getSubimage(96, 32, 32, 32);
		
		//Load Timer sprites
		BufferedImage sprsht_timer = (BufferedImage) loadImage("Resources/Sprites/HUD/Hourglassspritesheet.png");
		img_timer = new BufferedImage[sprsht_timer.getWidth()/112];
		img_timerLow = new BufferedImage[sprsht_timer.getWidth()/112];
		for(int i = 0; i < img_timer.length; i++){
			img_timer[i] = sprsht_timer.getSubimage(112*i, 0, 112, 112);
			img_timerLow[i] = sprsht_timer.getSubimage(112*i, 112, 112, 112);
		}
		
		//Load Mask sprites
		BufferedImage sprsht_mask = (BufferedImage) loadImage("Resources/Sprites/Objects/Mask.png");
		img_maskObject = sprsht_mask.getSubimage(0, 0,  16, 18);
		img_maskPossessed = sprsht_mask.getSubimage(16, 0, 16, 18);
		
		//Load floor sprites
		BufferedImage sprsht_floor = (BufferedImage) loadImage("Resources/Sprites/Tiles/Floor.png");
		for(int i = 0; i < 4; i++)img_floor[i] = sprsht_floor.getSubimage(i*64, 0, 64, 64);
		
		//Load wall sprites	
		BufferedImage sprsht_wall = (BufferedImage) loadImage("Resources/Sprites/Tiles/Wall.png");
		sprsht_wall = loadImage("Resources/Sprites/Tiles/Wall.png");
		for(int i = 0; i < 3; i++)img_wall[i] = sprsht_wall.getSubimage(i*64, 0, 64, 64);
		
	}
	
	public void createLevel(){
		
		gen.generateLevel();
		gen.drawMap();
		getTileMap(gen);
		setTileSolids();
		solids = gen.getSolids();
		timer.resetTime();
		
	}
	
	public void setTileSolids(){
		
		for(int x = 1; x < 11; x++){
			for(int y = 1; y < 11; y++){
				if(!tiles[x][y].isSolid() || tiles[x][y].getTileID() == TileID.Exit){
					
					//Decides which sides of the walls will be solid
					if(tiles[x-1][y].isSolid() )tiles[x-1][y].setRightSolid(true);
					if(tiles[x+1][y].isSolid())tiles[x+1][y].setLeftSolid(true);
					if(tiles[x][y-1].isSolid())tiles[x][y-1].setDownSolid(true);
					if(tiles[x][y+1].isSolid())tiles[x][y+1].setUpSolid(true);
					
				}
			}
		}
		
	}
	
	public BufferedImage loadImage(String pathName){
		
		try{
			return ImageIO.read(new File(pathName));
		}catch(Exception IOError){
			System.out.println("Image could not be found at " + pathName);
			return null;
		}

	}

	public void getTileMap(LevelGenerator lg){
		
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				this.tiles[x][y] = lg.giveTile(x, y);
			}
		}
		
	}
	
	public synchronized void start(){
		
		thread = new Thread(this);
		thread.start();
		running = true;
		
	}
	
	public synchronized void stop(){
		
		try{
			
			//Waits for other threads to complete
			thread.join();
			running = false;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void run(){
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(running){
				render();
			}
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		
		stop();
		
	}
	
	private void tick(){
		
		if(gameState == GameState.Playing){
			if(playing){
				handler.tick();
				timer.tick();
				if(coinFreq > 0)coinFreq--;
			}
		}else if(gameState == GameState.Menu){
			menu.tick();
		}else if(gameState == GameState.Statistics){
			stats.tick();
		}
		
	}
	
	private void render(){
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		if(gameState == GameState.Playing){
			if(playing){
				renderTiles(g);
				handler.render(g);
				hud.render(g);
			}else{
				g.setColor(Color.WHITE);
				g.drawString("You have run out of time! \n"
						+ " You collected £" + money + " worth of treasure!\n"
								+ " Press any key to return to Menu!", 200, 200);
			}
		}else if(gameState == GameState.Menu){
			menu.render(g);
		}else if(gameState == GameState.Statistics){
			stats.render(g);
		}
		
		g.dispose();
		bs.show();
		
	}
	
	public void renderTiles(Graphics g){
		
		for(int x = 0; x < 12; x++){
			for(int y = 0; y < 12; y++){
				if(tiles[x][y] != null){
					tiles[x][y].render(g);
				}
			}
		}
		
	}
	
	public static void main(String args[]){
		
		new Game();
		
	}
	
	public static int clamp(int val, int min, int max){
		
		if(val <= min)return min;
		if(val >= max)return max;
		return val;
		
	}
	
}
