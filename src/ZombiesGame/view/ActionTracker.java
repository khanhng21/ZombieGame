package ZombiesGame.view;

/**
 * ActionTracker class tracks changes between which keys are currently being pressed
 * implements Singleton design pattern to ensure single instance of object exists and to share one reference
 */
public class ActionTracker
{
    // ensures one instance of ActionTracker exists
    private static ActionTracker instance;

    // booleans defining whether specific keys are being pressed or not
    private boolean isUp, isDown, isLeft, isRight, isClicked;


    /**
     * Default constructor that does nothing
     */
    private ActionTracker()
    {
        // does nothing
    }

    /**
     * resets the state of the ActionTracker,
     * set the instance to null to avoid keys states from transferring over to new game
     */
    public void reset()
    {
        instance = null;
//        isUp = false; isDown = false; isLeft = false; isRight = false; isClicked = false;
    }



    // setter methods
    public void setUp(boolean b)
    {
        this.isUp = b;
    }

    public void setDown(boolean b)
    {
        this.isDown = b;
    }

    public void setRight(boolean b)
    {
        this.isRight = b;
    }

    public void setLeft(boolean b)
    {
        this.isLeft = b;
    }

    public void setClicked(boolean b)
    {
        isClicked = b;
    }


    // getter methods
    public boolean isUp()
    {
        return isUp;
    }

    public boolean isDown()
    {
        return isDown;
    }

    public boolean isRight()
    {
        return isRight;
    }

    public boolean isLeft()
    {
        return isLeft;
    }

    public boolean isClicked()
    {
        return isClicked;
    }


    /**
     * gets single instance of ActionTracker.
     * If none exists, create a new one
     * @return instance of ActionTracker
     */
    public static ActionTracker getInstance()
    {
        if (ActionTracker.instance == null)
            instance = new ActionTracker();

        return ActionTracker.instance;
    }

}
