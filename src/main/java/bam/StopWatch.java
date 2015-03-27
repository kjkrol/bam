package bam;

import org.lwjgl.Sys;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class StopWatch {


    private static final int ONE_MINUTE_IN_MS = 1000;

    /**
     * time at last frame
     */
    private long lastMeasurement;

    /**
     * Calculate how many milliseconds have passed
     * since last frame.
     *
     * @return milliseconds passed since last frame
     */
    public int getDelta() {
        final long time = getTime();
        int delta = (int) (time - lastMeasurement);
        lastMeasurement = time;
        return delta;
    }

    /**
     * Get the accurate system time
     *
     * @return The system time in milliseconds
     */
    private long getTime() {
        return (Sys.getTime() * ONE_MINUTE_IN_MS) / Sys.getTimerResolution();
    }
}
