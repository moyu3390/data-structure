/**
 * BerDecoder
 * <p>
 * 1.0
 * <p>
 * 2022/12/15 9:57
 */

package cn.com.infosec.data.structure.asn.decoder;

import cn.com.infosec.data.structure.asn.AsnClassDescription;
import cn.com.infosec.data.structure.asn.annotation.AsnPostProcessMethod;
import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.converter.HexStringConverter;
import cn.com.infosec.data.structure.asn.exception.AsnConfigurationException;
import cn.com.infosec.data.structure.asn.exception.AsnDecodeException;
import cn.com.infosec.data.structure.asn.exception.AsnException;
import cn.com.infosec.data.structure.asn.field.CollectionTaggedField;
import cn.com.infosec.data.structure.asn.field.PrimitiveTaggedField;
import cn.com.infosec.data.structure.asn.field.TaggedField;
import cn.com.infosec.data.structure.asn.field.accessor.DirectFieldAccessor;
import cn.com.infosec.data.structure.asn.field.accessor.FieldAccessor;
import cn.com.infosec.data.structure.asn.support.Counter;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.UniversalTags;
import cn.com.infosec.data.structure.asn.tlv.BerData;
import cn.com.infosec.data.structure.asn.tlv.BerDataReader;
import cn.com.infosec.data.structure.asn.tlv.TlvDataReader;
import cn.com.infosec.data.structure.asn.util.BerUtils;
import cn.com.infosec.data.structure.asn.util.ClassUtils;
import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.data.structure.asn.util.VisualValueUtils;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.utils.NodePathUtil;
import cn.com.infosec.data.structure.validator.ValidatorExecutor;
import cn.com.infosec.data.structure.validator.ValidatorType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BerDecoder implements AsnDecoder<byte[]> {
    private final FieldAccessor fieldAccessor;
    private final TlvDataReader tlvDataReader;
    private final Map<Class<?>, AsnClassDescription> classDescriptionCache;
    private final Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache;

    public BerDecoder() {
        this(new BerDataReader());
    }

    public BerDecoder(final TlvDataReader tlvDataReader) {
        this(tlvDataReader, new DirectFieldAccessor());
    }

    public BerDecoder(final TlvDataReader tlvDataReader, final FieldAccessor fieldAccessor) {
        this(fieldAccessor, tlvDataReader, new HashMap<>(), new HashMap<>());
    }

    public BerDecoder(FieldAccessor fieldAccessor, TlvDataReader tlvDataReader, Map<Class<?>, AsnClassDescription> classDescriptionCache, Map<Class<? extends AsnConverter<byte[], Object>>, AsnConverter<byte[], Object>> converterCache) {
        this.fieldAccessor = fieldAccessor;
        this.tlvDataReader = tlvDataReader;
        this.classDescriptionCache = classDescriptionCache;
        this.converterCache = converterCache;
    }

    @Override
    public <X> X decode(final Class<X> clazz, final byte[] data) {
        if (data == null) {
            throw new AsnDecodeException("Cannot decode null data into: " + clazz.getSimpleName());
        }

        return decodeStructure(clazz, data);
    }


    @Override
    public StructureJsonData decode(byte[] input, boolean onlyStructure) {
        return decode(input, "", onlyStructure);
    }


    @Override
    public StructureJsonData decode(byte[] input, String currentNodePath, boolean onlyStructure) {
        if (Objects.isNull(input)) {
            throw new AsnDecodeException("Cannot decode null data into: " + input);
        }
        return decodeStructure(input, NodePathUtil.getParentId(currentNodePath), NodePathUtil.getChildNum(currentNodePath), onlyStructure);
    }


    @Override
    public StructureJsonData decode(byte[] input, String currentNodeParentPath, String currentNodeNum, boolean onlyStructure) {
        if (Objects.isNull(input)) {
            throw new AsnDecodeException("Cannot decode null data into: " + input);
        }
        // 验证数据结构
        ValidatorExecutor.execut(ValidatorType.DER, input, null);
        return decodeStructure(input, NodePathUtil.path2Id(currentNodeParentPath), currentNodeNum, onlyStructure);
    }


    @Override
    public String decodeToJson(byte[] input, boolean onlyStructure) {
        StructureJsonData decode = decode(input, onlyStructure);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(decode);
        return json;
    }


    @Override
    public String decodeToJson(byte[] input, String currentNodePath, boolean onlyStructure) {
        if (Objects.isNull(input)) {
            throw new AsnDecodeException("Cannot decode null data into: " + input);
        }

        StructureJsonData decode = decode(input, currentNodePath, onlyStructure);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(decode);
        return json;
    }


    @Override
    public String decodeToJson(byte[] input, String currentNodeParentPath, String currentNodeNum, boolean onlyStructure) {
        StructureJsonData decode = decode(input, currentNodeParentPath, currentNodeNum, onlyStructure);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(decode);
        return json;
    }

    private StructureJsonData decodeStructure(final byte[] data, String parentId, String childNodeNum, boolean onlyStructure) {
        try {
            final BerData tlvData = tlvDataReader.readNext(new ByteArrayInputStream(data));

            StructureJsonData structureJsonData = new StructureJsonData();
            final Tag parsedFieldTag = BerUtils.parseTag(tlvData.getTag());
            fillBeanAttributes(tlvData, structureJsonData, parsedFieldTag, parentId, childNodeNum);
            if (!parsedFieldTag.isConstructed()) {
                decodePrimitiveField(structureJsonData, tlvData, onlyStructure);
                return structureJsonData;
            }

            final ByteArrayInputStream valueStream = new ByteArrayInputStream(tlvData.getValue());

            List<StructureJsonData> childs = new ArrayList<>();
            structureJsonData.setChildren(childs);
            int count = 0;
            while (valueStream.available() > 0) {
                //Read element by element
                final BerData fieldTlvData = tlvDataReader.readNext(valueStream);
                StructureJsonData newStructureJsonData = new StructureJsonData();
                final Tag dataTag = BerUtils.parseTag(fieldTlvData.getTag());
                fillBeanAttributes(fieldTlvData, newStructureJsonData, dataTag, structureJsonData.getId(), count + "");

                if (dataTag.isConstructed()) {
                    decodeCollectionField(newStructureJsonData, fieldTlvData, onlyStructure);
                } else {
                    decodePrimitiveField(newStructureJsonData, fieldTlvData, onlyStructure);
                }
                childs.add(newStructureJsonData);
                count++;

            }
            return structureJsonData;
        } catch (final Exception e) {
            throw new AsnDecodeException(String.format("Cannot decode '%s' into '%s' class", HexUtils.encode(data), StructureJsonData.class.getName()), e);
        }
    }

    private static void fillBeanAttributes(BerData tlvData, StructureJsonData structureJsonData, Tag parsedFieldTag, String parentId, String childNodeNum) {
        // 获取tag和length
        final Tag tlvTag = parsedFieldTag;
        // tagNum
        int t = Byte.toUnsignedInt(tlvTag.getEncoding()[0]);
        // length
        int l = BerUtils.parseLength(tlvData.getLength());
        // 是否为结构体
        boolean isContainer = tlvTag.isConstructed();
        // tagClass
        String tagClass = tlvTag.getTagType().name();
        // 根据tagNum 匹配tagName
        String key = UniversalTags.ASN1PKCS_TAGS.entrySet().stream().filter(m -> Objects.equals(m.getValue(), tlvTag.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
        String tagName = "CONTEXT";
        if (!Objects.isNull(key)) {
            tagName = key;
        }

        // todo 先默认设置tagName
        structureJsonData.setNodeAlias(tagName);
        structureJsonData.setTag(tlvTag);
        String nodeId = NodePathUtil.genNodeId(parentId, childNodeNum);
        structureJsonData.setpId(parentId);
        structureJsonData.setId(nodeId);

        String path = NodePathUtil.getPathByNodeId(nodeId);
        structureJsonData.setPath(path);
        structureJsonData.setContainer(isContainer);
        structureJsonData.setLength(l);
        structureJsonData.setTagClass(tagClass);
        structureJsonData.setTagName(tagName);
        structureJsonData.setTemplate(!tlvTag.isConstructed());
        structureJsonData.setTagNum(t);
    }


    private <X> X decodeStructure(final Class<X> clazz, final byte[] data) {
        try {
            final AsnClassDescription asnClassDescription = loadAsnClassDescription(clazz);
            final BerData tlvData = tlvDataReader.readNext(new ByteArrayInputStream(data));
            // 获取tag和length
            final Tag tlvTag = BerUtils.parseTag(tlvData.getTag());
            // tagNum
            int t = Byte.toUnsignedInt((byte) tlvTag.getValue());
            // length
            int l = BerUtils.parseLength(tlvData.getLength());
            // 是否为结构体
            boolean isContainer = tlvTag.isConstructed();
            // tagClass
            String tagClass = tlvTag.getTagType().name();
            // 根据tagNum 匹配tagName
            Optional<String> key = UniversalTags.ASN1PKCS_TAGS.entrySet().stream().filter(m -> Objects.equals(m.getValue(), t)).map(Map.Entry::getKey).findFirst();
            String tagName = "";
            if (!Objects.isNull(key)) {
                tagName = key.get();
            }


            if (clazz.isInterface()) {
                return decodePolymorphic(asnClassDescription, tlvData.getValue());
            }

            final ByteArrayInputStream valueStream = new ByteArrayInputStream(tlvData.getValue());
            final X instance = clazz.getDeclaredConstructor().newInstance();

            final Counter<Tag> tagCounter = new Counter<Tag>();
            while (valueStream.available() > 0) {
                //Read element by element
                final BerData fieldTlvData = tlvDataReader.readNext(valueStream);

                final TaggedField taggedField = parseTaggedField(asnClassDescription, tagCounter, fieldTlvData);
                if (taggedField == null) {
                    continue;
                }

                if (taggedField.isPrimitive()) {
                    decodePrimitiveField(instance, fieldTlvData, taggedField);
                } else if (taggedField.isStructure()) {
                    decodeStructureField(instance, fieldTlvData, taggedField);
                } else if (taggedField.isCollection()) {
                    decodeCollectionField(instance, fieldTlvData, taggedField);
                } else {
                    throw new AsnDecodeException("Unknown TaggedField type: " + taggedField);
                }
            }

            invokePostProcessMethod(clazz, instance);

            return instance;
        } catch (final Exception e) {
            throw new AsnDecodeException(String.format("Cannot decode '%s' into '%s' class", HexUtils.encode(data), clazz.getName()), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <X> X decodePolymorphic(final AsnClassDescription asnClassDescription, final byte[] data) {
        // read the next tlv which actually represents the nested choice implementation data
        final BerData implementationData = tlvDataReader.readNext(new ByteArrayInputStream(data));
        final Tag tag = BerUtils.parseTag(implementationData.getTag());
        Class<?> implementation = asnClassDescription.findImplementationByTag(tag);

        if (implementation == null) {
            return null;
        }

        if (ClassUtils.isPrimitiveOrWrapper(implementation)) {
            final AsnConverter<byte[], Object> converter = loadAsnConverterFromCache((Class<? extends AsnConverter<byte[], Object>>) implementation);
            return (X) converter.decode(implementationData.getValue());
        }

        return (X) decodeStructure(implementation, implementationData.toTlv());
    }

    private void decodeCollection(final List<StructureJsonData> collection, final byte[] elementData, String parentId, boolean onlyStructure) {
        try {
            final ByteArrayInputStream stream = new ByteArrayInputStream(elementData);
            int count = 0;
            while (stream.available() > 0) {
                final BerData elementBerData = tlvDataReader.readNext(stream);
                final Tag parsedElementTag = BerUtils.parseTag(elementBerData.getTag());

                if (parsedElementTag.isConstructed()) {
                    collection.add(decodeStructure(elementBerData.toTlv(), parentId, count + "", onlyStructure));
                } else {
                    //noinspection unchecked
                    final AsnConverter<byte[], String> asnConverter = new HexStringConverter();
                    StructureJsonData structureJsonData = new StructureJsonData();
                    fillBeanAttributes(elementBerData, structureJsonData, parsedElementTag, parentId, count + "");
                    String visualValue = VisualValueUtils.getVisualValue(structureJsonData.getTagName(), elementBerData);
                    if (Objects.isNull(visualValue)) {
                        visualValue = "";
                    }

                    if (visualValue.trim().length() > 0) {
                        String alias = visualValue.substring(0, visualValue.indexOf("("));
                        structureJsonData.setNodeAlias(alias);
                    }
                    if (onlyStructure) {
                        structureJsonData.setVisualValue("");
                        structureJsonData.setHexValue("");
                    } else {
                        structureJsonData.setVisualValue(visualValue);
                        byte[] value = elementBerData.getValue();
                        structureJsonData.setHexValue(asnConverter.decode(value));
                    }
                    collection.add(structureJsonData);
                }
                count++;
            }
        } catch (final Exception e) {
            throw new AsnDecodeException(String.format("Cannot decode collection data '%s' into '%s' class", HexUtils.encode(elementData), "taggedField.getType().getName()"), e);
        }
    }

    private void decodeCollection(final Collection<Object> collection, final byte[] elementData, final CollectionTaggedField taggedField) {
        try {
            final ByteArrayInputStream stream = new ByteArrayInputStream(elementData);
            while (stream.available() > 0) {
                final BerData elementBerData = tlvDataReader.readNext(stream);
                final Tag parsedElementTag = BerUtils.parseTag(elementBerData.getTag());

                if (taggedField.getType().isInterface()) {
                    final AsnClassDescription asnClassDescription = loadAsnClassDescription(taggedField.getType());
                    final Object decodedType = decodePolymorphic(asnClassDescription, elementBerData.toTlv());
                    if (decodedType != null) {
                        collection.add(decodedType);
                    }
                } else if (taggedField.getElementTag().equals(parsedElementTag)) {
                    if (taggedField.isStructured()) {
                        collection.add(decodeStructure(taggedField.getType(), elementBerData.toTlv()));
                    } else {
                        //noinspection unchecked
                        final AsnConverter<byte[], Object> asnConverter = loadAsnConverterFromCache((Class<? extends AsnConverter<byte[], Object>>) taggedField.getConverter());
                        collection.add(asnConverter.decode(elementBerData.getValue()));
                    }
                }
            }
        } catch (final Exception e) {
            throw new AsnDecodeException(String.format("Cannot decode collection data '%s' into '%s' class", HexUtils.encode(elementData), taggedField.getType().getName()), e);
        }
    }

    private <X> void decodePrimitiveField(final X instance, final BerData fieldTlvData, final TaggedField taggedField) {
        //noinspection unchecked
        final AsnConverter<byte[], Object> asnConverter = loadAsnConverterFromCache((Class<? extends AsnConverter<byte[], Object>>) ((PrimitiveTaggedField) taggedField).getConverter());

        try {
            fieldAccessor.setFieldValue(instance, taggedField.getField(), asnConverter.decode(fieldTlvData.getValue()));
        } catch (final AsnException e) {
            throw new AsnDecodeException(String.format("Cannot set value '%s' into field '%s'", HexUtils.encode(fieldTlvData.getValue()), taggedField.getField().getName()), e);
        }
    }

    private void decodePrimitiveField(final StructureJsonData instance, final BerData fieldTlvData, boolean onlyStructure) {
        //noinspection unchecked
        final AsnConverter<byte[], String> asnConverter = new HexStringConverter();

        try {
            String visualValue = VisualValueUtils.getVisualValue(instance.getTagName(), fieldTlvData);
            if (Objects.isNull(visualValue)) {
                visualValue = "";
            }
            // todo 还需要加上识别现有的数据结构.有模板的数据，从模板数据中复制nodeAlias属性

            if (visualValue.trim().length() > 0) {
                String alias = visualValue.substring(0, visualValue.indexOf("("));
                instance.setNodeAlias(alias);
            }
            if (onlyStructure) {
                instance.setVisualValue("");
                instance.setHexValue("");
                return;
            }


            instance.setVisualValue(visualValue);

            byte[] value = fieldTlvData.getValue();
            instance.setHexValue(asnConverter.decode(value));
        } catch (final AsnException e) {
            throw new AsnDecodeException(String.format("Cannot set value '%s' into field '%s'", HexUtils.encode(fieldTlvData.getValue()), "HexValue"), e);
        }
    }

    private <X> void decodeStructureField(final X instance, final BerData fieldTlvData, final TaggedField taggedField) {
        fieldAccessor.setFieldValue(instance, taggedField.getField(), decodeStructure(taggedField.getField().getType(), fieldTlvData.toTlv()));
    }

    private void decodeCollectionField(final StructureJsonData instance, final BerData fieldTlvData, boolean onlyStructure) {

        List<StructureJsonData> childs = new ArrayList<>();
        instance.setChildren(childs);
        String parentId = instance.getId();
        decodeCollection(childs, fieldTlvData.getValue(), parentId, onlyStructure);
    }

    private <X> void decodeCollectionField(final X instance, final BerData fieldTlvData, final TaggedField taggedField) {
        final Class<?> fieldClass = taggedField.getField().getType();

        final Collection<Object> collection;

        if (fieldClass.isAssignableFrom(List.class)) {
            collection = new ArrayList<>();
        } else if (fieldClass.isAssignableFrom(Set.class)) {
            collection = new HashSet<>();
        } else {
            throw new AsnDecodeException(String.format("Unsupported collection type: '%s'. Only List and Set supported!", fieldClass));
        }

        fieldAccessor.setFieldValue(instance, taggedField.getField(), collection);
        decodeCollection(collection, fieldTlvData.getValue(), (CollectionTaggedField) taggedField);
    }

    private TaggedField parseTaggedField(final AsnClassDescription asnClassDescription, final Counter<Tag> tagCounter, final BerData fieldTlvData) {
        final Tag parsedFieldTag = BerUtils.parseTag(fieldTlvData.getTag());
        final int index = tagCounter.count(parsedFieldTag);
        return asnClassDescription.findByTag(parsedFieldTag, index);
    }

    private AsnClassDescription loadAsnClassDescription(final Class<?> clazz) {
        return classDescriptionCache.computeIfAbsent(clazz, AsnClassDescription::new);
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

    private <X> void invokePostProcessMethod(final Class<X> clazz, final X instance) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final AsnPostProcessMethod asnPostProcessMethod = clazz.getDeclaredAnnotation(AsnPostProcessMethod.class);

        if (asnPostProcessMethod != null) {
            final Method declaredMethod = clazz.getDeclaredMethod(asnPostProcessMethod.value());
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            declaredMethod.invoke(instance);
        }
    }
}
