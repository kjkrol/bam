package bam.nat;

import bam.commons.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Allows loading libraries included in jar file.
 *
 * @author Karol Krol
 * @since 1.0.0
 */
public final class NativeOpenGlLibsLoaderUtil {

    /**
     * Class logger
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(NativeOpenGlLibsLoaderUtil.class);

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
    private static final String WIN = "win";

    /**
     * Mac OS X OS name
     */
    private static final String MAC = "mac";

    /**
     * Linux OS name
     */
    private static final String LINUX = "linux";

    /**
     * 64 bits Architecture type
     */
    private static final String AMD64 = "amd64";
    private static final String IA64 = "ia64";

    /**
     * Name of the directory containing libraries for supported OSs and archs
     */
    private static final String ROOT_LIB_DIR = "libs/native/";

    private static final String WINDOWS_DIR = "windows/";
    private static final String MACOSX_DIR = "macosx/";
    private static final String LINUX_DIR = "linux/";
    private static final String SOLARIS_DIR = "solaris/";

    /**
     * Size of the buffer for saving library
     */
    private static final int BUFFER_SIZE = 16 * 1024;
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";

    /**
     * Hidden constructor.
     */
    private NativeOpenGlLibsLoaderUtil() {
    }

    /**
     * Loads JNI library residing in the jar.
     */
    public static void loadLibs() {

        final File tmpDir = getTempDir();
        String[] libsNames = null;
        String subDir = null;

        final String osName = System.getProperty(OS_NAME).toLowerCase();
        if (osName.startsWith(MAC)) {
            libsNames = new String[]{"libjinput-osx.dylib", "liblwjgl.dylib", "openal.dylib"};
            subDir = MACOSX_DIR;
        }
        unpackLibs(libsNames, subDir, tmpDir);
        addLibsToJavaLibraryPath(tmpDir.getAbsolutePath());
    }

    /**
     *
     * @return
     */
    private static File getTempDir() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        final File tmpDir = new File(tmpDirName);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        return tmpDir;
    }

    /**
     *
     * @param libsNames
     * @param subDir
     * @param tmpDir
     */
    private static void unpackLibs(final String[] libsNames, final String subDir, final File tmpDir) {

        Arrays.stream(libsNames).forEach(libName -> {
            try {
                unpackLib(subDir, libName, tmpDir);
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
    private static String unpackLib(final String subDir, final String libraryName, final File tmpDir) throws IOException {

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

    /**
     *
     * @param subDir
     * @param libraryName
     * @return
     */
    private static InputStream getInputStreamToFile(final String subDir, final String libraryName) {
        final String path = ROOT_LIB_DIR + subDir + libraryName;
        return NativeOpenGlLibsLoaderUtil.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     *
     * @param libraryName
     * @param tmpDir
     * @return
     * @throws IOException
     */
    private static File createTempLibFile(final String libraryName, final File tmpDir) throws IOException {
        final File tempFile = new File(tmpDir + "/" + libraryName);
        tempFile.createNewFile();
        /* Clean up when exiting, delete created library file */
        tempFile.deleteOnExit();
        return tempFile;
    }

    /**
     *
     * @param closeable
     */
    private static void closeWithCheck(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @param tmpDirName
     */
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
