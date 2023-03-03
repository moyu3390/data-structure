/**
 * SelectNode
 * <p>
 * 1.0
 * <p>
 * 2022/12/15 10:34
 */

package cn.com.infosec.data.structure.bean;

public class SelectNode extends BaseNodeData {
    /**
     * 数据结构标识
     */
    private String SJDID;
    /**
     * 节点标识
     */
    private String nodeId;
    /**
     * 父节点标识
     */
    private String pId;

    public String getSJDID() {
        return SJDID;
    }

    public void setSJDID(String SJDID) {
        this.SJDID = SJDID;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
