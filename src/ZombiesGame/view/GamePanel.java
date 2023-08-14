package ZombiesGame.view;


import ZombiesGame.controller.GameInfo;
import ZombiesGame.messages.*;
import ZombiesGame.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

/**
 * GamePanel class that inherits from JPanel
 * defines layout of active game screen(when playing is in progress) and handles drawing and updating of components
 * (player, enemies, projectiles)
 * handles key inputs and button presses, regularly creates messages to animate movement
 */
public class GamePanel extends JPanel
{
    private BlockingQueue<Message> queue;
    private ActionTracker keysPressed;

    // timers for movement, entity creation
    private final Timer animationTimer;
    private final Timer projectileTimer;
    private final Timer enemyTimer;

    // data for drawing
    private LinkedList<Entity> entities;
    private final SpriteData spriteData;
    private Dimension dimensions;
    private Point mousePosition;
    private int spriteSize;

    // labels for score and high score
    private final JLabel highScoreLabel;
    private final JLabel scoreLabel;


    /**
     * Default constructor that does a lot of stuff
     * initializes variables, adds listeners to this panel, creates timers to handle regular message creation
     * @param queue reference to message queue to add messages for game updates(movement)
     *              and creation of projectiles and enemies
     * @param spriteSize constant integer value that defines the base size of sprites for drawing image components
     *                   (player, enemy, projectile, items)
     * @param d dimensions of the game screen, used for drawing the background, initializing the game,
     *          getting preferred dimensions
     */
    public GamePanel(BlockingQueue<Message> queue, int spriteSize, Dimension d)
    {
        this.dimensions = d;
        this.spriteSize = spriteSize;
        keysPressed     = ActionTracker.getInstance();
        this.spriteData = new SpriteData();
        this.queue      = queue;

        addMouseMotionListener(new MouseDraggedHandler());
        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());

        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));

        highScoreLabel = new JLabel();
        highScoreLabel.setForeground(Color.BLACK);
        highScoreLabel.setFont(new Font("Serif", Font.PLAIN, 30));

        add(scoreLabel);
        add(highScoreLabel);

        // defines delay in message generation for game updates = ~60 refreshes/sec
        int REFRESH_DELAY = 1000 / 60;

        // defines delay in message generation for projectile creation = ~5 projectiles/sec
        int FIRE_RATE_DELAY = 1000 / 5;

        // defines delay in message generation for enemy creation = ~2 enemies/sec
        int SPAWN_DELAY = 1000 / 25;

        // timer that should handle all animations(movement)
        animationTimer = new Timer(REFRESH_DELAY, e -> {
            try {
                queue.put(new UpdateEntitiesMessage());

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        // timer for rate of projectile generation
        projectileTimer = new Timer(FIRE_RATE_DELAY, e -> {
            try {
                if (keysPressed.isClicked())
                    queue.put(new CreateProjectileMessage(mousePosition));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        // timer for rate of enemy creation
        enemyTimer = new Timer(SPAWN_DELAY, e -> {
            try {
                queue.put(new CreateEnemyMessage());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        this.setFocusable(true);
        this.setDoubleBuffered(true);
    }


    /**
     * starts a new game, starts timers
     */
    public void start()
    {
        // start/restart game
        try {
            queue.put(new NewGameMessage(dimensions.width, dimensions.height, spriteSize));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // starts timers for model and view updates, projectile enemy creation
        animationTimer.start();
        projectileTimer.start();
        enemyTimer.start();
    }


    /**
     * stops game by stopping timers, resetting ActionTracker
     */
    public void stop()
    {
        animationTimer.stop();
        projectileTimer.stop();
        enemyTimer.stop();

        // reset ActionTracker to prevent key states from carrying over to new game
        keysPressed.reset();
        keysPressed = ActionTracker.getInstance();
    }


    /**
     * updates list of active entities to draw, scores to display, redraw active entities
     * @param info GameInfo class that stores necessary info for drawing, score, high score, and active entities
     */
    public void updateView(GameInfo info)
    {
        this.entities = info.getEntityInfo();

        int highScore = info.getHighScore();
        int score = info.getScore();
        scoreLabel.setText("SCORE: " + score);
        highScoreLabel.setText("HIGH SCORE: " + highScore);

        repaint();
    }

    /**
     * gets preferred dimensions of the JPanel for sizing of the container(JFrame)
     * @return Dimension defined from the constants passed to this class from the JFrame
     */
    @Override
    public Dimension getPreferredSize()
    {
        return dimensions;
    }


    /**
     * draws all entities with specified sprite data at given position, depending on what type of entity they are
     * @param g does the drawing
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(spriteData.getBackground(), 0, 0, null); // draw background

        // makes sure entities list is assigned to something first
        if(entities != null)
        {
            for(Entity e: entities)
            {
                if (e.getClass() == Player.class)
                {
                    g2.drawImage(spriteData.getPlayerSprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                }
                else if (e.getClass() == Projectile.class)
                {
                    g2.drawImage(spriteData.getProjectileSprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                }
                else if (e.getClass() == Enemy.class)
                {
                    g2.drawImage(spriteData.getEnemySprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                }
                else if (e.getClass() == Item.class)
                {
                    if (((Item) e).getType() == 0){
                        g2.drawImage(spriteData.getCoffeeSprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                    }
                    if (((Item) e).getType() == 1){
                        g2.drawImage(spriteData.getMushroomSprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                    }
                    if (((Item) e).getType() == 2){
                        g2.drawImage(spriteData.getChickenSprite(), e.getX(), e.getY(), spriteSize, spriteSize, null);
                    }
                }
            }
        }
    }


    /**
     * Listens to key presses/releases and stores info in ActionTracker
     */
    private class KeyHandler extends KeyAdapter
    {
        // when specific keys are pressed, change states in ActionTracker to true
        @Override
        public void keyPressed(KeyEvent e)
        {
            int code = e.getKeyCode();

            if(code == KeyEvent.VK_W)
                keysPressed.setUp(true);
            if(code == KeyEvent.VK_A)
                keysPressed.setLeft(true);
            if(code == KeyEvent.VK_S)
                keysPressed.setDown(true);
            if(code == KeyEvent.VK_D)
                keysPressed.setRight(true);
        }

        // when specific keys are released, change states in ActionTracker to false
        @Override
        public void keyReleased(KeyEvent e)
        {
            int code = e.getKeyCode();

            if(code == KeyEvent.VK_W)
                keysPressed.setUp(false);
            if(code == KeyEvent.VK_A)
                keysPressed.setLeft(false);
            if(code == KeyEvent.VK_S)
                keysPressed.setDown(false);
            if(code == KeyEvent.VK_D)
                keysPressed.setRight(false);
        }
    }


    /**
     * Listens to mouse clicks, presses, and releases to create messages and update ActionTracker
     */
    private class MouseHandler extends MouseAdapter
    {
        // When mouse is clicked once, get position and create a new message for projectile creation
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                mousePosition = e.getPoint(); // gets position of the mouse at the time of click
                queue.put(new CreateProjectileMessage(mousePosition)); // creates new message to create new projectile
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        // When mouse click is held, get position and update ActionTracker
        @Override
        public void mousePressed(MouseEvent e)
        {
            //System.out.println("clicked at: " + mousePosition);
            mousePosition = e.getPoint();
            keysPressed.setClicked(true);
        }

        // when mouse is released, update ActionTracker
        @Override
        public void mouseReleased(MouseEvent e)
        {
            keysPressed.setClicked(false);
        }
    }


    /**
     * Listens to when mouse is dragged while click is held to constantly update mouse position
     */
    private class MouseDraggedHandler extends MouseMotionAdapter
    {
        // when mouse click is held, constantly update mouse position
        @Override
        public void mouseDragged(MouseEvent e)
        {
            mousePosition = e.getPoint();
        }
    }
}