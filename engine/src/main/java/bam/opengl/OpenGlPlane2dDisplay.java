package bam.opengl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OpenGlPlane2dDisplay {

    private AtomicBoolean displayEnable = new AtomicBoolean();

    @Getter
    private final OpenGlConfiguration configuration;

    private final OpenGlSetup openGlSetup;

    public OpenGlPlane2dDisplay(OpenGlConfiguration configuration) {
        this.configuration = configuration;
        openGlSetup = new OpenGlSetup(configuration);
    }

    public boolean isDisplayEnabled() {
        return displayEnable.get() && !Display.isCloseRequested();
    }

    public float getDisplayWidth() {
        return configuration.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return configuration.getDisplayHeight();
    }

    public void startDisplay() {
        if (displayEnable.compareAndSet(false, true)) {
            try {
                Display.create();
                openGlSetup.setupGL11();
                new OpenGlSetup(configuration).setupGL11();
            } catch (LWJGLException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    public void closeDisplay() {
        if (displayEnable.get()) {
            Display.destroy();
        }
    }

    public void redraw(Redrawing redrawing) {
        /* Clear The Screen And The Depth Buffer */
        if (displayEnable.get()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();

            redrawing.redraw();

            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();
            Display.sync(configuration.getFpsLimit());
            Display.update();
        }
    }

    @FunctionalInterface
    public interface Redrawing {
        void redraw();
    }
}
