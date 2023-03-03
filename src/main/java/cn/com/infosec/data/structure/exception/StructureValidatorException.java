/**
 * StructureValidatorException
 * <p>
 * 1.0
 * <p>
 * 2023/1/3 16:49
 */

package cn.com.infosec.data.structure.exception;

public class StructureValidatorException extends StructureException {
    public StructureValidatorException(String msg) {
        super(msg);
    }

    public StructureValidatorException(Exception e) {
        super(e);
    }

    public StructureValidatorException(String msg, Exception e) {
        super(msg, e);
    }
}
