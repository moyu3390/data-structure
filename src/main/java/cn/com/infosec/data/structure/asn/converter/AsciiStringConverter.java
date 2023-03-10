
package cn.com.infosec.data.structure.asn.converter;

import java.nio.charset.StandardCharsets;

public class AsciiStringConverter implements AsnConverter<byte[], String> {

  @Override
  public String decode(final byte[] data) {
    if (data == null) {
      return null;
    }

    return new String(data, StandardCharsets.US_ASCII);
  }

  @Override
  public byte[] encode(final String data) {
    if (data == null) {
      return null;
    }

    return data.getBytes(StandardCharsets.US_ASCII);
  }
}