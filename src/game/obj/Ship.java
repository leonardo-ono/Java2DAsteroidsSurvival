package game.obj;


import game.AsteroidsGame;
import game.AsteroidsGame.State;
import game.Keyboard;
import game.Obj;
import java.awt.Polygon;
import java.awt.event.KeyEvent;

/**
 *
 * @author leonardo
 */
public class Ship extends Obj {
    
    public double vx, vy;
    public boolean accelerating;
    private long shotTime;
    private long unhittableStartTime;
    private long hittedStartTime;

    public Ship(AsteroidsGame game) {
        super(game);
        setShape();
        visible = false;
    }
    
    private void setShape() {
        Polygon shipShape = new Polygon();
        shipShape.addPoint(10, 0);
        shipShape.addPoint(-10, 5);
        shipShape.addPoint(-10, -5);
        shape = shipShape;
    }

    @Override
    public void updatePlaying() {
        long currentTime = System.currentTimeMillis();
        
        if (accelerating = Keyboard.keyDown[KeyEvent.VK_UP]) { // accelerating ?
            vx += 0.25 * Math.cos(angle);
            vy += 0.25 * Math.sin(angle);
        }
        if (Keyboard.keyDown[KeyEvent.VK_LEFT]) {
            angle -= 0.1;
        }
        else if (Keyboard.keyDown[KeyEvent.VK_RIGHT]) {
            angle += 0.1;
        }
        
        boolean shotKeyPressed = Keyboard.keyDown[KeyEvent.VK_SPACE] || true; 
        if (shotKeyPressed && (currentTime - shotTime > 100)) {
            game.shot(x, y, angle);
            shotTime = currentTime;
        }
        
        vx = vx > 2 ? 2 : vx;
        vy = vy > 2 ? 2 : vy;
        x += vx;
        y += vy;
        
        x = x < -10 ? game.getWidth() : x;
        x = x > game.getWidth() + 10 ? -10 : x;
        y = y < -10 ? game.getHeight() : y;
        y = y > game.getHeight() + 10 ? -10 : y;
        
        if (currentTime - unhittableStartTime < 3000) {
            visible = !visible;
        }
        else {
            visible = true;
            Asteroid hittedAsteroid = (Asteroid) game.checkCollision(this, Asteroid.class);
            if (hittedAsteroid != null) {
                game.showExplosion(x, y);
                game.addScore(AsteroidsGame.ASTEROID_SCORE_TABLE[hittedAsteroid.size]);
                game.hit();
                hittedAsteroid.hit();
                return;
            }
            
            Saucer hittedSaucer = (Saucer) game.checkCollision(this, Saucer.class);
            if (hittedSaucer != null) {
                game.showExplosion(x, y);
                game.addScore(AsteroidsGame.SAUCER_SCORE_TABLE[hittedSaucer.size]);
                game.hit();
                hittedSaucer.hit();
                return;
            }

            SaucerShot hittedSaucerShot = (SaucerShot) game.checkCollision(this, SaucerShot.class);
            if (hittedSaucerShot != null) {
                game.showExplosion(x, y);
                game.hit();
                hittedSaucerShot.hit();
                return;
            }
        }
        
        // if all asteroids is destroyed, create again
        if (game.checkAllObjectsDestroyed(Asteroid.class)) {
            unhittableStartTime = System.currentTimeMillis();
            game.createAsteroids();
        }
    }

    @Override
    public void updateHitted() {
        if (System.currentTimeMillis() - hittedStartTime > 3000) {
            game.playNextLife();
        }
    }
    
    @Override
    public void StateChanged(State newState) {
        if (newState == State.PLAYING) {
            x = game.getWidth() / 2;
            y = game.getHeight() / 2;
            vx = vy = 0;
            unhittableStartTime = System.currentTimeMillis();
            shotTime = unhittableStartTime;
        }
        else if (newState == State.HITTED) {
            hittedStartTime = System.currentTimeMillis();
            visible = false;
        }
        else if (newState == State.GAME_OVER) {
            visible = false;
        }
    }
    
}
