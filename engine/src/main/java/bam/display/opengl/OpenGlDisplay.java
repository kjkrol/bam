package bam.display.opengl;

import bam.display.DisplayParams;
import lombok.extern.slf4j.Slf4j;
import nativelibs.NativeLibsBinder;
import nativelibs.NativeLibsJarIntrospectSearch;
import nativelibs.NativeLibsSearch;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class OpenGlDisplay {

    private AtomicBoolean displayEnable = new AtomicBoolean();

    private final OpenGlSetup openGlSetup;

    private final int fpsLimit;

    private final float width;

    private final float height;

    public OpenGlDisplay(DisplayParams displayParams) {
        fpsLimit = displayParams.getFpsLimit();
        width = displayParams.getWidth();
        height = displayParams.getHeight();
        openGlSetup = new OpenGlSetup(displayParams);
    }

    public void start() {
        if (displayEnable.compareAndSet(false, true)) {
            try {
                bindNativeLibs();
                org.lwjgl.opengl.Display.create();
                openGlSetup.setup();
            } catch (LWJGLException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    public void stop() {
        if (displayEnable.get()) {
            org.lwjgl.opengl.Display.destroy();
        }
    }

    public void redraw(Runnable redrawing) {
        if (displayEnable.get()) {
            // Clear The Screen And The Depth Buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();
            redrawing.run();
            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();
            org.lwjgl.opengl.Display.sync(fpsLimit);
            org.lwjgl.opengl.Display.update();
        }
    }

    public boolean isDisplayEnabled() {
        return displayEnable.get() && !org.lwjgl.opengl.Display.isCloseRequested();
    }

    public float getDisplayWidth() {
        return width;
    }

    public float getDisplayHeight() {
        return height;
    }

    private void bindNativeLibs() {
        final NativeLibsBinder nativeLibsBinder = new NativeLibsBinder();
        final NativeLibsSearch nativeLibsSearch = new NativeLibsJarIntrospectSearch();
        nativeLibsBinder.bindLibs(nativeLibsSearch.getNativeLibraries());
    }
}
