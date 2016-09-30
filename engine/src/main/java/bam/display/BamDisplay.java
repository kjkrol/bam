package bam.display;

import bam.display.base.Displayable;
import bam.display.opengl.DisplayConfiguration;
import bam.display.opengl.OpenGlDisplay;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BamDisplay implements Displayable {

    private final Displayable openGlDisplay;

    public BamDisplay(DisplayConfiguration configuration) {
        this.openGlDisplay = new OpenGlDisplay(configuration);
    }

    @Override
    public void startDisplay() {
        openGlDisplay.startDisplay();
    }

    @Override
    public void closeDisplay() {
        openGlDisplay.closeDisplay();
    }

    @Override
    public void redraw(Runnable redraw) {
        openGlDisplay.redraw(redraw);
    }

    @Override
    public boolean isDisplayEnabled() {
        return openGlDisplay.isDisplayEnabled();
    }

    @Override
    public float getDisplayWidth() {
        return openGlDisplay.getDisplayWidth();
    }

    @Override
    public float getDisplayHeight() {
        return openGlDisplay.getDisplayHeight();
    }

}
