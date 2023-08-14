package ZombiesGame.messages;

import java.awt.*;

/**
 * The CreateProjectileMessage class is a message passed to the controller which tells the game to create a projectile
 */
public class CreateProjectileMessage implements Message
{
    private Point mousePosition;

    /**
     * Creates an instanc of the CreateProjectileMessage
     * @param mousePosition
     */
    public CreateProjectileMessage(Point mousePosition)
    {
        this.mousePosition = mousePosition;

//        System.out.println( mousePosition + "  " + playerPosition);
    }

    /**
     * Function that gets the mouse position when the mouse is pressed
     * @return
     */
    public Point getMousePosition()
    {
        return mousePosition;
    }
}