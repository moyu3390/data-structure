/**
 * IDataTypeParser
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 17:25
 */

package cn.com.infosec.data.structure.asn.parser;

public interface IDataTypeParser {

    DataType parseData(final byte[] data);
}
