package bam.common.jarscan;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public final class JarFileIntrospectNativeLibsScan {
    private static final String ROOT_LIB_DIR = "libs/native";

    public Stream<Path> getNativeLibraries() {
        return new SystemNameScanner().getSystemName().map(osName -> {
            final String osArch = new SystemArchScanner().getSystemArch();
            final Path targetPath = Paths.get(ROOT_LIB_DIR, osName, osArch);
            return scan(targetPath);
        }).orElseGet(Stream::empty);
    }

    private Stream<Path> scan(final Path targetPath) {
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

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private final class SystemNameScanner {
        private static final String OS_NAME = "os.name";
        private static final String LINUX = "linux";
        private static final String MAC = "mac";
        private static final String WIN = "win";

        Optional<String> getSystemName() {
            final String osName = System.getProperty(OS_NAME).toLowerCase();
            final Pattern pattern = Pattern.compile("(" + MAC + "|" + WIN + "|" + LINUX + ")");
            final Matcher matcher = pattern.matcher(osName);
            if (matcher.find()) {
                return Optional.of(matcher.group(1));
            }
            return Optional.empty();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private final class SystemArchScanner {
        private static final String OS_ARCH = "os.arch";
        private static final String X86 = "x86";
        private static final String X64 = "x64";

        String getSystemArch() {
            return System.getProperty(OS_ARCH).contains("64") ? X64 : X86;
        }
    }
}
