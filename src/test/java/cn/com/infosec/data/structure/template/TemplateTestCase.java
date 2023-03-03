/**
 * TemplateTestCase
 * <p>
 * 1.0
 * <p>
 * 2022/12/30 9:25
 */

package cn.com.infosec.data.structure.template;

import cn.com.infosec.data.structure.bean.StructureJsonData;
import cn.com.infosec.data.structure.json.IJsonNodeModifier;
import cn.com.infosec.data.structure.json.IJsonParser;
import cn.com.infosec.data.structure.json.editor.JsonNodeAttributesEditor;
import cn.com.infosec.data.structure.json.editor.JsonNodeModifier;
import cn.com.infosec.data.structure.json.parser.GsonParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateTestCase {
    IJsonNodeModifier jsonNodeModifier = new JsonNodeModifier();
    IJsonParser parser = new GsonParser();
    JsonNodeAttributesEditor jsonNodeAttributesEditor = new JsonNodeAttributesEditor();

    @Test
    public void addAttributes() {
        String templateFilePath = "person.json";
        String path = TemplateTestCase.class.getClassLoader().getResource(templateFilePath).getPath();

        try {
            RandomAccessFile file = new RandomAccessFile(path, "r");
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = file.readLine()) != null) {
                buffer.append(str);
            }
            System.err.println(buffer.toString().trim().replaceAll(" ", ""));
            List<String> attributes = new ArrayList<>();
            attributes.add("nodeIdentity");
            String json = jsonNodeAttributesEditor.addAttributes(buffer.toString(), attributes);

            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String strr = gson.toJson(jsonObject);
            System.err.println(strr);
            writeFile(strr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void copyAttributesTest() {
        String templateFilePath = "out3.json";
        String path = TemplateTestCase.class.getClassLoader().getResource(templateFilePath).getPath();

        try {
            RandomAccessFile file = new RandomAccessFile(path, "r");
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = file.readLine()) != null) {
                buffer.append(str);
            }
            System.err.println(buffer.toString().trim().replaceAll(" ", ""));

            String srcJsonData = buffer.toString();

            String attributeSourceData = srcJsonData;
            Map<String, String> attributesMap = new HashMap<>();
            attributesMap.put("nodeIdentity", "nodeAlias");
            String templateJson = jsonNodeAttributesEditor.copyAttributes(srcJsonData, attributeSourceData, attributesMap, true);


            JsonObject jsonObject = JsonParser.parseString(templateJson).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String strr = gson.toJson(jsonObject);
            System.err.println(strr);
            writeFile(strr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Deprecated
    public void addAttribute() {
        String templateFilePath = "p7signedData.json";
        String path = TemplateTestCase.class.getClassLoader().getResource(templateFilePath).getPath();
        try {
//            BufferedReader reader = new BufferedReader(new FileReader(path));
//            List<String> lines = Files.readAllLines(Paths.get(path));
//            lines.stream().forEach(l->buffer.append(l));
//            System.out.println(buffer.toString());
            RandomAccessFile file = new RandomAccessFile(path, "r");
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = file.readLine()) != null) {
                buffer.append(str);
            }
            System.err.println(buffer.toString().trim().replaceAll(" ", ""));
            StructureJsonData decode = parser.decode(buffer.toString(), true);
            System.out.println(decode);
            String dest = buffer.toString();
            String src = parser.encode(decode);

            List<String> attributes = new ArrayList<>();
            attributes.add("nodeIdentity");
            String newstr = jsonNodeModifier.copyAttributes(dest, src, attributes);
            System.err.println(newstr);
            JsonObject jsonObject = JsonParser.parseString(newstr).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String strr = gson.toJson(jsonObject);
            System.err.println(strr);
            writeFile(strr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeFile(String context) {
        try {
            String path = TemplateTestCase.class.getClassLoader().getResource("").getPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + File.separator + "out33.json"));
            writer.write(context);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
