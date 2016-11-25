package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author leonardo
 */
public class SaucerShot extends Obj {
    
    private long startTime;
    private double vx, vy;
    
    public SaucerShot(AsteroidsGame game, double x, double y, double angle) {
        super(game);
        this.x = x;
        this.y = y;
        this.angle = angle;
        shape = new Ellipse2D.Double(-3, -3, 6, 6);
        vx = 5 * Math.cos(angle);
        vy = 5 * Math.sin(angle);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        x += vx;
        y += vy;
        
        x = x < -1 ? game.getWidth() : x;
        x = x > game.getWidth() + 1 ? -1 : x;
        y = y < -1 ? game.getHeight() : y;
        y = y > game.getHeight() + 1 ? -1 : y;

        destroyed |= System.currentTimeMillis() - startTime > 2000;
    }
    
    public void hit() {
        destroyed = true;
    }
    
}
