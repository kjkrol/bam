package bam.common;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Set;

@Slf4j
public class NativeLibrariesLoader {
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final String JAVA_LIBRARY_PATH = "java.library.path";
    private static final String SYS_PATHS = "sys_paths";

    @NonNull
    private final Set<String> libNames;

    @NonNull
    private final String resourcesDirectoryWithingJarFile;

    private File osTempDirectory;

    @Builder
    private NativeLibrariesLoader(@Singular Set<String> libNames, String resourcesDirectoryWithingJarFile) {
        this.libNames = libNames;
        this.resourcesDirectoryWithingJarFile = resourcesDirectoryWithingJarFile;
    }

    public void loadLibraries() {
        if (isTempDirectoryExist()) {
            saveAllLibrariesIntoTemporaryOsSpace();
            addLibsToJavaLibraryPath();
        }
    }

    private boolean isTempDirectoryExist() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        osTempDirectory = new File(tmpDirName);
        return osTempDirectory.exists() || osTempDirectory.mkdir();
    }

    private void saveAllLibrariesIntoTemporaryOsSpace() {
        log.info("libNames size = {}", libNames.size());
        libNames.stream().forEach(libName -> {
            try {
                saveLibraryIntoTemporaryOsSpace(libName);
            } catch (UnsatisfiedLinkError | IOException e) {
                log.error("Native library: {} failed to load.\n", libName);
                log.error(e.getMessage(), e);
                System.exit(1);
            }
        });
    }

    private void saveLibraryIntoTemporaryOsSpace(final String libraryName) throws IOException {
        final InputStream in = initInputStreamToResourceFromJar(libraryName);
        final OutputStream out = initOutputStreamToTemporaryFile(libraryName);
        IOUtils.copy(in, out);
    }

    private InputStream initInputStreamToResourceFromJar(final String libraryName) {
        final String path = resourcesDirectoryWithingJarFile + libraryName;
        return NativeLibrariesLoader.class.getClassLoader().getResourceAsStream(path);
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
