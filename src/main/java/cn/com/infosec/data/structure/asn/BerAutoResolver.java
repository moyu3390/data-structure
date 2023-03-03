
package cn.com.infosec.data.structure.asn;

import cn.com.infosec.data.structure.asn.converter.*;
import cn.com.infosec.data.structure.asn.exception.AsnConfigurationException;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.TagType;
import cn.com.infosec.data.structure.asn.tag.UniversalTags;
import cn.com.infosec.data.structure.asn.util.ClassUtils;

import java.util.Collection;

public class BerAutoResolver {

    public static Class<? extends AsnConverter<?, ?>> getUniversalConverterClass(final Class<?> c) {
        if (c == null) {
            throw new AsnConfigurationException("Cannot get a converter for null class");
        }

        if (c == String.class) {
            // String has multiple tags that can represent it, just hardcode this one
            return Utf8StringConverter.class;
        }

        final Class<?> clazz = ClassUtils.supportedWrapperToPrimitive(c);
        if (clazz.isPrimitive() || clazz == byte[].class) {
            for (final Mappings mapping : Mappings.values()) {
                if (mapping.clazz != null && mapping.clazz.equals(clazz)) {
                    return mapping.converterClass;
                }
            }
        }

        throw new AsnConfigurationException("Cannot get a converter for: " + clazz.getName());
    }

    public static Tag getUniversalTag(final Class<?> c, final boolean constructed) {
        if (c == null) {
            return null;
        }

        if (c == String.class) {
            // String has multiple tags that can represent it, just hardcode this one
            return new Tag(Mappings.OCTET_STRING.value, TagType.UNIVERSAL, constructed);
        }

        Integer value = null;

        final Class<?> clazz = ClassUtils.supportedWrapperToPrimitive(c);
        if (clazz.isPrimitive() || clazz == byte[].class) {
            for (final Mappings mapping : Mappings.values()) {
                if (mapping.clazz != null && mapping.clazz.equals(clazz)) {
                    value = mapping.value;
                    break;
                }
            }
        }

        if (value == null) {
            value = Collection.class.isAssignableFrom(c) ? Mappings.SET.value : Mappings.SEQUENCE.value;
        }

        return new Tag(value, TagType.UNIVERSAL, constructed);
    }

    private enum Mappings {
        BOOLEAN(UniversalTags.BOOLEAN, boolean.class, BooleanConverter.class),
        INTEGER(UniversalTags.INTEGER, int.class, IntegerConverter.class),
        SHORT(UniversalTags.INTEGER, short.class, ShortConverter.class),
        LONG(UniversalTags.INTEGER, long.class, LongConverter.class),
        BIT_STRING(UniversalTags.BIT_STRING, null, null), // not configured
        OCTET_STRING(UniversalTags.OCTET_STRING, byte[].class, AutoConverter.class),
        ENUMERATED(UniversalTags.ENUMERATED, null, null), // not configured
        UTF8_STRING(UniversalTags.UTF8_STRING, String.class, Utf8StringConverter.class),
        SEQUENCE(UniversalTags.SEQUENCE, null, null), // not configured
        SET(UniversalTags.SET, null, null), // not configured
        NUMERIC_STRING(UniversalTags.NUMERIC_STRING, String.class, Utf8StringConverter.class),
        PRINTABLE_STRING(UniversalTags.PRINTABLE_STRING, String.class, Utf8StringConverter.class),
        TELETEX_STRING(UniversalTags.TELETEX_STRING, String.class, Utf8StringConverter.class),
        VIDEOTEX_STRING(UniversalTags.VIDEOTEX_STRING, String.class, Utf8StringConverter.class),
        IA5_STRING(UniversalTags.IA5_STRING, String.class, AsciiStringConverter.class),
        UTC_TIME(UniversalTags.UTC_TIME, null, null), // not configured
        GENERALIZED_TIME(UniversalTags.GENERALIZED_TIME, null, null), // not configured
        GRAPHIC_STRING(UniversalTags.GRAPHIC_STRING, String.class, Utf8StringConverter.class),
        VISIBLE_STRING(UniversalTags.VISIBLE_STRING, String.class, AsciiStringConverter.class),
        GENERAL_STRING(UniversalTags.GENERAL_STRING, String.class, Utf8StringConverter.class),
        UNIVERSAL_STRING(UniversalTags.UNIVERSAL_STRING, String.class, Utf8StringConverter.class),
        BITMAP_STRING(UniversalTags.BITMAP_STRING, String.class, Utf8StringConverter.class),
        CHARACTER_STRING(UniversalTags.CHARACTER_STRING, String.class, Utf8StringConverter.class);

        private final int value;
        private final Class<?> clazz;
        private final Class<? extends AsnConverter<?, ?>> converterClass;

        Mappings(int value, Class<?> clazz, Class<? extends AsnConverter<?, ?>> converterClass) {
            this.value = value;
            this.clazz = clazz;
            this.converterClass = converterClass;
        }
    }
}
