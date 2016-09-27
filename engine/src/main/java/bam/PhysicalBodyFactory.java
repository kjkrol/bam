package bam;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import java.util.function.Function;

class PhysicalBodyFactory {

    private final Function<BodyDef, Body> createBody;

    PhysicalBodyFactory(Function<BodyDef, Body> createBody) {
        this.createBody = createBody;
    }


    Body create(Vec2 position, PhysicalBodyParams physicalBodyParams) {
        final BodyDef bodyDef = createBodyDef(position, physicalBodyParams.getBodyType());
        final Body body = createBody.apply(bodyDef);
        final FixtureDef fixtureDef = createFixtureDef(physicalBodyParams);
        body.createFixture(fixtureDef);
        return body;
    }

    private FixtureDef createFixtureDef(PhysicalBodyParams physicalBodyParams) {
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = physicalBodyParams.getShape();
        fixtureDef.density = physicalBodyParams.getFixture().density;
        fixtureDef.friction = physicalBodyParams.getFixture().friction;
        fixtureDef.restitution = physicalBodyParams.getFixture().restitution;
        return fixtureDef;
    }

    private BodyDef createBodyDef(Vec2 position, BodyType bodyType) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        return bodyDef;
    }

}
