package bam.shape.request;

import bam.shape.model.Circle;
import bam.shape.request.base.CreateNewShapeRequest;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;

public class CreateNewCircleRequest extends CreateNewShapeRequest<Circle> {

    private final float radius;

    public CreateNewCircleRequest(FixtureDef fixtureDef, ReadableColor color, float radius) {
        super(fixtureDef, color);
        this.radius = radius;
    }

    @Override
    public Circle create(Body body) {
        return new Circle(body, getFixtureDef(), getColor(), radius);
    }
}
