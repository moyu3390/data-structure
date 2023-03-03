/**
 * ASN1GeneratorCommandtEST
 * <p>
 * 1.0
 * <p>
 * 2023/1/3 18:18
 */

package cn.com.infosec.data.structure.provide;

import cn.com.infosec.data.structure.asn.util.HexUtils;
import cn.com.infosec.data.structure.bean.SelectNode;
import cn.com.infosec.data.structure.provide.generator.ASN1DataGenerator;
import cn.com.infosec.data.structure.utils.FileUtil;
import cn.com.infosec.v160.util.encoders.Base64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASN1DataGeneratorCommandTest {


    @Test
    public void asn1GeneratorCommandTest() {
        SelectNode sn1 = new SelectNode();
        sn1.setNodePath("/1/0/1/0/1/0/1/0/1");
        SelectNode sn2 = new SelectNode();
        sn2.setNodePath("/1/0/1/0/1/1");
        SelectNode sn3 = new SelectNode();
        sn3.setNodePath("/1/0/2/2");
        List<SelectNode> selectNodes = new ArrayList<>();
        selectNodes.add(sn1);
        selectNodes.add(sn2);
        selectNodes.add(sn3);

        // todo 从文件读取模板数据
        String fileContext = FileUtil.readInternalFileContext2String("P7EnvelopedData.json");
        Map<String, byte[]> nodeMap = new HashMap<>();
        final byte[] encodedA = HexUtils.decode("300D810161A208A106810101820102");
        nodeMap.put("/1/0/1/0/1/0/1/0/1", encodedA);
        final byte[] encodedB = HexUtils.decode("301D810101A218A106810101820102A106810103820104A2068101FF820102");
        nodeMap.put("/1/0/1/0/1/1", encodedB);


        byte[] bytes = ASN1DataGenerator.generatorDerData(fileContext, selectNodes, nodeMap);

        // todo 从der数据中获取节点数据后，验证每一个节点的数据是否是tlv格式
        System.err.println(new String(Base64.encode(bytes)));

    }


}
