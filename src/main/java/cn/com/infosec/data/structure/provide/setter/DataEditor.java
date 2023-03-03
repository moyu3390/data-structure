/**
 * DataEditor
 * <p>
 * 1.0
 * <p>
 * 2023/1/9 17:33
 */

package cn.com.infosec.data.structure.provide.setter;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.encoder.AsnEncoder;
import cn.com.infosec.data.structure.asn.encoder.BerEncoder;
import cn.com.infosec.data.structure.asn.validator.impl.Asn1StrucutreSimpleValidatorImpl;
import cn.com.infosec.data.structure.json.IJsonNodeModifier;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.editor.JsonNodeModifier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.utils.NodePathUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class DataEditor {
    final static IJsonQuerier JSON_QUERIER = new GsonQuerier();
    final static AsnDecoder<byte[]> ASN_DECODER = new BerDecoder();
    final static AsnEncoder<byte[]> ASN_ENCODER = new BerEncoder();

    final static IJsonNodeModifier JSON_NODE_MODIFIER = new JsonNodeModifier();
    final static IJsonParser JSON_PARSER = new GsonParser();

    final static boolean isFullData = false;

    /**
     * 根据JSON格式中的ID，设置负载数据
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param id      JSON节点的ID
     * @param value   待设置的节点值（负载）
     * @return
     */
    public static byte[] setNodeValueById(String derJson, String id, byte[] value) {

        if (Objects.isNull(id) || id.trim().length() == 0) {
            return null;
        }

        String nodePath = NodePathUtil.getPathByNodeId(id);
        return setNodeValueByPath(derJson, nodePath, value);
    }

    /**
     * 根据JSON格式中的ID，设置负载数据
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param path    JSON节点的Path
     * @param value   待设置的节点值（负载）
     * @return
     */
    public static byte[] setNodeValueByPath(String derJson, String path, byte[] value) {
        if (Objects.isNull(derJson) || derJson.trim().length() == 0) {
            return null;
        }

        if (Objects.isNull(path) || path.trim().length() == 0) {
            return null;
        }

        if (Objects.isNull(value) || value.length == 0) {
            return null;
        }

        String newJsonData = JSON_NODE_MODIFIER.modifyNodeValue(derJson, path, value);
        String nodeJson = newJsonData;
        if (!isFullData) {
            newJsonData = JSON_PARSER.rebuildNodePath(newJsonData);
            nodeJson = JSON_QUERIER.queryNode(newJsonData, path);
        }
        byte[] nodeData = ASN_ENCODER.encode(nodeJson);
        return nodeData;
    }


    /**
     * 根据JSON格式中的Path，替换节点
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param id      JSON节点的id
     * @param tagRaw  待设置的节点值（TLV）
     * @return DER编码的二进制形式
     */
    public static byte[] replaceNodeById(String derJson, String id, byte[] tagRaw) {

        if (Objects.isNull(id) || id.trim().length() == 0) {
            return null;
        }

        String nodePath = NodePathUtil.getPathByNodeId(id);
        return replaceNodeByPath(derJson, nodePath, tagRaw);
    }

    /**
     * 根据JSON格式中的Path，替换节点
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param path    JSON节点的Path
     * @param tagRaw  待设置的节点值（TLV）
     * @return DER编码的二进制形式
     */
    public static byte[] replaceNodeByPath(String derJson, String path, byte[] tagRaw) {

        if (Objects.isNull(derJson) || derJson.trim().length() == 0) {
            return null;
        }

        if (Objects.isNull(path) || path.trim().length() == 0) {
            return null;
        }

        if (Objects.isNull(tagRaw) || tagRaw.length == 0) {
            return null;
        }

        String nodeJsonData = ASN_DECODER.decodeToJson(tagRaw, path, false);
        String newJsonData = JSON_NODE_MODIFIER.replaceNodeData(derJson, path, nodeJsonData);
        String nodeJson = newJsonData;
        if (!isFullData) {
            newJsonData = JSON_PARSER.rebuildNodePath(newJsonData);
            nodeJson = JSON_QUERIER.queryNode(newJsonData, path);
        }
        byte[] nodeData = ASN_ENCODER.encode(nodeJson);
        return nodeData;
    }


    /**
     * 合并JSON模板与参数
     *
     * @param templateJson JSON模板
     * @param setting      模板参数（负载）
     * @param replacing    模板参数（替换）
     * @return DER编码的二进制形式
     */
    public static byte[] genDERByTemplate(String templateJson, Map<String, byte[]> setting, Map<String, byte[]> replacing) {

        String value = JSON_NODE_MODIFIER.modifyNodesValue(templateJson, setting);

        Map<String, String> replacingMap = checkParams(replacing);

        String newData = JSON_NODE_MODIFIER.replaceNodesData(value, replacingMap);

        byte[] encode = ASN_ENCODER.encode(newData);
        return encode;
    }

    /**
     * 检查和过滤参数，去除参数中值为null或值不是tlv结构的元素。
     *
     * @param nodesMap
     * @return
     */
    private static Map<String, String> checkParams(Map<String, byte[]> nodesMap) {
        Map<String, String> nodesJsonMap = new HashMap<>();
        Iterator<Map.Entry<String, byte[]>> iterator = nodesMap.entrySet().iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<String, byte[]> next = iterator.next();
            String path = next.getKey();
            byte[] newNodeData = next.getValue();
            if (Objects.nonNull(newNodeData)) {
                boolean isStructure = validateTLV(newNodeData);
                if (isStructure) {
                    String decodeToJson = ASN_DECODER.decodeToJson(newNodeData, false);
                    nodesJsonMap.put(path, decodeToJson);
                }
            }
        }
        return nodesJsonMap;
    }

    private static boolean validateTLV(byte[] newNodeData) {
        try {
            Asn1StrucutreSimpleValidatorImpl validator = new Asn1StrucutreSimpleValidatorImpl();
            validator.validator(newNodeData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
