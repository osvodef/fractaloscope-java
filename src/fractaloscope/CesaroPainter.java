package fractaloscope;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ForkJoinPool;

public class CesaroPainter implements Painter {
    private Display display;
    private double angle;
    private int iterations;
    private int sides;
    private ForkJoinPool pool;

    public CesaroPainter(Display display) {
        this.display = display;

        angle = 0.866;
        iterations = 5;
        sides = 2;

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

        if(name.equals("sides")) {
            sides = value.intValue();
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
        ForkJoinCesaroGenerator[] tasks = new ForkJoinCesaroGenerator[sides];

        double dAngle = 2 * Math.PI / sides;
        double vertexAngle = 0;

        int newSides = (sides == 2) ? 1 : sides;
        double newAngle = (newSides == 1) ? -angle : angle;

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

        for(int i = 0; i < newSides; i++) {
            double x1 = Math.cos(vertexAngle) * 100;
            double y1 = Math.sin(vertexAngle) * 100;
            double x2 = Math.cos(vertexAngle + dAngle) * 100;
            double y2 = Math.sin(vertexAngle + dAngle) * 100;

            tasks[i] = new ForkJoinCesaroGenerator(g, display.getColorMap(),
                    display.getViewPort(), display.getRatio(), color, colorRange, newAngle, iterations, x1, y1, x2, y2);

            vertexAngle += dAngle;
        }

        synchronized(buffer) {
            g.setStroke(new BasicStroke((float)1.5));
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
            for(int i = 0; i < newSides; i++) {
                pool.invoke(tasks[i]);
            }
        }

        g.dispose();
    }
}
