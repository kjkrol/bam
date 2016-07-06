package bam.objects;

import org.jbox2d.common.Vec2;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ControllableBamObject {

    /**
     *
     * @param abstractBamObject
     * @param delVelocity
     * @param freq
     */
    default void move(final AbstractBamObject abstractBamObject, final Vec2 delVelocity, final float freq) {
        float x = abstractBamObject.getBody().getMass() * delVelocity.x * freq;
        float y = abstractBamObject.getBody().getMass() * delVelocity.y * freq;
        abstractBamObject.getBody().applyForceToCenter(new Vec2(x, y));
    }
}
