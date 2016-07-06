package bam.sample;

import bam.AbstractBamPlane;
import bam.BamObjectsFactory;
import bam.GLUtil;
import bam.objects.AbstractBamObject;
import bam.objects.Oval;
import bam.objects.Rect;
import lombok.Getter;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BamSampleApp extends AbstractBamPlane {

    private static final Logger LOGGER = LoggerFactory.getLogger(BamSampleApp.class);

    private static final float DEFAULT_VELOCITY_VALUE = 5000f;

    private static final float PI_RAD = 180f;

    private static final float RAD_TO_ANGLE_COEFFICIENT = (float) (Math.PI / PI_RAD);

    private static final float BORDER_WALL_WITH = 10f;

    private static final float BORDER_WALL_DENSITY = 10f;

    private static final float BORDER_WALL_FRICTION = 0.5f;

    private static final float BORDER_WALL_RESTITUTION = 0.1f;

    private static final float BALL_INIT_X_POS = 200f;

    private static final float BALL_INIT_Y_POS = 400f;

    private static final float BALL_INIT_RADIUS = 12;

    private static final float BRIDGE_START_X_POS = 120;

    private static final float BRIDGE_START_Y_POS = 120;

    private static final float BRIDGE_WIDTH = 100;

    private static final float BRIDGE_THICKNESS = 2;

    private static final int BRIDGE_COMPONENTS_NUMBER = 40;

    private static final int BRIDGE_MAX_ANGLE = 3;

    public static void main(String[] args) {
        final BamSampleApp bamSampleApp = new BamSampleApp();
        bamSampleApp.start();
    }

    @Getter
    private AbstractBamObject controlledBamObject;

    @Override
    public void initTextures() {
        final Texture woodenBoxTexture = GLUtil.getTexture("textures/wooden_box.png", GLUtil.ImageType.PNG);
        final Texture ballTexture = GLUtil.getTexture("textures/ball.png", GLUtil.ImageType.PNG);
    }

    @Override
    public void initPlane() {
        /* add border walls */
        this.addBorderWalls(BORDER_WALL_WITH);

        /* add bamObjects */
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = BORDER_WALL_DENSITY;
        fixtureDef.friction = BORDER_WALL_FRICTION;
        fixtureDef.restitution = BORDER_WALL_RESTITUTION;
        final Oval oval = this.getBamObjectsFactory().createOval(BamObjectsFactory.ovalBuilder()
                .bodyType(BodyType.DYNAMIC)
                .color(ReadableColor.RED)
                .position(new Vec2(BALL_INIT_X_POS, BALL_INIT_Y_POS))
                .radius(BALL_INIT_RADIUS)
                .fixtureDef(fixtureDef));
        this.controlledBamObject = oval;

        this.buildElasticBridge(new Vec2(BRIDGE_START_X_POS, BRIDGE_START_Y_POS), BRIDGE_WIDTH, BRIDGE_THICKNESS,
                BRIDGE_COMPONENTS_NUMBER, BRIDGE_MAX_ANGLE);

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

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            xVel -= DEFAULT_VELOCITY_VALUE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            xVel += DEFAULT_VELOCITY_VALUE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            yVel += DEFAULT_VELOCITY_VALUE;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            yVel -= DEFAULT_VELOCITY_VALUE;
        }
        final Vec2 velocity = new Vec2(xVel, yVel);
        getControlledBamObject().move(getControlledBamObject(), velocity, freq);
    }

    private void addBorderWalls(final float borderWidth) {
        this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(new Vec2(0, AbstractBamPlane.WINDOW_HEIGHT))
                .width(AbstractBamPlane.WINDOW_WIDTH)
                .height(borderWidth)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.YELLOW));
        this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(new Vec2(0, 0))
                .width(AbstractBamPlane.WINDOW_WIDTH)
                .height(borderWidth)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.YELLOW));
        this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(new Vec2(0, 0))
                .width(borderWidth)
                .height(AbstractBamPlane.WINDOW_HEIGHT)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.YELLOW));
        this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(new Vec2(AbstractBamPlane.WINDOW_WIDTH, 0))
                .width(borderWidth)
                .height(AbstractBamPlane.WINDOW_HEIGHT)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.YELLOW));
    }

    private void buildElasticBridge(final Vec2 startPosition, final float width, final float thickness,
                                    final int componentsNumber, final float maxAngle) {

        final float componentWidth = width / (float) componentsNumber;

        Rect rect1 = this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(startPosition)
                .width(componentWidth)
                .height(thickness)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.ORANGE)
        );
        Rect rect2;
        for (int index = 1; index < componentsNumber - 1; ++index) {
            rect2 = this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                    .position(new Vec2(rect1.getXPos() + componentWidth * 2, rect1.getYPos()))
                    .width(componentWidth)
                    .height(thickness)
                    .bodyType(BodyType.DYNAMIC)
                    .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                    .color(ReadableColor.GREEN)
            );

            this.join(rect1, rect2, maxAngle);
            rect1 = rect2;
        }

        rect2 = this.getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                .position(new Vec2(rect1.getXPos() + componentWidth * 2, rect1.getYPos()))
                .width(componentWidth)
                .height(thickness)
                .bodyType(BodyType.STATIC)
                .fixtureDef(Rect.DEFAULT_FIXTURE_DEF)
                .color(ReadableColor.ORANGE));
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

        revoluteJointDef.lowerAngle = -maxAngle * RAD_TO_ANGLE_COEFFICIENT;
        revoluteJointDef.upperAngle = +maxAngle * RAD_TO_ANGLE_COEFFICIENT;

        revoluteJointDef.localAnchorA.set(+width1, 0);
        revoluteJointDef.localAnchorB.set(-width2, 0);
        this.getWorld().createJoint(revoluteJointDef);
    }

}
