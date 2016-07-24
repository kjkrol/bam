package bam;

import bam.model.BaseModel;
import bam.model.Oval;
import bam.model.Rect;
import lombok.Builder;
import lombok.Getter;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;

import java.util.function.Function;


public class BamObjectsFactory {

    private final PhysicalBodyFactory physicalBodyFactory;

    @Getter
    private final Function<BaseModel, Boolean> bamObjectListAppender;

    BamObjectsFactory(final PhysicalBodyFactory physicalBodyFactory,
                      final Function<BaseModel, Boolean> appendBamObjectsList) {
        this.physicalBodyFactory = physicalBodyFactory;
        this.bamObjectListAppender = appendBamObjectsList;
    }

    public Rect createRect(RectBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    public Oval createOval(OvalBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    private <T extends BaseModel> T create(final Vec2 position,
                                           final float[] params,
                                           final BodyType bodyType,
                                           final FixtureDef fixtureDef,
                                           final ReadableColor color,
                                           final Function<float[], Shape> shapeFunction,
                                           final Function<OpenGlModelParams, T> bamObjConstructor) {
        final Shape shape = shapeFunction.apply(params);
        final PhysicalBodyParams physicalBodyParams = PhysicalBodyParams.builder()
                .bodyType(bodyType)
                .fixture(fixtureDef)
                .shape(shape)
                .build();
        final Body body = physicalBodyFactory.create(position, physicalBodyParams);
        final OpenGlModelParams openGlModelParams = OpenGlModelParams.builder()
                .body(body)
                .color(color)
                .params(params)
                .build();
        final T t = bamObjConstructor.apply(openGlModelParams);
        body.setUserData(t);
        this.bamObjectListAppender.apply(t);
        return t;
    }

    @Builder(builderMethodName = "rectBuilder")
    private static Rect buildRect(Vec2 position, float width, float height, BodyType bodyType,
                                  FixtureDef fixtureDef, ReadableColor color,
                                  BamObjectsFactory bamObjectsFactory) {

        return bamObjectsFactory.create(position, new float[]{width, height}, bodyType, fixtureDef, color,
                (float[] params) -> {
                    final PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(params[0], params[1]);
                    return boxShape;
                }, Rect::new);
    }

    @Builder(builderMethodName = "ovalBuilder")
    private static Oval createOval(Vec2 position, float radius, BodyType bodyType,
                                   FixtureDef fixtureDef, ReadableColor color,
                                   BamObjectsFactory bamObjectsFactory) {
        return bamObjectsFactory.create(position, new float[]{radius}, bodyType, fixtureDef, color,
                (float[] params) -> {
                    final Shape circleShape = new CircleShape();
                    circleShape.setRadius(params[0]);
                    return circleShape;
                }, Oval::new);
    }
}
