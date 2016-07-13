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
import java.util.Optional;
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

    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String WIN = "win";

    private static final String X86 = "x86";
    private static final String X64 = "x64";

    private static final String ROOT_LIB_DIR = "libs/native/";

    private final NativeLibLoader nativeLibLoader = new NativeLibLoader();

    public void loadNativeLibs() {
        final String osName = getSystemName();
        final String osArch = getSystemArch();
        if (osName.startsWith(MAC)) {
            scanPathForNativeLibraries(ROOT_LIB_DIR + "/" + MAC + "/" + osArch)
                    .ifPresent(s -> s.forEach(nativeLibLoader::addFileToJavaLibraryPath));
        } else if (osName.startsWith(WIN)) {
            scanPathForNativeLibraries(ROOT_LIB_DIR + "/" + WIN + "/" + osArch)
                    .ifPresent(s -> s.forEach(nativeLibLoader::addFileToJavaLibraryPath));
        } else if (osName.startsWith(LINUX)) {
            scanPathForNativeLibraries(ROOT_LIB_DIR + "/" + LINUX + "/" + osArch)
                    .ifPresent(pathStream -> pathStream.forEach(nativeLibLoader::addFileToJavaLibraryPath));
        }
    }

    private Optional<Stream<Path>> scanPathForNativeLibraries(String path) {
        try {
            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            final Path jarPath = Paths.get(jarURI);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            final Path targetPath = fileSystem.getPath(path);
            return Optional.of(Files.walk(targetPath, 1).filter(Path::isAbsolute));
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    private String getSystemName() {
        return System.getProperty(OS_NAME).toLowerCase();
    }

    private String getSystemArch() {
        return System.getProperty(OS_ARCH).contains("64") ? X64 : X86;
    }
}
