package ZombiesGame.view;

import ZombiesGame.controller.GameInfo;
import ZombiesGame.messages.FirstScreenMessage;
import ZombiesGame.messages.StartGameMessage;
import ZombiesGame.messages.Message;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;

/**
 * StartPanel that inherits from JPanel
 * Defines the layout of the start screen, uses action listeners to create messages for switching screens
 * To be added to main JFrame
 */
public class StartPanel extends JPanel{

    private Dimension dimensions;
    private BlockingQueue<Message> queue;

    private JLabel highScoreLabel;

    /**
     * constructor that defines the layout of the game over screen and action listener for 'Play' button
     * @param queue reference to message queue to add message when 'Play' button is pressed
     * @param d screen dimensions to define preferred size
     *          and to format the layout of labels and buttons relative to size of screen
     */
    public StartPanel(BlockingQueue<Message> queue, Dimension d){
        this.queue = queue;
        this.dimensions = d;

        // sets layout manager to boxlayout
        BoxLayout startLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(startLayout);
        setBackground(Color.BLACK);

        // panel to contain title text
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);

        // title text stored in jlabel, formatted using html
        JLabel title = new JLabel();
        String labelText = String.format("<html><div style = 'text-align: center;' " +
                "'WIDTH=%d>%s</div></html>", dimensions.width/2, "PEW PEW");

        title.setText(labelText);
        title.setForeground(Color.RED);
        title.setFont(new Font("Serif", Font.PLAIN, dimensions.height/7));

        titlePanel.add(title);


        // HIGH SCORE TEXT: FONT SIZE, COLOR
        highScoreLabel = new JLabel();
        highScoreLabel.setForeground(Color.WHITE);
        highScoreLabel.setFont(new Font("Serif", Font.PLAIN, dimensions.height/20));


        // START BUTTON: SETS FONT STYLE, SIZE, COLOR
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Serif", Font.PLAIN, dimensions.height/20));
        startButton.setForeground(Color.WHITE);


        // START BUTTON : REMOVES THE 3D LOOK OF THE BUTTON
        startButton.setBorderPainted(true);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);

        // required to center components, very lame
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add all components to this panel
        add(Box.createRigidArea(new Dimension(0,dimensions.height/10)));
        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0,dimensions.height/15)));
        add(highScoreLabel);
        add(Box.createRigidArea(new Dimension(0,dimensions.height/15)));
        add(startButton);
        add(Box.createRigidArea(new Dimension(0, dimensions.height)));

        // when initializing this panel, create message to update label text and display high score label
        try {
            queue.put(new FirstScreenMessage());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        // START BUTTON: ACTION LISTENER
        startButton.addActionListener(e -> {
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
     * updates the text of the high score label
     * @param info GameInfo class that contains data needed in the view, used to get current high score
     */
    public void updateScore(GameInfo info)
    {
        highScoreLabel.setText("High Score: " + info.getHighScore());
    }
}
