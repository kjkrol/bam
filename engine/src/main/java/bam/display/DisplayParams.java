package bam.display;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.nonNull;

@ToString
@Getter
public class DisplayParams {

    private static final int DEFAULT_DISPLAY_WIDTH = 640;
    private static final int DEFAULT_DISPLAY_HEIGHT = 480;
    private static final String DEFAULT_TITLE = "BAM! <Demo>";
    private static final int DEFAULT_FPS_LIMIT = 60 * 4;

    private final int width;
    private final int height;
    private final String title;
    private final int fpsLimit;
    private final boolean fullscreen;

    @Builder
    private DisplayParams(Integer width, Integer height, String title, Integer fpsLimit, Boolean fullscreen) {
        this.width = nonNull(width) ? width : DEFAULT_DISPLAY_WIDTH;
        this.height = nonNull(height) ? height : DEFAULT_DISPLAY_HEIGHT;
        this.title = nonNull(title) ? title : DEFAULT_TITLE;
        this.fpsLimit = nonNull(fpsLimit) ? fpsLimit : DEFAULT_FPS_LIMIT;
        this.fullscreen = nonNull(fullscreen) ? fullscreen : false;
    }

}
