package bam.common;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

@Slf4j
class NativeLibLoader {
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";
    private final Path libraryPath;

    private File osTempDirectory;

    @Builder
    private NativeLibLoader(final Path libraryPath) {
        this.libraryPath = libraryPath;
    }

    void loadLibraries() {
        if (isTempDirectoryExist()) {
            saveLibraryIntoTemporaryOsSpace();
            addLibsToJavaLibraryPath();
        }
    }

    private boolean isTempDirectoryExist() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        osTempDirectory = new File(tmpDirName);
        return osTempDirectory.exists() || osTempDirectory.mkdir();
    }

    private void saveLibraryIntoTemporaryOsSpace() {
        try {
            saveLibraryIntoTemporaryOsSpace(libraryPath);
        } catch (UnsatisfiedLinkError | IOException e) {
            log.error("Native library: {} failed to load.\n", libraryPath);
            log.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    private void saveLibraryIntoTemporaryOsSpace(final Path libraryPath) throws IOException {
        final InputStream in = initInputStreamToResourceFromJar(libraryName);
        final OutputStream out = initOutputStreamToTemporaryFile(libraryName);
        IOUtils.copy(in, out);
    }

    private InputStream initInputStreamToResourceFromJar(final String libraryName) {
        return NativeLibLoader.class.getClassLoader().getResourceAsStream(path);
    }

    private OutputStream initOutputStreamToTemporaryFile(final String libraryName) throws IOException {
        final File temporaryFile = createTemporaryFile(libraryName);
        return new FileOutputStream(temporaryFile);
    }

    private File createTemporaryFile(final String libraryName) throws IOException {
        final File tempFile = new File(osTempDirectory, libraryName);
        if (tempFile.createNewFile()) {
            tempFile.deleteOnExit();
        }
        return tempFile;
    }

    private void addLibsToJavaLibraryPath() {
        try {
            System.setProperty(JAVA_LIBRARY_PATH, osTempDirectory.getAbsolutePath());
            final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        }
    }
}
