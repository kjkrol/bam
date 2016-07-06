package bam.commons;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

/**
 * Loads native OpenGL libraries included in jar file.
 *
 * @author Karol Krol
 * @since 1.0.0
 */
public final class NativeLibsLoaderUtil {

    /**
     * Class logger
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(NativeLibsLoaderUtil.class);

    /**
     * OS name system property name
     */
    private static final String OS_NAME = "os.name";

    /**
     * OS arch system property name
     */
    private static final String OS_ARCH = "os.arch";

    /**
     * Temporary directory system property name
     */
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
     * Windows OS name
     */
    public static final String WIN = "win";

    /**
     * Mac OS X OS name
     */
    public static final String MAC = "mac";

    /**
     * Linux OS name
     */
    public static final String LINUX = "linux";

    /**
     * 64 bits Architecture type
     */
    public static final String X86 = "x86";
    public static final String I386 = "i386";

    /**
     * Name of the directory containing libraries for supported OSs and archs
     */
    private static final String ROOT_LIB_DIR = "libs/native/";

    /**
     * Size of the buffer for saving library
     */
    private static final int BUFFER_SIZE = 16 * 1024;
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";

    /**
     * Hidden constructor.
     */
    private NativeLibsLoaderUtil() {
    }

    @Data
    public static class LibsWithSubDir {
        private final String[] libsNames;
        private final String subDir;
    }

    /**
     * Loads JNI library residing in the jar.
     */
    public static void loadLibs(BiFunction<String, String, Optional<LibsWithSubDir>> biFunction) {

        final File tmpDir = getTempDir();
        final String osName = System.getProperty(OS_NAME).toLowerCase();
        final String osArch = System.getProperty(OS_ARCH);
        biFunction.apply(osName, osArch).ifPresent(libsWithSubDir -> {
            unpackLibs(libsWithSubDir, tmpDir);
            addLibsToJavaLibraryPath(tmpDir.getAbsolutePath());
        });
    }

    private static File getTempDir() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        final File tmpDir = new File(tmpDirName);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        return tmpDir;
    }

    private static void unpackLibs(final LibsWithSubDir libsWithSubDir, final File tmpDir) {
        Arrays.stream(libsWithSubDir.getLibsNames()).forEach(libName -> {
            try {
                unpackLib(libsWithSubDir.getSubDir(), libName, tmpDir);
            } catch (UnsatisfiedLinkError | IOException e) {
                LOGGER.error("Native code library failed to load.\n");
                LOGGER.error(e.getMessage(), e);
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
    private static String unpackLib(final String subDir, final String libraryName, final File tmpDir)
            throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = getInputStreamToFile(subDir, libraryName);
            final File tempFile = createTempLibFile(libraryName, tmpDir);
            out = new FileOutputStream(tempFile);
            IOUtil.rewrite(in, out, Optional.empty(), new AtomicBoolean());
            LOGGER.info("Unpack lib file: " + tempFile.getAbsoluteFile());
            return tempFile.getAbsolutePath();
        } finally {
            closeWithCheck(in);
            closeWithCheck(out);
        }
    }

    private static InputStream getInputStreamToFile(final String subDir, final String libraryName) {
        final String path = ROOT_LIB_DIR + subDir + libraryName;
        return NativeLibsLoaderUtil.class.getClassLoader().getResourceAsStream(path);
    }

    private static File createTempLibFile(final String libraryName, final File tmpDir) throws IOException {
        final File tempFile = new File(tmpDir + "/" + libraryName);
        tempFile.createNewFile();
        /* Clean up when exiting, delete created library file */
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static void closeWithCheck(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static void addLibsToJavaLibraryPath(final String tmpDirName) {
        try {
            System.setProperty(JAVA_LIBRARY_PATH, tmpDirName);
            final Field fieldSysPath = ClassLoader.class.getDeclaredField(SYS_PATHS);
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
