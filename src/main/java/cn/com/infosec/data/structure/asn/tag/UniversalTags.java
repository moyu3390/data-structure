package cn.com.infosec.data.structure.asn.tag;

import java.util.HashMap;
import java.util.Map;

public final class UniversalTags {
    public static final int BOOLEAN = 1;
    public static final int INTEGER = 2;
    public static final int BIT_STRING = 3;
    public static final int OCTET_STRING = 4;
    public static final int NULL = 5;
    public static final int OBJECT_IDENTIFIER = 6;
    public static final int OBJECT_DESCRIPTOR = 7;
    public static final int EXTERNAL = 8;
    public static final int REAL = 9;

    public static final int ENUMERATED = 10;
    public static final int EMBEDDED_PDV = 11; // decimal 11
    public static final int UTF8_STRING = 12;
    public static final int RELATIVE_OID = 13; // decimal 13
    public static final int SEQUENCE = 16;
    public static final int SEQUENCE_OF = 16; // for completeness - used to model a SEQUENCE of the same type.
    public static final int SET = 17;
    public static final int SET_OF = 17; // for completeness - used to model a SET of the same type.
    public static final int NUMERIC_STRING = 18;
    public static final int PRINTABLE_STRING = 19;
    public static final int TELETEX_STRING = 20;
    public static final int T61_STRING = 20; // decimal 20
    public static final int VIDEOTEX_STRING = 21;
    public static final int IA5_STRING = 22;
    public static final int UTC_TIME = 23;
    public static final int GENERALIZED_TIME = 24;
    public static final int GRAPHIC_STRING = 25;
    public static final int VISIBLE_STRING = 26;
    public static final int GENERAL_STRING = 27;
    public static final int UNIVERSAL_STRING = 28;
    public static final int CHARACTER_STRING = 29;
    public static final int UNRESTRICTED_STRING = 29; // decimal 29
    public static final int BITMAP_STRING = 30;


    public static final int CONSTRUCTED = 32; // decimal 32

    public static final int UNIVERSAL = 32; // decimal 32
    public static final int APPLICATION = 64; // decimal 64
    public static final int TAGGED = 128; // decimal 128 - maybe should deprecate this.
    public static final int CONTEXT_SPECIFIC = 128; // decimal 128
    public static final int PRIVATE = 192; // decimal 192

    public static final int FLAGS = 224;


    public static Map<String, Integer> ASN1PKCS_TAGS = new HashMap<>();

    static {
        ASN1PKCS_TAGS.put("BOOLEAN", BOOLEAN);
        ASN1PKCS_TAGS.put("INTEGER", INTEGER);
        ASN1PKCS_TAGS.put("BIT_STRING", BIT_STRING);
        ASN1PKCS_TAGS.put("OCTET_STRING", OCTET_STRING);
        ASN1PKCS_TAGS.put("NULL", NULL);
        ASN1PKCS_TAGS.put("OBJECT_IDENTIFIER", OBJECT_IDENTIFIER);
        ASN1PKCS_TAGS.put("OBJECT_DESCRIPTOR", OBJECT_DESCRIPTOR);
        ASN1PKCS_TAGS.put("EXTERNAL", EXTERNAL);
        ASN1PKCS_TAGS.put("REAL", REAL);
        ASN1PKCS_TAGS.put("ENUMERATED", ENUMERATED);
        ASN1PKCS_TAGS.put("EMBEDDED_PDV", EMBEDDED_PDV);
        ASN1PKCS_TAGS.put("UTF8_STRING", UTF8_STRING);
        ASN1PKCS_TAGS.put("RELATIVE_OID", RELATIVE_OID);
        ASN1PKCS_TAGS.put("SEQUENCE", SEQUENCE);
        ASN1PKCS_TAGS.put("SEQUENCE", SEQUENCE_OF);
        ASN1PKCS_TAGS.put("SET", SET);
        ASN1PKCS_TAGS.put("SET", SET_OF);
        ASN1PKCS_TAGS.put("NUMERIC_STRING", NUMERIC_STRING);
        ASN1PKCS_TAGS.put("PRINTABLE_STRING", PRINTABLE_STRING);
        ASN1PKCS_TAGS.put("TELETEX_STRING", TELETEX_STRING);
        ASN1PKCS_TAGS.put("T61_STRING", T61_STRING);
        ASN1PKCS_TAGS.put("VIDEOTEX_STRING", VIDEOTEX_STRING);
        ASN1PKCS_TAGS.put("IA5_STRING", IA5_STRING);
        ASN1PKCS_TAGS.put("UTC_TIME", UTC_TIME);
        ASN1PKCS_TAGS.put("GENERALIZED_TIME", GENERALIZED_TIME);
        ASN1PKCS_TAGS.put("GRAPHIC_STRING", GRAPHIC_STRING);
        ASN1PKCS_TAGS.put("VISIBLE_STRING", VISIBLE_STRING);
        ASN1PKCS_TAGS.put("GENERAL_STRING", GENERAL_STRING);
        ASN1PKCS_TAGS.put("UNIVERSAL_STRING", UNIVERSAL_STRING);
        ASN1PKCS_TAGS.put("CHARACTER_STRING", CHARACTER_STRING);
        ASN1PKCS_TAGS.put("UNRESTRICTED_STRING", UNRESTRICTED_STRING);
        ASN1PKCS_TAGS.put("BITMAP_STRING", BITMAP_STRING);
        ASN1PKCS_TAGS.put("CONSTRUCTED", CONSTRUCTED);
        ASN1PKCS_TAGS.put("UNIVERSAL", UNIVERSAL);
        ASN1PKCS_TAGS.put("APPLICATION", APPLICATION);
        ASN1PKCS_TAGS.put("TAGGED", TAGGED);
        ASN1PKCS_TAGS.put("CONTEXT_SPECIFIC", CONTEXT_SPECIFIC);
        ASN1PKCS_TAGS.put("PRIVATE", PRIVATE);
        ASN1PKCS_TAGS.put("FLAGS", FLAGS);
    }
}
