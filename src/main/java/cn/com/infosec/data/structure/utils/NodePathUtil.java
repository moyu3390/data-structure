/**
 * NodePathUtil
 * <p>
 * 1.0
 * <p>
 * 2022/12/26 17:25
 */

package cn.com.infosec.data.structure.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Objects;

public class NodePathUtil {

    public static String path2Id(String path) {
        if (Objects.isNull(path) || path.trim().length() == 0) {
            return "";
        }
        String pid = "";
        if (path.startsWith("/")) {
            String temp = path.replaceAll("\\/", ".");
            if (temp.startsWith(".")) {
                temp = temp.substring(1);
            }
            if (temp.endsWith(".")) {
                temp = temp.substring(0, temp.length() - 1);
            }
            pid = temp;
        }
        return pid;
    }

    public static String getPathByNodeId(String nodeId) {
        if (Objects.isNull(nodeId) || nodeId.trim().length() == 0) {
            return "";
        }
        String nId = nodeId.replaceAll("\\.", "/");
        if (!nId.startsWith("/")) {
            nId = "/" + nId;
        }
        return nId;
    }

    public static String genNodeId(String parentId, String currentNodeNum) {
        String nodeId = parentId;
        if (!Objects.isNull(currentNodeNum) && currentNodeNum.trim().length() > 0) {
            nodeId = nodeId + "." + currentNodeNum;
            if (Objects.isNull(parentId) || parentId.trim().compareTo("") == 0) {
                nodeId = currentNodeNum;
            }
        } else {
            if (!Objects.isNull(parentId) && parentId.trim().compareTo("") > 0) {
                nodeId = parentId + "." + "0";
            }
        }
        return nodeId;
    }

    public static String getParentId(String path) {
        String parentPaht = getParentPath(path);
        return path2Id(parentPaht);
    }

    public static String getParentPath(String path) {
        if (Objects.isNull(path)) {
            return "";
        }
        if (("").equals(path)) {
            return "";
        }
        String parentPath = "";

        if (path.startsWith("/")) {
            String temp = path;
            if (temp.endsWith("/")) {
                temp = temp.substring(0, temp.length() - 1);
            }
            int index = temp.lastIndexOf("/");
            parentPath = temp.substring(0, index);
        }
        return parentPath;
    }


    public static String getChildNum(String path) {
        String parent = getParentPath(path);
        String num = "";
        if (!Objects.isNull(parent) && parent.trim().length() > 0) {
            num = path.replaceAll(parent, "");
            num = num.replaceAll("\\/", "");
            return num;
        }
        if (!Objects.isNull(path) && path.trim().length() > 0) {
            num = path.replaceAll("\\/", "");
            return num;
        }
        return null;
    }


    public static boolean isLeafNode(String jsonData) {
        // 判断数据中是否包含children节点：有-结构数据；无-叶子节点
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        boolean children = jsonObject.asMap().containsKey("children");
        boolean isContainer = jsonObject.get("isContainer").getAsBoolean();
        boolean isHasHexValue = jsonObject.asMap().containsKey("hexValue");
        return !isContainer && !children && isHasHexValue;
    }


    public static void main(String[] args) {
        String path = "/3";
        String path2 = "/3/0/1/2";
        String path3 = "/3/0/1/3/";
        String path4 = "/3/0";
        System.err.println(path2Id(path));
        System.err.println(path2Id(path2));
        System.err.println(path2Id(path3));
        System.err.println(path2Id(path4));


        System.err.println(getParentPath(path));
        System.err.println(getParentPath(path2));
        System.err.println(getParentPath(path3));
        System.err.println(getParentPath(path4));


        System.err.println(getChildNum(path));
        System.err.println(getChildNum(path2));
        System.err.println(getChildNum(path3));
        System.err.println(getChildNum(path4));
    }
}
