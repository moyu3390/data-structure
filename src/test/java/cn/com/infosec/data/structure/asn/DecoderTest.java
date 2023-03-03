package cn.com.infosec.data.structure.asn;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.model.*;
import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.v160.util.encoders.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DecoderTest {
    private final AsnDecoder<byte[]> decoder = new BerDecoder();

    @Test
    public void shouldDecodeTest() {
        final byte[] ber = HexUtils.decode("F03C0101FF020118311004063859980690030406385998069002A11FA20D040546697273740201018201FFA20E04065365636F6E64020102820100830128");
        final Object decoded = decoder.decode(ber, false);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(decoded);

        System.err.println(json);
        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";

        base64Der = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";


        byte[] decode1 = Base64.decode(base64Der);
        json = decoder.decodeToJson(decode1, false);
        System.err.println(json);
    }

    @Test
    public void shouldDecodeMultipleAddressStingsAndDiscardTheExtraOne() {
        final byte[] encoded = HexUtils.decode("302aa40c0201010404616472318201ffa40c020102040461647232820100a40c020103040461647233820100");
        final MultipleAddressWrapper decoded = decoder.decode(MultipleAddressWrapper.class, encoded);
        assertThat(decoded.getAddressOne()).isEqualTo(new Address("adr1", 1, true));
        assertThat(decoded.getAddressTwo()).isEqualTo(new Address("adr2", 2, false));
    }

    @Test
    public void shouldDecodePolymorphicType() {
        final byte[] encodedA = HexUtils.decode("300D810161A208A106810101820102");
        final EventWrapper wrapperA = decoder.decode(EventWrapper.class, encodedA);
        assertThat(wrapperA.getId()).isEqualTo("a");
        assertThat(wrapperA.getEvent()).isInstanceOf(EventA.class);
        assertThat(wrapperA.getEvent().getValue()).isEqualTo(1);
        assertThat(((EventA) wrapperA.getEvent()).getNumber()).isEqualTo(2);

        final byte[] encodedB = HexUtils.decode("300D810162A208A2068101FF820102");
        final EventWrapper wrapperB = decoder.decode(EventWrapper.class, encodedB);
        assertThat(wrapperB.getId()).isEqualTo("b");
        assertThat(wrapperB.getEvent()).isInstanceOf(EventB.class);
        assertThat(wrapperB.getEvent().getValue()).isEqualTo(2);
        assertThat(((EventB) wrapperB.getEvent()).isEnabled()).isTrue();
    }

    @Test
    public void shouldDecodePolymorphicCollection() {
        final byte[] encoded = HexUtils.decode("301D810101A218A106810101820102A106810103820104A2068101FF820102");
        System.err.println(new String(Base64.encode(encoded)));
        final EventListWrapper wrapper = decoder.decode(EventListWrapper.class, encoded);
        assertThat(wrapper.getId()).isEqualTo(1);
        assertThat(wrapper.getEvents()).containsExactlyInAnyOrder(
                new EventA(1, 2),
                new EventA(3, 4),
                new EventB(true, 2)
        );
    }
}
