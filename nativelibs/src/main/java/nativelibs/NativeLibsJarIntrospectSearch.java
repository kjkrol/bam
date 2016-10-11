package nativelibs;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class NativeLibsJarIntrospectSearch implements NativeLibsSearch {

    @Override
    public Optional<Path> transform(Path targetPath) {
        try {
            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            final Path jarPath = Paths.get(jarURI);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            return Optional.of(fileSystem.getPath(targetPath.toString()));
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

}
