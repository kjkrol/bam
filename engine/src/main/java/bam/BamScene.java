package bam;

import bam.display.BamDisplay;
import bam.display.DisplayParams;
import bam.physics.BamPhysics;
import bam.model.shape.base.BaseShape;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;

import java.util.HashSet;
import java.util.Set;

@ToString
@Slf4j
public class BamScene {

    private final Set<BaseShape> shapes = new HashSet<>();
    private final BamDisplay bamDisplay;
    private final BamPhysics bamPhysics;

    @Getter
    private final BamSceneCreator bamSceneCreator;

    public BamScene(World world, DisplayParams displayParams) {
        this.bamPhysics = new BamPhysics(world);
        this.bamDisplay = new BamDisplay(displayParams, bamPhysics::close);
        this.bamSceneCreator = new BamSceneCreator(shapes::add, world::createBody);
    }

    public void start() {
        bamPhysics.start();
        bamDisplay.start(() -> shapes.forEach(BaseShape::draw));
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
