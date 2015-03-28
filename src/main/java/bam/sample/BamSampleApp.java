package bam.sample;

import bam.AbstractBamPlane;
import bam.GLUtil;
import bam.objects.AbstractBamObject;
import bam.objects.Ball;
import bam.objects.Box;
import lombok.Data;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import java.util.Optional;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class BamSampleApp extends AbstractBamPlane {

    public static void main(String[] args) {
        final BamSampleApp bamSampleApp = new BamSampleApp();
        bamSampleApp.start();
    }

    /**
     *
     */
    private Optional<AbstractBamObject> controlledBamObject;

    @Override
    public void initPlane() {
        final Texture woodenBoxTexture = GLUtil.getTexture("src/main/resources/textures/wooden_box.png", GLUtil.ImageType.PNG);
        final Texture stoneTexture = GLUtil.getTexture("src/main/resources/textures/gray_rock.png", GLUtil.ImageType.PNG);

        /* add border walls*/
        final Box border0 = this.createBox(new Vec2(0, WINDOW_HEIGHT), WINDOW_WIDTH, 5, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, stoneTexture);
        this.bamObjects.add(border0);
        final Box border1 = this.createBox(new Vec2(0, 0), WINDOW_WIDTH, 5, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, stoneTexture);
        this.bamObjects.add(border1);
        final Box border2 = this.createBox(new Vec2(0, 0), 5, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, stoneTexture);
        this.bamObjects.add(border2);
        final Box border3 = this.createBox(new Vec2(WINDOW_WIDTH, 0), 5, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, stoneTexture);
        this.bamObjects.add(border3);

        /* add bamObjects */
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        final Ball ball = this.createBall(new Vec2(200f, 400f), Box.SIZE, BodyType.DYNAMIC, fixtureDef, stoneTexture);
        this.bamObjects.add(ball);
        this.controlledBamObject = Optional.of(ball);

        for (int index1 = 0; index1 < 60; ++index1) {
            for (int index2 = 0; index2 < 20; ++index2) {
                final Box box = this.createBox(new Vec2(10f + index1 * 10, 340f - index2 * 10), 4, 4, BodyType.DYNAMIC, Box.DEFAULT_FIXTURE_DEF, woodenBoxTexture);
                this.bamObjects.add(box);
            }
        }

    }

    @Override
    protected World initWorld() {
        final Vec2 gravity = new Vec2(0.0f, -10.0f);
        final World world = new World(gravity);
        world.setAllowSleep(true);
        return world;
    }

    @Override
    public void control(float freq) {

        float xVel = 0.0f;
        float yVel = 0.0f;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xVel -= 50000.0f;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xVel += 50000.0f;

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) yVel += 50000.0f;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) yVel -= 50000.0f;

        final Vec2 velocity = new Vec2(xVel, yVel);

        this.controlledBamObject.ifPresent(controlledObj -> this.move(controlledObj, velocity, freq));
    }

    private void move(final AbstractBamObject abstractBamObject, final Vec2 delVelocity, final float freq) {
        float x = abstractBamObject.getBody().getMass() * delVelocity.x * freq;
        float y = abstractBamObject.getBody().getMass() * delVelocity.y * freq;
        abstractBamObject.getBody().applyForceToCenter(new Vec2(x, y));
    }

    private Box createBox(final Vec2 position, final float width, final float height, final BodyType bodyType,
                          final FixtureDef templateFixture, final Texture texture) {
        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);
        final Body body = this.physicalBodyFactory.createBody(position, boxShape, bodyType, templateFixture);
        return new Box(body, texture, width, height);
    }

    private Ball createBall(final Vec2 position, final float radius, final BodyType bodyType,
                          final FixtureDef templateFixture, final Texture texture) {
        final Shape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        final Body body = this.physicalBodyFactory.createBody(position, circleShape, bodyType, templateFixture);
        return new Ball(body, texture, radius);
    }

}
