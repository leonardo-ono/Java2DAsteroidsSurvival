package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 *
 * @author leonardo
 */
public class Asteroid extends Obj {
    
    public double vx, vy, va;
    public int size;
    public int halfSize;
    
    // size = 1 small, 2 medium, 3 large
    public Asteroid(AsteroidsGame game, double x, double y, int size) {
        super(game);
        this.x = x;
        this.y = y;
        this.size = size;
        this.halfSize = size * 10;
        angle = (2 * Math.PI) * Math.random();
        double v = 0.5 + 1 * Math.random();
        vx = (4 - size) * Math.cos(angle) * v;
        vy = (4 - size) * Math.sin(angle) * v;
        va = 0.01 + 0.05 * Math.random();
        //setShape();
        generateRandomShape();
    }
    
    private void setShape() {
        //Ellipse2D asteroidShape = new Ellipse2D.Double(-halfSize, -halfSize, 2 * halfSize, 2 * halfSize);
        Rectangle.Double asteroidShape = new Rectangle.Double(-halfSize, -halfSize, 2 * halfSize, 2 * halfSize);
        shape = asteroidShape;
    }

    private void generateRandomShape() {
        Polygon randomAsteroidShape = new Polygon();
        int f = 5 + (int) (5 * Math.random());
        double da = (2 * Math.PI) / f;
        double a = (2 * Math.PI) * Math.random();
        for (int i = 0; i < f; i++) {
            double ad = halfSize + halfSize * Math.random();
            double ax = ad * Math.cos(a);
            double ay = ad * Math.sin(a);
            randomAsteroidShape.addPoint((int) ax, (int) ay);
            a += da;
        }
        shape = randomAsteroidShape;
    }
    
    @Override
    public void update() {
        angle += va;
        
        x += vx;
        y += vy;
        
        x = x < -halfSize ? game.getWidth() : x;
        x = x > game.getWidth() + halfSize ? -halfSize : x;
        y = y < -halfSize ? game.getHeight() : y;
        y = y > game.getHeight() + halfSize ? -halfSize : y;
    }

    public void hit() {
        if (size > 1) {
            game.add(new Asteroid(game, x, y, size - 1));
            game.add(new Asteroid(game, x, y, size - 1));
            game.add(new Asteroid(game, x, y, size - 1));
        }
        destroyed = true;
    }
    
}
