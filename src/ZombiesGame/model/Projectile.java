package ZombiesGame.model;

import java.awt.*;

/**
 * Projectile class inheriting from Entity
 * has all basic methods and fields (position, velocity, etc.)
 */
public class Projectile extends Entity
{
    /**
     * Default constructor that defines x and y position -- as well as x and y velocities
     * @param e Entity whose position this projectile will be created at
     * @param dx velocity in x, determines how much the projectile will move in x direction per update call
     * @param dy velocity in y, determines how much the projectile will move in y direction per update call
     */
    public Projectile(Entity e, int dx, int dy)
    {
        super(e.x, e.y);

        this.dx = dx;
        this.dy = dy;
    }


    /**
     * get projectile hitbox for collision checks
     * @return Rectangle with width and height of 32, at position of projectile, accounting for smaller hitbox size
     */
    public Rectangle getHitBox()
    {
        int hitboxWidth = 32;
        int hitboxHeight = 32;

        return new Rectangle(x + hitboxWidth/2, y + hitboxHeight/2, hitboxWidth, hitboxWidth);
    }


    /**
     * checks whether collision occurs with another entity, assuming they are different entities
     * @param e given entity e that may collide with this entity
     * @return true if hitboxes intersect and entity 'e' is an enemy, else return false
     */
    @Override
    public boolean collidesWith(Entity e)
    {
        // check if intersection occurs between hitboxes and that they don't reference same object
        boolean hasCollision = getHitBox().intersects(e.getHitBox()) && this != e;
        Class type = e.getClass();

        // if e is an enemy and hitboxes touch, they are colliding
        if (type == Enemy.class && hasCollision)
        {
            return true;
        }
        return false;
    }
}
