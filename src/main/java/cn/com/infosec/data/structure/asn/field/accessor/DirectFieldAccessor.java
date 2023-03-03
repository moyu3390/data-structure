
package cn.com.infosec.data.structure.asn.field.accessor;

import cn.com.infosec.data.structure.asn.exception.AsnAccessException;

import java.lang.reflect.Field;

public class DirectFieldAccessor implements FieldAccessor {

  @Override
  public void setFieldValue(final Object instance, final Field field, final Object value) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }

    try {
      field.set(instance, value);
    } catch (final IllegalAccessException e) {
      throw new AsnAccessException(e);
    }
  }

  @Override
  public <T> T getFieldValue(final Object instance, final Field field) {
    if (!field.isAccessible()) {
      field.setAccessible(true);
    }

    try {
      //noinspection unchecked
      return (T) field.get(instance);
    } catch (final IllegalAccessException e) {
      throw new AsnAccessException(e);
    }
  }
}