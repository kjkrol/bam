package bam.opengl;

import bam.common.CrossPlatformLibsLoader;

public class OpenGlLoader {
    public void load() {
        new CrossPlatformLibsLoader().loadNativeLibs();
    }
}
