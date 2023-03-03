
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.exception.AsnDecodeException;
import cn.com.infosec.data.structure.asn.util.HexUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BerData {
  private final byte[] tag;
  private final byte[] length;
  private final byte[] value;

  public BerData(final byte[] tag, final byte[] length, final byte[] value) {
    this.tag = tag;
    this.length = length;
    this.value = value;
  }

  public byte[] toTlv() {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      out.write(tag);
      out.write(length);
      out.write(value);
    } catch (final IOException e) {
      throw new AsnDecodeException(String.format("Cannot convert %s to byte[]", this), e);
    }

    return out.toByteArray();
  }

  @Override
  public String toString() {
    return String.format("BerData[tag=%s, length=%s, value=%s]",
        HexUtils.encode(tag),
        HexUtils.encode(length),
        HexUtils.encode(value)
    );
  }

  public byte[] getTag() {
    return tag;
  }

  public byte[] getLength() {
    return length;
  }

  public byte[] getValue() {
    return value;
  }
}
