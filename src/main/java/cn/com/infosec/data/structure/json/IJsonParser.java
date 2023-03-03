/**
 * IJsonParser
 * <p>
 * 1.0
 * <p>
 * 2022/12/26 10:56
 */

package cn.com.infosec.data.structure.json;

import cn.com.infosec.data.structure.bean.StructureJsonData;

import java.util.Map;

public interface IJsonParser<T> {
    /**
     * 把json字符串转为SJD对象
     *
     * @param jsonData
     * @return
     */
    StructureJsonData decode(String jsonData, boolean onlyStructure);

    /**
     * 按照指定编码解码json后，转换为对象。
     *
     * @param jsonData
     * @param encoding      json串的字符编码
     * @param onlyStructure
     * @return
     */
    StructureJsonData decode(String jsonData, String encoding, boolean onlyStructure);


    /**
     * 把对象转为json字符串
     *
     * @param t
     * @return
     */
    String encode(T t);

    /**
     * 对象转json
     *
     * @param t
     * @param encoding json串的字符编码
     * @return 输出指定编码的json串
     */
    String encode(T t, String encoding);

//    /**
//     * 把SJD对象转为json字符串
//     *
//     * @param structureJsonData
//     * @return
//     */
//    String encode(StructureJsonData structureJsonData);
//
//    /**
//     * 把SJD对象转为json字符串
//     *
//     * @param encoding json串的字符编码
//     * @return 输出指定编码的json串
//     */
//    String encode(StructureJsonData structureJsonData, String encoding);

    /**
     * 把json字符串转为map
     *
     * @param jsonData
     * @return
     */
    Map<String, Object> innerMap(String jsonData);

    /**
     * 把SJD对象转为map
     *
     * @param structureJsonData
     * @return
     */
    Map<String, Object> innerMap(StructureJsonData structureJsonData);

    /**
     * 重新生成json数据的path和id
     *
     * @param jsonData
     * @return
     */
    String rebuildNodePath(String jsonData);

    /**
     * 根据指定的父节点路径，重新生成json数据的节点path和id
     *
     * @param jsonData
     * @param parentPath
     * @return
     */
    String rebuildNodePathByParentPath(String jsonData, String parentPath);

    /**
     * 根据指定的父节点路径，重新生成json数据的节点path和id
     *
     * @param jsonData
     * @param parentPath
     * @param currentNodeNum 节点在父节点中的顺序号，从0开始
     * @return
     */
    String rebuildNodePathByParentPath(String jsonData, String parentPath, String currentNodeNum);

}
