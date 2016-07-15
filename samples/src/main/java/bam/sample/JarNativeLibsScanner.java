package bam.sample;

import bam.opengl.JarNativeLibsScan;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Slf4j
class JarNativeLibsScanner implements JarNativeLibsScan {
    private static final String ROOT_LIB_DIR = "libs/native";
    private JavaLibraryPath javaLibraryPath;

    JarNativeLibsScanner() {
        final JavaTemporaryDirectory javaTemporaryDirectory = new JavaTemporaryDirectory();
        if (javaTemporaryDirectory.isTempDirectoryExist()) {
            javaLibraryPath = new JavaLibraryPath(javaTemporaryDirectory.getJavaTempDirectory().toString());
        }
    }

    @Override
    public void findAndAddNativeLibsToJavaLibraryPath() {
        SystemNameScanner.getSystemName().ifPresent(osName -> {
            final String osArch = SystemArchScanner.getSystemArch();
            final Path targetPath = Paths.get(ROOT_LIB_DIR, osName, osArch);
            findNativeLibraries(targetPath).forEach(javaLibraryPath::addFile);
        });
    }

    private Stream<Path> findNativeLibraries(final Path targetPath) {
        try {
            final URL jarURI = getClass().getProtectionDomain().getCodeSource().getLocation();
            final JarURLConnection juc = (JarURLConnection) jarURI.openConnection();
            final JarFile jarFile = juc.getJarFile();
            log.info("targetPath.toString() = {} ", targetPath.toString());
            return jarFile.stream()
                    .filter(o -> !o.isDirectory())
                    .filter(o -> o.getName().startsWith(targetPath.toString()))
                    .peek(o -> log.info(o.getName()))
                    .map(o -> o.));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return Stream.empty();
    }
}

