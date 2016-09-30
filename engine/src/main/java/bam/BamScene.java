package bam;

import bam.display.Displayable;
import bam.display.DisplayConfiguration;
import bam.display.opengl.OpenGlDisplay;
import bam.shape.model.base.BaseShape;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

@ToString
@Slf4j
public class BamScene {
    private static final float TIME_AMOUNT_FACTOR = 0.001f;
    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 10;

    private final World world;
    private final List<BaseShape> shapes = new ArrayList<>();
    private final StopWatch stopWatch = new StopWatch();
    private final Displayable bamDisplay;

    @Getter
    private final BamSceneCreator bamSceneCreator;

    public BamScene(World world, DisplayConfiguration configuration) {
        this.world = world;
        this.bamDisplay = new OpenGlDisplay(configuration);
        this.bamSceneCreator = new BamSceneCreator(shapes::add, world::createBody);
    }

    public void start() {
        bamDisplay.startDisplay();
        stopWatch.getDelta();
        while (bamDisplay.isDisplayEnabled()) {
            refreshWorldState();
//            this.control(freq);
            bamDisplay.redraw(() -> shapes.forEach(BaseShape::draw));
        }
        bamDisplay.closeDisplay();
    }

    private void refreshWorldState() {
        final int delta = stopWatch.getDelta();
        final float freq = delta * TIME_AMOUNT_FACTOR;
        world.step(freq, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public float getDisplayWidth() {
        return bamDisplay.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return bamDisplay.getDisplayHeight();
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
