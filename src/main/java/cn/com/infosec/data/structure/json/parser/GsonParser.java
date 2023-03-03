/**
 * GosnParser
 * <p> Gson 解析器
 * 1.0
 * <p>
 * 2022/12/26 11:01
 */

package cn.com.infosec.data.structure.json.parser;

import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.exception.StructureException;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.utils.EncodingUtil;
import cn.com.infosec.data.structure.utils.NodePathUtil;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class GsonParser<T> implements IJsonParser<T> {
    Gson gson = new GsonBuilder()/*.disableHtmlEscaping()*/.create();

    @Override
    public StructureJsonData decode(String jsonData, boolean onlyStructure) {
        return decode(jsonData, null, onlyStructure);
    }

    @Override
    public StructureJsonData decode(String jsonData, String encoding, boolean onlyStructure) {
        if (Objects.isNull(jsonData) || jsonData.trim().length() == 0) {
            return null;
        }
        try {
            String data = jsonData;
            if (Objects.nonNull(encoding) && encoding.trim().length() > 0) {
                byte[] bytes = jsonData.getBytes(encoding);
                data = new String(bytes, EncodingUtil.getJVMEnconding());
//                System.err.println("解码后："+data);
            }
            StructureJsonData structureJsonData = gson.fromJson(data, StructureJsonData.class);
            return structureJsonData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            StructureJsonData structureJsonData = gson.fromJson(jsonData, StructureJsonData.class);
            return structureJsonData;
        }
    }


    @Override
    public String encode(T t) {
        return encode(t, null);
    }

    @Override
    public String encode(T t, String encoding) {
        if (Objects.isNull(t)) {
            return null;
        }
        String json = gson.toJson(t);
        try {
            if (Objects.nonNull(encoding) && encoding.trim().length() > 0) {
                json = new String(json.getBytes(EncodingUtil.getJVMEnconding()), encoding);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public Map<String, Object> innerMap(String jsonData) {
        return null;
    }

    @Override
    public Map<String, Object> innerMap(StructureJsonData structureJsonData) {
        return null;
    }

    @Override
    public String rebuildNodePath(String jsonData) {
        if (Objects.isNull(jsonData) || jsonData.trim().length() == 0) {
            throw new StructureException("Cannot rebuild null data into: " + jsonData);
        }
        return scanEveryNodeAndGenPath(jsonData, "", null);
    }

    @Override
    public String rebuildNodePathByParentPath(String jsonData, String parentPath) {
        if (Objects.isNull(jsonData) || jsonData.trim().length() == 0) {
            throw new StructureException("Cannot rebuild null data into: " + jsonData);
        }
        String currentNodeNum = null;
        if (!Objects.isNull(parentPath) && parentPath.trim().length() > 0) {
            currentNodeNum = "0";
        }
        return rebuildNodePathByParentPath(jsonData, parentPath, currentNodeNum);
    }

    @Override
    public String rebuildNodePathByParentPath(String jsonData, String parentPath, String currentNodeNum) {
        log.info("重新构造path");
        if (Objects.isNull(jsonData) || jsonData.trim().length() == 0) {
            throw new StructureException("Cannot rebuild null data into: " + jsonData);
        }
        return scanEveryNodeAndGenPath(jsonData, parentPath, currentNodeNum);
    }


    /**
     * 扫描每一个节点并生成路径和id
     *
     * @param jsonData
     * @param parentPath
     * @param currentNodeNum
     * @return
     */
    private String scanEveryNodeAndGenPath(String jsonData, String parentPath, String currentNodeNum) {
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        String parentId = NodePathUtil.path2Id(parentPath);
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String nodeId = NodePathUtil.genNodeId(parentId, currentNodeNum);
            String path = NodePathUtil.getPathByNodeId(nodeId);
            asJsonObject.addProperty("id", nodeId);
            asJsonObject.addProperty("pId", parentId);
            asJsonObject.addProperty("path", path);

            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                scanEveryNodeAndGenPathByJsonArray(jsonArray, nodeId);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            scanEveryNodeAndGenPathByJsonArray(jsonArray, parentId);
        } else if (jsonElement.isJsonNull()) {
            JsonNull jsonNull = jsonElement.getAsJsonNull();
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        }
        return jsonElement.toString();
    }

    public void scanEveryNodeAndGenPathByJsonArray(JsonArray jsonArray, String parentId) {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            JsonElement jsonElement = iterator.next();
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");

                String nodeId = NodePathUtil.genNodeId(parentId, count + "");
                String path = NodePathUtil.getPathByNodeId(nodeId);
                asJsonObject.addProperty("id", nodeId);
                asJsonObject.addProperty("pId", parentId);
                asJsonObject.addProperty("path", path);

                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    scanEveryNodeAndGenPathByJsonArray(asJsonArray, nodeId);
                }
            }
            count++;
        }
    }


    public static void main(String[] args) {
        String jsonData = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"type\":\"PRIVATE\",\"constructed\":true,\"encoding\":[-16]},\"tagNum\":240,\"length\":60,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":1,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[1]},\"tagNum\":1,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"BOOLEAN\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"18\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"2\",\"pId\":\"\",\"path\":\"/2\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"2.0\",\"pId\":\"2\",\"path\":\"/2/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069003\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"2.1\",\"pId\":\"2\",\"path\":\"/2/1\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069002\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"3\",\"pId\":\"\",\"path\":\"/3\",\"tag\":{\"value\":1,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-95]},\"tagNum\":161,\"length\":31,\"isContainer\":true,\"children\":[{\"id\":\"3.0\",\"pId\":\"3\",\"path\":\"/3/0\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"3.0.0\",\"pId\":\"3.0\",\"path\":\"/3/0/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":5,\"isContainer\":false,\"hexValue\":\"4669727374\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.0.1\",\"pId\":\"3.0\",\"path\":\"/3/0/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.0.2\",\"pId\":\"3.0\",\"path\":\"/3/0/2\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"3.1\",\"pId\":\"3\",\"path\":\"/3/1\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"3.1.0\",\"pId\":\"3.1\",\"path\":\"/3/1/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"5365636F6E64\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.1.1\",\"pId\":\"3.1\",\"path\":\"/3/1/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"02\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.1.2\",\"pId\":\"3.1\",\"path\":\"/3/1/2\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false}],\"tagName\":\"BOOLEAN\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"4\",\"pId\":\"\",\"path\":\"/4\",\"tag\":{\"value\":3,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-125]},\"tagNum\":131,\"length\":1,\"isContainer\":false,\"hexValue\":\"28\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"PRIVATE\",\"isTemplate\":false}";
        String parentPath = "/5/5/5";
        GsonParser parser = new GsonParser();
        String s = parser.rebuildNodePathByParentPath(jsonData, parentPath, "9");
        System.err.println(s);
    }

}
