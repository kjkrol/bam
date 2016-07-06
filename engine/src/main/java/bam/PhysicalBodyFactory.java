package bam;

import lombok.Builder;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public class PhysicalBodyFactory {

    private final World world;

    public PhysicalBodyFactory(final World world) {
        this.world = world;
    }


    public Body createBody(BodyBuilder builder) {
        return builder.physicalBodyFactory(this).build();
    }

    @Builder
    private static Body buildBody(final Vec2 position, final Shape shape, final BodyType bodyType,
                                  final FixtureDef templateFixture, final PhysicalBodyFactory physicalBodyFactory) {

        final BodyDef bd = new BodyDef();
        bd.type = bodyType;
        bd.position.set(position);

        final FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = templateFixture.density;
        fd.friction = templateFixture.friction;
        fd.restitution = templateFixture.restitution;

        final Body body = physicalBodyFactory.world.createBody(bd);
        body.createFixture(fd);
        return body;
    }
}
