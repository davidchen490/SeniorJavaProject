import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class InventoryState extends GameState {

	private int[][] currentOption;
	int currentOptionx;
	int currentOptiony;
	Dimension dimension;
	int width;
	int height;
	ArrayList<Item> items;
	Item consumable;
	
	public InventoryState(GameStateManager gsm, Dimension d) {
		super(gsm);
		currentOptionx = 0;
		currentOptiony = 0;
		dimension = d;
		width = (int)dimension.getWidth();
		height = (int)dimension.getHeight();
		currentOption = new int[4][12];
		items = new ArrayList<Item>();
		init();
	}

	@Override
	public void init() {
		
	}
	
	public void setInventory(ArrayList<Item> items) {
		this.items = items;
	}

	public void setConsumable(Item i) {
		consumable = i;
	}
	
	@Override
	public void update() {
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5));
		g.drawRect(0, 0, width, height);
		g.setStroke(new BasicStroke(1));
		for (int y = 0; y < currentOption.length; y++) {
			for (int x = 0; x < currentOption[y].length; x++) {
				g.drawRect(28 + 38 * x, 38 * y + 76 + 256, 34, 34);
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(30 + 38 * x, 38 * y + 78 + 256, 32, 32);
				g.setColor(Color.BLACK);
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(28, 28, width - 56, height / 2);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(38, 38, height / 2 - 20, height / 2 - 20);
		g.fillRect(28 + height / 2, 38, 188, height / 2 - 20);
		
		for (int x = 0; x < items.size(); x++) {
			Item item = items.get(x);
			g.drawImage(item.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH), 30 + 38 * x, 78 + 256, null);
			//g.drawImage(item.getImage().getScaledInstance(height / 2 - 18, height / 2 - 18, Image.SCALE_SMOOTH),  40, 40, null);
		}
		
		if (consumable.getType() != -1) {
			Item item = consumable;
			g.drawImage(item.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH), 30 + 38 * items.size(), 78 + 256, null);
		}
		
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.WHITE);
				
		g.drawRect(30 + 38 * currentOptionx, 78 + 256 + 38 * currentOptiony, 32, 32);
		
		System.out.println(currentOptionx);
		
		if (currentOptionx < items.size() && currentOptiony == 0) {
			Item i = items.get(currentOptionx);
			Content.drawWhiteString1(g, i.toString(), 28 + height / 2 + 94 - i.toString().length() * 6, 38);
			Content.drawSmallBlueString(g, i.rarity(), 30 + height / 2, 66);
			Content.drawSmallBlueString(g, i.condition(), 35 + height / 2, 76);
			g.setColor(Color.BLACK);
			g.fillRect(28 + height / 2, 86, 188, 3);
			
			int m = i.effect().length() / 28;
			
			for (int x = 0; x < m; x++) {
				String s = i.effect().substring(x*28, x*28 + 28);
				Content.drawSmallBlueString(g, s, 30 + height / 2, 90 + 7 * x);
			}
			String s = i.effect().substring(m*28, i.effect().length());
			Content.drawSmallBlueString(g, s, 30 + height / 2, 90 + 7 * m + 1);
			
			g.drawImage(i.getImage().getScaledInstance(height / 2 - 24, height / 2 - 24, Image.SCALE_SMOOTH), 40, 40, null);
		}
		else if (currentOptionx == items.size() && currentOptiony == 0 && consumable.getType() != -1) {
			Item i = consumable;
			Content.drawWhiteString1(g, i.toString(), 28 + height / 2 + 94 - i.toString().length() * 6, 38);
			Content.drawSmallBlueString(g, i.rarity(), 30 + height / 2, 66);
			Content.drawSmallBlueString(g, i.condition(), 35 + height / 2, 76);
			g.setColor(Color.BLACK);
			g.fillRect(28 + height / 2, 86, 188, 3);
			
			int m = i.effect().length() / 28;
			
			for (int x = 0; x < m; x++) {
				String s = i.effect().substring(x*28, x*28 + 28);
				Content.drawSmallBlueString(g, s, 30 + height / 2, 90 + 7 * x);
			}
			String s = i.effect().substring(m*28, i.effect().length());
			Content.drawSmallBlueString(g, s, 30 + height / 2, 90 + 7 * m + 1);
			
			g.drawImage(i.getImage().getScaledInstance(height / 2 - 24, height / 2 - 24, Image.SCALE_SMOOTH), 40, 40, null);
		}
		
		
		
		
		
		
		
		
		
	}

	@Override
	public void handleInput() {
		if ((Keys.isPressed(Keys.ESCAPE) || Keys.isPressed(Keys.I))) {
			gsm.setInventory(false);
		}
		if ((Keys.isPressed(Keys.S) || Keys.isPressed(Keys.DOWN)) && currentOptiony < currentOption.length - 1) {
			currentOptiony++;
		}
		if ((Keys.isPressed(Keys.W) || Keys.isPressed(Keys.UP)) && currentOptiony > 0) {
			currentOptiony--;
		}
		if ((Keys.isPressed(Keys.D) || Keys.isPressed(Keys.RIGHT)) && currentOptionx < currentOption[currentOptiony].length - 1) {
			currentOptionx++;
		}
		if ((Keys.isPressed(Keys.A) || Keys.isPressed(Keys.LEFT)) && currentOptionx > 0) {
			currentOptionx--;
		}
	}

}
