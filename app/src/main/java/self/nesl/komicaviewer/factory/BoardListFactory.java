package self.nesl.komicaviewer.factory;

import android.os.Bundle;

import org.jsoup.Jsoup;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.category.KomicaTop50Category;
import self.nesl.komicaviewer.parser.Parser;
import self.nesl.komicaviewer.parser.komica.KomicaBoardsParser;
import self.nesl.komicaviewer.request.DefaultRequest;
import self.nesl.komicaviewer.request.Request;

public class BoardListFactory implements Factory<List<Board>> {
    private Category category;

    public BoardListFactory(Category category) {
        this.category=category;
    }

    public Request createRequest(Bundle bundle) {
        Request request = null;
        if (category instanceof KomicaCategory) {
            request = new DefaultRequest("http://www.komica.org/bbsmenu.html");
        }else if(category instanceof KomicaTop50Category){
            request = new DefaultRequest("http://www.komica.org/mainmenu2019.html");
        }
       return request;
    }

    public List<Board> parse(String response) {
        Parser<List<Board>> parser = null;
        if (category instanceof KomicaCategory) {
            parser = new KomicaBoardsParser(Jsoup.parse(response), false);
        }else if(category instanceof KomicaTop50Category){
            parser = new KomicaBoardsParser(Jsoup.parse(response), true);
        }
        return parser.parse();
    }
}
