package bam.sample;

import bam.AbstractBamPlane;
import bam.GLUtil;
import bam.objects.AbstractBamObject;
import bam.objects.Box;
import lombok.Data;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import java.awt.*;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class BamPlane extends AbstractBamPlane {

    public static void main(String[] args) {
        final BamPlane bamPlane = new BamPlane();
        bamPlane.start();
    }

    /**
     *
     */
    private Box controllableBox;

    @Override
    public void initPlane() {
        final Texture boxTexture = GLUtil.getTexture("src/main/resources/imgs/box.png");

        /* add border walls*/
        final Box border0 = this.createBox(new Vec2(0, WINDOW_HEIGHT), WINDOW_WIDTH, 5, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, boxTexture);
        this.bamObjects.add(border0);
        final Box border1 = this.createBox(new Vec2(0, 0), WINDOW_WIDTH, 5, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, boxTexture);
        this.bamObjects.add(border1);
        final Box border2 = this.createBox(new Vec2(0, 0), 5, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, boxTexture);
        this.bamObjects.add(border2);
        final Box border3 = this.createBox(new Vec2(WINDOW_WIDTH, 0), 5, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, boxTexture);
        this.bamObjects.add(border3);

        /* add bamObjects */
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        final Box box0 = this.createBox(new Vec2(100f, 400f), Box.SIZE, Box.SIZE, BodyType.DYNAMIC, fixtureDef, boxTexture);
        this.bamObjects.add(box0);

        final Box box1 = this.createBox(new Vec2(200f, 400f), Box.SIZE, Box.SIZE, BodyType.DYNAMIC, Box.DEFAULT_FIXTURE_DEF, boxTexture);
        this.bamObjects.add(box1);

        this.controllableBox = box0;
    }

    @Override
    protected World initWorld() {
        final Vec2 gravity = new Vec2(0.0f, -30.0f);
        final World world = new World(gravity);
        world.setAllowSleep(true);
        return world;
    }

    @Override
    public void control(int delta) {

        float xVel = 0.0f;
        float yVel = 0.0f;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xVel -= 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xVel += 0.35f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) yVel += 0.35f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) yVel -= 0.35f * delta;

        this.move(controllableBox, new Vec2(xVel, yVel));
    }

    private void move(final AbstractBamObject abstractBamObject, final Vec2 delVelocity) {

        float x = abstractBamObject.getBody().getMass() * delVelocity.x / AbstractBamPlane.TIME_STEP;
        float y = abstractBamObject.getBody().getMass() * delVelocity.y / AbstractBamPlane.TIME_STEP;
        abstractBamObject.getBody().applyForceToCenter(new Vec2(x, y));
    }

    private Box createBox(final Vec2 position, final float width, final float height, final BodyType bodyType,
                          final FixtureDef templateFixture, final Texture texture) {
        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);
        final Body body = this.physicalBodyFactory.createBody(position, boxShape, bodyType, templateFixture);
        return new Box(body, texture, width, height);
    }

}
