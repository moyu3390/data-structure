
package cn.com.infosec.data.structure.asn.tlv;

import java.io.InputStream;

/**
 * Reads BER TLV structures.
 */
public interface TlvDataReader {
  /**
   * Read the next TLV.
   *
   * @param inputStream input stream
   * @return next TLV structure
   */
  BerData readNext(InputStream inputStream);
}
