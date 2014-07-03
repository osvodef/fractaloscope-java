package fractaloscope;

public interface Painter {
    public abstract void setParam(String name, Number value);
    public abstract Number getParam(String name);
    public abstract void paint();
}
