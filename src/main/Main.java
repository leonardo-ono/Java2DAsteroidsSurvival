package main;

import game.AsteroidsGame;
import game.Display;

/**
 *
 * @author leonardo
 */
public class Main {

    public static void main(String[] args) {
        AsteroidsGame asteroidsGame = new AsteroidsGame();
        Display display = new Display(asteroidsGame);
        display.start();
    }
    
}
