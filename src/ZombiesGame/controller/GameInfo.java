package ZombiesGame.controller;

import ZombiesGame.model.Entity;
import ZombiesGame.model.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * The GameInfo class contains the data from the model class that we want to bring to the View package.
 * It is created in the Controller class before passing it to the view
 */
public class GameInfo
{
    private LinkedList<Entity> entities;
    private int score;
    private int highScore;

    /**
     * Creates an instance of the GameInfo class
     * @param m a Model object
     */
    public GameInfo(Model m)
    {
        entities = m.getEntities();
        score = m.getScore();
        highScore = m.getHighScore();
    }


    /**
     * Gets the entities list
     * @return
     */
    public LinkedList<Entity> getEntityInfo()
    {
        return entities;
    }

    /**
     * Gets the score of the game
     * @return returns the score
     */
    public int getScore(){
        return score;
    }

    /**
     * Gets the high score of the game
     * @return returns the high score
     */
    public int getHighScore(){
        return highScore;
    }




}
