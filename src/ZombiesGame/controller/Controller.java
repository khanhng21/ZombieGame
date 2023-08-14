package ZombiesGame.controller;


import ZombiesGame.messages.*;
import ZombiesGame.model.*;
import ZombiesGame.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * The Controller classes connects the game by having direct communication to the model and the view. It is responsible for changing the state of the game for
 * every message that has been passed to this class. The state of the game means the positions of all the entities present in the game, and the model data corresponding
 * to these entities. It also means the state of the game whether the game should enter the start screen, game screen, or the game over screen.
 */
public class Controller
{
    private BlockingQueue<Message> queue;
    private Model model;
    private View view;

    private List<Valve> valves = new LinkedList<>();


    /**
     * Creates an instance of the Controller class
     * @param queue the queue that contains all the messages passed from the view
     * @param model the model class that handles all the model objects in the game
     * @param view the view class that handles all the view objects in the game
     */
    public Controller(BlockingQueue<Message> queue, Model model, View view)
    {
        this.queue  = queue;
        this.model  = model;
        this.view   = view;

        valves.add(new NewGameValve());
        valves.add(new CreateProjectileValve());
        valves.add(new CreateEnemyValve());
        valves.add(new UpdateEntitiesValve());
        valves.add(new StartGameValve());
        valves.add(new FirstScreenValve());
    }

    /**
     * The function that loops over the queue and checks whether the message can executed by any of the valves in the list
     * @throws Exception
     */
    public void mainLoop() throws Exception
    {
        ValveResponse response = ValveResponse.EXECUTED;

        Message message = null;
        while (response != ValveResponse.FINISH)
        {
            try
            {
                message = queue.take();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            for (Valve valve : valves)
            {
                response = valve.execute(message);
                // if successfully processed or game over, leave the loop
                if (response != ValveResponse.MISS)
                    break;
            }
        }
    }


    /**
     * One of the valves that the controller checks whether the message from the queue is from the class "NewGameMessage"
     */
    private class NewGameValve implements Valve
    {
        /**
         * Calls the initialization/reset of the game state and data, and the starting position of player. It updates the view.
         * @param message the message pass from the view along with data to properly reset game state and data
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message) {
            if (message.getClass() != NewGameMessage.class)
            {
                return ValveResponse.MISS;
            }

            NewGameMessage m = (NewGameMessage) message;

            // reset game state
            model.createNewGame(m.getWidth(),m.getHeight(),m.getSpriteSize());

            model.createPlayer();
            // send render data to View
            GameInfo data = model.getGameStatus();
            view.updateView(data);

            return ValveResponse.EXECUTED;
        }
    }

    /**
     * One of the valves that the controller checks whether the message from the queue is from the class "CreateProjectileMessage"
     */
    private class CreateProjectileValve implements Valve
    {
        /**
         * Calls the function that creates a projectile whenever the mouse was clicked. It sends new updates back to the view for rendering the bullet
         * @param message the message passed from the view that contains the mouse coordinates to create the projectile
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message)
        {
            if (message.getClass() != CreateProjectileMessage.class)
            {
                return ValveResponse.MISS;
            }

            CreateProjectileMessage m = (CreateProjectileMessage) message;

            model.createProjectile(m.getMousePosition());
            GameInfo data = model.getGameStatus();
            view.updateView(data);

            return ValveResponse.EXECUTED;
        }
    }

    /**
     * One of the valves that the controller checks whether the message from the queue is from the class "CreateEnemyMessage"
     */
    private class CreateEnemyValve implements Valve
    {
        /**
         * Calls the functions that creates the enemy, and it sends new updates back to the view for rendering the enemy
         * @param message the message passed from the view to execute the following function
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message)
        {
            if (message.getClass() != CreateEnemyMessage.class)
            {
                return ValveResponse.MISS;
            }

            CreateEnemyMessage m = (CreateEnemyMessage) message;

            model.createEnemy();
            GameInfo data = model.getGameStatus();
            view.updateView(data);

            return ValveResponse.EXECUTED;
        }
    }


    /**
     * One of the valves that the controller checks whether the message from the queue is from the class "UpdateEntitiesMessage"
     */
    private class UpdateEntitiesValve implements Valve
    {
        /**
         * Calls the functions responsible for updating the positions of the entities in the view, and the functions that handles collision.
         * It is also responsible for switching the state of the game when the player is hit by an enemy.
         * @param message passed from the view to execute the following function
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message) {
            if (message.getClass() != UpdateEntitiesMessage.class) {
                return ValveResponse.MISS;
            }

            UpdateEntitiesMessage m = (UpdateEntitiesMessage) message;

            model.updateEntities();
            model.checkCollisions();
            model.checkBoundaryCollisions();

            // *player is always assumed to be first entity added
            Entity player = model.getEntities().getFirst();
            if (!player.isActive())
            {
                 model.updateHighScore();
                 GameInfo info = model.getGameStatus();
                 view.updateScore(info);
                 view.switchPanel("gameOverPanel");
            }
            model.removeInactive();

            GameInfo data = model.getGameStatus();
            view.updateView(data);
          
            return ValveResponse.EXECUTED;
        }
    }


    /**
     * Valve that handles messages of type StartGameMessage
     */
    private class StartGameValve implements Valve
    {
        /**
         * Switches the game to its game screen. This is called when the user interacts with buttons in the Start or Game Over screen
         * It also updates the high score of the player
         * @param message the message passed from the view to execute the following function
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message) {
            if (message.getClass() != StartGameMessage.class)
            {
                return ValveResponse.MISS;
            }

            StartGameMessage m = (StartGameMessage) message;

            model.updateHighScore();
            GameInfo info = model.getGameStatus();
            view.updateScore(info);
            view.updateView(info);
            view.switchPanel("gamePanel");

            return ValveResponse.EXECUTED;
        }
    }


    /**
     * One of the valves that the controller checks whether the message from the queue is from the class "FirstScreenMessage"
     */
    private class FirstScreenValve implements Valve
    {
        /**
         * Updates the score on the start screen when the game first starts
         * @param message the message passed from the view to execute the following function
         * @return a return value whether the valve response is either ignored or executed
         */
        @Override
        public ValveResponse execute(Message message) {
            if (message.getClass() != FirstScreenMessage.class)
            {
                return ValveResponse.MISS;
            }

            FirstScreenMessage m = (FirstScreenMessage) message;

            model.updateHighScore();
            GameInfo info = model.getGameStatus();
            view.updateScore(info);

            return ValveResponse.EXECUTED;
        }
    }
}
