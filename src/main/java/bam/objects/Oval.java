package bam.objects;

import lombok.Data;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;
import org.newdawn.slick.opengl.Texture;

import java.util.Optional;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Oval extends AbstractBamObject {

    private final float radius;

    private final int edges;
    private final float[] x;
    private final float[] y;
    private final float[] tx;
    private final float[] ty;

    public Oval(Body body, Optional<Texture> texture, Optional<ReadableColor> color, float radius) {
        super(body, texture, color);
        this.radius = radius;
        int initEdges = (int) (radius / 4);
        this.edges = initEdges < 12 ? 12 : initEdges;
        this.x = new float[edges];
        this.y = new float[edges];
        this.tx = new float[edges];
        this.ty = new float[edges];
        this.init();
    }

    public Oval(Body body, Optional<Texture> texture, Optional<ReadableColor> color, float[] params) {
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

        GL11.glPointSize(4);
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

        GL11.glBegin(GL11.GL_POLYGON);
        this.color.ifPresent(c -> GL11.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), 0.5f));
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
            tx[index] = xcos * 0.5f + 0.5f;
            ty[index] = ysin * 0.5f + 0.5f;
        }
    }
}
