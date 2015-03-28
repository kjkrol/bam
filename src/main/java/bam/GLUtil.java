package bam;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class GLUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GLUtil.class);

    /**
     * Loads font.
     *
     * @return
     */
    public static TrueTypeFont getFont(final String fontName) {
        final Font awtFont = new Font(fontName, Font.BOLD, 16);
        return new TrueTypeFont(awtFont, false);
    }

    public enum ImageType {
        PNG, JPG;
    }

    /**
     * Loads texture from PNG file
     *
     * @param pathToFile
     * @return
     */
    public static Texture getTexture(final String pathToFile, ImageType imageType) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture(imageType.name(), ResourceLoader.getResourceAsStream(pathToFile));

            LOGGER.info("Texture loaded: " + texture);
            LOGGER.info(">> Image width: " + texture.getImageWidth());
            LOGGER.info(">> Image height: " + texture.getImageHeight());
            LOGGER.info(">> Texture width: " + texture.getTextureWidth());
            LOGGER.info(">> Texture height: " + texture.getTextureHeight());
            LOGGER.info(">> Texture ID: " + texture.getTextureID());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return texture;
    }

    /**
     * @param width
     * @param height
     * @return
     * @throws LWJGLException
     */
    public static DisplayMode getDisplayMode(int width, int height) throws LWJGLException {
        /* find out what the current bits per pixel of the desktop is */
        final int bpp = Display.getDisplayMode().getBitsPerPixel();
        final DisplayMode[] modes = Display.getAvailableDisplayModes();
        DisplayMode mode = null;

        for (int index = 0; index < modes.length; index++) {
            if ((modes[index].getBitsPerPixel() == bpp) || (mode == null)) {
                if ((modes[index].getWidth() == width) && (modes[index].getHeight() == height)) {
                    mode = modes[index];
                }
            }
        }
        return mode;
    }

}
