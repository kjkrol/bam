package bam.sample.model;

import bam.BamScene;
import bam.shape.model.Circle;
import bam.shape.request.CreateNewCircleRequest;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.lwjgl.util.ReadableColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedBall {
    private static final float BALL_INIT_X_POS = 200f;
    private static final float BALL_INIT_Y_POS = 400f;
    private static final float BALL_INIT_RADIUS = 12;
    private BamScene bamScene;

    @Autowired
    public RedBall(BamScene bamScene) {
        this.bamScene = bamScene;
    }

    public void create() {
        bamScene.getBamSceneCreator().addShape(
                new CreateNewCircleRequest(Circle.DEFAULT_FIXTURE_DEF, ReadableColor.RED, BALL_INIT_RADIUS),
                new Vec2(BALL_INIT_X_POS, BALL_INIT_Y_POS),
                BodyType.DYNAMIC);
    }
}
