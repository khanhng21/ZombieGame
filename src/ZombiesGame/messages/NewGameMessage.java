package ZombiesGame.messages;

/**
 * The NewGameMessage class is a message passed to the controller which tells the game to start initializing the game objects/data and the player
 */
public class NewGameMessage implements Message
{
    private int width;
    private int height;
    private int spriteSize;

    /**
     * Creates an instance of the NewGameMessage class
     * @param panelWidth
     * @param panelHeight
     * @param spriteSize
     */
    public NewGameMessage(int panelWidth, int panelHeight, int spriteSize)
    {
        width = panelWidth;
        height = panelHeight;
        this.spriteSize = spriteSize;
    }

    /**
     * This function gets the width passed to the NewGameMessage class
     * @return
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * This function gets the height passed to the NewGameMessage class
     * @return
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * This function gets the sprite size passed to the NewGameMessage class
     * @return
     */
    public int getSpriteSize()
    {
        return spriteSize;
    }
}
