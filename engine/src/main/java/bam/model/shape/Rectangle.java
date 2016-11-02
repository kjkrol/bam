package bam.model.shape;

import bam.model.shape.base.BaseShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public class Rectangle extends BaseShape {

    private final float width;
    private final float height;

    public Rectangle(Body body, FixtureDef fixtureDef, float width, float height) {
        super(body, fixtureDef);
        this.width = width;
        this.height = height;
        init();
    }

    @Override
    protected Shape createShape() {
        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);
        return boxShape;
    }
}
