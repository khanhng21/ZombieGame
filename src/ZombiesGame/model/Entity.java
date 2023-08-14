package ZombiesGame.model;

import java.awt.*;

/**
 * abstract class inherited by all game objects,
 * stores data about position, velocity, activity, collision
 * defines common methods for updating position, activity, collision, and checking for collision
 */
public abstract class Entity
{
	// entity collision and activity state
	protected boolean isColliding;
	private boolean isActive;

	// entity positions and velocities
	protected int x;
	protected int y;
	protected int dx;
	protected int dy;


	/**
	 * Default constructor for Entity and inheriting classes that initializes starting position and collision,
	 * activity state
	 * @param x integer value specifying the x-coordinates of entity
	 * @param y integer value specifying the y-coordinates of entity
	 */
	public Entity(int x, int y)
	{
		isColliding = false;
		isActive = true;
		this.x  = x;
		this.y  = y;
	}


	/**
	 * Updates the x and y positions through current x and y velocities(dx and dy)
	 */
	public void translate()
	{
		 x += dx;
		 y += dy;
	}


	/**
	 * Sets the entity to inactive (dead)
	 * once set to inactive, will remain inactive until deleted
	 */
	public void setInactive()
	{
		isActive = false;
	}


	/**
	 * Gets the x component of entity position (needed in view for drawing)
	 * @return x component of position
	 */
	public int getX()
	{
		return x;
	}


	/**
	 * Gets y component of entity positions (needed in view for drawing)
	 * @return y component of position
	 */
	public int getY()
	{
		return y;
	}


	/**
	 * Gets whether the entity is active or inactive
	 * @return isActive field specifying whether entity is active/inactive
	 */
	public boolean isActive()
	{
		return this.isActive;
	}


	/**
	 * Gets hitbox defined by a rectangle
	 * Used for collision checks
	 * @return Rectangle with specific width and height, created at center of entity sprite
	 */
	public abstract Rectangle getHitBox();


	/**
	 * Checks whether entity collides with another given entity, assuming they reference different entities
	 * @param e given entity e that may collide with this entity
	 * @return true if this entity interacts with other entity 'e' and hitbox intersects,
	 * false if this entity doesn't interact with other entity 'e' or hitbox doesn't intersect
	 */
	public abstract boolean collidesWith(Entity e);
}
