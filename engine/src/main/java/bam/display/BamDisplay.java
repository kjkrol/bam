package bam.display;

import bam.display.opengl.OpenGlDisplay;

//TODO: ta klasa powinna działać w osobnym watku i dostawac polecenia poprzez kolejke komunikatow
//TODO: ta klasa powinna miec lepsza nazwe
//TODO: ta klasa powinna w calosci przejac odpowiedzialnosc za kontrolę prezentacji grafiki
public class BamDisplay {

    private final OpenGlDisplay openGlDisplay;

    public BamDisplay(DisplayParams displayParams) {
        this.openGlDisplay = new OpenGlDisplay(displayParams);
    }
}
