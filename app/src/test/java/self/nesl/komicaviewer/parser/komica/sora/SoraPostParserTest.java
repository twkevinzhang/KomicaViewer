package self.nesl.komicaviewer.parser.komica.sora;

import static self.nesl.komicaviewer.util.TestUtils.print;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.jsoup.Jsoup;
import self.nesl.komicaviewer.models.Post;
import self.nesl.komicaviewer.util.TestUtils;

public class SoraPostParserTest extends TestCase {
    protected SoraPostParser parser;

    protected void setUp(){
        parser = new SoraPostParser(
                "https://sora.komica.org/00/pixmicat.php?res=25208017",
                Jsoup.parse(TestUtils.loadFile("./src/test/html/sora/ReplyPost.html"))
        );
    }

    public void testParse() {
        Post post= parser.parse();
        print(new Gson().toJson(post));
    }

    public void testParseText() {
        print(parser.parseText());
    }
}