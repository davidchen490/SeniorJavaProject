import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;


public class BossState extends GameState{
	
	//difficulty
	public static int PACIFIST = 0;
	public static int TEMPERATE = 1;
	public static int TURTLEMAGDON = 2;
	
	Boss boss;
	
	private Room[][] room;
	
	private boolean blockInput;
	private int difficulty = 1;
	private int sectorSize;
	private int xsector;
	private int ysector;
	
	private Player player;
	private TileMap tileMap;
	private Hud hud;
	private Dimension d;
	
	private JPanel parent;

	public BossState(GameStateManager gsm, Dimension d, int difficulty,  Player player) {
		super(gsm);
		this.d = d;
		this.player = player;
		this.difficulty = difficulty;
	}

	@Override
	public void draw(Graphics2D g) {		
		//draw tilemap
		tileMap.draw(g);
		
		//draw hud
		hud.draw(g);
		
		//draw enemies
		boss.draw(g);
		
		//draw player		
		player.draw1(g);
	}

	@Override
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			//Jukebox.stop("music1");
			gsm.setPaused(true);
		}
		else if (Keys.isPressed(Keys.I)) {
			gsm.giveInventory(player.getInventory());
			gsm.setConsumable(player.getConsumable());
			gsm.setInventory(true);			
		}
		if(blockInput) return;
		if(Keys.isDown(Keys.A)) player.setLeft();
		if(Keys.isDown(Keys.D)) player.setRight();
		if(Keys.isDown(Keys.W)) player.setUp();
		if(Keys.isDown(Keys.S)) player.setDown();
		if(Keys.isDown(Keys.UP)) player.attackingUp();
		if(Keys.isDown(Keys.DOWN)) player.attackingDown();
		if(Keys.isDown(Keys.LEFT)) player.attackingLeft();
		if(Keys.isDown(Keys.RIGHT)) player.attackingRight();
		if(Keys.isPressed(Keys.SPACE)) player.setAction();
	}

	@Override
	public void init() {
		//initiate random class
		
		Random r = new Random();
				
		//load map
		tileMap = new TileMap(32);
		tileMap.loadTiles("/Tilesets/tileset.png");
		tileMap.loadMap("/Maps/map2.map");
		
		
		//initialize player
		boss = new Boss(tileMap, player, difficulty);
		ArrayList<Enemy> enemies = new ArrayList<Enemy> ();
		player = new Player(tileMap, enemies, difficulty);
		player.setTilePosition(r.nextInt(1) + 70, r.nextInt(1) + 40);
		player.setEnemies(enemies);
		boss.setTilePosition(r.nextInt(62), r.nextInt(62) + 9);
		
		BossThread thread = new BossThread(boss);
		
		//set up camera position
		sectorSize = 1;
		xsector = player.getx() / sectorSize - 256;
		ysector = player.gety() / sectorSize - 256;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);
		
		//load hud
		hud = new Hud(player, d, parent);
		
		
		// load music
		Jukebox.load("Resources/Music/bossmusic.wav", "music1");
		Jukebox.loop("music1", 1000, 1000, Jukebox.getFrames("music1") - 1000);
//		Jukebox.load("Resources/Music/finish.mp3", "finish");
//		Jukebox.setVolume("finish", -10);
		
		// load sfx
		Jukebox.load("Resources/SFX/collect.wav", "collect");
		Jukebox.load("Resources/SFX/mapmove.wav", "mapmove");
		Jukebox.load("Resources/SFX/tilechange.wav", "tilechange");
		
		thread.run(); 
		//System.out.println(room[0][3].getDoors());
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		handleInput();
		
		//System.out.println(enemy.getx() + ", " + enemy.gety());
		
		//update camera
		int oldxs = xsector;
		int oldys = ysector;
		xsector = player.getx() / sectorSize - 256;
		ysector = player.gety() / sectorSize - 256;
		tileMap.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		tileMap.update();
		
		//update player
		player.update();
		
		//update enemies
		boss.update();

		hud.update();
	}

}
