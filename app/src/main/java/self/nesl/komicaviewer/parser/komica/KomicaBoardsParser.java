package self.nesl.komicaviewer.parser.komica;

import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.parser.Parser;

public class KomicaBoardsParser implements Parser<List<Board>> {
    private Element doc;
    private boolean top50;

    public KomicaBoardsParser(Element source, boolean top50) {
        this.doc = source;
        this.top50 = top50;
    }

    @Override
    public List<Board> parse() {
        return top50 ? parseTop50() : parseAll();
    }

    private List<Board> parseAll() {
        return doc.select("div#list ul").stream().flatMap(ul -> {
            String ui_title = ul.selectFirst(".category").text();
            return ul.select("li").stream().map(li -> {
                String li_title = li.text();
                String li_link = li.select("a").attr("href");
                if (li_link.contains("/index.")) {
                    li_link = li_link.substring(0, li_link.indexOf("/index."));
                }
                Board board = new Board(li_link);
                board.setTitle(li_title);
                board.setUrl(li_link);
                board.addTag(ui_title);
                return board;
            });
        }).collect(Collectors.toList());
    }

    private List<Board> parseTop50() {
        return doc.selectFirst(".divTableRow").select("a").stream().map(e -> {
            String url = e.attr("href");
            if (url.contains("/index.")) {
                url = url.substring(0, url.indexOf("/index."));
            }
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String title = e.text();
            Board b = new Board(url);
            b.setTitle(title);
            b.setUrl(url);
            return b;
        }).collect(Collectors.toList());
    }
}
