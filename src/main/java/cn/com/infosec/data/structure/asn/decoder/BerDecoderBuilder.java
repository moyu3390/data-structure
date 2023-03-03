
package cn.com.infosec.data.structure.asn.decoder;

import cn.com.infosec.data.structure.asn.AsnClassDescription;
import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.field.accessor.DirectFieldAccessor;
import cn.com.infosec.data.structure.asn.field.accessor.FieldAccessor;
import cn.com.infosec.data.structure.asn.tlv.BerDataReader;
import cn.com.infosec.data.structure.asn.tlv.TlvDataReader;

import java.util.HashMap;
import java.util.Map;

public class BerDecoderBuilder {

  private FieldAccessor fieldAccessor = new DirectFieldAccessor();
  private TlvDataReader tlvDataReader = new BerDataReader();
  private Map<Class<?>, AsnClassDescription> classDescriptionCache = new HashMap<>();
  private Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache = new HashMap<>();

  public BerDecoderBuilder fieldAccessor(final FieldAccessor fieldAccessor) {
    this.fieldAccessor = fieldAccessor;
    return this;
  }

  public BerDecoderBuilder tlvDataReader(final TlvDataReader tlvDataReader) {
    this.tlvDataReader = tlvDataReader;
    return this;
  }

  public BerDecoderBuilder classDescriptionCache(final Map<Class<?>, AsnClassDescription> classDescriptionCache) {
    this.classDescriptionCache = classDescriptionCache;
    return this;
  }

  public BerDecoderBuilder converterCache(final Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache) {
    this.converterCache = converterCache;
    return this;
  }

  public BerDecoder build() {
    return new BerDecoder(fieldAccessor, tlvDataReader, classDescriptionCache, converterCache);
  }
}
