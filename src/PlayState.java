import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;


public class PlayState extends GameState{
	
	//difficulty
	public static int PACIFIST = 0;
	public static int TEMPERATE = 1;
	public static int TURTLEMAGDON = 2;
	
	private ArrayList<Rectangle> boxes;
	private ArrayList<Enemy> enemies;
	private ArrayList<PowerUP> powerups;
	private ArrayList<Item> items;
	private ArrayList<Chest> chests;
	private ArrayList<Thread> enemyThreads; 
	
	private Room[][] room;
	
	private boolean blockInput;
	private boolean eventFinish;
	private boolean eventStart;
	
	private int eventTick;
	private int numPowerups;
	private int difficulty;
	private int sectorSize;
	private int xsector;
	private int ysector;
	
	private Player player;
	private TileMap tileMap;
	private Hud hud;
	private Dimension d;
	
	private JPanel parent;

	public PlayState(GameStateManager gsm, Dimension d, int difficulty) {
		super(gsm);
		this.d = d;
		this.difficulty = difficulty;
		System.out.println(difficulty);
	}

	@Override
	public void draw(Graphics2D g) {		
		//draw tilemap
		tileMap.draw(g);
		
		//draw hud
		hud.draw(g);
		
		//draw chest
		for (Chest chest : chests) {
			chest.draw(g);
		}
		
		//draw enemies
		for (Enemy enemy : enemies)
			enemy.draw(g);
		
		//draw powerups
		for (PowerUP powerup : powerups)
			powerup.draw(g);
		
		//draw player		
		player.draw(g);
		
		//draw items
		for(Item i : items) {
			i.draw(g);
		}
	}
	
	private void eventFinish() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 9; i++) {
				if(i % 2 == 0) boxes.add(new Rectangle(-128, i * 16, GamePanel.WIDTH, 16));
				else boxes.add(new Rectangle(128, i * 16, GamePanel.WIDTH, 16));
			}
			//Jukebox.stop("music1");
			//Jukebox.play("finish");
		}
		if(eventTick > 1) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0) {
					if(r.x < 0) r.x += 4;
				}
				else {
					if(r.x > 0) r.x -= 4;
				}
			}
		}
		if(eventTick > 33) {
			/*if(!Jukebox.isPlaying("finish")) {
				Data.setTime(player.getTicks());
				gsm.setState(GameStateManager.GAMEOVER);
			}*/
		}
	}

	private void eventStart() {
		eventTick++;
		if (eventTick == 1) {
			boxes.clear();
			for (int i = 0 ; i< 9; i++) {
				boxes.add(new Rectangle(0, i*16, GamePanel.WIDTH, 16));
			}
		}
		if (eventTick > 1 && eventTick < 32) {
			for (int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if (i % 2 == 0) {
					r.x -= 4;
				}
			}
		}
		if (eventTick == 33) {
			boxes.clear();
			eventStart = false;
			eventTick = 0;
		}
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
		
		
		//create lists
		items = new ArrayList<Item>();
		powerups = new ArrayList<PowerUP>();
		enemies = new ArrayList<Enemy>();
		enemyThreads = new ArrayList<Thread>();
		chests = new ArrayList<Chest>();
		room = new Room[4][4];
				
		//load map
		tileMap = new TileMap(32);
		tileMap.loadTiles("/Tilesets/tileset.png");
		tileMap.loadMap("/Maps/map1.map");
		
		//create Player
		player = new Player(tileMap, enemies, difficulty);
		
		//fill lists
		populateItems();
		populatePowerUps();
		
		//initialize player
		player.setTilePosition(r.nextInt(1) + 56, r.nextInt(1) + 56);
		player.setMasterItems(items);
		
		//set up camera position
		sectorSize = GamePanel.WIDTH;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);
		
		//load hud
		hud = new Hud(player, d, parent);
		
		// load music
		Jukebox.load("Resources/Music/bossmusic.wav", "music1");
		//Jukebox.setVolume("music1", );
		Jukebox.loop("music1", 1000, 1000, Jukebox.getFrames("music1") - 1000);
//		Jukebox.load("Resources/Music/finish.mp3", "finish");
//		Jukebox.setVolume("finish", -10);
		
		// load sfx
		Jukebox.load("Resources/SFX/collect.wav", "collect");
		Jukebox.load("Resources/SFX/mapmove.wav", "mapmove");
		Jukebox.load("Resources/SFX/tilechange.wav", "tilechange");
		
		// start event
		boxes = new ArrayList<Rectangle>();
		eventStart = true;
		populateRooms();
		
		
		//System.out.println(room[0][3].getDoors());
	}

	private void populateItems() {
/*		Item item;
		
		item = new Item(tileMap);
		item.setType(Item.RUBBER_STRIP);
		item.setTilePosition(26, 37);
		items.add(item);
		
		item = new Item(tileMap);
		item.setType(Item.ANESTHESIA);
		item.setTilePosition(12, 4);
		items.add(item);*/
	}

	private void populatePowerUps() {
		powerups.add(new PowerUP(tileMap));
		powerups.get(numPowerups).setType(PowerUP.HEALTH);
		powerups.get(numPowerups).setTilePosition(3, 3);
		numPowerups++;
	}
		
	private void populateRooms() {
		for (int y = 0; y < tileMap.getHeight() / tileMap.getTileSize() / 16; y++) {
			for (int x = 0; x < tileMap.getWidth() / tileMap.getTileSize() / 16; x++) {
				room[y][x] = new Room(tileMap, y, x);
				room[y][x].setPlayer(player);
				room[y][x].setMasterEnemies(enemies);
				room[y][x].setDifficulty(difficulty);
				room[y][x].setItems(items);
				room[y][x].setChests(chests);
				room[y][x].setEnemyThreads(enemyThreads);
				room[y][x].setPowerUps(powerups);
				room[y][x].init();
			}
		}
		room[0][0].getGSM(gsm);
		player.setEnemyThreads(enemyThreads);
	}
	
	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		handleInput();
		
		//System.out.println(enemy.getx() + ", " + enemy.gety());
		
		if (eventStart) eventStart();
		if (eventFinish) eventFinish();
		
		//update camera
		int oldxs = xsector;
		int oldys = ysector;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		tileMap.update();
		
		if(oldxs != xsector || oldys != ysector) {
			//Jukebox.play("mapmove");
		}
		
		if(tileMap.isMoving()) return;
		
		//update chests
		for (Chest chest : chests)
			chest.update();
		
		//update player
		player.update();

		for (Item item : items) {
			item.update();
		}
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				room[y][x].update();
			}
		}
		
		if (player.isDead()) {
			gsm.setPlayer(player);
			gsm.setState(GameStateManager.GAMEOVER);
		}
		
		//update enemies
		for (Enemy enemy : enemies)
			enemy.update();
		
		
		//update items
		try {
			for(int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
							
				if(player.intersects(item)) {
					if (item.collected(player)) {
						items.remove(i);
						i--;
						//Jukebox.play("collect");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		 
		hud.update();
		
		try {
			for (int i = 0; i < powerups.size(); i++) {
				if (player.intersects(powerups.get(i))) {
					System.out.println("picked up powerup");
					powerups.get(i).collected(player);
					powerups.remove(i);				
				}
			}
		} catch (Exception e) {}
	}

}
