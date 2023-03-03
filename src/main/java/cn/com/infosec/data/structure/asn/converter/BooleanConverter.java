
package cn.com.infosec.data.structure.asn.converter;

import cn.com.infosec.data.structure.asn.exception.AsnConvertException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

public class BooleanConverter implements AsnConverter<byte[], Boolean> {

  @Override
  public Boolean decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    if (data.length != 1) {
      throw new AsnConvertException("Data has multiple bytes: " + HexUtils.encode(data));
    }

    if (data[0] == 0) {
      return false;
    }

    if (data[0] == -1) {
      return true;
    }

    throw new AsnConvertException(String.format("%s doesn't represent boolean", HexUtils.encode(data)));
  }

  @Override
  public byte[] encode(final Boolean data) {
    if (data == null) {
      return null;
    }

    return new byte[]{(byte) (data ? -1 : 0)};
  }
}