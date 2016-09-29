package bam.shape.request.base;

import bam.shape.model.base.AbstractShape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;


@RequiredArgsConstructor
@Getter
public abstract class CreateNewShapeRequest<T extends AbstractShape> {
    private final FixtureDef fixtureDef;
    private final ReadableColor color;

    public abstract T create(Body body);
}
