package bam.display.opengl;

import bam.display.DisplayConfiguration;
import nativelibs.NativeLibsBinder;
import nativelibs.NativeLibsJarIntrospectSearch;
import nativelibs.NativeLibsSearch;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

@ToString
@Slf4j
class OpenGlSetup {

    @Getter
    private final DisplayConfiguration configuration;

    OpenGlSetup(DisplayConfiguration configuration) {
        this.configuration = configuration;
    }

    void setup() {
        bindNativeLibs();
        setupDisplay();
        setupGL11();
    }

    private void bindNativeLibs() {
        final NativeLibsBinder nativeLibsBinder = new NativeLibsBinder();
        final NativeLibsSearch nativeLibsSearch = new NativeLibsJarIntrospectSearch();
        nativeLibsBinder.bindLibs(nativeLibsSearch.getNativeLibraries());
    }

    private void setupGL11() {
        log.info("setup OpenGl : starting");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, configuration.getDisplayWidth(), 0, configuration.getDisplayHeight(), 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);
        GL11.glLoadIdentity();
        log.info("setup OpenGl : done");
    }

    private void setupDisplay() {
        try {
            log.info("setup Displayable : starting");
            final DisplayMode mode = detectDisplayMode();
            Display.setDisplayMode(mode);
            Display.setFullscreen(configuration.isFullscreen());
            Display.setTitle(configuration.getTitle());
            log.info("setup Displayable : done");
        } catch (LWJGLException e) {
            log.error("setup Displayable : error");
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

    // TODO: it's so badly (ugly) written... pls fix this
    private DisplayMode detectDisplayMode() throws LWJGLException {
        /* find out what the current bits per pixel of the desktop is */
        final int bpp = Display.getDisplayMode().getBitsPerPixel();
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode mode = null;

        for (DisplayMode mode1 : modes) {
            if ((mode1.getBitsPerPixel() == bpp) || (mode == null)) {
                int width = configuration.getDisplayWidth();
                int height = configuration.getDisplayHeight();
                if ((mode1.getWidth() == width) && (mode1.getHeight() == height)) {
                    mode = mode1;
                }
            }
        }
        return mode;
    }

}
