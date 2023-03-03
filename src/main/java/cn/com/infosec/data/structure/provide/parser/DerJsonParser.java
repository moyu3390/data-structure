/**
 * Data2JsonParser
 * <p> 解析页面填写的数据或选择的文件内容 为 json格式字符串
 * 1.0
 * <p>
 * 2022/12/30 16:01
 */

package cn.com.infosec.data.structure.provide.parser;

import cn.com.infosec.data.structure.asn.decoder.AsnDecoder;
import cn.com.infosec.data.structure.asn.decoder.BerDecoder;
import cn.com.infosec.data.structure.asn.encoder.AsnEncoder;
import cn.com.infosec.data.structure.asn.encoder.BerEncoder;
import cn.com.infosec.data.structure.asn.parser.DataType;
import cn.com.infosec.data.structure.asn.parser.DataTypeParserExecutor;
import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.bean.vo.StructureDataVO;
import cn.com.infosec.data.structure.coding.DataCodingType;
import cn.com.infosec.data.structure.config.Config;
import cn.com.infosec.data.structure.json.IJsonNodeModifier;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.IJsonQuerier;
import cn.com.infosec.data.structure.json.editor.JsonNodeModifier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import cn.com.infosec.data.structure.json.querier.GsonQuerier;
import cn.com.infosec.data.structure.utils.FileUtil;
import cn.com.infosec.data.structure.utils.NodePathUtil;
import cn.com.infosec.data.structure.validator.ValidatorExecutor;
import cn.com.infosec.data.structure.validator.ValidatorType;
import cn.com.infosec.v160.util.encoders.Base64;
import cn.com.infosec.v160.util.encoders.Hex;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DerJsonParser {

    //    private static final Logger LOG = LoggerFactory.getLogger(DerJsonParser.class);
    static final IJsonParser JSON_PARSER = new GsonParser();
    static final IJsonNodeModifier NODE_MODIFIER = new JsonNodeModifier();

    final static IJsonQuerier JSON_QUERIER = new GsonQuerier();
    final static AsnDecoder<byte[]> ASN_DECODER = new BerDecoder();
    final static AsnEncoder<byte[]> ASN_ENCODER = new BerEncoder();

    private static final String baseDir = "cn/com/infosec/data/structure/templates/";

    /**
     * @param data       解析asn1数据为json格式(匹配已有的数据结构和设置别名等)
     * @param codingType 数据编码结构类型： 1=Base64,2=Hex,3=无 || B=Base64,H=Hex,N=无
     * @return json字符串
     */
    public static String derDataAnalysis(byte[] data, String codingType) {

        StructureDataVO<StructureJsonData> structureDataVO = derDataAnalysis(codingType, data);
        String jsonData = JSON_PARSER.encode(structureDataVO);
        return jsonData;
    }

    /**
     * @param codingType 数据编码结构类型： 1=Base64,2=Hex,3=无 || B=Base64,H=Hex,N=无
     * @param data       解析asn1数据为StructureDataVO对象(匹配已有的数据结构和设置别名等)
     * @return json字符串
     */
    public static StructureDataVO<StructureJsonData> derDataAnalysis(String codingType, byte[] data) {
//        log.info("开始解析数据");
        if (Objects.isNull(data) || data.length < 1) {
            return null;
        }
        byte[] d = decodeData(data, codingType);
        //  解析asn
        StructureJsonData structureJsonData = ASN_DECODER.decode(d, Config.onlyStructure);
        // json解析，把java对象解析成json串
        String srcJsonData = JSON_PARSER.encode(structureJsonData);
        // 通过der字节数组，匹配现有的asn数据类型,
        DataType dataType = DataTypeParserExecutor.execute(d);
        String dataTypeName = "";
        if (Objects.nonNull(dataType) && !DataType.UNKNOW_DATA.equals(dataType)) {
            // 为生成的java对象生成名称后半部分（数据结构与哪一种已知对象相匹配）
            dataTypeName = dataType.getDataType();
            // 找到现有数据类型属性模板文件，复制节点别名属性
            String attributeTemplateFileName = dataType.getAttributeTemplateName();
            // 读取文件内容
            attributeTemplateFileName = baseDir + attributeTemplateFileName;
            String destJsonData = FileUtil.readInternalFileContext2String(attributeTemplateFileName);
            // 复制节点别名属性
            List<String> attributes = new ArrayList<>();
            attributes.add("nodeAlias");
            attributes.add("nodeIdentity");
            srcJsonData = NODE_MODIFIER.copyAttributes(srcJsonData, destJsonData, attributes);
        }

        StructureDataVO<StructureJsonData> structureDataVO = new StructureDataVO<>();
        StructureJsonData decode = JSON_PARSER.decode(srcJsonData, false);
        if (Objects.nonNull(dataTypeName) && dataTypeName.trim().length() > 0) {
            structureDataVO.setStrucutreName("_" + dataTypeName);
        }
        structureDataVO.setData(decode);
        return structureDataVO;
    }


    public static String derDataAnalysis2Json(byte[] data, String codingType) {
        StructureDataVO<StructureJsonData> structureJsonDataStructureDataVO = derDataAnalysis(codingType, data);
        if (Objects.isNull(structureJsonDataStructureDataVO) || Objects.isNull(structureJsonDataStructureDataVO.getData())) {
            return null;
        }
        return JSON_PARSER.encode(structureJsonDataStructureDataVO.getData());
    }



    /**
     * 将DER编码转为JSON格式(只是单纯的转换为json)
     *
     * @param derBytes
     * @return
     */
    public static String der2json(byte[] derBytes) {
        if (Objects.isNull(derBytes) || derBytes.length < 1) {
            return null;
        }
        return ASN_DECODER.decodeToJson(derBytes, false);
    }

    /**
     * 按指定字符集编码，将DER编码转为JSON格式(只是单纯的转换为json)
     *
     * @param derBytes
     * @return
     */
    public static String der2json(byte[] derBytes, String encoding) {
        if (Objects.isNull(derBytes) || derBytes.length < 1) {
            return null;
        }
        if (Objects.isNull(encoding) || encoding.trim().length() == 0) {
            encoding = "UTF-8";
        }
        StructureJsonData structureJsonData = ASN_DECODER.decode(derBytes, false);
        String jsonData = JSON_PARSER.encode(structureJsonData, encoding);
        return jsonData;
    }

    /**
     * 将JSON字符串转换为DER编码
     *
     * @param derJson
     * @return
     */
    public static byte[] json2der(String derJson) {
        StructureJsonData structureJsonData = JSON_PARSER.decode(derJson, false);
        byte[] encode = ASN_ENCODER.encode(structureJsonData);
        return encode;
    }

    /**
     * 按指定字符编码，将JSON字符串转换为DER编码
     *
     * @param derJson
     * @param encoding
     * @return
     */
    public static byte[] json2der(String derJson, String encoding) {
        StructureJsonData structureJsonData = JSON_PARSER.decode(derJson, encoding, false);
        byte[] bytes = ASN_ENCODER.encode(structureJsonData);
        return bytes;
    }


    private static byte[] decodeData(byte[] data, String codingType) {
        byte[] d = data;
        if (Objects.isNull(codingType)) {
            return d;
        }
        DataCodingType dataCodingType = DataCodingType.fromType(codingType);
        if (Objects.isNull(dataCodingType)) {
            return d;
        }
        // 编解码
        if (DataCodingType.BASE64.equals(dataCodingType)) {
            d = Base64.decode(data);
        } else if (DataCodingType.HEX.equals(dataCodingType)) {
            d = Hex.decode(data);
        }
        return d;
    }

    public static Map<String, byte[]> getNodeValueReferTemplateByIds(String template, byte[] derBytes, List<String> ids) {
        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        List<String> paths = ids.stream().map(s -> NodePathUtil.getPathByNodeId(s)).collect(Collectors.toList());
        Map<String, byte[]> nodeValueReferTemplateByPaths = getNodeValueReferTemplateByPaths(template, derBytes, paths);
        Map<String, byte[]> nodeIdsMap = new HashMap<>();
        nodeValueReferTemplateByPaths.entrySet().forEach(e -> {
            String p = e.getKey();
            byte[] v = e.getValue();
            String id = NodePathUtil.path2Id(p);
            nodeIdsMap.put(id, v);
        });
        return nodeIdsMap;
    }

    public static Map<String, byte[]> getNodeValueReferTemplateByPaths(String template, byte[] derBytes, List<String> paths) {

        Map<String, byte[]> resultMap = new HashMap<>();
        if (Objects.isNull(template) || paths.isEmpty() || Objects.isNull(derBytes)) {
            return resultMap;
        }
        //  解析字节数据
        String derDataJson = ASN_DECODER.decodeToJson(derBytes, false);
        // 验证der结构和sjd是否匹配
        ValidatorExecutor.execut(ValidatorType.JSON, derDataJson, template);
        for (String nodePath : paths) {
            // 选中的节点数据
            String selectNodeData = JSON_QUERIER.queryNode(derDataJson, nodePath);
            // SN节点在SJD中不存在
            if (Objects.isNull(selectNodeData) || selectNodeData.trim().length() == 0) {
                resultMap.put(nodePath, null);
                continue;
            }
            // 把提取出的节点数据对象转成der字节数组
            byte[] encode = ASN_ENCODER.encode(selectNodeData);
            resultMap.put(nodePath, encode);
        }
        return resultMap;
    }

//    public static void main(String[] args) {
//        List<String> ids = new ArrayList<>();
//        ids.add("1.0.1");
//        ids.add("1.0");
//        ids.add("1.0.1.1.2");
//        ids.add("1.0.11.235.4");
//        ids.add("1.0.1.1.1.1");
//        ids.add("1.0.1.0.0.0");
//        ids.add("1.0.1.2.3.6.6");
//
//        List<String> paths = ids.stream().map(d -> NodePathUtil.getPathByNodeId(d)).collect(Collectors.toList());
//        System.err.println(paths);
//    }
}
