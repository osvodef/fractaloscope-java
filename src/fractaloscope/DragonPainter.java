package fractaloscope;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

public class DragonPainter implements Painter {
    private Display display;
    private double angle;
    private int iterations;
    private ForkJoinPool pool;

    public DragonPainter(Display display) {
        this.display = display;
        angle = Math.cos(Math.PI / 4);
        iterations = 12;

        pool = new ForkJoinPool();
    }

    @Override
    public void setParam(String name, Number value) {
        if(name.equals("angle")) {
            angle = value.doubleValue();
            return;
        }

        if(name.equals("iterations")) {
            iterations = value.intValue();
            return;
        }
    }

    @Override
    public Number getParam(String name) {
        return null;
    }

    @Override
    public void paint() {
        BufferedImage buffer = display.getVectorBuffer();
        Graphics2D g = buffer.createGraphics();

        //Хак для цветовых схем, начинающихся с тёмных оттенков
        int firstColor = display.getColorMap().getColor(0);
        double color, colorRange;
        if((firstColor & 0x000000FF) < 5 &&
                (firstColor & 0x0000FF00 >> 8) < 5 &&
                (firstColor & 0x00FF0000 >> 16) < 5) {
            color = 0.3;
            colorRange = 0.7;
        } else {
            color = 0;
            colorRange = 1;
        }

        ForkJoinDragonGenerator task = new ForkJoinDragonGenerator(g, display.getColorMap(),
                display.getViewPort(), display.getRatio(), color, colorRange, angle, iterations, -100, 0, 100, 0);

        synchronized(buffer) {
            g.setStroke(new BasicStroke((float)1.5));
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
            pool.invoke(task);
        }

        g.dispose();
    }
}
