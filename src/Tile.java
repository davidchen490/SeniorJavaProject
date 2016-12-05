import java.awt.image.BufferedImage;

public class Tile {
	private BufferedImage image;
	private int type;
	
	//tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;
	public static final int OPEN_DOOR = 2;
	public static final int CLOSED_DOOR = 3;
	
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	public BufferedImage getImage() {
		return image;		
	}
	public int getType() {
		return type;
	}
	@Override
	public String toString() {
		if (getType() == NORMAL) {
			return "NORMAL";
		}
		if (getType() == BLOCKED) {
			return "BLOCKED";
		}
		if (getType() == OPEN_DOOR) {
			return "OPEN_DOOR";
		}
		if (getType() == CLOSED_DOOR) {
			return "CLOSED_DOOR";
		}
		return "?";
	}
}
