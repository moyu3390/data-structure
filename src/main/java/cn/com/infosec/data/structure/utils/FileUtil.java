/**
 * FileUtil
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 18:12
 */

package cn.com.infosec.data.structure.utils;


import cn.com.infosec.data.structure.exception.StructureException;

import java.io.*;
import java.util.Objects;

public class FileUtil {


    /**
     * 读取jar包内部文件内容
     *
     * @param filePath
     * @return
     */
    public static String readInternalFileContext2String(String filePath) {
        BufferedReader bufferedReader = null;
        try {
            InputStream resourceAsStream = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            InputStreamReader reader = new InputStreamReader(resourceAsStream);
            bufferedReader = new BufferedReader(reader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new StructureException(e);
        } finally {
            if (Objects.nonNull(bufferedReader)) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * 读取jar包内部文件内容
     *
     * @param filePath
     * @return
     */
    public static byte[] readInternalFileContext2Bytes(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = FileUtil.class.getClassLoader().getResourceAsStream(filePath);
            if (Objects.isNull(inputStream)) {
                return null;
            }
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            return data;
        } catch (IOException e) {
            throw new StructureException(e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }


    public static String readFile2String(String fileAbstractPath) {
        RandomAccessFile file = null;
        try {
            String path = fileAbstractPath;
            file = new RandomAccessFile(path, "r");
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = file.readLine()) != null) {
                buffer.append(str);
            }
            return buffer.toString();
        } catch (IOException e) {
            throw new StructureException(e);
        } finally {
            if (!Objects.isNull(file)) {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static byte[] readFile2Bytes(String fileAbstractPath) {
        RandomAccessFile file = null;
        try {
            String path = fileAbstractPath;
            file = new RandomAccessFile(path, "r");
            String str = null;
            Long length = file.length();
            byte[] data = new byte[length.intValue()];
            file.read(data);
            return data;
        } catch (Exception e) {
            throw new StructureException(e);
        } finally {
            if (!Objects.isNull(file)) {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
