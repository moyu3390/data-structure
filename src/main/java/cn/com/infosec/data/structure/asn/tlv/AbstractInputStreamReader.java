
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.exception.AsnReadException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInputStreamReader {

  protected int readByte(final InputStream inputStream) {
    try {
      final int bite = inputStream.read();
      checkClosure(bite);
      return bite;
    } catch (final IOException e) {
      throw new AsnReadException(e);
    }
  }

  protected void checkClosure(final int bite) throws IOException {
    if (bite < 0) {
      throw new IOException("Socket closed during message assembly");
    }
  }

  protected byte[] readBytes(final InputStream inputStream, final int bytesToRead) {
    final byte[] result = new byte[bytesToRead];
    final DataInputStream dis = new DataInputStream(inputStream);
    try {
      dis.readFully(result);
    } catch (final IOException e) {
      throw new AsnReadException(e);
    }

    return result;
  }
}
