package bam.display.opengl.shape;

import bam.display.opengl.shape.base.BaseOpenGlShape;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public class RectangleOQL extends BaseOpenGlShape {

    private static final float HALF = 0.5f;
    private final float width;
    private final float height;

    protected RectangleOQL(ReadableColor color, float width, float height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void drawShape() {
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(-width, -height);
        GL11.glVertex2f(+width, -height);
        GL11.glVertex2f(+width, +height);
        GL11.glVertex2f(-width, +height);
        GL11.glEnd();
        final ReadableColor color = getColor();
        if (nonNull(color)) {
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
