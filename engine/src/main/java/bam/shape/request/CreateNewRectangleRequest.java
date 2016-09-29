package bam.shape.request;


import bam.shape.model.Rectangle;
import bam.shape.request.base.CreateNewShapeRequest;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;

public class CreateNewRectangleRequest extends CreateNewShapeRequest<Rectangle> {

    private final float width;
    private final float height;

    public CreateNewRectangleRequest(FixtureDef fixtureDef, ReadableColor color, float width, float height) {
        super(fixtureDef, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public Rectangle create(Body body) {
        return new Rectangle(body, getFixtureDef(), getColor(), width, height);
    }
}
