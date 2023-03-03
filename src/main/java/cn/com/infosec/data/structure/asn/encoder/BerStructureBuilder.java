
package cn.com.infosec.data.structure.asn.encoder;

import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.util.BerUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class BerStructureBuilder {
    private final ByteArrayOutputStream result = new ByteArrayOutputStream();
    private final List<byte[]> values = new ArrayList<>();

    public BerStructureBuilder() {

    }

    public void addTag(final Tag tag) {
        try {
            result.write(BerUtils.convert(tag));
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BerStructureBuilder(final Tag tag) {
        addTag(tag);
    }

    public void addValue(final byte[] value) {
        values.add(value);
    }

    public byte[] build() {
        try {
            result.write(BerUtils.encodeLength(values.stream().mapToInt(b -> b.length).sum()));
            for (final byte[] value : values) {
                result.write(value);
            }
            return result.toByteArray();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public byte[] buildData() {
        try {
            for (final byte[] value : values) {
                result.write(value);
            }
            return result.toByteArray();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
