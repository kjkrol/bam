package bam;

import bam.nat.OpenGLNativeLibsLoaderUtil;
import bam.objects.AbstractBamObject;
import lombok.Data;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public abstract class AbstractBamPlane {

    static {
        OpenGLNativeLibsLoaderUtil.loadLibs();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBamPlane.class);

    protected static final String TITLE = "BAM!";
    protected static final int WINDOW_WIDTH = 640;
    protected static final int WINDOW_HEIGHT = 480;

    protected static final float TIME_STEP = 1.0f / 60.f;
    protected static final int VELOCITY_ITERATIONS = 20;
    protected static final int POSITION_ITERATIONS = 20;

    protected final List<AbstractBamObject> bamObjects;

    protected final World world;

    protected final PhysicalBodyFactory physicalBodyFactory;

    protected final StopWatch stopWatch;

    public AbstractBamPlane() {
        this.bamObjects = new ArrayList<>();
        this.stopWatch = new StopWatch();
        this.world = this.initWorld();
        this.physicalBodyFactory = new PhysicalBodyFactory(this.world);
        this.initGL();
        this.initPlane();
    }

    /**
     *
     */
    public void start() {
        this.getStopWatch().getDelta();
        while (!Display.isCloseRequested()) {
            int delta = this.getStopWatch().getDelta();
            this.getWorld().step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.control(delta);

            /* Clear The Screen And The Depth Buffer */
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glPushMatrix();
            this.getBamObjects().stream().forEach(AbstractBamObject::draw);
            GL11.glLoadIdentity();
            GL11.glPopMatrix();
            GL11.glFlush();

            Display.update();
            Display.sync(60); // cap fps to 60fps
        }
        Display.destroy();
    }

    protected abstract World initWorld();

    protected abstract void control(int delta);

    protected abstract void initPlane();

    /**
     * @throws LWJGLException
     */
    protected void initGL() {
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
            GL11.glLoadIdentity();
        } catch (LWJGLException e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(0);
        }
    }

}
