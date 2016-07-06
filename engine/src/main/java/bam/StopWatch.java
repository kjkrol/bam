package bam;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class StopWatch {

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
        final long time = System.currentTimeMillis();
        final int delta = (int) (time - this.lastMeasurement);
        this.lastMeasurement = time;
        return delta;
    }

}
