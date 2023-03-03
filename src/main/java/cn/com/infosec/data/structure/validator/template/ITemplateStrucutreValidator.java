/**
 * ITemplateStrucutreValidator
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 15:13
 */

package cn.com.infosec.data.structure.validator.template;

import cn.com.infosec.data.structure.validator.IDataStructureValidator;

public interface ITemplateStrucutreValidator<T> extends IDataStructureValidator<T> {
    void validator(String templateJson, String jsonData);
}
