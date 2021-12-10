package self.nesl.komicaviewer.ui.render;

import static self.nesl.komicaviewer.util.Utils.print;

import android.util.Patterns;

import junit.framework.TestCase;

import java.util.regex.Matcher;

public class PostRenderTest extends TestCase {

    public void testAddLinkedText() {
        String text = "szsjvcn https://www.google.com sadbk hgvjh https://www.google2.com";
        Matcher m = Patterns.WEB_URL.matcher(text);
        int index=0;
        while (m.find()){
            String url = m.group();
            String preParagraph = text.substring(index, m.start());

            print(preParagraph, url);

            index = m.end();
        }
        String lastParagraph = text.substring(index);
        print(lastParagraph);
    }
}