package game.obj;


import game.AsteroidsGame;
import game.Obj;
import java.awt.Graphics2D;
import java.awt.Polygon;


/**
 *
 * @author leonardo
 */
public class ShipPropulsion extends Obj {
    
    private Ship ship;
    private boolean show;
    private Polygon propulsionShape = new Polygon();
    
    public ShipPropulsion(AsteroidsGame game, Ship ship) {
        super(game);
        this.ship = ship;
        setShape();
    }

    private void setShape() {
        propulsionShape.addPoint(-25, 0);
        propulsionShape.addPoint(-10, 3);
        propulsionShape.addPoint(-10, -3);
        shape = propulsionShape;
    }

    @Override
    public void update() {
        x = ship.x;
        y = ship.y;
        angle = ship.angle;
        visible = ship.visible;
        show = !show;
        propulsionShape.xpoints[0] = -25 + (int) (10 * Math.random());
    }

    @Override
    public void draw(Graphics2D g) {
        if (ship.accelerating && show) {
            super.draw(g);
        }
    }
    
}
