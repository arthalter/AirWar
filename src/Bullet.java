
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
public class Bullet extends Flyings{
    //子弹是一个宽5长20的长方形
    int speed=5;
    Bullet(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.w = 5;
        this.h = 20;
        //子弹颜色为黄色矩形
    }
    public void move() {
        y -= speed;
    }
    @Override
    public void Paint(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, w, h);
    }
}
