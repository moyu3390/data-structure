package cn.com.infosec.data.structure.asn.decoder;

import cn.com.infosec.data.structure.bean.StructureJsonData;

/**
 * Used for decoding Asn data.
 *
 * @param <I> type of input
 */
public interface AsnDecoder<I> {
    /**
     * Decodes input raw data into specific classes.
     *
     * @param clazz type of output
     * @param input raw Asn data
     * @param <T>   output class
     * @return decoded data
     */
    <T> T decode(Class<T> clazz, I input);


    StructureJsonData decode(I input, boolean onlyStructure);


    StructureJsonData decode(I input, String currentNodePath, boolean onlyStructure);


    StructureJsonData decode(I input, String currentNodeParentPath, String currentNodeNum, boolean onlyStructure);


    String decodeToJson(byte[] input, boolean onlyStructure);


    String decodeToJson(byte[] input, String currentNodePath, boolean onlyStructure);


    String decodeToJson(byte[] input, String currentNodeParentPath, String currentNodeNum, boolean onlyStructure);
}
