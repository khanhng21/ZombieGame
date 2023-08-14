import ZombiesGame.model.Player;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTester
{
    @Test
    public void testHitBox()
    {
        Player p = new Player(100, 100);

        Rectangle hitbox = p.getHitBox();

        boolean equalPosition = (p.getX()  == hitbox.x) && (p.getY() == hitbox.y);
        boolean equalDimensions = (hitbox.width == 64) && (hitbox.height == 64);

        assertTrue(equalPosition && equalDimensions,
                "expected output was true, actual value was false, positions not equal or hitbox not correct dimensions");
    }
}
