
package cn.com.infosec.data.structure.asn.converter;

import cn.com.infosec.data.structure.asn.exception.AsnConvertException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

import java.math.BigInteger;

public class LongConverter implements AsnConverter<byte[], Long> {

  @Override
  public Long decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    try {
      return new BigInteger(data).longValueExact();
    } catch (final ArithmeticException | NumberFormatException e) {
      throw new AsnConvertException(String.format("Cannot convert %s to long", HexUtils.encode(data)), e);
    }
  }

  @Override
  public byte[] encode(final Long data) {
    if (data == null) {
      return null;
    }

    return BigInteger.valueOf(data).toByteArray();
  }
}