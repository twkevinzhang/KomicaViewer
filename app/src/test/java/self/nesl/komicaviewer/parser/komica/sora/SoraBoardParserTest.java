package self.nesl.komicaviewer.parser.komica.sora;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.jsoup.Jsoup;
import java.util.List;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.util.TestUtils;

public class SoraBoardParserTest extends TestCase {
    protected SoraBoardParser parser;

    protected void setUp(){
        parser = new SoraBoardParser(
                "https://sora.komica.org/00",
                Jsoup.parse(TestUtils.loadFile("./src/test/html/sora/BoardPage.html"))
        );
    }

    public void testParse() {
        List<Post> posts= parser.parse();
        TestUtils.print(new Gson().toJson(posts));
    }
}