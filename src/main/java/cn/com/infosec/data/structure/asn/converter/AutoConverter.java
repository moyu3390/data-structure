
package cn.com.infosec.data.structure.asn.converter;

/**
 * A no-op converter used as a marker to indicate automatic type resolving for the conversion should be used.
 */
public class AutoConverter implements AsnConverter<Object, Object> {

  @Override
  public Object decode(final Object data) {
    return data;
  }

  @Override
  public Object encode(final Object data) {
    return data;
  }
}
