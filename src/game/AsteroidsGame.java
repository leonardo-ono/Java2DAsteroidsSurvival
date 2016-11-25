package game;


import game.obj.Asteroid;
import game.obj.HUD;
import game.obj.Initializer;
import game.obj.Saucer;
import game.obj.SaucerShot;
import game.obj.Ship;
import game.obj.ShipPropulsion;
import game.obj.Shot;
import game.obj.Spark;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class AsteroidsGame {

    public static final String TITLE = "Asteroids Survival";
    public static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
    
    private List<Obj> objs = new ArrayList<Obj>();
    private List<Obj> objsAdd = new ArrayList<Obj>();
    private List<Obj> objsRemove = new ArrayList<Obj>();

    public static final int[] ASTEROID_SCORE_TABLE = { 0, 100, 50, 20 };
    public static final int[] SAUCER_SCORE_TABLE = { 0, 1000, 200 };

    public static enum State { INITIALIZING, TITLE, PLAYING, HITTED, GAME_OVER }
    private State state = State.INITIALIZING;
    private Ship ship;
    private int lives = 3;
    private int score;
    private int hiscore;
    
    public AsteroidsGame() {
        init();
    }

    public int getWidth() {
        return SCREEN_WIDTH;
    }

    public int getHeight() {
        return SCREEN_HEIGHT;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            for (Obj obj : objs) {
                obj.StateChanged(state);
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }
    
    public int getHiscore() {
        return hiscore;
    }
    
    public void updateHiscore() {
        if (score > hiscore) {
            hiscore = score;
        }
        score = 0;
    }
    
    public void add(Obj obj) {
        objsAdd.add(obj);
    }
    
    private void init() {
        add(new Initializer(this));
        add(new HUD(this));
        add(ship = new Ship(this));
        add(new ShipPropulsion(this, ship));
        add(new Saucer(this, ship));
        createAsteroids();
    }
    
    public void createAsteroids() {
        for (int i = 0; i < 10; i++) {
            createOneAsteroid();
        }
    }

    public void createOneAsteroid() {
        int p = (int) (4 * Math.random());
        int x = 0;
        int y = 0;
        if (p == 0) {
            x = 0;
            y = (int) (SCREEN_HEIGHT * Math.random());
        }
        else if (p == 1) {
            x = SCREEN_WIDTH;
            y = (int) (SCREEN_HEIGHT * Math.random());
        }
        else if (p == 2) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = 0;
        }
        else if (p == 3) {
            x = (int) (SCREEN_WIDTH * Math.random());
            y = SCREEN_HEIGHT;
        }
        Asteroid asteroid = new Asteroid(this, x, y, 3);
        //while (collides(ship, asteroid)) {
        //    asteroid.x = SCREEN_WIDTH * Math.random();
        //    asteroid.y = SCREEN_HEIGHT * Math.random();
        //}
        add(asteroid);
    }
    
    private void removeAllAsteroids() {
        for (Obj obj : objs) {
            if (obj instanceof Asteroid) {
                obj.destroyed = true;
            }
        }
    }
    
    public void update() {
        for (Obj obj : objs) {
            obj.update();
            if (obj.destroyed) {
                objsRemove.add(obj);
            }
        }
        objs.addAll(objsAdd);
        objsAdd.clear();
        objs.removeAll(objsRemove);
        objsRemove.clear();
    }
    
    public void draw(Graphics2D g) {
        g.setBackground(Color.BLACK);
        g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        for (Obj obj : objs) {
            obj.draw(g);
        }
    }

    // TODO: provisory checkCollision() method that at least works, 
    //       but needs to improve later ?
    public Obj checkCollision(Obj o1, Class collidedObjType) {
        for (Obj o2 : objs) {
            if (o1 == o2 || !collidedObjType.isInstance(o2)
                || o1.shape == null || o2.shape == null
                    || o1.destroyed || o2.destroyed
                    || !o1.visible || !o2.visible) {
                    continue;
            }
            Area a1 = new Area(o1.shape);
            Area a2 = new Area(o2.shape);
            a1.transform(o1.getTranform());
            a2.transform(o2.getTranform());
            a1.intersect(a2);
            if (!a1.isEmpty()) {
                return o2;
            }
        }
        return null;
    }

    public boolean collides(Obj o1, Obj o2) {
        Area a1 = new Area(o1.shape);
        Area a2 = new Area(o2.shape);
        a1.transform(o1.getTranform());
        a2.transform(o2.getTranform());
        a1.intersect(a2);
        return !a1.isEmpty();
    }
    
    public void shot(double x, double y, double angle) {
        objsAdd.add(new Shot(this, x, y, angle));
    }

    public void saucerShot(double x, double y) {
        double rx = 40 * Math.random() - 20;
        double ry = 40 * Math.random() - 20;
        double dx = (ship.x + rx) - x;
        double dy = (ship.y + ry) - y;
        double angle = Math.atan2(dy, dx);
        objsAdd.add(new SaucerShot(this, x, y, angle));
    }
    
    public void showExplosion(double x, double y) {
        for (int i = 0; i < 30; i++) {
            objsAdd.add(new Spark(this, x, y));
        }
    }
    
    public boolean checkAllObjectsDestroyed(Class type) {
        for (Obj obj : objs) {
            if (type.isInstance(obj)) {
                if (obj.destroyed == false) {
                    return false;
                }
            }
        }
        for (Obj obj : objsAdd) {
            if (type.isInstance(obj)) {
                if (obj.destroyed == false) {
                    return false;
                }
            }
        }
        return true;
    }
    
    // ---

    public void start() {
        removeAllAsteroids();
        createAsteroids();
        setState(State.PLAYING);
    }

    public void hit() {
        lives--;
        if (lives <= 0) {
            setState(State.GAME_OVER);
        }
        else {
            setState(State.HITTED);
        }
    }

    public void playNextLife() {
        setState(State.PLAYING);
    }

    public void backToTitle() {
        removeAllAsteroids();
        createAsteroids();
        setState(State.TITLE);
        lives = 3;
    }
    
}
