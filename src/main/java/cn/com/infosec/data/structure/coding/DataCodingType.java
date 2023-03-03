/**
 * DataCodingType
 * <p>
 * 1.0
 * <p>
 * 2023/1/5 14:37
 */

package cn.com.infosec.data.structure.coding;

public enum DataCodingType {

    BASE64(1, "B", "Base64"), HEX(2, "H", "Hex"), NONE(3, "N", "NONE");

    private final int index;
    private final String type;
    private final String name;

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static DataCodingType fromCode(final int code) {
        for (final DataCodingType type : DataCodingType.values()) {
            if (type.getIndex() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DataCodingType code: " + code);
    }

    public static DataCodingType fromCode(final String code) {
        for (final DataCodingType type : DataCodingType.values()) {
            if (String.valueOf(type.getIndex()).equals(code.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DataCodingType code: " + code);
    }

    public static DataCodingType fromType(final String type) {
        for (final DataCodingType t : DataCodingType.values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        try {
            return fromCode(type);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    DataCodingType(int index, String type, String name) {
        this.index = index;
        this.type = type;
        this.name = name;
    }
}
