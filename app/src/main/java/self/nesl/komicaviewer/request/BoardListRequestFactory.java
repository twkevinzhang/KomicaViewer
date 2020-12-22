package self.nesl.komicaviewer.request;

import android.os.Bundle;

import java.util.List;

import self.nesl.komicaviewer.models.Board;
import self.nesl.komicaviewer.models.category.Category;
import self.nesl.komicaviewer.models.category.KomicaCategory;
import self.nesl.komicaviewer.models.category.KomicaTop50Category;
import self.nesl.komicaviewer.request.komica.host.KomicaBoardListRequest;
import self.nesl.komicaviewer.request.komica.host.KomicaTop50BoardListRequest;

public class BoardListRequestFactory {
    private Category category;

    public BoardListRequestFactory(Category category) {
        this.category=category;
    }

    public Request<List<Board>> createRequest(Bundle bundle) {
        Request<List<Board>> request = null;
        if (category instanceof KomicaCategory) {
            request = KomicaBoardListRequest.create(category, "http://www.komica.org/bbsmenu.html");
        }else if(category instanceof KomicaTop50Category){
            request = KomicaTop50BoardListRequest.create(category, "http://www.komica.org/mainmenu2019.html");
        }
        return request;
    }
}
