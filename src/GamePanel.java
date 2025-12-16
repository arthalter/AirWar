
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.GlyphMetrics;
import java.awt.image.*;
import java.util.*;
public class GamePanel extends JPanel {
    Player player=new Player();
    GamePanel() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                player.moveTo(e.getX(), e.getY());
            }
        });
    }
    void start()
    {
        new Thread(()->{
            int time=0;
            while(true) {
                time++;



                repaint();
                try {
                    Thread.sleep(10);
                }
                catch ( InterruptedException e){}
            }
        }).start();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setBackground(Color.BLACK);
        player.Paint(g);
    }
}
