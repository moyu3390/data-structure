/**
 * ASN1DataParser
 * <p> 依据SJD数据结构(即已解析保存的数据结构)，进行解析DER数据，根据选择的SJD节点，获取DER中对应的数据。
 * 1.0
 * <p>
 * 2023/1/3 16:28
 */

package cn.com.infosec.data.structure.provide.parser;

import cn.com.infosec.data.structure.bean.SelectNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ASN1DataParser extends DerJsonParser {

    /**
     * 依据SJD数据结构(即已解析保存的数据结构)，进行解析DER数据，根据选择的SJD节点，获取DER中对应的数据。
     * 输入参数：der编码的字节数组（TLV组装格式）。
     * 输出参数：Map[key->已选择节点的路径；value->已选择节点的数据(TLV格式的Der编码的字节数组)]。
     *
     * @param structureJsonData 控件页面中选择的SJD
     * @param selectNodes       已选择的SJD中保存的SN
     * @param derData           asn1数据
     * @return
     */
    public static Map<String, byte[]> parseSelectNodesValue(String structureJsonData, List<SelectNode> selectNodes, byte[] derData) {
        Map<String, byte[]> resultMap = new HashMap<>();
        if (Objects.isNull(structureJsonData) || selectNodes.isEmpty() || Objects.isNull(derData)) {
            return resultMap;
        }
        List<String> paths = selectNodes.stream().map(s -> s.getNodePath()).collect(Collectors.toList());
        Map<String, byte[]> nodeValueReferTemplateByPaths = getNodeValueReferTemplateByPaths(structureJsonData, derData, paths);
        return nodeValueReferTemplateByPaths;
    }
}
