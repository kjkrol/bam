package bam;

import bam.model.BaseBamType;
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
import org.newdawn.slick.opengl.Texture;

import java.util.function.Function;


public class BamObjectsFactory {

    private final PhysicalBodyFactory physicalBodyFactory;

    @Getter
    private final Function<BaseBamType, Boolean> bamObjectListAppender;

    BamObjectsFactory(final PhysicalBodyFactory physicalBodyFactory,
                      final Function<BaseBamType, Boolean> appendBamObjectsList) {
        this.physicalBodyFactory = physicalBodyFactory;
        this.bamObjectListAppender = appendBamObjectsList;
    }

    public Rect createRect(RectBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    public Oval createOval(OvalBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    private <T extends BaseBamType> T create(final Vec2 position, final float[] params, final BodyType bodyType,
                                             final FixtureDef fixtureDef, Texture texture, ReadableColor color,
                                             final Function<float[], Shape> shapeFunction,
                                             final Function4Args<T, Body, Texture, ReadableColor, float[]> bamObjConstructor) {
        final Shape shape = shapeFunction.apply(params);
        final PhysicalBodyParams physicalBodyParams = PhysicalBodyParams.builder()
                .bodyType(bodyType)
                .fixture(fixtureDef)
                .shape(shape)
                .build();
        final Body body = physicalBodyFactory.create(position, physicalBodyParams);
        final T t = bamObjConstructor.apply(body, texture, color, params);
        body.setUserData(t);
        this.bamObjectListAppender.apply(t);
        return t;
    }

    @Builder(builderMethodName = "rectBuilder")
    private static Rect buildRect(Vec2 position, float width, float height, BodyType bodyType,
                                  FixtureDef fixtureDef, Texture texture, ReadableColor color,
                                  BamObjectsFactory bamObjectsFactory) {

        return bamObjectsFactory.create(position, new float[]{width, height}, bodyType, fixtureDef, texture, color,
                (float[] params) -> {
                    final PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(params[0], params[1]);
                    return boxShape;
                }, Rect::new);
    }

    @Builder(builderMethodName = "ovalBuilder")
    private static Oval createOval(Vec2 position, float radius, BodyType bodyType,
                                   FixtureDef fixtureDef, Texture texture, ReadableColor color,
                                   BamObjectsFactory bamObjectsFactory) {
        return bamObjectsFactory.create(position, new float[]{radius}, bodyType, fixtureDef, texture, color,
                (float[] params) -> {
                    final Shape circleShape = new CircleShape();
                    circleShape.setRadius(params[0]);
                    return circleShape;
                }, Oval::new);
    }
}
