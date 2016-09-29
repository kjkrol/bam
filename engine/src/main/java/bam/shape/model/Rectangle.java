package bam.shape.model;

import bam.shape.model.base.AbstractShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public class Rectangle extends AbstractShape {
    private static final float HALF = 0.5f;
    private final float width;
    private final float height;

    public Rectangle(Body body, FixtureDef fixtureDef, ReadableColor color, float width, float height) {
        super(body, fixtureDef, color);
        this.width = width;
        this.height = height;
        init();
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

    @Override
    protected Shape createShape() {
        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);
        return boxShape;
    }
}
