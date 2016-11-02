package bam.display.opengl.shape.base;

import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.ReadableColor;

import static java.util.Objects.nonNull;

public abstract class BaseOpenGlShape {

    private static final float PI_RAD = 180f;
    private static final float ANGLE_TO_RAD_COEFFICIENT = (float) (PI_RAD / Math.PI);

    @Getter
    private final ReadableColor color;

    protected BaseOpenGlShape(ReadableColor color) {
        this.color = color;
    }

    public void draw(float xPos, float yPos, float angle) {
        GL11.glLoadIdentity();
        GL11.glTranslatef(xPos, yPos, 0);
        angle = angle * ANGLE_TO_RAD_COEFFICIENT;
        GL11.glRotatef(angle, 0, 0, 1);
        ReadableColor color1 = nonNull(color) ? color : ReadableColor.WHITE;
        GL11.glColor3f(color1.getRed(), color1.getGreen(), color1.getBlue());
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        this.drawShape();
    }

    protected abstract void drawShape();
}
