/**
 * StructureJsonData
 * <p>
 * 1.0
 * <p>
 * 2022/12/15 9:57
 */

package cn.com.infosec.data.structure.bean;

import cn.com.infosec.data.structure.asn.annotation.*;
import cn.com.infosec.data.structure.asn.converter.HexStringConverter;
import cn.com.infosec.data.structure.asn.tag.Tag;
import cn.com.infosec.data.structure.asn.tag.TagType;

import java.util.List;
import java.util.Objects;

@AsnPostProcessMethod("postDecode")
@AsnStructure(@AsnTag(value = 16, type = TagType.PRIVATE))
public class StructureJsonData {

    /**
     * 节点唯一标识
     */
    private String id;
    /**
     * 父节点唯一标识，根节点的pId=0
     */
    private String pId;
    /**
     * 节点路径
     */
    private String path;
    /**
     * 数据标记对象（用于反序列化）
     */
    private Tag tag;
    /**
     * 数据类型标识（asn1数据类型）
     */
    private Integer tagNum;
    /**
     * 负载长度
     */
    private Integer length;
    /**
     * 是否是结构体
     */
    private Boolean isContainer;
    /**
     * 负载内容：
     * 叶子节点必须包含此属性，非叶子节点不包含。与children互斥
     */
    @AsnPrimitive
    private String hexValue;
    /**
     * 子节点：
     * 与hexValue互斥，即若有子节点，不显示hexValue
     */
    @AsnCollection(structured = false, asnConverter = HexStringConverter.class, type = String.class)
    private List<StructureJsonData> children;
    /**
     * 数据类型的文本显示
     */
    private String tagName;
    /**
     * Tag类型标识，即通用、私有、上下文、专用
     */
    private String tagClass;
    /**
     * hexValue内容的文本化显示:
     * 如hexValue=2a864886f70d010702， visualValue=signedData(1.2.840.113549.1.7.2)
     */
    private String visualValue;
    /**
     * 是否是模板节点，仅模板文件有效
     */
    private Boolean isTemplate;

    /**
     * 节点标识，仅叶子节点有,在模板中标识
     */
    private String nodeIdentity = "";
    /**
     * 画布中显示的数据类型(Java中的数据类型，暂时固定字节数组)
     */
    private String dataType = "byte[]";

    /**
     * 画布中显示的节点名称（生成逻辑见设计文档）
     */
    private String nodeAlias = "";
    /**
     * 该节点是否需要校验，在开启校验时起作用。模板中的属性
     */
    private boolean isValidator;

    public StructureJsonData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTagNum() {
        return tagNum;
    }

    public void setTagNum(Integer tagNum) {
        this.tagNum = tagNum;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getContainer() {
        return isContainer;
    }

    public void setContainer(Boolean container) {
        isContainer = container;
    }

    public String getHexValue() {
        return hexValue;
    }

    public void setHexValue(String hexValue) {
        this.hexValue = hexValue;
    }

    public List<StructureJsonData> getChildren() {
        return children;
    }

    public void setChildren(List<StructureJsonData> children) {
        this.children = children;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagClass() {
        return tagClass;
    }

    public void setTagClass(String tagClass) {
        this.tagClass = tagClass;
    }

    public String getVisualValue() {
        return visualValue;
    }

    public void setVisualValue(String visualValue) {
        this.visualValue = visualValue;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getNodeAlias() {
        return nodeAlias;
    }

    public void setNodeAlias(String nodeAlias) {
        this.nodeAlias = nodeAlias;
    }

    public boolean isValidator() {
        return isValidator;
    }

    public void setValidator(boolean validator) {
        isValidator = validator;
    }

    public String getNodeIdentity() {
        return nodeIdentity;
    }

    public void setNodeIdentity(String nodeIdentity) {
        this.nodeIdentity = nodeIdentity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructureJsonData that = (StructureJsonData) o;
        return Objects.equals(id, that.id) && Objects.equals(pId, that.pId) && Objects.equals(path, that.path) && Objects.equals(tag, that.tag) && Objects.equals(tagNum, that.tagNum) && Objects.equals(length, that.length) && Objects.equals(isContainer, that.isContainer) && Objects.equals(hexValue, that.hexValue) && Objects.equals(children, that.children) && Objects.equals(tagName, that.tagName) && Objects.equals(tagClass, that.tagClass) && Objects.equals(visualValue, that.visualValue) && Objects.equals(isTemplate, that.isTemplate) && Objects.equals(dataType, that.dataType) && Objects.equals(nodeAlias, that.nodeAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pId, path, tag, tagNum, length, isContainer, hexValue, children, tagName, tagClass, visualValue, isTemplate, dataType, nodeAlias);
    }

    @Override
    public String toString() {
        return "StructureJsonData{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", path='" + path + '\'' +
                ", tag=" + tag +
                ", tagNum=" + tagNum +
                ", length=" + length +
                ", isContainer=" + isContainer +
                ", hexValue='" + hexValue + '\'' +
                ", children=" + children +
                ", tagName='" + tagName + '\'' +
                ", tagClass='" + tagClass + '\'' +
                ", visualValue='" + visualValue + '\'' +
                ", isTemplate=" + isTemplate +
                ", nodeIdentity='" + nodeIdentity + '\'' +
                ", dataType='" + dataType + '\'' +
                ", nodeAlias='" + nodeAlias + '\'' +
                ", isValidator=" + isValidator +
                '}';
    }
}
