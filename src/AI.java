import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
//import java.util.Random;

public class AI 
{
	Rectangle AI, target;
	
	int xDirection, yDirection;
	
	boolean resting = false; 
	
	boolean shouldSetRandDir = true;
	
	//Find a path to the target
	public static double findPathToTarget(Rectangle AI, Rectangle target)
	{
		double xDis = target.x + target.width / 2 - AI.x + AI.width / 2;
		double yDis = target.y + target.height / 2 - AI.y + AI.height / 2;
		return Math.atan2(yDis, xDis);
	}
	
	//Choose random direction
	public static double chooseRandomDirection()
	{
		double angle = Math.toRadians(Math.random() * 360);
		return angle;
	}
	
	//Move in that direction
	public void detectEdges()
	{
		if(AI.x <= 0);
			setXDirection(1);
		if(AI.x >= 600 - AI.width);
			setXDirection(-1);
		if(AI.y >= 25);
			setYDirection(1);
		if(AI.y > 400);
			setYDirection(-1);
	}
	
	public static int setXDirection(int dir)
	{
		return dir;
	}
	public static int setYDirection(int dir)
	{
		return dir;
	}
	public void move(){
		AI.x += xDirection;
		AI.y += yDirection;
	}
	//In run method, move in that direction and then wait
	//@Override
	/*
	public void run()
	{
		try{
			while(true)
			{
				if(!resting)
				{
					/**For random direction
					if(shouldSetRandDir)
					{
						setXDirection(chooseRandomDirection());
						setYDirection(chooseRandomDirection());
						shouldSetRandDir = false;
					}
					long start = System.currentTimeMillis();
					long end = start + 3*500;
					while(System.currentTimeMillis() < end)
					{
						findPathToTarget(AI, target);
						move();
						detectEdges();
						Thread.sleep(10);
					}
					resting = true;
				}
				else
				{
					Thread.sleep(3000);
					shouldSetRandDir = true;
					resting = false;
				}
			}
		}catch(Exception ex)
		{
			System.err.println(ex.getMessage());
		}
	}*/
}
