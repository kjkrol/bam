package bam.model.base;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;


@Builder
@Getter
@ToString
public class PhysicalBodyParams {
    private final Shape shape;
    private final BodyType bodyType;
    private final FixtureDef fixture;
}
