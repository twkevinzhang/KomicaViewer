package self.nesl.komicaviewer.request.komica.host;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.parser.komica.KomicaBoardsParser;
import self.nesl.komicaviewer.request.Request;

public class KomicaTop50BoardListRequest extends Request<List<Board>> {
    public KomicaTop50BoardListRequest(String url) {
        super( url);
    }

    public static KomicaTop50BoardListRequest create(Category category, String boardListUrl) {
        return new KomicaTop50BoardListRequest(boardListUrl);
    }

    @Override
    public List<Board> parse(String response) {
        return new KomicaBoardsParser(Jsoup.parse(response), true).parse();
    }
}
