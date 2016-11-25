package game;


import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 *
 * @author leonardo
 */
public class Display extends Canvas {
    
    private Stroke stroke = new BasicStroke(2);
    private boolean running = true;
    private BufferStrategy bs;
    private AsteroidsGame game;
    private JFrame frame;
    
    public Display(AsteroidsGame game) {
        this.game = game;
        addKeyListener(new KeyHandler());
    }
    
    public void start() {
        if (frame != null) {
            return;
        }
        frame = new JFrame();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setTitle(game.TITLE);
                frame.setSize(game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.add(Display.this);
                frame.setVisible(true);
                Display.this.requestFocus();
                
                createBufferStrategy(2);
                bs = getBufferStrategy();
                new Thread(new MainLoop()).start();
            }
        });
    }
    
    private class MainLoop implements Runnable {

        @Override
        public void run() {
            long framePeriod = 25;
            long startTime;
            long endTime;
            while (running) {
                startTime = System.currentTimeMillis();
                game.update();
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(stroke);
                game.draw(g);
                g.dispose();
                bs.show();

                do {
                    endTime = System.currentTimeMillis();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) { }
                }
                while (framePeriod - (endTime - startTime) > 0);
            }
        }
        
    }
    
    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Keyboard.keyDown[e.getKeyCode()] = true;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Keyboard.keyDown[e.getKeyCode()] = false;
        }
        
    }
    
}
