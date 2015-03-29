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
public class Ball extends AbstractBamObject {

    private final float radius;

    private final int edges = 16;
    private float[] x = new float[edges];
    private float[] y = new float[edges];
    private float[] tx = new float[edges];
    private float[] ty = new float[edges];

    public Ball(Body body, Texture texture, float radius) {
        super(body, Optional.of(texture), Optional.empty());
        this.radius = radius;
        this.init();
    }

    public Ball(Body body, ReadableColor color, float radius) {
        super(body, Optional.empty(), Optional.of(color));
        this.radius = radius;
        this.init();
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
