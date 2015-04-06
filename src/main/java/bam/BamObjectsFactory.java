package bam;

import bam.commons.Function4Args;
import bam.objects.AbstractBamObject;
import bam.objects.Oval;
import bam.objects.Rect;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class BamObjectsFactory {

    protected final Function4Args<Body, Vec2, Shape, BodyType, FixtureDef> createBody;
    protected final Function<AbstractBamObject, Boolean> bamObjectListAppender;

    /**
     *
     * @param createBody
     * @param appendBamObjectsList
     */
    public BamObjectsFactory(final Function4Args<Body, Vec2, Shape, BodyType, FixtureDef> createBody,
                             final Function<AbstractBamObject, Boolean> appendBamObjectsList) {
        this.createBody = createBody;
        this.bamObjectListAppender = appendBamObjectsList;
    }

    /**
     *
     * @param position
     * @param params
     * @param bodyType
     * @param templateFixture
     * @param texture
     * @param color
     * @param shapeFunction
     * @param bamObjConstructor
     * @param <T>
     * @return
     */
    private <T extends AbstractBamObject> T create(final Vec2 position, final float[] params, final BodyType bodyType,
                                                   final FixtureDef templateFixture, Optional<Texture> texture, Optional<ReadableColor> color,
                                                   final Function<float[], Shape> shapeFunction,
                                                   final Function4Args<T, Body, Optional<Texture>, Optional<ReadableColor>, float[]> bamObjConstructor) {

        final Shape shape = shapeFunction.apply(params);
        final Body body = this.createBody.apply(position, shape, bodyType, templateFixture);
        final T t = bamObjConstructor.apply(body, texture, color, params);
        body.setUserData(t);
        this.bamObjectListAppender.apply(t);
        return t;
    }

    /**
     *
     * @param position
     * @param width
     * @param height
     * @param bodyType
     * @param templateFixture
     * @param texture
     * @param color
     * @return
     */
    public Rect createRect(Vec2 position, float width, float height, BodyType bodyType,
                           FixtureDef templateFixture, Optional<Texture> texture, Optional<ReadableColor> color) {
        return this.create(position, new float[]{width, height}, bodyType, templateFixture, texture, color,
                (float[] params) -> {
                    final PolygonShape boxShape = new PolygonShape();
                    boxShape.setAsBox(params[0], params[1]);
                    return boxShape;
                }, Rect::new);
    }

    /**
     *
     * @param position
     * @param radius
     * @param bodyType
     * @param templateFixture
     * @param texture
     * @param color
     * @return
     */
    public Oval createOval(Vec2 position, float radius, BodyType bodyType,
                           FixtureDef templateFixture, Optional<Texture> texture, Optional<ReadableColor> color) {
        return this.create(position, new float[]{radius}, bodyType, templateFixture, texture, color,
                (float[] params) -> {
                    final Shape circleShape = new CircleShape();
                    circleShape.setRadius(params[0]);
                    return circleShape;
                }, Oval::new);
    }
}
