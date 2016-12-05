// The only subclass the fully utilizes the
// Entity superclass (no other class requires
// movement in a tile based map).
// Contains all the gameplay associated with
// the 

//package com.neet.DiamondHunter.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

//import com.neet.DiamondHunter.Manager.Content;
//import com.neet.DiamondHunter.Manager.JukeBox;
//import com.neet.DiamondHunter.TileMap.TileMap;

public class Player extends Entity {
	
	// sprites
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	private BufferedImage[] upSprites;
	private BufferedImage downStop;
	private BufferedImage leftStop;
	private BufferedImage rightStop;
	private BufferedImage upStop;
	
	// animation
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DOWNATTACK = 4;
	private final int LEFTATTACK = 5;
	private final int RIGHTATTACK = 6;
	private final int UPATTACK = 7;
	
	// gameplay
	//these should have a cap or max val
	private ArrayList<Enemy> enemies;
	private ArrayList<Item> items;
	private ArrayList<SeekingBullet> seekingBullets;
	private ArrayList<Bullet> bullets;
	private ArrayList<Thread> enemyThreads;
	private ArrayList<Item> masterItems;
	private ArrayList<PowerUP> powerups;
	private Item consumableItem;
	private int totalPower;
	private int totalSpeed;
	private int difficulty;

	private boolean hasRubberStrip; // projectiles will be deflected off sword or will cancel shots
	private boolean hasStrengthEnhancer; // 150% damage increase, at least 1 damage increase guaranteed
	private boolean hasCloakOfAgility; // 50% chance of dodging all damage
	private boolean hasWheelOfTime; // one time use of stopping time for 5 seconds.
	private boolean hasAnesthesia; // resurrect, but the timer increases by some amount (long time)
	private boolean hasFourLeafClover; // increased power ups and better chest drops
	private boolean hasNiceSneakers; // increased movement speed
	private boolean hasSpiderLeg; // 150% damage to just spiders
	private boolean hasMetalDetector; // after each room have chance to get items
	private boolean hasHeatedRubber; // after use, projectiles will be deflected off sword and will heat seek for enemies
	private boolean hasRechargingShield; // blocks one hit, recharges every two cleared rooms
	private boolean hasMartyrdomDeathCharm; // in death, explode gloriously and respawn with permanently one HP
	private boolean hasBandage; // use to gain health points depending on difficulty
	
	private boolean usedHeatedRubber; // tracks if player has HeatedRubber buff
	private long heatedRubberEndTime;
	private int heatedRubberDuration = 7000;
	
	private boolean stopTime;
	private int stopTimeDuration = 2000; 
	private long endStopTime;
	
	private boolean usedMartyrdomDeathCharm;
	private boolean usedAnesthesia;
	
	private int cloakRollToBeat = 75; // you must roll over 75 randomly to avoid damage
	
	private int numGold;
	private int totalGold;
	private long ticks;
	
	private int shieldCharge = 0;

	//attacking things
	
	private boolean attacking = false;
	private boolean attackingUp = false;
	private boolean attackingDown = false;
	private boolean attackingLeft = false;
	private boolean attackingRight = false;
	private boolean initiateAttack = false;
	
	private int baseDamage;
	private int modifiedDamage;
	private int ATTACK_DURATION = 200;
	private int ATTACK_DELAY = 200;
	private long attackStart;
	private long attackEnd;
	private long canAttackAgain;
	private Rectangle attackBound;
	private Random r;
	private long checkDamageTime;
	
	private BufferedImage straw;
	private BufferedImage upStraw;
	private BufferedImage upStraw1;
	private BufferedImage downStraw;
	private BufferedImage downStraw1;
	private BufferedImage leftStraw;
	private BufferedImage leftStraw1;
	private BufferedImage rightStraw;
	private double percent;
	
	
	
