package bam;

import bam.model.BaseModel;
import org.jbox2d.common.Vec2;

public interface ControllableBamObject {

    default void move(final BaseModel baseModel, final Vec2 delVelocity, final float freq) {
        float x = baseModel.getBody().getMass() * delVelocity.x * freq;
        float y = baseModel.getBody().getMass() * delVelocity.y * freq;
        baseModel.getBody().applyForceToCenter(new Vec2(x, y));
    }
}
