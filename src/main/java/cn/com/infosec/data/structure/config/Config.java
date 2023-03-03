/**
 * Config
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 15:30
 */

package cn.com.infosec.data.structure.config;

import cn.com.infosec.data.structure.utils.ConfigPropertiesUtil;

import java.io.Serializable;

public class Config implements Serializable {
    public static final boolean isValidator = Boolean.valueOf(ConfigPropertiesUtil.getConfigProperty("isValidator", "false"));
    public static final String asn1StrucutreValidatorName = ConfigPropertiesUtil.getConfigProperty("Asn1StrucutreValidatorName", "Asn1StrucutreSimpleValidatorImpl");
    public static final String templateStrucutreValidatorName = ConfigPropertiesUtil.getConfigProperty("TemplateStrucutreValidatorImpl", "TemplateStrucutreValidatorImpl");

    public static final boolean onlyStructure = Boolean.valueOf(ConfigPropertiesUtil.getConfigProperty("onlyStructure", "false"));


    private Config() {
    }
}
