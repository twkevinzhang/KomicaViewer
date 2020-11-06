package self.nesl.komicaviewer.models.komica._2cat;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import self.nesl.komicaviewer.models.Parser;
import self.nesl.komicaviewer.models.komica.sora.SoraBoardParser;
import self.nesl.komicaviewer.util.UrlUtils;

import static self.nesl.komicaviewer.util.Utils.print;

public class _2catBoardParser extends SoraBoardParser {

    public _2catBoardParser(String boardUrl, Element element) {
        super(boardUrl, element);
    }

    @Override
    public Class<? extends Parser> getPostParserClass() {
        return _2catPostParser.class;
    }

    public static String getBoardId(String url){
        String boardCode= new UrlUtils(url).getPath();
        return boardCode.replace("/","");
    }

    @Override
    public Elements getThreads(){
        return post.getOrigin().select("div.threadpost");
    }
}
