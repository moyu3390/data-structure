/**
 * IJsonComparator
 * <p> json比较类
 * 1.0
 * <p>
 * 2022/12/29 18:28
 */

package cn.com.infosec.data.structure.json.comparator;

import cn.com.infosec.data.structure.config.Config;
import cn.com.infosec.data.structure.json.IJsonComparator;
import com.google.gson.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JsonComparator implements IJsonComparator {
    /**
     * 比较两个json数据是否一致
     *
     * @param srcJsonData
     * @param destJsonData
     * @param onlyStructure 为true:仅比较结构；否则：比较结构和数据内容
     * @return
     */
    @Override
    public boolean compare(String srcJsonData, String destJsonData, boolean onlyStructure) {
        // 以srcjsondata为基准，进行比较
        JsonElement srcJsonElement = JsonParser.parseString(srcJsonData);
        JsonElement destJsonElement = JsonParser.parseString(destJsonData);
        boolean nodeIsValidate = true;
        if(destJsonElement.isJsonObject()) {
            nodeIsValidate = nodeIsValidate(destJsonElement.getAsJsonObject());
        }
        if (srcJsonElement.isJsonObject() && destJsonElement.isJsonObject()) {
            return compareJsonObject(srcJsonElement.getAsJsonObject(), destJsonElement.getAsJsonObject(), onlyStructure, nodeIsValidate);
        } else if (srcJsonElement.isJsonArray() && destJsonElement.isJsonArray()) {
            return compareJsonArray(srcJsonElement.getAsJsonArray(), destJsonElement.getAsJsonArray(), onlyStructure, nodeIsValidate);
        } else if (srcJsonElement.isJsonNull() && destJsonElement.isJsonNull()) {
            return srcJsonElement.getAsJsonNull().equals(destJsonElement.getAsJsonNull());
        } else if (srcJsonElement.isJsonPrimitive() && destJsonElement.isJsonPrimitive()) {
            return compareJsonPrimitive(srcJsonElement.getAsJsonPrimitive(), destJsonElement.getAsJsonPrimitive(), onlyStructure, nodeIsValidate);
        }
        return false;
    }


    private boolean compareJsonPrimitive(JsonPrimitive srcJsonPrimitive, JsonPrimitive destJsonPrimitive, boolean onlyStructure, boolean nodeIsValidate) {
        if (Config.isValidator && nodeIsValidate && !onlyStructure) {
            if (Objects.isNull(srcJsonPrimitive) & Objects.isNull(destJsonPrimitive)) {
                return true;
            }
            if (Objects.isNull(srcJsonPrimitive) || Objects.isNull(destJsonPrimitive)) {
                return false;
            }
            return srcJsonPrimitive.equals(destJsonPrimitive);
        }
        return true;
    }


    private boolean compareJsonArray(JsonArray srcJsonArray, JsonArray destJsonArray, boolean onlyStructure, boolean nodeIsValidate) {
        if (nodeIsValidate) {
            if (Objects.isNull(srcJsonArray) & Objects.isNull(destJsonArray)) {
                return true;
            }
            if (Objects.isNull(srcJsonArray) || Objects.isNull(destJsonArray)) {
                return false;
            }
            if (srcJsonArray.isEmpty() || destJsonArray.isEmpty()) {
                return false;
            }
            if (srcJsonArray.size() != destJsonArray.size()) {
                return false;
            }
        }
        boolean result = true;
        int size = destJsonArray.size();
        for (int i = 0; i < size; i++) {
            JsonElement destJsonElement = destJsonArray.get(i);
            boolean v = nodeIsValidate;
            if (destJsonElement.isJsonObject()) {
                v = nodeIsValidate(destJsonElement.getAsJsonObject());
            }
            JsonElement srcJsonElement = srcJsonArray.get(i);
            result = compareTo(onlyStructure, destJsonElement, srcJsonElement, v);
            if (!result) {
                return result;
            }
        }
        return result;
    }

    private boolean compareTo(boolean onlyStructure, JsonElement destJsonElement, JsonElement srcJsonElement, boolean nodeIsValidate) {
        boolean result = true;
        try {
            if (srcJsonElement.isJsonObject() && destJsonElement.isJsonObject()) {
                result = compareJsonObject(srcJsonElement.getAsJsonObject(), destJsonElement.getAsJsonObject(), onlyStructure, nodeIsValidate);
            } else if (srcJsonElement.isJsonArray() && destJsonElement.isJsonArray()) {
                result = compareJsonArray(srcJsonElement.getAsJsonArray(), destJsonElement.getAsJsonArray(), onlyStructure, nodeIsValidate);
            } else if (srcJsonElement.isJsonPrimitive() && destJsonElement.isJsonPrimitive()) {
                result = compareJsonPrimitive(srcJsonElement.getAsJsonPrimitive(), destJsonElement.getAsJsonPrimitive(), onlyStructure, nodeIsValidate);
            } else if (srcJsonElement.isJsonNull() && destJsonElement.isJsonNull()) {
                result = srcJsonElement.getAsJsonNull().equals(destJsonElement.getAsJsonNull());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private boolean nodeIsValidate(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get("isValidator");
        if (Objects.nonNull(jsonElement) && Config.isValidator) {
            boolean isValidate = jsonElement.getAsBoolean();
            return isValidate;
        }
        return false;
    }

    private boolean compareJsonObject(JsonObject jsonObject, JsonObject compareTargetJsonObject, boolean onlyStructure, boolean nodeIsValidate) {
        boolean result = true;
        if (nodeIsValidate) {
            if ((Objects.isNull(jsonObject) & Objects.isNull(compareTargetJsonObject))) {
                return true;
            }
            if (Objects.isNull(jsonObject) || Objects.isNull(compareTargetJsonObject)) {
                return false;
            }
            Set<String> jsonObjectKeySet = jsonObject.keySet();
            Set<String> compareKeySet = compareTargetJsonObject.keySet();
            if ((Objects.isNull(jsonObjectKeySet) & Objects.isNull(compareKeySet)) || (jsonObjectKeySet.isEmpty() & compareKeySet.isEmpty())) {
                return true;
            }
            if (!jsonObjectKeySet.containsAll(compareKeySet)) {
                return false;
            }
        }
//        if(jsonObject.isJsonObject()) {
//            JsonElement path = jsonObject.get("path");
//            if(Objects.nonNull(path)) {
//                System.err.println(path + "---->" + nodeIsValidate);
//            }
//        }
        // 校验 value; 以destJsonElement为基准，假设 destJsonElement有较少的key
        Iterator<Map.Entry<String, JsonElement>> iterator = compareTargetJsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            try {
                String key = next.getKey();
                JsonElement destJsonElement = next.getValue();
                boolean isValidate = nodeIsValidate;
                if (destJsonElement.isJsonObject()) {
                    isValidate = nodeIsValidate(destJsonElement.getAsJsonObject());
                }
                JsonElement srcJsonElement = jsonObject.get(key);
                if (Objects.isNull(srcJsonElement) & Objects.isNull(destJsonElement)) {
                    continue;
                }
                if(!isValidate) {
                    if (Objects.isNull(srcJsonElement) || Objects.isNull(destJsonElement)) {
                        continue;
                    }
                } else {
                    if (Objects.isNull(srcJsonElement) || Objects.isNull(destJsonElement)) {
                        return false;
                    }
                }
                result = compareTo(onlyStructure, destJsonElement, srcJsonElement, isValidate);
                if (!result) {
                    return result;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


}
