/**
 * ValidatorType
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 16:00
 */

package cn.com.infosec.data.structure.validator;

import cn.com.infosec.data.structure.asn.validator.impl.Asn1StrucutreSimpleValidatorImpl;

public enum ValidatorType {
    DER(0, Asn1StrucutreSimpleValidatorImpl.class.getSimpleName()), JSON(1, Asn1StrucutreSimpleValidatorImpl.class.getSimpleName());

    private final int code;
    private final String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ValidatorType fromCode(final int code) {
        for (final ValidatorType type : ValidatorType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ValidatorType code: " + code);
    }

    ValidatorType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
