
package cn.com.infosec.data.structure.asn.field;

import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.tag.Tag;

import java.lang.reflect.Field;

public class CollectionTaggedField extends TaggedField {
  private final boolean structured;
  private final Class<?> type;
  private final Tag elementTag;
  private final Class<? extends AsnConverter<?, ?>> converter;

  public CollectionTaggedField(final int fieldPosition, final Tag tag, final Field field, final boolean structured, final Class<?> type, final Tag elementTag,
                               final Class<? extends AsnConverter<?, ?>> converter) {
    super(fieldPosition, tag, field);
    this.structured = structured;
    this.type = type;
    this.elementTag = elementTag;
    this.converter = converter;
  }

  @Override
  public boolean isCollection() {
    return true;
  }

  public boolean isStructured() {
    return structured;
  }

  public Class<?> getType() {
    return type;
  }

  public Tag getElementTag() {
    return elementTag;
  }

  public Class<? extends AsnConverter<?, ?>> getConverter() {
    return converter;
  }
}
