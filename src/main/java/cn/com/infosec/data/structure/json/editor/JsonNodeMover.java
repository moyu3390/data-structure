/**
 * JsonNodeMover
 * <p> json节点移动类
 * 1.0
 * <p>
 * 2022/12/27 15:38
 */

package cn.com.infosec.data.structure.json.editor;

import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.UniversalTags;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.exception.StructureException;
import cn.com.infosec.data.structure.json.IJsonNodeMover;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JsonNodeMover implements IJsonNodeMover {

    GsonQuerier gsonQuerier = new GsonQuerier();
    JsonNodeModifier jsonNodeModifier = new JsonNodeModifier();

    public static Map<String, Integer> ASN1PKCS_TAGS = new HashMap<>();

    static {
        ASN1PKCS_TAGS.put("SEQUENCE", UniversalTags.SEQUENCE);
        ASN1PKCS_TAGS.put("SEQUENCE", UniversalTags.SEQUENCE_OF);
        ASN1PKCS_TAGS.put("SET", UniversalTags.SET);
        ASN1PKCS_TAGS.put("SET", UniversalTags.SET_OF);
    }

    /**
     * 移动节点：目标节点不能是叶子节点和非容器类型的节点
     *
     * @param jsonData   json数据
     * @param srcPath    待移动的节点
     * @param targetPath 目标节点
     * @param order      待移动节点在目标节点中的开始顺序，从0开始；负数-代表不指定，默认放置最后
     * @return
     */
    @Override
    public String moveNode(String jsonData, String srcPath, String targetPath, int order) {
        // 检查被移动节点类型--只能移动叶子节点
        // 取出要移动的节点数据
        String srcNodeJson = checkAndReturnSrcNode(jsonData, srcPath);
        // 检查目标节点类型
        checkTargetNode(jsonData, targetPath);
        // 在原数据中删除,得到删除后的json数据，存为临时变量
        String jsonTempData = jsonNodeModifier.deleteNode(jsonData, srcPath);
        // todo 把取出的节点数据，添加到目标节点中
        String newJsonData = jsonNodeModifier.addNodeData(jsonTempData, targetPath, srcNodeJson, order);
        return newJsonData;
    }

    /**
     * 检查目标节点的结构类型是否为结构体，数据类型为容器类型
     *
     * @param jsonData
     * @param targetPath 目标节点路径
     */
    private void checkTargetNode(String jsonData, String targetPath) {
        StructureJsonData targetSJD = gsonQuerier.query(jsonData, targetPath);
        String targetJsonData = gsonQuerier.queryNode(jsonData, targetPath);
        boolean leafNode = isLeafNode(targetJsonData);
        boolean isContext = targetSJD.getContainer();
        if (leafNode) {
            throw new StructureException("目标节点不能是叶子节点");
        }

        if(!isContext) {
            Tag tlvTag = targetSJD.getTag();
            String key = ASN1PKCS_TAGS.entrySet().stream().filter(m -> Objects.equals(m.getValue(), tlvTag.getValue())).map(Map.Entry::getKey).findFirst().orElse(null);
            if (Objects.isNull(key)) {
                throw new StructureException("目标节点不是容器类型");
            }
        }
    }

    /**
     * 检查被移动节点是否为叶子节点，检查通过，返回被移动节点数据
     *
     * @param jsonData
     * @param srcNodePath 被移动节点路径
     */
    private String checkAndReturnSrcNode(String jsonData, String srcNodePath) {
        String srcNodeJson = gsonQuerier.queryNode(jsonData, srcNodePath);
        checkSrcNode(srcNodeJson);
        return srcNodeJson;
    }

    /**
     * 检查被移动节点是否为叶子节点
     *
     * @param srcNodeJson 被移动节点数据
     */
    private void checkSrcNode(String srcNodeJson) {
        if (!isLeafNode(srcNodeJson)) {
            throw new StructureException("要移动的节点不是叶子节点");
        }
    }

    private static boolean isLeafNode(String jsonData) {
        // 判断数据中是否包含children节点：有-结构数据；无-叶子节点
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        boolean children = jsonObject.asMap().containsKey("children");
        boolean isContainer = jsonObject.get("isContainer").getAsBoolean();
        boolean isHasHexValue = jsonObject.asMap().containsKey("hexValue");
        return !isContainer && !children && isHasHexValue;
    }

}
