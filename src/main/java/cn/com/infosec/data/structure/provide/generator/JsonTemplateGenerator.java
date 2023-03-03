/**
 * JsonTemplateGenerator
 * <p> 模板生成
 * 1.0
 * <p>
 * 2022/12/29 16:46
 */

package cn.com.infosec.data.structure.provide.generator;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.bean.StructureJsonData;

public class JsonTemplateGenerator {
    final AsnDecoder<byte[]> decoder = new BerDecoder();

    public StructureJsonData generate(byte[] input) {
        return decoder.decode(input, true);
    }

    public StructureJsonData generate(byte[] input, String currentNodePath) {
        return decoder.decode(input, currentNodePath, true);
    }

    public StructureJsonData generate(byte[] input, String currentNodeParentPath, String currentNodeNum) {
        return decoder.decode(input, currentNodeParentPath, currentNodeNum, true);
    }

    public String generateToJson(byte[] input) {
        return decoder.decodeToJson(input, true);
    }

    public String generateToJson(byte[] input, String currentNodePath) {
        return decoder.decodeToJson(input, currentNodePath, true);
    }

    public String generateToJson(byte[] input, String currentNodeParentPath, String currentNodeNum) {
        return decoder.decodeToJson(input, currentNodeParentPath, currentNodeNum, true);
    }
}
