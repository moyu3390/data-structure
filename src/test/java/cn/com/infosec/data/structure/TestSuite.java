/**
 * TestSuite
 * <p>
 * 1.0
 * <p>
 * 2023/1/11 14:47
 */

package cn.com.infosec.data.structure;

import cn.com.infosec.data.structure.asn.*;
import cn.com.infosec.data.structure.provide.ASN1DataGeneratorCommandTest;
import cn.com.infosec.data.structure.provide.ASN1DataParserCommandTest;
import cn.com.infosec.data.structure.provide.DataParseCommandTest;
import cn.com.infosec.data.structure.provide.MapSetterCommandTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ASN1DataGeneratorCommandTest.class,
        ASN1DataParserCommandTest.class,
        DataParseCommandTest.class,
        MapSetterCommandTest.class,
        BerEncoderTest.class,
        DecoderTest.class,
        JsonComparatorTest.class,
        JsonNodeModifierTest.class,
        JsonNodeModifierTest.class,
        JsonParserTest.class,
        JsonTemplateGeneratorTest.class
})
public class TestSuite {

}
