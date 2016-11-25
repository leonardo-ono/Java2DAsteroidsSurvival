package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.Rectangle;

/**
 *
 * @author leonardo
 */
public class Spark extends Obj {
    
    private long startTime;
    private long lifeTime;
    private double vx, vy;
    
    public Spark(AsteroidsGame game, double x, double y) {
        super(game);
        this.x = x;
        this.y = y;
        this.angle = (Math.PI * 2) * Math.random();
        shape = new Rectangle(-1, -1, 2, 2);
        double v = 0.5 + 1 * Math.random();
        vx = 2 * Math.cos(angle) * v;
        vy = 2 * Math.sin(angle) * v;
        startTime = System.currentTimeMillis();
        lifeTime = (long) (250 + 250 * Math.random());
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
        
        x = x < -1 ? game.getWidth() : x;
        x = x > game.getWidth() + 1 ? -1 : x;
        y = y < -1 ? game.getHeight() : y;
        y = y > game.getHeight() + 1 ? -1 : y;
        
        destroyed = System.currentTimeMillis() - startTime > lifeTime;
    }
    
}
