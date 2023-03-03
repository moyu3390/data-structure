/**
 * IJsonNodeMover
 * <p>
 * 1.0
 * <p>
 * 2022/12/27 13:45
 */

package cn.com.infosec.data.structure.json;

public interface IJsonNodeMover {
    /**
     * 移动节点：目标节点不能是叶子节点和非容器类型的节点
     * @param jsonData json数据
     * @param movePath 待移动的节点
     * @param targetPath 目标节点
     * @param order 待移动节点在目标节点中的开始顺序，从0开始；负数-代表不指定，默认放置最后
     * @return
     */
    public String moveNode(String jsonData, String movePath, String targetPath, int order);
}
