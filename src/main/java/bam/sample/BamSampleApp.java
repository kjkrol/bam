package bam.sample;

import bam.AbstractBamPlane;
import bam.GLUtil;
import bam.objects.AbstractBamObject;
import bam.objects.Ball;
import bam.objects.Box;
import lombok.Data;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class BamSampleApp extends AbstractBamPlane {

    private static final Logger LOGGER = LoggerFactory.getLogger(BamSampleApp.class);

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
        final Texture ballTexture = GLUtil.getTexture("src/main/resources/textures/ball.png", GLUtil.ImageType.PNG);

        final float borderWidth = 10;

        /* add border walls */
        final Box topBorder = this.createBox(new Vec2(0, WINDOW_HEIGHT), WINDOW_WIDTH, borderWidth, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.YELLOW);
        topBorder.getBody().setUserData(topBorder);
        this.bamObjects.add(topBorder);
        final Box bottomBorder = this.createBox(new Vec2(0, 0), WINDOW_WIDTH, borderWidth, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.YELLOW);
        bottomBorder.getBody().setUserData(bottomBorder);
        this.bamObjects.add(bottomBorder);
        final Box leftBorder = this.createBox(new Vec2(0, 0), borderWidth, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.YELLOW);
        leftBorder.getBody().setUserData(leftBorder);
        this.bamObjects.add(leftBorder);
        final Box rightBorder = this.createBox(new Vec2(WINDOW_WIDTH, 0), borderWidth, WINDOW_HEIGHT, BodyType.STATIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.YELLOW);
        rightBorder.getBody().setUserData(rightBorder);
        this.bamObjects.add(rightBorder);

        /* add bamObjects */
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        final Ball ball = this.createBall(new Vec2(200f, 400f), Ball.DEFAULT_SIZE, BodyType.DYNAMIC, fixtureDef, ReadableColor.RED);
        this.bamObjects.add(ball);
        this.controlledBamObject = Optional.of(ball);
        ball.getBody().setUserData(ball);
//
        final Box box1 = this.createBox(new Vec2(40f, 340f), Box.DEFAULT_SIZE, Box.DEFAULT_SIZE, BodyType.DYNAMIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.GREEN);
        this.bamObjects.add(box1);
        final Box box2 = this.createBox(new Vec2(210f, 340f), Box.DEFAULT_SIZE, Box.DEFAULT_SIZE, BodyType.DYNAMIC, Box.DEFAULT_FIXTURE_DEF, ReadableColor.GREEN);
        this.bamObjects.add(box2);

        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
//        revoluteJointDef.initialize(box1.getBody(), box2.getBody(), box1.getBody().getWorldCenter());
        revoluteJointDef.bodyA = box1.getBody();
        revoluteJointDef.bodyB = box2.getBody();
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.referenceAngle = 0;
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = (float) (-45.0f * Math.PI / 180.f);
        revoluteJointDef.upperAngle = (float) (45.0f * Math.PI / 180.f);

        revoluteJointDef.localAnchorA = new Vec2(0.75f * Box.DEFAULT_SIZE, 0);
        revoluteJointDef.localAnchorB = new Vec2(-0.75f * Box.DEFAULT_SIZE, 0);
        world.createJoint(revoluteJointDef);

    }

    @Override
    protected World initWorld() {
        final Vec2 gravity = new Vec2(0.0f, -100.0f);
        final World world = new World(gravity);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
//                final Object obj1 = contact.getFixtureA().getBody().getUserData();
//                final Object obj2 = contact.getFixtureB().getBody().getUserData();
//                if (null != obj1 && null != obj2) {
//                    LOGGER.info("Ball is touching the wall.");
//                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
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

    private Box createBox(final Vec2 position, final float width, final float height, final BodyType bodyType,
                          final FixtureDef templateFixture, final ReadableColor color) {
        final PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(width, height);
        final Body body = this.physicalBodyFactory.createBody(position, boxShape, bodyType, templateFixture);
        return new Box(body, color, width, height);
    }

    private Ball createBall(final Vec2 position, final float radius, final BodyType bodyType,
                            final FixtureDef templateFixture, final Texture texture) {
        final Shape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        final Body body = this.physicalBodyFactory.createBody(position, circleShape, bodyType, templateFixture);
        return new Ball(body, texture, radius);
    }

    private Ball createBall(final Vec2 position, final float radius, final BodyType bodyType,
                            final FixtureDef templateFixture, final ReadableColor color) {
        final Shape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        final Body body = this.physicalBodyFactory.createBody(position, circleShape, bodyType, templateFixture);
        return new Ball(body, color, radius);
    }

}
