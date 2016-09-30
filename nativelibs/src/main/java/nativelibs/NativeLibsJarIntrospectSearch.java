package nativelibs;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class NativeLibsJarIntrospectSearch implements NativeLibsSearch {
    @Override
    public Stream<Path> scan(Path targetPath) {
        try {
            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            final Path jarPath = Paths.get(jarURI);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            final Path path = fileSystem.getPath(targetPath.toString());
            return Files.walk(path, 1).filter(Path::isAbsolute);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Stream.empty();
    }
}
