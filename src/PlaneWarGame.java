import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

// ==========================================
// 1. 主类：游戏控制与画布 (PlaneWarGame)
// ==========================================
public class PlaneWarGame extends JPanel {
    private Hero hero;
    private List<FlyingObject> flyings = new ArrayList<>(); // 存储敌机和子弹
    private List<Bullet> bullets = new ArrayList<>();       // 存储子弹
    private List<Enemy> enemies = new ArrayList<>();        // 存储敌机

    private int state; // 0:运行, 1:结束
    public static final int WIDTH = 480;
    public static final int HEIGHT = 700;
    public static BufferedImage planeImg;

    // 静态块加载图片
    static {
        try {
            // 请确保项目根目录下有 images/plane.png
            planeImg = ImageIO.read(new File("images/plane.png"));
        } catch (IOException e) {
            // 如果没图，创建一个简单的方块图代替，防止报错
            planeImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = planeImg.createGraphics();
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, 50, 50);
            g.dispose();
        }
    }

    public PlaneWarGame() {
        hero = new Hero();
        // 鼠标监听：英雄机跟随鼠标
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (state == 0) {
                    hero.moveTo(e.getX(), e.getY());
                }
            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
    }

    // 游戏主循环逻辑
    public void action() {
        new Thread(() -> {
            int time = 0;
            while (true) {
                if (state == 0) {
                    // 1. 生成敌机 (每400毫秒)
                    if (time % 40 == 0) enemies.add(new Enemy());
                    // 2. 发射子弹 (每200毫秒)
                    if (time % 20 == 0) bullets.add(hero.shoot());

                    // 3. 移动
                    bullets.forEach(Bullet::step);
                    enemies.forEach(Enemy::step);

                    // 4. 碰撞检测与清理
                    checkCollisions();
                    cleanOutOfBounds();
                }

                // 重绘界面
                repaint();
                try { Thread.sleep(10); } catch (InterruptedException e) {}
                time++;
            }
        }).start();
    }

    // 碰撞检测
    private void checkCollisions() {
        // 遍历子弹和敌机
        Iterator<Bullet> bit = bullets.iterator();
        while (bit.hasNext()) {
            Bullet b = bit.next();
            Iterator<Enemy> eit = enemies.iterator();
            while (eit.hasNext()) {
                Enemy e = eit.next();
                if (e.shootBy(b)) { // 如果撞上了
                    eit.remove(); // 移除敌机
                    bit.remove(); // 移除子弹
                    break;
                }
            }
        }

        // 检查英雄机撞机
        for (Enemy e : enemies) {
            if (hero.hit(e)) {
                state = 1; // 游戏结束
                break;
            }
        }
    }

    // 清理越界的物体
    private void cleanOutOfBounds() {
        bullets.removeIf(b -> b.y < 0);
        enemies.removeIf(e -> e.y > HEIGHT);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // 清空背景
        setBackground(Color.BLACK); // 设置黑色背景

        // 画英雄机
        hero.paintObject(g);

        // 画敌机
        for (Enemy e : enemies) e.paintObject(g);

        // 画子弹
        for (Bullet b : bullets) b.paintObject(g);

        // 画状态
        if (state == 1) {
            g.setColor(Color.RED);
            g.setFont(new Font("Verdana", Font.BOLD, 30));
            g.drawString("GAME OVER", 140, 300);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("简单飞机大战");
        PlaneWarGame game = new PlaneWarGame();
        frame.add(game);
        frame.setSize(WIDTH, HEIGHT);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.action(); // 开始游戏
    }
}

// ==========================================
// 2. 抽象父类 (FlyingObject)
// ==========================================
abstract class FlyingObject {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage image;

    // 移动方法，交给子类实现
    public abstract void step();

    // 绘制方法
    public void paintObject(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        }
    }

    // 碰撞检测：判断两个矩形是否相交
    public boolean hit(FlyingObject other) {
        Rectangle r1 = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle r2 = new Rectangle(other.x, other.y, other.width, other.height);
        return r1.intersects(r2);
    }

    // 专门用于子弹打敌机的辅助判断
    public boolean shootBy(Bullet b) {
        return this.hit(b);
    }
}

// ==========================================
// 3. 英雄机类 (Hero)
// ==========================================
class Hero extends FlyingObject {
    public Hero() {
        this.image = PlaneWarGame.planeImg;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = 200;
        this.y = 500;
    }

    @Override
    public void step() {
        // 英雄机由鼠标控制，不需要自动移动逻辑
    }

    public void moveTo(int x, int y) {
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }

    public Bullet shoot() {
        // 子弹从飞机正上方发射
        return new Bullet(this.x + this.width / 2 - 2, this.y - 10);
    }
}

// ==========================================
// 4. 敌机类 (Enemy)
// ==========================================
class Enemy extends FlyingObject {
    private int speed = 2;

    public Enemy() {
        this.image = PlaneWarGame.planeImg; // 简化：敌机暂时用同样的图，也可以加载不同的
        this.width = 40; // 敌机稍微小一点
        this.height = 40;
        this.x = new Random().nextInt(PlaneWarGame.WIDTH - width);
        this.y = -height; // 从屏幕上方生成
    }

    @Override
    public void step() {
        y += speed; // 向下移动
    }

    // 重写绘制，给敌机加个红色滤镜区分，或者翻转图片
    @Override
    public void paintObject(Graphics g) {
        // 如果没有单独敌机图，就用方块或者原图绘制
        super.paintObject(g);
    }
}

// ==========================================
// 5. 子弹类 (Bullet)
// ==========================================
class Bullet extends FlyingObject {
    private int speed = 5;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 4;   // 宽
        this.height = 10; // 高
        this.image = null; // 子弹没有图片，用形状画
    }

    @Override
    public void step() {
        y -= speed; // 向上移动
    }

    @Override
    public void paintObject(Graphics g) {
        // 【需求实现】子弹用长方形代替
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }
}