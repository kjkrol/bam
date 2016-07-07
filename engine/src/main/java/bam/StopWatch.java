package bam;

class StopWatch {

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
    int getDelta() {
        final long time = System.currentTimeMillis();
        final int delta = (int) (time - this.lastMeasurement);
        this.lastMeasurement = time;
        return delta;
    }

}
