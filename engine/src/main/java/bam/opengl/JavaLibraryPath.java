package bam.opengl;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public final class JavaLibraryPath {
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";
    private final String dir;

    public JavaLibraryPath(String dir) {
        this.dir = dir;
        init();
    }

    public void addFile(Path libraryPath) {
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
