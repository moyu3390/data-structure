/**
 * BaseNodeData
 * <p>
 * 1.0
 * <p>
 * 2022/12/15 10:23
 */

package cn.com.infosec.data.structure.bean;

import java.io.Serializable;

public class BaseNodeData implements Serializable {
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 节点别名，在画布方法中展示的名称
     */
    private String nodeAlias;
    /**
     * 数据类型（Java中）
     */
    private String dataType;

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodeAlias() {
        return nodeAlias;
    }

    public void setNodeAlias(String nodeAlias) {
        this.nodeAlias = nodeAlias;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
