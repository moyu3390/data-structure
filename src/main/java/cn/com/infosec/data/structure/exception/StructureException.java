/**
 * StructureException
 * <p>
 * 1.0
 * <p>
 * 2022/12/27 18:21
 */

package cn.com.infosec.data.structure.exception;

public class StructureException extends RuntimeException {
    public StructureException(final String msg) {
        super(msg);
    }

    public StructureException(final Exception e) {
        super(e);
    }

    public StructureException(final String msg, final Exception e) {
        super(msg, e);
    }
}
