/**
 * IDataStructureValidator
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 15:05
 */

package cn.com.infosec.data.structure.validator;

public interface IDataStructureValidator<T> {



    void validator(T data);

    String getValidatorName();
}
