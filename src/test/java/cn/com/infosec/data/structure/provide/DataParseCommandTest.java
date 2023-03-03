/**
 * DataParseCommandTest
 * <p>
 * 1.0
 * <p>
 * 2023/1/3 9:49
 */

package cn.com.infosec.data.structure.provide;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.encoder.BerStructureBuilder;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.TagType;
import cn.com.infosec.data.structure.asn.tlv.BerData;
import cn.com.infosec.data.structure.asn.tlv.BerDataReader;
import cn.com.infosec.data.structure.asn.tlv.TlvDataReader;
import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.provide.parser.DerJsonParser;
import cn.com.infosec.data.structure.utils.EncodingUtil;
import cn.com.infosec.data.structure.utils.FileUtil;
import cn.com.infosec.v160.asn1.*;
import cn.com.infosec.v160.asn1.cms.EncryptedContentInfo;
import cn.com.infosec.v160.asn1.cms.EncryptedData;
import cn.com.infosec.v160.asn1.pkcs.ContentInfo;
import cn.com.infosec.v160.asn1.pkcs.PKCSObjectIdentifiers;
import cn.com.infosec.v160.asn1.x509.AlgorithmIdentifier;
import cn.com.infosec.v160.util.Arrays;
import cn.com.infosec.v160.util.encoders.Base64;
import cn.com.infosec.v160.util.encoders.Hex;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class DataParseCommandTest {


    @Test
    public void derDataAnalysisTest() {
        long start = System.currentTimeMillis();
        long count = 0;
        final byte[] ber = HexUtils.decode("F03C0101FF020118311004063859980690030406385998069002A11FA20D040546697273740201018201FFA20E04065365636F6E64020102820100830128");
        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";
        base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
        String string = "";
        byte[] decode1 = Base64.decode(base64Der);
        while (count < 1) {
//        string = Data2JsonParser.derDataAnalysis(ber, "H");
//        System.err.println(string);
//        decode1 = ber;

            string = DerJsonParser.derDataAnalysis(base64Der.getBytes(), "1");
//            System.err.println(string);
//            string = DerJsonParser.derDataAnalysis(decode1, "3");
//            System.err.println(string);
            JsonObject asJsonObject = JsonParser.parseString(string).getAsJsonObject();
            JsonElement jsonElement = asJsonObject.get("data");
//            System.err.println(jsonElement.toString());
            count++;
        }
        long end = System.currentTimeMillis();
        System.err.println("执行" + count + "次解析，耗时：(" + (end - start) + ") ms");
    }

    @Test
    public void der2json_test() {
        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";
        String der2json = DerJsonParser.der2json(Base64.decode(base64Der));
        System.err.println(der2json);
    }

    @Test
    public void der2json_by_encoding_test() {
        String base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
        String der2json = DerJsonParser.der2json(Base64.decode(base64Der), "ASCII");
        System.err.println(der2json);
    }

    @Test
    public void json2Der_by_encoding_test() {
        String jsonData_ascii = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":428,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010703\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"envelopedData(1.2.840.113549.1.7.3)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"envelopedData\",\"isValidator\":false},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":0,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-96]},\"tagNum\":160,\"length\":413,\"isContainer\":true,\"children\":[{\"id\":\"1.0\",\"pId\":\"1\",\"path\":\"/1/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":409,\"isContainer\":true,\"children\":[{\"id\":\"1.0.0\",\"pId\":\"1.0\",\"path\":\"/1/0/0\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"1.0.1\",\"pId\":\"1.0\",\"path\":\"/1/0/1\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":353,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0\",\"pId\":\"1.0.1\",\"path\":\"/1/0/1/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":349,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.0\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/0\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"1.0.1.0.1\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/1\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":69,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0\",\"pId\":\"1.0.1.0.1\",\"path\":\"/1/0/1/0/1/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":52,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.0\",\"pId\":\"1.0.1.0.1.0\",\"path\":\"/1/0/1/0/1/0/0\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":11,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.0.0\",\"pId\":\"1.0.1.0.1.0.0\",\"path\":\"/1/0/1/0/1/0/0/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.0.0.0\",\"pId\":\"1.0.1.0.1.0.0.0\",\"path\":\"/1/0/1/0/1/0/0/0/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"550406\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"C(2.5.4.6)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"C\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.0.0.0.1\",\"pId\":\"1.0.1.0.1.0.0.0\",\"path\":\"/1/0/1/0/1/0/0/0/1\",\"tag\":{\"value\":19,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":2,\"isContainer\":false,\"hexValue\":\"434E\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"PRINTABLE_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.0.1\",\"pId\":\"1.0.1.0.1.0\",\"path\":\"/1/0/1/0/1/0/1\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":19,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.1.0\",\"pId\":\"1.0.1.0.1.0.1\",\"path\":\"/1/0/1/0/1/0/1/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":17,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.1.0.0\",\"pId\":\"1.0.1.0.1.0.1.0\",\"path\":\"/1/0/1/0/1/0/1/0/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"O(2.5.4.10)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"O\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.0.1.0.1\",\"pId\":\"1.0.1.0.1.0.1.0\",\"path\":\"/1/0/1/0/1/0/1/0/1\",\"tag\":{\"value\":19,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":10,\"isContainer\":false,\"hexValue\":\"494E464F534543204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"PRINTABLE_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.0.2\",\"pId\":\"1.0.1.0.1.0\",\"path\":\"/1/0/1/0/1/0/2\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.2.0\",\"pId\":\"1.0.1.0.1.0.2\",\"path\":\"/1/0/1/0/1/0/2/0\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.1.0.2.0.0\",\"pId\":\"1.0.1.0.1.0.2.0\",\"path\":\"/1/0/1/0/1/0/2/0/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"OU(2.5.4.11)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OU\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.0.2.0.1\",\"pId\":\"1.0.1.0.1.0.2.0\",\"path\":\"/1/0/1/0/1/0/2/0/1\",\"tag\":{\"value\":19,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":7,\"isContainer\":false,\"hexValue\":\"494E464F204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"PRINTABLE_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false},{\"id\":\"1.0.1.0.1.1\",\"pId\":\"1.0.1.0.1\",\"path\":\"/1/0/1/0/1/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":13,\"isContainer\":false,\"hexValue\":\"00CE3E602E7DAE236566566F8B\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false},{\"id\":\"1.0.1.0.2\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/2\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.2.0\",\"pId\":\"1.0.1.0.2\",\"path\":\"/1/0/1/0/2/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010101\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"rsaEncryption(1.2.840.113549.1.1.1)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"rsaEncryption\",\"isValidator\":false},{\"id\":\"1.0.1.0.2.1\",\"pId\":\"1.0.1.0.2\",\"path\":\"/1/0/1/0/2/1\",\"tag\":{\"value\":5,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"NULL\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false},{\"id\":\"1.0.1.0.3\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/3\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":256,\"isContainer\":false,\"hexValue\":\"A5A0DBBA3F574179649BCEF049AA0F53F4945AE742A4F225E0A89213B74B8E22390B17E5B681813FF390558D691CC6E24603FBAC1EB83EB6A6015D19BE0742188577DFD1179E7A60CA6F56113AF567BC58470569F9D613418982FCD64DF9B8370C8CA72D77484CDEDB5410DFEB90E0A0A67C531626CE6560DB0B6C41BF497E54172468855076BF680A003E1BBCBB57DD1AE310A8D35A699E5DA5299D9BFBC287F199CF21E5CF33DA2287969E627522A307176D68D51FB92565E526D027A4E4F4D01FF7B5CC24C0DA9B99A803D67EFCC99E8F6881ACFC48905B48E3BE9A1B5126721AFBAEB713BE6B28A0ED550F70A464D7F56E4EA84973EC35E0B8303296E6E9\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false},{\"id\":\"1.0.2\",\"pId\":\"1.0\",\"path\":\"/1/0/2\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":47,\"isContainer\":true,\"children\":[{\"id\":\"1.0.2.0\",\"pId\":\"1.0.2\",\"path\":\"/1/0/2/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010701\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"data(1.2.840.113549.1.7.1)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"data\",\"isValidator\":false},{\"id\":\"1.0.2.1\",\"pId\":\"1.0.2\",\"path\":\"/1/0/2/1\",\"tag\":{\"value\":16,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":12,\"isContainer\":true,\"children\":[{\"id\":\"1.0.2.1.0\",\"pId\":\"1.0.2.1\",\"path\":\"/1/0/2/1/0\",\"tag\":{\"value\":6,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":8,\"isContainer\":false,\"hexValue\":\"2A864886F70D0304\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"rc4(1.2.840.113549.3.4)\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"rc4\",\"isValidator\":false},{\"id\":\"1.0.2.1.1\",\"pId\":\"1.0.2.1\",\"path\":\"/1/0/2/1/1\",\"tag\":{\"value\":5,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"NULL\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false},{\"id\":\"1.0.2.2\",\"pId\":\"1.0.2\",\"path\":\"/1/0/2/2\",\"tag\":{\"value\":0,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-128]},\"tagNum\":128,\"length\":20,\"isContainer\":false,\"hexValue\":\"98C3F87E6F5C7EEA14661BF1AFBF61BE8A637EEC\",\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"CONTEXT\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}],\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"CONTEXT\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}";
        byte[] asciis = DerJsonParser.json2der(jsonData_ascii, "ASCII");
        String base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
        byte[] decode = Base64.decode(base64Der);
        System.err.println(Arrays.areEqual(asciis, decode));

        String json_iso8859 = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"tagType\":\"PRIVATE\",\"constructed\":true,\"encoding\":[-16]},\"tagNum\":240,\"length\":67,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":1,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[1]},\"tagNum\":1,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"BOOLEAN\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"BOOLEAN\",\"isValidator\":false},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"18\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"2\",\"pId\":\"\",\"path\":\"/2\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"2.0\",\"pId\":\"2\",\"path\":\"/2/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069003\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"2.1\",\"pId\":\"2\",\"path\":\"/2/1\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069002\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false},{\"id\":\"3\",\"pId\":\"\",\"path\":\"/3\",\"tag\":{\"value\":1,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-95]},\"tagNum\":161,\"length\":38,\"isContainer\":true,\"children\":[{\"id\":\"3.0\",\"pId\":\"3\",\"path\":\"/3/0\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":20,\"isContainer\":true,\"children\":[{\"id\":\"3.0.0\",\"pId\":\"3.0\",\"path\":\"/3/0/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":12,\"isContainer\":false,\"hexValue\":\"E6B581E5B9B4E7B981E58D8E\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"3.0.1\",\"pId\":\"3.0\",\"path\":\"/3/0/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.0.2\",\"pId\":\"3.0\",\"path\":\"/3/0/2\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.1\",\"pId\":\"3\",\"path\":\"/3/1\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"3.1.0\",\"pId\":\"3.1\",\"path\":\"/3/1/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"5365636F6E64\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"3.1.1\",\"pId\":\"3.1\",\"path\":\"/3/1/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"02\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.1.2\",\"pId\":\"3.1\",\"path\":\"/3/1/2\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"BOOLEAN\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"BOOLEAN\",\"isValidator\":false},{\"id\":\"4\",\"pId\":\"\",\"path\":\"/4\",\"tag\":{\"value\":3,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-125]},\"tagNum\":131,\"length\":1,\"isContainer\":false,\"hexValue\":\"28\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"BIT_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"PRIVATE\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}";


        String daa = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"tagType\":\"PRIVATE\",\"constructed\":true,\"encoding\":[-16]},\"tagNum\":240,\"length\":67,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":1,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[1]},\"tagNum\":1,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"BOOLEAN\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"BOOLEAN\",\"isValidator\":false},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"18\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"2\",\"pId\":\"\",\"path\":\"/2\",\"tag\":{\"value\":17,\"tagType\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"2.0\",\"pId\":\"2\",\"path\":\"/2/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069003\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"2.1\",\"pId\":\"2\",\"path\":\"/2/1\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069002\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SET\",\"isValidator\":false},{\"id\":\"3\",\"pId\":\"\",\"path\":\"/3\",\"tag\":{\"value\":1,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-95]},\"tagNum\":161,\"length\":38,\"isContainer\":true,\"children\":[{\"id\":\"3.0\",\"pId\":\"3\",\"path\":\"/3/0\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":20,\"isContainer\":true,\"children\":[{\"id\":\"3.0.0\",\"pId\":\"3.0\",\"path\":\"/3/0/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":12,\"isContainer\":false,\"hexValue\":\"E6B581E5B9B4E7B981E58D8E\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"3.0.1\",\"pId\":\"3.0\",\"path\":\"/3/0/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.0.2\",\"pId\":\"3.0\",\"path\":\"/3/0/2\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.1\",\"pId\":\"3\",\"path\":\"/3/1\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"3.1.0\",\"pId\":\"3.1\",\"path\":\"/3/1/0\",\"tag\":{\"value\":4,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"5365636F6E64\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"OCTET_STRING\",\"isValidator\":false},{\"id\":\"3.1.1\",\"pId\":\"3.1\",\"path\":\"/3/1/1\",\"tag\":{\"value\":2,\"tagType\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"02\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false},{\"id\":\"3.1.2\",\"pId\":\"3.1\",\"path\":\"/3/1/2\",\"tag\":{\"value\":2,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"INTEGER\",\"isValidator\":false}],\"tagName\":\"BOOLEAN\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"BOOLEAN\",\"isValidator\":false},{\"id\":\"4\",\"pId\":\"\",\"path\":\"/4\",\"tag\":{\"value\":3,\"tagType\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-125]},\"tagNum\":131,\"length\":1,\"isContainer\":false,\"hexValue\":\"28\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"CONTEXT\",\"visualValue\":\"\",\"isTemplate\":true,\"dataType\":\"byte[]\",\"nodeAlias\":\"BIT_STRING\",\"isValidator\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"PRIVATE\",\"isTemplate\":false,\"dataType\":\"byte[]\",\"nodeAlias\":\"SEQUENCE\",\"isValidator\":false}";

        IJsonQuerier jsonQuerier = new GsonQuerier();
        String path = "/3/0/0";
        String node = jsonQuerier.queryNode(daa, path);
        IJsonParser jsonParser = new GsonParser();
        StructureJsonData structureJsonData = jsonParser.decode(node, false);
        String hexValue = structureJsonData.getHexValue();

        try {
            String gbk = new String(Hex.decode(hexValue), EncodingUtil.getJVMEnconding());
            System.err.println(gbk);
            byte[] da = DerJsonParser.json2der(json_iso8859, "GBK");
            AsnDecoder asnDecoder = new BerDecoder();
            String decodeToJson = asnDecoder.decodeToJson(da, false);
            node = jsonQuerier.queryNode(decodeToJson, path);
            StructureJsonData decode1 = jsonParser.decode(node, false);
            hexValue = decode1.getHexValue();
            gbk = new String(Hex.decode(hexValue));
            System.err.println(gbk);


        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void analysisDataByReadFileTest() {
        byte[] data = FileUtil.readFile2Bytes("D:\\p7detach.txt");
        String dataJson = DerJsonParser.derDataAnalysis(data, "B");
        JsonObject asJsonObject = JsonParser.parseString(dataJson).getAsJsonObject();
        JsonElement jsonElement = asJsonObject.get("data");
        System.err.println(jsonElement.toString());

        data = FileUtil.readFile2Bytes("d:\\person.txt");
        dataJson = DerJsonParser.derDataAnalysis(data, "H");
        asJsonObject = JsonParser.parseString(dataJson).getAsJsonObject();
        jsonElement = asJsonObject.get("data");
        System.err.println(jsonElement.toString());
    }

    @Test
    public void analysisData() {
//        String enc = "MIAGCSqGSIb3DQEHBqCAMIACAQAwgAYJKoZIhvcNAQEBMAsGCSqGSIb3DQEBAYCCAQEA5jdDEiCji0qftHPvHAiZpYf2svDdmGiR1p1HJPZouVk0/XennsnpHI6cCxuB0Hc8lvR3AV/efuEzCzp3241ICxpgAvxgDaazKLrIvqEcR2irpRWRMAhR2hkjHiQlBF/fp1JKTtq49tCy7Cbu6ZnzJ5gyNvia3liRugZVi4Vgjym3/0r6TAJ81OA2BowK+BRan5fo9L+1pJMayO4ViewesvYiKavDdv1aU28H2AGSK4t5z9wClzupjzXkMzOvndVDp2a1J80dzJZCFFN1Pt0zOPPwz+eWWfCGDtrUcOLYEoig9APxulKLgriLF56Eev4vJOnV1GsChKGlo4+qTjpe/QAAAAAAAAAA";
        String enc = "MIIBKwYUMS4yLjg0MC4xMTM1NDkuMS4xLjEwDwYLBgkqhkiG9w0BAQEFAASCAQCCOjjmaR/d3Ss6NQFWiEq3mRKlxr5H6T0dw+WLX13mN+2nE+aVeOUaCVm17ExvauSh7XBow51xM7OIax1cutBa2qDtFIZE8aBe1BvcG79/RCsWvSDZL50Gswrxv/mwKod382n2K1alVSV+ia6BQRSD/pz4PMeeGIrJZLGymqkWqtwf71zGLPbOAnSS3m3e5Fgw66xfM9mIiAfJ+U24NMWBhAeNzenvpjT9r4sSnyP/lzqrtLu0P3J8RgBrXI6nMYJ0X0V5uaAwNrZfSxFzTaDdtDgIQgnSdhigmTS0QZx+M5oLkdOPTc6Zbl0shdFbncILFQxzMecB0/ZjbpXSDb3P";
        String json = DerJsonParser.der2json(Base64.decode(enc));
        System.err.println(json);
    }


    @Test
    public void asemblyP7() {
        String asyEncData = "gjo45mkf3d0rOjUBVohKt5kSpca+R+k9HcPli19d5jftpxPmlXjlGglZtexMb2rkoe1waMOdcTOziGsdXLrQWtqg7RSGRPGgXtQb3Bu/f0QrFr0g2S+dBrMK8b/5sCqHd/Np9itWpVUlfomugUEUg/6c+DzHnhiKyWSxspqpFqrcH+9cxiz2zgJ0kt5t3uRYMOusXzPZiIgHyflNuDTFgYQHjc3p76Y0/a+LEp8j/5c6q7S7tD9yfEYAa1yOpzGCdF9FebmgMDa2X0sRc02g3bQ4CEIJ0nYYoJk0tEGcfjOaC5HTj03OmW5dLIXRW53CCxUMczHnAdP2Y26V0g29zw==";
        ASN1ObjectIdentifier contentType = PKCSObjectIdentifiers.encryptedData;
        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption);

        ASN1OctetString encryptedData = new BEROctetString(Base64.decode(asyEncData));
        EncryptedContentInfo encryptedContentInfo = new EncryptedContentInfo(contentType, algorithmIdentifier, encryptedData);

        EncryptedData data = new EncryptedData(encryptedContentInfo, null);
        ContentInfo info = new ContentInfo(contentType,data);
        try {
            byte[] encoded = info.getEncoded("DL");
            System.err.println(new String(Base64.encode(encoded)));

            BerStructureBuilder structureBuilder = new BerStructureBuilder();
            Tag ptag = new Tag(16, TagType.UNIVERSAL, true);
            structureBuilder.addTag(ptag);

            BerStructureBuilder builder = new BerStructureBuilder();
            Tag tag = new Tag(6, TagType.UNIVERSAL, false);

            builder.addTag(tag);
            TlvDataReader dataReader = new BerDataReader();
            BerData dataa = dataReader.readNext(new ByteArrayInputStream(contentType.getEncoded()));

            builder.addValue(dataa.getValue());
            byte[] v = builder.build();
            structureBuilder.addValue(v);

            builder = new BerStructureBuilder();
            tag = new Tag(16, TagType.UNIVERSAL, true);
            builder.addTag(tag);

            BerStructureBuilder b = new BerStructureBuilder();
            b.addTag(new Tag(6, TagType.UNIVERSAL, false));

            BerData algId = dataReader.readNext(new ByteArrayInputStream(PKCSObjectIdentifiers.rsaEncryption.getEncoded()));

            b.addValue(algId.getValue());
            byte[] v1 = b.build();
            builder.addValue(v1);
            b = new BerStructureBuilder();
            b.addTag(new Tag(5, TagType.UNIVERSAL, false));
            byte[] v2 = b.build();
            builder.addValue(v2);
            v = builder.build();
            structureBuilder.addValue(v);

            builder = new BerStructureBuilder();
            builder.addTag(new Tag(4, TagType.UNIVERSAL, false));
            builder.addValue(Base64.decode(asyEncData));
            v = builder.build();
            structureBuilder.addValue(v);
            byte[] encData = structureBuilder.build();


            BerStructureBuilder versionbuilder = new BerStructureBuilder();
            versionbuilder.addTag(new Tag(2, TagType.UNIVERSAL, false));
            ASN1Integer version = new ASN1Integer(0);
            BerData ver = dataReader.readNext(new ByteArrayInputStream(version.getEncoded()));
            versionbuilder.addValue(ver.getValue());
            byte[] vb = versionbuilder.build();

            BerStructureBuilder ppbuilder = new BerStructureBuilder();
            ppbuilder.addTag(new Tag(16, TagType.UNIVERSAL, true));
            ppbuilder.addValue(vb);
            ppbuilder.addValue(encData);
            byte[] asyEncDataRes = ppbuilder.build();

            System.err.println(new String(Base64.encode(asyEncDataRes)));
            ASN1Primitive sequence = BERSequence.fromByteArray(asyEncDataRes);
            contentType = PKCSObjectIdentifiers.encryptedData;

            ContentInfo contentInfo = new ContentInfo(contentType,sequence);
            byte[] strucutreData = contentInfo.getEncoded("DL");
            System.err.println(new String(Base64.encode(strucutreData)));

            String json = DerJsonParser.der2json(strucutreData);
            System.err.println(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Test
    public void parseData() {
        String str = "MIIBKwYUMS4yLjg0MC4xMTM1NDkuMS4xLjEwDwYLBgkqhkiG9w0BAQEFAASCAQCCOjjmaR/d3Ss6NQFWiEq3mRKlxr5H6T0dw+WLX13mN+2nE+aVeOUaCVm17ExvauSh7XBow51xM7OIax1cutBa2qDtFIZE8aBe1BvcG79/RCsWvSDZL50Gswrxv/mwKod382n2K1alVSV+ia6BQRSD/pz4PMeeGIrJZLGymqkWqtwf71zGLPbOAnSS3m3e5Fgw66xfM9mIiAfJ+U24NMWBhAeNzenvpjT9r4sSnyP/lzqrtLu0P3J8RgBrXI6nMYJ0X0V5uaAwNrZfSxFzTaDdtDgIQgnSdhigmTS0QZx+M5oLkdOPTc6Zbl0shdFbncILFQxzMecB0/ZjbpXSDb3P";
        String json = DerJsonParser.der2json(Base64.decode(str));
        System.err.println(json);
    }

    @Test
    public void parseP7Data() {
        String str = "MIIDPwYKKoEcz1UGAQQCAqCCAy8wggMrAgEBMQ4wDAYIKoEcz1UBgxEFADAMBgoqgRzPVQYBBAIBoIICRzCCAkMwggHnoAMCAQICBSirwEsIMAwGCCqBHM9VAYN1BQAwRTELMAkGA1UEBhMCY24xIzAhBgNVBAoMGklORk9TRUMgVGVjaG5vbG9naWVzIFNNMklEMREwDwYDVQQDDAhhcHBTTTJJRDAeFw0xNTA3MDkwNjQ4NDdaFw0yMDEyMjkwNjQ4NDdaMEYxCzAJBgNVBAYTAmNuMSMwIQYDVQQKDBpJTkZPU0VDIFRlY2hub2xvZ2llcyBTTTJJRDESMBAGA1UEAwwJc20ySWR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAE1quEEjM8TjKHV0yaXfBw0mEoqwlG1p97YzAPbFx6LyZ4Ffs6X7nVvJhka5hm350Zew6dEB/5cLScr/FD7tUtJ6OBwDCBvTAfBgNVHSMEGDAWgBQUnMMn64GFFUOZIozU+IhQn+oQmzAJBgNVHRMEAjAAMGMGA1UdHwRcMFowWKBWoFSkUjBQMQ4wDAYDVQQDDAVjcmw0NzEMMAoGA1UECwwDY3JsMSMwIQYDVQQKDBpJTkZPU0VDIFRlY2hub2xvZ2llcyBTTTJJRDELMAkGA1UEBhMCY24wCwYDVR0PBAQDAgeAMB0GA1UdDgQWBBSe0vvZNv4028wPqOePm+B9rweUijAMBggqgRzPVQGDdQUAA0gAMEUCIQCrBOV8Fihr5JR3NI/OI3DbCXeN6lAYMqD6qrEaigMAKAIgGnRYivTEy66DRbbeR1Kq3/nPyha0ubkkp4g5oc3Vp6kxgbwwgbkCAQEwTjBFMQswCQYDVQQGEwJjbjEjMCEGA1UECgwaSU5GT1NFQyBUZWNobm9sb2dpZXMgU00ySUQxETAPBgNVBAMMCGFwcFNNMklEAgUoq8BLCDAMBggqgRzPVQGDEQUAMA0GCSqBHM9VAYItAQUABEcwRQIgHaZDzn99kShjiqQNly0KuSr5UF4fvlWalaBHYTYgHGkCIQCfXmQbjGalmIXMKinx2p1Qtw56SwiTQuamiYrCZJ3zOw==";
        String json = DerJsonParser.der2json(Base64.decode(str));
        System.err.println(json);
    }


}
