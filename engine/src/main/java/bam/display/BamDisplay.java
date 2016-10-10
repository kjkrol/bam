package bam.display;

import bam.display.opengl.OpenGlDisplay;
import bam.shape.model.base.BaseShape;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

//TODO: ta klasa powinna działać w osobnym watku i dostawac polecenia poprzez kolejke komunikatow
//TODO: ta klasa powinna miec lepsza nazwe
//TODO: ta klasa powinna w calosci przejac odpowiedzialnosc za kontrolę prezentacji grafiki
public class BamDisplay {

    private final OpenGlDisplay openGlDisplay;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private AtomicBoolean display = new AtomicBoolean();

    public BamDisplay(DisplayParams displayParams) {
        this.openGlDisplay = new OpenGlDisplay(displayParams);
    }

    public void run(Set<BaseShape> shapes) {
        openGlDisplay.start();
        while (display.compareAndSet(false, true)) {
            executorService.submit(() ->
                    openGlDisplay.redraw(() -> shapes.forEach(BaseShape::draw))
            );
        }
    }

    public float getDisplayWidth() {
        return openGlDisplay.getDisplayWidth();
    }

    public float getDisplayHeight() {
        return openGlDisplay.getDisplayHeight();
    }

}
