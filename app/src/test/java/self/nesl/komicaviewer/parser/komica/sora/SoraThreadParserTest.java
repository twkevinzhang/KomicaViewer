package self.nesl.komicaviewer.parser.komica.sora;

import com.google.gson.Gson;

import junit.framework.TestCase;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.request.KThread;
import self.nesl.komicaviewer.util.TestUtils;

public class SoraThreadParserTest extends TestCase {
    protected SoraThreadParser parser;

    protected void setUp(){
        parser = new SoraThreadParser(
                "https://sora.komica.org/00/pixmicat.php?res=25214959",
                Jsoup.parse(TestUtils.loadFile("./src/test/html/sora/ThreadPage.html"))
        );
    }

    public void testParse() {
        KThread posts= parser.parse();
        TestUtils.print(new Gson().toJson(posts));
    }
}