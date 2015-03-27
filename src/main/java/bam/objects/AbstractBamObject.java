package bam.objects;

import lombok.Data;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.newdawn.slick.opengl.Texture;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public abstract class AbstractBamObject {

    public static final FixtureDef DEFAULT_FIXTURE_DEF = new FixtureDef();
    public static final int SIZE = 30;

    static {
        DEFAULT_FIXTURE_DEF.density = 1.0f;
        DEFAULT_FIXTURE_DEF.friction = 1.0f;
        DEFAULT_FIXTURE_DEF.restitution = 0.5f;
    }

    protected final Body body;

    protected final Texture texture;

    protected final float width;

    protected final float height;

    public abstract void draw();

    public float getXPos() {
        return body.getPosition().x;
    }

    public float getYPos() {
        return body.getPosition().y;
    }


}