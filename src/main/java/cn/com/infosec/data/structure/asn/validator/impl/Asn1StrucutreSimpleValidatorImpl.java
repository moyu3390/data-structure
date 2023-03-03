/**
 * Asn1StrucutreValidator
 * <p> ASN1结构简单验证器
 * 1.0
 * <p>
 * 2022/12/29 15:11
 */

package cn.com.infosec.data.structure.asn.validator.impl;

import cn.com.infosec.data.structure.asn.exception.AsnDecodeException;
import cn.com.infosec.data.structure.asn.validator.IAsn1StrucutreValidator;
import cn.com.infosec.v160.asn1.ASN1InputStream;
import cn.com.infosec.v160.asn1.ASN1Primitive;
import cn.com.infosec.v160.asn1.util.ASN1Dump;

import java.io.ByteArrayInputStream;

public class Asn1StrucutreSimpleValidatorImpl implements IAsn1StrucutreValidator<byte[]> {
    @Override
    public void validator(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ASN1InputStream inputStream = new ASN1InputStream(byteArrayInputStream);
            // 验证
            ASN1Primitive primitive = inputStream.readObject();
            ASN1Dump.dumpAsString(primitive, false);
        } catch (Exception e) {
            throw new AsnDecodeException(e);
        }
    }


    @Override
    public String getValidatorName() {
        return this.getClass().getSimpleName();
    }
}
