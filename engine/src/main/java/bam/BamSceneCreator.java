package bam;

import bam.shape.model.base.BaseShape;
import bam.shape.request.base.CreateNewShapeRequest;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import java.util.function.Function;

public class BamSceneCreator {

    private final Function<BodyDef, Body> createWorldsBody;

    private final Function<BaseShape, Boolean> shapesListAppender;

    BamSceneCreator(Function<BaseShape, Boolean> shapesListAppender,
                    Function<BodyDef, Body> createWorldsBody) {
        this.shapesListAppender = shapesListAppender;
        this.createWorldsBody = createWorldsBody;
    }

    public <T extends BaseShape> void addShape(CreateNewShapeRequest<T> request,
                                               Vec2 position, BodyType bodyType) {
        final Body body = create(position, bodyType);
        final T shape = request.create(body);
        shapesListAppender.apply(shape);
    }

    private Body create(Vec2 position, BodyType bodyType) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        return createWorldsBody.apply(bodyDef);
    }


}
