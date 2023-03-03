/**
 * JsonNodeAttributesEditor
 * <p> 节点属性修改
 * 1.0
 * <p>
 * 2023/1/10 16:16
 */

package cn.com.infosec.data.structure.json.editor;

import cn.com.infosec.data.structure.exception.StructureException;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonNodeAttributesEditor {

    private IJsonQuerier querier = new GsonQuerier();


    public String addAttributes(String jsonData, List<String> attributes) {

        JsonElement srcJsonElement = JsonParser.parseString(jsonData);
        // 遍历json，每个节点添加属性，已存在则忽略，不存在则添加
        if (srcJsonElement.isJsonObject()) {
            JsonObject asJsonObject = srcJsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String path = asJsonObject.get("path").getAsString();
            // 查询属性列表，挨个覆盖值
            addAttributes(asJsonObject, attributes);
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                addNodeAttributesByJsonArray(jsonArray, attributes);
            }
        } else if (srcJsonElement.isJsonArray()) {
            JsonArray jsonArray = srcJsonElement.getAsJsonArray();
            addNodeAttributesByJsonArray(jsonArray, attributes);
        } else {
            throw new StructureException("jsonElement 是" + srcJsonElement.getClass() + "类型");
        }
        return srcJsonElement.toString();
    }

    private void addNodeAttributesByJsonArray(JsonArray jsonArray, List<String> attributes) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String path = asJsonObject.get("path").getAsString();

                // 查询属性列表，挨个覆盖值
                addAttributes(asJsonObject, attributes);
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    addNodeAttributesByJsonArray(asJsonArray, attributes);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }


    /**
     * 添加属性
     *
     * @param srcJsonObject
     * @param attributes
     */
    private void addAttributes(JsonObject srcJsonObject, List<String> attributes) {
        for (String at : attributes) {
            JsonElement jsonElement = srcJsonObject.get(at);
            if (Objects.nonNull(jsonElement)) {
                continue;
            }
            srcJsonObject.addProperty(at, "");
        }
    }

    /**
     * 节点复制属性(每个叶子节点的属性都会复制)
     *
     * @param destJsonData             属性值目标对象（复制后需要填充的对象）
     * @param attributeSourcesJsonData 属性值来源对象
     * @param attributesMap            k-->v; k:填充对象的属性；v:开源对象的属性
     * @param isCopyLeafOnly           是否只复制叶子节点的属性
     * @return
     */
    public String copyAttributes(String destJsonData, String attributeSourcesJsonData, Map<String, String> attributesMap, boolean isCopyLeafOnly) {
        JsonElement srcJsonElement = JsonParser.parseString(destJsonData);
        // 遍历json，每个节点复制属性
        if (srcJsonElement.isJsonObject()) {
            JsonObject asJsonObject = srcJsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String path = asJsonObject.get("path").getAsString();
            if (isCopyLeafOnly) {
                if (!isTrue) {
                    String targetNode = querier.queryNode(attributeSourcesJsonData, path);
                    JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
                    // 查询属性列表，挨个覆盖值
                    copyAttributes(asJsonObject, targetNodeJsonObject, attributesMap);
                }
            } else {
                String targetNode = querier.queryNode(attributeSourcesJsonData, path);
                JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
                // 查询属性列表，挨个覆盖值
                copyAttributes(asJsonObject, targetNodeJsonObject, attributesMap);
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                copyNodeAttributesByJsonArray(jsonArray, attributeSourcesJsonData, attributesMap, isCopyLeafOnly);
            }
        } else if (srcJsonElement.isJsonArray()) {
            JsonArray jsonArray = srcJsonElement.getAsJsonArray();
            copyNodeAttributesByJsonArray(jsonArray, attributeSourcesJsonData, attributesMap, isCopyLeafOnly);
        } else {
            throw new StructureException("jsonElement 是" + srcJsonElement.getClass() + "类型");
        }
        return srcJsonElement.toString();
    }

    private void copyNodeAttributesByJsonArray(JsonArray jsonArray, String destJsonData, Map<String, String> attributesMap, boolean isCopyLeafOnly) {
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement jsonElement = jsonArray.get(i);
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String path = asJsonObject.get("path").getAsString();

                if (isCopyLeafOnly) {
                    if (!isTrue) {
                        String targetNode = querier.queryNode(destJsonData, path);
                        JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
                        // 查询属性列表，挨个覆盖值
                        copyAttributes(asJsonObject, targetNodeJsonObject, attributesMap);
                    }
                } else {
                    String targetNode = querier.queryNode(destJsonData, path);
                    JsonObject targetNodeJsonObject = JsonParser.parseString(targetNode).getAsJsonObject();
                    // 查询属性列表，挨个覆盖值
                    copyAttributes(asJsonObject, targetNodeJsonObject, attributesMap);
                }

                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    copyNodeAttributesByJsonArray(asJsonArray, destJsonData, attributesMap, isCopyLeafOnly);
                }
            } else {
                throw new StructureException("不是JsonObject对象");
            }
        }
    }

    /**
     * 覆写属性
     *
     * @param srcJsonObject    属性值目标对象（复制后需要填充的对象）
     * @param targetJsonObject 属性值来源对象
     * @param attributesMap    k-->v; k:填充对象的属性；v:开源对象的属性
     */
    private void copyAttributes(JsonObject srcJsonObject, JsonObject targetJsonObject, Map<String, String> attributesMap) {

        attributesMap.entrySet().forEach(m -> {
            String key = m.getKey();
            String value = m.getValue();
            JsonElement jsonElement = targetJsonObject.get(value);
            if (Objects.nonNull(jsonElement)) {
                srcJsonObject.add(key, jsonElement);
            }
        });
    }

}
