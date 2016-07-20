package bam;

/**
 * @author Karol Krol
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface Function4Args<R, A, B, C, D> {

    /**
     * Applies this function to the given arguments.
     *
     * @param a the first function argument
     * @param b the second function argument
     * @param c the third function argument
     * @param d the fourth function argument
     * @return the function result
     */
    R apply(A a, B b, C c, D d);
}
