package bam.sample;

import bam.AbstractBamPlane;
import bam.GLUtil;
import bam.objects.AbstractBamObject;
import bam.objects.Oval;
import bam.objects.Rect;
import lombok.Data;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
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
    public void initTextures() {
        final Texture woodenBoxTexture = GLUtil.getTexture("src/main/resources/textures/wooden_box.png", GLUtil.ImageType.PNG);
        final Texture ballTexture = GLUtil.getTexture("src/main/resources/textures/ball.png", GLUtil.ImageType.PNG);
    }

    @Override
    public void initPlane() {

        /* add border walls */
        this.addBorderWalls(10);

        /* add bamObjects */
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        final Oval oval = this.bamObjectsFactory.createOval(new Vec2(200f, 400f), 12, BodyType.DYNAMIC, fixtureDef,
                Optional.empty(), Optional.of(ReadableColor.RED));
        this.controlledBamObject = Optional.of(oval);

        this.buildElasticBridge(new Vec2(120, 140), 100, 2, 40, 3);

    }

    @Override
    protected World initWorld() {
        final Vec2 gravity = new Vec2(0.0f, -100.0f);
        final World world = new World(gravity);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

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
        this.controlledBamObject.ifPresent(controlledObj -> controlledObj.move(controlledObj, velocity, freq));
    }

    /**
     *
     * @param borderWidth
     */
    private void addBorderWalls(final float borderWidth) {
        this.bamObjectsFactory.createRect(new Vec2(0, WINDOW_HEIGHT), WINDOW_WIDTH, borderWidth, BodyType.STATIC,
                Rect.DEFAULT_FIXTURE_DEF, Optional.empty(), Optional.of(ReadableColor.YELLOW));
        this.bamObjectsFactory.createRect(new Vec2(0, 0), WINDOW_WIDTH, borderWidth, BodyType.STATIC,
                Rect.DEFAULT_FIXTURE_DEF, Optional.empty(), Optional.of(ReadableColor.YELLOW));
        this.bamObjectsFactory.createRect(new Vec2(0, 0), borderWidth, WINDOW_HEIGHT, BodyType.STATIC,
                Rect.DEFAULT_FIXTURE_DEF, Optional.empty(), Optional.of(ReadableColor.YELLOW));
        this.bamObjectsFactory.createRect(new Vec2(WINDOW_WIDTH, 0), borderWidth, WINDOW_HEIGHT,
                BodyType.STATIC, Rect.DEFAULT_FIXTURE_DEF, Optional.empty(), Optional.of(ReadableColor.YELLOW));
    }

    private void buildElasticBridge(final Vec2 startPosition, final float width, final float thickness,
                                    final int componentsNumber, final float maxAngle) {

        final float componentWidth = width / (float) componentsNumber;

        Rect rect1 = this.bamObjectsFactory.createRect(startPosition, componentWidth, thickness,
                BodyType.STATIC, Rect.DEFAULT_FIXTURE_DEF,
                Optional.empty(), Optional.of(ReadableColor.ORANGE));
        Rect rect2;
        for (int index = 1; index < componentsNumber - 1 ; ++index) {
            rect2 = this.bamObjectsFactory.createRect(new Vec2(rect1.getXPos() + componentWidth * 2, rect1.getYPos()),
                    componentWidth, thickness, BodyType.DYNAMIC, Rect.DEFAULT_FIXTURE_DEF,
                    Optional.empty(), Optional.of(ReadableColor.GREEN));
            this.join(rect1, rect2, maxAngle);
            rect1 = rect2;
        }

        rect2 = this.bamObjectsFactory.createRect(new Vec2(rect1.getXPos() + componentWidth * 2, rect1.getYPos()),
                componentWidth, thickness, BodyType.STATIC, Rect.DEFAULT_FIXTURE_DEF,
                Optional.empty(), Optional.of(ReadableColor.ORANGE));
        this.join(rect1, rect2, maxAngle);
    }

    private void join(final Rect rect1, final Rect rect2, final float maxAngle) {

        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = rect1.getBody();
        revoluteJointDef.bodyB = rect2.getBody();
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.referenceAngle = 0;
        revoluteJointDef.enableLimit = true;

        final float width1 = rect1.getWidth();
        final float width2 = rect2.getWidth();

        revoluteJointDef.lowerAngle = (float) (-maxAngle * Math.PI / 180.f);
        revoluteJointDef.upperAngle = (float) (+maxAngle * Math.PI / 180.f);

        revoluteJointDef.localAnchorA.set(+width1, 0);
        revoluteJointDef.localAnchorB.set(-width2, 0);
        world.createJoint(revoluteJointDef);
    }

}
