package bam.display.opengl;

import bam.display.DisplayParams;
import lombok.extern.slf4j.Slf4j;
import nativelibs.NativeLibsBinder;
import nativelibs.NativeLibsJarIntrospectSearch;
import nativelibs.NativeLibsSearch;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    public void init() {
        if (!displayEnable.get()) {
            try {
                bindNativeLibs();
                Display.create();
                openGlSetup.setup();
                displayEnable.set(true);
            } catch (LWJGLException e) {
                log.info(e.getMessage(), e);
            }
        }
    }

    public void destroy() {
        if (displayEnable.get()) {
            Display.destroy();
        }
    }

    public void draw(Runnable redrawing) {
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

    public boolean isDisplayEnabled() {
        return displayEnable.get() && !Display.isCloseRequested();
    }

    public float getDisplayWidth() {
        return width;
    }

    public float getDisplayHeight() {
        return height;
    }

    private void bindNativeLibs() {
        Set<Path> pathStream;
        try {
            pathStream = new NativeLibsJarIntrospectSearch().getNativeLibraries();
        } catch (ProviderNotFoundException e) {
            log.error(e.getMessage());
            pathStream = new NativeLibsIDEIntrospectSearch().getNativeLibraries();
        }
        new NativeLibsBinder().bindLibs(pathStream);
    }

    private class NativeLibsIDEIntrospectSearch implements  NativeLibsSearch {

        @Override
        public Optional<Path> transform(Path targetPath) {
            final URL dirURL = OpenGlDisplay.class.getClassLoader().getResource(targetPath.toString());
            if (Objects.nonNull(dirURL)) {
                String dirName = dirURL.getFile();
                return Optional.of(new File(dirName).toPath());
            }
            return Optional.empty();
        }
    }
}
