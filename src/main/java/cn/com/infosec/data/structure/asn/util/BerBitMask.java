
package cn.com.infosec.data.structure.asn.util;

public final class BerBitMask {
  public static int MOST_SIGNIFICANT_BIT = 0b10000000;
  public static int NON_LEADING_BITS = 0b01111111;
  public static int CLASS_BITS = 0b11000000;
  public static int CONSTRUCTED_BIT = 0b00100000;
  public static int TAG_VALUE_BITS = 0b00011111;
}
