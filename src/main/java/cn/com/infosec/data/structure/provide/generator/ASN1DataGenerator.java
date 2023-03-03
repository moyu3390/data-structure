/**
 * ASN1Generator
 * <p> 依据SJD数据结构(即已解析保存的数据结构)，进行组装新的DER数据：根据选择的SJD节点和节点数据参数Map，为选择的SJD节点设置新数据后生成新的der数据
 * 1.0
 * <p>
 * 2023/1/3 17:04
 */

package cn.com.infosec.data.structure.provide.generator;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.encoder.AsnEncoder;
import cn.com.infosec.data.structure.asn.encoder.BerEncoder;
import cn.com.infosec.data.structure.bean.SelectNode;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.exception.StructureValidatorException;
import cn.com.infosec.data.structure.json.IJsonNodeModifier;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.editor.JsonNodeModifier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.utils.ListSortUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ASN1DataGenerator {

    private final static IJsonQuerier JSON_QUERIER = new GsonQuerier();
    private final static AsnEncoder<byte[]> ASN_ENCODER = new BerEncoder();
    private final static AsnDecoder<byte[]> ASN_DECODER = new BerDecoder();

    private final static IJsonParser JSON_PARSER = new GsonParser();

    private final static IJsonNodeModifier JSON_NODE_MODIFIER = new JsonNodeModifier();

    /**
     * 依据SJD数据结构(即已解析保存的数据结构)，进行组装新的DER数据：根据选择的SJD节点和节点数据参数Map，为选择的SJD节点设置新数据后生成新的der数据。
     * 输入参数：Map[key->已选择节点的路径；value->已选择节点的数据(TLV格式的Der编码的字节数组)]。
     * 输出参数：der编码的字节数组（TLV组装格式）。
     *
     * @param structureJsonData 选择的SJD数据结构模板
     * @param selectNodes       SJD中选择的节点
     * @param nodesMap          新数据Map[key->已选择节点的路径；value->已选择节点的数据(TLV格式的Der编码的字节数组)]。
     * @return der编码的字节数组（TLV组装格式）。
     */
    public static byte[] generatorDerData(String structureJsonData, List<SelectNode> selectNodes, Map<String, byte[]> nodesMap) {
        if (Objects.isNull(structureJsonData) || selectNodes.isEmpty() || Objects.isNull(nodesMap) || nodesMap.isEmpty()) {
            return null;
        }
        // 验证选择的sn在sjd中都存在
        validateNodesExist(structureJsonData, selectNodes);
        // 构造sn的Map,新map中的value有可能为null
        Map<String, String> nodesAndData = assembleNodesAndData(selectNodes, nodesMap);
        // 替换节点数据
        String newJsonData = JSON_NODE_MODIFIER.replaceNodesData(structureJsonData, nodesAndData);

        List<String> snPaths = nodesAndData.keySet().stream().collect(Collectors.toList());

        // 查找sn的新数据，转成对象
        List<StructureJsonData> snData = getSNDataByPaths(newJsonData, snPaths);

        // 按照节点排序
        ListSortUtil.sort(snData, "getPath", "desc");

        // 重新生成新der数据
        StructureJsonData structureJsonData1 = JSON_NODE_MODIFIER.createNewJsonData(snData);
        // 重新构造数据，根据实际数据重新定义TLV中的L
        byte[] encode = ASN_ENCODER.encode(structureJsonData1);

        return encode;
    }


    private static List<StructureJsonData> getSNData(String jsonData, List<SelectNode> selectNodes) {
        List<StructureJsonData> list = new ArrayList<>();
        selectNodes.stream().forEach(s -> {
            String selectNodeData = JSON_QUERIER.queryNode(jsonData, s.getNodePath());
            if (Objects.nonNull(selectNodeData) && selectNodeData.trim().length() > 0) {
                StructureJsonData sjd = JSON_PARSER.decode(selectNodeData, false);
                list.add(sjd);
            }
        });
        return list;
    }

    private static List<StructureJsonData> getSNDataByPaths(String jsonData, List<String> selectNodePaths) {
        List<StructureJsonData> list = new ArrayList<>();
        selectNodePaths.stream().forEach(s -> {
            String selectNodeData = JSON_QUERIER.queryNode(jsonData, s);
            if (Objects.nonNull(selectNodeData) && selectNodeData.trim().length() > 0) {
                StructureJsonData sjd = JSON_PARSER.decode(selectNodeData, false);
                list.add(sjd);
            }
        });
        return list;
    }

    /**
     * 组装节点和数据
     *
     * @param selectNodes 已选择的节点
     * @param nodesMap    传入的节点数据
     * @return
     */
    private static Map<String, String> assembleNodesAndData(final List<SelectNode> selectNodes, final Map<String, byte[]> nodesMap) {
        Map<String, String> resultMap = new HashMap<>();
        for (SelectNode sn : selectNodes) {
            String nodePath = sn.getNodePath();
            byte[] bytes = nodesMap.get(nodePath);
            if(Objects.nonNull(bytes)) {
                String nodeJsonData = ASN_DECODER.decodeToJson(bytes, nodePath, false);
                resultMap.put(nodePath, nodeJsonData);
                continue;
            }
            resultMap.put(nodePath, null);
        }
        return resultMap;
    }


    private static void validateNodesExist(String jsonData, List<SelectNode> selectNodes) {
        for (SelectNode SN : selectNodes) {
            String nodePath = SN.getNodePath();
            // 选中的节点数据
            String selectNodeData = JSON_QUERIER.queryNode(jsonData, nodePath);
            // SN节点在SJD中不存在
            if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
                throw new StructureValidatorException(nodePath + " 节点不存在");
            }
        }
    }
}
