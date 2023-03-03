/**
 * TTT
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 11:32
 */

package cn.com.infosec.data.structure.template;

import cn.com.infosec.data.structure.utils.FileUtil;

public class TTT {
    public static void main(String[] args) {


        String attributeTemplateFileName = "P7SignedData.json";
        String destJsonData = FileUtil.readInternalFileContext2String(attributeTemplateFileName);
        System.err.println(destJsonData);
        byte[] bytes = FileUtil.readInternalFileContext2Bytes(attributeTemplateFileName);
        System.err.println(new String(bytes));
    }
}
