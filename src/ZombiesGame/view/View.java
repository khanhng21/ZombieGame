package ZombiesGame.view;

import ZombiesGame.controller.GameInfo;
import ZombiesGame.messages.Message;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

/**
 * Main View class that handles switching between different screens(sub-panels) and passing GameInfo to sub-panels
 */
public class View extends JFrame
{
    // dimension constants
    private final int SCALE             = 4;
    private final int BASE_TILE_SIZE    = 16;
    private final int TILE_SIZE         = SCALE * BASE_TILE_SIZE; // 64

    private final int WIDTH             = 16 * TILE_SIZE; // 1024
    private final int HEIGHT            = 12 * TILE_SIZE; // 768

    // layouts and panels
    private final CardLayout layout;
    private final JPanel cardPanel;
    private final StartPanel startPanel;
    private final GamePanel gamePanel;
    private final GameOverPanel gameOverPanel;

    // message queue that is passed to other sub-panels
    private BlockingQueue<Message> queue;


    /**
     * Constructor that sets up the layout manager, sub-panels, and sets frame to visible
     * @param title String value that determines the title of the JFrame
     * @param queue message queue for passing messages to the controller -- passed to sub-panels for more specific
     *              cases
     */
    public View(String title, BlockingQueue<Message> queue)
    {
        this.queue = queue;
        setTitle(title);

        layout = new CardLayout();

        // all defined panels
        cardPanel = new JPanel(layout); // main Panel that contains all sub-panels(screens)
        startPanel = new StartPanel(queue, new Dimension(WIDTH, HEIGHT));
        gamePanel = new GamePanel(queue, TILE_SIZE, new Dimension(WIDTH, HEIGHT));
        gameOverPanel = new GameOverPanel(queue, new Dimension(WIDTH, HEIGHT));

        // add sub-panels to main panel
        cardPanel.add(startPanel, "startPanel");
        cardPanel.add(gamePanel, "gamePanel");
        cardPanel.add(gameOverPanel, "gameOverPanel");

        // add main panel to Frame
        add(cardPanel);

        // do other things
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }


    /**
     * Switches between specified panels
     * @param panel String value that specifies the name assigned to each sub-panel
     *              -- used for switching between panels
     */
    public void switchPanel(String panel)
    {
        layout.show(cardPanel, panel);

        if (panel.equals("startPanel"))
        {
            startPanel.requestFocus();
        }
        else if (panel.equals("gamePanel"))
        {
            gamePanel.requestFocus();
            gamePanel.start();
        }
        else if (panel.equals("gameOverPanel"))
        {
            gameOverPanel.requestFocus();
            gamePanel.stop();
        }
    }


    /**
     * passes GameInfo to gamePanel to draw
     * @param info GameInfo class storing data from Model needed for drawing
     */
    public void updateView(GameInfo info)
    {
        gamePanel.updateView(info);
    }


    /**
     * passes GameInfo to startPanel and gameOverPanel to update scores displayed
     * @param info GameInfo class storing data from Model needed for drawing
     */
    public void updateScore(GameInfo info) {
        startPanel.updateScore(info);
        gameOverPanel.updateScore(info);
    }
}
