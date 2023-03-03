/**
 * ValidatorExecutor
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 15:43
 */

package cn.com.infosec.data.structure.validator;

import cn.com.infosec.data.structure.asn.validator.impl.Asn1StrucutreSimpleValidatorImpl;
import cn.com.infosec.data.structure.config.Config;
import cn.com.infosec.data.structure.exception.StructureValidatorException;
import cn.com.infosec.data.structure.validator.template.ITemplateStrucutreValidator;
import cn.com.infosec.data.structure.validator.template.validator.TemplateStrucutreValidatorImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ValidatorExecutor {
    private static final Map<String, IDataStructureValidator> dataStructureValidatorMap;


    static {
        dataStructureValidatorMap = initValidator();
    }


    public static void execut(ValidatorType validatorType, Object data, Object templateData) {
        if (!Config.isValidator) {
            return;
        }
        String validatorName = Config.asn1StrucutreValidatorName;
        if (validatorType.equals(ValidatorType.JSON)) {
            validatorName = Config.templateStrucutreValidatorName;
        }
        IDataStructureValidator validator = dataStructureValidatorMap.get(validatorName);
        if (Objects.isNull(validator)) {
//            return ;
            throw new StructureValidatorException("没有找到合适的验证器");
        }
        if (validatorType.equals(ValidatorType.JSON)) {
            ((ITemplateStrucutreValidator) validator).validator(templateData.toString(), data.toString());
            return;
        }
        validator.validator(data);
    }


    private static Map<String, IDataStructureValidator> initValidator() {
        Map<String, IDataStructureValidator> structureValidatorMap = new HashMap<>();
        // 后期可以动态扫描验证器实现类
        structureValidatorMap.put(TemplateStrucutreValidatorImpl.class.getSimpleName(), new TemplateStrucutreValidatorImpl());
        structureValidatorMap.put(Asn1StrucutreSimpleValidatorImpl.class.getSimpleName(), new Asn1StrucutreSimpleValidatorImpl());
        return structureValidatorMap;
    }
}
