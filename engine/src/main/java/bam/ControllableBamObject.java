package bam;

import bam.shape.model.base.AbstractShape;
import org.jbox2d.common.Vec2;

public interface ControllableBamObject {

    default void move(final AbstractShape abstractShape, final Vec2 delVelocity, final float freq) {
        float x = abstractShape.getBody().getMass() * delVelocity.x * freq;
        float y = abstractShape.getBody().getMass() * delVelocity.y * freq;
        abstractShape.getBody().applyForceToCenter(new Vec2(x, y));
    }
}
