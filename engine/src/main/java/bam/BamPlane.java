package bam;

import bam.model.BaseBamType;
import bam.opengl.OpenGlConfiguration;
import bam.opengl.OpenGlPlane2dDisplay;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

@ToString
@Slf4j
public class BamPlane {
    private static final float TIME_AMOUNT_FACTOR = 0.001f;
    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 10;

    private final World world;

    private final List<BaseBamType> bamObjects = new ArrayList<>();

    private final StopWatch stopWatch = new StopWatch();

    private final OpenGlConfiguration configuration;

    private final OpenGlPlane2dDisplay openGlPlane2dDisplay;

    @Getter
    private final BamObjectsFactory bamObjectsFactory;

    public BamPlane(World world, OpenGlConfiguration configuration) {
        this.configuration = configuration;
        this.world = world;
        this.openGlPlane2dDisplay = new OpenGlPlane2dDisplay(configuration);
        this.bamObjectsFactory = new BamObjectsFactory(world::createBody, this.bamObjects::add);
    }

    public void start() {
        openGlPlane2dDisplay.startDisplay();
        stopWatch.getDelta();
        while (openGlPlane2dDisplay.isDisplayEnabled()) {
            refreshWorldState();
//            this.control(freq);
            openGlPlane2dDisplay.redraw(() -> bamObjects.forEach(BaseBamType::draw));
        }
        openGlPlane2dDisplay.closeDisplay();
    }

    public float getDisplayWidth() {
        return configuration.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return configuration.getDisplayHeight();
    }

    private void refreshWorldState() {
        final int delta = stopWatch.getDelta();
        final float freq = delta * TIME_AMOUNT_FACTOR;
        world.step(freq, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
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
