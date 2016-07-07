package bam.opengl;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This <code>IOUtil</code> class provides static utility method for copying the data from one stream to another.
 * <p/>
 */
@NoArgsConstructor
final class IOUtil {

    /**
     * The default buffer size (32 kB)
     */
    private static final int BUFFER_SIZE = 1024 * 1024 * 32;

    private static final String ERR_MSG_PATTERN_EXCEEDED_MAXIMUM_SIZE_OF_THE_BUFFER = "Exceeded maximum size of the buffer [%d/%d]";

    private static final String OPERATION_HAS_BEEN_CANCELLED_BY_OTHER_PROCESS = "Operation has been cancelled by other process.";


    /**
     * Copies bytes from an InputStream to an OutputStream.
     * Operation could be interrupted by an required semaphore.
     *
     * @param is                                the input stream
     * @param os                                the output stream
     * @param permissibleNumberOfBytesToRewrite the permissible number of bytes to rewrite
     * @param interrupted                       the flag is check in loop; its current value determine if
     *                                          continue process or terminate with {@link java.io.IOException} throw
     * @return the number of rewritten bytes
     * @throws java.io.IOException If the first byte cannot be read for any reason other than
     *                             the end of the file, if the input stream has been closed, or
     *                             if some other I/O error occurs.
     */
    public static long rewrite(final InputStream is, final OutputStream os,
                               final Optional<Long> permissibleNumberOfBytesToRewrite,
                               final AtomicBoolean interrupted) throws IOException {
        /* the overall number of rewritten bytes */
        long allReadBytes = 0L;
        if (null != is) {
            /*
             * Prepare small buffer that will be used to rewrite data between
             * streams.
             */
            final byte[] bytes = new byte[BUFFER_SIZE];
            /*
             * initiate the counter of read bits (it does not contain overall
             * number of read bits but the number of read bits for each loop
             * cycle)
             */
            int byteCount = is.read(bytes);
            final long permissibleNoBtR = permissibleNumberOfBytesToRewrite.orElse(Long.MAX_VALUE);

            while (byteCount > -1) {
                if (interrupted.get()) {
                    throw new IOException(OPERATION_HAS_BEEN_CANCELLED_BY_OTHER_PROCESS);
                }
                /* Append (overall) counter */
                allReadBytes += byteCount;
                /* Check if acceptable buffer size was not exceeded. */

                if (allReadBytes > permissibleNoBtR) {
                    throw new IOException(String.format(ERR_MSG_PATTERN_EXCEEDED_MAXIMUM_SIZE_OF_THE_BUFFER,
                            allReadBytes, permissibleNoBtR));
                }
                /* Chucking and rewriting data. */
                os.write(bytes, 0, byteCount);
                os.flush();
                byteCount = is.read(bytes);
            }
        }
        return allReadBytes;
    }

}
