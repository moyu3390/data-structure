
package cn.com.infosec.data.structure.asn.converter;

/**
 * A converter used to decode/encode Asn data.
 *
 * @param <E> encoding type
 * @param <D> decoding type
 */
public interface AsnConverter<E, D> {
  /**
   * Decode raw Asn data.
   *
   * @param data data to decode
   * @return decoded data
   */
  D decode(E data);

  /**
   * Encode to raw Asn data.
   *
   * @param data data to encode
   * @return encoded data
   */
  E encode(D data);
}
