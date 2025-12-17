import java.awt.*;
import javax.swing.*;
public class AirWar {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Air War");
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setSize(500, 900);//大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//可关闭
        frame.setLocationRelativeTo(null);//居中
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);//可见
        gamePanel.start();//12121
    }
}
