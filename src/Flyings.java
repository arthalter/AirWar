import java.awt.*;

abstract class Flyings {
    protected int x,y;
    protected int w,h;
    protected Image img;
    public abstract void move();
t
    public void Paint(Graphics g) {
        g.drawImage(img,x,y,null);
    }

    public boolean hit(Flyings f) {
        Rectangle r1=new Rectangle(x,y,w,h);
        Rectangle r2=new Rectangle(f.x,f.y,f.w,f.h);
        return r1.intersects(r2);
    }
}
