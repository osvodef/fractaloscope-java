package fractaloscope;

public class BarnsleyPainter implements Painter {
    private Display display;
    private int iterations;

    private double curliness;
    private double fuzziness;

    private static double[][][] c = {{
        {0, 0,    0},
        {0, 0.16, 0}
    }, {
        { 0.85, -0.04,   0},
        { 0.04, 0.85, -1.6}
    }, {
        { 0.20,  0.26,   0},
        { -0.23,   0.22, -1.6}
    }, {
        {-0.15,   -0.28,    0},
        { -0.26,   0.24, -0.44}
    }};

    public BarnsleyPainter(Display display) {
        this.display = display;
        this.iterations = 335000;

        curliness = 0.04;
        fuzziness = 0.85;
    }

    @Override
    public void setParam(String name, Number value) {
        if(name.equals("iterations")) {
            this.iterations = value.intValue();
        }

        if(name.equals("curliness")) {
            this.curliness = value.doubleValue();
        }

        if(name.equals("fuzziness")) {
            this.fuzziness = value.doubleValue();
        }
    }

    @Override
    public Number getParam(String name) {
        return null;
    }


    private void setCoeffArray() {
        c[1][0][1] = -curliness;
        c[1][1][0] = curliness;

        c[1][0][0] = fuzziness;
        c[1][1][1] = fuzziness;
    }

    @Override
    public synchronized void paint() {
        int[] pixels = display.getPixels();
        ViewPort vp = display.getViewPort();
        double ratio = display.getRatio();
        int w  = display.getW();

        double xMin = vp.xMin;
        double xMax = vp.xMax;
        double yMin = vp.yMin;
        double yMax = vp.yMax;

        double[] pixelColors = new double[pixels.length];
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = 0xFF000000;
            pixelColors[i] = 0;
        }

        setCoeffArray();

        double x = 0;
        double y = 0;

        for(int i = 0; i < iterations; i++) {
            int ti;
            double random = Math.random();
            if (random < 0.01) {
                ti = 0;
            } else if (random < 0.86) {
                ti = 1;
            } else if (random < 0.93) {
                ti = 2;
            } else {
                ti = 3;
            }


            double newX = c[ti][0][0] * x + c[ti][0][1] * y + c[ti][0][2];
            double newY = c[ti][1][0] * x + c[ti][1][1] * y + c[ti][1][2];

            x = newX;
            y = newY;

            if(x > xMax || x < xMin || y > yMax || y < yMin) {
                continue;
            }

            int xPic = (int) ((x - xMin) / ratio);
            int yPic = (int) ((y - yMin) / ratio);
            int index =  yPic * w + xPic;

            if(index < pixelColors.length && index >=0) {
                pixelColors[index] += 0.05;
            }
        }

        boolean cyclicColorMap = (display.getColorMap().getColor(0) == display.getColorMap().getColor(1));

        for(int i = 0; i < pixels.length; i++) {
            if(pixelColors[i] >= 1) {
                pixels[i] = display.getColorMap().getColor(1);
            } else if(pixelColors[i] == 0) {
                if(!cyclicColorMap) {
                    pixels[i] = display.getColorMap().getColor(pixelColors[i]);
                }
            } else {
                pixels[i] = display.getColorMap().getColor(pixelColors[i]);
            }
        }

        synchronized (display.getBuffer()) {
            display.getSource().newPixels();
        }
    }
}
