/**
 * GsonQuerier
 * <p>
 * 1.0
 * <p>
 * 2022/12/26 11:36
 */

package cn.com.infosec.data.structure.json.querier;

import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class GsonQuerier implements IJsonQuerier {
    @Override
    public StructureJsonData query(String jsonData, String path) {
        return queryNode2SJD(jsonData, path);
    }

    @Override
    public StructureJsonData query(StructureJsonData structureJsonData, String path) {
        return queryNode2SJD(structureJsonData, path);
    }

    private StructureJsonData queryNode2SJD(String jsonData, String path) {
        Gson gson = new Gson();
        StructureJsonData structureJsonData = gson.fromJson(jsonData, StructureJsonData.class);
        return queryNode2SJD(structureJsonData, path);
    }

    private StructureJsonData queryNode2SJD(StructureJsonData structureJsonData, String path) {
        List<StructureJsonData> child = new ArrayList<>();
        getChildren(structureJsonData, path, child);
        System.err.println(child);
        return child.size() > 0 ? child.get(0) : null;
    }

    private void getChildren(StructureJsonData structureJsonData, String path, List<StructureJsonData> child) {
        boolean isContainer = structureJsonData.getContainer();
        if (isContainer) {
            List<StructureJsonData> children = structureJsonData.getChildren();
            if (children.size() > 0) {
                for (StructureJsonData sjd : children) {
                    getChildren(sjd, path, child);
                }
            }
        }
        if (structureJsonData.getPath().equals(path)) {
            child.add(structureJsonData);
        }
    }

    private String queryByJsonArray(JsonArray jsonArray, String path) {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        StringBuffer buffer = new StringBuffer();
        String result = null;
        while (iterator.hasNext()) {
            JsonElement jsonElement = iterator.next();
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String tempPath = asJsonObject.get("path").getAsString();
                if (path.equals(tempPath)) {
                    result = asJsonObject.toString();
                    return result;
                }
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    result = queryByJsonArray(asJsonArray, path);
                    if (!Objects.isNull(result) && result.trim().length() > 0) {
                        buffer.append(result);
                    }
                }
            }
        }
        return buffer.toString();
    }


    public String queryNode(String data, String path) {
        StringBuffer buffer = new StringBuffer();
        JsonElement jsonElement = JsonParser.parseString(data);
        String result = null;
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String tempPath = asJsonObject.get("path").getAsString();
            if (path.equals(tempPath)) {
                result = asJsonObject.toString();
                return result;
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                result = queryByJsonArray(jsonArray, path);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            result = queryByJsonArray(jsonArray, path);

        } else if (jsonElement.isJsonNull()) {
            JsonNull jsonNull = jsonElement.getAsJsonNull();
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        }
        if (!Objects.isNull(result) && result.trim().length() > 0) {
            buffer.append(result);
        }
        return buffer.toString();
    }


    private String queryByIdentityJsonArray(JsonArray jsonArray, String identity) {
        Iterator<JsonElement> iterator = jsonArray.iterator();
        StringBuffer buffer = new StringBuffer();
        String result = null;
        while (iterator.hasNext()) {
            JsonElement jsonElement = iterator.next();
            if (jsonElement.isJsonObject()) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                boolean isTrue = asJsonObject.keySet().contains("children");
                String tempIdentity = asJsonObject.get("nodeIdentity").getAsString();
                if (identity.equals(tempIdentity)) {
                    result = asJsonObject.toString();
                    return result;
                }
                if (isTrue) {
                    JsonArray asJsonArray = asJsonObject.get("children").getAsJsonArray();
                    result = queryByIdentityJsonArray(asJsonArray, identity);
                    if (!Objects.isNull(result) && result.trim().length() > 0) {
                        buffer.append(result);
                    }
                }
            }
        }
        return buffer.toString();
    }


    public String queryNodeByIdentity(String data, String identity) {
        StringBuffer buffer = new StringBuffer();
        JsonElement jsonElement = JsonParser.parseString(data);
        String result = null;
        if (jsonElement.isJsonObject()) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            boolean isTrue = asJsonObject.keySet().contains("children");
            String tempIdentity = asJsonObject.get("nodeIdentity").getAsString();
            if (identity.equals(tempIdentity)) {
                result = asJsonObject.toString();
                return result;
            }
            if (isTrue) {
                JsonArray jsonArray = asJsonObject.get("children").getAsJsonArray();
                result = queryByIdentityJsonArray(jsonArray, identity);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            result = queryByIdentityJsonArray(jsonArray, identity);

        } else if (jsonElement.isJsonNull()) {
            JsonNull jsonNull = jsonElement.getAsJsonNull();
        } else if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
        }
        if (!Objects.isNull(result) && result.trim().length() > 0) {
            buffer.append(result);
        }
        return buffer.toString();
    }







    public static void main(String[] args) {
        GsonQuerier gsonQuerier = new GsonQuerier();
        String jsonData = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"type\":\"PRIVATE\",\"constructed\":true,\"encoding\":[-16]},\"tagNum\":240,\"length\":60,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":1,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[1]},\"tagNum\":1,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"BOOLEAN\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"18\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"2\",\"pId\":\"\",\"path\":\"/2\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"2.0\",\"pId\":\"2\",\"path\":\"/2/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069003\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"2.1\",\"pId\":\"2\",\"path\":\"/2/1\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"385998069002\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"3\",\"pId\":\"\",\"path\":\"/3\",\"tag\":{\"value\":1,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-95]},\"tagNum\":161,\"length\":31,\"isContainer\":true,\"children\":[{\"id\":\"3.0\",\"pId\":\"3\",\"path\":\"/3/0\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"3.0.0\",\"pId\":\"3.0\",\"path\":\"/3/0/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":5,\"isContainer\":false,\"hexValue\":\"4669727374\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.0.1\",\"pId\":\"3.0\",\"path\":\"/3/0/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.0.2\",\"pId\":\"3.0\",\"path\":\"/3/0/2\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"FF\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"3.1\",\"pId\":\"3\",\"path\":\"/3/1\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-94]},\"tagNum\":162,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"3.1.0\",\"pId\":\"3.1\",\"path\":\"/3/1/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":6,\"isContainer\":false,\"hexValue\":\"5365636F6E64\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.1.1\",\"pId\":\"3.1\",\"path\":\"/3/1/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"02\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"3.1.2\",\"pId\":\"3.1\",\"path\":\"/3/1/2\",\"tag\":{\"value\":2,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-126]},\"tagNum\":130,\"length\":1,\"isContainer\":false,\"hexValue\":\"00\",\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"INTEGER\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false}],\"tagName\":\"BOOLEAN\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"4\",\"pId\":\"\",\"path\":\"/4\",\"tag\":{\"value\":3,\"type\":\"CONTEXT\",\"constructed\":false,\"encoding\":[-125]},\"tagNum\":131,\"length\":1,\"isContainer\":false,\"hexValue\":\"28\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"CONTEXT\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"PRIVATE\",\"isTemplate\":false}";
        String path = "/3/0/1";
        String s = gsonQuerier.queryNode(jsonData, path);
        System.err.println(s);
        jsonData = "{\"id\":\"\",\"pId\":\"\",\"path\":\"\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":1187,\"isContainer\":true,\"children\":[{\"id\":\"0\",\"pId\":\"\",\"path\":\"/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010702\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"signedData(1.2.840.113549.1.7.2)\",\"isTemplate\":true},{\"id\":\"1\",\"pId\":\"\",\"path\":\"/1\",\"tag\":{\"value\":0,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-96]},\"tagNum\":160,\"length\":1172,\"isContainer\":true,\"children\":[{\"id\":\"1.0\",\"pId\":\"1\",\"path\":\"/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":1168,\"isContainer\":true,\"children\":[{\"id\":\"1.0.0\",\"pId\":\"1.0\",\"path\":\"/1/0/0\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1.0.1\",\"pId\":\"1.0\",\"path\":\"/1/0/1\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":11,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0\",\"pId\":\"1.0.1\",\"path\":\"/1/0/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.1.0.0\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":5,\"isContainer\":false,\"hexValue\":\"2B0E03021A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"SHA1(1.3.14.3.2.26)\",\"isTemplate\":true},{\"id\":\"1.0.1.0.1\",\"pId\":\"1.0.1.0\",\"path\":\"/1/0/1/0/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.2\",\"pId\":\"1.0\",\"path\":\"/1/0/2\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":25,\"isContainer\":true,\"children\":[{\"id\":\"1.0.2.0\",\"pId\":\"1.0.2\",\"path\":\"/1/0/2/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010701\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"data(1.2.840.113549.1.7.1)\",\"isTemplate\":true},{\"id\":\"1.0.2.1\",\"pId\":\"1.0.2\",\"path\":\"/1/0/2/1\",\"tag\":{\"value\":0,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-96]},\"tagNum\":160,\"length\":12,\"isContainer\":true,\"children\":[{\"id\":\"1.0.2.1.0\",\"pId\":\"1.0.2.1\",\"path\":\"/1/0/2/1/0\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":10,\"isContainer\":false,\"hexValue\":\"6C73646A666C64736A66\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3\",\"pId\":\"1.0\",\"path\":\"/1/0/3\",\"tag\":{\"value\":0,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-96]},\"tagNum\":160,\"length\":753,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0\",\"pId\":\"1.0.3\",\"path\":\"/1/0/3/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":749,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0\",\"pId\":\"1.0.3.0\",\"path\":\"/1/0/3/0/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":469,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.0\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/0\",\"tag\":{\"value\":0,\"type\":\"CONTEXT\",\"constructed\":true,\"encoding\":[-96]},\"tagNum\":160,\"length\":3,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.0.0\",\"pId\":\"1.0.3.0.0.0\",\"path\":\"/1/0/3/0/0/0/0\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"02\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.1\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":13,\"isContainer\":false,\"hexValue\":\"05AB163391878B6AACBBBDA372\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.2\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/2\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.2.0\",\"pId\":\"1.0.3.0.0.2\",\"path\":\"/1/0/3/0/0/2/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D01010B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"sha256WithRSAEncryption(1.2.840.113549.1.1.11)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.2.1\",\"pId\":\"1.0.3.0.0.2\",\"path\":\"/1/0/3/0/0/2/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.3\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/3\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":52,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.0\",\"pId\":\"1.0.3.0.0.3\",\"path\":\"/1/0/3/0/0/3/0\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":11,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.0.0\",\"pId\":\"1.0.3.0.0.3.0\",\"path\":\"/1/0/3/0/0/3/0/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.0.0.0\",\"pId\":\"1.0.3.0.0.3.0.0\",\"path\":\"/1/0/3/0/0/3/0/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"550406\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"C(2.5.4.6)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.3.0.0.1\",\"pId\":\"1.0.3.0.0.3.0.0\",\"path\":\"/1/0/3/0/0/3/0/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":2,\"isContainer\":false,\"hexValue\":\"434E\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.3.1\",\"pId\":\"1.0.3.0.0.3\",\"path\":\"/1/0/3/0/0/3/1\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":19,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.1.0\",\"pId\":\"1.0.3.0.0.3.1\",\"path\":\"/1/0/3/0/0/3/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":17,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.1.0.0\",\"pId\":\"1.0.3.0.0.3.1.0\",\"path\":\"/1/0/3/0/0/3/1/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"O(2.5.4.10)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.3.1.0.1\",\"pId\":\"1.0.3.0.0.3.1.0\",\"path\":\"/1/0/3/0/0/3/1/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":10,\"isContainer\":false,\"hexValue\":\"494E464F534543204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.3.2\",\"pId\":\"1.0.3.0.0.3\",\"path\":\"/1/0/3/0/0/3/2\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.2.0\",\"pId\":\"1.0.3.0.0.3.2\",\"path\":\"/1/0/3/0/0/3/2/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.3.2.0.0\",\"pId\":\"1.0.3.0.0.3.2.0\",\"path\":\"/1/0/3/0/0/3/2/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"OU(2.5.4.11)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.3.2.0.1\",\"pId\":\"1.0.3.0.0.3.2.0\",\"path\":\"/1/0/3/0/0/3/2/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":7,\"isContainer\":false,\"hexValue\":\"494E464F204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.4\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/4\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":30,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.4.0\",\"pId\":\"1.0.3.0.0.4\",\"path\":\"/1/0/3/0/0/4/0\",\"tag\":{\"value\":23,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[23]},\"tagNum\":23,\"length\":13,\"isContainer\":false,\"hexValue\":\"3232313131333036333231305A\",\"tagName\":\"UTC_TIME\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.4.1\",\"pId\":\"1.0.3.0.0.4\",\"path\":\"/1/0/3/0/0/4/1\",\"tag\":{\"value\":23,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[23]},\"tagNum\":23,\"length\":13,\"isContainer\":false,\"hexValue\":\"3233303131323036333231305A\",\"tagName\":\"UTC_TIME\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.5\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/5\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":52,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.0\",\"pId\":\"1.0.3.0.0.5\",\"path\":\"/1/0/3/0/0/5/0\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":11,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.0.0\",\"pId\":\"1.0.3.0.0.5.0\",\"path\":\"/1/0/3/0/0/5/0/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.0.0.0\",\"pId\":\"1.0.3.0.0.5.0.0\",\"path\":\"/1/0/3/0/0/5/0/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"550406\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"C(2.5.4.6)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.5.0.0.1\",\"pId\":\"1.0.3.0.0.5.0.0\",\"path\":\"/1/0/3/0/0/5/0/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":2,\"isContainer\":false,\"hexValue\":\"434E\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.5.1\",\"pId\":\"1.0.3.0.0.5\",\"path\":\"/1/0/3/0/0/5/1\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":19,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.1.0\",\"pId\":\"1.0.3.0.0.5.1\",\"path\":\"/1/0/3/0/0/5/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":17,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.1.0.0\",\"pId\":\"1.0.3.0.0.5.1.0\",\"path\":\"/1/0/3/0/0/5/1/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"O(2.5.4.10)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.5.1.0.1\",\"pId\":\"1.0.3.0.0.5.1.0\",\"path\":\"/1/0/3/0/0/5/1/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":10,\"isContainer\":false,\"hexValue\":\"494E464F534543204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.5.2\",\"pId\":\"1.0.3.0.0.5\",\"path\":\"/1/0/3/0/0/5/2\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.2.0\",\"pId\":\"1.0.3.0.0.5.2\",\"path\":\"/1/0/3/0/0/5/2/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.5.2.0.0\",\"pId\":\"1.0.3.0.0.5.2.0\",\"path\":\"/1/0/3/0/0/5/2/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"OU(2.5.4.11)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.5.2.0.1\",\"pId\":\"1.0.3.0.0.5.2.0\",\"path\":\"/1/0/3/0/0/5/2/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":7,\"isContainer\":false,\"hexValue\":\"494E464F204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.6\",\"pId\":\"1.0.3.0.0\",\"path\":\"/1/0/3/0/0/6\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":290,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.6.0\",\"pId\":\"1.0.3.0.0.6\",\"path\":\"/1/0/3/0/0/6/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.0.6.0.0\",\"pId\":\"1.0.3.0.0.6.0\",\"path\":\"/1/0/3/0/0/6/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010101\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"rsaEncryption(1.2.840.113549.1.1.1)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.0.6.0.1\",\"pId\":\"1.0.3.0.0.6.0\",\"path\":\"/1/0/3/0/0/6/0/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.0.6.1\",\"pId\":\"1.0.3.0.0.6\",\"path\":\"/1/0/3/0/0/6/1\",\"tag\":{\"value\":3,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[3]},\"tagNum\":3,\"length\":271,\"isContainer\":false,\"hexValue\":\"003082010A028201010090806DD95790DE6A9435F4322DA4C8EE0095DCE67047E10869FD72BF3C809CD4B003D93A315DA3D5F649950C25C0BE0974D77330ABFA8DB10735A9A48D1D6DE21053A4F4FA71922515D192A3875E411122E3A8C8228B2D5CC6339B03D09EBA9FCEB7B0F572626C0B21EF970CE5E77FFDAEF936A60BCD8C7F43BE30D359F0B8E354838577CE019748A6F3423459DDC2707FBD1082CC323B054B754140200BFC904882804F127E7CEC9B103A7BCD18E626EA6EC0E3934C77E83FAD422A4D3B8596C090C3880A721F8217F0A33B3A39D885AE398C1FF7091A8BBE43479321D3D8A26D12B5A8D9C55930FF26586584C8460BFBAFD68D77529A8839E1A41F06DBCAE70203010001\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.1\",\"pId\":\"1.0.3.0\",\"path\":\"/1/0/3/0/1\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"1.0.3.0.1.0\",\"pId\":\"1.0.3.0.1\",\"path\":\"/1/0/3/0/1/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D01010B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"sha256WithRSAEncryption(1.2.840.113549.1.1.11)\",\"isTemplate\":true},{\"id\":\"1.0.3.0.1.1\",\"pId\":\"1.0.3.0.1\",\"path\":\"/1/0/3/0/1/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.3.0.2\",\"pId\":\"1.0.3.0\",\"path\":\"/1/0/3/0/2\",\"tag\":{\"value\":3,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[3]},\"tagNum\":3,\"length\":257,\"isContainer\":false,\"hexValue\":\"0070D138759B4D22971F300909BE2EB6D7ACAC06E36C5AD696EACCDB80B18834FE697B6618D1C0CAF02016635597400974370C7E25C8FFAC2130BD480089D4913A16E0AEC592A39BD6EAA273AF8838ED3FF1C5E505E239C3408CC0CC0CBAE0A63B0F1AECE79606CD44F56FAA47B39ECD5CCE0985CC6387F45137633531162C33CAEFBFB4F526515D176A485C92D54FEAE42D8F5361880204992AE7EFFA8F10170E479BDCE104B3F743C7041FFB2D5933E9C1C4B762A1B7B90A9C1729D46A270BF385A378D88347C712EFA1201C96D4162C904225FFE7CD85D96E1EC21208C60DA05B3B9D476CABC565F9D871BB3D39D4D8701A810B89C1095E299E57C0E84336E7\",\"tagName\":\"BIT_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false},{\"id\":\"1.0.4\",\"pId\":\"1.0\",\"path\":\"/1/0/4\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":364,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0\",\"pId\":\"1.0.4\",\"path\":\"/1/0/4/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":360,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.0\",\"pId\":\"1.0.4.0\",\"path\":\"/1/0/4/0/0\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":1,\"isContainer\":false,\"hexValue\":\"01\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true},{\"id\":\"1.0.4.0.1\",\"pId\":\"1.0.4.0\",\"path\":\"/1/0/4/0/1\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":69,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0\",\"pId\":\"1.0.4.0.1\",\"path\":\"/1/0/4/0/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":52,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.0\",\"pId\":\"1.0.4.0.1.0\",\"path\":\"/1/0/4/0/1/0/0\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":11,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.0.0\",\"pId\":\"1.0.4.0.1.0.0\",\"path\":\"/1/0/4/0/1/0/0/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.0.0.0\",\"pId\":\"1.0.4.0.1.0.0.0\",\"path\":\"/1/0/4/0/1/0/0/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"550406\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"C(2.5.4.6)\",\"isTemplate\":true},{\"id\":\"1.0.4.0.1.0.0.0.1\",\"pId\":\"1.0.4.0.1.0.0.0\",\"path\":\"/1/0/4/0/1/0/0/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":2,\"isContainer\":false,\"hexValue\":\"434E\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.1.0.1\",\"pId\":\"1.0.4.0.1.0\",\"path\":\"/1/0/4/0/1/0/1\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":19,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.1.0\",\"pId\":\"1.0.4.0.1.0.1\",\"path\":\"/1/0/4/0/1/0/1/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":17,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.1.0.0\",\"pId\":\"1.0.4.0.1.0.1.0\",\"path\":\"/1/0/4/0/1/0/1/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"O(2.5.4.10)\",\"isTemplate\":true},{\"id\":\"1.0.4.0.1.0.1.0.1\",\"pId\":\"1.0.4.0.1.0.1.0\",\"path\":\"/1/0/4/0/1/0/1/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":10,\"isContainer\":false,\"hexValue\":\"494E464F534543204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.1.0.2\",\"pId\":\"1.0.4.0.1.0\",\"path\":\"/1/0/4/0/1/0/2\",\"tag\":{\"value\":17,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[49]},\"tagNum\":49,\"length\":16,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.2.0\",\"pId\":\"1.0.4.0.1.0.2\",\"path\":\"/1/0/4/0/1/0/2/0\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":14,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.1.0.2.0.0\",\"pId\":\"1.0.4.0.1.0.2.0\",\"path\":\"/1/0/4/0/1/0/2/0/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":3,\"isContainer\":false,\"hexValue\":\"55040B\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"OU(2.5.4.11)\",\"isTemplate\":true},{\"id\":\"1.0.4.0.1.0.2.0.1\",\"pId\":\"1.0.4.0.1.0.2.0\",\"path\":\"/1/0/4/0/1/0/2/0/1\",\"tag\":{\"value\":19,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[19]},\"tagNum\":19,\"length\":7,\"isContainer\":false,\"hexValue\":\"494E464F204341\",\"tagName\":\"PRINTABLE_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.1.1\",\"pId\":\"1.0.4.0.1\",\"path\":\"/1/0/4/0/1/1\",\"tag\":{\"value\":2,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[2]},\"tagNum\":2,\"length\":13,\"isContainer\":false,\"hexValue\":\"05AB163391878B6AACBBBDA372\",\"tagName\":\"INTEGER\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.2\",\"pId\":\"1.0.4.0\",\"path\":\"/1/0/4/0/2\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":9,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.2.0\",\"pId\":\"1.0.4.0.2\",\"path\":\"/1/0/4/0/2/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":5,\"isContainer\":false,\"hexValue\":\"2B0E03021A\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"SHA1(1.3.14.3.2.26)\",\"isTemplate\":true},{\"id\":\"1.0.4.0.2.1\",\"pId\":\"1.0.4.0.2\",\"path\":\"/1/0/4/0/2/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.3\",\"pId\":\"1.0.4.0\",\"path\":\"/1/0/4/0/3\",\"tag\":{\"value\":16,\"type\":\"UNIVERSAL\",\"constructed\":true,\"encoding\":[48]},\"tagNum\":48,\"length\":13,\"isContainer\":true,\"children\":[{\"id\":\"1.0.4.0.3.0\",\"pId\":\"1.0.4.0.3\",\"path\":\"/1/0/4/0/3/0\",\"tag\":{\"value\":6,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[6]},\"tagNum\":6,\"length\":9,\"isContainer\":false,\"hexValue\":\"2A864886F70D010101\",\"tagName\":\"OBJECT_IDENTIFIER\",\"tagClass\":\"UNIVERSAL\",\"visualValue\":\"rsaEncryption(1.2.840.113549.1.1.1)\",\"isTemplate\":true},{\"id\":\"1.0.4.0.3.1\",\"pId\":\"1.0.4.0.3\",\"path\":\"/1/0/4/0/3/1\",\"tag\":{\"value\":5,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[5]},\"tagNum\":5,\"length\":0,\"isContainer\":false,\"hexValue\":\"\",\"tagName\":\"NULL\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false},{\"id\":\"1.0.4.0.4\",\"pId\":\"1.0.4.0\",\"path\":\"/1/0/4/0/4\",\"tag\":{\"value\":4,\"type\":\"UNIVERSAL\",\"constructed\":false,\"encoding\":[4]},\"tagNum\":4,\"length\":256,\"isContainer\":false,\"hexValue\":\"787A4B9AF757B8EA3479B0BEE853292016B9C68AA42720D16C924168C856B29322844C01FED7A7C70CB4FBA9A4D3CDA867238EA7F5B6E9501CB38FAF2C40F3B030CD0453BE4B1AC5D858625C70B367552CC5067F4CEAF8F4E92CD4B82C544765EB39184E894EE85A5B43B58FB31E769F91DEA987E12028719CC7B82F6D387B001FF86D81754C33F0DB273719307991D1C14ED30BA33F2B4882BFB949C25D4E2687263D2AB99947921D7A28298EEA860797B96214ACABCCAEC8C916C8448214D9D5EF074BC66F8C64E25D81E7ABD4B4C3CDB109DCD22CD1A282321FD370E035DED6A229514B051DA62B37E41DD141B404A784880DDCCC8B838B449660C74EC105\",\"tagName\":\"OCTET_STRING\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":true}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SET\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}],\"tagName\":\"CONTEXT\",\"tagClass\":\"CONTEXT\",\"isTemplate\":false}],\"tagName\":\"SEQUENCE\",\"tagClass\":\"UNIVERSAL\",\"isTemplate\":false}";

        path = "/1/0/3/0/0/2/0";
        s = gsonQuerier.queryNode(jsonData, path);
        System.err.println(s);
    }

}