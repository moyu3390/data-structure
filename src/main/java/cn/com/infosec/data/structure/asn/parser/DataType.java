/**
 * DataType
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 17:03
 */

package cn.com.infosec.data.structure.asn.parser;

public enum DataType {
    PKCS7_SIGNED_DATA(0, "P7SignedData", "P7SignedData.json"),
    PKCS7_ENVELOPED_DATA(1, "P7EnvelopedData", "P7EnvelopedData.json"),
    PKCS7_DIGESTED_DATA(2, "P7DigestedData", "P7DigestedData.json"),
    PKCS7_ENCRYPTED_DATA(3, "P7EncryptedData", "P7EncryptedData.json"),
    RAW_DATA(4, "RawData", "RawData.json"),
    PKCS7_SIGNEDANDENVELOPED_DATA(5, "P7SignedAndEnvelopedData", "P7SignedAndEnvelopedData.json"),
    X509CERTIFICATE_DATA(6, "X509CertificateStructure", "X509CertificateStructure.json"),
    PRIVATE_DATA(99, "Private", "Private.json"),
    UNKNOW_DATA(199, "Unknow", "Unknow.json");

    private int index;
    private String dataType;
    private String attributeTemplateName;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAttributeTemplateName() {
        return attributeTemplateName;
    }

    public void setAttributeTemplateName(String attributeTemplateName) {
        this.attributeTemplateName = attributeTemplateName;
    }

    DataType(int index, String dataType, String attributeTemplateName) {
        this.index = index;
        this.dataType = dataType;
        this.attributeTemplateName = attributeTemplateName;
    }

}
