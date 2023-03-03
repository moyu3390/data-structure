
package cn.com.infosec.data.structure.asn.field;

import cn.com.infosec.data.structure.asn.tag.Tag;

import java.lang.reflect.Field;

public abstract class TaggedField implements Comparable<TaggedField> {
  private final int fieldPosition; // helps keep the class defined order when encoding
  private final Tag tag;
  private final Field field;

  public TaggedField(int fieldPosition, Tag tag, Field field) {
    this.fieldPosition = fieldPosition;
    this.tag = tag;
    this.field = field;
  }

  public boolean isPrimitive() {
    return false;
  }

  public boolean isStructure() {
    return false;
  }

  public boolean isCollection() {
    return false;
  }

  public int getFieldPosition() {
    return fieldPosition;
  }

  public Tag getTag() {
    return tag;
  }

  public Field getField() {
    return field;
  }

  @Override
  public int compareTo(final TaggedField tf) {
    return this.fieldPosition > tf.fieldPosition ? 1 : -1;
  }
}