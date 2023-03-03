
package cn.com.infosec.data.structure.asn.field;

import cn.com.infosec.data.structure.asn.tag.Tag;

import java.lang.reflect.Field;

public class StructureTaggedField extends TaggedField {

  public StructureTaggedField(final int fieldPosition, final Tag tag, final Field field) {
    super(fieldPosition, tag, field);
  }

  @Override
  public boolean isStructure() {
    return true;
  }
}
