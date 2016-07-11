package bam.model;

import bam.ControllableBamObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

@ToString
@RequiredArgsConstructor
public abstract class BaseBamType implements ControllableBamObject {

    public static final FixtureDef DEFAULT_FIXTURE_DEF = new FixtureDef();

    private static final float PI_RAD = 180f;

    private static final float ANGLE_TO_RAD_COEFFICIENT = (float) (PI_RAD / Math.PI);

    private static final float DEFAULT_DENSITY_COEFFICIENT = 1.0f;

    private static final float DEFAULT_FRICTION_COEFFICIENT = 0.7f;

    private static final float DEFAULT_RESTITUTION_COEFFICIENT = 0.5f;

    static {
        DEFAULT_FIXTURE_DEF.density = DEFAULT_DENSITY_COEFFICIENT;
        DEFAULT_FIXTURE_DEF.friction = DEFAULT_FRICTION_COEFFICIENT;
        DEFAULT_FIXTURE_DEF.restitution = DEFAULT_RESTITUTION_COEFFICIENT;
    }

    @Getter
    private final Body body;

    @Getter
    private final Texture texture;

    @Getter
    private final ReadableColor color;

    protected abstract void drawTexture();

    protected abstract void drawShape();

    public void draw() {

        GL11.glLoadIdentity();
        GL11.glTranslatef(getXPos(), getYPos(), 0);

        final float angle = body.getAngle() * ANGLE_TO_RAD_COEFFICIENT;
        GL11.glRotatef(angle, 0, 0, 1);
        ReadableColor color1 = null != this.color ? color : ReadableColor.WHITE;
        GL11.glColor3f(color1.getRed(), color1.getGreen(), color1.getBlue());

        if (null != this.texture) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.texture.bind();
            this.drawTexture();
        } else {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            this.drawShape();
        }

    }

    public float getXPos() {
        return body.getPosition().x;
    }

    public float getYPos() {
        return body.getPosition().y;
    }

}
