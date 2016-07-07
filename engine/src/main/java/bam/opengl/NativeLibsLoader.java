package bam.opengl;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Loads native OpenGL libraries included in jar file.
 */
@Slf4j
@Builder
class NativeLibsLoader {

    /**
     * Temporary directory system property name
     */
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    private static final String JAVA_LIBRARY_PATH = "java.library.path";

    private static final String SYS_PATHS = "sys_paths";


    @Singular
    private final Set<String> libsNames;

    private final String subDir;

    /**
     * Loads JNI library residing in the jar.
     */
    void loadLibs() {
        getTempDir().ifPresent(tmpDir -> {
            unpackLibs(tmpDir);
            addLibsToJavaLibraryPath(tmpDir.getAbsolutePath());
        });
    }

    private Optional<File> getTempDir() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        final File tmpDir = new File(tmpDirName);
        if (tmpDir.exists() || tmpDir.mkdir()) {
            return Optional.of(tmpDir);
        }
        return Optional.empty();
    }

    private void unpackLibs(final File tmpDir) {
        libsNames.stream().forEach(libName -> {
            try {
                unpackLib(subDir, libName, tmpDir);
            } catch (UnsatisfiedLinkError | IOException e) {
                log.error("Native code library failed to load.\n");
                log.error(e.getMessage(), e);
                System.exit(1);
            }
        });
    }

    /**
     * Saves the library form the jar into the temporary OS space.
     *
     * @return absolute path to the library for this OS and arch
     * @throws IOException
     */
    private String unpackLib(final String subDir, final String libraryName, final File tmpDir)
            throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = getInputStreamToFile(subDir, libraryName);
            final File tempFile = createTempLibFile(libraryName, tmpDir);
            out = new FileOutputStream(tempFile);
            IOUtil.rewrite(in, out, Optional.empty(), new AtomicBoolean());
            log.info("Unpack lib file: " + tempFile.getAbsoluteFile());
            return tempFile.getAbsolutePath();
        } finally {
            closeWithCheck(in);
            closeWithCheck(out);
        }
    }

    private InputStream getInputStreamToFile(final String subDir, final String libraryName) {
        final String path = subDir + libraryName;
        return NativeLibsLoader.class.getClassLoader().getResourceAsStream(path);
    }

    private File createTempLibFile(final String libraryName, final File tmpDir) throws IOException {
        final File tempFile = new File(tmpDir + "/" + libraryName);
        tempFile.createNewFile();
        /* Clean up when exiting, delete created library file */
        tempFile.deleteOnExit();
        return tempFile;
    }

    private void addLibsToJavaLibraryPath(final String tmpDirName) {
        try {
            System.setProperty(JAVA_LIBRARY_PATH, tmpDirName);
            final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void closeWithCheck(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
