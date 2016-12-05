import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

public class ControlState extends GameState {

	Dimension dimension;
	int width;
	int height;
	
	public ControlState(GameStateManager gsm, Dimension d) {
		super(gsm);
		dimension = d;
		width = (int)d.getWidth();
		height = (int)d.getHeight();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.fillRoundRect(width / 4, height / 16, width / 2, height / 8, width / 32, width / 32);
		Content.drawWhiteString(g, "CONTROLS", width / 2 - "controls".length() * 8, height / 8 - 12);
		
		Content.drawWhiteString(g, "MOVE", width / 4 - 5*8, height / 4);
		Content.drawWhiteString(g, "W", width / 4 - 2 * 8, height / 4 + height / 16);
		Content.drawWhiteString(g, "ASD", width / 4 - 4 * 8, height / 4 + height / 16 + 12);
		
		Content.drawWhiteString(g, "ATTACK", 3 * width / 4 - 6 * 9,  height / 4);
		Content.drawWhiteString(g, "ARROW KEYS", 3 * width / 4 - 10 * 8, height / 4 + height / 16 + 12);
		
		Content.drawWhiteString(g, "INVENTORY", width / 4 - 10 * 8, height / 2);
		Content.drawWhiteString(g, "I", width / 4 - 2 * 8, height / 2 + 36);
		
		Content.drawWhiteString(g, "USE ITEM", 3 * width / 4 - 9 * 8, height / 2);
		Content.drawWhiteString(g, "SPACEBAR", 3 * width / 4 - 9 * 8, height / 2 + 36);
		
		Content.drawWhiteString(g, "PAUSE AND BACK", width / 2 - 14 * 8, 3 * height / 4);
		Content.drawWhiteString(g, "ESCAPE OR ESC", width / 2 - 14 * 8, 3 * height / 4 + 36);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		if (Keys.isPressed(Keys.ESCAPE)) gsm.setState(GameStateManager.MENU);
		if (Keys.isPressed(Keys.SPACE)) gsm.setState(GameStateManager.MENU);
		if (Keys.isPressed(Keys.ENTER)) gsm.setState(GameStateManager.MENU);
	}
	

}
