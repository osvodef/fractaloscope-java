package fractaloscope;

import java.util.concurrent.RecursiveAction;


public class ForkJoinJuliaGenerator extends RecursiveAction {
    private ColorMap scheme;
    private int start;
    private int length;
    private int[] pixels;

    private int width;
    private ViewPort viewPort;
    private double ratio;

    private double cRe;
    private double cIm;
    private int power;
    private int maxIter;

    private static int sThreshold = 500;

    public ForkJoinJuliaGenerator(double cRe,
                                  double cIm,
                                  int power,
                                  int maxIter,
                                  ColorMap scheme,
                                  int start,
                                  int length,
                                  int[] pixels,
                                  int width,
                                  ViewPort viewPort,
                                  double ratio) {
        this.cRe = cRe;
        this.cIm = cIm;
        this.power = power;
        this.maxIter = maxIter;
        this.scheme = scheme;
        this.start = start;
        this.length = length;
        this.pixels = pixels;
        this.width = width;
        this.viewPort = viewPort;
        this.ratio = ratio;
    }

    @Override
    protected void compute() {
        if(length < sThreshold) {
            computeDirectly();
            return;
        }

        int split = length / 2;

        invokeAll(
            new ForkJoinJuliaGenerator(cRe, cIm, power, maxIter, scheme, start, split, pixels, width, viewPort, ratio),
            new ForkJoinJuliaGenerator(cRe, cIm, power, maxIter, scheme, start + split, length - split, pixels, width, viewPort, ratio)
        );
    }

    protected double iterate(double re, double im) {
        int i;

        for(i = 0; i < maxIter; i++) {
            if(re * re + im * im > 4) {
                return i;
            }

            double oldRe = re;
            double oldIm = im;

            for(int j = 0; j < power - 1; j++) {
                double newRe = re * oldRe - im * oldIm;
                double newIm = im * oldRe + re * oldIm;

                re = newRe;
                im = newIm;
            }

            re += cRe;
            im += cIm;
        }

        return maxIter;
    }

    protected void computeDirectly() {
        for(int i = start; i < start + length; i++) {
            double re = viewPort.xMin + (i % width) * ratio;
            double im = viewPort.yMin + (i / width) * ratio;

            pixels[i] = scheme.getColor(iterate(re, im) / maxIter);
        }
    }
}
