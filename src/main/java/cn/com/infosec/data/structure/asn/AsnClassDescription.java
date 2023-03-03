
package cn.com.infosec.data.structure.asn;

import cn.com.infosec.data.structure.asn.annotation.*;
import cn.com.infosec.data.structure.asn.converter.AsnConverter;
import cn.com.infosec.data.structure.asn.converter.AutoConverter;
import cn.com.infosec.data.structure.asn.field.CollectionTaggedField;
import cn.com.infosec.data.structure.asn.field.PrimitiveTaggedField;
import cn.com.infosec.data.structure.asn.field.StructureTaggedField;
import cn.com.infosec.data.structure.asn.field.TaggedField;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The cached representation of a class. All annotated fields of a class will be analyzed and stored for further processing.
 */
public class AsnClassDescription {
  private Map<Tag, List<TaggedField>> multimap;
  private List<TaggedField> classOrderedTaggedFields;
  private Map<Tag, Class<?>> polymorphics;

  public AsnClassDescription(final Class<?> clazz) {
    init(clazz);
  }

  // ensures that the order of class defined fields will be kept when encoding
  public List<TaggedField> getClassDeclaredOrderedTaggedFields() {
    if (classOrderedTaggedFields == null) {
      classOrderedTaggedFields = multimap.values()
          .stream()
          .flatMap(Collection::stream)
          .sorted()
          .collect(Collectors.toList());
    }

    return classOrderedTaggedFields;
  }

  public TaggedField findByTag(final Tag tag, final int index) {
    final List<TaggedField> taggedFields = multimap.get(tag);
    if (taggedFields == null || taggedFields.isEmpty()) {
      return null;
    }

    if (index >= taggedFields.size()) {
      // this means that we need less data with this tag than there is available
      // if we need to get the second data, we also need to define the first with proper ordering
      // if we wanted to get the 1st and 3rd, we would also need to define the 2nd to order them correctly

      // example is available in asn-ber-parser BerDecoderTest where we define MultipleAddressStringWrapper with 2 fields with the same tag
      // but 3 are available and the 3rd is discarded
      return null;
    }

    return taggedFields.get(index);
  }

  public Class<?> findImplementationByTag(final Tag tag) {
    if (polymorphics == null) {
      return null;
    }

    return polymorphics.get(tag);
  }

  private void init(final Class<?> clazz) {
    multimap = new HashMap<>();

    if (clazz.isInterface()) {
      polymorphics = new HashMap<>();
      for (final AsnPolymorphic polymorphic : clazz.getDeclaredAnnotationsByType(AsnPolymorphic.class)) {
        polymorphics.put(tag(polymorphic.value(), polymorphic.type(), ClassUtils.isPrimitiveOrWrapper(polymorphic.type())), polymorphic.type());
      }
    }

    int fieldPosition = 0;
    for (final Field field : clazz.getDeclaredFields()) {
      TaggedField taggedField = null;

      if (field.isAnnotationPresent(AsnPrimitive.class)) {
        taggedField = primitiveField(fieldPosition, field);
      } else if (field.isAnnotationPresent(AsnStructure.class)) {
        taggedField = structureField(fieldPosition, field);
      } else if (field.isAnnotationPresent(AsnCollection.class)) {
        taggedField = collectionField(fieldPosition, field);
      }

      if (taggedField != null) {
        multimap.computeIfAbsent(taggedField.getTag(), k -> new ArrayList<>()).add(taggedField);
      }

      fieldPosition++;
    }
  }

  private TaggedField primitiveField(final int fieldPosition, final Field field) {
    final AsnPrimitive primitiveTag = field.getAnnotation(AsnPrimitive.class);
    final AsnTag asnTag = primitiveTag.value();
    final  Class<? extends AsnConverter<?, ?>> converter = converter(primitiveTag.asnConverter(), field.getType());
    return new PrimitiveTaggedField(fieldPosition, tag(asnTag, field.getType(), false), field, converter);
  }

  private TaggedField structureField(final int fieldPosition, final Field field) {
    final AsnStructure structureTag = field.getAnnotation(AsnStructure.class);
    final AsnTag asnTag = structureTag.value();
    return new StructureTaggedField(fieldPosition, tag(asnTag, field.getType(), true), field);
  }

  private TaggedField collectionField(final int fieldPosition, final Field field) {
    final AsnCollection collectionTag = field.getAnnotation(AsnCollection.class);
    final Tag tag = tag(collectionTag.value(), field.getType(), true);
    final Tag elementTag = tag(collectionTag.elementTag(), collectionTag.type(), collectionTag.structured());
    final Class<? extends AsnConverter<?, ?>> converter = collectionTag.structured() ? null : converter(collectionTag.asnConverter(), collectionTag.type());
    return new CollectionTaggedField(fieldPosition, tag, field, collectionTag.structured(), collectionTag.type(), elementTag, converter);
  }

  private Tag tag(final AsnTag asnTag, final Class<?> clazz, final boolean structured) {
    if (asnTag.value() == -1) {
      return BerAutoResolver.getUniversalTag(clazz, structured);
    }

    return new Tag(asnTag.value(), asnTag.type(), structured);
  }

  private Class<? extends AsnConverter<?, ?>> converter(final Class<? extends AsnConverter<?, ?>> converter, final Class<?> clazz) {
    if (converter.equals(AutoConverter.class)) {
      return BerAutoResolver.getUniversalConverterClass(clazz);
    }

    return converter;
  }
}