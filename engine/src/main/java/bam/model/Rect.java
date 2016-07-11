package bam.model;

import lombok.Getter;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

public class Rect extends BaseBamType {

    public static final int DEFAULT_SIZE = 30;

    private static final float HALF = 0.5f;

    @Getter
    private final float width;

    @Getter
    private final float height;

    public Rect(Body body, Texture texture, ReadableColor color, float width, float height) {
        super(body, texture, color);
        this.width = width;
        this.height = height;
    }

    public Rect(Body body, Texture texture, ReadableColor color, float[] param) {
        this(body, texture, color, param[0], param[1]);
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

        final ReadableColor color = this.getColor();
        if (null != color) {
            GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), HALF);
        }
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2f(-width, -height);
        GL11.glVertex2f(+width, -height);
        GL11.glVertex2f(+width, +height);
        GL11.glVertex2f(-width, +height);
        GL11.glEnd();
    }

}
