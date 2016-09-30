package bam.display;

public interface Displayable {

    void start();

    void stop();

    void redraw(Runnable redrawing);

    boolean isDisplayEnabled();

    float getDisplayWidth();

    float getDisplayHeight();
}
