package bam.common;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class CrossPlatformLibsLoader {
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
    private static final String X64 = "x64";
    private static final String X86 = "x86";
    private static final String ROOT_LIB_DIR = "libs/native/";
    private static final String MAC_OSX_DIR = ROOT_LIB_DIR + "macosx/";
    private static final String WINDOWS_DIR = ROOT_LIB_DIR + "windows/";
    private static final String LINUX_DIR = ROOT_LIB_DIR + "linux/";

    private final Set<Path> libs = new HashSet<>();

    public void loadNativeLibs() {
        final String osName = System.getProperty(OS_NAME).toLowerCase();
        final String osArch = System.getProperty(OS_ARCH);
        if (osName.startsWith(MAC)) {
            scanPath(MAC_OSX_DIR + X64);
        } else if (osName.startsWith(WIN)) {
            if (osArch.equalsIgnoreCase(X64)) {
                scanPath(WINDOWS_DIR + X64);
            } else {
                scanPath(WINDOWS_DIR + X86);
            }
        } else if (osName.startsWith(LINUX)) {
            if (osArch.equalsIgnoreCase(X64)) {
                scanPath(LINUX_DIR + X64);
            } else {
                scanPath(LINUX_DIR + X86);
            }
        }
        traverseLibs(libs);
    }

    private void scanPath(String path) {
        try {
            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            final Path jarPath = Paths.get(jarURI);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            final Path targetPath = fileSystem.getPath(path);
            final Stream<Path> walk = Files.walk(targetPath, 1);
            walk.forEach(libs::add);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void traverseLibs(Set<Path> libsPaths) {
        libsPaths.forEach(libPath -> NativeLibLoader.builder()
                .libraryName(libPath)
                .build()
                .loadLibraries()
        );
    }
}
