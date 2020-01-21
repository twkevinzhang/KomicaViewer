package self.nesl.komicaviewer.komica;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardDB;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;


// 取得資料 的 模組
public class KomicaTop50BoardsVM extends MyViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();
    private Web web;

    @Override
    public void loadTop50Boards(){
        ArrayList<Board> arr=BoardDB.getKomicaTop50Boards(web);
        if(arr!=null && arr.size()>0){
            boardslist.postValue(arr);
        }else{
            scrapyTop50Boards();
        }
    }

    @Override
    public void scrapyTop50Boards(){
        AndroidNetworking.get(web.getTop50BoardUrl())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                ArrayList<Board> arr=toTop50Boardlist(Jsoup.parse(response),web);
                BoardDB.updateKomicaTop50BoardUrls(arr,web);
                boardslist.postValue(arr);
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("BlVM",web.getTop50BoardUrl());
                Log.e("BlVM",anError.getErrorBody());
                Log.e("BlVM",anError.getErrorDetail());
                Log.e("BlVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public ArrayList<Board> toTop50Boardlist(Document doc,Web web) {
        ArrayList<Board> boards = new ArrayList<Board>();
        for (Element e : doc.getElementsByClass("divTableRow").select("a")) {
            String url = e.attr("href");
            if (url.contains("/index.")) {
                url = url.substring(0, url.indexOf("/index."));
            }
            if(url.substring(url.length()-1).equals("/")){
                url=url.substring(0,url.length()-1);
            }
            String title = e.text();
            boards.add(new Board(web,title, url).setTitle(title));
        }
        return boards;
    }

    @Override
    public LiveData<ArrayList<Board>> getBoards() {
        return boardslist;
    }
    @Override
    public void setWeb(Web web) {
        this.web=web;
    }
}
