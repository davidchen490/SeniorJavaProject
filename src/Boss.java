import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

public class Boss extends Entity {
	
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWNATTACK = 4;
	private final int LEFTATTACK = 5;
	private final int RIGHTATTACK = 6;
	private final int UPATTACK = 7;
	
	private int damageTaken = 0;
	private Player player;
	private boolean active = true;
	private boolean charging = false;
	private boolean resting = false;
	private double chargingAngle;

	private BufferedImage[] spriteUp;
	private BufferedImage[] spriteDown;
	private BufferedImage[] spriteUpAttack;
	private BufferedImage[] spriteDownAttack;
	private BufferedImage[] spriteLeftAttack;
	private BufferedImage[] spriteRightAttack;
	private BufferedImage[] fireballUp;
	private BufferedImage[] fireballDown;
	private BufferedImage[] fireballLeft;
	private BufferedImage[] fireballRight;
	private int difficulty;
	
	
	public Boss(TileMap tm, Player _player, int difficulty) {
		super(tm);
		player = _player;
		width = 128;
		height = 128;
		
		totalHealth = 50 + 50 * difficulty;
		currentHealth = totalHealth;
		
		contactDamage = 3 + difficulty;
		
		this.difficulty = difficulty;
		
		moveSpeed = (2- difficulty) / 2 + 2;
		
		resting = false;
		
		spriteUp = Content.BOSS[0];
		spriteDown = Content.BOSS[1];
		spriteUp = Content.BOSS[2];
		spriteDown = Content.BOSS[3];
		spriteLeftAttack = Content.BOSS[4];
		spriteRightAttack = Content.BOSS[5];
		fireballUp = Content.BOSS[6];
		fireballDown = Content.BOSS[7];
		fireballLeft = Content.BOSS[8];
		fireballRight = Content.BOSS[9];
		
		animation = new Animation();
		animation.setFrames(spriteDown);
		animation.setDelay(5);
		animation.setNumFrames(2);
	}
	
	public void setChargingAngle(double angle) {
		chargingAngle = angle;
	}
	
	public Player getplayer() {
		return player;
	}
	
	public boolean getRestingState() {
		return resting;
	}
	
	public void takeDamage(int i) {
		super.takeDamage(i);
		damageTaken += i;
	}
	
	public void setCharging(boolean b) {
		charging = b;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setTilePosition(int i1, int i2) {
		super.setTilePosition(i1, i2);
		System.out.println(i2 + ", " + i1);
	}
	
	public int getx() {
		return super.getx();
	}
	
	public int gety() {
		return super.gety();
	}
	
	public void setMoveSpeed(int i) {
		moveSpeed = i;
	}
	
	public int getDamageTaken() {
		return damageTaken;
	}
	
	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);

		animation.setDelay(d);
	}
	
	public void charge(double angle) {
		double disX = (moveSpeed * Math.cos(angle));
		double disY = (moveSpeed * Math.sin(angle));
		x += disX;
		y += disY;
	}
	
	public void setUp() {
		super.setUp();
	}
	public void setDown() {
		super.setDown();
	}
	
	public void setLeft() {
		super.setLeft();
	}
	
	public void setRight() {
		super.setRight();
	}
	
	public void update() {
		if (charging) {
			charge(chargingAngle);
		}
		if (getActive()) {
			animation.update();
			super.update();
		}
		if(down) {
			if(currentAnimation != DOWN) {
				setAnimation(DOWN, spriteDown, 5);
			}
		}
		if(up) {
			if (currentAnimation != UP) {
				setAnimation(UP, spriteUp, 5);
			}
		}
	}
	
	public boolean getActive() {
		if (x < xmap || x > -xmap + tileMap.getTileSize() * 16 || y < -ymap || y > -ymap + tileMap.getTileSize() * 16 || !active)
			return false;
		else 
			return true;
	}
	
	public void draw(Graphics2D g) {
		//draw enemy
		//super.draw(g);
		setMapPosition();
		
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height /2, null);
		
		//draw bullets
		for (Bullet bullet : bullets) {
			bullet.draw(g);
		}
		
	}

}
