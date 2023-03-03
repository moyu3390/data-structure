
package cn.com.infosec.data.structure.asn.encoder;

import cn.com.infosec.data.structure.bean.StructureJsonData;

/**
 * Used for encoding into Asn data.
 *
 * @param <O> type of output
 */
public interface AsnEncoder<O> {

    /**
     * Encodes objects into raw data.
     *
     * @param object object to encode
     * @return encoded data
     */
    O encode(Object object);

    O encode(String jsonString);
//    O encode(String jsonString,String encoding);

    O encode(StructureJsonData structureJsonData);
//    O encode(StructureJsonData structureJsonData, String encoding);
}
