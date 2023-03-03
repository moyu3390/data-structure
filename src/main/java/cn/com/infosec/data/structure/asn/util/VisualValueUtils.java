/**
 * VisualValueUtils
 * <p>
 * 1.0
 * <p>
 * 2022/12/23 9:57
 */

package cn.com.infosec.data.structure.asn.util;

import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.asn.tag.UniversalTags;
import cn.com.infosec.data.structure.asn.tlv.BerData;
import cn.com.infosec.v160.asn1.ASN1ObjectIdentifier;

import java.util.Objects;

public class VisualValueUtils {


    public static String getVisualValue(StructureJsonData instance, BerData berData) {
        return getVisualValue(instance.getTagName(), berData);
    }

    public static String getVisualValue(String tagName, BerData berData) {
        byte[] tlvData = getTlvData(berData);
        Integer integer = UniversalTags.ASN1PKCS_TAGS.get(tagName);
        if (Objects.nonNull(tagName) && Objects.nonNull(integer) && UniversalTags.OBJECT_IDENTIFIER == integer.intValue()) {
            ASN1ObjectIdentifier objectIdentifier = ASN1ObjectIdentifier.getInstance(tlvData);
            String id = objectIdentifier.getId();
            // 根据oid查找描述 如：1.2.840.113549.1.7.2==>signedData
            String oidType = ObjectIdentifiersUtils.getObjectIdentifierTypeName(id);
            String visual = oidType + "(%s)";
            visual = String.format(visual, id);
            return visual;
        }
        return null;
    }


    private static byte[] getTlvData(BerData berData) {
        if (Objects.isNull(berData)) {
            return new byte[0];
        }
        int tagLength = 0;
        if (!Objects.isNull(berData.getTag())) {
            tagLength = berData.getTag().length;
        }
        int lenLength = 0;
        if (!Objects.isNull(berData.getLength())) {
            lenLength = berData.getLength().length;
        }
        int valueLength = 0;
        if (!Objects.isNull(berData.getValue())) {
            valueLength = berData.getValue().length;
        }
        byte[] idData = new byte[tagLength + lenLength + valueLength];
        if (tagLength > 0) {
            System.arraycopy(berData.getTag(), 0, idData, 0, tagLength);
        }
        if (lenLength > 0) {
            System.arraycopy(berData.getLength(), 0, idData, tagLength, lenLength);
        }
        if (valueLength > 0) {
            System.arraycopy(berData.getValue(), 0, idData, tagLength + lenLength, valueLength);
        }
        return idData;
    }
}
