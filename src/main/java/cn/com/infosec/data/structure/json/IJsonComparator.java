/**
 * IJsonCompare
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 17:30
 */

package cn.com.infosec.data.structure.json;

public interface IJsonComparator {
    /**
     * 比较两个json是否一致
     * @param srcJsonData
     * @param destJsonData
     * @param onlyStructure 仅比较结构
     * @return
     */
    boolean compare(String srcJsonData,String destJsonData,boolean onlyStructure);

}
