package ZombiesGame.model;

import ZombiesGame.controller.GameInfo;
import ZombiesGame.view.ActionTracker;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;

/**
 *  Model class:
 *  stores all game data - entities, positions, scores, game states, etc.
 *  handles all logic - velocity calculations, collision calculations and logic, updates to entity states,
 *  removing entities, updating score and difficulty
 */
public class Model
{
    // screen dimensions and size of sprites for position calculations
    private int screenWidth;
    private int screenHeight;
    private int spriteSize;

    // variables for enemy movement and creation
    private int enemySpeed;
    private int maxEnemies;
    private int currentEnemies;

    // score data
    private int score;
    private int highScore;
    private File scoreFile;

    // point values
    private static final int NORMAL_POINT   = 5;
    private static final int ITEM_POINT     = 50;

    // difficulty levels defined by point specific thresholds
    private final int EASY                  = 150;
    private final int NORMAL                = 250;
    private final int HARD                  = 500;

    private final LinkedList<Entity> entities   = new LinkedList<>(); // list of entities that are on the field
    private final Random r                      = new Random();


    /**
     *  model constructor: generates a new file that stores highscore upon first time running program
     */
    public Model()
    {
        try {
            scoreFile = new File("score.txt");
            if (scoreFile.createNewFile()) {
                System.out.println("File created: " + scoreFile.getName());
                saveScoreToFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * creates a new game/restarts game by resetting game state
     *
     * @param width width of screen
     * @param height height of screen
     * @param spriteSize size of the object sprites
     */
    public void createNewGame(int width, int height, int spriteSize)
    {
        this.screenWidth        = width;
        this.screenHeight       = height;
        this.spriteSize         = spriteSize;

        this.maxEnemies         = 4; // reset enemy limit
        this.enemySpeed         = 4; // reset enemy speed
        this.currentEnemies     = 0; // reset current enemy count

        this.score              = 0; // reinitialize score to 0

        entities.clear();
    }


    /**
     * creates a new player in the center of the screen,
     * calculated using screen width, screen height, and pixel size
     * adds to active game entities -> player always assumed to be at index 0 (first entity added)
     */
    public void createPlayer()
    {
        // calculate coordinates of the center of the screen, accounting for sprite size of player
        int startX = (screenWidth/2) - (spriteSize/2);
        int startY = (screenHeight/2) - (spriteSize/2);

        // new player created at the positions, added to entities
        entities.add(new Player(startX, startY));
    }


    /**
     * creates a new projectile at the position of the player
     * calculates and sets a predefined trajectory using player and mouse position
     * adds to active game entities
     *
     * @param mousePos position of the mouse when mouse click or press occurs
     */
    public void createProjectile(Point mousePos)
    {
        // projectile velocity and position calculated upon creation, no need to change it afterwards
        Entity player   = entities.getFirst();
        int deltaX      = mousePos.x - player.getX(); // difference in x pos between mouse and player (x1 - x0)
        int deltaY      = mousePos.y - player.getY(); // difference in y pos between mouse and player (y1 - y0)

        int k         = 25; // speed/magnitude
        double theta  = Math.toDegrees(Math.atan2(deltaY, deltaX));
        theta         = Math.toRadians(theta); // direction defined in radians (relative to player)

        // Velocity : change in x and change in y per call to translate
        double dx   = k * Math.cos(theta);
        double dy   = k * Math.sin(theta);

        // new projectile created based on position of player, add to entities
        entities.add(new Projectile(player, (int) dx, (int) dy));
    }


    /**
     * creates a new enemy at a random position along the edge of the screen(left, right, top, or bottom border)
     * adds to game, increments enemy counter
     */
    public void createEnemy()
    {
        // default x and y positions
        int x = 0;
        int y = 0;

        // check to make sure # enemies in the game doesn't exceed max
        if (currentEnemies < maxEnemies)
        {
            // randomly generated boolean that determines whether to create the enemy along the x or y-axis of screen
            boolean isOnXAxis = r.nextBoolean();

            if (isOnXAxis) {
                // generates random x-coords, taking into account the size of the sprite (64-960)
                x = r.nextInt(screenWidth - spriteSize) + spriteSize;

                // random boolean to determine whether the enemy spawns on the bottom or top of screen
                boolean isOnBottom = r.nextBoolean();

                if (isOnBottom) {
                    // sets y to be the very bottom of the screen, else defaults to top of the screen(0)
                    y = screenHeight;
                }
            }
            else
            {
                // generates random y-coords, taking into account size of sprite (64-704)
                y = r.nextInt(screenHeight - (spriteSize)) + spriteSize;

                // determines whether the enemy will spawn on the right or left side of the screen
                boolean isOnRight = r.nextBoolean();

                if (isOnRight)
                {
                    // sets y to be on right of the screen, else defaults to left of the screen
                    x = screenWidth;
                }
            }

            entities.add(new Enemy(x, y)); // creates new enemy using generated x and y coords, adds to game
            currentEnemies++; // increment enemy counter
        }
    }


    /**
     * updates the player velocity based on which keys are being pressed
     */
    public void updatePlayerVelocity()
    {
        Player player               = (Player) entities.getFirst();
        ActionTracker keysPressed   = ActionTracker.getInstance();
        int speed                   = 7;

        // when either both left and right aren't being pressed, or when both are being pressed -- x velocity is 0
        // else only one of the two is being pressed, update x velocity accordingly
        if (!(keysPressed.isLeft() || keysPressed.isRight()) || (keysPressed.isLeft() && keysPressed.isRight()))
        {
            player.dx = 0;
        }
        else if (keysPressed.isLeft())
        {
            player.dx = -speed;
        }
        else if (keysPressed.isRight())
        {
            player.dx = speed;
        }

        // when either both up and down aren't being pressed, or when both are being pressed -- y velocity is 0
        // else only of the two is pressed, update y velocity accordingly
        if (!(keysPressed.isDown() || keysPressed.isUp()) || (keysPressed.isDown() && keysPressed.isUp()))
        {
            player.dy = 0;
        }
        else if (keysPressed.isUp())
        {
            player.dy = -speed;
        }
        else if (keysPressed.isDown())
        {
            player.dy = speed;
        }
    }


    /**
     * updates the velocity of a given enemy
     * recalculates velocity so that enemy moves towards player
     *
     * @param enemy Enemy object whose velocity will be updated
     */
    private void updateEnemyVelocity(Entity enemy)
    {
        // checks to make sure collision isn't occurring (velocity calculated differently under collision)
        if (!enemy.isColliding)
        {
            // updates enemy velocity using same calculations as projectile velocity calculation
            Entity player   = entities.getFirst();
            int deltaX      = player.getX() - enemy.getX();
            int deltaY      = player.getY() - enemy.getY();

            double theta  = Math.toDegrees(Math.atan2(deltaY, deltaX));
            theta         = Math.toRadians(theta);

            // Velocity : change in x and y per update call, will differ depending on difficulty
            double dx   = enemySpeed * Math.cos(theta);
            double dy   = enemySpeed * Math.sin(theta);

            // set velocity
            enemy.dx = (int) dx;
            enemy.dy = (int) dy;
        }
    }


    /**
     * updates all entity positions and player and enemy velocities
     */
    public void updateEntities()
    {
        for(Entity e : entities)
        {
            e.translate(); // updates entity positions

            // update velocities if entity is player or zombie
            if (e.getClass() == Player.class)
            {
                updatePlayerVelocity();
            }
            else if (e.getClass() == Enemy.class)
            {
                updateEnemyVelocity(e);
            }
        }
    }


    /**
     * updates the current score using given parameter
     * if total score reaches a certain point, update the difficulty
     *
     * @param scoreValue amount that the total score is incremented by
     */
    public void updateScore(int scoreValue)
    {
        this.score += scoreValue; // add to score

        // if score reaches a certain threshold, increase difficulty
        if (score > HARD)
        {
            updateDifficulty(11, 6);
        }
        else if (score > NORMAL)
        {
            updateDifficulty(10,6);
        }
        else if(score > EASY)
        {
            updateDifficulty(6, 5);
        }
    }


    /**
     * updates the difficulty by changing the max enemies allowed on the screen, and the speed of enemy movement
     * @param maxEnemies the max number of enemies that are allowed to exist at a time
     * @param enemySpeed the speed of the enemies (amount that they move per update call)
     */
    private void updateDifficulty(int maxEnemies, int enemySpeed)
    {
        this.maxEnemies = maxEnemies;
        this.enemySpeed = enemySpeed;
    }


    /**
     * saves high score to file "score.txt" by writing to the file
     */
    public void saveScoreToFile()
    {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(scoreFile));
            bw.write(String.valueOf(score));
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**
     * reads the high score
     * @return String value containing the high score stored in file
     */
    public String checkScoreInFile()
    {
        String firstLine = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(scoreFile));
            firstLine = br.readLine();
            //System.out.println("Read score: " + firstLine);
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return firstLine;
    }


    /**
     * update the high score in file if current score is higher than previously recorded score
     * updates the high score variable
     */
    public void updateHighScore()
    {
        String keptScore = checkScoreInFile();
        if (getScore() > Integer.parseInt(keptScore)){
            saveScoreToFile();
            System.out.println("New high score is " + getScore() + ". Previous high score was " + keptScore);
            highScore = getScore();
        }
        else {
            highScore = Integer.parseInt(keptScore);
        }
    }


    /**
     * checks collisions between every entity in the game
     * makes appropriate changes to game states when collision occurs between specific entities
     * (velocities, score, alive/dead state)
     */
    public void checkCollisions()
    {
        Entity e1;
        Entity e2;

        // reset collision states for active entities
        for (Entity e : entities)
        {
            e.isColliding = false;
        }

        // check for collisions
        for(int i = 0; i < entities.size(); i++)
        {
            e1 = entities.get(i); // entity A

            // once collision is checked for A, no need to check that specific entity again until next call
            for (int j = i + 1; j < entities.size(); j++)
            {
                e2 = entities.get(j); // the rest of the entities (B)

                // check for collision between A and B
                if(e1.collidesWith(e2))
                {
                    if (e1.getClass() == Player.class && e2.getClass() == Enemy.class)
                    {
                        // if player collides with entity, set player to inactive(dead)
                        e1.setInactive();
                    }
                    else if (e1.getClass() == Player.class && e2.getClass() == Item.class)
                    {
                        // if player collides with any items, update current score, set item to inactive
                        updateScore(ITEM_POINT);
                        e2.setInactive();
                    }
                    else if (e1.getClass() == Enemy.class && e2.getClass() == Enemy.class)
                    {
                        // if both are enemies -- do very complicated calculations

                        e1.isColliding = true;
                        e2.isColliding = true;

                        // collision vectors(x and y component)
                        int colVectorX = e2.getX() - e1.getX();
                        int colVectorY = e2.getY() - e1.getY();

                        // calculate distance
                        double distance = (float) Math.sqrt((colVectorX * colVectorX) + (colVectorY * colVectorY));

                        // calculate normalized collision vector (direction)
                        double normColVectorX = colVectorX / distance;
                        double normColVectorY = colVectorY / distance;

                        // relative velocity vector
                        int relVelX = e1.dx - e2.dx;
                        int relVelY = e1.dy - e2.dy;
                        double speed = (relVelX * normColVectorX) + (relVelY * normColVectorY);

                        if (speed < 0)
                        {
                            break;
                        }

                        // calculate and assign new velocities to enemies
                        e1.dx -= (speed * normColVectorX);
                        e1.dy -= (speed * normColVectorY);
                        e2.dx += (speed * normColVectorX);
                        e2.dy += (speed * normColVectorY);
                    }
                    else if ((e1.getClass() == Enemy.class && e2.getClass() == Projectile.class)
                            || (e1.getClass() == Projectile.class && e2.getClass() == Enemy.class))
                    {
                        // if an enemy collides with a projectile or a projectile collides with an enemy
                        // (depends on which is checked first), update score, set both to inactive
                        updateScore(NORMAL_POINT);
                        e1.setInactive();
                        e2.setInactive();
                    }
                }
            }
        }
    }


    /**
     * Checks if entities collide with boundaries, execute changes to their states if collision occurs
     * (change their velocities or set them to inactive)
     */
    public void checkBoundaryCollisions()
    {
        // checks all active entities
        for (Entity e : entities)
        {
            Class type = e.getClass(); // is the entity a player, enemy, projectile, or item?

            if (type == Player.class)
            {
                if ((e.dx < 0 && e.x < 0) || (e.dx > 0 && e.x >= screenWidth - e.getHitBox().width))
                {
                    // if player is moving left and hits left wall
                    // or if player is moving right and hits right wall -> stop (x-velocity = 0)
                    e.dx = 0;
                }

                if ((e.dy < 0 && e.y < 0) || (e.dy > 0 && e.y >= screenHeight - e.getHitBox().height))
                {
                    // if player is moving up and hits top wall
                    // or if player is moving down and hits lower wall -> stop (y-velocity = 0)
                    e.dy = 0;
                }
            }
            else
            {
                // if x position exceeds the left or right boundaries of the screen,
                // set to inactive if it's a projectile, reverse velocity if enemy
                if (e.x < 0 || e.x > screenWidth)
                {
                    if (type == Projectile.class)
                    {
                        e.setInactive();
                    }
                    else
                    {
                        e.dx *= -1;
                    }
                }

                // if y position exceeds top or bottom boundary,
                // set to inactive if projectile, reverse y-velocity if enemy
                if (e.y < 0 || e.y > screenHeight)
                {
                    if (type == Projectile.class)
                    {
                        e.setInactive();
                    }
                    else
                    {
                        e.dy *= -1;
                    }
                }
            }

            // in-case a game object slips through the border checks, removes it
            // the smaller the margin, the easier it is for them to be removed
            int errorMargin = 15;

            if ((e.x < -errorMargin || e.x > screenWidth + errorMargin) ||
                    (e.y < -errorMargin || e.y > screenHeight + errorMargin))
            {
                // don't remove if player (just in case)
                if (e.getClass() != Player.class)
                    e.setInactive();
            }
        }
    }



    /**
     * remove all entities that were set as inactive during collision checks (removes them from the game)
     */
    public void removeInactive()
    {
        LinkedList<Entity> inactiveEntities = new LinkedList<>(); // entities to be removed
        LinkedList<Entity> itemsGenerated   = new LinkedList<>(); // items to be added
        int enemiesRemoved = 0; // number of enemies removed during the call

        // collects the list of "dead"/inactive game entities for removal
        for (Entity e : entities)
        {
            if(!e.isActive() && e.getClass() != Player.class)
            {
                inactiveEntities.add(e);

                // if entity is an enemy, increment enemies removed counter, have chance of creating item
                if (e.getClass() == Enemy.class)
                {
                    enemiesRemoved++;

                    // randomly generate items for each enemy killed
                    // probability of item generating is 1/12
                    boolean itemIsGenerated = r.nextInt(12) == 0;

                    if (itemIsGenerated)
                    {
                        itemsGenerated.add(new Item(e)); // create new item at position of enemy
                    }

                }
            }
        }

        currentEnemies -= enemiesRemoved; // update current enemy counter
        entities.removeAll(inactiveEntities); // remove "dead" entities from game
        entities.addAll(itemsGenerated); // add all generated items to game
    }


    /**
     * gets current score
     * @return current score
     */
    public int getScore()
    {
        return score;
    }


    /**
     * gets current high score
     * @return current high score
     */
    public int getHighScore()
    {
        return highScore;
    }


    /**
     * gets the list of entities that are active(for drawing in the view)
     * @return list containing currently active entities
     */
    public LinkedList<Entity> getEntities()
    {
        return new LinkedList<>(entities);
    }


    /**
     * gets info from model necessary for drawing in the view
     * @return GameInfo class that stores necessary info from model to draw in view
     */
    public GameInfo getGameStatus()
    {
        return new GameInfo(this);
    }


    /**
     * used for testing model class only
     * @param e Entity to be added to list of active entities
     */
    public void addEntity(Entity e){
        entities.add(e);
    }
}



