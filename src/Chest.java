import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Chest extends Entity{
	
	public static int DOUBLE_GOLD_CHEST = 0;
	public static int GOLD_CHEST = 1;
	public static int SILVER_CHEST = 2;
	
	int chestType;
	boolean hasLuckyCharm;
	boolean opening = false, done = false;
	
	long startChestAnimation;
	long endChestAnimation;
	private int openDelay = 2000;
	
	Random r;
	Item item;
	
	BufferedImage[][] chests;
	BufferedImage[] bigChest;
	BufferedImage[] openChests;
	
	public Chest(TileMap tm, boolean hasFourLeafClover) {
		super(tm);
		
		chests = Content.CHEST;
		bigChest = Content.BIG_CHEST[0];
		
		r = new Random();
		hasLuckyCharm = hasFourLeafClover;
		setChestType();
		Jukebox.load("Resources/SFX/chest.wav", "chest");
	}
	public void setChestType() {
		int rand;
		if (hasLuckyCharm) {
			chestType = r.nextInt(3);
		}
		else {
			rand = r.nextInt(100);
			if (rand < 50) {
				chestType = SILVER_CHEST;
				cwidth = width = 32;
				cheight = height = 32;
				animation.setFrames(chests[0]);
			}
			else if (rand >= 50 && rand < 75) {
				chestType = GOLD_CHEST;
				cwidth = width = 32;
				cheight = height = 32;
				animation.setFrames(chests[1]);
			}
			else if (rand >= 75) {
				chestType = DOUBLE_GOLD_CHEST;
				cwidth = width = 64;
				cheight = height = 32;
				animation.setFrames(bigChest);
			}
		}
		animation.setDelay(25);
	}
	
	public void determineItem() {
		item = new Item(tileMap);
		item.setTilePosition(y / tileSize, x / tileSize);
		int itemNumber = r.nextInt(100);
		if (hasLuckyCharm) {
			
		}
		else {
			if (chestType == DOUBLE_GOLD_CHEST) {
				if (itemNumber >= 90) {
					item.setType(Item.MARTYRDOM_DEATH_CHARM);					
				}
				else if (itemNumber >= 80 && itemNumber < 90) {
					item.setType(Item.ANESTHESIA);
				}
				else if (itemNumber >= 60 && itemNumber < 80) {
					item.setType(Item.WHEEL_OF_TIME);
				}
				else if (itemNumber >= 40 && itemNumber < 60) {
					item.setType(Item.METAL_DETECTOR);
				}
				else if (itemNumber >= 20 && itemNumber < 40) {
					item.setType(Item.RECHARGING_SHIELD);
				}
				else if (itemNumber >= 15 && itemNumber < 20) {
					item.setType(Item.NICE_SNEAKERS);
				}
				else if (itemNumber >= 10 && itemNumber < 15) {
					item.setType(Item.STRENGTH_ENHANCER);
				}
				else if (itemNumber >= 5 && itemNumber < 10) {
					item.setType(Item.HEATED_RUBBER);
				}
				else if (itemNumber >= 0 && itemNumber < 5) {
					item.setType(Item.RUBBER_STRIP);
				}
			}
			else if (chestType == GOLD_CHEST) {
				if (itemNumber >= 95) {
					item.setType(Item.MARTYRDOM_DEATH_CHARM);
				}
				else if (itemNumber >= 90) {
					item.setType(Item.ANESTHESIA);
				}
				else if (itemNumber >= 81) {
					item.setType(Item.BANDAGE);
				}
				else if (itemNumber >= 72) {
					item.setType(Item.CLOAK);
				}
				else if (itemNumber >= 63) {
					item.setType(Item.FOUR_LEAF_CLOVER);
				}
				else if (itemNumber >= 54) {
					item.setType(Item.HEATED_RUBBER);
				}
				else if (itemNumber >= 45) {
					item.setType(Item.METAL_DETECTOR);
				}
				else if (itemNumber >= 36) {
					item.setType(Item.NICE_SNEAKERS);
				}
				else if (itemNumber > 27) {
					item.setType(Item.RUBBER_STRIP);
				}
				else if (itemNumber > 18) {
					item.setType(Item.RECHARGING_SHIELD);
				}
				else if (itemNumber > 9) {
					item.setType(Item.STRENGTH_ENHANCER);
				}
			}
			else {
				if (itemNumber >= 90) {
					item.setType(Item.STRENGTH_ENHANCER);
				}
				else if (itemNumber >= 80) {
					item.setType(Item.FOUR_LEAF_CLOVER);
				}
				else if (itemNumber >= 70) {
					item.setType(Item.HEATED_RUBBER);
				}
				else if (itemNumber >= 60) {
					item.setType(Item.BANDAGE);
				}
				else if (itemNumber >= 50) {
					item.setType(Item.NICE_SNEAKERS);
				}
				else if (itemNumber >= 40) {
					item.setType(Item.CLOAK);
				}
				else if (itemNumber < 40) {
					item.setType(Item.RUBBER_STRIP);
				}
			}
		}
	}
	public Item recieve() {
		determineItem();
		return item;
	}
	public boolean open() {
		if (!opening && !done) {
			startChestAnimation = System.currentTimeMillis();
			endChestAnimation = startChestAnimation + openDelay;
			opening = true;
		}
		else if (opening && !animation.hasPlayedOnce()) {
			animation.update();
			Jukebox.play("chest");
		}
		else if (opening && animation.hasPlayedOnce()) {
			opening = false;
			done = true;
			return true;
		}
		return false;		
	}	
	public void draw(Graphics2D g) {
		if (!done && chestType != DOUBLE_GOLD_CHEST) {
			super.draw(g);
		}
		else if (!done && chestType == DOUBLE_GOLD_CHEST) {
			setMapPosition();
			g.drawImage(animation.getImage().getScaledInstance(64, 32, Image.SCALE_SMOOTH), x + xmap - width / 2, y + ymap - height / 2, null);
		}
		else {
			setMapPosition();
			if (chestType == SILVER_CHEST)
				g.drawImage(chests[0][2], x + xmap - width / 2, y + ymap - height / 2, null);
			else if (chestType == GOLD_CHEST)
				g.drawImage(chests[1][2], x + xmap - width / 2, y + ymap - height / 2, null);
			else if (chestType == DOUBLE_GOLD_CHEST)
				g.drawImage(bigChest[2].getScaledInstance(64, 32, Image.SCALE_SMOOTH), x + xmap - width / 2, y + ymap - height / 2, null);
		}
	}
}
