package self.nesl.komicaviewer.util;

import org.junit.Test;

import static org.junit.Assert.*;

// JUnit: Java單元測試，不包含Android Context
// is JUnit 4，not default Groovy JUnit
// https://ithelp.ithome.com.tw/articles/10217081
public class UtilTest {

    @Test
    public void parseChiToEngWeek() {
        String input = "一";
        String expect = "Mon";
        String out = Util.parseChiToEngWeek(input);
        assertEquals(expect, out);
    }
}