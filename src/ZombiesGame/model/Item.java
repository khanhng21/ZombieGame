package ZombiesGame.model;

import java.awt.*;
import java.util.Random;

/**
 * Item class that inherits from Entity
 * has all basic methods and fields (position, velocity, etc.)
 */
public class Item extends Entity
{

    Random r = new Random();
    private int type = 0;

    /**
     * Defualt constructor that creates an item object at the position of another entity 'e'
     * @param e Entity whose position will be used to create an item
     */
    public Item (Entity e)
    {
        super(e.x, e.y);
        type = r.nextInt(3);
    }

    public int getType(){
        return type;
    }

    /**
     * get item hitbox for collision checks
     * @return Rectangle with width and height of 64, at position of item
     */
    @Override
    public Rectangle getHitBox()
    {
        int hitboxHeight = 64;
        int hitboxWidth = 64;

        return new Rectangle(x, y, hitboxWidth, hitboxHeight);
    }


    // no real need to fully implement this method for items, during collision check,
    // player collision will always be checked first and handle player-item collisions
    @Override
    public boolean collidesWith(Entity e)
    {
        // check if intersection occurs between hitboxes and that they don't reference same object
        boolean hasCollision = getHitBox().intersects(e.getHitBox()) && this != e;
        Class type = e.getClass();

        // if e is a player and hitboxes are touching, return true
        if (type == Player.class && hasCollision)
        {
            return true;
        }
        return false;
    }
}
