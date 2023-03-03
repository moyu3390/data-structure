
package cn.com.infosec.data.structure.asn.tlv;

import java.io.InputStream;

public class BerValueReader extends AbstractInputStreamReader {
  private static final byte[] EMPTY = new byte[0];

  public byte[] read(final InputStream inputStream, final int bytesToRead) {
    if (bytesToRead == 0) {
      return EMPTY;
    }

    return readBytes(inputStream, bytesToRead);
  }
}
