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
public class Box extends AbstractBamObject {

    public Box(Body body, Texture texture, float width, float height) {
        super(body, texture, width, height);
    }

    @Override
    public void draw() {
        this.texture.bind();

        GL11.glLoadIdentity();
        GL11.glTranslatef(getXPos(), getYPos(), 0);

        float angle = (float) (body.getAngle() * 180 / Math.PI);
        GL11.glRotatef(angle, 0f, 0f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(-width, -height);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(+width, -height);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(+width, +height);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(-width, +height);

        GL11.glEnd();
    }

}
