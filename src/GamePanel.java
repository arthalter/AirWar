
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
public class GamePanel extends JPanel {
    int state=0;
    Player player=new Player();
    java.util.List<Enemy> enemy = new ArrayList<>();
    java.util.List<Bullet> bullet = new ArrayList<>();
    GamePanel() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                player.moveTo(e.getX(), e.getY());
            }
        });
    }
    void check()
    {
        for(Enemy e:enemy)
        {
            if(player.hit(e))
            {
                state=1;
                System.out.println("1313");
                break;
            }
        }
        for (Bullet b:bullet)
        {
            for(Enemy e:enemy)
            {
                if(b.hit(e))
                {
                    e.y=1000;
                    b.y=-10;
                }
            }
        }
    }
    void clear()
    {
        enemy.removeIf(e->e.y>900);
        bullet.removeIf(b->b.y<0);
    }
    void start()
    {
        new Thread(()->{
            int time=0;
            while(true) {
                time++;
                if(state==0)
                {
                    if(time%40==0)
                        enemy.add(new Enemy());
                    if(time%20==0)
                        bullet.add(new Bullet(player.x,player.y));
                    enemy.forEach(e->e.move());
                    bullet.forEach(e->e.move());
                }
                check();
                clear();

                repaint();
                try {
                    Thread.sleep(10);
                }
                catch ( InterruptedException e){}
            }
        }).start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(100);
        if(state==0)
        {
            player.Paint(g);
            //enemy移动
            enemy.forEach(e->e.Paint(g));
            bullet.forEach(e->e.Paint(g));
        }else
        {
            //gameover界面
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 100, 400);
        }

    }
}
