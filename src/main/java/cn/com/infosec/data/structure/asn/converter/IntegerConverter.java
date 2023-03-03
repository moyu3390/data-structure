
package cn.com.infosec.data.structure.asn.converter;

import cn.com.infosec.data.structure.asn.exception.AsnConvertException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

import java.math.BigInteger;

public class IntegerConverter implements AsnConverter<byte[], Integer> {

  @Override
  public Integer decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    try {
      return new BigInteger(data).intValueExact();
    } catch (final ArithmeticException | NumberFormatException e) {
      throw new AsnConvertException(String.format("Cannot convert %s to integer", HexUtils.encode(data)), e);
    }
  }

  @Override
  public byte[] encode(final Integer data) {
    if (data == null) {
      return null;
    }

    return BigInteger.valueOf(data).toByteArray();
  }
}