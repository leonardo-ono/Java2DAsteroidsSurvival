package game.obj;


import game.AsteroidsGame;
import game.AsteroidsGame.State;
import game.Obj;
import java.awt.Polygon;

/**
 *
 * @author leonardo
 */
public class Saucer extends Obj {
    
    public Ship ship;
    // size 1=small, 2=large
    public int size;
    public double targetX, targetY, vx, vy;
    public long hideStartTime;
    public long nextShowTime;
    
    public long nextStartShotTime;
    public long nextShotTime;

    public Saucer(AsteroidsGame game, Ship ship) {
        super(game);
        this.ship = ship;
        setupNew();
    }
    
    private void setShape() {
        Polygon saurcerPolygon = new Polygon();
        saurcerPolygon.addPoint(2, -1);
        saurcerPolygon.addPoint(1, -2);
        saurcerPolygon.addPoint(-1, -2);
        saurcerPolygon.addPoint(-2, -1);
        saurcerPolygon.addPoint(-4, 0);
        saurcerPolygon.addPoint(-2, 1);
        saurcerPolygon.addPoint(2, 1);
        saurcerPolygon.addPoint(4, 0);
        saurcerPolygon.addPoint(2, -1);
        saurcerPolygon.addPoint(-2, -1);
        saurcerPolygon.addPoint(-4, 0);
        saurcerPolygon.addPoint(4, 0);
        for (int p = 0; p < saurcerPolygon.npoints; p++) {
            saurcerPolygon.xpoints[p] *= (5 * size);
            saurcerPolygon.ypoints[p] *= (5 * size);
        }
        shape = saurcerPolygon;
    }

    @Override
    public void update() {
        if (game.getState() == State.TITLE) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        if (visible) {
            double dx = targetX - x;
            double dy = targetY - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 50) {
                targetX = game.getWidth() * Math.random();
                targetY = game.getHeight() * Math.random();
            }
            vx += (dx / dist) * 0.1;
            vy += (dy / dist) * 0.1;
            
            vx = vx > 2 ? 2 : vx < -2 ? -2 : vx;
            vy = vy > 2 ? 2 : vy < -2 ? -2 : vy;
            
            x += vx;
            y += vy;

            int halfSize = 10 * size;
            x = x < -halfSize ? game.getWidth() : x;
            x = x > game.getWidth() + halfSize ? -halfSize : x;
            y = y < -halfSize ? game.getHeight() : y;
            y = y > game.getHeight() + halfSize ? -halfSize : y;

            if (currentTime - nextStartShotTime > nextShotTime) {
                game.saucerShot(x, y);
                nextStartShotTime = System.currentTimeMillis();
                nextShotTime = 5000 + (long) (5000 * Math.random());
            }
        }
        else if (currentTime - hideStartTime > nextShowTime) {
            visible = true;
        }
    }

    public void hit() {
        setupNew();
    }
    
    private void setupNew() {
        x = 0;
        y = game.getHeight() * Math.random();
        vx = 2;
        vy = 0;
        targetX = game.getWidth() * Math.random();
        targetY = game.getHeight() * Math.random();
            
        visible = false;
        size = 1 + (int) (2 * Math.random());
        setShape();
        hideStartTime = System.currentTimeMillis();
        nextShowTime = 10000 + (long) (10000 * Math.random());
        nextStartShotTime = hideStartTime;
        nextShotTime = 5000 + (long) (5000 * Math.random());
    }

    @Override
    public void StateChanged(State newState) {
        if (!visible && newState == State.PLAYING) {
            setupNew();
        }
        else if (newState == State.TITLE) {
            visible = false;
        }
    }
    
}
