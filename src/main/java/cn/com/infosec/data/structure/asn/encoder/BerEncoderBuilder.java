package cn.com.infosec.data.structure.asn.encoder;

import cn.com.infosec.data.structure.asn.AsnClassDescription;
import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.field.accessor.DirectFieldAccessor;
import cn.com.infosec.data.structure.asn.field.accessor.FieldAccessor;

import java.util.HashMap;
import java.util.Map;

public class BerEncoderBuilder {

  private FieldAccessor fieldAccessor = new DirectFieldAccessor();
  private Map<Class<?>, AsnClassDescription> classDescriptionCache = new HashMap<>();
  private Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache = new HashMap<>();

  public BerEncoderBuilder fieldAccessor(final FieldAccessor fieldAccessor) {
    this.fieldAccessor = fieldAccessor;
    return this;
  }

  public BerEncoderBuilder classDescriptionCache(final Map<Class<?>, AsnClassDescription> classDescriptionCache) {
    this.classDescriptionCache = classDescriptionCache;
    return this;
  }

  public BerEncoderBuilder converterCache(final Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache) {
    this.converterCache = converterCache;
    return this;
  }

  public BerEncoder build() {
    return new BerEncoder(fieldAccessor, classDescriptionCache, converterCache);
  }
}