package self.nesl.komicaviewer.parser.komica._2cat;

import static self.nesl.komicaviewer.util.TestUtils.print;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.jsoup.Jsoup;

import self.nesl.komicaviewer.models.KThread;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.util.TestUtils;

public class _2catThreadParserTest extends TestCase {
    protected Parser<KThread> parser;

    protected void setUp(){
        parser = new _2catThreadParser(
                "https://2cat.org/granblue/?res=963",
                Jsoup.parse(TestUtils.loadFile("./src/test/html/_2cat/ThreadPage.html"))
        );
    }

    public void testParse() {
        KThread post= parser.parse();
        print(new Gson().toJson(post));
    }
}