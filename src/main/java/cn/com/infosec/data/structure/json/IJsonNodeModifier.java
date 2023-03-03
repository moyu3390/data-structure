/**
 * IJsonNodeModifier
 * <p>
 * 1.0
 * <p>
 * 2022/12/27 13:42
 */

package cn.com.infosec.data.structure.json;

import cn.com.infosec.data.structure.bean.StructureJsonData;

import java.util.List;
import java.util.Map;

public interface IJsonNodeModifier {
    public String modifyNodeValue(String jsonData, String path, byte[] nodeValue);

    public String modifyNodesValue(String jsonData, Map<String, byte[]> nodesMap);
    public String replaceNodeData(String jsonData, String path, String nodeJsonData);

    public String replaceNodesData(String jsonData, Map<String, String> nodesMap);

    public String deleteNode(String jsonData, String path);

    public String deleteNodes(String jsonData, List<String> delNodePaths);

    /**
     * 添加节点数据
     *
     * @param jsonData
     * @param targetPath
     * @param childData
     * @param order
     * @return
     */
    public String addNodeData(String jsonData, String targetPath, String childData, int order);

    /**
     * 从destJsonData中把arrtibutes属性值复制到srcJsonData中的同属性值
     * 目前仅支持结构相同的json属性复制
     *
     * @param jsonData
     * @param attributeSourcesJsonData 属性值来源json
     * @param attributes   每个节点下要复制的属性列表
     * @return
     */
    public String copyAttributes(String jsonData, String attributeSourcesJsonData, List<String> attributes);

    /**
     * 组装新数据，暂时只支持到二级节点
     * @param sns
     * @return
     */
    public StructureJsonData createNewJsonData(List<StructureJsonData> sns);

}
