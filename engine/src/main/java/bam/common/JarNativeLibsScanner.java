package bam.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
public class JarNativeLibsScanner {
    private static final String ROOT_LIB_DIR = "libs/native";
    private JavaLibraryPath javaLibraryPath;

    public JarNativeLibsScanner() {
        final JavaTemporaryDirectory javaTemporaryDirectory = new JavaTemporaryDirectory();
        if (javaTemporaryDirectory.isTempDirectoryExist()) {
            javaLibraryPath = new JavaLibraryPath(javaTemporaryDirectory.getJavaTempDirectory().toString());
        }
    }

    public void findAndAddNativeLibsToJavaLibraryPath() {
        SystemNameScanner.getSystemName().ifPresent(osName -> {
            final String osArch = SystemArchScanner.getSystemArch();
            final Path targetPath = Paths.get(ROOT_LIB_DIR, osName, osArch);
            findNativeLibraries(targetPath)
                    .ifPresent(s -> s.forEach(javaLibraryPath::addFile));
        });
    }

    private Optional<Stream<Path>> findNativeLibraries(final Path targetPath) {
        try {
            final URI jarURI = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            final Path jarPath = Paths.get(jarURI);
            final FileSystem fileSystem = FileSystems.newFileSystem(jarPath, null);
            final Path path = fileSystem.getPath(targetPath.toString());
            return Optional.of(Files.walk(path, 1).filter(Path::isAbsolute));
        } catch (IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class SystemNameScanner {
        private static final String OS_NAME = "os.name";
        private static final String LINUX = "linux";
        private static final String MAC = "mac";
        private static final String WIN = "win";

        static Optional<String> getSystemName() {
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
    private static final class SystemArchScanner {
        private static final String OS_ARCH = "os.arch";
        private static final String X86 = "x86";
        private static final String X64 = "x64";

        static String getSystemArch() {
            return System.getProperty(OS_ARCH).contains("64") ? X64 : X86;
        }
    }

    private static final class JavaLibraryPath {
        private static final String JAVA_LIBRARY_PATH = "java.library.path";
        private static final String SYS_PATHS = "sys_paths";

        private final String dir;

        JavaLibraryPath(String dir) {
            this.dir = dir;
            init();
        }

        void addFile(Path libraryPath) {
            final Path tempFile = Paths.get(dir, libraryPath.getFileName().toString());
            try {
                Files.copy(libraryPath, tempFile).toFile().deleteOnExit();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        private void init() {
            try {
                System.setProperty(JAVA_LIBRARY_PATH, dir);
                final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
                fieldSysPath.setAccessible(true);
                fieldSysPath.set(null, null);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private static class JavaTemporaryDirectory {
        private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

        @Getter
        private File javaTempDirectory;

        boolean isTempDirectoryExist() {
            final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
            javaTempDirectory = new File(tmpDirName);
            return javaTempDirectory.exists() || javaTempDirectory.mkdir();
        }
    }
}
