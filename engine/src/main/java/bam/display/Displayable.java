package bam.display;

public interface Displayable {

    void startDisplay();

    void closeDisplay();

    void redraw(Runnable redrawing);

    boolean isDisplayEnabled();

    float getDisplayWidth();

    float getDisplayHeight();
}
