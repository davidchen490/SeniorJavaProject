import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Room {
	private TileMap tileMap;
	private Random r;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Enemy> publicEnemies;
	private ArrayList<Item> masterItems;
	private ArrayList<Chest> chests;
	private ArrayList<Thread> enemyThreads;
	private ArrayList<PowerUP> powerups;
	private Chest chest;
	private GameStateManager gsm;
	
	private int x;
	private int y;
	private int width;
	private int height;
	private int row;
	private int col;
	private int difficulty;
	private int state;
	 
	private ArrayList<Point> viableSpawnPoints;
	private ArrayList<Point> doors;
	
	public static int UNENTERED = 0;	
	public static int ENTERED = 1;
	public static int LOCK_DOORS = 2;
	public static int SPAWNED_ENEMIES = 3;
	public static int DROP_PHASE = 4;
	public static int CLEARED = 5;
	
	private int rand;	
	
	public int ENEMY_SPAWN_DELAY = 2000;
	public int CHECK_DOOR_DELAY = 1000;
	
	long enterRoomTime;
	long checkDoorTime;
	long spawnEnemyTime;
	long exitRoomTime;

	
	public Room(TileMap tm) {
		tileMap =  tm;
		width = tileMap.getTileSize() * 16;
		height = tileMap.getTileSize() * 16;		
	}
	public Room(TileMap tm, int _row, int _col) {
		tileMap = tm;
		row = _row;
		col = _col;
		width = tileMap.getTileSize() * 16;
		height = tileMap.getTileSize() * 16;
		x = _col * width;
		y = _row * height;
	}
	public void setPosition(int _row, int _col) {
		row = _row;
		col = _col;
		x = _col * width;
		y = _row * height;
	}
	public void getGSM(GameStateManager gsm) {
		this.gsm = gsm;
	}
	public void setItems(ArrayList<Item> items) {
		masterItems = items;
	}
	public void setChests(ArrayList<Chest> chests) {
		this.chests = chests;
	}
	public void setEnemyThreads(ArrayList<Thread> threads) {
		enemyThreads = threads;
	}
	public void setPowerUps(ArrayList<PowerUP> powerups) {
		this.powerups = powerups;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	public ArrayList<Point> getDoors() {
		return doors;
	}
	public void setPlayer(Player _player) {
		player = _player;
	}
	public void setMasterEnemies(ArrayList<Enemy> _enemies) {
		publicEnemies = _enemies;
	}
	public String toString() {
		if (state == UNENTERED) {
			return "Room: (" + col + ", " + row + ") is unentered."; 
		}
		else if (state == ENTERED) {
			return "Room: (" + col + ", " + row + ") is entered.";
		}
		else if (state == LOCK_DOORS) {
			return "Room: (" + col + ", " + row + ") has locked its doors.";
		}
		else if (state == SPAWNED_ENEMIES) {
			return "Room: (" + col + ", " + row + ") has spawned enemies.";
		}
		else if (state == CLEARED) {
			return "Room: (" + col + ", " + row + ") is cleared.";
		}
		else if (state == DROP_PHASE) {
			return "Room: (" + col + ", " + row + ") is dropping items.";
		}
		else {
			return "Room: (" + col + ", " + row + ") is unknown."; 
		}
	}
	public boolean hasPlayer() {
		int playerRoomX = -tileMap.getx();
		int playerRoomY = -tileMap.gety();
		if (playerRoomX == x && playerRoomY == y)
			return true;
		else 
			return false;
	}
	public void setDifficulty(int _difficulty) {
		difficulty = _difficulty;
	}
	public void init() {
		Jukebox.load("Resources/SFX/door.wav", "door");
		r = new Random();
		state = UNENTERED;
		viableSpawnPoints = new ArrayList<Point>();
		doors = new ArrayList<Point>();
		enemies = publicEnemies;
		for (int y = 16 * row; y < 16 * (row+1); y++) {
			for (int x = 16 * col; x < 16*(col+1); x++) {
				if (tileMap.getType(y, x) == Tile.NORMAL) {
					viableSpawnPoints.add(new Point(x, y));
				}
				else if (tileMap.getType(y, x) == Tile.OPEN_DOOR) {
					doors.add(new Point(x, y));
				}
			}
		}	
	}
	public void spawnEnemies() {
		int numOfCreatures = 1;
		if (difficulty == PlayState.PACIFIST) {
			numOfCreatures = 2 + r.nextInt(2);
		}
		else if (difficulty == PlayState.TEMPERATE) {
			numOfCreatures = 3 + r.nextInt(4);
		}
		else if (difficulty == PlayState.TURTLEMAGDON) {
			numOfCreatures = 4 + r.nextInt(6);
		}
		for (int i = 0; i < numOfCreatures; i++) {
			enemies.add(new Enemy(tileMap, player));
		}
		for (Enemy enemy : enemies) {
			int spawn = r.nextInt(viableSpawnPoints.size());
			
			enemy.setPosition(
					(int)viableSpawnPoints.get(spawn).getX() * 32,
					(int)viableSpawnPoints.get(spawn).getY() * 32);
			
			Thread thread = new Thread(new EnemyThread(enemy));
			enemyThreads.add(thread);
			thread.start();
		
		}
	}
	public boolean lockDoors() {
		if (doors.size() == 0) {
			return false;
		}
		for (Point point : doors) {
			if (player.getCol() != point.getX() ||
					player.getRow() != point.getY()) {
			}
			else {
				return false;
			}
		}
		for (Point point : doors)
			tileMap.setTile((int)point.getY(), (int)point.getX(),
					(tileMap.getIndex((int)point.getY(), (int)point.getX()) + 24));
		return true;
	}
	public void unlockDoors() {
		for (Point point : doors) {
			tileMap.setTile((int)point.getY(), (int)point.getX(),
					(tileMap.getIndex((int)point.getY(), (int)point.getX()) - 24));
		}
	}
	public void update() {
		//System.out.println(hasPlayer());
		//System.out.println(state);
		if (hasPlayer() && 
				state == UNENTERED) {
			checkDoorTime = System.currentTimeMillis() + CHECK_DOOR_DELAY;
			state = ENTERED;
			System.out.println(toString());		
			
					
		}
		else if (hasPlayer() && 
				state == ENTERED) {
			if (System.currentTimeMillis() > checkDoorTime) {
				boolean worked = lockDoors();
				if (worked) {
					enterRoomTime = System.currentTimeMillis();
					spawnEnemyTime = System.currentTimeMillis() + ENEMY_SPAWN_DELAY;
					state = LOCK_DOORS;
					Jukebox.play("door");
					System.out.println(toString());
					rand = r.nextInt(100);
					
					
					if (player.hasMetalDetector())
						if (rand > 50) {
							chest = new Chest(tileMap, player.hasFourLeafClover());
							int random = r.nextInt(viableSpawnPoints.size());
							Point point = viableSpawnPoints.get(random);
							chest.setTilePosition((int)point.getY(), (int)point.getX());
							chests.add(chest);
							viableSpawnPoints.remove(random);
						}
						else {
							System.out.println("no chest");
						}
					else 
						if (rand > 60) {
							chest = new Chest(tileMap, player.hasFourLeafClover());
							int random = r.nextInt(viableSpawnPoints.size());
							Point point = viableSpawnPoints.get(random);
							chest.setTilePosition((int)point.getY(), (int)point.getX());
							chests.add(chest);
							viableSpawnPoints.remove(random);
						}
						else {
							System.out.println("no chest");
						}
				}				
			}
		}
		else if (hasPlayer() && 
				state == LOCK_DOORS) {
			if (System.currentTimeMillis() > spawnEnemyTime) {
				spawnEnemies();
				state = SPAWNED_ENEMIES;
				System.out.println(toString());
			}
		}
		else if (hasPlayer() &&
				state == SPAWNED_ENEMIES) {
			toString();
			if (publicEnemies.size() == 0) {
				unlockDoors();
				Jukebox.play("door");
				state = DROP_PHASE;
				System.out.println(toString());
			}
		}
		else if (hasPlayer() &&
				state == DROP_PHASE) {
			toString();
			if (player.hasMetalDetector()) {
				if (rand > 50) {
					boolean done = chest.open();
					if (done) {
						masterItems.add(chest.recieve());
						state = CLEARED;
					}
				}
				else if (rand < 50) {
					PowerUP powerup = new PowerUP(tileMap);
					powerup.setType(PowerUP.HEALTH);
					int random = r.nextInt(viableSpawnPoints.size());
					powerup.setTilePosition((int)viableSpawnPoints.get(random).getX(),(int)viableSpawnPoints.get(random).getY());
					powerups.add(powerup);
					state = CLEARED;
				}
			}
			else {
				if (rand > 60) {
					boolean done = chest.open();
					if (done) {
						masterItems.add(chest.recieve());
						state = CLEARED;
					}
				}
				else if (rand < 60) {
					PowerUP powerup = new PowerUP(tileMap);
					powerup.setType(PowerUP.HEALTH);
					int random = r.nextInt(viableSpawnPoints.size());
					powerup.setTilePosition((int)viableSpawnPoints.get(random).getX(),(int)viableSpawnPoints.get(random).getY());
					powerups.add(powerup);
					state = CLEARED;
				}
				else {
					state = CLEARED;
				}
			}	
		}
		else if (hasPlayer() &&
				state == CLEARED) {
			System.out.println(toString());
			try {
				gsm.setPlayer(player);
				gsm.setState(GameStateManager.GAMEOVER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}