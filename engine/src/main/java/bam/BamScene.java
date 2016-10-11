package bam;

import bam.display.BamDisplay;
import bam.display.DisplayParams;
import bam.shape.model.base.BaseShape;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ToString
@Slf4j
public class BamScene {
    private static final float ADVANCE_SIMULATION_BY_TIME_IN_SECONDS = 1 / 20f;
    private static final int HOW_STRONGLY_TO_CORRECT_VELOCITY = 8;
    private static final int HOW_STRONGLY_TO_CORRECT_POSITION = 13;

    private static final int EVERY_TENTH = 10;

    private final World world;
    private final Set<BaseShape> shapes = new HashSet<>();
    private final StopWatch stopWatch = new StopWatch();
    private final BamDisplay bamDisplay;

    @Getter
    private final BamSceneCreator bamSceneCreator;

    public BamScene(World world, DisplayParams displayParams) {
        this.world = world;
        this.bamDisplay = new BamDisplay(displayParams);
        this.bamSceneCreator = new BamSceneCreator(shapes::add, world::createBody);
    }

    // TODO: rozdzielić na dwa wątki: jeden zajmuje się odświeżaniem obrazu, drugi fizyką?
    // TODO: teraz to sie nie kompiluje, gdyz trzeba rozdzielic display mgmt od physics mgmt (osobne watki)

    public void start() {
        final ExecutorService es1 = Executors.newSingleThreadExecutor();
        final ExecutorService es2 = Executors.newSingleThreadExecutor();
        refreshWorldState();
        es2.submit(() -> bamDisplay.start()).isDone();
        for (int i = 0;; ++i) {
            es1.submit(this::refreshWorldState);
            if (i % EVERY_TENTH == 0) {
                es2.submit(() -> bamDisplay.refresh(shapes));
            }
        }
    }

    private void refreshWorldState() {
        final int delta = stopWatch.getDelta();
//        final float freq = delta * ADVANCE_SIMULATION_BY_TIME_IN_SECONDS;
        world.step(ADVANCE_SIMULATION_BY_TIME_IN_SECONDS, HOW_STRONGLY_TO_CORRECT_VELOCITY, HOW_STRONGLY_TO_CORRECT_POSITION);
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
