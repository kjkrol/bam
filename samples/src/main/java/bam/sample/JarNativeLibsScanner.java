package bam.sample;

import bam.opengl.JarNativeLibsScanning;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class JarNativeLibsScanner implements JarNativeLibsScanning {
    private static final String ROOT_LIB_DIR = "libs/native";
    private JavaLibraryPath javaLibraryPath;

    public JarNativeLibsScanner() {
        final JavaTemporaryDirectory javaTemporaryDirectory = new JavaTemporaryDirectory();
        if (javaTemporaryDirectory.isTempDirectoryExist()) {
            javaLibraryPath = new JavaLibraryPath(javaTemporaryDirectory.getJavaTempDirectory().toString());
        }
    }

    @Override
    public void findAndAddNativeLibsToJavaLibraryPath() {
        SystemNameScanner.getSystemName().ifPresent(osName -> {
            final String osArch = SystemArchScanner.getSystemArch();
            final Path targetPath = Paths.get(ROOT_LIB_DIR, osName, osArch);
            findNativeLibraries(targetPath)
                    .ifPresent(s -> s.forEach(javaLibraryPath::addFile));
        });
    }

    private Optional<Stream<Path>> findNativeLibraries(final Path targetPath) {
        log.error("############################## findNativeLibraries");
        try {
            log.info("############################## 0");
//            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();

            String path1 = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String jarURI = URLDecoder.decode(path1, "UTF-8");

            log.info("############################## 1, jarURI = {}", jarURI);
            final Path jarPath = Paths.get(jarURI);
            log.info("############################## 2, jarPath = {}", jarPath);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            log.info("############################## 3");
            final Path path = fileSystem.getPath(targetPath.toString());
            log.info("############################## 4");
            return Optional.of(Files.walk(path, 1).filter(Path::isAbsolute));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}

