package bam.display.opengl;

import bam.display.DisplayParams;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

@ToString
@Slf4j
class OpenGlSetup {

    @Getter
    private final DisplayParams configuration;

    OpenGlSetup(DisplayParams configuration) {
        this.configuration = configuration;
    }

    void setup() {
        setupDisplay();
        setupGL11();
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
        GL11.glOrtho(0, configuration.getWidth(), 0, configuration.getHeight(), 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);
        GL11.glLoadIdentity();
        log.info("setup OpenGl : done");
    }

    private void setupDisplay() {
        try {
            log.info("setup BamDisplay : starting");
            final DisplayMode displayMode = detectDisplayMode();
            log.info("set display mode: {}", displayMode);
            Display.setDisplayMode(displayMode);
            Display.setFullscreen(configuration.isFullscreen());
            Display.setTitle(configuration.getTitle());
            log.info("setup BamDisplay : done");
        } catch (LWJGLException e) {
            log.error("setup BamDisplay : error");
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

    private DisplayMode detectDisplayMode() throws LWJGLException {
        final int bpp = Display.getDisplayMode().getBitsPerPixel();
        final int width = configuration.getWidth();
        final int height = configuration.getHeight();
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        return Arrays.stream(modes)
                .filter(mode -> mode.getBitsPerPixel() == bpp)
                .filter(mode -> mode.getWidth() == width)
                .filter(mode -> mode.getHeight() == height)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("DisplayMode no available"));
    }

}
