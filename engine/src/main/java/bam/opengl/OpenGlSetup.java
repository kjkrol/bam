package bam.opengl;

import bam.nativelibs.NativeLibsBinder;
import bam.nativelibs.NativeLibsJarIntrospectSearch;
import bam.nativelibs.NativeLibsSearch;
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
    private final OpenGlConfiguration configuration;

    OpenGlSetup(OpenGlConfiguration configuration) {
        this.configuration = configuration;
        final NativeLibsBinder nativeLibsBinder = new NativeLibsBinder();
        final NativeLibsSearch nativeLibsSearch = new NativeLibsJarIntrospectSearch();
        nativeLibsBinder.bindLibs(nativeLibsSearch.getNativeLibraries());
        setupDisplay();
        log.info("OpenGlSetup constructed");
    }

    void setupGL11() {
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
    }

    private void setupDisplay() {
        try {
            final DisplayMode mode = GLUtil.getDisplayMode(configuration.getDisplayWidth(), configuration.getDisplayHeight());
            Display.setDisplayMode(mode);
            Display.setFullscreen(configuration.isFullscreen());
            Display.setTitle(configuration.getTitle());
            log.info("Setup Display: DONE");
        } catch (LWJGLException e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

}
