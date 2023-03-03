/**
 * JsonNodeModifier
 * <p> 数据节点修改类
 * 1.0
 * <p>
 * 2022/12/26 10:28
 */

package cn.com.infosec.data.structure.json.editor;

import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.TagType;
import cn.com.infosec.data.structure.asn.validator.impl.Asn1StrucutreSimpleValidatorImpl;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.exception.StructureException;
import cn.com.infosec.data.structure.json.IJsonComparator;
import cn.com.infosec.data.structure.json.IJsonNodeModifier;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.comparator.JsonComparator;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.utils.NodePathUtil;
import cn.com.infosec.v160.util.encoders.Hex;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonNodeModifier implements IJsonNodeModifier {
    private IJsonParser parser = new GsonParser();

    private IJsonQuerier querier = new GsonQuerier();

    private IJsonComparator jsonComparator = new JsonComparator();


    @Override
    public String modifyNodeValue(String jsonData, String path, byte[] nodeValue) {
        if (Objects.isNull(nodeValue)) {
            return "";
        }
        // 不是叶子节点不允许更改值
        String queryNode = querier.queryNode(jsonData, path);
        boolean leafNode = NodePathUtil.isLeafNode(queryNode);
        if (!leafNode) {
            throw new StructureException(path + " 不是叶子节点");
        }

        // 校验是否为结构数据
        boolean isStructure = true;
        Asn1StrucutreSimpleValidatorImpl validator = new Asn1StrucutreSimpleValidatorImpl();
        try {
            validator.validator(nodeValue);
        } catch (Exception e) {
            isStructure = false;
        }
        // 是否为结构体
        if(isStructure) {
            throw new StructureException("叶子节点载荷不能为结构体");
        }

        String newDataJson = Hex.toHexString(nodeValue);

        JsonElement jsonElement = JsonParser.parseString(jsonData);
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String tempPath = asJsonObject.get("path").getAsString();
            if (path.equals(tempPath) && !isTrue) {
                // 直接返回新数据
                asJsonObject.addProperty("hexValue", newDataJson);
                return asJsonObject.toString();
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                modifyNodeValueByJsonArray(jsonArray, path, newDataJson);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            modifyNodeValueByJsonArray(jsonArray, path, newDataJson);
        } else {
            System.out.println("jsonElement 是" + jsonElement.getClass() + "类型");
        }
        return jsonElement.toString();
    }

    public void modifyNodeValueByJsonArray(JsonArray jsonArray, String path, String newData) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String tempPath = asJsonObject.get("path").getAsString();
                if (path.equals(tempPath) && !isTrue) {
                    // 递归定位，定位成功则替换
                    asJsonObject.addProperty("hexValue", newData);
                    return;
                }
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    modifyNodeValueByJsonArray(asJsonArray, path, newData);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }




    @Override
    public String modifyNodesValue(String jsonData, Map<String, byte[]> nodesMap) {
        Iterator<Map.Entry<String, byte[]>> iterator = nodesMap.entrySet().iterator();
        String json = jsonData;
        for (; iterator.hasNext(); ) {
            Map.Entry<String, byte[]> next = iterator.next();
            String path = next.getKey();
            byte[] newNodeValue = next.getValue();
            if (Objects.nonNull(newNodeValue)) {
                json = modifyNodeValue(json, path, newNodeValue);
            }
        }
        return json;
    }

    /**
     * 根据SJD结构，修改单个叶子节点内容
     *
     * @param jsonData     原json数据
     * @param path         要修改的节点path
     * @param nodeJsonData 节点的新数据
     * @return
     */
    public String replaceNodeData(String jsonData, String path, String nodeJsonData) {
        if (Objects.isNull(nodeJsonData)) {
            return "";
        }
        String newDataJson = nodeJsonData;

        JsonElement jsonElement = JsonParser.parseString(jsonData);
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String tempPath = asJsonObject.get("path").getAsString();
            if (path.equals(tempPath)) {
                // 直接返回新数据
                jsonElement = JsonParser.parseString(newDataJson);
                return jsonElement.toString();
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                replaceNodeDataByJsonArray(jsonArray, path, newDataJson);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            replaceNodeDataByJsonArray(jsonArray, path, newDataJson);
        } else {
            System.out.println("jsonElement 是" + jsonElement.getClass() + "类型");
        }

        return jsonElement.toString();
    }

    /**
     * 批量修改节点内容
     *
     * @param jsonData 原json
     * @param nodesMap 要修改的节点path以及对应的新内容
     * @return
     */
    @Override
    public String replaceNodesData(String jsonData, Map<String, String> nodesMap) {
        Iterator<Map.Entry<String, String>> iterator = nodesMap.entrySet().iterator();
        String json = jsonData;
        for (; iterator.hasNext(); ) {
            Map.Entry<String, String> next = iterator.next();
            String path = next.getKey();
            String newNodeData = next.getValue();
            if (Objects.nonNull(newNodeData)) {
                json = replaceNodeData(json, path, newNodeData);
            }
        }
        return json;
    }

    /**
     * 删除对应节点
     *
     * @param jsonData
     * @param path
     * @return
     */
    @Override
    public String deleteNode(String jsonData, String path) {
        StringBuffer buffer = new StringBuffer();
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        String result = null;
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String tempPath = asJsonObject.get("path").getAsString();
            if (path.equals(tempPath)) {
                return "";
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                deleteNodeDataByJsonArray(jsonArray, path);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            deleteNodeDataByJsonArray(jsonArray, path);
        } else {
            throw new StructureException("jsonElement 是" + jsonElement.getClass() + "类型");
        }
        if (!Objects.isNull(result) && result.trim().length() > 0) {
            buffer.append(result);
        }
        return jsonElement.toString();
    }

    @Override
    public String deleteNodes(String jsonData, List<String> delNodePaths) {
        String json = jsonData;
        for (String path : delNodePaths) {
            json = deleteNode(json, path);
        }
        return json;
    }

    public void replaceNodeDataByJsonArray(JsonArray jsonArray, String path, String newData) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String tempPath = asJsonObject.get("path").getAsString();
                if (path.equals(tempPath)) {
                    // 递归定位，定位成功则替换
                    jsonArray.set(i, JsonParser.parseString(newData));
                    return;
                }
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    replaceNodeDataByJsonArray(asJsonArray, path, newData);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }

    public void deleteNodeDataByJsonArray(JsonArray jsonArray, String path) {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement jsonElement = iterator.next();
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String tempPath = asJsonObject.get("path").getAsString();
                if (path.equals(tempPath)) {
                    // 递归定位，定位成功则替换
                    iterator.remove();
                    return;
                }
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    deleteNodeDataByJsonArray(asJsonArray, path);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }

    public String addNodeData(String jsonData, String targetPath, String childData, int order) {
        StringBuffer buffer = new StringBuffer();
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        String result = null;
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String tempPath = asJsonObject.get("path").getAsString();
            if (targetPath.equals(tempPath)) {
                addChildDataToNode(childData, targetPath, order, asJsonObject);
                return jsonElement.toString();
            }
            boolean isTrue = asJsonObject.keySet().contains("children");
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                addNodeDataByJsonArray(jsonArray, targetPath, childData, order);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            addNodeDataByJsonArray(jsonArray, targetPath, childData, order);
        } else {
            throw new StructureException("jsonElement 是" + jsonElement.getClass() + "类型");
        }
        if (!Objects.isNull(result) && result.trim().length() > 0) {
            buffer.append(result);
        }
        return jsonElement.toString();
    }

    @Override
    public String copyAttributes(String jsonData, String attributeSourcesJsonData, List<String> attributes) {
        // 复制前先比较两个数据的结构是否一致。
        boolean compare = jsonComparator.compare(jsonData, attributeSourcesJsonData, true);
        if (!compare) {
            throw new StructureException("Json结构不一致,复制属性失败");
        }
        JsonElement srcJsonElement = JsonParser.parseString(jsonData);
        // 遍历json，每个节点复制属性
        if (srcJsonElement.isJsonObject()) {
            JsonObject asJsonObject = srcJsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String path = asJsonObject.get("path").getAsString();
            String targetNode = querier.queryNode(attributeSourcesJsonData, path);
            JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
            // 查询属性列表，挨个覆盖值
            writeAttributes(asJsonObject, targetNodeJsonObject, attributes);

            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                writeNodeAttributesByJsonArray(jsonArray, attributeSourcesJsonData, attributes);
            }
        } else if (srcJsonElement.isJsonArray()) {
            JsonArray jsonArray = srcJsonElement.getAsJsonArray();
            writeNodeAttributesByJsonArray(jsonArray, attributeSourcesJsonData, attributes);
        } else {
            throw new StructureException("jsonElement 是" + srcJsonElement.getClass() + "类型");
        }
        return srcJsonElement.toString();
    }

    private void writeNodeAttributesByJsonArray(JsonArray jsonArray, String destJsonData, List<String> attributes) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String path = asJsonObject.get("path").getAsString();
                String targetNode = querier.queryNode(destJsonData, path);
                JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
                // 查询属性列表，挨个覆盖值
                writeAttributes(asJsonObject, targetNodeJsonObject, attributes);
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    writeNodeAttributesByJsonArray(asJsonArray, destJsonData, attributes);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }


    /**
     * 覆写属性
     *
     * @param srcJsonObject
     * @param targetJsonObject
     * @param attributes
     */
    private void writeAttributes(JsonObject srcJsonObject, JsonObject targetJsonObject, List<String> attributes) {
        for (String at : attributes) {
            JsonElement jsonElement = targetJsonObject.get(at);
            if (Objects.isNull(jsonElement)) {
                continue;
            }
            srcJsonObject.add(at, jsonElement);
        }
    }


    /**
     * @param jsonArray
     * @param targetPath 目标路径
     * @param childData  要添加的内容
     * @param order      在目标节点中的顺序，从0开始
     */
    public void addNodeDataByJsonArray(JsonArray jsonArray, String targetPath, String childData, int order) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                String tempPath = asJsonObject.get("path").getAsString();
                if (targetPath.equals(tempPath)) {
                    // 递归定位，定位成功则添加
                    // 找到目标节点的子节点，没有则新创建
                    addChildDataToNode(childData, targetPath, order, asJsonObject);
                    return;
                }
                boolean isTrue = asJsonObject.keySet().contains("children");
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    addNodeDataByJsonArray(asJsonArray, targetPath, childData, order);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }

    private void addChildDataToNode(String childData, String targetPath, int order, JsonObject asJsonObject) {
        if (!asJsonObject.asMap().containsKey("children")) {
            asJsonObject.add("children", new JsonArray());
        }
        JsonElement tempJsonElement = asJsonObject.get("children");
        if (Objects.isNull(tempJsonElement)) {
            JsonArray childArray = new JsonArray();
            tempJsonElement = childArray;
        }
        JsonArray childTempArray = tempJsonElement.getAsJsonArray();
        // 向数组中添加数据
        int childSize = childTempArray.size();
        if (order < 0 || order > childSize - 1) {
            // 加在最后位置
            order = childSize;
        }
        // json数据重新构造path和id
        String childJsonData = parser.rebuildNodePathByParentPath(childData, targetPath, order + "");
        childTempArray.asList().add(order, JsonParser.parseString(childJsonData));
        asJsonObject.addProperty("isContainer", true);
    }


    public StructureJsonData createNewJsonData(List<StructureJsonData> sns) {
        if (Objects.isNull(sns) || sns.isEmpty()) {
            return null;
        }
        StructureJsonData structureJsonData = new StructureJsonData();
        structureJsonData.setpId("");
        structureJsonData.setPath("");
        structureJsonData.setId("");
        structureJsonData.setNodeAlias("Root");
        structureJsonData.setTagNum(48);
        byte[] encoding = {48};
        Tag tag = new Tag(16, TagType.UNIVERSAL, true, encoding);
        structureJsonData.setTagClass(TagType.UNIVERSAL.name());
        structureJsonData.setContainer(true);
        structureJsonData.setTagName("SEQUENCE");
        structureJsonData.setTemplate(false);
        structureJsonData.setDataType("byte[]");
        structureJsonData.setValidator(false);
        structureJsonData.setTag(tag);
        structureJsonData.setChildren(sns);
        return structureJsonData;
    }


}
