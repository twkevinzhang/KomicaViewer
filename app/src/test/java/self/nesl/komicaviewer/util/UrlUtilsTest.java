package self.nesl.komicaviewer.util;

import org.junit.Test;

import static org.junit.Assert.*;

// JUnit: Java單元測試，不包含Android Context
// is JUnit 4，not default Groovy JUnit
// https://ithelp.ithome.com.tw/articles/10217081
public class UrlUtilsTest {

    @Test
    public void start() {

        String boardUrl="https://2cat.org/~webgame/";
        String host=new UrlUtils(boardUrl).getHost();
        boardUrl=boardUrl.replace(host+"/~",host+"/");

        assertEquals(boardUrl,"https://2cat.org/webgame/");
    }
}