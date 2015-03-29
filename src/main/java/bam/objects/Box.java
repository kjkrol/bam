package bam.objects;

import lombok.Data;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

import java.util.Optional;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Box extends AbstractBamObject {

    public static final int DEFAULT_SIZE = 30;

    protected final float width;

    protected final float height;

    public Box(Body body, Texture texture, float width, float height) {
        super(body, Optional.of(texture), Optional.empty());
        this.width = width;
        this.height = height;
    }

    public Box(Body body, ReadableColor color, float width, float height) {
        super(body, Optional.empty(), Optional.of(color));
        this.width = height;
        this.height = width;
    }

    @Override
    protected void drawTexture() {

        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(-width, -height);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(+width, -height);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(+width, +height);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(-width, +height);

        GL11.glEnd();
    }

    @Override
    protected void drawShape() {

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(-width, -height);
        GL11.glVertex2f(+width, -height);
        GL11.glVertex2f(+width, +height);
        GL11.glVertex2f(-width, +height);
        GL11.glEnd();

        this.color.ifPresent(c -> GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), 0.5f));
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2f(-width, -height);
        GL11.glVertex2f(+width, -height);
        GL11.glVertex2f(+width, +height);
        GL11.glVertex2f(-width, +height);
        GL11.glEnd();
    }

}
