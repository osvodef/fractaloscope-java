package fractaloscope;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class ForkJoinDragonGenerator extends RecursiveAction {
    private double angle;
    private double color;
    private double colorRange;
    private int iteration;
    private double ratio;
    private ViewPort vp;


    private double x1;
    private double y1;
    private double x2;
    private double y2;

    private ColorMap cm;
    private Graphics g;

    public ForkJoinDragonGenerator(Graphics g, ColorMap cm, ViewPort vp, double ratio,
                                   double color, double colorRange,
                                   double angle, int iteration,
                                   double x1, double y1, double x2, double y2) {
        this.g = g;
        this.cm = cm;
        this.vp = vp;
        this.ratio = ratio;
        this.color = color;
        this.colorRange = colorRange;
        this.angle = angle;
        this.iteration = iteration;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected void compute() {
        if(iteration == 0) {
            synchronized(g) {
                g.setColor(new Color(cm.getColor(color)));
                g.drawLine(
                        (int) ((x1 - vp.xMin) / ratio),
                        (int) ((y1 - vp.yMin) / ratio),
                        (int) ((x2 - vp.xMin) / ratio),
                        (int) ((y2 - vp.yMin) / ratio)
                );
            }
            return;
        }

        // 1---H
        //     |
        //     2
        double xH = (x1 + x2) / 2 + ((y2 - y1) / 2) * (angle / Math.sqrt(1 - angle * angle));
        double yH = (y1 + y2) / 2 - ((x2 - x1) / 2) * (angle / Math.sqrt(1 - angle * angle));
        double cr = colorRange / 2;

        invokeAll(
                new ForkJoinDragonGenerator(g, cm, vp, ratio, color,      cr, angle, iteration - 1, x1, y1, xH, yH),
                new ForkJoinDragonGenerator(g, cm, vp, ratio, color + cr, cr, angle, iteration - 1, x2, y2, xH, yH)
        );
    }
}
