package bam.opengl;

import bam.common.NativeLibrariesLoader;

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
            NativeLibrariesLoader.builder()
                    .libName("libjinput-osx.dylib")
                    .libName("liblwjgl.dylib")
                    .libName("openal.dylib")
                    .resourcesDirectoryWithingJarFile(MACOSX_DIR)
                    .build().loadLibraries();
        } else if (osName.startsWith(WIN)) {
            if (osArch.equalsIgnoreCase(X86)) {
                NativeLibrariesLoader.builder()
                        .libName("jinput-dx8.dll")
                        .libName("jinput-raw.dll")
                        .libName("lwjgl.dll")
                        .libName("OpenAL32.dll")
                        .resourcesDirectoryWithingJarFile(WINDOWS_DIR)
                        .build().loadLibraries();
            } else {
                NativeLibrariesLoader.builder()
                        .libName("jinput-dx8_64.dll")
                        .libName("jinput-raw_64.dll")
                        .libName("lwjgl64.dll")
                        .libName("OpenAL64.dll")
                        .resourcesDirectoryWithingJarFile(WINDOWS_DIR)
                        .build().loadLibraries();
            }
        } else if (osName.startsWith(LINUX)) {
            if (osArch.equalsIgnoreCase(I386)) {
                NativeLibrariesLoader.builder()
                        .libName("libjinput-linux.so")
                        .libName("liblwjgl.so")
                        .libName("libopenal.so")
                        .resourcesDirectoryWithingJarFile(LINUX_DIR)
                        .build().loadLibraries();
            } else {
                NativeLibrariesLoader.builder()
                        .libName("libjinput-linux64.so")
                        .libName("liblwjgl64.so")
                        .libName("libopenal64.so")
                        .resourcesDirectoryWithingJarFile(LINUX_DIR)
                        .build().loadLibraries();
            }
        }
    }
}
