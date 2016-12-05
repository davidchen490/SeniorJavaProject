// The game object superclass.
// This class has all the logic required
// to move around a tile based map.

//package com.neet.DiamondHunter.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;


public abstract class Entity {
	
	// dimensions
	protected int width;
	protected int height;
	protected int cwidth;
	protected int cheight;
	
	// position
	protected int x;
	protected int y;
	protected int dx;
	protected int dy;
	
	protected int xdest;
	protected int ydest;
	protected int rowTile;
	protected int colTile;
	
	// movement
	protected boolean moving;
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	
	private int previousDirection;
	
	private int UP = 0;
	private int DOWN = 1;
	private int LEFT = 2;
	private int RIGHT = 3;
	
	protected boolean movingLeft;
	protected boolean movingRight;
	protected boolean movingUp;
	protected boolean movingDown;
	
	// attributes
	protected int moveSpeed;
	
	// tilemap
	protected TileMap tileMap;
	protected int tileSize;
	protected int xmap;
	protected int ymap;
	
	// bullets
	protected ArrayList<Bullet> bullets;
	
	// animation
	protected Animation animation;
	protected int currentAnimation;
	
	//health
	protected int currentHealth;
	protected int totalHealth;
	protected int contactDamage;
	protected boolean dead;
	
	//knockback timers
	protected int KB_DURATION = 250;
	protected long startKb = System.currentTimeMillis();
	protected long endKb = System.currentTimeMillis();
	
	//invincibility times
	private int INVINC_DURATION = 250;
	private long startInvinc = System.currentTimeMillis();
	private long endInvinc = System.currentTimeMillis() + INVINC_DURATION;
	
	public Entity(TileMap tm) {
		tileMap = tm;
		tileSize = tileMap.getTileSize();
		animation = new Animation();
		bullets = new ArrayList<Bullet>();
	}
	
	public int getx() { return x; }
	public int gety() { return y; }
	public int getwidth() { return width; }
	public int getheight() { return height; }
	public int getRow() { return rowTile; }
	public int getCol() { return colTile; }
	public int getHealth() { return currentHealth; }
	public int getTotalHealth() { return totalHealth; }
	
	public void setPosition(int i1, int i2) {
		x = i1;
		y = i2;
		xdest = x;
		ydest = y;
	}
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	public void setTilePosition(int i1, int i2) {
		y = i1 * tileSize + tileSize / 2;
		x = i2 * tileSize + tileSize / 2;
		xdest = x;
		ydest = y;
	}

	public void setLeft() {
		if(moving) return;
		left = true;
		moving = validateNextPosition();
	}
	public void setRight() {
		if(moving) return;
		right = true;
		moving = validateNextPosition();
	}
	public void setUp() {
		if(moving) return;
		up = true;
		moving = validateNextPosition();
	}
	public void setDown() {
		if(moving) return;
		down = true;
		moving = validateNextPosition();
	}
	
	public boolean intersects(Entity o) {
		return getRectangle().intersects(o.getRectangle());
	}
	
