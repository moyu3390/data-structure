
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.util.BerUtils;

import java.io.InputStream;

public class BerDataReader implements TlvDataReader {
  private final BerTagReader tagReader;
  private final BerLengthReader lengthReader;
  private final BerValueReader valueReader;

  public BerDataReader() {
    this.tagReader = new BerTagReader();
    this.lengthReader = new BerLengthReader();
    this.valueReader = new BerValueReader();
  }

  @Override
  public BerData readNext(final InputStream inputStream) {
    final byte[] tag = tagReader.read(inputStream);
    final byte[] length = lengthReader.read(inputStream);
    final byte[] value = valueReader.read(inputStream, BerUtils.parseLength(length));
    return new BerData(tag, length, value);
  }
}
