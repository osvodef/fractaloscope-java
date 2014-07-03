package fractaloscope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public abstract class Renderer extends JPanel implements Runnable {
    private BufferStrategy bufferStrategy;
    protected Canvas drawArea;
    private boolean stopped = false;

    public void init() {
        Thread t = new Thread(this);
        drawArea = new Canvas();
        drawArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
        });
        drawArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onMouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }
        });
        drawArea.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                onMouseWheelMoved(e);
            }
        });

        setIgnoreRepaint(true);
        t.start();
    }

    public void update() {
        if(!bufferStrategy.contentsLost()) {
            bufferStrategy.show();
        }
    }

    public BufferStrategy getBufferStrategy() {
        return bufferStrategy;
    }

    public void createBufferStrategy(int numBuffers) {
        drawArea.createBufferStrategy(numBuffers);
    }

    //Subclasses should override this method to do any drawing
    public abstract void draw(Graphics2D g);

    public void update(Graphics2D g) {
        g.setColor(g.getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public Graphics2D getGraphics() {
        return (Graphics2D)bufferStrategy.getDrawGraphics();
    }

    //Do not override this method
    public void run() {
        setLayout(new BorderLayout());
        add(drawArea, BorderLayout.CENTER);
        createBufferStrategy(2);
        bufferStrategy = drawArea.getBufferStrategy();

        long currTime = System.currentTimeMillis();

        //Main loop
        while(!stopped) {
            //Get time past
            long elapsedTime = System.currentTimeMillis()-currTime;
            currTime += elapsedTime;

            //Flip or show the back buffer
            update();

            //Handle Drawing
            Graphics2D g = getGraphics();
            update(g);
            draw(g);

            //Dispose of graphics context
            g.dispose();
        }
    }

    public void onMouseDragged(MouseEvent e) {}
    public void onMouseMoved(MouseEvent e) {}
    public void onMouseClicked(MouseEvent e) {}
    public void onMouseWheelMoved(MouseWheelEvent e) {}
    public void onMousePressed(MouseEvent e) {}
    public void onMouseReleased(MouseEvent e) {}
}