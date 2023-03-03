
package cn.com.infosec.data.structure.asn.util;

public final class ClassUtils {

  public static boolean isPrimitiveOrWrapper(final Class<?> clazz) {
    return supportedWrapperToPrimitive(clazz).isPrimitive();
  }

  public static Class<?> supportedWrapperToPrimitive(final Class<?> clazz) {
    if (clazz == Integer.class) {
      return int.class;
    }

    if (clazz == Long.class) {
      return long.class;
    }

    if (clazz == Boolean.class) {
      return boolean.class;
    }

    if (clazz == Short.class) {
      return short.class;
    }

    return clazz;
  }
}
