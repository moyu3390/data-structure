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

import cn.com.infosec.data.structure.asn.annotation.AsnPrimitive;
import cn.com.infosec.data.structure.asn.annotation.AsnTag;

public class Address {

    @AsnPrimitive
    private String street;

    @AsnPrimitive
    private int number;

    @AsnPrimitive(@AsnTag(2))
    private boolean enabled;

    public Address() {
    }

    public Address(String street, int number, boolean enabled) {
        this.street = street;
        this.number = number;
        this.enabled = enabled;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Address)) {
            return false;
        } else {
            Address other = (Address) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label31:
                {
                    Object this$street = this.getStreet();
                    Object other$street = other.getStreet();
                    if (this$street == null) {
                        if (other$street == null) {
                            break label31;
                        }
                    } else if (this$street.equals(other$street)) {
                        break label31;
                    }

                    return false;
                }

                if (this.getNumber() != other.getNumber()) {
                    return false;
                } else {
                    return this.isEnabled() == other.isEnabled();
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof Address;
    }

    public int hashCode() {
        int result = 1;
        Object $street = this.getStreet();
        result = result * 59 + ($street == null ? 43 : $street.hashCode());
        result = result * 59 + this.getNumber();
        result = result * 59 + (this.isEnabled() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "Address(street=" + this.getStreet() + ", number=" + this.getNumber() + ", enabled=" + this.isEnabled() + ")";
    }
}
