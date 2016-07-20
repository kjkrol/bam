package bam.sample.config;

import bam.sample.model.BorderWall;
import bam.sample.model.RedBall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SampleScene {
    private static final float BORDER_WALL_WITH = 10f;

    private final BorderWall borderWall;
    private final RedBall redBall;

    @Autowired
    public SampleScene(BorderWall borderWall, RedBall redBall) {
        this.borderWall = borderWall;
        this.redBall = redBall;
    }

    void init() {
        borderWall.create(BORDER_WALL_WITH);
        redBall.create();
    }

}
