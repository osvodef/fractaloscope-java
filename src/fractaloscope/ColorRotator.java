package fractaloscope;

public class ColorRotator extends Thread {
    private Display display;
    private boolean isRunning = true;

    public ColorRotator(Display display) {
        this.display = display;
    }

    public void run() {
        while(isRunning) {
            double newOffset = display.getOffset() + 0.015;
            newOffset = newOffset >= 1 ? 0 : newOffset;
            display.setOffset(newOffset);
            try {
                sleep(30);
            } catch(InterruptedException e) {}
        }
    }

    public void cease() {
        isRunning = false;
    }
}
