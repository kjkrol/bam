package bam;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

@FunctionalInterface
interface PhysicalBodyFactory {

    Body createBody(BodyDef bodyDef);

    default Body create(Vec2 position, PhysicalBodyParams physicalBodyParams) {
        final BodyDef bd = new BodyDef();
        bd.type = physicalBodyParams.getBodyType();
        bd.position.set(position);

        final FixtureDef fd = new FixtureDef();
        fd.shape = physicalBodyParams.getShape();
        fd.density = physicalBodyParams.getFixture().density;
        fd.friction = physicalBodyParams.getFixture().friction;
        fd.restitution = physicalBodyParams.getFixture().restitution;

        final Body body = createBody(bd);
        body.createFixture(fd);

        return body;
    }

}
