/**
 * DataTypeParserExecutor
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 16:59
 */

package cn.com.infosec.data.structure.asn.parser;

import cn.com.infosec.data.structure.asn.parser.impl.P7DataParser;
import cn.com.infosec.data.structure.asn.parser.impl.X509CertificateDataParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DataTypeParserExecutor {

    private static final List<IDataTypeParser> parsers;

    static {
        parsers = init();
    }

    private static List<IDataTypeParser> init() {
        List<IDataTypeParser> parsers1 = new ArrayList<>();
        parsers1.add(new P7DataParser());
        parsers1.add(new X509CertificateDataParser());
//        parsers1.add(new PrivateDataParser());
        return parsers1;
    }

    public static DataType execute(byte[] data) {
        DataType dataType = DataType.UNKNOW_DATA;
        for (IDataTypeParser parser : parsers) {
            dataType = parser.parseData(data);
            if (!dataType.equals(DataType.UNKNOW_DATA)) {
                break;
            }
        }
        return dataType;
    }





}
