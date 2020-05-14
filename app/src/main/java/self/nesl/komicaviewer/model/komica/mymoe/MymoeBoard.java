package self.nesl.komicaviewer.model.komica.mymoe;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Picture;
import self.nesl.komicaviewer.model.Post;
import self.nesl.komicaviewer.model.komica.sora.SoraBoard;
import self.nesl.komicaviewer.model.komica.sora.SoraPost;
import self.nesl.komicaviewer.ui.board.BoardViewModel;
import self.nesl.komicaviewer.util.UrlUtil;

import static self.nesl.komicaviewer.ui.board.BoardViewModel.COLUMN_PAGE;
import static self.nesl.komicaviewer.util.ProjectUtil.installThreadTag;
import static self.nesl.komicaviewer.util.ProjectUtil.parseTime;
import static self.nesl.komicaviewer.util.Util.parseChiToEngWeek;
import static self.nesl.komicaviewer.util.Util.print;

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
