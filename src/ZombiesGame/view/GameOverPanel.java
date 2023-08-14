package ZombiesGame.view;

import ZombiesGame.controller.GameInfo;
import ZombiesGame.messages.Message;
import ZombiesGame.messages.StartGameMessage;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

/**
 * GameOverPanel class that inherits from JPanel to define the layout of the GameOver screen
 * To be added to the main JFrame and create messages to transition/switch to other screens
 */
public class GameOverPanel extends JPanel {

    BlockingQueue<Message> queue;

    private Dimension dimensions;

    private JLabel scoreLabel;
    private JLabel highScoreLabel; //

    /**
     * constructor that defines the layout of the game over screen and action listener for 'Play Again' button
     * @param queue reference to queue to add message when 'Play Again' button is pressed
     * @param d screen dimensions to define preferred size
     *          and to format the layout of labels and buttons relative to size of screen
     */
    public GameOverPanel(BlockingQueue<Message> queue, Dimension d) {
        this.dimensions = d;
        this.queue = queue;

        // defines layout of this panel
        BoxLayout gameOverLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(gameOverLayout);
        setBackground(Color.BLACK);

        // panel to contain title text
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);

        // title text, formatted using html
        JLabel title = new JLabel();
        String labelText = String.format("<html><div style = 'text-align: center;' " +
                "'WIDTH=%d>%s</div></html>", dimensions.width/2, "GAME OVER");

        title.setText(labelText);
        title.setForeground(Color.RED);
        title.setFont(new Font("Serif", Font.PLAIN, dimensions.height/7));

        titlePanel.add(title);

        // SCORE LABEL: set color, font, size
        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Serif", Font.PLAIN, dimensions.height/20));

        // HIGH SCORE LABEL: set color, font, size
        highScoreLabel = new JLabel();
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setFont(new Font("Serif", Font.PLAIN, dimensions.height/20));

        // PLAY AGAIN BUTTON : set text, font, color
        JButton playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.setFont(new Font("Serif", Font.PLAIN, dimensions.height/20));
        playAgainButton.setForeground(Color.WHITE);

        // PLAY AGAIN BUTTON : REMOVES THE 3D LOOK OF THE BUTTON
        playAgainButton.setBorderPainted(true);
        playAgainButton.setFocusPainted(false);
        playAgainButton.setContentAreaFilled(false);

        // needed to center components, not cool
        titlePanel.setAlignmentX(CENTER_ALIGNMENT);
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        highScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        playAgainButton.setAlignmentX(CENTER_ALIGNMENT);

        // add all components to this panel
        add(Box.createRigidArea(new Dimension(0,dimensions.height/10)));
        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0,dimensions.height/25)));
        add(scoreLabel);
        add(Box.createRigidArea(new Dimension(0,dimensions.height/30)));
        add(highScoreLabel);
        add(Box.createRigidArea(new Dimension(0,dimensions.height/15)));
        add(playAgainButton);
        add(Box.createRigidArea(new Dimension(0, dimensions.height)));

        // PLAY AGAIN BUTTON: ACTION LISTENER
        playAgainButton.addActionListener(e -> {
            try {
                queue.put(new StartGameMessage());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
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
     * updates the score and high score text on the screen
     * @param info GameInfo class that contains data from the Model needed for updating the score
     *             (values of score and high score)
     */
    public void updateScore(GameInfo info)
    {
        scoreLabel.setText("Score: " + info.getScore());
        highScoreLabel.setText("High Score: " + info.getHighScore());
    }
}
