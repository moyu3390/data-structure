
package cn.com.infosec.data.structure.asn.tag;

import java.util.Arrays;

public class Tag implements Comparable<Tag> {
    private final int value;
    private final TagType tagType;
    private final boolean constructed;
    private byte[] encoding;


    public Tag(final int value, final TagType tagType, final boolean constructed) {
        this.value = value;
        this.tagType = tagType;
        this.constructed = constructed;
    }

    public Tag(final int value, final TagType tagType, final boolean constructed, byte[] encoding) {
        this.value = value;
        this.tagType = tagType;
        this.constructed = constructed;
        this.encoding = encoding;
    }

    public int getValue() {
        return value;
    }

    public TagType getTagType() {
        return tagType;
    }

    public boolean isConstructed() {
        return constructed;
    }

    public byte[] getEncoding() {
        return encoding;
    }

    public void setEncoding(byte[] encoding) {
        this.encoding = encoding;
    }

    @Override
    // order by type, then by value
    public int compareTo(final Tag tag) {
        final int typeComparison = tagType.compareTo(tag.tagType);

        if (typeComparison != 0) {
            return typeComparison;
        }

        return Integer.compare(value, tag.value);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "value=" + value +
                ", type=" + tagType +
                ", constructed=" + constructed +
                ", encoding=" + Arrays.toString(encoding) +
                '}';
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tag)) {
            return false;
        } else {
            Tag other = (Tag) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getValue() != other.getValue()) {
                return false;
            } else {
                Object this$type = this.getTagType();
                Object other$type = other.getTagType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Tag;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getValue();
        Object $type = this.getTagType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        return result;
    }
}