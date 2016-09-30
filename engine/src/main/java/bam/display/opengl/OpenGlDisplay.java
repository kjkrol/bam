package bam.display.opengl;

import bam.display.base.Displayable;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OpenGlDisplay implements Displayable {

    private AtomicBoolean displayEnable = new AtomicBoolean();

    private final OpenGlSetup openGlSetup;

    private final int fpsLimit;

    private final float width;

    private final float height;

    public OpenGlDisplay(DisplayConfiguration configuration) {
        fpsLimit = configuration.getFpsLimit();
        this.width = configuration.getDisplayWidth();
        this.height = configuration.getDisplayHeight();
        openGlSetup = new OpenGlSetup(configuration);
    }

    @Override
    public void startDisplay() {
        if (displayEnable.compareAndSet(false, true)) {
            try {
                openGlSetup.setup();
                Display.create();
            } catch (LWJGLException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public void closeDisplay() {
        if (displayEnable.get()) {
            Display.destroy();
        }
    }

    @Override
    public void redraw(Runnable redrawing) {
        /* Clear The Screen And The Depth Buffer */
        if (displayEnable.get()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            redrawing.run();

            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();
            Display.sync(fpsLimit);
            Display.update();
        }
    }

    @Override
    public boolean isDisplayEnabled() {
        return displayEnable.get() && !Display.isCloseRequested();
    }

    @Override
    public float getDisplayWidth() {
        return width;
    }

    @Override
    public float getDisplayHeight() {
        return height;
    }
}
