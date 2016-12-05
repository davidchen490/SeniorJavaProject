import java.util.Random;

public class BossThread extends Thread {
	Boss boss;
	Random r; 
	public BossThread(Boss _boss) {
		boss = _boss;
		r = new Random();
		//Jukebox.load(s, n);
	}
	
	public void run() {
		try {
			while (true) {
				if (!boss.isDead()) {
					System.out.println("hello");
					if (boss.getDamageTaken() < 50) {
						System.out.println("tesT");
						if (!boss.getRestingState()) {
							System.out.println("at least here?");
							long start = System.currentTimeMillis();
							System.out.println("1");
							int i = 0;
							System.out.println("2");
							int intervals = r.nextInt(3 - boss.getDifficulty()) + 3;
							System.out.println("3");
							long end = System.currentTimeMillis() + r.nextInt((3 - boss.getDifficulty()) * 1000) + 4000;
							System.out.println("4");
							int timeEach = (int) ((end - start) / intervals);
							System.out.println("5");
							double disX = boss.getplayer().getx();
							System.out.println("6");
							double disZ = boss.getx();
							System.out.println("6");
							double disY = boss.getplayer().gety() - boss.gety();
							System.out.println("7");
							double angle = Math.atan2(disY, disX);
							System.out.println("8");
							while (System.currentTimeMillis() < end) {
								if ((int)(end - start) - timeEach * i / timeEach > 0) {
									i++;
									disX = boss.getplayer().getx() - boss.getx();
									disY = boss.getplayer().gety() - boss.gety();
									angle = Math.atan2(disY, disX);			
								}
								boss.charge(angle);
							}
							Thread.sleep(10);
							
						}
						else {
							
						}														
					}																	
					else if (boss.getDamageTaken() < 100 && boss.getDamageTaken() >= 50) {
						
					}
					else if (boss.getDamageTaken() < 150 && boss.getDamageTaken() >= 100) {
						
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
}
