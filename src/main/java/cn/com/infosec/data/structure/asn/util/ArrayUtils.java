
package cn.com.infosec.data.structure.asn.util;

public final class ArrayUtils {

  public static void reverse(final byte[] array) {
    if (array == null) {
      return;
    }

    int i = 0;
    int j = array.length - 1;
    byte tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }
}
