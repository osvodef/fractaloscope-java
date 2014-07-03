package fractaloscope;

public class ColorMap {
    private int[] palette;
    private double offset;

    public ColorMap(int[] map) {
        palette = map;
        offset = 0;
    }

    public int getColor(double position) {
        position = position + offset;

        position = position > 1 ? position - 1 : position;
        position = position > 1 ? 1 : position;
        position = position < 0 ? 0 : position;
        int idx = (int) (position * (palette.length - 1));
        return palette[idx];
    }


    public int[] getColorPalette() {
        return palette;
    }

    public void setColorPalette(int[] palette) {
        this.palette = palette;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }
}