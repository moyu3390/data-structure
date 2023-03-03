/**
 * ASN1ParserCommandTest
 * <p>
 * 1.0
 * <p>
 * 2023/1/4 11:51
 */

package cn.com.infosec.data.structure.provide;

import cn.com.infosec.data.structure.asn.validator.impl.Asn1StrucutreSimpleValidatorImpl;
import cn.com.infosec.data.structure.bean.SelectNode;
import cn.com.infosec.data.structure.provide.parser.ASN1DataParser;
import cn.com.infosec.data.structure.utils.FileUtil;
import cn.com.infosec.v160.util.encoders.Base64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ASN1DataParserCommandTest {

    @Test
    public void asn1ParseCommandTest() {
//        String jsonDataTemplate = FileUtil.readInternalFileContext2String("P7EnvelopedData.json");
        String jsonDataTemplate = FileUtil.readInternalFileContext2String("P7SignedData.json");
        SelectNode sn1 = new SelectNode();
        sn1.setNodePath("/1/0/1/0/1/0/1/0/1");
        SelectNode sn2 = new SelectNode();
        sn2.setNodePath("/1/0/1/0/1/1");
        SelectNode sn3 = new SelectNode();
        sn3.setNodePath("/1/0/2/2");
        SelectNode sn4 = new SelectNode();
        sn4.setNodePath("/1/0/2");
        List<SelectNode> selectNodes = new ArrayList<>();
        selectNodes.add(sn1);
        selectNodes.add(sn2);
        selectNodes.add(sn3);
        selectNodes.add(sn4);

//        String base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
        String base64Der = "MIIDPwYKKoEcz1UGAQQCAqCCAy8wggMrAgEBMQ4wDAYIKoEcz1UBgxEFADAMBgoqgRzPVQYBBAIBoIICRzCCAkMwggHnoAMCAQICBSirwEsIMAwGCCqBHM9VAYN1BQAwRTELMAkGA1UEBhMCY24xIzAhBgNVBAoMGklORk9TRUMgVGVjaG5vbG9naWVzIFNNMklEMREwDwYDVQQDDAhhcHBTTTJJRDAeFw0xNTA3MDkwNjQ4NDdaFw0yMDEyMjkwNjQ4NDdaMEYxCzAJBgNVBAYTAmNuMSMwIQYDVQQKDBpJTkZPU0VDIFRlY2hub2xvZ2llcyBTTTJJRDESMBAGA1UEAwwJc20ySWR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE1quEEjM8TjKHV0yaXfBw0mEoqwlG1p97YzAPbFx6LyZ4Ffs6X7nVvJhka5hm350Zew6dEB/5cLScr/FD7tUtJ6OBwDCBvTAfBgNVHSMEGDAWgBQUnMMn64GFFUOZIozU+IhQn+oQmzAJBgNVHRMEAjAAMGMGA1UdHwRcMFowWKBWoFSkUjBQMQ4wDAYDVQQDDAVjcmw0NzEMMAoGA1UECwwDY3JsMSMwIQYDVQQKDBpJTkZPU0VDIFRlY2hub2xvZ2llcyBTTTJJRDELMAkGA1UEBhMCY24wCwYDVR0PBAQDAgeAMB0GA1UdDgQWBBSe0vvZNv4028wPqOePm+B9rweUijAMBggqgRzPVQGDdQUAA0gAMEUCIQCrBOV8Fihr5JR3NI/OI3DbCXeN6lAYMqD6qrEaigMAKAIgGnRYivTEy66DRbbeR1Kq3/nPyha0ubkkp4g5oc3Vp6kxgbwwgbkCAQEwTjBFMQswCQYDVQQGEwJjbjEjMCEGA1UECgwaSU5GT1NFQyBUZWNobm9sb2dpZXMgU00ySUQxETAPBgNVBAMMCGFwcFNNMklEAgUoq8BLCDAMBggqgRzPVQGDEQUAMA0GCSqBHM9VAYItAQUABEcwRQIgHaZDzn99kShjiqQNly0KuSr5UF4fvlWalaBHYTYgHGkCIQCfXmQbjGalmIXMKinx2p1Qtw56SwiTQuamiYrCZJ3zOw==";
        byte[] decode1 = Base64.decode(base64Der);

        Map<String, byte[]> nodesValueMap = ASN1DataParser.parseSelectNodesValue(jsonDataTemplate, selectNodes, decode1);
        Asn1StrucutreSimpleValidatorImpl validator = new Asn1StrucutreSimpleValidatorImpl();
        nodesValueMap.entrySet().forEach(e -> {
            System.err.println(e.getKey() + " ====> " + new String(Base64.encode(e.getValue())));
            validator.validator(e.getValue());
        });

    }

}
