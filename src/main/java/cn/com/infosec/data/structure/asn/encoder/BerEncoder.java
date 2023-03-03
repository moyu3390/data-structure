/**
 * BerEncoder
 * <p>
 * 1.0
 * <p>
 * 2022/12/15 9:57
 */

package cn.com.infosec.data.structure.asn.encoder;

import cn.com.infosec.data.structure.asn.AsnClassDescription;
import cn.com.infosec.data.structure.asn.annotation.AsnStructure;
import cn.com.infosec.data.structure.asn.annotation.AsnTag;
import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.exception.AsnConfigurationException;
import cn.com.infosec.data.structure.asn.exception.AsnEncodeException;
import cn.com.infosec.data.structure.asn.field.CollectionTaggedField;
import cn.com.infosec.data.structure.asn.field.PrimitiveTaggedField;
import cn.com.infosec.data.structure.asn.field.TaggedField;
import cn.com.infosec.data.structure.asn.field.accessor.DirectFieldAccessor;
import cn.com.infosec.data.structure.asn.field.accessor.FieldAccessor;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.util.BerUtils;
import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.v160.util.encoders.Hex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BerEncoder implements AsnEncoder<byte[]> {
    private final FieldAccessor fieldAccessor;
    private final Map<Class<?>, AsnClassDescription> classDescriptionCache;
    private final Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache;
    Gson gson = new GsonBuilder().create();

    public BerEncoder() {
        this(new DirectFieldAccessor());
    }

    public BerEncoder(final FieldAccessor fieldAccessor) {
        this(fieldAccessor, new HashMap<>(), new HashMap<>());
    }

    public BerEncoder(FieldAccessor fieldAccessor, Map<Class<?>, AsnClassDescription> classDescriptionCache, Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache) {
        this.fieldAccessor = fieldAccessor;
        this.classDescriptionCache = classDescriptionCache;
        this.converterCache = converterCache;
    }

    @Override
    public byte[] encode(final Object object) {
        if (object instanceof StructureJsonData) {
            return encode((StructureJsonData) object);
        }

        final AsnStructure clazzDeclaredAnnotation = object.getClass().getDeclaredAnnotation(AsnStructure.class);

        if (clazzDeclaredAnnotation == null) {
            throw new AsnEncodeException("Missing class AsnStructure annotation");
        }

        final AsnTag tag = clazzDeclaredAnnotation.value();
        final Tag fieldStructureTag = new Tag(tag.value(), tag.type(), true);
        return encodeStructure(object, fieldStructureTag);
    }


    @Override
    public byte[] encode(String jsonString) {
        if (Objects.isNull(jsonString) || jsonString.trim().length() == 0) {
            throw new AsnEncodeException("Json String is empty");
        }
        try {
            StructureJsonData structureJsonData = gson.fromJson(jsonString, StructureJsonData.class);
            return encode(structureJsonData);
        } catch (JsonSyntaxException e) {
            throw new AsnEncodeException("Json String convert StructureJsonData failed", e);
        }
    }


    @Override
    public byte[] encode(StructureJsonData structureJsonData) {
        return encodeStructure(structureJsonData);
    }

    private byte[] encodeStructure(final StructureJsonData structureJsonData) {
        final BerStructureBuilder berStructureBuilder;
        try {
            Tag structureTag = structureJsonData.getTag();
            // 解析tag,组装tag字节数据
            berStructureBuilder = new BerStructureBuilder(structureTag);
            boolean isConstructed = structureJsonData.getContainer();
            List<StructureJsonData> childList = structureJsonData.getChildren();
//            boolean hasChild = (!Objects.isNull(childList)&&!childList.isEmpty());
            // 解析value
            final byte[] encoded;
            if (isConstructed) {
                List<StructureJsonData> children = structureJsonData.getChildren();
                encoded = encodeStructure(children);
            } else {
                String hexValue = structureJsonData.getHexValue();
                byte[] valueData = new byte[0];
                if (!Objects.isNull(hexValue) && hexValue.trim().length() > 0) {
                    valueData = Hex.decode(hexValue);
                }
                encoded = valueData;
            }
            if (!Objects.isNull(encoded)) {
                berStructureBuilder.addValue(encoded);
            }
        } catch (final Exception e) {
            throw new AsnEncodeException(String.format("Cannot encode '%s'", structureJsonData), e);
        }
        return berStructureBuilder.build();
    }

    private byte[] encodeStructure(final List<StructureJsonData> children) {
        BerStructureBuilder berStructureBuilder = new BerStructureBuilder();
        try {
            if (Objects.isNull(children) || children.isEmpty()) {
                return null;
            }
            for (StructureJsonData structureJsonData : children) {
                Tag structureTag = structureJsonData.getTag();
                BerStructureBuilder builder = new BerStructureBuilder();
                // 解析tag,组装tag字节数据
                builder.addTag(structureTag);
                // 解析length，组装length字节数据
//                byte[] lengthData = BerUtils.encodeLength(structureJsonData.getLength());
//                berStructureBuilder.addValue(lengthData);
                boolean isConstructed = structureJsonData.getContainer();
                byte[] encodes;
                if (isConstructed) {
                    encodes = encodeStructure(structureJsonData.getChildren());
                } else {
                    String hexValue = structureJsonData.getHexValue();
                    byte[] valueData = new byte[0];
                    if (!Objects.isNull(hexValue) && hexValue.trim().length() > 0) {
                        valueData = Hex.decode(hexValue);
                    }
                    encodes = valueData;
                }
                if (!Objects.isNull(encodes)) {
                    builder.addValue(encodes);
                }
                byte[] bytes = builder.build();
                berStructureBuilder.addValue(bytes);
            }
        } catch (final Exception e) {
            throw new AsnEncodeException(String.format("Cannot encode '%s'", children), e);
        }
        return berStructureBuilder.buildData();
    }




    private byte[] encodeStructure(final Object object, final Tag structureTag) {
        final BerStructureBuilder berStructureBuilder;
        try {
            final Class<?> clazz = object.getClass();
            final AsnClassDescription asnClassDescription = classDescriptionCache.computeIfAbsent(clazz, AsnClassDescription::new);

            berStructureBuilder = new BerStructureBuilder(structureTag);

            for (final TaggedField taggedField : asnClassDescription.getClassDeclaredOrderedTaggedFields()) {
                final byte[] encoded;
                if (taggedField.isPrimitive()) {
                    encoded = encodePrimitive(object, (PrimitiveTaggedField) taggedField);

                    if (encoded == null) {
                        continue;
                    }
                } else if (taggedField.isStructure()) {
                    final Object fieldValue = fieldAccessor.getFieldValue(object, taggedField.getField());

                    if (fieldValue == null) {
                        continue;
                    }
                    encoded = encodeStructure(fieldValue, taggedField.getTag());
                } else if (taggedField.isCollection()) {
                    final CollectionTaggedField collectionTaggedField = (CollectionTaggedField) taggedField;

                    final Collection<Object> collection = fieldAccessor.getFieldValue(object, taggedField.getField());

                    if (collection == null) {
                        continue;
                    }

                    final BerStructureBuilder collectionBuilder = new BerStructureBuilder(collectionTaggedField.getTag());

                    if (collectionTaggedField.isStructured()) {
                        collection.forEach(e -> collectionBuilder.addValue(encodeStructure(e, collectionTaggedField.getElementTag())));
                    } else {
                        collection.forEach(e -> {
                            //noinspection unchecked
                            final AsnConverter<byte[], Object> asnConverter = loadAsnConverterFromCache((Class<? extends AsnConverter<byte[], Object>>) collectionTaggedField.getConverter());
                            collectionBuilder.addValue(encodePrimitive(collectionTaggedField.getElementTag(), asnConverter.encode(e)));
                        });
                    }

                    encoded = collectionBuilder.build();
                } else {
                    continue;
                }

                berStructureBuilder.addValue(encoded);
            }
        } catch (final Exception e) {
            throw new AsnEncodeException(String.format("Cannot encode '%s'", object), e);
        }

        return berStructureBuilder.build();
    }

    private byte[] encodePrimitive(final Object object, final PrimitiveTaggedField taggedField) {
        try {
            //noinspection unchecked
            final AsnConverter<byte[], Object> asnConverter = loadAsnConverterFromCache((Class<? extends AsnConverter<byte[], Object>>) taggedField.getConverter());
            final byte[] encodedFieldValue = asnConverter.encode(fieldAccessor.getFieldValue(object, taggedField.getField()));

            if (encodedFieldValue == null) {
                return null;
            }

            return encodePrimitive(taggedField.getTag(), encodedFieldValue);
        } catch (final Exception e) {
            throw new AsnEncodeException(String.format("Cannot encode '%s' from '%s'", taggedField, object), e);
        }
    }

    private byte[] encodePrimitive(final Tag tag, final byte[] value) {
        try {
            final ByteArrayOutputStream result = new ByteArrayOutputStream();

            result.write(BerUtils.convert(tag));
            result.write(BerUtils.encodeLength(value.length));
            result.write(value);

            return result.toByteArray();
        } catch (final IOException e) {
            throw new UncheckedIOException(String.format("Cannot encode tag %s and value '%s' to a primitive", tag, HexUtils.encode(value)), e);
        }
    }

    private AsnConverter<byte[], Object> loadAsnConverterFromCache(final Class<? extends AsnConverter<byte[], Object>> asnConverterClass) {
        return converterCache.computeIfAbsent(asnConverterClass, c -> {
            try {
                return c.getDeclaredConstructor().newInstance();
            } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException |
                           InvocationTargetException e) {
                throw new AsnConfigurationException(String.format("Cannot create a new instance of converter %s", c), e);
            }
        });
    }
}
