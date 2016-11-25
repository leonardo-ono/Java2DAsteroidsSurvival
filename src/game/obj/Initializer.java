package game.obj;


import game.AsteroidsGame;
import game.AsteroidsGame.State;
import game.Obj;

/**
 *
 * @author leonardo
 */
public class Initializer extends Obj {
    
    private long startTime;
    
    public Initializer(AsteroidsGame game) {
        super(game);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void updateInitializing() {
        if (System.currentTimeMillis() - startTime > 100) {
            game.setState(State.TITLE);
        }
    }
    
}
