package bam.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
class NativeLibLoader {

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";

    private File osTempDirectory;

    private boolean successfullyInitialized;

    NativeLibLoader() {
        init();
    }

    void addFileToJavaLibraryPath(Path libraryPath) {
        if (successfullyInitialized) {
            final File tempFile = new File(osTempDirectory.getAbsolutePath() + "/" + libraryPath.getFileName().toString());
            try {
                Files.copy(libraryPath, tempFile.toPath());
                tempFile.deleteOnExit();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void init() {
        if (isTempDirectoryExist()) {
            successfullyInitialized = setupJavaLibraryPath();
        }
    }

    private boolean isTempDirectoryExist() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        osTempDirectory = new File(tmpDirName);
        return osTempDirectory.exists() || osTempDirectory.mkdir();
    }


    private boolean setupJavaLibraryPath() {
        try {
            System.setProperty(JAVA_LIBRARY_PATH, osTempDirectory.getAbsolutePath());
            final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
            return true;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
