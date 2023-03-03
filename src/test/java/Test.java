import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Test
 * <p>
 * 1.0
 * <p>
 * 2022/12/21 11:57
 */

public class Test {
    public static void main(String[] args) {
        System.out.println(Byte.toUnsignedInt((byte) 9));
        System.out.println(9 & 255);

        try {
            FileReader reader = new FileReader("d:\\encoding.txt");
            System.err.println(reader.getEncoding());
            InputStreamReader reader1 = new InputStreamReader(new FileInputStream("d:\\encoding.txt"));
            System.err.println(reader1.getEncoding());


            String str = new String("lasjflasdjfSDLFJASL说多了几分手法十分手动阀".getBytes("utf-8"));
            System.err.println(str);
            byte[] bytes = str.getBytes();
            String s = new String(bytes, "iso-8859-1");

            System.err.println(s);

            String temp  = "lasjflasdjfSDLFJASLè¯´å¤\u009Aäº\u0086å\u0087 å\u0088\u0086æ\u0089\u008Bæ³\u0095å\u008D\u0081å\u0088\u0086æ\u0089\u008Bå\u008A¨é\u0098\u0080";

            byte[] bytes1 = temp.getBytes("iso-8859-1");
            String x = new String(bytes1,"UTF-8");
            System.err.println(x);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
