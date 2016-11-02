package bam.sample.model;

import bam.BamScene;
import bam.model.request.CreateNewRectangleRequest;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.lwjgl.util.ReadableColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorderWall {
    private static final float BORDER_WALL_DENSITY = 10f;
    private static final float BORDER_WALL_FRICTION = 0.5f;
    private static final float BORDER_WALL_RESTITUTION = 0.1f;
    private BamScene bamScene;

    @Autowired
    public BorderWall(BamScene bamScene) {
        this.bamScene = bamScene;
    }

    public void create(final float breadth, final float borderWidth, final float borderHeight) {
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = BORDER_WALL_DENSITY;
        fixtureDef.friction = BORDER_WALL_FRICTION;
        fixtureDef.restitution = BORDER_WALL_RESTITUTION;

        bamScene.getBamSceneCreator().addShape(
                new CreateNewRectangleRequest(fixtureDef, ReadableColor.YELLOW, borderWidth, breadth),
                new Vec2(0, borderHeight),
                BodyType.STATIC);
        bamScene.getBamSceneCreator().addShape(
                new CreateNewRectangleRequest(fixtureDef, ReadableColor.YELLOW, borderWidth, breadth),
                new Vec2(0, 0),
                BodyType.STATIC);
        bamScene.getBamSceneCreator().addShape(
                new CreateNewRectangleRequest(fixtureDef, ReadableColor.YELLOW, breadth, borderHeight),
                new Vec2(0, 0),
                BodyType.STATIC);
        bamScene.getBamSceneCreator().addShape(
                new CreateNewRectangleRequest(fixtureDef, ReadableColor.YELLOW, breadth, borderHeight),
                new Vec2(borderWidth, 0),
                BodyType.STATIC);
    }
}
