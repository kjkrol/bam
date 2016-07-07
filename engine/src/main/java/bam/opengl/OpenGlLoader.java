package bam.opengl;

public class OpenGlLoader {

    /**
     * OS name system property name
     */
    private static final String OS_NAME = "os.name";

    /**
     * OS arch system property name
     */
    private static final String OS_ARCH = "os.arch";

    private static final String WIN = "win";

    private static final String MAC = "mac";

    private static final String LINUX = "linux";

    private static final String X86 = "x86";

    private static final String I386 = "i386";

    /**
     * Name of the directory containing libraries for supported OS and arch
     */
    private static final String ROOT_LIB_DIR = "libs/native/";

    private static final String WINDOWS_DIR = ROOT_LIB_DIR + "windows/";

    private static final String MACOSX_DIR = ROOT_LIB_DIR + "macosx/";

    private static final String LINUX_DIR = ROOT_LIB_DIR + "linux/";

    private final String osName = System.getProperty(OS_NAME).toLowerCase();

    private final String osArch = System.getProperty(OS_ARCH);

    public void load() {

        if (osName.startsWith(MAC)) {
            NativeLibsLoader.builder()
                    .libsName("libjinput-osx.dylib")
                    .libsName("liblwjgl.dylib")
                    .libsName("openal.dylib")
                    .subDir(MACOSX_DIR)
                    .build().loadLibs();
        } else if (osName.startsWith(WIN)) {
            if (osArch.equalsIgnoreCase(X86)) {
                NativeLibsLoader.builder()
                        .libsName("jinput-dx8.dll")
                        .libsName("jinput-raw.dll")
                        .libsName("lwjgl.dll")
                        .libsName("OpenAL32.dll")
                        .subDir(WINDOWS_DIR)
                        .build().loadLibs();
            } else {
                NativeLibsLoader.builder()
                        .libsName("jinput-dx8_64.dll")
                        .libsName("jinput-raw_64.dll")
                        .libsName("lwjgl64.dll")
                        .libsName("OpenAL64.dll")
                        .subDir(WINDOWS_DIR)
                        .build().loadLibs();
            }
        } else if (osName.startsWith(LINUX)) {
            if (osArch.equalsIgnoreCase(I386)) {
                NativeLibsLoader.builder()
                        .libsName("libjinput-linux.so")
                        .libsName("liblwjgl.so")
                        .libsName("libopenal.so")
                        .subDir(LINUX_DIR)
                        .build().loadLibs();
            } else {
                NativeLibsLoader.builder()
                        .libsName("libjinput-linux64.so")
                        .libsName("liblwjgl64.so")
                        .libsName("libopenal64.so")
                        .subDir(LINUX_DIR)
                        .build().loadLibs();
            }
        }
    }
}
