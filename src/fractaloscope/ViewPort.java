package fractaloscope;

public class ViewPort {
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    
    private static double zoomFactor = 1.3;

    public ViewPort() {}

    public ViewPort(double xMin, double xMax, double yMin, double yMax) {
        set(xMin, xMax, yMin, yMax);
    }

    public ViewPort(ViewPort other) {
        set(other.xMin, other.xMax, other.yMin, other.yMax);
    }
    
    public void set(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }
    
    private void zoom(boolean in, double centerX, double centerY) {
        double top = centerY - yMin;
        double bottom = yMax - centerY;
        double left = centerX - xMin;
        double right = xMax - centerX;

        if(in) {
            top /= zoomFactor;
            bottom /= zoomFactor;
            left /= zoomFactor;
            right /= zoomFactor;
        } else {
            top *= zoomFactor;
            bottom *= zoomFactor;
            left *= zoomFactor;
            right *= zoomFactor;
        }

        yMin = centerY - top;
        yMax = centerY + bottom;
        xMin = centerX - left;
        xMax = centerX + right;
    }
    
    public void zoomIn(double centerX, double centerY) {
        zoom(true, centerX, centerY);
    }

    public void zoomOut(double centerX, double centerY) {
        zoom(false, centerX, centerY);
    }

    public void move(ViewPort initialCondition, double dX, double dY) {
        xMin = initialCondition.xMin - dX;
        xMax = initialCondition.xMax - dX;
        yMin = initialCondition.yMin - dY;
        yMax = initialCondition.yMax - dY;
    }

    public double getWidth() {
        return xMax - xMin;
    }

    public double getHeight() {
        return yMax - yMin;
    }
}
