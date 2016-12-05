// Possibly redundant subclass of Entity.
// There are two types of items: Axe and boat.
// Upon collection, informs the Player
// that the Player does indeed have the item.


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Item extends Entity{
	
	private BufferedImage sprite;
	private int type;
	private Sparkle sparkle;
	
/*program reset room + fuck up timer*/	public static final int ANESTHESIA = 0; // resurrect, but the timer increases by some amount (long time)
/*a*/	public static final int BANDAGE = 1; // use to gain health points depending on difficulty
/*a*/	public static final int CLOAK = 2; // 50% chance of dodging all damage
	public static final int FOUR_LEAF_CLOVER = 3; // increased power ups and better chest drops
/*a*/	public static final int HEATED_RUBBER = 4; // after use, projectiles will be deflected off sword and will heat seek for enemies
/*a*/	public static final int MARTYRDOM_DEATH_CHARM = 5; // in death, explode gloriously and respawn with permanently one HP
/*a*/	public static final int METAL_DETECTOR = 6; // after each room have chance to get items
	public static final int NICE_SNEAKERS = 7; // increased movement speed
	public static final int RUBBER_STRIP = 8; // projectiles will be deflected off sword or will cancel shots
	public static final int RECHARGING_SHIELD = 9; // blocks one hit, recharges every two cleared rooms
	public static final int SPIDER_LEG = 10; // 150% damage to just spiders
/*a*/	public static final int STRENGTH_ENHANCER = 11; // 150% damage increase, at least 1 damage increase guaranteed
	public static final int WHEEL_OF_TIME = 12; // one time use of stopping time for 5 seconds.
	
	private boolean poof = false;
	private BufferedImage[] animated;
	
	public Item(TileMap tm) {
		super(tm);
		type = -1;
		width = height = 32;
		cwidth = cheight = 32;
		animated = Content.POOF[0];
		animation.setFrames(animated);
		animation.setDelay(4);
		sparkle = new Sparkle(tm);
	}
	
	public void setTilePosition(int row, int col) {
		super.setTilePosition(row, col);		
		sparkle.setTilePosition(row, col);
	}
	
	public BufferedImage getImage() {
		return sprite;
	}
	
	public String toString() {
		if (type == ANESTHESIA) {
			return "ANESTHESIA";
		}
		else if (type == BANDAGE) {
			return "BANDAGE";
		}
		else if (type == CLOAK) {
			return "CLOAK OF AGILITY";
		}
		else if (type == FOUR_LEAF_CLOVER) {
			return "FOUR LEAF CLOVER";
		}
		else if (type == HEATED_RUBBER) {
			return "HEATED RUBBER";
		}
		else if (type == MARTYRDOM_DEATH_CHARM) {
			return "MARTYDOM DEATH CHARM";
		}
		else if (type == METAL_DETECTOR) {
			return "METAL DETECTOR";
		}
		else if (type == NICE_SNEAKERS) {
			return "NICE SNEAKERS";
		}
		else if (type == RUBBER_STRIP) {
			return "RUBBER STRIP";
		}
		else if (type == RECHARGING_SHIELD) {
			return "RECHARGING SHIELD";
		}
		else if (type == SPIDER_LEG) {
			return "SPIDER LEG";
		}
		else if (type == STRENGTH_ENHANCER) {
			return "STRENGTH ENHANCER";
		}
		else if (type == WHEEL_OF_TIME) {
			return "WHEEL OF TIME";
		}
		else {
			return "?";
		}
	}
	
	public String effect() {
		if (type == ANESTHESIA) {
			return "Puts you to sleep after you die. Brings you back to life with half of your total health, although the run counts as a void.";
		}
		else if (type == BANDAGE) {
			return "Heals you for a certain amount depending on difficulty";
		}
		else if (type == CLOAK) {
			return "Chance to dodge both melee damage and projectile damage.";
		}
		else if (type == FOUR_LEAF_CLOVER) {
			return "Improves chances of getting a gold or better chest. Also gives better stuff from chests.";
		}
		else if (type == HEATED_RUBBER) {
			return "Deflect shots with homing bullets that deal 1 damage. Also increases your melee damage by 2. Note: the homing bullets may be buggy.";
		}
		else if (type == MARTYRDOM_DEATH_CHARM) {
			return "When you die you explode    gloriously and somehow you  survive                     You restore one health and cannot heal However you still have all of your items.";
		}
		else if (type == METAL_DETECTOR) {
			return "Better chances of finding something after you clear the room.";
		}
		else if (type == NICE_SNEAKERS) {
			return "Comfortable running shoes that allow you to move faster.";
		}
		else if (type == RUBBER_STRIP) {
			return "Improvement to your straw. When hitting bullets, you now deflect them instead of making them disappear.";
		}
		else if (type == RECHARGING_SHIELD) {
			return "Gain a shield that blocks damage every two room clears.";
		}
		else if (type == SPIDER_LEG) {
			return "Increased damage to spiders. Only spiders are able to drop these.";
		}
		else if (type == STRENGTH_ENHANCER) {
			return "You feel stronger after using this drug. You gain increased melee damage.";
		}
		else if (type == WHEEL_OF_TIME) {
			return "Mysterious, ancient relic that you somehow found. Stops time for a short duration.";
		}
		else {
			return "?";
		}
	}
	
	public String rarity() {
		if (type == ANESTHESIA) {
			return "Very Rare Consumable";
		}
		else if (type == BANDAGE) {
			return "Common Consumable";
		}
		else if (type == CLOAK) {
			return "Rare Item";
		}
		else if (type == FOUR_LEAF_CLOVER) {
			return "Uncommon Item";
		}
		else if (type == HEATED_RUBBER) {
			return "Common Consumable";
		}
		else if (type == MARTYRDOM_DEATH_CHARM) {
			return "Very Rare Consumable";
		}
		else if (type == METAL_DETECTOR) {
			return "Rare Item";
		}
		else if (type == NICE_SNEAKERS) {
			return "Uncommon Item";
		}
		else if (type == RUBBER_STRIP) {
			return "Common Item";
		}
		else if (type == RECHARGING_SHIELD) {
			return "Rare Item";
		}
		else if (type == SPIDER_LEG) {
			return "Rare Item";
		}
		else if (type == STRENGTH_ENHANCER) {
			return "Uncommon Item";
		}
		else if (type == WHEEL_OF_TIME) {
			return "Very Rare Consumable";
		}
		else {
			return "?";
		}
	}
	
	public String condition() {
		if (type == ANESTHESIA) {
			return "Consumed upon death";
		}
		else if (type == BANDAGE) {
			return "Consumed with Use Action";
		}
		else if (type == CLOAK) {
			return "Passive Effect";
		}
		else if (type == FOUR_LEAF_CLOVER) {
			return "Passive Effect";
		}
		else if (type == HEATED_RUBBER) {
			return "Consumed with Use Action";
		}
		else if (type == MARTYRDOM_DEATH_CHARM) {
			return "Consumed upon death";
		}
		else if (type == METAL_DETECTOR) {
			return "passive effect";
		}
		else if (type == NICE_SNEAKERS) {
			return "passive effect";
		}
		else if (type == RUBBER_STRIP) {
			return "passive effect";
		}
		else if (type == RECHARGING_SHIELD) {
			return "passive effect";
		}
		else if (type == SPIDER_LEG) {
			return "passive effect";
		}
		else if (type == STRENGTH_ENHANCER) {
			return "passive effect";
		}
		else if (type == WHEEL_OF_TIME) {
			return "consumed with use action";
		}
		else {
			return "?";
		}
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int i) {
		type = i;
		if(type == ANESTHESIA) {
			sprite = Content.ANESTHESIA[0][0];
		}
		else if(type == BANDAGE) {
			sprite = Content.BANDAGE[0][0];
		}
		else if(type == CLOAK) {
			sprite = Content.CLOAK[0][0];
		}
		else if(type == FOUR_LEAF_CLOVER) {
			sprite = Content.FOUR_LEAF_CLOVER[0][0];
		}
		else if (type == HEATED_RUBBER) {
			sprite = Content.HEATED_RUBBER[0][0];
		}
		else if(type == MARTYRDOM_DEATH_CHARM) {
			sprite = Content.MARTYRDOM_DEATH_CHARM[0][0];
		}
		else if(type == METAL_DETECTOR) {
			sprite = Content.METAL_DETECTOR[0][0];
		}
		else if(type == NICE_SNEAKERS) {
			sprite = Content.NICE_SNEAKERS[0][0];
		}
		else if(type == RUBBER_STRIP) {
			sprite = Content.RUBBER_STRIP[0][0];
		}
		else if(type == RECHARGING_SHIELD) {
			sprite = Content.RECHARGING_SHIELD[0][0];
		}
		else if(type == SPIDER_LEG) {
			sprite = Content.SPIDER_LEG[0][0];
		}
		else if(type == STRENGTH_ENHANCER) {
			sprite = Content.STRENGTH_ENHANCER[0][0];
		}
		else if(type == WHEEL_OF_TIME) {
			sprite = Content.WHEEL_OF_TIME[0][0];
		}
		else {
			System.out.println("invalid item");
		}
	}
	
	public static BufferedImage getImage(int i) {
		int type = i;
		BufferedImage sprite;
		if(type == ANESTHESIA) {
			sprite = Content.ANESTHESIA[0][0];
		}
		else if(type == BANDAGE) {
			sprite = Content.BANDAGE[0][0];
		}
		else if(type == CLOAK) {
			sprite = Content.CLOAK[0][0];
		}
		else if(type == FOUR_LEAF_CLOVER) {
			sprite = Content.FOUR_LEAF_CLOVER[0][0];
		}
		else if (type == HEATED_RUBBER) {
			sprite = Content.HEATED_RUBBER[0][0];
		}
		else if(type == MARTYRDOM_DEATH_CHARM) {
			sprite = Content.MARTYRDOM_DEATH_CHARM[0][0];
		}
		else if(type == METAL_DETECTOR) {
			sprite = Content.METAL_DETECTOR[0][0];
		}
		else if(type == NICE_SNEAKERS) {
			sprite = Content.NICE_SNEAKERS[0][0];
		}
		else if(type == RUBBER_STRIP) {
			sprite = Content.RUBBER_STRIP[0][0];
		}
		else if(type == RECHARGING_SHIELD) {
			sprite = Content.RECHARGING_SHIELD[0][0];
		}
		else if(type == SPIDER_LEG) {
			sprite = Content.SPIDER_LEG[0][0];
		}
		else if(type == STRENGTH_ENHANCER) {
			sprite = Content.STRENGTH_ENHANCER[0][0];
		}
		else if(type == WHEEL_OF_TIME) {
			sprite = Content.WHEEL_OF_TIME[0][0];
		}
		else {
			sprite = null;
		}
		return sprite;
	}
	
	public boolean isUsable() {
		if (type == BANDAGE) {
			return true;
		}
		else if (type == HEATED_RUBBER) {
			return true;
		}
		else if (type == WHEEL_OF_TIME) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean collected(Player p) {
		if(type == ANESTHESIA && (!p.hasAnesthesia() && !p.hasMartyrdomDeathCharm())) {
			p.setHasAnesthesia(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == BANDAGE && (!p.hasBandage() && !p.hasHeatedRubber() && !p.hasWheelOfTime())) {
			p.setHasBandage(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == CLOAK && !p.hasCloakOfAgility()) {
			p.setHasCloakOfAgility(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == FOUR_LEAF_CLOVER) {
			p.setHasFourLeafClover(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == HEATED_RUBBER && (!p.hasBandage() && !p.hasHeatedRubber() && !p.hasWheelOfTime())) {
			p.setHasHeatedRubber(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == MARTYRDOM_DEATH_CHARM && (!p.hasAnesthesia()) && !p.hasMartyrdomDeathCharm()) {
			p.setHasMartyrdomDeathCharm(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == METAL_DETECTOR && !p.hasMetalDetector()) {
			p.setHasMetalDetector(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == NICE_SNEAKERS && !p.hasNiceSneakers()) {
			p.setHasNiceSneakers(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == RUBBER_STRIP && !p.hasRubberStrip()) {
			p.setHasRubberStrip(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == RECHARGING_SHIELD && !p.hasRechargingShield()) {
			p.setHasRechargingShield(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == SPIDER_LEG && !p.hasSpiderLeg()) {
			p.setHasSpiderLeg(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == STRENGTH_ENHANCER && !p.hasStrengthEnhancer()) {
			p.setHasStrengthEnhancer(true);
			p.addToInventory(type, isUsable());
			return true;
		}
		else if (type == WHEEL_OF_TIME && (!p.hasBandage() && !p.hasHeatedRubber() && !p.hasWheelOfTime())) {
			p.setHasWheelOfTime(true);
			p.addToInventory(type, isUsable());
			return true;
		}		
		else {
			System.out.println("?");
			return false;
		}
	}
	
	public void update() {
		if (!poof && !animation.hasPlayedOnce()) {
			animation.update();
		}
		else if (animation.hasPlayedOnce()) {
			poof = true;
			sparkle.update();
		}
	}
	
	public void draw(Graphics2D g) {
		if (!poof)
			super.draw(g);
		else {
			try {
				setMapPosition();
				g.drawImage(sprite.getScaledInstance(16, 16, Image.SCALE_SMOOTH), x + xmap - width / 2 + 8, y + ymap - height / 2 + 8, null);
				sparkle.draw(g);
			} catch (NullPointerException e) {
				System.out.println(toString());
			}
		}
	}
	
}

