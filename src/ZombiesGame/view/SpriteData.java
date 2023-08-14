package ZombiesGame.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * contains all sprite data(buffered images), passes them to the view for drawing
 */
public class SpriteData
{
    // sprite data
    BufferedImage playerUp, playerDown, playerLeft, playerRight;
    BufferedImage projectile;
    BufferedImage background;
    BufferedImage enemy;
    BufferedImage coffee;
    BufferedImage mushroom;
    BufferedImage chicken;


    /**
     * Constructor reads all image data from sprites package and stores them in fields
     */
    public SpriteData()  {
        try
        {
            // player direction sprites
            playerUp = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/player_up.png"));
            playerDown = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/player_down.png"));
            playerLeft = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/player_left.png"));
            playerRight = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/player_right.png"));

            // other sprites
            projectile = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/projectile.png"));
            background = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/background_2.png"));
            enemy = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/zombie.png"));


            coffee = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/item_1.png"));
            mushroom = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/mushroom.png"));
            chicken = ImageIO.read(getClass().getResourceAsStream("/ZombiesGame/sprites/chicken.png"));

        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }


    /**
     * returns a player sprite with specified direction depending on which keys are being pressed
     * @return BufferedImage that stores player sprite
     */
    public BufferedImage getPlayerSprite()
    {
        ActionTracker keysPressed = ActionTracker.getInstance();

        if (keysPressed.isDown())
        {
            return playerDown;
        }
        else if (keysPressed.isLeft())
        {
            return playerLeft;
        }
        else if (keysPressed.isRight())
        {
            return playerRight;
        }
        else
        {
            return playerUp;
        }
    }


    /**
     * Gets projectile sprite
     * @return BufferedImage that stores data for projectile
     */
    public BufferedImage getProjectileSprite()
    {
        return projectile;
    }


    /**
     * Gets enemy sprite
     * @return BufferedImage that stores data for enemy
     */
    public BufferedImage getEnemySprite() {
        return enemy;
    }


    /**
     * Gets coffee sprite
     * @return BufferedImage that stores data for sprite
     */
    public BufferedImage getCoffeeSprite()
    {
      return coffee;
    }

    /**
     * Gets mushroom sprite
     * @return BufferedImage that stores data for sprite
     */
    public BufferedImage getMushroomSprite(){
        return mushroom;
    }

    /**
     * Gets chicken sprite
     * @return BufferedImage that stores data for sprite
     */
    public BufferedImage getChickenSprite(){
        return chicken;
    }

    /**
     * Gets background image
     * @return BufferedImage that stores the data for background image
     */
    public BufferedImage getBackground()
    {
        return background;
    }
}
