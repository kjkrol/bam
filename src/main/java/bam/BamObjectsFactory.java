package bam;

import bam.commons.Function4Args;
import bam.objects.AbstractBamObject;
import bam.objects.Oval;
import bam.objects.Rect;
import lombok.Builder;
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

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class BamObjectsFactory {

    protected final Function<PhysicalBodyFactory.BodyBuilder, Body> createBody;
    protected final Function<AbstractBamObject, Boolean> bamObjectListAppender;

    public BamObjectsFactory(final Function<PhysicalBodyFactory.BodyBuilder, Body> createBody,
                             final Function<AbstractBamObject, Boolean> appendBamObjectsList) {
        this.createBody = createBody;
        this.bamObjectListAppender = appendBamObjectsList;
    }

    private <T extends AbstractBamObject> T create(final Vec2 position, final float[] params, final BodyType bodyType,
                                                   final FixtureDef templateFixture, Texture texture, ReadableColor color,
                                                   final Function<float[], Shape> shapeFunction,
                                                   final Function4Args<T, Body, Texture, ReadableColor, float[]> bamObjConstructor) {
        final Shape shape = shapeFunction.apply(params);
        final Body body = this.createBody.apply(
                PhysicalBodyFactory.builder()
                        .position(position)
                        .bodyType(bodyType)
                        .shape(shape)
                        .templateFixture(templateFixture)
        );
        final T t = bamObjConstructor.apply(body, texture, color, params);
        body.setUserData(t);
        this.bamObjectListAppender.apply(t);
        return t;
    }

    public Rect createRect(RectBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    public Oval createOval(OvalBuilder builder) {
        return builder.bamObjectsFactory(this).build();
    }

    @Builder(builderMethodName = "rectBuilder")
    private static Rect buildRect(Vec2 position, float width, float height, BodyType bodyType,
                                  FixtureDef templateFixture, Texture texture, ReadableColor color,
                                  BamObjectsFactory bamObjectsFactory) {

        return bamObjectsFactory.create(position, new float[]{width, height}, bodyType, templateFixture, texture, color,
                (float[] params) -> {
                    final PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(params[0], params[1]);
                    return boxShape;
                }, Rect::new);
    }


    @Builder(builderMethodName = "ovalBuilder")
    private static Oval createOval(Vec2 position, float radius, BodyType bodyType,
                                   FixtureDef templateFixture, Texture texture, ReadableColor color,
                                   BamObjectsFactory bamObjectsFactory) {
        return bamObjectsFactory.create(position, new float[]{radius}, bodyType, templateFixture, texture, color,
                (float[] params) -> {
                    final Shape circleShape = new CircleShape();
                    circleShape.setRadius(params[0]);
                    return circleShape;
                }, Oval::new);
    }
}
