package ZombiesGame.model;

import java.awt.*;

/**
 * Enemy class that inherits from Entity
 * has all basic methods and fields (position, velocity, etc.)
 */
public class Enemy extends Entity
{
	/**
	 * Default constructor defined through superclass constructor
	 * @param x integer value specifying the x-coordinates of entity
	 * @param y integer value specifying the y-coordinates of entity
	 */
	public Enemy(int x, int y)
	{
		super(x, y);
	}


	/**
	 * get enemy hitbox for collision checks
	 * @return Rectangle with width and height of 64, at position of enemy
	 */
	@Override
	public Rectangle getHitBox()
	{
		int hitboxWidth 	= 64;
		int hitboxHeight 	= 64;

		// creates hit box that should be in the center of the enemy;
		return new Rectangle(x, y, hitboxWidth, hitboxHeight);
	}


	/**
	 * Checks whether entity collides with another given entity, assuming they reference different entities
	 * @param e given entity 'e' that may collide with this entity
	 * @return true if hitboxes intersect, and entity 'e' is a player, projectile, or another enemy, false otherwise
	 */
	@Override
	public boolean collidesWith(Entity e)
	{
		// check if intersection occurs between hitboxes and that they don't reference same object
		boolean hasCollision = getHitBox().intersects(e.getHitBox()) && this != e;
		Class type = e.getClass();

		// if e is a player, projectile, or another enemy and hitboxes touch return true
		if ((type == Player.class || type == Enemy.class || type == Projectile.class) && hasCollision)
		{
			return true;
		}

		return false;
	}
}