	public Player(TileMap tm, ArrayList<Enemy> enemies, int difficulty) {
		
		//initalize player
		super(tm);
		
		this.difficulty = difficulty;
		
		this.enemies = enemies; 
		
		seekingBullets = new ArrayList<SeekingBullet>();
		bullets = new ArrayList<Bullet>();
		items = new ArrayList<Item>();
		
		attackBound = new Rectangle();
		r = new Random();
		
		consumableItem = new Item(tileMap);
		consumableItem.setType(-1);
		
		
		//Player dimensions
		
		width = 25;
		height = 35;
		cwidth = 25;
		cheight = 35;
		
		//Player movement speed
		moveSpeed = 3;
		
		//Player damage
		baseDamage = 2;
		modifiedDamage = baseDamage;		
		
		//Player default values
		if (difficulty == 0)
			totalHealth = 20;
		else if (difficulty == 1)
			totalHealth = 15;
		else
			totalHealth = 10;
		
		currentHealth = totalHealth;
		
		straw = toBufferedImage(Content.STRAW[0][0].getScaledInstance(2, 64 , Image.SCALE_SMOOTH));
		
		//Player Animations
		
		downSprites = Content.PLAYER[0];
		leftSprites = Content.PLAYER[1];
		rightSprites = Content.PLAYER[2];
		upSprites = Content.PLAYER[3];		
		
		animation.setFrames(downSprites);
		animation.setDelay(2);
		
		Jukebox.load("Resources/SFX/woosh.wav", "woosh");
		Jukebox.setVolume("woosh", -5);
	}
	public void setEnemies(ArrayList<Enemy> _enemies) {
		enemies = _enemies;
	}
	
	public void setMasterItems(ArrayList<Item> items) {
		masterItems = items;
	}
	
