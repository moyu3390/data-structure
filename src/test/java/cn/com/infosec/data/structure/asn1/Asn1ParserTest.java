///***** BEGIN LICENSE BLOCK *****
//* Version: CPL 1.0/GPL 2.0/LGPL 2.1
//*
//* The contents of this file are subject to the Common Public
//* License Version 1.0 (the "License"); you may not use this file
//* except in compliance with the License. You may obtain a copy of
//* the License at http://www.eclipse.org/legal/cpl-v10.html
//*
//* Software distributed under the License is distributed on an "AS
//* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
//* implied. See the License for the specific language governing
//* rights and limitations under the License.
//*
//* Copyright (C) 2011 Martin Bosslet <Martin.Bosslet@googlemail.com>
//*
//* Alternatively, the contents of this file may be used under the terms of
//* either of the GNU General Public License Version 2 or later (the "GPL"),
//* or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
//* in which case the provisions of the GPL or the LGPL are applicable instead
//* of those above. If you wish to allow use of your version of this file only
//* under the terms of either the GPL or the LGPL, and not to allow others to
//* use your version of this file under the terms of the CPL, indicate your
//* decision by deleting the provisions above and replace them with the notice
//* and other provisions required by the GPL or the LGPL. If you do not delete
//* the provisions above, a recipient may use your version of this file under
//* the terms of any one of the CPL, the GPL or the LGPL.
// */
//package cn.com.infosec.data.structure.asn1;
//
//import cn.com.infosec.data.structure.asn1.constants.Tags;
//import cn.com.infosec.data.structure.asn1.encode.Asn1Serializer;
//import cn.com.infosec.data.structure.asn1.parser.Asn1Parser;
//import cn.com.infosec.data.structure.asn1.resources.Resources;
//import cn.com.infosec.data.structure.asn1.tag.TagClass;
//import cn.com.infosec.v160.util.encoders.Base64;
//import cn.com.infosec.v160.util.encoders.Hex;
//import org.junit.Assert;
//import org.junit.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import static cn.com.infosec.data.structure.asn1.Utils.byteTimes;
//import static cn.com.infosec.data.structure.asn1.Utils.bytesOf;
//import static org.junit.Assert.*;
///**
// *
// * @author <a href="mailto:Martin.Bosslet@googlemail.com">Martin Bosslet</a>
// */
//public class Asn1ParserTest {
//
//    @Test
//    public void parseConstructed() throws Exception {
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
////        InputStream in = Resources.certificate();
//        String hexDer = "F0390101FF020118311085063859980690038506385998069002A11F300D040546697273740201018201FF300E04065365636F6E64020102820100";
//        byte[] decode = Hex.decode(hexDer);
//
//        String base64Der = "MIIEowYJKoZIhvcNAQcCoIIElDCCBJACAQExCzAJBgUrDgMCGgUAMBkGCSqGSIb3DQEHAaAMBApsc2RqZmxkc2pmoIIC8TCCAu0wggHVoAMCAQICDQWrFjORh4tqrLu9o3IwDQYJKoZIhvcNAQELBQAwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0EwHhcNMjIxMTEzMDYzMjEwWhcNMjMwMTEyMDYzMjEwWjA0MQswCQYDVQQGEwJDTjETMBEGA1UEChMKSU5GT1NFQyBDQTEQMA4GA1UECxMHSU5GTyBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJCAbdlXkN5qlDX0Mi2kyO4AldzmcEfhCGn9cr88gJzUsAPZOjFdo9X2SZUMJcC+CXTXczCr+o2xBzWppI0dbeIQU6T0+nGSJRXRkqOHXkERIuOoyCKLLVzGM5sD0J66n863sPVyYmwLIe+XDOXnf/2u+TamC82Mf0O+MNNZ8LjjVIOFd84Bl0im80I0Wd3CcH+9EILMMjsFS3VBQCAL/JBIgoBPEn587JsQOnvNGOYm6m7A45NMd+g/rUIqTTuFlsCQw4gKch+CF/CjOzo52IWuOYwf9wkai75DR5Mh09iibRK1qNnFWTD/JlhlhMhGC/uv1o13UpqIOeGkHwbbyucCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAcNE4dZtNIpcfMAkJvi6216ysBuNsWtaW6szbgLGINP5pe2YY0cDK8CAWY1WXQAl0Nwx+Jcj/rCEwvUgAidSROhbgrsWSo5vW6qJzr4g47T/xxeUF4jnDQIzAzAy64KY7Dxrs55YGzUT1b6pHs57NXM4Jhcxjh/RRN2M1MRYsM8rvv7T1JlFdF2pIXJLVT+rkLY9TYYgCBJkq5+/6jxAXDkeb3OEEs/dDxwQf+y1ZM+nBxLdiobe5CpwXKdRqJwvzhaN42INHxxLvoSAcltQWLJBCJf/nzYXZbh7CEgjGDaBbO51HbKvFZfnYcbs9OdTYcBqBC4nBCV4pnlfA6EM25zGCAWwwggFoAgEBMEUwNDELMAkGA1UEBhMCQ04xEzARBgNVBAoTCklORk9TRUMgQ0ExEDAOBgNVBAsTB0lORk8gQ0ECDQWrFjORh4tqrLu9o3IwCQYFKw4DAhoFADANBgkqhkiG9w0BAQEFAASCAQB4ekua91e46jR5sL7oUykgFrnGiqQnINFskkFoyFaykyKETAH+16fHDLT7qaTTzahnI46n9bbpUByzj68sQPOwMM0EU75LGsXYWGJccLNnVSzFBn9M6vj06SzUuCxUR2XrORhOiU7oWltDtY+zHnafkd6ph+EgKHGcx7gvbTh7AB/4bYF1TDPw2yc3GTB5kdHBTtMLoz8rSIK/uUnCXU4mhyY9KrmZR5IdeigpjuqGB5e5YhSsq8yuyMkWyESCFNnV7wdLxm+MZOJdgeer1LTDzbEJ3NIs0aKCMh/TcOA13taiKVFLBR2mKzfkHdFBtASnhIgN3MyLg4tElmDHTsEF";
//
//        byte[] decode1 = Base64.decode(base64Der);
//
//
//        InputStream in = new ByteArrayInputStream(decode1);
//        try {
//            Asn1 asn = p.parse(in);
//            assertNotNull(asn);
//            assertTrue(asn instanceof Constructed);
//            Constructed cons = (Constructed)asn;
//            Iterable<Asn1> contents = cons.getContent();
//            assertNotNull(contents);
//            assertTrue(contents.iterator().hasNext());
//        }
//        finally {
//            in.close();
//        }
//    }
//
//    @Test
//    public void parseEncodeEquality() throws Exception {
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = Resources.certificate();
//
//        try {
//            byte[] raw = Resources.read(Resources.certificate());
//            Asn1 asn = p.parse(new ByteArrayInputStream(raw));
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Asn1Serializer.serialize(asn, baos);
//            assertArrayEquals(raw, baos.toByteArray());
//        }
//        finally {
//            in.close();
//        }
//    }
//
//    @Test
//    public void primitiveParse() {
//        byte[] raw = bytesOf(0x02,0x02,0x01,0x00);
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        Assert.assertEquals(Tags.INTEGER, h.getTag());
//        assertEquals(TagClass.UNIVERSAL, h.getTagClass());
//        assertFalse(h.isConstructed());
//        assertFalse(h.isInfiniteLength());
//        assertEquals(2, h.getLength());
//        assertEquals(2, h.getHeaderLength());
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//
//        assertArrayEquals(raw, result);
//    }
//
//    @Test
//    public void constructedEncode() {
//        byte[] raw = bytesOf(0x30,0x06,0x04,0x01,0x01,0x04,0x01,0x02);
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        assertEquals(Tags.SEQUENCE, h.getTag());
//        assertEquals(TagClass.UNIVERSAL, h.getTagClass());
//        assertTrue(h.isConstructed());
//        assertFalse(h.isInfiniteLength());
//        assertEquals(6, h.getLength());
//        assertEquals(2, h.getHeaderLength());
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//        assertArrayEquals(raw, result);
//    }
//
//    @Test
//    public void complexLength() throws IOException{
//        byte[] raw = bytesOf(0x04,0x82,0x03,0xe8);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(raw);
//        baos.write(byteTimes(0x00, 1000));
//        raw = baos.toByteArray();
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        assertEquals(Tags.OCTET_STRING, h.getTag());
//        assertEquals(TagClass.UNIVERSAL, h.getTagClass());
//        assertFalse(h.isConstructed());
//        assertFalse(h.isInfiniteLength());
//        assertEquals(1000, h.getLength());
//        assertEquals(4, h.getHeaderLength());
//
//        baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//        assertArrayEquals(raw, result);
//    }
//
//    @Test
//    public void complexTagSingleOctet() throws IOException {
//        byte[] raw = bytesOf(0xdf,0x2a,0x01);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(raw);
//        baos.write(bytesOf(0x00));
//        raw = baos.toByteArray();
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        assertEquals(42, h.getTag());
//        assertEquals(TagClass.PRIVATE, h.getTagClass());
//        assertFalse(h.isConstructed());
//        assertFalse(h.isInfiniteLength());
//        assertEquals(1, h.getLength());
//        assertEquals(3, h.getHeaderLength());
//
//        baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//        assertArrayEquals(raw, result);
//    }
//
//    @Test
//    public void complexTagTwoOctets() throws IOException {
//        byte[] raw = bytesOf(0x5f,0x82,0x2c,0x01);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(raw);
//        baos.write(bytesOf(0x00));
//        raw = baos.toByteArray();
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        assertEquals(300, h.getTag());
//        assertEquals(TagClass.APPLICATION, h.getTagClass());
//        assertFalse(h.isConstructed());
//        assertFalse(h.isInfiniteLength());
//        assertEquals(1, h.getLength());
//        assertEquals(4, h.getHeaderLength());
//
//        baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//        assertArrayEquals(raw, result);
//    }
//
//    @Test
//    public void infiniteLengthParsing() throws IOException {
//        byte[] raw = bytesOf(0x24,0x80,0x04,0x01,0x01,0x04,0x01,0x02,0x00,0x00);
//
//        Asn1Parser p = new Asn1Parser(new ParserFactory());
//        InputStream in = new ByteArrayInputStream(raw);
//        Asn1 asn1 = p.parse(in);
//        Header h = asn1.getHeader();
//        assertEquals(Tags.OCTET_STRING, h.getTag());
//        assertEquals(TagClass.UNIVERSAL, h.getTagClass());
//        assertTrue(h.isConstructed());
//        assertTrue(h.isInfiniteLength());
//        assertEquals(-1, h.getLength());
//        assertEquals(2, h.getHeaderLength());
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Asn1Serializer.serialize(asn1, baos);
//        byte[] result = baos.toByteArray();
//        assertArrayEquals(raw, result);
//    }
//
//}
