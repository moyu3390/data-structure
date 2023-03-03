
package cn.com.infosec.data.structure.asn.field.accessor;

import java.lang.reflect.Field;

/**
 * Help access and modify object field values.
 */
public interface FieldAccessor {
  /**
   * Modify field value inside of an object instance.
   *
   * @param instance object to mutate
   * @param field    field to change
   * @param value    new value of the field
   */
  void setFieldValue(Object instance, Field field, Object value);

  /**
   * Get the value of an objects field.
   *
   * @param instance object to access
   * @param field    field to read
   * @param <T>      type of value
   * @return value of the field
   */
  <T> T getFieldValue(Object instance, Field field);
}