	public boolean intersects(Rectangle o) {
		return getRectangle().intersects(o);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x + xmap - width / 2,
				y + ymap - height / 2, width, height);
	}
	
	public boolean isDead(){ return dead; }
	
	public int getDamage(){ return contactDamage; }
	
	public void takeDamage(int damage) {
		if(dead) return;
		if (System.currentTimeMillis() > endInvinc) {
			currentHealth -= damage;
			startInvinc = System.currentTimeMillis();
			endInvinc = System.currentTimeMillis() + INVINC_DURATION;			
		}
		if(currentHealth < 0) currentHealth = 0;
		if(currentHealth == 0) dead = true;
	}
	
	public void knockBack(double angle, int kbValue) {
		if (System.currentTimeMillis() > endKb) {
			startKb = System.currentTimeMillis();
			endKb = System.currentTimeMillis() + KB_DURATION;
			if (angle == 1.57) {
				try {
					if (tileMap.getType((int)((gety() - (width / 2) - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.BLOCKED &&
						tileMap.getType((int)((gety() - (width / 2) - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.CLOSED_DOOR &&
							x + (width / 2) < -xmap + tileMap.getTileSize() * 16 &&
							x - (width / 2) > -xmap &&
							y - (height / 2) > -ymap &&
							y + (height / 2) < -ymap + tileMap.getTileSize() * 16) {
						setPosition((int)Math.floor(getx() - Math.cos(angle) * kbValue), (int)Math.floor(gety() - Math.sin(angle) * kbValue));
					}
					else 
						knockBack(angle, kbValue / 2);
				} catch (Exception e) { }
			}
			else if (angle == -1.57) {
				try {
					if (tileMap.getType((int)((gety() + (width / 2) - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.BLOCKED &&
					tileMap.getType((int)((gety() + (width / 2) - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.CLOSED_DOOR &&
							x + (width / 2) < -xmap + tileMap.getTileSize() * 16 &&
							x - (width / 2) > -xmap &&
							y - (height / 2) > -ymap &&
							y + (height / 2) < -ymap + tileMap.getTileSize() * 16) {
						setPosition((int)Math.floor(getx() - Math.cos(angle) * kbValue), (int)Math.floor(gety() - Math.sin(angle) * kbValue));
					}
					else 
						knockBack(angle, kbValue / 2);
				} catch (Exception e) { }
			}
			else if (angle == 0) {
				try {
					if (tileMap.getType((int)((gety() - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (width / 2) - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.BLOCKED &&
						tileMap.getType((int)((gety() - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() - (width / 2) - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.CLOSED_DOOR &&
							x + (width / 2) < -xmap + tileMap.getTileSize() * 16 &&
							x - (width / 2) > -xmap &&
							y - (height / 2) > -ymap &&
							y + (height / 2) < -ymap + tileMap.getTileSize() * 16) {
						setPosition((int)Math.floor(getx() - Math.cos(angle) * kbValue), (int)Math.floor(gety() - Math.sin(angle) * kbValue));
					}
					else 
						knockBack(angle, kbValue / 2);
				} catch (Exception e) { }
			}
			else if (angle == 3.14) {
				try {
					if (tileMap.getType((int)((gety() - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() + (width / 2) - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.BLOCKED &&
						tileMap.getType((int)((gety() - (Math.sin(angle) * kbValue)) / tileMap.getTileSize()), 
							(int)((getx() + (width / 2) - (Math.cos(angle) * kbValue)) / tileMap.getTileSize())) != Tile.CLOSED_DOOR &&
							x + (width / 2) < -xmap + tileMap.getTileSize() * 16 &&
							x - (width / 2) > -xmap &&
							y - (height / 2) > -ymap &&
							y + (height / 2) < -ymap + tileMap.getTileSize() * 16) {
						setPosition((int)Math.floor(getx() - Math.cos(angle) * kbValue), (int)Math.floor(gety() - Math.sin(angle) * kbValue));
					}
					else 
						knockBack(angle, kbValue / 2);
				} catch (Exception e) { }
			}

		}
		
	}
	
	public void setAction() {
	}
	
	// Returns whether or not the entity can
	// move into the next position.
	
	public boolean[][] checkAroundMe() {
		boolean check[][] = new boolean[3][3];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				check[row][col] = (tileMap.getType((int)Math.ceil((y - (height / 2) + (height / 2 * row)) / tileSize), (int)(x  - (width / 2) + (width / 2 * col)) / tileSize) == Tile.BLOCKED ||
						tileMap.getType((int)Math.ceil((y - (height / 2) + (height / 2 * row)) / tileSize), (int)(x  - (width / 2) + (width / 2 * col)) / tileSize) == Tile.CLOSED_DOOR);
			}
		}
		return check;
	}
		
	public boolean validateNextPosition() {
		boolean[][] checks = new boolean [3] [3];
		if (getClass().getName().equalsIgnoreCase("Player"))
			checks = checkAroundMe();
		boolean canReturn = true;
		if(moving && canReturn) return true;
		
		rowTile = y / tileSize;
		colTile = x / tileSize;
		
		setMapPosition();
		if (getClass().getName().equalsIgnoreCase("Player")) {
			if (previousDirection == UP) {
				if (checks[0][0] == true || checks[0][1] == true || checks[0][2] == true) {
					setPosition(getx(), (int)Math.ceil((gety() + (height / 2)) / tileSize * 32) - 32);
					xdest = x;
					ydest = y;
				}
			}
			else if (previousDirection == DOWN) {
					if (checks[2][0] == true || checks[2][1] == true || checks[2][2] == true) {
						setPosition(getx(), (int)Math.ceil(gety() + (height / 2)) / tileSize * 32 - 1);
						xdest = x;
						ydest = y;
					}
			}
			else if (previousDirection == LEFT) {
					if (checks[0][0] == true || checks[1][0] == true || checks[2][0] == true) {
						setPosition((int)Math.ceil(getx() + (width / 2)) / tileSize * 32 + 1, gety());
						xdest = x;
						ydest = y;
					}
			}
			else if (previousDirection == RIGHT) {
					if (checks[0][2] == true || checks[1][2] == true || checks[2][2] == true) {
						setPosition((int)Math.ceil(getx() + (width / 2)) / tileSize * 32 - 1, gety());
						xdest = x;
						ydest = y;
					}
					
			}
			
			if (checks[0][1] == true) {
				setPosition(getx(), (int)Math.ceil(gety() + (height / 2)) / tileSize * 32 + (height / 2));
				xdest = x;
				ydest = y;
			}
			else if (checks[2][1] == true) {
				setPosition(getx(), (int)Math.ceil(gety() + (height / 2)) / tileSize * 32 - (height / 2) - 2);
				xdest = x;
				ydest = y;
			}
			else if (checks[1][0] == true) {
				setPosition((int)Math.ceil(getx() + (width / 2)) / tileSize * 32 + (width / 2) + 2, gety());
				xdest = x;
				ydest = y;
			}
			else if (checks[1][2] == true) {
				setPosition((int)Math.ceil(getx() + (width / 2)) / tileSize * 32 - (width / 2) - 2, gety());
				xdest = x;
				ydest = y;
			}
		}
		

		
		if(left) {
			if(colTile == 0 || tileMap.getType((y - (height / 2)) / tileSize, (x - (width / 2) - moveSpeed) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y + (height / 2)) / tileSize, (x - (width / 2) - moveSpeed) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y - (height / 2)) / tileSize, (x - (width / 2) - moveSpeed) / tileSize) == Tile.CLOSED_DOOR
					|| tileMap.getType((y + (height / 2)) / tileSize, (x - (width / 2) - moveSpeed) / tileSize) == Tile.CLOSED_DOOR) {
				return false;
			}
			else {
				previousDirection = LEFT;
				xdest = x - moveSpeed;
			}
		}
		if(right) {
			if(colTile == 0 || tileMap.getType((y - (height / 2)) / tileSize, (x + width/2 + moveSpeed) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y + (height / 2)) / tileSize, (x + width/2 + moveSpeed) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y - (height / 2)) / tileSize, (x + width/2 + moveSpeed) / tileSize) == Tile.CLOSED_DOOR
					|| tileMap.getType((y + (height / 2)) / tileSize, (x + width/2 + moveSpeed) / tileSize) == Tile.CLOSED_DOOR) {
				return false;
			}
			else {
				previousDirection = RIGHT;
				xdest = x + moveSpeed;
			}
		}
		if(up) {
			if(rowTile == 0 || tileMap.getType((y - (height / 2) - moveSpeed) / tileSize, (x - (width / 2)) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y - (height / 2) - moveSpeed) / tileSize, (x + (width / 2)) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y - (height / 2) - moveSpeed) / tileSize, (x - (width / 2)) / tileSize) == Tile.CLOSED_DOOR
					|| tileMap.getType((y - (height / 2) - moveSpeed) / tileSize, (x + (width / 2)) / tileSize) == Tile.CLOSED_DOOR) {
				return false;
			}
			else {
				previousDirection = UP;
				ydest = y - moveSpeed;
			}
		}
		if(down) {
			if(rowTile == tileMap.getNumRows() - 1 || tileMap.getType((y + (height / 2) + moveSpeed) / tileSize, (x - (width / 2)) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y + (height / 2) + moveSpeed) / tileSize, (x + (width / 2)) / tileSize) == Tile.BLOCKED
					|| tileMap.getType((y + (height / 2) + moveSpeed) / tileSize, (x - (width / 2)) / tileSize) == Tile.CLOSED_DOOR
					|| tileMap.getType((y + (height / 2) + moveSpeed) / tileSize, (x + (width / 2)) / tileSize) == Tile.CLOSED_DOOR) {
				return false;
			}
			else {
				previousDirection = DOWN;
				ydest = y + moveSpeed;
			}
		}
		//System.out.println(""  + colTile + " " + rowTile);
		//System.out.println(""  + x + ", " + y);
		//System.out.println(tileMap.getIndex(xmap + x / tileSize, ymap + y / tileSize));		
		return true;
		
	}
	// Calculates the destination coordinates.
	public void getNextPosition() {
		
		if(left && x > xdest) x -= moveSpeed;
		else left = false;
		if(left && x < xdest) x = xdest;
		
		if(right && x < xdest) x += moveSpeed;
		else right = false;
		if(right && x > xdest) x = xdest;
		
		if(up && y > ydest) y -= moveSpeed;
		else up = false;
		if(up && y < ydest) y = ydest;
		
		if(down && y < ydest) y += moveSpeed;
		else down = false;
		if(down && y > ydest) y = ydest;
		
	}
	
	public void update() {
		
 // get next position
		if(moving) getNextPosition();
		else { left = false; right = false; up = false; down = false; }
		/* check stop moving
		System.out.println("xdest=" + xdest + ", ydest=" + ydest);
		System.out.println("x=" + x + ", y=" + y);
		System.out.println(movingLeft);
		System.out.println(left);*/
		if((movingLeft && x <= xdest) || (movingRight && x >= xdest) || (movingUp && y <= ydest) || (movingDown && y >= ydest)) {
			movingLeft = movingRight = movingUp = movingDown = moving = false;
			rowTile = y / tileSize;
			colTile = x / tileSize;
			dx = 0;
			dy = 0;
		}

		if(left){
			dx = -moveSpeed;
			movingLeft = true;
		}
		if(right){
			dx = moveSpeed;
			movingRight = true;
		}
		if(up){
			dy = -moveSpeed;
			movingUp = true;
		}
		if(down){
			dy = moveSpeed;
			movingDown = true;
		}
		
		if (dx != 0 || dy != 0)
			move();
		
		// update animation
//		animation.update();
		
	}
	
	public void move() {
		x += dx;
		y += dy; 
		animation.update();
	}
	
	// Draws the entity.
	public void draw(Graphics2D g) {
		setMapPosition();		
		if (getClass().getName().equalsIgnoreCase("Player")) {
			g.drawImage((Image)animation.getImage().getScaledInstance(30, 45, Image.SCALE_SMOOTH), x + xmap - width / 2,
					y + ymap - height / 2, null
					);
		}
		else 
			g.drawImage((Image)animation.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH), x + xmap - width / 2,
				y + ymap - height / 2, null
				);
		g.setColor(Color.BLACK);
	}
	
}
