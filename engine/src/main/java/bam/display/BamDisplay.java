package bam.display;

import bam.display.opengl.OpenGlDisplay;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BamDisplay {

    private static final int DISPLAY_START_DELAY_TIME_IN_MS = 50;
    private static final int DISPLAY_REFRESH_TIME_IN_MS = 20;

    private final OpenGlDisplay openGlDisplay;

    private final ScheduledExecutorService displayEngineScheduler = Executors.newSingleThreadScheduledExecutor();

    public BamDisplay(DisplayParams displayParams) {
        this.openGlDisplay = new OpenGlDisplay(displayParams);
    }

    public void start(Runnable redrawing) {
        displayEngineScheduler.submit(() -> openGlDisplay.init());
        displayEngineScheduler.scheduleAtFixedRate(() -> openGlDisplay.draw(redrawing),
                DISPLAY_START_DELAY_TIME_IN_MS, DISPLAY_REFRESH_TIME_IN_MS, TimeUnit.MILLISECONDS);
    }

    public float getDisplayWidth() {
        return openGlDisplay.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return openGlDisplay.getDisplayHeight();
    }

}
