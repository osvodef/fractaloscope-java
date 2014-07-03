package fractaloscope;

import java.util.Timer;
import java.util.TimerTask;


public class DebouncedResizer {
    private Timer timer = null;
    private long lastHit = 0;
    private long debounceDelay = 0;
    private long checkDelay = 0;
    private int w;
    private int h;
    private Display display;

    public void execute() {
        display.setBuffers(w, h);
    };

    public DebouncedResizer(Display display, long debounceDelay, long checkDelay, int w, int h) {
        this.display = display;
        this.debounceDelay = debounceDelay;
        this.checkDelay = checkDelay;
        this.w = w;
        this.h = h;
    }

    public void hit(int w, int h){
        this.w = w;
        this.h = h;
        lastHit = System.currentTimeMillis();
        if(this.timer != null){
            this.timer.cancel();
            this.timer = null;
        }
        this.timer = new Timer("Debounce", true);
        this.timer.schedule(new DebounceTask(this), 0, checkDelay);
    }

    private void checkExecute(){
        if((System.currentTimeMillis() - lastHit) > debounceDelay){
            this.timer.cancel();
            this.timer = null;
            execute();
        }
    }

    private class DebounceTask extends TimerTask {

        private DebouncedResizer debounceInstance = null;

        public DebounceTask(DebouncedResizer debounceInstance) {
            this.debounceInstance = debounceInstance;
        }

        @Override
        public void run() {
            debounceInstance.checkExecute();
        }
    }

}