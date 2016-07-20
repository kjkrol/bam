package bam.sample.model;

import bam.BamObjectsFactory;
import bam.BamPlane;
import bam.model.Oval;
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
    private BamPlane bamPlane;

    @Autowired
    public RedBall(BamPlane bamPlane) {
        this.bamPlane = bamPlane;
    }

    public void create() {
        bamPlane.getBamObjectsFactory().createOval(BamObjectsFactory.ovalBuilder()
                .bodyType(BodyType.DYNAMIC)
                .color(ReadableColor.RED)
                .position(new Vec2(BALL_INIT_X_POS, BALL_INIT_Y_POS))
                .radius(BALL_INIT_RADIUS)
                .fixtureDef(Oval.DEFAULT_FIXTURE_DEF));
    }
}
