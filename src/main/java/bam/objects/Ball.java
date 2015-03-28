package bam.objects;

import lombok.Data;
import org.jbox2d.dynamics.Body;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Ball extends AbstractBamObject {

    private final float radius;

    private final int edges = 20;
    private float[] x = new float[edges];
    private float[] y = new float[edges];
    private float[] tx = new float[edges];
    private float[] ty = new float[edges];

    public Ball(Body body, Texture texture, float radius) {
        super(body, texture);
        this.radius = radius;
        this.init();
    }

    @Override
    public void draw() {
        this.texture.bind();

        GL11.glLoadIdentity();
        GL11.glTranslatef(getXPos(), getYPos(), 0);

        float angle = (float) (body.getAngle() * 180 / Math.PI) - 90.0f;
        GL11.glRotatef(angle, 0f, 0f, 1f);

        GL11.glBegin(GL11.GL_POLYGON);
        for (int index = 0; index < this.edges; ++index) {
            GL11.glTexCoord2f(tx[index], ty[index]);
            GL11.glVertex2f(x[index], y[index]);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);

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
