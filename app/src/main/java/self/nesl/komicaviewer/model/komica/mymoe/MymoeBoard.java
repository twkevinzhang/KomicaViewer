package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.ui.board.BoardViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class MymoeBoard extends SoraBoard {

    public MymoeBoard() {}

    public MymoeBoard(Document doc,String boardUrl){
       super(doc,boardUrl,new MymoePost());
    }

    @Override
    public void download(Bundle bundle, OnResponse onResponse) {
        String pageUrl= getBoardUrl();
        int page=0;
        if(bundle!=null){
            page=bundle.getInt(BoardViewModel.COLUMN_PAGE,0);
        }

        if (page != 0) {
            pageUrl += "/pixmicat.php?page_num="+ page;
        }
        print(new Object(){}.getClass(),"AndroidNetworking",pageUrl);
        AndroidNetworking.get(pageUrl).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                onResponse.onResponse(new MymoeBoard(Jsoup.parse(response), getBoardUrl()));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
            }
        });
    }
}
