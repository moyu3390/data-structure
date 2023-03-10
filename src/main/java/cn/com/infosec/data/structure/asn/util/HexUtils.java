
package cn.com.infosec.data.structure.asn.util;

import cn.com.infosec.data.structure.asn.exception.AsnParseException;

public final class HexUtils {

  private static final char[] DIGITS = "0123456789ABCDEF".toCharArray();

  public static String encode(final byte[] data) {
    final int l = data.length;
    final char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
      out[j++] = DIGITS[0x0F & data[i]];
    }
    return new String(out);
  }

  public static byte[] decode(final String hex) {
    final char[] data = hex.toCharArray();
    final int len = data.length;

    if ((len & 0x01) != 0) {
      throw new AsnParseException("Odd number of characters.");
    }

    final byte[] out = new byte[len >> 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++) {
      int f = toDigit(data[j], j) << 4;
      j++;
      f = f | toDigit(data[j], j);
      j++;
      out[i] = (byte) (f & 0xFF);
    }

    return out;
  }

  private static int toDigit(final char ch, final int index) {
    final int digit = Character.digit(ch, 16);
    if (digit == -1) {
      throw new AsnParseException("Illegal hexadecimal character " + ch + " at index " + index);
    }
    return digit;
  }
}
