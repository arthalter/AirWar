import javax.swing.*;

public class Player extends Flyings {
    Player () {
        img = new ImageIcon(getClass().getResource("/images/plane.png")).getImage();
        w=50;
        h=50;
    }
    @Override
    public void move() {
        // Player movement logic here
    }
    void moveTo(int mx, int my) {
        x = mx - w / 2;
        y = my - h / 2;
    }

}
