
package cn.com.infosec.data.structure.asn.tag;


/**
 * Tag type.
 */
public enum TagType {
  UNIVERSAL(0), APPLICATION(1), CONTEXT(2), PRIVATE(3);

  private final int code;

  public int getCode() {
    return code;
  }

  public static TagType fromCode(final int code) {
    for (final TagType tagType : TagType.values()) {
      if (tagType.getCode() == code) {
        return tagType;
      }
    }

    throw new IllegalArgumentException("Unknown Type code: " + code);
  }

  TagType(int code) {
    this.code = code;
  }
}