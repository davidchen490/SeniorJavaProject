import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class GameOverState extends GameState {

	ArrayList<Integer> sizes;
	ArrayList<String> words;
	ArrayList<Point> offset;
	int width;
	int height;
	Dimension dimension;
	int currentOption;
	boolean win;
	private String[] options = {
			"RESUME",
			"RESTART",
		};
	
	public GameOverState(GameStateManager gsm, Dimension d) {
		super(gsm);
		words = new ArrayList<String>();
		sizes = new ArrayList<Integer>();
		offset = new ArrayList<Point>();
		dimension = d;
		width = (int)d.getWidth();
		height = (int)d.getHeight();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		if (!gsm.getPlayer().isDead()) {
			words.add("YOU HAVE WON");
		}
		else {
			words.add("YOU HAVE DECEASED");
		}
		
		words.add("RESTART");
		words.add("MENU");

		sizes.add(words.get(0).length() * 8);
		offset.add(new Point(width / 2 - sizes.get(0), height / 16));
		
		sizes.add(words.get(1).length() * 8);
		offset.add(new Point(width / 2 - sizes.get(1), height / 4 - 8));
		
		sizes.add(words.get(2).length() * 8);
		offset.add(new Point(width / 2 - sizes.get(2), height / 2 - 8));
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(new Color(0,0,0,126));
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.GRAY);
		
		g.fillRoundRect(width / 4, 3 * height / 16, width / 2, height / 8, width / 32, width / 32);
		g.fillRoundRect(width / 4, 7 * height / 16, width / 2, height / 8, width / 32, width / 32);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.GRAY.darker());
		
		g.drawRoundRect(width / 4, 3 * height / 16, width / 2, height / 8, width / 32, width / 32);
		g.drawRoundRect(width / 4, 7 * height / 16, width / 2, height / 8, width / 32, width / 32);
		
		for (int i = 0; i < 3; i ++) {
			Content.drawWhiteString(g, words.get(i), (int)offset.get(i).getX(), (int)offset.get(i).getY());
		}
		
		g.setColor(Color.WHITE);
		if (currentOption == 0) {
			g.drawRoundRect(width / 4, 3 * height / 16, width / 2, height / 8, width / 32, width / 32);
		}
		else if (currentOption == 1) {
			g.drawRoundRect(width / 4, 7 * height / 16, width / 2, height / 8, width / 32, width / 32);
		}
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if(Keys.isPressed(Keys.ESCAPE)) {
			gsm.setState(GameStateManager.MENU);
		}
		if ((Keys.isPressed(Keys.S) || Keys.isPressed(Keys.DOWN)) && currentOption < options.length - 1) {
			currentOption++;
		}
		if ((Keys.isPressed(Keys.W) || Keys.isPressed(Keys.UP)) && currentOption > 0) {
			currentOption--;
		}
		if (Keys.isPressed(Keys.ENTER)) {
			selectOption();
		}
		if (Keys.isPressed(Keys.SPACE)) {
			selectOption();
		}
	}
	
	private void selectOption() {
		if (currentOption == 0) {
			gsm.setState(GameStateManager.PLAY);
			
		}
		if (currentOption == 1) {
			gsm.setState(GameStateManager.MENU);
			
		}
	}
}
