package bam.display;

import bam.display.opengl.OpenGlDisplay;
import bam.shape.model.base.BaseShape;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

//TODO: ta klasa powinna działać w osobnym watku i dostawac polecenia poprzez kolejke komunikatow
//TODO: ta klasa powinna miec lepsza nazwe
//TODO: ta klasa powinna w calosci przejac odpowiedzialnosc za kontrolę prezentacji grafiki
@Slf4j
public class BamDisplay {

    private final OpenGlDisplay openGlDisplay;

    public BamDisplay(DisplayParams displayParams) {
        this.openGlDisplay = new OpenGlDisplay(displayParams);
    }

    public void start() {
        openGlDisplay.init();
        openGlDisplay.draw(() -> { });
    }

    public void refresh(Set<BaseShape> shapes) {
        openGlDisplay.draw(() -> shapes.forEach(BaseShape::draw));
    }

    public float getDisplayWidth() {
        return openGlDisplay.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return openGlDisplay.getDisplayHeight();
    }

}
