/**
 * X509CertificateDataParser
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 17:27
 */

package cn.com.infosec.data.structure.asn.parser.impl;

import cn.com.infosec.asn1.ASN1InputStream;
import cn.com.infosec.asn1.ASN1Sequence;
import cn.com.infosec.asn1.x509.X509CertificateStructure;
import cn.com.infosec.data.structure.asn.parser.DataType;
import cn.com.infosec.data.structure.asn.parser.IDataTypeParser;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class X509CertificateDataParser implements IDataTypeParser {
    @Override
    public DataType parseData(byte[] data) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            ASN1InputStream inputStream = new ASN1InputStream(byteArrayInputStream);
            ASN1Sequence asn1Sequence = (ASN1Sequence) inputStream.readObject();
            X509CertificateStructure instance = X509CertificateStructure.getInstance(asn1Sequence);
            if (Objects.nonNull(instance)) {
                return DataType.X509CERTIFICATE_DATA;
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return DataType.UNKNOW_DATA;
    }
}
