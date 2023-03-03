/**
 * MapSetter
 * <p> 选择通过数据解析后保存的数据结构的节点，根据输入的数据，为选择的节点重新生成新的节点内容
 * 1.0
 * <p>
 * 2023/1/3 14:16
 */

package cn.com.infosec.data.structure.provide.setter;

import cn.com.infosec.data.structure.bean.SelectNode;
import cn.com.infosec.data.structure.bean.StructureJsonData;

import java.util.*;

public class MapSetter extends DataEditor {

    /**
     * 选择通过数据解析后保存的数据结构的节点，根据输入的数据，为选择的节点重新生成新的节点内容。
     * 输入参数：一般是经过指令输出得到的TLV格式的Der编码的字节数组。
     * 输出参数：Map[key->已选择节点的路径；value->已选择节点的数据]。
     *
     * @param structureJsonData
     * @param selectNodes
     * @param nodesValueMap
     * @return
     */
    public static Map<String, byte[]> mapSetter(String structureJsonData, List<SelectNode> selectNodes, Map<String, byte[]> nodesValueMap) {
        Map<String, byte[]> resultMap = new HashMap<>();

        //从json串中解析节点列表中的节点内容，把节点内容替换后，重新生成der编码
        if (Objects.isNull(structureJsonData) || Objects.isNull(selectNodes) || selectNodes.isEmpty()) {
            return resultMap;
        }
        for (SelectNode selectNode : selectNodes) {
            String nodePath = selectNode.getNodePath();
            // 选中的节点数据
            String selectNodeData = JSON_QUERIER.queryNode(structureJsonData, nodePath);
            // 如果选中的节点在结构中不存在，设置值为null
            if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
                resultMap.put(nodePath, null);
                continue;
            }
            // 如果选中的节点在入参中不存在，设置值为null
            if (!nodesValueMap.containsKey(nodePath)) {
//                byte[] encode = ASN_ENCODER.encode(selectNodeData);
                resultMap.put(nodePath, null);
                continue;
            }
            // 入参中选中的节点新数据
            byte[] newData = nodesValueMap.get(nodePath);
            StructureJsonData newJD = ASN_DECODER.decode(newData, false);
            byte[] encode = ASN_ENCODER.encode(newJD);
            if (Arrays.equals(newData, encode)) {
                resultMap.put(nodePath, newData);
            }
        }
        return resultMap;
    }
}
