import java.awt.Graphics2D;

public class SeekingBullet extends Bullet {
	long endDelay;
	long seekingDelay = 500;
	int enemyNumber;
	Enemy enemy;
	public SeekingBullet(double angle, int x, int y, TileMap tm) {
		super(angle, x, y, tm);
		image = Content.FIREBALL[0][0];
		endDelay = System.currentTimeMillis() + seekingDelay;
	}
	public void setEnemy (Enemy e, int _enemyNumber) {
		enemy = e;
		enemyNumber = _enemyNumber;
	}
	public boolean update() {
		setMapPosition();
		if (System.currentTimeMillis() < endDelay) {
			x += (dx * speed);
			y += (dy * speed);
			if (x < -xmap || x > -xmap + tileMap.getTileSize() * 16 || y < -ymap ||
					y > -ymap + tileMap.getTileSize() * 16 ||
					tileMap.getType((int)((x) / tileMap.getTileSize()) , (int)((y) / tileMap.getTileSize())) == Tile.BLOCKED ||
					enemy.isDead()) {
				return true;
			}
			return false;
		}
		else {
			double angle = Math.atan2(enemy.gety() - y, enemy.getx() - x);
			dx = Math.cos(angle);
			dy = Math.sin(angle);
			x += (dx * speed);
			y += (dy * speed);
			if (x < -xmap || x > -xmap + tileMap.getTileSize() * 16 || y < -ymap ||
					y > -ymap + tileMap.getTileSize() * 16 ||
					tileMap.getType((int)((x) / tileMap.getTileSize()) , (int)((y) / tileMap.getTileSize())) == Tile.BLOCKED ||
					enemy.isDead()) {
				return true;
			}
			return false;
		}
	}
	public Enemy getEnemy() {
		return enemy;
	}
	public int getEnemyNumber() {
		return enemyNumber;
	}
	public void draw(Graphics2D g) {
		g.fillRect((int)x-r+xmap, (int)y-r+ymap, 2*r, 2*r);
	}
}
