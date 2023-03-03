/**
 * ObjectIdentifiersUtils
 * <p>
 * 1.0
 * <p>
 * 2022/12/23 10:25
 */

package cn.com.infosec.data.structure.asn.util;

import cn.com.infosec.v160.asn1.ASN1ObjectIdentifier;
import cn.com.infosec.v160.asn1.gm.GMObjectIdentifiers;
import cn.com.infosec.v160.asn1.pkcs.PKCSObjectIdentifiers;
import cn.com.infosec.v160.asn1.x509.X509ObjectIdentifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectIdentifiersUtils {

    // 1.2.840.113549.1.7.2==>signedData
    private static final Map<ASN1ObjectIdentifier, String> OID_MAP;


    public static ASN1ObjectIdentifier getObjectIdentifier(String oid) {
        ASN1ObjectIdentifier asn1ObjectIdentifier = OID_MAP.entrySet().stream().filter(m -> m.getKey().getId().equals(oid)).map(Map.Entry::getKey).findFirst().orElse(null);
        return asn1ObjectIdentifier;
    }

    public static String getObjectIdentifierTypeName(String oid) {
        String typeName = OID_MAP.entrySet().stream().filter(m -> m.getKey().getId().equals(oid)).map(Map.Entry::getValue).findFirst().orElse(null);
        return (Objects.isNull(typeName) || typeName.trim().length() == 0) ? "" : typeName;
    }

    static {
        OID_MAP = init();
    }

    private static Map<ASN1ObjectIdentifier, String> init() {
        Map<ASN1ObjectIdentifier, String> OID_MAP = new HashMap<>();
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_1, "PKCS_1");
        OID_MAP.put(PKCSObjectIdentifiers.rsaEncryption, "rsaEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.md2WithRSAEncryption, "md2WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.md4WithRSAEncryption, "md4WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.md5WithRSAEncryption, "md5WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, "sha1WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.srsaOAEPEncryptionSET, "srsaOAEPEncryptionSET");
        OID_MAP.put(PKCSObjectIdentifiers.id_RSAES_OAEP, "RSAES_OAEP");
        OID_MAP.put(PKCSObjectIdentifiers.id_mgf1, "mgf1");
        OID_MAP.put(PKCSObjectIdentifiers.id_pSpecified, "pSpecified");
        OID_MAP.put(PKCSObjectIdentifiers.id_RSASSA_PSS, "RSASSA_PSS");
        OID_MAP.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "sha256WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "sha384WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "sha512WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "sha224WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha512_224WithRSAEncryption, "sha512_224WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.sha512_256WithRSAEncryption, "sha512_256WithRSAEncryption");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_3, "PKCS_3");
        OID_MAP.put(PKCSObjectIdentifiers.dhKeyAgreement, "dhKeyAgreement");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_5, "PKCS_5");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC, "pbeWithMD2AndDES_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, "pbeWithMD2AndRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC, "pbeWithMD5AndDES_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, "pbeWithMD5AndRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC, "pbeWithSHA1AndDES_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, "pbeWithSHA1AndRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.id_PBES2, "PBES2");
        OID_MAP.put(PKCSObjectIdentifiers.id_PBKDF2, "PBKDF2");
        OID_MAP.put(PKCSObjectIdentifiers.encryptionAlgorithm, "encryptionAlgorithm");
        OID_MAP.put(PKCSObjectIdentifiers.des_EDE3_CBC, "des_EDE3_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.RC2_CBC, "RC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.rc4, "rc4");
        OID_MAP.put(PKCSObjectIdentifiers.digestAlgorithm, "digestAlgorithm");
        OID_MAP.put(PKCSObjectIdentifiers.md2, "md2");
        OID_MAP.put(PKCSObjectIdentifiers.md4, "md4");
        OID_MAP.put(PKCSObjectIdentifiers.md5, "md5");
        OID_MAP.put(PKCSObjectIdentifiers.id_hmacWithSHA1, "hmacWithSHA1");
        OID_MAP.put(PKCSObjectIdentifiers.id_hmacWithSHA224, "hmacWithSHA224");
        OID_MAP.put(PKCSObjectIdentifiers.id_hmacWithSHA256, "hmacWithSHA256");
        OID_MAP.put(PKCSObjectIdentifiers.id_hmacWithSHA384, "hmacWithSHA384");
        OID_MAP.put(PKCSObjectIdentifiers.id_hmacWithSHA512, "hmacWithSHA512");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_7, "PKCS_7");
        OID_MAP.put(PKCSObjectIdentifiers.data, "data");
        OID_MAP.put(PKCSObjectIdentifiers.signedData, "signedData");
        OID_MAP.put(PKCSObjectIdentifiers.envelopedData, "envelopedData");
        OID_MAP.put(PKCSObjectIdentifiers.signedAndEnvelopedData, "signedAndEnvelopedData");
        OID_MAP.put(PKCSObjectIdentifiers.digestedData, "digestedData");
        OID_MAP.put(PKCSObjectIdentifiers.encryptedData, "encryptedData");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9, "PKCS_9");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_emailAddress, "pkcs_9_at_emailAddress");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_unstructuredName, "pkcs_9_at_unstructuredName");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_contentType, "pkcs_9_at_contentType");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_messageDigest, "pkcs_9_at_messageDigest");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_signingTime, "pkcs_9_at_signingTime");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_counterSignature, "pkcs_9_at_counterSignature");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_challengePassword, "pkcs_9_at_challengePassword");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress, "pkcs_9_at_unstructuredAddress");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_extendedCertificateAttributes, "pkcs_9_at_extendedCertificateAttributes");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_signingDescription, "pkcs_9_at_signingDescription");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, "pkcs_9_at_extensionRequest");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_smimeCapabilities, "pkcs_9_at_smimeCapabilities");
        OID_MAP.put(PKCSObjectIdentifiers.id_smime, "smime");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, "pkcs_9_at_friendlyName");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, "pkcs_9_at_localKeyId");
        OID_MAP.put(PKCSObjectIdentifiers.x509certType, "x509certType");
        OID_MAP.put(PKCSObjectIdentifiers.certTypes, "certTypes");
        OID_MAP.put(PKCSObjectIdentifiers.x509Certificate, "x509Certificate");
        OID_MAP.put(PKCSObjectIdentifiers.sdsiCertificate, "sdsiCertificate");
        OID_MAP.put(PKCSObjectIdentifiers.crlTypes, "crlTypes");
        OID_MAP.put(PKCSObjectIdentifiers.x509Crl, "x509Crl");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_cmsAlgorithmProtect, "aa_cmsAlgorithmProtect");
        OID_MAP.put(PKCSObjectIdentifiers.preferSignedData, "preferSignedData");
        OID_MAP.put(PKCSObjectIdentifiers.canNotDecryptAny, "pkcs_9");
        OID_MAP.put(PKCSObjectIdentifiers.sMIMECapabilitiesVersions, "pkcs_9");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct, "ct");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct_authData, "ct_authData");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct_TSTInfo, "ct_TSTInfo");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct_compressedData, "ct_compressedData");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct_authEnvelopedData, "ct_authEnvelopedData");
        OID_MAP.put(PKCSObjectIdentifiers.id_ct_timestampedData, "ct_timestampedData");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg, "alg");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg_PWRI_KEK, "alg_PWRI_KEK");
        OID_MAP.put(PKCSObjectIdentifiers.id_rsa_KEM, "rsa_KEM");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti, "cti");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfOrigin, "cti_ets_proofOfOrigin");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfReceipt, "cti_ets_proofOfReceipt");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfDelivery, "cti_ets_proofOfDelivery");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfSender, "cti_ets_proofOfSender");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfApproval, "cti_ets_proofOfApproval");
        OID_MAP.put(PKCSObjectIdentifiers.id_cti_ets_proofOfCreation, "cti_ets_proofOfCreation");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa, "aa");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_receiptRequest, "aa_receiptRequest");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_contentHint, "aa_contentHint");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_msgSigDigest, "aa_msgSigDigest");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_contentReference, "aa_contentReference");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_encrypKeyPref, "aa_encrypKeyPref");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_signingCertificate, "aa_signingCertificate");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_signingCertificateV2, "aa_signingCertificateV2");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_contentIdentifier, "aa_contentIdentifier");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken, "aa_signatureTimeStampToken");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_sigPolicyId, "aa_ets_sigPolicyId");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_commitmentType, "aa_ets_commitmentType");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_signerLocation, "aa_ets_signerLocation");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_signerAttr, "aa_ets_signerAttr");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_otherSigCert, "aa_ets_otherSigCert");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_contentTimestamp, "aa_ets_contentTimestamp");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_certificateRefs, "aa_ets_certificateRefs");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_revocationRefs, "aa_ets_revocationRefs");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_certValues, "aa_ets_certValues");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_revocationValues, "aa_ets_revocationValues");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_escTimeStamp, "aa_ets_escTimeStamp");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_certCRLTimestamp, "aa_ets_certCRLTimestamp");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_ets_archiveTimestamp, "aa_ets_archiveTimestamp");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_decryptKeyID, "aa_decryptKeyID");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_implCryptoAlgs, "aa_implCryptoAlgs");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_asymmDecryptKeyID, "aa_asymmDecryptKeyID");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_implCompressAlgs, "aa_implCompressAlgs");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_communityIdentifiers, "aa_communityIdentifiers");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_sigPolicyId, "aa_sigPolicyId");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_commitmentType, "aa_commitmentType");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_signerLocation, "aa_signerLocation");
        OID_MAP.put(PKCSObjectIdentifiers.id_aa_otherSigCert, "aa_otherSigCert");
        OID_MAP.put(PKCSObjectIdentifiers.id_spq_ets_uri, "spq_ets_uri");
        OID_MAP.put(PKCSObjectIdentifiers.id_spq_ets_unotice, "spq_ets_unotice");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_12, "pkcs_12");
        OID_MAP.put(PKCSObjectIdentifiers.bagtypes, "bagtypes");
        OID_MAP.put(PKCSObjectIdentifiers.keyBag, "keyBag");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag, "pkcs8ShroudedKeyBag");
        OID_MAP.put(PKCSObjectIdentifiers.certBag, "certBag");
        OID_MAP.put(PKCSObjectIdentifiers.crlBag, "crlBag");
        OID_MAP.put(PKCSObjectIdentifiers.secretBag, "secretBag");
        OID_MAP.put(PKCSObjectIdentifiers.safeContentsBag, "safeContentsBag");
        OID_MAP.put(PKCSObjectIdentifiers.pkcs_12PbeIds, "pkcs_12PbeIds");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4, "pbeWithSHAAnd128BitRC4");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC4, "pbeWithSHAAnd40BitRC4");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC, "pbeWithSHAAnd3_KeyTripleDES_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd2_KeyTripleDES_CBC, "pbeWithSHAAnd2_KeyTripleDES_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, "pbeWithSHAAnd128BitRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC, "pbeWithSHAAnd40BitRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.pbewithSHAAnd40BitRC2_CBC, "pbewithSHAAnd40BitRC2_CBC");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap, "alg_CMS3DESwrap");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg_CMSRC2wrap, "alg_CMSRC2wrap");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg_ESDH, "alg_ESDH");
        OID_MAP.put(PKCSObjectIdentifiers.id_alg_SSDH, "alg_SSDH");


        // GM
        OID_MAP.put(GMObjectIdentifiers.sm_scheme, "sm_scheme");
        OID_MAP.put(GMObjectIdentifiers.sm6_ecb, "sm6_ecb");
        OID_MAP.put(GMObjectIdentifiers.sm6_cbc, "sm6_cbc");
        OID_MAP.put(GMObjectIdentifiers.sm6_ofb128, "sm6_ofb128");
        OID_MAP.put(GMObjectIdentifiers.sm6_cfb128, "sm6_cfb128");
        OID_MAP.put(GMObjectIdentifiers.sm1_ecb, "sm1_ecb");
        OID_MAP.put(GMObjectIdentifiers.sm1_cbc, "sm1_cbc");
        OID_MAP.put(GMObjectIdentifiers.sm1_ofb128, "sm1_ofb128");
        OID_MAP.put(GMObjectIdentifiers.sm1_cfb128, "sm1_cfb128");
        OID_MAP.put(GMObjectIdentifiers.sm1_cfb1, "sm1_cfb1");
        OID_MAP.put(GMObjectIdentifiers.sm1_cfb8, "sm1_cfb8");
        OID_MAP.put(GMObjectIdentifiers.ssf33_ecb, "ssf33_ecb");
        OID_MAP.put(GMObjectIdentifiers.ssf33_cbc, "ssf33_cbc");
        OID_MAP.put(GMObjectIdentifiers.ssf33_ofb128, "ssf33_ofb128");
        OID_MAP.put(GMObjectIdentifiers.ssf33_cfb128, "ssf33_cfb128");
        OID_MAP.put(GMObjectIdentifiers.ssf33_cfb1, "ssf33_cfb1");
        OID_MAP.put(GMObjectIdentifiers.ssf33_cfb8, "ssf33_cfb8");
        OID_MAP.put(GMObjectIdentifiers.sms4, "sms4");
        OID_MAP.put(GMObjectIdentifiers.sms4_ecb, "sms4_ecb");
        OID_MAP.put(GMObjectIdentifiers.sms4_cbc, "sms4_cbc");
        OID_MAP.put(GMObjectIdentifiers.sms4_ofb128, "sms4_ofb128");
        OID_MAP.put(GMObjectIdentifiers.sms4_cfb128, "sms4_cfb128");
        OID_MAP.put(GMObjectIdentifiers.sms4_cfb1, "sms4_cfb1");
        OID_MAP.put(GMObjectIdentifiers.sms4_cfb8, "sms4_cfb8");
        OID_MAP.put(GMObjectIdentifiers.sms4_ctr, "sms4_ctr");
        OID_MAP.put(GMObjectIdentifiers.sms4_gcm, "sms4_gcm");
        OID_MAP.put(GMObjectIdentifiers.sms4_ccm, "sms4_ccm");
        OID_MAP.put(GMObjectIdentifiers.sms4_xts, "sms4_xts");
        OID_MAP.put(GMObjectIdentifiers.sms4_wrap, "sms4_wrap");
        OID_MAP.put(GMObjectIdentifiers.sms4_wrap_pad, "sms4_wrap_pad");
        OID_MAP.put(GMObjectIdentifiers.sms4_ocb, "sms4_ocb");
        OID_MAP.put(GMObjectIdentifiers.sm5, "sm5");
        OID_MAP.put(GMObjectIdentifiers.sm2p256v1, "sm2p256v1");
        OID_MAP.put(GMObjectIdentifiers.sm2sign, "sm2sign");
        OID_MAP.put(GMObjectIdentifiers.sm2exchange, "sm2exchange");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt, "sm2encrypt");
        OID_MAP.put(GMObjectIdentifiers.wapip192v1, "wapip192v1");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_recommendedParameters, "sm2encrypt_recommendedParameters");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_specifiedParameters, "sm2encrypt_specifiedParameters");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sm3, "sm2encrypt_with_sm3");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sha1, "sm2encrypt_with_sha1");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sha224, "sm2encrypt_with_sha224");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sha256, "sm2encrypt_with_sha256");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sha384, "sm2encrypt_with_sha384");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_sha512, "sm2encrypt_with_sha512");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_rmd160, "sm2encrypt_with_rmd160");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_whirlpool, "sm2encrypt_with_whirlpool");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_blake2b512, "sm2encrypt_with_blake2b512");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_blake2s256, "sm2encrypt_with_blake2s256");
        OID_MAP.put(GMObjectIdentifiers.sm2encrypt_with_md5, "sm2encrypt_with_md5");
        OID_MAP.put(GMObjectIdentifiers.id_sm9PublicKey, "sm9PublicKey");
        OID_MAP.put(GMObjectIdentifiers.sm9sign, "sm9sign");
        OID_MAP.put(GMObjectIdentifiers.sm9keyagreement, "sm9keyagreement");
        OID_MAP.put(GMObjectIdentifiers.sm9encrypt, "sm9encrypt");
        OID_MAP.put(GMObjectIdentifiers.sm3, "sm3");
        OID_MAP.put(GMObjectIdentifiers.hmac_sm3, "hmac_sm3");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sm3, "sm2sign_with_sm3");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sha1, "sm2sign_with_sha1");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sha256, "sm2sign_with_sha256");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sha512, "sm2sign_with_sha512");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sha224, "sm2sign_with_sha224");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_sha384, "sm2sign_with_sha384");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_rmd160, "sm2sign_with_rmd160");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_whirlpool, "sm2sign_with_whirlpool");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_blake2b512, "sm2sign_with_blake2b512");
        OID_MAP.put(GMObjectIdentifiers.sm2sign_with_blake2s256, "sm2sign_with_blake2s256");
        OID_MAP.put(GMObjectIdentifiers.sm_pkcs7_scheme, "sm_pkcs7_scheme");
        OID_MAP.put(GMObjectIdentifiers.data, "data");
        OID_MAP.put(GMObjectIdentifiers.signedData, "signedData");
        OID_MAP.put(GMObjectIdentifiers.envelopedData, "envelopedData");
        OID_MAP.put(GMObjectIdentifiers.signedAndEnvelopedData, "signedAndEnvelopedData");
        OID_MAP.put(GMObjectIdentifiers.encryptedData, "encryptedData");
        OID_MAP.put(GMObjectIdentifiers.q5_ids, "q5_ids");
        OID_MAP.put(GMObjectIdentifiers.id_PBKDF, "PBKDF");
        OID_MAP.put(GMObjectIdentifiers.id_PBES, "PBES");
        OID_MAP.put(GMObjectIdentifiers.id_PBMAC, "PBMAC");
        OID_MAP.put(GMObjectIdentifiers.ckx, "ckx");
        OID_MAP.put(GMObjectIdentifiers.ckx_bags, "ckx_bags");
        OID_MAP.put(GMObjectIdentifiers.ckx_keyBag, "ckx_keyBag");
        OID_MAP.put(GMObjectIdentifiers.ckx_shroudedKeyBag, "ckx_shroudedKeyBag");
        OID_MAP.put(GMObjectIdentifiers.ckx_certBag, "ckx_certBag");
        OID_MAP.put(GMObjectIdentifiers.ckx_crlBag, "ckx_crlBag");
        OID_MAP.put(GMObjectIdentifiers.ckx_secretBag, "ckx_secretBag");
        OID_MAP.put(GMObjectIdentifiers.ckx_safeContentBag, "ckx_safeContentBag");
        OID_MAP.put(GMObjectIdentifiers.pkcs9_ids, "pkcs9_ids");
        OID_MAP.put(GMObjectIdentifiers.pkcs9_friendlyName, "pkcs9_friendlyName");
        OID_MAP.put(GMObjectIdentifiers.pkcs9_localKeyId, "pkcs9_localKeyId");
        OID_MAP.put(GMObjectIdentifiers.pkcs9_certTypes, "pkcs9_certTypes");
        OID_MAP.put(GMObjectIdentifiers.pkcs9_crlTypes, "pkcs9_crlTypes");
        OID_MAP.put(GMObjectIdentifiers.x509Certificate, "x509Certificate");
        OID_MAP.put(GMObjectIdentifiers.sdsiCertificate, "sdsiCertificate");

        // X509
        OID_MAP.put(X509ObjectIdentifiers.commonName, "C");
        OID_MAP.put(X509ObjectIdentifiers.countryName, "C");
        OID_MAP.put(X509ObjectIdentifiers.localityName, "L");
        OID_MAP.put(X509ObjectIdentifiers.stateOrProvinceName, "SPN");
        OID_MAP.put(X509ObjectIdentifiers.organization, "O");
        OID_MAP.put(X509ObjectIdentifiers.organizationalUnitName, "OU");
        OID_MAP.put(X509ObjectIdentifiers.id_at_telephoneNumber, "telephoneNumber");
        OID_MAP.put(X509ObjectIdentifiers.id_at_name, "name");
        OID_MAP.put(X509ObjectIdentifiers.id_at_organizationIdentifier, "organizationIdentifier");
        OID_MAP.put(X509ObjectIdentifiers.id_SHA1, "SHA1");
        OID_MAP.put(X509ObjectIdentifiers.ripemd160, "ripemd160");
        OID_MAP.put(X509ObjectIdentifiers.ripemd160WithRSAEncryption, "ripemd160WithRSAEncryption");
        OID_MAP.put(X509ObjectIdentifiers.id_ea_rsa, "ea_rsa");
        OID_MAP.put(X509ObjectIdentifiers.id_pkix, "pkix");
        OID_MAP.put(X509ObjectIdentifiers.id_pe, "pe");
        OID_MAP.put(X509ObjectIdentifiers.id_ce, "ce");
        OID_MAP.put(X509ObjectIdentifiers.id_ad, "ad");
        OID_MAP.put(X509ObjectIdentifiers.id_ad_caIssuers, "ad_caIssuers");
        OID_MAP.put(X509ObjectIdentifiers.id_ad_ocsp, "ad_ocsp");
        OID_MAP.put(X509ObjectIdentifiers.ocspAccessMethod, "ocspAccessMethod");
        OID_MAP.put(X509ObjectIdentifiers.crlAccessMethod, "crlAccessMethod");

        return OID_MAP;
    }
}
