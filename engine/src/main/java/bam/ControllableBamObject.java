package bam;

import bam.shape.model.base.BaseShape;
import org.jbox2d.common.Vec2;

public interface ControllableBamObject {

    default void move(final BaseShape baseShape, final Vec2 delVelocity, final float freq) {
        float x = baseShape.getBody().getMass() * delVelocity.x * freq;
        float y = baseShape.getBody().getMass() * delVelocity.y * freq;
        baseShape.getBody().applyForceToCenter(new Vec2(x, y));
    }
}
