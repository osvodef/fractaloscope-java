package fractaloscope;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;

import prefuse.util.ColorLib;

public class Display extends Renderer {
    private App app;

    private int[] pixels;
    private MemoryImageSource source;
    private Image buffer;
    private BufferedImage vectorBuffer;

    private int w;
    private int h;

    private ViewPort viewPort;
    private ViewPort cViewPort;

    private int dragStartX;
    private int dragStartY;
    private ViewPort dragStartViewPort;

    private ArrayList<int[]> palettes;
    private ColorMap colorMap;

    private Painter painter;

    private ColorRotator rotator = null;

    private boolean isFreezed = false;
    private boolean isDragging = false;

    private DebouncedResizer resizer;

    public Display(App app) {
        this.app = app;
        setBuffers(495, 495);
        resizer = new DebouncedResizer(this, 50, 50, 495, 495);

        fillPalettes();
        colorMap = new ColorMap(palettes.get(1));
        viewPort = new ViewPort();
        cViewPort = new ViewPort();

        setFractal("julia");

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int newW = getWidth();
                int newH = getHeight();
                int size = newW < newH ? newW : newH;
                resizer.hit(size, size);
            }
        });
    }

    public void setBuffers(int w, int h) {
        this.w = w;
        this.h = h;
        pixels = new int[w * h];
        source = new MemoryImageSource(w, h, pixels, 0, w);
        source.setAnimated(true);
        buffer = createImage(this.source);
        vectorBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        if(painter != null) {
            refresh();
        }
    }

    private void fillPalettes() {
        int[] invertedGrayScalePalette = new int[500];
        for(int i = 0; i < 500; i++) {
            int level = (int) (i / 499.0 * 255);
            int color = (255 << 24) | (level << 16) | (level << 8) | level;
            invertedGrayScalePalette[i] = color;
        }
        palettes = new ArrayList<int[]>();
        palettes.add(ColorLib.getGrayscalePalette(500));
        palettes.add(invertedGrayScalePalette);
        palettes.add(ColorLib.getHSBPalette(500, (float) 0.8, 1));
        palettes.add(ColorLib.getHotPalette(500));
        palettes.add(ColorLib.getCoolPalette(500));
    }

    @Override
    public void draw(Graphics2D g) {
        if(painter instanceof JuliaPainter || painter instanceof BarnsleyPainter) {
            synchronized (buffer) {
                g.drawImage(buffer, (drawArea.getWidth() - w) / 2, (drawArea.getHeight() - h) / 2, null);
            }
        } else {
            synchronized(vectorBuffer) {
                g.drawImage(vectorBuffer,  (drawArea.getWidth() - w) / 2, (drawArea.getHeight() - h) / 2, null);
            }
        }
    }


    private int getX(MouseEvent e) {
        return e.getX() - (drawArea.getWidth() - w) / 2;
    }

    private int getY(MouseEvent e) {
        return e.getY() - (drawArea.getHeight() - h) / 2;
    }

    private boolean isInPicture(MouseEvent e) {
        return !(getX(e) < 0 || getY(e) < 0 || getX(e) > w || getY(e) > h);
    }


    // Переключение фрактала
    public void setFractal(String name) {
        if(name.equals("barnsley")) {
            isFreezed = true;
            isDragging = false;
            painter = new BarnsleyPainter(this);
            resetViewports();
            refresh();
            return;
        }

        if(name.equals("cesaro")) {
            isFreezed = true;
            isDragging = false;
            painter = new CesaroPainter(this);
            resetViewports();
            refresh();
            return;
        }

        if(name.equals("dragon")) {
            isFreezed = true;
            isDragging = false;
            painter = new DragonPainter(this);
            resetViewports();
            refresh();
            return;
        }

        if(name.equals("julia")) {
            isFreezed = true;
            isDragging = false;
            painter = new JuliaPainter(this);
            resetViewports();
            refresh();
            return;
        }
    }

    void resetViewports() {
        if(painter instanceof BarnsleyPainter) {
            viewPort.set(-5.66, 6.83, -11.05, 1.44);
        }

        if(painter instanceof CesaroPainter) {
            viewPort.set(-100, 100, -100, 100);
        }

        if(painter instanceof DragonPainter) {
            viewPort.set(-175, 136, -175, 136);
        }

        if(painter instanceof JuliaPainter) {
            viewPort.set(-1.6, 1.6, -1.6, 1.6);
            cViewPort.set(-1.6, 1.6, -1.6, 1.6);
        }
    }


    // Залипательный режим

    public void onMouseClicked(MouseEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        if(!(painter instanceof JuliaPainter)) {
            return;
        }

        isFreezed = !isFreezed;
    }

    public void onMouseMoved(MouseEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        if(!(painter instanceof JuliaPainter) || isFreezed) {
            return;
        }

        double cRatio = cViewPort.getWidth() / w;

        double newCRe = cViewPort.xMin + getX(e) * cRatio;
        double newCIm = cViewPort.yMin + getY(e) * cRatio;

        app.setC(newCRe, newCIm);
        painter.setParam("cRe", newCRe);
        painter.setParam("cIm", newCIm);

        refresh();
    }





    // Zoomable-поведение

    public void onMouseWheelMoved(MouseWheelEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        if(!isFreezed || isDragging) {
            return;
        }

        double currentRe = viewPort.xMin + getX(e) * getRatio();
        double currentIm = viewPort.yMin + getY(e) * getRatio();

        if(e.getWheelRotation() < 0) {
            viewPort.zoomIn(currentRe, currentIm);
        } else {
            viewPort.zoomOut(currentRe, currentIm);
        }

        if(painter instanceof JuliaPainter) {
            currentRe = painter.getParam("cRe").doubleValue();
            currentIm = painter.getParam("cIm").doubleValue();

            if(e.getWheelRotation() < 0) {
                cViewPort.zoomIn(currentRe, currentIm);
            } else {
                cViewPort.zoomOut(currentRe, currentIm);
            }
        }

        refresh();
    }





    // Draggable-поведение

    public void onMousePressed(MouseEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        isDragging = true;

        dragStartX = getX(e);
        dragStartY = getY(e);

        dragStartViewPort = new ViewPort(viewPort);
    }

    public void onMouseReleased(MouseEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        isDragging = false;
        dragStartViewPort = null;
    }

    public void onMouseDragged(MouseEvent e) {
        if(!isInPicture(e)) {
            return;
        }

        if(!isFreezed || !isDragging) {
            return;
        }

        int movedX = getX(e) - dragStartX;
        int movedY = getY(e) - dragStartY;

        viewPort.move(dragStartViewPort, movedX * getRatio(), movedY * getRatio());

        refresh();
    }



    // Сеттер параметров, относящихся к конкретному фракталу
    public void setParam(String name, Number value) {
        painter.setParam(name, value);
        refresh();
    }
    
    public void refresh() {
        painter.paint();
    }



    // Сеттеры общих параметров дисплея
    public void setColorScheme(int index) {
        colorMap.setColorPalette(palettes.get(index));
        refresh();
    }

    public void setOffset(double offset) {
        colorMap.setOffset(offset);
        refresh();
    }





    // Геттеры объектов, участвующих в отрисовке

    public int[] getPixels() {
        return pixels;
    }

    public Image getBuffer() {
        return buffer;
    }

    public BufferedImage getVectorBuffer() {
        return vectorBuffer;
    }

    public MemoryImageSource getSource() {
        return source;
    }

    public ColorMap getColorMap() {
        return colorMap;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }

    public double getRatio() {
        return viewPort.getWidth() / w;
    }

    public double getOffset() {
        return colorMap.getOffset();
    }

    public int getW() {
        return w;
    }




    // Цветовая анимация

    public void switchRotation() {
        if(rotator == null) {
            startRotation();
        } else {
            stopRotation();
        }
    }

    public void startRotation() {
        if(rotator != null) {
            return;
        }

        rotator = new ColorRotator(this);
        rotator.start();
    }

    public void stopRotation() {
        if(rotator == null) {
            return;
        }

        rotator.cease();
        rotator = null;
        colorMap.setOffset(0);
        refresh();
    }
}
