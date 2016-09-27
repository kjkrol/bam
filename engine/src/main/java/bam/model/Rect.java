package bam.model;

import bam.OpenGlModelParams;
import bam.model.base.BaseModel;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public class Rect extends BaseModel {
    private static final float HALF = 0.5f;
    private final float width;
    private final float height;

    public Rect(OpenGlModelParams openGlModelParams) {
        this(openGlModelParams.getBody(),
                openGlModelParams.getColor(),
                openGlModelParams.getParams()[0],
                openGlModelParams.getParams()[1]);
    }

    private Rect(Body body, ReadableColor color, float width, float height) {
        super(body, color);
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
