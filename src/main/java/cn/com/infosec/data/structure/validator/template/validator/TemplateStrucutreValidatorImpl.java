/**
 * TemplateStrucutreValidatorImpl
 * <p> 模板数据结构验证器
 * 1.0
 * <p>
 * 2022/12/29 15:06
 */

package cn.com.infosec.data.structure.validator.template.validator;

import cn.com.infosec.data.structure.exception.StructureException;
import cn.com.infosec.data.structure.json.IJsonComparator;
import cn.com.infosec.data.structure.json.comparator.JsonComparator;
import cn.com.infosec.data.structure.validator.template.ITemplateStrucutreValidator;

import java.util.Objects;

public class TemplateStrucutreValidatorImpl implements ITemplateStrucutreValidator<String[]> {
    IJsonComparator jsonComparator = new JsonComparator();

    @Override
    public void validator(String templateJson, String jsonData) {
        if (!jsonComparator.compare(jsonData, templateJson, true)) {
            throw new StructureException("数据结构与模板结构不一致,数据验证失败");
        }
    }

    @Override
    public void validator(String... jsonData) {
        // 判断使用哪个模板进行验证
        if (Objects.isNull(jsonData) || jsonData.length < 2) {
            throw new StructureException("校验的数据不正确");
        }
        validator(jsonData[1], jsonData[0]);
    }

    @Override
    public String getValidatorName() {
        return this.getClass().getSimpleName();
    }

}
