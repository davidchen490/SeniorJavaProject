import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;


public class Enemy extends Entity {
/**
 * Set stats: health, speed, power
 * Work with the AI and Bullet class to fire when the mob is stationary for 3 seconds
 * At the point of firing the bullet should travel linearly toward the player and not track
 * Radius of sensing player? -> Probably in the AI
 *
 */
	protected boolean resting;
	protected Player player;
	
	// sprites
	protected BufferedImage[] image;
	
	// animation
	protected boolean active = true;
	// bullets
	protected ArrayList<Bullet> bullets;
	

	
	public Enemy(TileMap tm, Player _player){
		super(tm);
		bullets = new ArrayList<Bullet>();
		player = _player;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;
		
		moveSpeed = 2;
		
		resting = false;
		
		image = Content.SPIDER[0];
		
		/**
		 * Different mobs have different:
		 * Dimensions for collision purposes -> mob type
		 * Movement and attack patterns -> different AI's?
		 */		
		
		animation.setFrames(image);
		animation.setDelay(4);
		
		totalHealth = 5 * ((player.getDifficulty() * 2) + 1);
		currentHealth = totalHealth;
		contactDamage = (int) Math.ceil((player.getDifficulty() + 1) / 2);
		
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public void setAction() {
		super.setAction();
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
	
	public boolean getRestingState() {
		return resting;
	}
	
	public void setRestingState(boolean _resting) {
		resting = _resting;
	}
	
	public Player getplayer() {
		return player;
	}
	
	public TileMap getTileMap() {
		return tileMap;
	}
	
	public boolean getActive() {
		if (x < xmap || x > -xmap + tileMap.getTileSize() * 16 || y < -ymap || y > -ymap + tileMap.getTileSize() * 16 || !active)
			return false;
		else 
			return true;
	}
	
	public void setActive(boolean b) {
		active = b;
	}
	
	public void update() {
		if (getActive()) {
			animation.update();
			super.update();
			
			try {
				for (int i = 0; i < bullets.size(); i++) {
					boolean thing = bullets.get(i).update();
					if (thing) {
						bullets.remove(i);
					}
				}
			} catch (Exception e) {
				
			}
		}	
	}
	
	public void draw(Graphics2D g) {
		//draw enemy
		//super.draw(g);
		setMapPosition();
		
		g.drawImage(animation.getImage(), x + xmap - width / 2, y + ymap - height /2, null);
		
		//draw bullets
		try {
			for (Bullet bullet : bullets)
				bullet.draw(g);
		} catch (ConcurrentModificationException e) {
			
		}	
		
	}
}
