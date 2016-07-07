package bam;

import bam.model.BaseBamType;
import org.jbox2d.common.Vec2;

public interface ControllableBamObject {

    default void move(final BaseBamType baseBamType, final Vec2 delVelocity, final float freq) {
        float x = baseBamType.getBody().getMass() * delVelocity.x * freq;
        float y = baseBamType.getBody().getMass() * delVelocity.y * freq;
        baseBamType.getBody().applyForceToCenter(new Vec2(x, y));
    }
}
