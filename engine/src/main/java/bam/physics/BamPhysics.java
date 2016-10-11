package bam.physics;

import org.jbox2d.dynamics.World;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BamPhysics {

    private static final int PHYSICS_START_DELAY_TIME_IN_MS = 50;
    private static final int PHYSICS_REFRESH_TIME_IN_MS = 10;

    private static final float ADVANCE_SIMULATION_BY_TIME_IN_SECONDS = 1 / 20f;
    private static final int HOW_STRONGLY_TO_CORRECT_VELOCITY = 8;
    private static final int HOW_STRONGLY_TO_CORRECT_POSITION = 13;

    private final ScheduledExecutorService physicsEngineScheduler = Executors.newSingleThreadScheduledExecutor();

    private final World world;

    public BamPhysics(World world) {
        this.world = world;
    }

    public void start() {
        physicsEngineScheduler.scheduleAtFixedRate(
                () ->  world.step(ADVANCE_SIMULATION_BY_TIME_IN_SECONDS,
                        HOW_STRONGLY_TO_CORRECT_VELOCITY,
                        HOW_STRONGLY_TO_CORRECT_POSITION),
                PHYSICS_START_DELAY_TIME_IN_MS, PHYSICS_REFRESH_TIME_IN_MS, TimeUnit.MILLISECONDS);
    }
}
