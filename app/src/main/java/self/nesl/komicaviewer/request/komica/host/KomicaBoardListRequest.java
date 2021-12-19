package self.nesl.komicaviewer.request.komica.host;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.parser.komica.KomicaBoardsParser;
import self.nesl.komicaviewer.request.Request;

public class KomicaBoardListRequest extends Request {
    public KomicaBoardListRequest(String url) {
        super(url);
    }

    public static KomicaBoardListRequest create(Category category, String boardListUrl) {
        return new KomicaBoardListRequest(boardListUrl);
    }
}
