// Contains a reference to the Player.
// Draws all relevant information at the
// bottom of the screen.

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.*;

public class Hud {
	
	private int yoffset;
	
	private BufferedImage bar;
	private BufferedImage fullHeart;
	private BufferedImage halfHeart;
	private BufferedImage emptyHeart;
	
	private Player player;
	
	private Dimension dimension;
	
	private Font font;
	private Color textColor;
	
	private JPanel parent;
	
	int fullHearts;
	int halfHearts;
	int emptyHearts;
	int totalHearts;
	
	int healthOffset;
	
	public Hud(Player p, Dimension d, JPanel _parent) {
		
		player = p;
		yoffset = (int)d.getHeight() - 32;
		
		dimension = d;
		
		bar = Content.BAR[0][0];
		fullHeart = Content.FULL_HEART[0][0];
		halfHeart = Content.HALF_HEART[0][0];
		emptyHeart = Content.EMPTY_HEART[0][0];
		
		
		font = new Font("Arial", Font.PLAIN, 8);
		textColor = new Color(47, 64, 126);
		
		parent = _parent;
		
		
	}
	
	public void update() {
		totalHearts = player.getTotalHealth() / 2;
		fullHearts = (int) Math.floor(player.getHealth() / 2);
		halfHearts = player.getHealth() % 2;
		emptyHearts = ((player.getTotalHealth() - player.getHealth()) / 2);
		healthOffset = (totalHearts) * 16 + 80; // first division
	}
	
	public void draw(Graphics2D g) {
		
		// draw hud
		g.setColor(new Color(169, 169, 169, 192));
		g.fillRect(0, yoffset - 3, (int)dimension.getWidth(), (int)dimension.getHeight() + 3);
		g.setColor(Color.black);
		g.drawImage(bar.getScaledInstance(64, 32, Image.SCALE_SMOOTH), 1, yoffset, null);
		

		
		for (int i = 0; i < fullHearts; i++) {
			g.drawImage(fullHeart.getScaledInstance(16, 16, Image.SCALE_SMOOTH), 32 + i * 16, yoffset + 3, null);
		}
		for (int i = 0; i < emptyHearts; i++) {
			g.drawImage(emptyHeart.getScaledInstance(16, 16, Image.SCALE_SMOOTH), 16 + (totalHearts * 16) - (i * 16), yoffset + 3, null);
		}
		if (halfHearts == 1) {
			g.drawImage(halfHeart.getScaledInstance(16, 16, Image.SCALE_SMOOTH), 32 + fullHearts * 16, yoffset + 3, null);
		}
		
		if (player.getHealth() >= 10)
			Content.drawSmallBlueString(g, "" + player.getHealth() + "/" + player.getTotalHealth(), (totalHearts + 2) * 16 + 10,  (int)yoffset + 5);
		else
			Content.drawSmallRedString(g, "0" + player.getHealth() + "/" + player.getTotalHealth(), (totalHearts + 2) * 16 + 10,  (int)yoffset + 5);
		
		g.setColor(Color.gray.darker());
		g.setStroke(new BasicStroke(3));
		g.drawRect((int)dimension.getWidth() / 2 - 17, yoffset - 3, (int)34, (int)34); // first division

		int textX = healthOffset + 2;
		
		
		
		if (player.getConsumable().getType() != -1) {
			g.drawImage(player.getConsumable().getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH), (int)dimension.getWidth() / 2 - 15, yoffset - 1, null);
		}
		
		
		
		// draw time
		int minutes = (int) (player.getTicks() / 3600);
		int seconds = (int) ((player.getTicks() / 60) % 60);
		if(minutes < 10) {
			if(seconds < 10) Content.drawString(g, "0" + minutes + ":0" + seconds, (int)(dimension.getWidth() / 2) - 40, 3);
			else Content.drawString(g, "0" + minutes + ":" + seconds, (int)(dimension.getWidth() / 2) - 40, 3);
		}
		else {
			if(seconds < 10) Content.drawString(g, minutes + ":0" + seconds, (int)(dimension.getWidth() / 2) - 32, 3);
			else Content.drawString(g, minutes + ":" + seconds, (int)(dimension.getWidth() / 2) - 32, 3);
		}
		
		
		
	}
	
}
