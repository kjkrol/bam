package bam.opengl;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class OpenGlConfiguration {

    private static final int DEFAULT_DISPLAY_WIDTH = 640;
    private static final int DEFAULT_DISPLAY_HEIGHT = 480;
    private static final String DEFAULT_TITLE = "BAM! <Demo>";
    private static final int DEFAULT_FPS_LIMIT = 60 * 4;

    private final int displayWidth;
    private final int displayHeight;
    private final String title;
    private final int fpsLimit;
    private final boolean fullscreen;

    @Builder
    private OpenGlConfiguration(Integer displayWidth, Integer displayHeight, String title, Integer fpsLimit,
                                Boolean fullscreen) {
        this.displayWidth = (displayWidth != null ? displayWidth : DEFAULT_DISPLAY_WIDTH);
        this.displayHeight = (displayHeight != null ? displayHeight : DEFAULT_DISPLAY_HEIGHT);
        this.title = (title != null ? title : DEFAULT_TITLE);
        this.fpsLimit = (fpsLimit != null ? fpsLimit : DEFAULT_FPS_LIMIT);
        this.fullscreen = (fullscreen != null ? fullscreen : false);
    }

}
