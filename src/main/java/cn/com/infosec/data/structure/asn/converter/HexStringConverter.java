
package cn.com.infosec.data.structure.asn.converter;

import cn.com.infosec.data.structure.asn.exception.AsnConvertException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

public class HexStringConverter implements AsnConverter<byte[], String> {

  @Override
  public String decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    return HexUtils.encode(data);
  }

  @Override
  public byte[] encode(final String data) {
    if (data == null) {
      return null;
    }

    try {
      return HexUtils.decode(data);
    } catch (final RuntimeException e) {
      throw new AsnConvertException(String.format("Cannot encode %s as hex", data), e);
    }
  }
}
