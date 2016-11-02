package bam.display.opengl.shape;

import bam.display.opengl.shape.base.BaseOpenGlShape;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public class CircleOQL extends BaseOpenGlShape {

    private static final float HALF = 0.5f;

    private static final int POINT_SIZE = 4;

    private final float radius;

    private final float[][] edges;

    protected CircleOQL(ReadableColor color, float radius, float[][] edges) {
        super(color);
        this.radius = radius;
        this.edges = edges;
    }

    @Override
    protected void drawShape() {

        GL11.glPointSize(POINT_SIZE);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, radius);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, radius);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int index = 0; index < edges.length; ++index) {
            GL11.glVertex2f(edges[index][0], edges[index][1]);
        }
        GL11.glEnd();

        final ReadableColor color = getColor();
        GL11.glBegin(GL11.GL_POLYGON);
        if (nonNull(color)) {
            GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), HALF);
        }
        for (int index = 0; index < edges.length; ++index) {
            GL11.glVertex2f(edges[index][0], edges[index][1]);
        }

        GL11.glEnd();
    }
}
