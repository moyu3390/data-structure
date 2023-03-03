
package cn.com.infosec.data.structure.asn.tlv;

import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.util.BerUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
public class BerDataExtractReader implements TlvDataReader {
  private final List<Tag> tags;
  private final BerTagReader tagReader;
  private final BerLengthReader lengthReader;
  private final BerValueReader valueReader;

  public BerDataExtractReader(final List<Tag> tags) {
    this.tags = tags;
    this.tagReader = new BerTagReader();
    this.lengthReader = new BerLengthReader();
    this.valueReader = new BerValueReader();
  }

  @Override
  public BerData readNext(final InputStream inputStream) {
    byte[] tag;
    byte[] length;
    byte[] value;

    int depth = 0;
    InputStream stream = inputStream;
    do {
      tag = tagReader.read(stream);
      length = lengthReader.read(stream);
      value = valueReader.read(stream, BerUtils.parseLength(length));

      final Tag parsedTag = BerUtils.parseTag(tag);

      if (parsedTag.equals(tags.get(depth))) {
        depth++;
        stream = new ByteArrayInputStream(value);
      }
    } while (depth < tags.size());

    return new BerData(tag, length, value);
  }
}
