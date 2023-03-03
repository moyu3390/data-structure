/**
 * EncodingUtil
 * <p>
 * 1.0
 * <p>
 * 2023/1/9 15:38
 */

package cn.com.infosec.data.structure.utils;

import cn.com.infosec.data.structure.exception.StructureException;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.Objects;

public class EncodingUtil {
    static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 获取字节数组编码类型
     *
     * @param bytes 文件bytes数组
     * @return 编码类型
     */
    public static String getEncoding(byte[] bytes) {
        if (Objects.isNull(bytes) || bytes.length == 0) {
            return getJVMEnconding();
        }
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        return encoding;
    }

    public static String getJVMEnconding() {
        String property = System.getProperty("file.encoding");
        if (Objects.isNull(property) || property.trim().length() == 0) {
            return DEFAULT_ENCODING;
        }
        return property;
    }

    public static byte[] encodeToEncoding(byte[] data, String encoding) {
        if (Objects.isNull(data) || data.length == 0) {
            return data;
        }
        if (Objects.isNull(encoding) || encoding.trim().length() < 1) {
            return data;
        }
        String encodeType = getEncoding(data);
        try {
            if (encodeType.equalsIgnoreCase(encoding)) {
                return data;
            }
            System.err.println("原始编码：" + encodeType);
            System.err.println("指定编码：" + encoding);
            InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream(data), encoding);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            int ch;
            while ((ch = inputStreamReader.read()) != -1) {
                outputStreamWriter.write(ch);
                outputStreamWriter.flush();
            }
            byte[] d = out.toByteArray();

            String t = new String(data, encoding);
            return t.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new StructureException(e);
        } catch (Exception e) {
            throw new StructureException(e);
        }
    }


}