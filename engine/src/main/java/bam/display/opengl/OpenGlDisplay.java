package bam.display.opengl;

import bam.display.DisplayConfiguration;
import bam.display.Displayable;
import lombok.extern.slf4j.Slf4j;
import nativelibs.NativeLibsBinder;
import nativelibs.NativeLibsJarIntrospectSearch;
import nativelibs.NativeLibsSearch;
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
        width = configuration.getDisplayWidth();
        height = configuration.getDisplayHeight();
        openGlSetup = new OpenGlSetup(configuration);
    }

    @Override
    public void start() {
        if (displayEnable.compareAndSet(false, true)) {
            try {
                bindNativeLibs();
                Display.create();
                openGlSetup.setup();
            } catch (LWJGLException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    @Override
    public void stop() {
        if (displayEnable.get()) {
            Display.destroy();
        }
    }

    @Override
    public void redraw(Runnable redrawing) {
        if (displayEnable.get()) {
            // Clear The Screen And The Depth Buffer
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

    private void bindNativeLibs() {
        final NativeLibsBinder nativeLibsBinder = new NativeLibsBinder();
        final NativeLibsSearch nativeLibsSearch = new NativeLibsJarIntrospectSearch();
        nativeLibsBinder.bindLibs(nativeLibsSearch.getNativeLibraries());
    }
}
