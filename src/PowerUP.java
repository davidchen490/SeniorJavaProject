import java.awt.*;
public class PowerUP extends Entity{

	//FIELDS
	private int r;
	private int type;
	private Rectangle rect;
	private Color color1;
	private TileMap tm;
	
	public static int HEALTH = 1;
	public static int POWER = 2;
	public static int SPEED = 3;
	
	/**1 -- +1 life << 1 point is half a heart
		2 -- +1 power
		3 -- +2 power
	**/
	//CONSTRUCTOR
	public PowerUP(TileMap tm)
	{
		super(tm);
		type = -1;
		width = height = 32;
		cwidth = cheight = 32;
	}
	public void setType(int i) {
		type = i;
		if(type == HEALTH) {
			color1 = Color.PINK;//Health
			r = 3;
			width = height = 6;
			cwidth = cheight = 6;
		}
		if(type == POWER) {
			color1 = Color.YELLOW;//Power
			r = 3;
			width = height = 6;
			cwidth = cheight = 6;
		}		
		if(type == SPEED) {
			color1 = Color.BLUE;//Speed
			r = 5;
			width = height = 10;
			cwidth = cheight = 10;
		}
	}
	
	//FUNCTIONS
	public double getr()
	{
		return r;
	}
	public int getType()
	{
		return type;
	}
	public void collected(Player p) {
		if(type == 1) {
			p.collectedHealth();
		}	
	}	
	public void draw(Graphics2D g)
	{
		setMapPosition();
		g.setColor(color1);
		g.fillRect((int)(x + xmap) + width / 2 - r, (int)(y + ymap) + height / 2 - r, 2*r, 2*r);
		g.setColor(Color.BLACK);
	}
}
