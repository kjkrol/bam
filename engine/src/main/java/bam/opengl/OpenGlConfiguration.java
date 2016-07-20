package bam.opengl;

import bam.model.BaseBamType;
import bam.nativelibs.NativeLibsBinder;
import bam.nativelibs.NativeLibsJarIntrospectSearch;
import bam.nativelibs.NativeLibsSearch;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@ToString
@Slf4j
public class OpenGlConfiguration {
    private static final int DEFAULT_DISPLAY_WIDTH = 640;
    private static final int DEFAULT_DISPLAY_HEIGHT = 480;
    private static final String DEFAULT_TITLE = "BAM! <Demo>";
    private static final int FPS_LIMIT = 60 * 4;
    @Setter
    @Getter
    private int displayWith = DEFAULT_DISPLAY_WIDTH;
    @Setter
    @Getter
    private int displayHeight = DEFAULT_DISPLAY_HEIGHT;
    @Setter
    @Getter
    private String title = DEFAULT_TITLE;
    @Setter
    private boolean fullscreen;
    private AtomicBoolean displayEnable = new AtomicBoolean();

    public OpenGlConfiguration() {
        final NativeLibsBinder nativeLibsBinder = new NativeLibsBinder();
        final NativeLibsSearch nativeLibsSearch = new NativeLibsJarIntrospectSearch();
        nativeLibsBinder.bindLibs(nativeLibsSearch.getNativeLibraries());
        setupDisplay();
    }

    public boolean isDisplayEnabled() {
        return displayEnable.get() && !Display.isCloseRequested();
    }

    public void startDisplay() {
        if (displayEnable.compareAndSet(false, true)) {
            try {
                Display.create();
                setupGL11();
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

    // this is not a part of configuration
    public void refreshView(List<BaseBamType> bamObjects) {
        /* Clear The Screen And The Depth Buffer */
        if (displayEnable.get()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();
            bamObjects.forEach(BaseBamType::draw);
            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();
            Display.sync(FPS_LIMIT);
            Display.update();
        }
    }

    private void setupDisplay() {
        try {
            final DisplayMode mode = GLUtil.getDisplayMode(displayWith, displayHeight);
            Display.setDisplayMode(mode);
            Display.setFullscreen(fullscreen);
            Display.setTitle(title);
        } catch (LWJGLException e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

    private void setupGL11() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, DEFAULT_DISPLAY_WIDTH, 0, DEFAULT_DISPLAY_HEIGHT, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);
        GL11.glLoadIdentity();
    }
}
