package fractaloscope;

import java.util.concurrent.ForkJoinPool;

public class JuliaPainter implements Painter {
    private Display display;
    private ForkJoinPool pool;

    private int maxIter;

    private double cRe;
    private double cIm;

    private int power;

    public JuliaPainter(Display display) {
        this.display = display;

        maxIter = 40;
        cRe = -0.8;
        cIm = 0.156;
        power = 2;

        pool = new ForkJoinPool();
    }

    public void setParam(String name, Number value) {
        if(name.equals("cRe")) {
            cRe = value.doubleValue();
            return;
        }

        if(name.equals("cIm")) {
            cIm = value.doubleValue();
            return;
        }

        if(name.equals("maxIter")) {
            maxIter = value.intValue();
            return;
        }

        if(name.equals("power")) {
            power = value.intValue();
        }
    }

    public Number getParam(String name) {
        if(name.equals("cRe")) {
            return cRe;
        }

        if(name.equals("cIm")) {
            return cIm;
        }

        return null;
    }

    public void paint() {
        int[] pixels = display.getPixels();

        ForkJoinJuliaGenerator task = new ForkJoinJuliaGenerator(cRe, cIm, power, maxIter,
                display.getColorMap(), 0, pixels.length, pixels, display.getW(), display.getViewPort(), display.getRatio());

        pool.invoke(task);

        synchronized(display.getBuffer()) {
            display.getSource().newPixels();
        }
    }
}
