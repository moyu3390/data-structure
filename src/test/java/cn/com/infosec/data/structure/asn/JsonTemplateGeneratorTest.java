/**
 * TemplageGeneratorTest
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 16:58
 */

package cn.com.infosec.data.structure.asn;

import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.data.structure.provide.generator.JsonTemplateGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonTemplateGeneratorTest {

    JsonTemplateGenerator generator = new JsonTemplateGenerator();

    @Test
    public void templageGeneratorTest() {
//        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";
//        base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
//
//        byte[] decode1 = Base64.decode(base64Der);
//
//        String s = generator.generateToJson(decode1);
//        System.err.println(s);

        final byte[] ber = HexUtils.decode("F03C0101FF020118311004063859980690030406385998069002A11FA20D040546697273740201018201FFA20E04065365636F6E64020102820100830128");

        String s = generator.generateToJson(ber);
        System.err.println(s);

        JsonObject jsonObject = JsonParser.parseString(s).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strr = gson.toJson(jsonObject);
        System.err.println(strr);
        writeFile(strr);

    }


    private void writeFile(String context) {
        try {
            String path = JsonTemplateGeneratorTest.class.getClassLoader().getResource("").getPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + File.separator + "person.json"));
            writer.write(context);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
