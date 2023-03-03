
package cn.com.infosec.data.structure.asn.converter;

import cn.com.infosec.data.structure.asn.exception.AsnConvertException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

import java.math.BigInteger;

public class ShortConverter implements AsnConverter<byte[], Short> {

  @Override
  public Short decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    try {
      return new BigInteger(data).shortValueExact();
    } catch (final ArithmeticException | NumberFormatException e) {
      throw new AsnConvertException(String.format("Cannot convert %s to short", HexUtils.encode(data)), e);
    }
  }

  @Override
  public byte[] encode(final Short data) {
    if (data == null) {
      return null;
    }

    return BigInteger.valueOf(data).toByteArray();
  }
}