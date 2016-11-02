package bam.model.request.base;

import bam.model.shape.base.BaseShape;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;


@RequiredArgsConstructor
@Getter
public abstract class CreateNewShapeRequest<T extends BaseShape> {
    private final FixtureDef fixtureDef;
    private final ReadableColor color;

    public abstract T create(Body body);
}
