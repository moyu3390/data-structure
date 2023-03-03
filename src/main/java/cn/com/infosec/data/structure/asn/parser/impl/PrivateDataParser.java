/**
 * PrivateDataParser
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 17:27
 */

package cn.com.infosec.data.structure.asn.parser.impl;

import cn.com.infosec.data.structure.asn.parser.DataType;
import cn.com.infosec.data.structure.asn.parser.IDataTypeParser;

public class PrivateDataParser implements IDataTypeParser {
    @Override
    public DataType parseData(byte[] data) {

        return DataType.PRIVATE_DATA;
    }
}
