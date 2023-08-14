import ZombiesGame.model.*;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelTester
{
    @Test
    public void testHitBox()
    {
        Player p = new Player(100, 100);

        Rectangle hitbox = p.getHitBox();

        boolean equalPosition = (p.getX()  == hitbox.x) && (p.getY() == hitbox.y);
        boolean equalDimensions = (hitbox.width == 64) && (hitbox.height == 64);

        assertTrue(equalPosition && equalDimensions,
                "Rectangle and player hitbox must be equal");
    }


    @Test
    public void testEnemyCount(){
        Model m = new Model();
        m.createNewGame(1024, 768, 64);
        m.createEnemy();
        m.createEnemy();

        int numOfEnemies = 0;

        for (Entity e : m.getEntities()){
            if (e.getClass() == Enemy.class){
                numOfEnemies ++;
            }

        }
        assertEquals(2, numOfEnemies, "Called create enemy function 2 times, enemy count must be 2");
    }


    @Test
    public void testItemScoreCount(){
        Model m = new Model();
        m.createNewGame(1024, 768, 64);
        m.createPlayer();

        Item item1 = new Item(m.getEntities().getFirst());
        m.addEntity(item1);

        m.checkCollisions();
        int currentScore = m.getScore();
        assertEquals(50, currentScore, "When player collides with an item, score should increment by 50");
    }


    @Test
    public void testEnemyScoreCount(){
        Model m = new Model();
        m.createNewGame(1024, 768, 64);
        m.createPlayer();

        Projectile projectile = new Projectile(m.getEntities().getFirst(), 100, 200);
        Enemy enemy = new Enemy(projectile.getX(),  projectile.getY());

        m.addEntity(enemy);
        m.addEntity(projectile);

        m.checkCollisions();
        int currentScore = m.getScore();
        assertEquals(5, currentScore, "When a projectile collides with an enemy, score should increment by 5");
    }


    @Test
    public void testHighScoreInFile(){
        Model m = new Model();
        m.updateHighScore();
        int highScore = m.getHighScore();

        int highScoreInFile = Integer.parseInt(m.checkScoreInFile());

        assertTrue(highScore == highScoreInFile, "Checks if the high score on file is equal to the high score");
    }
}
