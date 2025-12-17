import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Enemy extends Flyings {
    int speed=2;
    Enemy () {
        img = new ImageIcon(getClass().getResource("/images/enemy.png")).getImage();
        w=50;
        h=50;
        x= ThreadLocalRandom.current().nextInt(0, 500 - w);
        y=0;
    }
    public void move()
    {
        y+=speed;
    }
}