	public void setMasterPowerups(ArrayList<PowerUP> powerups) {
		this.powerups = powerups;
	}
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);

		animation.setDelay(d);
	}
	
	public void collectedGold() { numGold++; }
	
	public int numGold() { return numGold; }
	
	public int getTotalGold() { return totalGold; }
	
	public void setTotalGold(int i) { totalGold = i; }
	
	public void collectedHealth(){ currentHealth += (2-difficulty) + 1; if (currentHealth > totalHealth) currentHealth = totalHealth; }//Health will be gained from enemies> particle
	
	public void addToInventory(int i, boolean consumable) {
		if (consumable) {
			consumableItem = new Item(tileMap);
			consumableItem.setType(i);
		}
		else {
			if (hasNiceSneakers()) {
				moveSpeed = 4;
			}
			Item item = new Item(tileMap);
			item.setType(i);
			items.add(item);
		}
	}
	
	public ArrayList<Item> getInventory() {
		return items;
	}
	
	public boolean hasRubberStrip() {
		return hasRubberStrip;
	}

	public void setHasRubberStrip(boolean hasRubberStrip) {
		this.hasRubberStrip = hasRubberStrip;
	}

	public boolean hasStrengthEnhancer() {
		return hasStrengthEnhancer;
	}

	public void setHasStrengthEnhancer(boolean hasStrengthEnhancer) {
		this.hasStrengthEnhancer = hasStrengthEnhancer;
	}

	public boolean hasCloakOfAgility() {
		return hasCloakOfAgility;
	}

	public void setHasCloakOfAgility(boolean hasCloakOfAgility) {
		this.hasCloakOfAgility = hasCloakOfAgility;
	}

	public boolean hasWheelOfTime() {
		return hasWheelOfTime;
	}

	public void setHasWheelOfTime(boolean hasWheelOfTime) {
		this.hasWheelOfTime = hasWheelOfTime;
	}

	public boolean hasAnesthesia() {
		return hasAnesthesia;
	}

	public void setHasAnesthesia(boolean hasAnesthesia) {
		this.hasAnesthesia = hasAnesthesia;
	}

	public boolean hasFourLeafClover() {
		return hasFourLeafClover;
	}

	public void setHasFourLeafClover(boolean hasFourLeafClover) {
		this.hasFourLeafClover = hasFourLeafClover;
	}

	public boolean hasNiceSneakers() {
		return hasNiceSneakers;
	}

	public void setHasNiceSneakers(boolean hasNiceSneakers) {
		this.hasNiceSneakers = hasNiceSneakers;
	}

	public boolean hasSpiderLeg() {
		return hasSpiderLeg;
	}

	public void setHasSpiderLeg(boolean hasSpiderLeg) {
		this.hasSpiderLeg = hasSpiderLeg;
	}

	public boolean hasMetalDetector() {
		return hasMetalDetector;
	}

	public void setHasMetalDetector(boolean hasMetalDetector) {
		this.hasMetalDetector = hasMetalDetector;
	}

	public boolean hasHeatedRubber() {
		return hasHeatedRubber;
	}

	public void setHasHeatedRubber(boolean hasHeatedRubber) {
		this.hasHeatedRubber = hasHeatedRubber;
	}

	public boolean hasRechargingShield() {
		return hasRechargingShield;
	}

	public void setHasRechargingShield(boolean hasRechargingShield) {
		this.hasRechargingShield = hasRechargingShield;
	}

	public boolean hasMartyrdomDeathCharm() {
		return hasMartyrdomDeathCharm;
	}

	public void setHasMartyrdomDeathCharm(boolean hasMartyrdomDeathCharm) {
		this.hasMartyrdomDeathCharm = hasMartyrdomDeathCharm;
	}

	public boolean hasBandage() {
		return hasBandage;
	}

	public void setHasBandage(boolean hasBandage) {
		this.hasBandage = hasBandage;
	}	
	
	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public boolean isAttackingUp() {
		return attackingUp;
	}

	public void setAttackingUp(boolean attackingUp) {
		this.attackingUp = attackingUp;
	}

	public boolean isAttackingDown() {
		return attackingDown;
	}

	public void setAttackingDown(boolean attackingDown) {
		this.attackingDown = attackingDown;
	}

	public boolean isAttackingLeft() {
		return attackingLeft;
	}

	public void setAttackingLeft(boolean attackingLeft) {
		this.attackingLeft = attackingLeft;
	}

	public boolean isAttackingRight() {
		return attackingRight;
	}

	public void setAttackingRight(boolean attackingRight) {
		this.attackingRight = attackingRight;
	}
	
	public Rectangle getAttackBound() {
		return attackBound;
	}
	
	// Used to update time.
	public long getTicks() { return ticks; }
	
	// Keyboard input. Moves the 
	
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
	
	public void setEnemyThreads(ArrayList<Thread> threads) {
		enemyThreads = threads;
	}
	
	@SuppressWarnings("deprecation")
	public void setAction() {
		super.setAction();
		if (hasBandage) {
			if (getHealth() == getTotalHealth()) {
				System.out.println("Already Full Health");
			}
			else {
				currentHealth += 4 * (3-difficulty);
				if (currentHealth > totalHealth)
					currentHealth = totalHealth;
				System.out.println("Healed" + 3 * (3-difficulty) + " health.");
				setHasBandage(false);
				consumableItem = new Item(tileMap);
				consumableItem.setType(-1);
			}
		}
		if (hasHeatedRubber) {
			if (usedHeatedRubber) {
				System.out.println("You already have the buff.");
			}
			else {
				System.out.println("You have used the burned rubber");
				usedHeatedRubber = true;
				setHasHeatedRubber(false);
				heatedRubberEndTime = System.currentTimeMillis() + ((3 - difficulty) * heatedRubberDuration);
				recalculateModifiedDamage();
				consumableItem = new Item(tileMap);
				consumableItem.setType(-1);
			}
			
		}		
		if (hasWheelOfTime) {
			stopTime = true;
			setHasWheelOfTime(false);
			for (Thread enemy : enemyThreads) {
				enemy.suspend();
			}
			System.out.println("Stopped Time");
			endStopTime = ((3 - difficulty) * 1000 + stopTimeDuration) + System.currentTimeMillis();
			consumableItem = new Item(tileMap);
			consumableItem.setType(-1);
		}
	}
	
	public Item getConsumable() {
		return consumableItem;
	}
	
	public void recalculateModifiedDamage() {
		modifiedDamage = baseDamage;
		if (usedHeatedRubber) {
			modifiedDamage += 2;
		}
		
		modifiedDamage += totalPower;
		
		if (hasStrengthEnhancer) {
			if ((modifiedDamage * 1.5) - modifiedDamage > 0)
				modifiedDamage *= 1.5;
			else
				modifiedDamage += 1;
		}
	}
	
	public void attackingUp() {
		if (attacking == false) {
			attacking = true;
			attackingUp = true;
			initiateAttack = true;
			Jukebox.play("woosh");
		}
	}
	
	public void attackingDown() {
		if (attacking == false) {
			attacking = true;
			attackingDown = true;
			initiateAttack = true;
			Jukebox.play("woosh");
		}
	}
	
	public void attackingLeft() {
		if (attacking == false) {
			attacking = true;
			attackingLeft = true;
			initiateAttack = true;
			Jukebox.play("woosh");
		}
	}
	
	public void attackingRight() {
		if (attacking == false) {
			attacking = true;
			attackingRight = true;
			initiateAttack = true;
			Jukebox.play("woosh");
		}
	}	
	
	public void clearedRoom() {
		if (hasRechargingShield) {
			shieldCharge ++;
		}
	}
	
	public int getDifficulty() {
		return difficulty;
	}
		
	public void update() {
		
		ticks++;
		
		if (stopTime && endStopTime < System.currentTimeMillis()) {
			stopTime = false;
			for (Thread enemy : enemyThreads)
				enemy.resume();
		}
		
		//check heated rubber duration
		if (usedHeatedRubber) {
			if (System.currentTimeMillis() > heatedRubberEndTime && System.currentTimeMillis() > checkDamageTime) {
				usedHeatedRubber = false;
				recalculateModifiedDamage();
			}
			else if (System.currentTimeMillis() < heatedRubberEndTime && System.currentTimeMillis() > checkDamageTime) {
				checkDamageTime += 1000;
			}
		}
		
		/**
		 * Need to check if player has collided with a
		 * mob
		 * powerup
		 */
		
		//check collide with mob
		
		
		//check attacking
		if (attacking) {
			if (System.currentTimeMillis() > canAttackAgain && initiateAttack) {
				attackStart = System.currentTimeMillis();
				attackEnd = System.currentTimeMillis() + ATTACK_DURATION;
				canAttackAgain = attackEnd + ATTACK_DELAY;
				initiateAttack = false;
			}
			else if (System.currentTimeMillis() > attackEnd && !initiateAttack) {
				attacking = false;
				attackingUp = false;
				attackingDown = false;
				attackingLeft = false;
				attackingRight = false;
			}
			else if (System.currentTimeMillis() < attackEnd) {
				if (attackingUp) {
					attackBound.setBounds(xmap + getx() - (int)(getwidth() * 2.5) , ymap + gety() - getheight() - getheight(),
								5*getwidth(), getheight() + getheight());
						
					percent = (double)(System.currentTimeMillis() - attackStart) / (double)ATTACK_DURATION; // ATTACK_DURATION;
						
					if (percent <= .5) {
						double rotationRequired = Math.toRadians (180 * (.5 - percent));
						double locationX = straw.getWidth() / 2;
						double locationY = straw.getHeight();
						AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
						AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						upStraw = op.filter(straw, null);
						
						tx = AffineTransform.getScaleInstance(-1, 1);
						tx.translate(-64, 0);//newStraw.getWidth(null), 0);
						op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						upStraw1 = op.filter(upStraw, null);
					}
					else {
						double rotationRequired = Math.toRadians (180 * (percent - .5));
						double locationX = straw.getWidth() / 2;
						double locationY = straw.getHeight();
						AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
						AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						upStraw1 = op.filter(straw, null);
					}
				}
				else if (attackingDown) {
					attackBound.setBounds(xmap + getx() - (int)(getwidth()*2.5), ymap + gety(),
							5*getwidth(), getheight() + getheight());
					percent = (double)(System.currentTimeMillis() - attackStart) / (double)ATTACK_DURATION; // ATTACK_DURATION;
					
					if (percent <= .5) {
						double rotationRequired = Math.toRadians (180 * (.5 - percent));
						double locationX = straw.getWidth() / 2;
						double locationY = straw.getHeight();
						AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
						AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						downStraw = op.filter(straw, null);
						
						tx = AffineTransform.getScaleInstance(-1, -1);
						tx.translate(-64, -64);//newStraw.getWidth(null), 0);
						op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						downStraw1 = op.filter(downStraw, null);
					}
					else {
						double rotationRequired = Math.toRadians (180 * (percent - .5));
						double locationX = straw.getWidth() / 2;
						double locationY = straw.getHeight();
						AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
						AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						downStraw = op.filter(straw, null);
						
						tx = AffineTransform.getScaleInstance(1, -1);
						tx.translate(0, -64);//newStraw.getWidth(null), 0);
						op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
						downStraw1 = op.filter(downStraw, null);
					}
				}
				else if (attackingLeft) {
					attackBound.setBounds(xmap + getx() - getwidth() - (int)(1.5*getwidth()) - 5, ymap + gety() - (int)(getheight()*2),
							getwidth() + (int)(1.5*getwidth()) + 5, (int)(4*getheight()));
					
					percent = (double)(System.currentTimeMillis() - attackStart) / (double)ATTACK_DURATION; // ATTACK_DURATION;
					double rotationRequired = Math.toRadians (180 * percent);
					double locationX = straw.getWidth() / 2;
					double locationY = straw.getHeight();
					AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					leftStraw = op.filter(straw, null);
					
					tx = AffineTransform.getScaleInstance(-1, 1);
					tx.translate(-64, 0);//newStraw.getWidth(null), 0);
					op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					leftStraw1 = op.filter(leftStraw, null);
				}
				else if (attackingRight) {
					attackBound.setBounds(xmap + getx(), ymap + gety() - (int)(2*getheight()),
							getwidth() + (int)(1.5*getwidth()) + 5, (int)(4*getheight()));
					
					percent = (double)(System.currentTimeMillis() - attackStart) / (double)ATTACK_DURATION; // ATTACK_DURATION;
					double rotationRequired = Math.toRadians (180 * percent);
					double locationX = straw.getWidth() / 2;
					double locationY = straw.getHeight();
					AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					rightStraw = op.filter(straw, null);								 
				}	
			}
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			try {
				if (this.intersects(enemies.get(i))) {
					if (hasCloakOfAgility) {
						int cloakRoll = r.nextInt(100) + 1;
						if (cloakRoll < cloakRollToBeat)
							if (shieldCharge < 2) {
								takeDamage(enemies.get(i).getDamage());
							}
							else {
								shieldCharge -= 2;
							}
					}
					else {						
						takeDamage(enemies.get(i).getDamage());
					}						
				}
			ArrayList<Bullet> bullets = enemies.get(i).getBullets();
				
				for (int b = 0; b < bullets.size(); b++) {
					if (this.intersects(bullets.get(b).getRectangle())) {
						if (hasCloakOfAgility) {
							int cloakRoll = r.nextInt(100);
							if (cloakRoll < cloakRollToBeat) {
								takeDamage(1);
								bullets.remove(b);
							}
						}
						else {
							takeDamage(1);
							bullets.remove(b);
						}									
					}
					if (!usedHeatedRubber && !hasRubberStrip && attacking) {
						if (bullets.get(b).getRectangle().intersects(attackBound)) {
							bullets.remove(b);
						}
					}
					else if (usedHeatedRubber && attacking) {
						SeekingBullet newBullet = null;
						Bullet bullet = bullets.get(b);
						if (bullet.getRectangle().intersects(attackBound)) {
							if (attackingUp || attackingDown) {
								newBullet = new SeekingBullet(360-bullet.getAngle(), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							else if (attackingLeft) {
								newBullet = new SeekingBullet(360-(bullet.getAngle() - 180), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							else if (attackingRight) {
								newBullet = new SeekingBullet(180-bullet.getAngle(), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							seekingBullets.add(newBullet);
							int enemyNumber = r.nextInt(enemies.size());
							seekingBullets.get(seekingBullets.size()-1).setEnemy(enemies.get(enemyNumber), enemyNumber);
							enemies.get(i).getBullets().remove(b);
						}
					}
					else if (hasRubberStrip && attacking) {
						Bullet newBullet = null;
						Bullet bullet = bullets.get(b);
						if (bullet.getRectangle().intersects(attackBound)) {
							if (attackingUp || attackingDown) {
								newBullet = new Bullet(360-bullet.getAngle(), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							else if (attackingLeft) {
								newBullet = new Bullet(360-(bullet.getAngle() - 180), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							else if (attackingRight) {
								newBullet = new Bullet(180-bullet.getAngle(), 
								(int)bullet.getx(),
								(int)bullet.gety(),
								tileMap
								);
							}
							this.bullets.add(newBullet);
							enemies.get(i).getBullets().remove(b);
						}
					}					
				} 
				if (enemies.get(i).getRectangle().intersects(attackBound) && attacking) {
					enemies.get(i).takeDamage(modifiedDamage);
					if (attackingUp)
						enemies.get(i).knockBack(1.57, 50);
					if (attackingDown)
						enemies.get(i).knockBack(-1.57, 50);
					if (attackingLeft)
						enemies.get(i).knockBack(0, 50);
					if (attackingRight)
						enemies.get(i).knockBack(3.14, 50);
					if (enemies.get(i).isDead()) {
						enemies.remove(i);
						int rand = r.nextInt(100);
						if (!hasFourLeafClover()) {
							if (rand > 97) {
								Item item = new Item(tileMap);
								item.setType(Item.SPIDER_LEG);
								item.setTilePosition(enemies.get(i).getRow(), enemies.get(i).getCol());
								masterItems.add(item);
							}
							else if (rand > 92) {
								PowerUP powerup = new PowerUP(tileMap);
								powerup.setType(PowerUP.HEALTH);
								powerup.setTilePosition(enemies.get(i).getRow(), enemies.get(i).getCol());
								powerups.add(powerup);
							}
						}
						else {
							if (rand > 95) {
								Item item = new Item(tileMap);
								item.setType(Item.SPIDER_LEG);
								item.setTilePosition(enemies.get(i).getRow(), enemies.get(i).getCol());
								masterItems.add(item);
							}
							else if (rand > 85) {
								PowerUP powerup = new PowerUP(tileMap);
								powerup.setType(PowerUP.HEALTH);
								powerup.setTilePosition(enemies.get(i).getRow(), enemies.get(i).getCol());
								powerups.add(powerup);
							}
						}
					}
				}
			} catch (Exception e) {}
		}				 
		
		//check if dead
		if (isDead()) {
			if (hasMartyrdomDeathCharm && !usedMartyrdomDeathCharm) {
				usedMartyrdomDeathCharm = true;
				currentHealth = 1;
				totalHealth = 1;
				Rectangle attack = new Rectangle(tileMap.getx(), tileMap.gety(),
						tileMap.getWidth(), tileMap.getHeight());
						
				for (Enemy enemy : enemies) {
					if (attack.intersects(enemy.getRectangle()))
						enemy.takeDamage((difficulty) * 5 + 10);
				}
				hasMartyrdomDeathCharm = false;
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getType() == Item.MARTYRDOM_DEATH_CHARM) {
						items.remove(i);
					}
				}
			}
			else if (hasAnesthesia && !usedAnesthesia) {
				currentHealth = totalHealth / 2;
				usedAnesthesia = true;
				hasAnesthesia = false;
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getType() == Item.ANESTHESIA) {
						items.remove(i);
					}
				}
			}
		}
		
		try {
			if (bullets.size() > 0)
				for (int i = 0; i < bullets.size(); i++) {
					boolean thing = bullets.get(i).update();
					if (thing) {
						bullets.remove(i);
					}
				}
		} catch (Exception e) { e.printStackTrace(System.out); }
		
		try {
			if (seekingBullets.size() > 0)
				for (int i = 0; i < seekingBullets.size(); i++) {
					boolean thing = seekingBullets.get(i).update();
					if (seekingBullets.get(i).getEnemy().isDead()) {
						seekingBullets.remove(i);
					}
					else if (thing) {
						seekingBullets.remove(i);
					}			
					else if (seekingBullets.get(i).getRectangle().intersects(seekingBullets.get(i).getEnemy().getRectangle())) {
						enemies.get(seekingBullets.get(i).getEnemyNumber()).takeDamage(1);
						seekingBullets.remove(i);
					}	
				}
		} catch (Exception e) { e.printStackTrace(System.out); }	
		
		// set animation
		if(down) {
			if(currentAnimation != DOWN) {
				setAnimation(DOWN, downSprites, 5);
			}
		}
		if(left) {
			if(currentAnimation != LEFT) {
				setAnimation(LEFT, leftSprites, 5);
			}
		}
		if(right) {
			if(currentAnimation != RIGHT) {
				setAnimation(RIGHT, rightSprites, 5);
			}
		}
		if(up) {
			if (currentAnimation != UP) {
				setAnimation(UP, upSprites, 5);
			}
		}
		
		// check if collided with powerup 
		
		// update position
		super.update();
	}
	
	public void draw1(Graphics2D g) {
		/*if (attacking) {
			g.setColor(Color.blue);
			g.fill(attackBound);
			g.setColor(Color.black);
		}*/
		try {
			for (SeekingBullet bullet : seekingBullets)
				bullet.draw(g);
			for (Bullet bullet : bullets)
				bullet.draw(g);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		if (attackingUp) {			
			if (percent < .5) 
				g.drawImage(upStraw1, xmap + getx() - straw.getHeight(), (int)(ymap +gety() - straw.getHeight()), null);
			else 
				g.drawImage(upStraw1, xmap + getx(), (int)(ymap + gety() - straw.getHeight()), null); 
		}
		else if (attackingDown) {
			if (percent < .5) 
				g.drawImage(downStraw1, xmap + getx() - straw.getHeight(), (int)(ymap + gety()), null);
			else 
				g.drawImage(downStraw1, xmap + getx(), (int)(ymap + gety()), null); 
		}
		else if (attackingRight) {
			g.drawImage(rightStraw, xmap + getx(), (int)ymap + gety() - straw.getHeight(), null);
		}
		else if (attackingLeft) {			
			g.drawImage(leftStraw1, (int)xmap + getx() - straw.getHeight(), (int)ymap + gety() - straw.getHeight(), null);
		}
		g.setColor(Color.black);
		
		setMapPosition();		
		
		if (xmap == 0 && ymap == 0)
			g.drawImage((Image)animation.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH), x - width / 2,
					y - height / 2, null
					);
		else if (xmap == 0)
			g.drawImage((Image)animation.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH), x - width / 2,
				256 - height / 2, null
				);
		else if (ymap == 0)
			g.drawImage((Image)animation.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH), 256 - width / 2,
					y - height / 2, null
					);
		else
			g.drawImage((Image)animation.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH), 256 - width / 2,
					256 - height / 2, null
					);
	}
	
	// Draw 
	public void draw(Graphics2D g) {
		/*if (attacking) {
			g.setColor(Color.blue);
			g.fill(attackBound);
			g.setColor(Color.black);
		}*/
		try {
			for (SeekingBullet bullet : seekingBullets)
				bullet.draw(g);
			for (Bullet bullet : bullets)
				bullet.draw(g);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		if (attackingUp) {			
			if (percent < .5) 
				g.drawImage(upStraw1, xmap + getx() - straw.getHeight(), (int)(ymap +gety() - straw.getHeight()), null);
			else 
				g.drawImage(upStraw1, xmap + getx(), (int)(ymap + gety() - straw.getHeight()), null); 
		}
		else if (attackingDown) {
			if (percent < .5) 
				g.drawImage(downStraw1, xmap + getx() - straw.getHeight(), (int)(ymap + gety()), null);
			else 
				g.drawImage(downStraw1, xmap + getx(), (int)(ymap + gety()), null); 
		}
		else if (attackingRight) {
			g.drawImage(rightStraw, xmap + getx(), (int)ymap + gety() - straw.getHeight(), null);
		}
		else if (attackingLeft) {			
			g.drawImage(leftStraw1, (int)xmap + getx() - straw.getHeight(), (int)ymap + gety() - straw.getHeight(), null);
		}
		g.setColor(Color.black);
		super.draw(g);
	}

	
}