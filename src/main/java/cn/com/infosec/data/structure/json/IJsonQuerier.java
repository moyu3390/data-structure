/**
 * IJsonQuerier
 * <p>
 * 1.0
 * <p>
 * 2022/12/26 11:32
 */

package cn.com.infosec.data.structure.json;

import cn.com.infosec.data.structure.bean.StructureJsonData;

public interface IJsonQuerier {

    StructureJsonData query(String jsonData, String path);

    StructureJsonData query(StructureJsonData structureJsonData, String path);

    public String queryNode(String data, String path);

    public String queryNodeByIdentity(String data, String identity);
}
