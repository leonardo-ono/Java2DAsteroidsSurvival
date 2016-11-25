package game;


import game.AsteroidsGame.State;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author leonardo
 */
public class Obj {
    
    public AsteroidsGame game;
    public double x, y;
    public double angle;
    public Shape shape;
    public Color color = Color.WHITE;
    public boolean visible = true;
    public boolean destroyed;
    public AffineTransform transform = new AffineTransform();

    public Obj(AsteroidsGame game) {
        this.game = game;
    }

    public AffineTransform getTranform() {
        transform.setToIdentity();
        transform.translate(x, y);
        transform.rotate(angle);
        return transform;
    }
    
    public void update() {
        switch(game.getState()) {
            case INITIALIZING : updateInitializing(); break;
            case TITLE : updateTitle(); break;
            case PLAYING : updatePlaying(); break;
            case HITTED : updateHitted(); break;
            case GAME_OVER : updateGameOver(); break;
        }
    }
    
    public void draw(Graphics2D g) {
        if (shape == null || !visible) {
            return;
        }
        AffineTransform previousTransform = g.getTransform();
        g.setColor(color);
        g.translate(x, y);
        g.rotate(angle);
        g.draw(shape);
        g.setTransform(previousTransform);
    }
    
    public void StateChanged(State newState)  {
    }

    public void updateInitializing() {
    }

    public void updateTitle() {
    }

    public void updatePlaying() {
    }

    public void updateHitted() {
    }

    public void updateGameOver() {
    }

}
