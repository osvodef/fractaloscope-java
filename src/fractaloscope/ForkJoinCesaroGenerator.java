package fractaloscope;


import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class ForkJoinCesaroGenerator extends RecursiveAction {
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

    private ColorMap scheme;
    private Graphics g;

    public ForkJoinCesaroGenerator(Graphics g, ColorMap scheme, ViewPort vp, double ratio,
                                   double color, double colorRange,
                                   double angle, int iteration,
                                   double x1, double y1, double x2, double y2) {
        this.g = g;
        this.scheme = scheme;
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
                g.setColor(new Color(scheme.getColor(color)));
                g.drawLine(
                        (int) ((x1 - vp.xMin) / ratio),
                        (int) ((y1 - vp.yMin) / ratio),
                        (int) ((x2 - vp.xMin) / ratio),
                        (int) ((y2 - vp.yMin) / ratio)
                );
            }
            return;
        }

        //       H
        //   a  /|\  a
        // 1---B M D---2

        double dx = x2 - x1;
        double dy = y2 - y1;

        double a = 1 / (2 * (1 + Math.sqrt(1 - angle * angle)));
        double H = a * angle;

        double xB = x1 + dx * a;
        double yB = y1 + dy * a;

        double xD = x2 - dx * a;
        double yD = y2 - dy * a;

        double xM = x1 + dx / 2;
        double yM = y1 + dy / 2;

        double xH = xM + dy * H;
        double yH = yM - dx * H;

        double cr = colorRange / 4;

        invokeAll(
                new ForkJoinCesaroGenerator(g, scheme, vp, ratio, color,          cr, angle, iteration - 1, x1, y1, xB, yB),
                new ForkJoinCesaroGenerator(g, scheme, vp, ratio, color +     cr, cr, angle, iteration - 1, xB, yB, xH, yH),
                new ForkJoinCesaroGenerator(g, scheme, vp, ratio, color + 2 * cr, cr, angle, iteration - 1, xH, yH, xD, yD),
                new ForkJoinCesaroGenerator(g, scheme, vp, ratio, color + 3 * cr, cr, angle, iteration - 1, xD, yD, x2, y2)
        );
    }
}
