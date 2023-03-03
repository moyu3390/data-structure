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
//import cn.com.infosec.data.structure.asn1.exceptions.ParseException;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// *
// * @author <a href="mailto:Martin.Bosslet@googlemail.com">Martin Bosslet</a>
// */
//public class Utils {
//
//    private Utils() {}
//
//    public static byte[] consume(InputStream stream) {
//
//        byte[] buf = new byte[8192];
//        int read = 0;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        try {
//            while ((read = stream.read(buf)) != -1) {
//                baos.write(buf, 0, read);
//            }
//        }
//        catch (IOException ex) {
//                throw new ParseException(ex);
//        }
//
//        return baos.toByteArray();
//    }
//
//    public static byte[] bytesOf(Integer... bytes) {
//        byte[] ret = new byte[bytes.length];
//        for (int i = 0; i < bytes.length; i++) {
//            ret[i] = bytes[i].byteValue();
//        }
//        return ret;
//    }
//
//    public static byte[] byteTimes(int b, int times) {
//        byte[] ret = new byte[times];
//        for (int i = 0; i < times; i++) {
//            ret[i] = (byte)b;
//        }
//        return ret;
//    }
//}
