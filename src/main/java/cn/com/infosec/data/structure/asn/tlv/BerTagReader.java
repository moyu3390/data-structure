
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.util.BerBitMask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BerTagReader extends AbstractInputStreamReader {

  public byte[] read(final InputStream inputStream) {
    final int firstByte = readByte(inputStream);
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.write(firstByte);

    // if first byte has bits 5-1 set to 1
    // then it is a multibyte value
    if (((byte) firstByte & BerBitMask.TAG_VALUE_BITS) == BerBitMask.TAG_VALUE_BITS) {

      int valueByte;
      do {
        valueByte = readByte(inputStream);
        out.write(valueByte);
      } while (((byte) valueByte & BerBitMask.MOST_SIGNIFICANT_BIT) == BerBitMask.MOST_SIGNIFICANT_BIT);
    }

    return out.toByteArray();
  }
}
