package bam.opengl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


import java.awt.Font;
import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GLUtil {

    /**
     * Loads font
     */
    public static TrueTypeFont getFont(final String fontName) {
        final Font awtFont = new Font(fontName, Font.BOLD, 16);
        return new TrueTypeFont(awtFont, false);
    }

    public enum ImageType {
        PNG, JPG
    }

    /**
     * Loads texture from PNG file
     */
    public static Texture getTexture(final String pathToFile, ImageType imageType) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture(imageType.name(), ResourceLoader.getResourceAsStream(pathToFile));
            log.info("Texture loaded: " + texture);
            log.info(">> Image width: " + texture.getImageWidth());
            log.info(">> Image height: " + texture.getImageHeight());
            log.info(">> Texture width: " + texture.getTextureWidth());
            log.info(">> Texture height: " + texture.getTextureHeight());
            log.info(">> Texture ID: " + texture.getTextureID());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return texture;
    }

    public static DisplayMode getDisplayMode(int width, int height) throws LWJGLException {
        /* find out what the current bits per pixel of the desktop is */
        final int bpp = Display.getDisplayMode().getBitsPerPixel();
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode mode = null;

        for (DisplayMode mode1 : modes) {
            if ((mode1.getBitsPerPixel() == bpp) || (mode == null)) {
                if ((mode1.getWidth() == width) && (mode1.getHeight() == height)) {
                    mode = mode1;
                }
            }
        }
        return mode;
    }

}
