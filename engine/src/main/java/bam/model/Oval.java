package bam.model;

import lombok.Getter;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

public class Oval extends BaseBamType {

    private static final int MINIMUM_EDGES_NUMBER = 12;

    private static final float HALF = 0.5f;

    private static final int FACTOR_FOUR = 4;

    @Getter
    private final float radius;

    @Getter
    private final int edges;

    @Getter
    private final float[] x;

    @Getter
    private final float[] y;

    @Getter
    private final float[] tx;

    @Getter
    private final float[] ty;

    public Oval(Body body, Texture texture, ReadableColor color, float radius) {
        super(body, texture, color);
        this.radius = radius;
        int initEdges = (int) (radius / FACTOR_FOUR);
        this.edges = initEdges < MINIMUM_EDGES_NUMBER ? MINIMUM_EDGES_NUMBER : initEdges;
        this.x = new float[edges];
        this.y = new float[edges];
        this.tx = new float[edges];
        this.ty = new float[edges];
        this.init();
    }

    public Oval(Body body, Texture texture, ReadableColor color, float[] params) {
        this(body, texture, color, params[0]);
    }

    @Override
    protected void drawTexture() {

        GL11.glBegin(GL11.GL_POLYGON);

        for (int index = 0; index < this.edges; ++index) {
            GL11.glTexCoord2f(tx[index], ty[index]);
            GL11.glVertex2f(x[index], y[index]);
        }

        GL11.glEnd();

    }

    @Override
    protected void drawShape() {

        GL11.glPointSize(FACTOR_FOUR);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, radius);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, radius);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int index = 0; index < this.edges; ++index) {
            GL11.glVertex2f(x[index], y[index]);
        }
        GL11.glEnd();

        final ReadableColor color = this.getColor();
        GL11.glBegin(GL11.GL_POLYGON);
        if (null != color) {
            GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), HALF);
        }
        for (int index = 0; index < this.edges; ++index) {
            GL11.glVertex2f(x[index], y[index]);
        }

        GL11.glEnd();
    }

    private void init() {
        for (int index = 0; index < this.edges; ++index) {
            double radian = 2 * Math.PI * index / this.edges;
            float xcos = (float) Math.cos(radian);
            float ysin = (float) Math.sin(radian);
            x[index] = xcos * this.radius;
            y[index] = ysin * this.radius;
            tx[index] = xcos * HALF + HALF;
            ty[index] = ysin * HALF + HALF;
        }
    }
}
