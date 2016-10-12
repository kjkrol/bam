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

    private Runnable cleanup;

    public BamDisplay(DisplayParams displayParams, Runnable cleanup) {
        this.openGlDisplay = new OpenGlDisplay(displayParams);
        this.cleanup = cleanup;
    }

    public void start(Runnable redrawing) {
        displayEngineScheduler.submit(openGlDisplay::init);
        displayEngineScheduler.scheduleAtFixedRate(() -> {
            if (openGlDisplay.isDisplayEnabled()) {
                openGlDisplay.draw(redrawing);
            } else {
                close();
            }
        }, DISPLAY_START_DELAY_TIME_IN_MS, DISPLAY_REFRESH_TIME_IN_MS, TimeUnit.MILLISECONDS);

    }

    public void close() {
        openGlDisplay.destroy();
        displayEngineScheduler.shutdown();
        cleanup.run();
    }

    public boolean isWorking() {
        return !displayEngineScheduler.isShutdown();
    }

    public float getDisplayWidth() {
        return openGlDisplay.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return openGlDisplay.getDisplayHeight();
    }

}
