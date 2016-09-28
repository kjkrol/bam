package bam.model.base;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jbox2d.dynamics.Body;
import org.lwjgl.util.ReadableColor;


@Builder
@Getter
@ToString
public class OpenGlModelParams {
    private final Body body;
    private final ReadableColor color;
    private final float[] params;
}
