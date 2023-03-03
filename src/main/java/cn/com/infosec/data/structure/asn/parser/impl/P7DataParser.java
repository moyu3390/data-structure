/**
 * P7DataParser
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 17:26
 */

package cn.com.infosec.data.structure.asn.parser.impl;

import cn.com.infosec.data.structure.asn.parser.DataType;
import cn.com.infosec.data.structure.asn.parser.IDataTypeParser;
import cn.com.infosec.jce.provider.fastparser.*;

import java.util.Arrays;
import java.util.Objects;

public class P7DataParser implements IDataTypeParser {
    @Override
    public DataType parseData(byte[] data) {
        try {
            Object dataObject = getDataObject(data);
            // 判断结果 并转换为DataType类型返回
            return convert(dataObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataType.UNKNOW_DATA;
    }


    public Object getDataObject(byte[] data) {
        Item tmpitem = new Item(0, data.length, 1);
        int position = 0;
        int offset = 2;
        DerUtil.computeOffset(data, tmpitem, tmpitem.offset, position);
        position = tmpitem.offset;
        int contentType = DerUtil.getContentType(data, position + offset);
//        System.err.println(contentType);
        return getParsedContentInfo(contentType);
    }


    public Class<?> getParsedContentInfo(int contentType) {
        switch (contentType) {
            case 1:
                return RawData.class;
            case 2:
                return SignedData.class;
            case 3:
                return EnvelopedData.class;
            case 4:
                return SignedAndEnvelopedData.class;
            case 5:
                return DigestedData.class;
            case 6:
                return EncryptedData.class;
            default:
                return null;
        }
    }


    private static DataType convert(Object data) {
        if (Objects.isNull(data)) {
            return DataType.UNKNOW_DATA;
        }
        if (data instanceof Class) {
            String simpleName = ((Class) data).getSimpleName().toLowerCase();
            DataType dataType = Arrays.stream(DataType.values()).filter(d -> d.getDataType().toLowerCase().contains(simpleName)).findFirst().orElse(DataType.UNKNOW_DATA);
            return dataType;
        }
        String simpleName = data.getClass().getSimpleName().toLowerCase();
        DataType dataType = Arrays.stream(DataType.values()).filter(d -> d.getDataType().toLowerCase().contains(simpleName)).findFirst().orElse(DataType.UNKNOW_DATA);
        return dataType;
    }

//    public static void main(String[] args) {
//        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";
//        String string = "";
//        byte[] decode1 = Base64.decode(base64Der);
//        Object dataObject = getDataObject(decode1);

//        String asyencBase64Data = "PsCd3491i/Qjm/E2wVG9ZNfJCyUaQff0z0XnmT9vEPIuD67hWuCkCMzAjRF50ioTtKEh20/TaL+Lol1g+jnAxqLP4ZKP1yJh/+s2YyULxHjyYjE5nzr//rDKi6CO8KvM2udRMhdNGXo/pcBl4qpbYSy6COVgTSTvqdBq+J9v5aH/RPM4NtOaJD6bIvUll9dJ0m0CzG+5ttrPmdrwMLjwNac6q88BjalJ9mtA4LHBmPn3tQg1urYFsFnmJLlgpAYFn4TB2ujnZoWzFmav3nwKquDEynrKqipbK0ishORBWUIBE7OgPbeFSKEO9cv0ow2T4rYojK0B6YLTTvbIcHRnaw==";
//        decode1 = Base64.decode(asyencBase64Data);
//        Object dataObject1 = getDataObject(decode1);
//        System.err.println(dataObject1);

//        String enc = "MIIFHAYJKoZIhvcNAQcDoIIFDTCCBQkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0GGWpI4hs04bf0oUTfMA0GCSqGSIb3DQEBAQUABIIBALZbg2UcPq6NlVZeDqWv/OkVASiAUQoqQ/RQ3dkqne2o0ee2hftzT4jafTFvaWgIK3Kfhs7pbX1bLvjmQqcq8sfi3HUd/lluzhSsxF8ch+FOBWL+O8IgSMS45Qn7Al7/d1dcJQv9jP0eRb1hcS5b0Qh3Dfw9MZyDQo10Im55MrtNs6vyMUidEP1iioCLjQUT0Wq9zHN2UFWPhghmS2AUBSWcXM5BieMiIXLA4sZPmTouBOPykrfRmlhjOASsfu9FpR00NzXnxroToyYNG9VZ/hDTpCwINHoVub4zoG5Lllpf5/PPWVuyN7dDAyMPPf/Kk+tJ+DDWJePcRJvVnq2JUI0wggOdBgkqhkiG9w0BBwEwDAYIKoZIhvcNAwQFAICCA4AL/SrZ2hJz2+Zehpq0l/Eeedjx9llTBbA8LXZ12iFoAcj8ZmKWxy9fnYqBeRbnNY0F2WijptC/SVNrHTmDTHNudBEmdqmiSvJ1h975+TaqTNFsRXd8eu1bb9UwuqVaBO2OZYZhxgcNrsmmBzgBI2JybK1BXqJaF/dbJV7Gw62OZ+vmogVc+4AVyxM5asY6TUN6rZEXWOc4fwECYeSrsmvjsItb6dzqGc2BO6Z1KX4VRkQJ0xTnCj3qf6jRff2UaRSxPfyN4gh5tB7hdN0hJ46jSLJ5l056FFpSyxa12Sy6olNYFG1mvS13tCgTPljCTQX6fAqFHcl8oaN7cdBf2jTBK8rg0MLyGgXhZFimKKRbR0NE2DHsPpPouapMJko8/8fy7jU//McHWFCSMFu47bbvd+VG9/D3RDcJqlcFv81ub01E5ekEXbf4vZgS6tvEaTHdX7vJlUwr4u+Y0r1RW9ru/boNCfMnfQYYIYWz2UIwgJSXrBrrWFK1lSj9hX3+PzZVxZ4QvrxlkBJf0UA04grE90XgqS1AbiSCBdU5O17v78UX57vPUt2IXfLD2xhSU+uhxTUx0WqfMdmwHXBW1gdf9nqI2A3pAEE/IIC+bCbLwzoNkkjfBiAE3Loo5uJQS8TtSTJkkAzTLJnQSGocX8wzB2VMi71CjTtJUndPNS4AAcKNxRm8SOT1pbat29W/meKsI1XYZq3qvLA4h2hp4kvrgcZ09L9Ysc1x9l4oBhoGPRv6JVKElWNOv9YK8dITZC3m8FAUP+imesqkyxQiMBejWoKFcmAevaM6TpLM89VZU2dTKSyn0iI6ECkYzo7ALLYyINIY7aN4+gAXTWocCo7TDrPDpY1U9jGV1h5ZHc5lvJ6qdRqut2Dx+rCzK2FBFqszHokfUAtLl9lhNobjDnFhFMpIlyv1oTlseYobwIucKBHsSZVWnsUkgn+49HLsgHrGbsh0b/kRI2a0Y5oloIHbswQjay/ujlyVN2b4W7x5+0PtJciW/IX+xhtXeg62es4NpvjVGLguLYoDXtyQLWpsIStKVLfjARCF+at/RE/ksVcIZpOzy4FbJTN7wwsrbF/sI0sBkjRNSs8C+a3eXJjCCnBrOx1fynlGDnk5TSCW2ekQdA9zlQvV/O4rGL7eVVD/dnryvXQejEfHUIyc1zCNj+cdigVd8/y4/VjS4ANx2w==";
//        enc = "MIIBrAYJKoZIhvcNAQcDoIIBnTCCAZkCAQAxggFhMIIBXQIBADBFMDQxCzAJBgNVBAYTAkNOMRMwEQYDVQQKEwpJTkZPU0VDIENBMRAwDgYDVQQLEwdJTkZPIENBAg0Azj5gLn2uI2VmVm+LMA0GCSqGSIb3DQEBAQUABIIBAKWg27o/V0F5ZJvO8EmqD1P0lFrnQqTyJeCokhO3S44iOQsX5baBgT/zkFWNaRzG4kYD+6weuD62pgFdGb4HQhiFd9/RF556YMpvVhE69We8WEcFafnWE0GJgvzWTfm4NwyMpy13SEze21QQ3+uQ4KCmfFMWJs5lYNsLbEG/SX5UFyRohVB2v2gKAD4bvLtX3RrjEKjTWmmeXaUpnZv7wofxmc8h5c8z2iKHlp5idSKjBxdtaNUfuSVl5SbQJ6Tk9NAf97XMJMDam5moA9Z+/Mmej2iBrPxIkFtI476aG1Emchr7rrcTvmsooO1VD3CkZNf1bk6oSXPsNeC4MDKW5ukwLwYJKoZIhvcNAQcBMAwGCCqGSIb3DQMEBQCAFJjD+H5vXH7qFGYb8a+/Yb6KY37s";
//        decode1 = Base64.decode(enc);
//        Object dataObject = getDataObject(decode1);
//        System.err.println(dataObject);

//    }


}
