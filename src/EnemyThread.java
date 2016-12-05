import java.util.Random;

public class EnemyThread extends Thread {
	
	private Enemy enemy;
	private Random r;
	int inaccuracy;
	int possibleInaccuracy;
	
	public EnemyThread(Enemy _enemy) {
		enemy = _enemy;
		r = new Random();
		Jukebox.load("Resources/SFX/arrow.wav", "arrow");
		Jukebox.setVolume("arrow", -10);
	}
	
	public void run() {
		try{
			while (true) {
				if (enemy.getActive() && !enemy.isDead()) {
					if(!enemy.getRestingState())
					{
						/**For random direction
						if(shouldSetRandDir)
						{
							setXDirection(chooseRandomDirection());
							setYDirection(chooseRandomDirection());
							shouldSetRandDir = false;
						}*/
						long start = System.currentTimeMillis();
						long end = start + r.nextInt(1500) + 500;
						while(System.currentTimeMillis() < end)
						{
							
							double disX = enemy.getplayer().getx() - enemy.getx();
							double disY = enemy.getplayer().gety() - enemy.gety();
							double angle = Math.atan2(disY, disX);
							
							if (Math.abs(disX) < Math.abs(disY)) { // Checks if enemy should move vertically
								
								if (disY > 0) {
									enemy.setDown(); // Enemy moves down
								}
								
								else if (disY < 0) {
									enemy.setUp(); // Enemy moves up
								}
								
							}
							
							else if (Math.abs(disX) > Math.abs(disY)) { // Checks if enemy should move horizontally
								
								if (disX > 0) {
									enemy.setRight(); // Enemy move to the right
								}
								
								else if (disX < 0) {
									enemy.setLeft(); // Enemy move to the left
								}
								
							}
							Thread.sleep(33);
						}
						enemy.setRestingState(true);
					}
					else
					{
						attackPlayer(enemy);
						int delay = r.nextInt(1000) + 500;
						int increments = r.nextInt(12) + 8;
						int delayEach = delay/increments;
						for (int i = 0; i < increments; i++) {
							if (i % 4 == 0) {
								attackPlayer(enemy);
								Jukebox.play("arrow");
							}
							Thread.sleep(delayEach);
						}
						//shouldSetRandDir = true;
						enemy.setRestingState(false);
					}
				}
				else {
					Thread.sleep(1000);
				}
			}
		} catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}		
	}
	
	public void attackPlayer(Enemy enemy) {
		int bulletX = enemy.getx();
		int bulletY = enemy.gety();
		double angle = Math.toDegrees(Math.atan2(enemy.getplayer().gety() - bulletY, enemy.getplayer().getx() - bulletX));
		Bullet bullet = new Bullet(angle, bulletX, bulletY, enemy.getTileMap());
		enemy.getBullets().add(bullet);
	}
}
