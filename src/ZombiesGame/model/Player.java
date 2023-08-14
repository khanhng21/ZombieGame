package ZombiesGame.model;

import java.awt.*;

/**
 * Player class that inherits from Entity
 * has all basic methods and fields (position, velocity, etc.)
 */
public class Player extends Entity
{
    /**
     * Default constructor defined through superclass constructor
     * @param x integer value specifying the x-coordinates of entity
     * @param y integer value specifying the y-coordinates of entity
     */
    public Player(int x, int y)
    {
        super(x, y);
    }


    /**
     * gets player hitbox for collision checks
     * @return Rectangle, created at player position, with height and width 64
     */
    @Override
    public Rectangle getHitBox()
    {
        int hitboxWidth = 64;
        int hitboxHeight = 64;

        return new Rectangle(x, y, hitboxWidth, hitboxHeight);
    }


    /**
     * checks for whether collision occurs between this entity and entity 'e', assuming they are not the same reference
     * @param e given entity e that may collide with this entity
     * @return true if hitboxes intersect and entity 'e' is an enemy or an item, otherwise false
     */
    @Override
    public boolean collidesWith(Entity e)
    {
        // check if intersection occurs between hitboxes and that they don't reference same object
        boolean hasCollision = getHitBox().intersects(e.getHitBox()) && this != e;
        Class type = e.getClass();

        // if e is an enemy or item and hitboxes are touching, return true
        // (only interacts with enemies and items)
        if ((type == Enemy.class || type == Item.class) && hasCollision)
        {
            return true;
        }
        return false;
    }
}
