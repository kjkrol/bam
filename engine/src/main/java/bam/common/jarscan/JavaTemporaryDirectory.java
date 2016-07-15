package bam.common.jarscan;

import lombok.Getter;

import java.io.File;

public final class JavaTemporaryDirectory {
    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    @Getter
    private File javaTempDirectory;

    public boolean isTempDirectoryExist() {
        final String tmpDirName = System.getProperty(JAVA_IO_TMPDIR);
        javaTempDirectory = new File(tmpDirName);
        return javaTempDirectory.exists() || javaTempDirectory.mkdir();
    }
}
