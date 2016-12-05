import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
public class Bullet {

	//FIELDS
	protected double x;
	protected double y;
	protected int r;
	
	protected double rad;
	protected double speed;
	protected double dx;
	protected double dy;
	
	protected Color color1;
	
	protected int xmap;
	protected int ymap;
	protected TileMap tileMap;
	protected BufferedImage image;
	long systime;
	
	//CONSTUCTOR
	public Bullet(double angle, int x, int y, TileMap tm){
		tileMap = tm;		
		
		this.x = x;
		this.y = y;
		r = 4;
		
		image = Content.SLIMEBALL[0][0];
		
		rad = Math.toRadians(angle);
		dx = Math.cos(rad);
		dy = Math.sin(rad);
		speed = 5;
		systime = System.currentTimeMillis() + 500;
	}
	//Functions
	public boolean update(){		
		x += (dx * speed);
		y += (dy * speed);
		
		setMapPosition();
		
		
		if (x < -xmap || x > -xmap + tileMap.getTileSize() * 16 || y < -ymap ||
				y > -ymap + tileMap.getTileSize() * 16 ||
				tileMap.getType((int)((y) / tileMap.getTileSize()) , (int)((x) / tileMap.getTileSize())) == Tile.BLOCKED ) {
			return true;
		}
		return false;
	}
	
	public double getAngle() {
		return Math.toDegrees(rad);
	}
	
	public double getx() {
		return x;
	}
	
	public double gety() {
		return y;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public Rectangle getRectangle() {
		return new Rectangle((int)x - r + xmap, (int)y - r + ymap, 2*r, 2*r);
	}
	
	public void draw(Graphics2D g){
		setMapPosition();
		g.drawImage(image, (int)x - r + xmap, (int)y - r + ymap, null);
	}
}
