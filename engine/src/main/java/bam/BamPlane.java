package bam;

import bam.model.BaseBamType;
import bam.model.Oval;
import bam.opengl.OpenGlConfiguration;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.ReadableColor;

import java.util.ArrayList;
import java.util.List;

@ToString
@Slf4j
public class BamPlane {
    private static final float TIME_AMOUNT_FACTOR_COEFFICIENT = 1000.f;
    private static final float TIME_AMOUNT_FACTOR = 1.0f / TIME_AMOUNT_FACTOR_COEFFICIENT;
    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 10;

    @Getter
    private final World world;

    @Getter
    private final OpenGlConfiguration openGlConfiguration;

    @Getter
    private final List<BaseBamType> bamObjects = new ArrayList<>();

    @Getter
    private final PhysicalBodyFactory physicalBodyFactory;

    @Getter
    private final BamObjectsFactory bamObjectsFactory;

    @Getter
    private final StopWatch stopWatch = new StopWatch();

    public BamPlane(World world, OpenGlConfiguration openGlConfiguration) {
        this.openGlConfiguration = openGlConfiguration;
        this.world = world;
        this.physicalBodyFactory = new PhysicalBodyFactory(world);
        initScene();
        this.bamObjectsFactory = new BamObjectsFactory(this.physicalBodyFactory::createBody, this.bamObjects::add);
    }

    public void start() {
        this.getStopWatch().getDelta();
        while (!Display.isCloseRequested()) {
            refreshWorldState();
//            this.control(freq);
            getOpenGlConfiguration().refreshView(getBamObjects());
        }
        Display.destroy();
    }

    private void refreshWorldState() {
        final int delta = this.getStopWatch().getDelta();
        final float freq = delta * TIME_AMOUNT_FACTOR;
        getWorld().step(freq, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    private static final float BORDER_WALL_WITH = 10f;

    private void initScene() {
        new BorderWall().create(BORDER_WALL_WITH);
        new RedBall();
    }

    private class BorderWall {
        private static final float BORDER_WALL_DENSITY = 10f;
        private static final float BORDER_WALL_FRICTION = 0.5f;
        private static final float BORDER_WALL_RESTITUTION = 0.1f;

        void create(final float breadth) {
            final FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = BORDER_WALL_DENSITY;
            fixtureDef.friction = BORDER_WALL_FRICTION;
            fixtureDef.restitution = BORDER_WALL_RESTITUTION;
            getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                    .position(new Vec2(0, openGlConfiguration.getDisplayWith()))
                    .width(openGlConfiguration.getDisplayWith())
                    .height(breadth)
                    .bodyType(BodyType.STATIC)
                    .fixtureDef(fixtureDef)
                    .color(ReadableColor.YELLOW));
            getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                    .position(new Vec2(0, 0))
                    .width(openGlConfiguration.getDisplayWith())
                    .height(breadth)
                    .bodyType(BodyType.STATIC)
                    .fixtureDef(fixtureDef)
                    .color(ReadableColor.YELLOW));
            getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                    .position(new Vec2(0, 0))
                    .width(breadth)
                    .height(openGlConfiguration.getDisplayHeight())
                    .bodyType(BodyType.STATIC)
                    .fixtureDef(fixtureDef)
                    .color(ReadableColor.YELLOW));
            getBamObjectsFactory().createRect(BamObjectsFactory.rectBuilder()
                    .position(new Vec2(openGlConfiguration.getDisplayWith(), 0))
                    .width(breadth)
                    .height(openGlConfiguration.getDisplayHeight())
                    .bodyType(BodyType.STATIC)
                    .fixtureDef(fixtureDef)
                    .color(ReadableColor.YELLOW));
        }
    }

    private class RedBall {
        private static final float BALL_INIT_X_POS = 200f;
        private static final float BALL_INIT_Y_POS = 400f;
        private static final float BALL_INIT_RADIUS = 12;

        void create() {
            getBamObjectsFactory().createOval(BamObjectsFactory.ovalBuilder()
                    .bodyType(BodyType.DYNAMIC)
                    .color(ReadableColor.RED)
                    .position(new Vec2(BALL_INIT_X_POS, BALL_INIT_Y_POS))
                    .radius(BALL_INIT_RADIUS)
                    .fixtureDef(Oval.DEFAULT_FIXTURE_DEF));
        }
    }
}
//    @Override
//    public void control(float freq) {
//        float xVel = 0.0f;
//        float yVel = 0.0f;
//        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//            xVel -= DEFAULT_VELOCITY_VALUE;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//            xVel += DEFAULT_VELOCITY_VALUE;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//            yVel += DEFAULT_VELOCITY_VALUE;
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//            yVel -= DEFAULT_VELOCITY_VALUE;
//        }
//        final Vec2 velocity = new Vec2(xVel, yVel);
//        getControlledBamObject().move(getControlledBamObject(), velocity, freq);
//    }
