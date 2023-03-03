
package cn.com.infosec.data.structure.asn.field;

import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.tag.Tag;

import java.lang.reflect.Field;

public class PrimitiveTaggedField extends TaggedField {
  private final Class<? extends AsnConverter<?, ?>> converter;

  public PrimitiveTaggedField(final int fieldPosition, final Tag tag, final Field field, final Class<? extends AsnConverter<?, ?>> converter) {
    super(fieldPosition, tag, field);
    this.converter = converter;
  }

  public Class<? extends AsnConverter<?, ?>> getConverter() {
    return converter;
  }

  @Override
  public boolean isPrimitive() {
    return true;
  }
}
