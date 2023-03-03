/**
 * MapGetter
 * <p> 依据SJD数据结构(即已解析保存的数据结构)，进行解析DER数据，根据选择的SJD节点，获取DER中对应的数据
 * 1.0
 * <p>
 * 2023/1/3 16:18
 */

package cn.com.infosec.data.structure.provide.getter;

import cn.com.infosec.data.structure.bean.SelectNode;
import cn.com.infosec.data.structure.bean.StructureJsonData;

import java.util.*;

public class MapGetter extends DataQuerier {

    /**
     * 依据SJD数据结构(即已解析保存的数据结构)，进行解析DER数据，根据选择的SJD节点，获取DER中对应的数据。
     * 输入参数：一般是经过MapParser控件解析得到，输入参数数据结构是Map[key->节点的路径；value->节点的数据(TLV格式的Der编码的字节数组)]。
     * 输出参数：Map[key->已选择节点的路径；value->已选择节点的数据(TLV格式的Der编码的字节数组)]。
     *
     * @param structureJsonData
     * @param selectNodes
     * @param nodesMap          一般是经过MapParser控件解析得到，输入参数数据结构是Map[key->节点的路径；value->节点的数据(TLV格式的Der编码的字节数组)]。
     * @return Map[key->已选择节点的路径；value->已选择节点的数据(TLV格式的Der编码的字节数组)]。
     */
    public static Map<String, byte[]> mapGetter(String structureJsonData, List<SelectNode> selectNodes, Map<String, byte[]> nodesMap) {
        Map<String, byte[]> resultMap = new HashMap<>();

        //从json串中解析节点列表中的节点内容，把节点内容替换后，重新生成der编码
        if (Objects.isNull(structureJsonData) || Objects.isNull(selectNodes) || selectNodes.isEmpty()) {
            return resultMap;
        }
        for (SelectNode selectNode : selectNodes) {
            String nodePath = selectNode.getNodePath();
            // 选中的节点数据
            String selectNodeData = JSON_QUERIER.queryNode(structureJsonData, nodePath);
            // SN节点在SJD中不存在
            if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
                resultMap.put(nodePath, null);
                continue;
            }
            if (!nodesMap.containsKey(nodePath)) {
//                byte[] encode = ASN_ENCODER.encode(selectNodeData);
                resultMap.put(nodePath, null);
                continue;
            }
            // 入参中传入的节点新数据
            byte[] newData = nodesMap.get(nodePath);
            StructureJsonData newJD = ASN_DECODER.decode(newData, false);
            byte[] encode = ASN_ENCODER.encode(newJD);
            if (Arrays.equals(newData, encode)) {
                resultMap.put(nodePath, newData);
            }
        }
        return resultMap;
    }
}
