/*
 * MIT License
 *
 * Copyright (c) 2020 Alen Turkovic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cn.com.infosec.data.structure.asn.model;

import cn.com.infosec.data.structure.asn.annotation.*;
import cn.com.infosec.data.structure.asn.converter.HexStringConverter;
import cn.com.infosec.data.structure.asn.tag.TagType;

import java.util.List;
import java.util.Objects;

@AsnPostProcessMethod("postDecode")
@AsnStructure(@AsnTag(value = 16, type = TagType.PRIVATE))
public class Person {

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
    private List<Person> children;
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


//    @AsnPrimitive
//    private boolean male;
//
//    @AsnPrimitive
//    private Integer age;
//
//    @AsnCollection(structured = false, asnConverter = HexStringConverter.class, type = String.class)
//    private Set<String> phones;
//
//    @AsnCollection(value = @AsnTag(1), elementTag = @AsnTag(2), type = Address.class)
//    private List<Address> addresses;
//
//    @AsnPrimitive(@AsnTag(3))
//    private short shoeSize;
//
//    private boolean adult;

//    public Person(final boolean male, final int age, final Set<String> phones) {
//        this.male = male;
//        this.age = age;
//        this.phones = phones;
//    }

    public Person() {
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

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
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

//    public boolean isMale() {
//        return male;
//    }
//
//    public void setMale(boolean male) {
//        this.male = male;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    public Set<String> getPhones() {
//        return phones;
//    }
//
//    public void setPhones(Set<String> phones) {
//        this.phones = phones;
//    }
//
//    public List<Address> getAddresses() {
//        return addresses;
//    }
//
//    public void setAddresses(List<Address> addresses) {
//        this.addresses = addresses;
//    }
//
//    public short getShoeSize() {
//        return shoeSize;
//    }
//
//    public void setShoeSize(short shoeSize) {
//        this.shoeSize = shoeSize;
//    }
//
//    public boolean isAdult() {
//        return adult;
//    }
//
//    public void setAdult(boolean adult) {
//        this.adult = adult;
//    }
//
//    private void postDecode() {
//        adult = age >= 18;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(pId, person.pId) && Objects.equals(path, person.path) && Objects.equals(tagNum, person.tagNum) && Objects.equals(length, person.length) && Objects.equals(isContainer, person.isContainer) && Objects.equals(hexValue, person.hexValue) && Objects.equals(children, person.children) && Objects.equals(tagName, person.tagName) && Objects.equals(tagClass, person.tagClass) && Objects.equals(visualValue, person.visualValue) && Objects.equals(isTemplate, person.isTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pId, path, tagNum, length, isContainer, hexValue, children, tagName, tagClass, visualValue, isTemplate);
    }


    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", pId='" + pId + '\'' +
                ", path='" + path + '\'' +
                ", tagNum=" + tagNum +
                ", length=" + length +
                ", isContainer=" + isContainer +
                ", hexValue='" + hexValue + '\'' +
                ", children=" + children +
                ", tagName='" + tagName + '\'' +
                ", tagClass='" + tagClass + '\'' +
                ", visualValue='" + visualValue + '\'' +
                ", isTemplate=" + isTemplate +
                '}';
    }
}
