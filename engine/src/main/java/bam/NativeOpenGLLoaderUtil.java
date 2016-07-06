package bam;

import bam.commons.NativeLibsLoaderUtil;

import java.util.Optional;

final class NativeOpenGLLoaderUtil {

    private static final String WINDOWS_DIR = "windows/";
    private static final String MACOSX_DIR = "macosx/";
    private static final String LINUX_DIR = "linux/";

    private NativeOpenGLLoaderUtil() {

    }

    static Optional<NativeLibsLoaderUtil.LibsWithSubDir> load(final String osName, final String osArch) {

        if (osName.startsWith(NativeLibsLoaderUtil.MAC)) {
            return Optional.of(new NativeLibsLoaderUtil.LibsWithSubDir(
                    new String[]{"libjinput-osx.dylib", "liblwjgl.dylib", "openal.dylib"},
                    MACOSX_DIR));
        } else if (osName.startsWith(NativeLibsLoaderUtil.WIN)) {
            if (osArch.equalsIgnoreCase(NativeLibsLoaderUtil.X86)) {
                return Optional.of(new NativeLibsLoaderUtil.LibsWithSubDir(
                        new String[]{"jinput-dx8.dll", "jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll"},
                        WINDOWS_DIR));
            } else {
                return Optional.of(new NativeLibsLoaderUtil.LibsWithSubDir(
                        new String[]{"jinput-dx8_64.dll", "jinput-raw_64.dll", "lwjgl64.dll", "OpenAL64.dll"},
                        WINDOWS_DIR));
            }
        } else if (osName.startsWith(NativeLibsLoaderUtil.LINUX)) {
            if (osArch.equalsIgnoreCase(NativeLibsLoaderUtil.I386)) {
                return Optional.of(new NativeLibsLoaderUtil.LibsWithSubDir(
                        new String[]{"libjinput-linux.so", "liblwjgl.so", "libopenal.so"},
                        LINUX_DIR));
            } else {
                return Optional.of(new NativeLibsLoaderUtil.LibsWithSubDir(
                        new String[]{"libjinput-linux64.so", "liblwjgl64.so", "libopenal64.so"},
                        LINUX_DIR));
            }
        }
        return Optional.empty();
    }
}
