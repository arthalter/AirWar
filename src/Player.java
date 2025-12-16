import javax.swing.*;

public class Player extends Flyings {
    Player () {
        img = new ImageIcon("images/plane.png").getImage();
        w = img.getWidth(null);
        h = img.getHeight(null);
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
