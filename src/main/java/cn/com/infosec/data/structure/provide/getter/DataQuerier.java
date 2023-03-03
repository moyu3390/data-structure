/**
 * DataQuerier
 * <p>
 * 1.0
 * <p>
 * 2023/1/9 16:41
 */

package cn.com.infosec.data.structure.provide.getter;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.encoder.AsnEncoder;
import cn.com.infosec.data.structure.asn.encoder.BerEncoder;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.utils.NodePathUtil;
import cn.com.infosec.v160.util.encoders.Hex;

import java.util.Objects;

public class DataQuerier {
    final static IJsonQuerier JSON_QUERIER = new GsonQuerier();
    final static AsnDecoder<byte[]> ASN_DECODER = new BerDecoder();
    final static AsnEncoder<byte[]> ASN_ENCODER = new BerEncoder();

    final static IJsonParser JSON_PARSER = new GsonParser();

    /**
     * 根据JSON格式中的ID，获取节点数据
     *
     * @param derJson   描述DER格式的JSON字符串
     * @param id        JSON节点的ID (格式：1.0.2.0.1)
     * @param onlyValue
     * @return true表示返回节点数据（payload），false表示返回包含Tag的节点数据
     */
    public static byte[] getNodeValueById(String derJson, String id, boolean onlyValue) {
        if (Objects.isNull(id) || id.trim().length() < 1) {
            return null;
        }
        // 转换为path
        String nodePath = NodePathUtil.getPathByNodeId(id);
        return getNodeValueByPath(derJson, nodePath, onlyValue);
    }

    /**
     * 根据JSON格式中的Path，获取节点数据
     *
     * @param derJson   描述DER格式的JSON字符串
     * @param path      JSON节点的路径 (格式：/1/0/2/0/1)
     * @param onlyValue
     * @return true表示返回节点数据（payload），false表示返回包含Tag的节点数据
     */
    public static byte[] getNodeValueByPath(String derJson, String path, boolean onlyValue) {
        if (Objects.isNull(derJson) || derJson.trim().length() < 1) {
            return null;
        }
        if (Objects.isNull(path) || path.trim().length() < 1) {
            return null;
        }
        // 选中的节点数据
        String selectNodeData = JSON_QUERIER.queryNode(derJson, path);
        // SN节点在SJD中不存在
        if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
            return null;
        }
        StructureJsonData structureJsonData = JSON_PARSER.decode(selectNodeData, false);
        if (Objects.isNull(structureJsonData)) {
            return null;
        }
        if (onlyValue) {
            String hexValue = structureJsonData.getHexValue();
            if (Objects.isNull(hexValue) || hexValue.trim().length() < 1) {
                return null;
            }
            byte[] data = Hex.decode(hexValue);
            return data;
        }
        // 返回包括tag的数据
        byte[] data = ASN_ENCODER.encode(structureJsonData);
        return data;
    }

    /**
     * 根据JSON格式中的ID，截取JSON片段
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param id      JSON节点的ID
     * @return 描述DER格式的JSON字符串
     */
    public static String subJsonById(String derJson, String id) {
        if (Objects.isNull(id) || id.trim().length() < 1) {
            return null;
        }
        // 转换为path
        String nodePath = NodePathUtil.getPathByNodeId(id);
        return subJsonByPath(derJson, nodePath);
    }

    /**
     * 根据JSON格式中的path，截取JSON片段
     *
     * @param derJson 描述DER格式的JSON字符串
     * @param path    JSON节点的path
     * @return 描述DER格式的JSON字符串
     */
    public static String subJsonByPath(String derJson, String path) {
        if (Objects.isNull(derJson) || derJson.trim().length() < 1) {
            return null;
        }
        if (Objects.isNull(path) || path.trim().length() < 1) {
            return null;
        }
        // 选中的节点数据
        String selectNodeData = JSON_QUERIER.queryNode(derJson, path);
        // SN节点在SJD中不存在
        if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
            return null;
        }
        return selectNodeData;
    }

    /**
     * 根据节点标识获取节点的JSON片段
     *
     * @param derJson      描述DER格式的JSON字符串
     * @param nodeIdentity 节点的标识，参照各数据类型模板中设置
     * @return 描述DER格式的JSON字符串
     */
    public static String subJsonByNodeIdentity(String derJson, String nodeIdentity) {
        if (Objects.isNull(derJson) || derJson.trim().length() < 1) {
            return null;
        }
        if (Objects.isNull(nodeIdentity) || nodeIdentity.trim().length() < 1) {
            return null;
        }
        // 选中的节点数据
        String selectNodeData = JSON_QUERIER.queryNodeByIdentity(derJson, nodeIdentity);
        // SN节点在SJD中不存在
        if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
            return null;
        }
        return selectNodeData;
    }





}
