package bam;

import bam.model.BaseBamType;
import bam.opengl.GLUtil;
import bam.opengl.OpenGlLoader;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;


@ToString
@Slf4j
public abstract class AbstractBamPlane {

    static {
        new OpenGlLoader().load();
    }

    protected static final int WINDOW_WIDTH = 640;
    protected static final int WINDOW_HEIGHT = 480;
    private static final String TITLE = "BAM!";
    private static final int FPS_LIMIT = 60 * 4;
    private static final int VELOCITY_ITERATIONS = 10;
    private static final int POSITION_ITERATIONS = 10;

    private static final float TIME_AMOUNT_FACTOR_COEFFICIENT = 1000.f;
    private static final float TIME_AMOUNT_FACTOR = 1.0f / TIME_AMOUNT_FACTOR_COEFFICIENT;

    @Getter
    private final List<BaseBamType> bamObjects = new ArrayList<>();

    @Getter
    private final World world;

    @Getter
    private final StopWatch stopWatch = new StopWatch();

    @Getter
    private final PhysicalBodyFactory physicalBodyFactory;

    @Getter
    private final BamObjectsFactory bamObjectsFactory;

    public AbstractBamPlane() {
        this.world = this.initWorld();
        this.physicalBodyFactory = new PhysicalBodyFactory(world);
        this.bamObjectsFactory = new BamObjectsFactory(this.physicalBodyFactory::createBody, this.bamObjects::add);
        this.initGL();
        this.initTextures();
        this.initPlane();
    }

    protected void start() {
        this.getStopWatch().getDelta();
        while (!Display.isCloseRequested()) {
            final int delta = this.getStopWatch().getDelta();
            final float freq = delta * TIME_AMOUNT_FACTOR;
            this.getWorld().step(freq, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.control(freq);

            /* Clear The Screen And The Depth Buffer */
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();
            this.getBamObjects().stream().forEach(BaseBamType::draw);
            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();

            Display.sync(FPS_LIMIT);
            Display.update();
        }
        Display.destroy();
    }

    protected abstract World initWorld();

    protected abstract void control(float freq);

    protected abstract void initTextures();

    protected abstract void initPlane();

    private void initGL() {
        try {
            final DisplayMode mode = GLUtil.getDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT);
            Display.setDisplayMode(mode);
            Display.setFullscreen(false);
            Display.setTitle(TITLE);
            Display.create();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, 1, -1);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClearDepth(1);

            GL11.glLoadIdentity();
        } catch (LWJGLException e) {
            log.error(e.getMessage(), e);
            System.exit(0);
        }
    }

}
