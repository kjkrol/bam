package bam.opengl.nativelibs;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class NativeLibsBinder {

    public void bindLibs(Stream<Path> paths) {
        final JavaTemporaryDirectory javaTemporaryDirectory = new JavaTemporaryDirectory();
        if (javaTemporaryDirectory.isTempDirectoryExist()) {
            final String javaTemporaryDir = javaTemporaryDirectory.getJavaTempDirectory().toString();
            final JavaLibraryPath javaLibraryPath = new JavaLibraryPath(javaTemporaryDir);
            paths.forEach(javaLibraryPath::addFile);
        }
    }

    private final class JavaLibraryPath {
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
                if (Files.notExists(tempFile)) {
                    Files.copy(libraryPath, tempFile).toFile().deleteOnExit();
                    log.info("copy file {} into dir {}", libraryPath, dir);
                } else {
                    log.info("file {} is already in dir {}", libraryPath, dir);
                }
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

    private final class JavaTemporaryDirectory {
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
