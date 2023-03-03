
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.util.BerBitMask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class BerLengthReader extends AbstractInputStreamReader {

  public byte[] read(final InputStream inputStream) {
    final int firstByte = readByte(inputStream);
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.write(firstByte);

    // if first byte has MSB set to 1
    // then bits 7-1 describe number of octets that represent length
    if (((byte) firstByte & BerBitMask.MOST_SIGNIFICANT_BIT) == BerBitMask.MOST_SIGNIFICANT_BIT) {
      final int lengthOctetsRequired = (byte) firstByte & BerBitMask.NON_LEADING_BITS;
      final byte[] lengthOctets = readBytes(inputStream, lengthOctetsRequired);

      try {
        out.write(lengthOctets);
      } catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    return out.toByteArray();
  }
}
