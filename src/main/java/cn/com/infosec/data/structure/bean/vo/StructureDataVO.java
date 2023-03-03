/**
 * TemplateDataStructure
 * <p> 模板数据结构
 * 1.0
 * <p>
 * 2022/12/29 14:18
 */

package cn.com.infosec.data.structure.bean.vo;

import cn.com.infosec.data.structure.bean.StructureJsonData;

import java.io.Serializable;

public class StructureDataVO<T> implements Serializable {
    /**
     * 要保存的数据名称后半部分
     */
    private String strucutreName = "";

    private T data;

    public String getStrucutreName() {
        return strucutreName;
    }

    public void setStrucutreName(String strucutreName) {
        this.strucutreName = strucutreName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
