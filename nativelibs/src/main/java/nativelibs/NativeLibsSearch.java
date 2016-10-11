package nativelibs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;

@FunctionalInterface
public interface NativeLibsSearch {

    String ROOT_LIB_DIR = "libs/native";

    default Set<Path> getNativeLibraries() {
        return new SystemNameScanner().getSystemName().map(osName -> {
            String osArch = new SystemArchScanner().getSystemArch();
            Path targetPath = Paths.get(ROOT_LIB_DIR, osName, osArch);
            return scan(targetPath);
        }).orElseGet(Collections::emptySet);
    }

    @SuppressWarnings("unchecked")
    default Set<Path> scan(Path targetPath) {
        return (Set<Path>) transform(targetPath)
                .map(path -> {
                    try {
                        return Files.walk(path.toAbsolutePath(), 1)
                                .filter(Path::isAbsolute)
                                .collect(toSet());
                    } catch (IOException e) {
                        return Collections.emptySet();
                    }
                })
                .orElseThrow(() -> new RuntimeException("Can not scan given path."));

    }

    Optional<Path> transform(Path targetPath);


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class SystemNameScanner {
        private static final String OS_NAME = "os.name";
        private static final String LINUX = "linux";
        private static final String MAC = "mac";
        private static final String WIN = "win";

        Optional<String> getSystemName() {
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
    class SystemArchScanner {
        private static final String OS_ARCH = "os.arch";
        private static final String X86 = "x86";
        private static final String X64 = "x64";

        String getSystemArch() {
            return System.getProperty(OS_ARCH).contains("64") ? X64 : X86;
        }
    }
}
