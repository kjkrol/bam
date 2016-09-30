package bam.shape.model.base;

import bam.ControllableBamObject;
import lombok.Getter;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public abstract class BaseShape implements ControllableBamObject {
    private static final float DEFAULT_DENSITY_COEFFICIENT = 1.0f;
    private static final float DEFAULT_FRICTION_COEFFICIENT = 0.7f;
    private static final float DEFAULT_RESTITUTION_COEFFICIENT = 0.5f;
    public static final FixtureDef DEFAULT_FIXTURE_DEF = new FixtureDef();

    static {
        DEFAULT_FIXTURE_DEF.density = DEFAULT_DENSITY_COEFFICIENT;
        DEFAULT_FIXTURE_DEF.friction = DEFAULT_FRICTION_COEFFICIENT;
        DEFAULT_FIXTURE_DEF.restitution = DEFAULT_RESTITUTION_COEFFICIENT;
    }

    private static final float PI_RAD = 180f;
    private static final float ANGLE_TO_RAD_COEFFICIENT = (float) (PI_RAD / Math.PI);

    @Getter
    private final Body body;

    @Getter
    private final ReadableColor color;

    private final FixtureDef fixture;

    protected BaseShape(Body body, FixtureDef fixture, ReadableColor color) {
        this.color = color;
        this.body = body;
        this.fixture = fixture;
    }

    protected void init() {
        final Shape shape = createShape();
        final FixtureDef fixtureDef = createFixtureDef(shape, fixture);
        body.createFixture(fixtureDef);
    }

    public void draw() {
        GL11.glLoadIdentity();
        GL11.glTranslatef(getXPos(), getYPos(), 0);
        final float angle = body.getAngle() * ANGLE_TO_RAD_COEFFICIENT;
        GL11.glRotatef(angle, 0, 0, 1);
        ReadableColor color1 = nonNull(color) ? color : ReadableColor.WHITE;
        GL11.glColor3f(color1.getRed(), color1.getGreen(), color1.getBlue());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.drawShape();
    }

    protected abstract void drawShape();

    protected abstract Shape createShape();

    private float getXPos() {
        return body.getPosition().x;
    }

    private float getYPos() {
        return body.getPosition().y;
    }

    private FixtureDef createFixtureDef(Shape shape, FixtureDef fixture) {
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = fixture.density;
        fixtureDef.friction = fixture.friction;
        fixtureDef.restitution = fixture.restitution;
        return fixtureDef;
    }
}
