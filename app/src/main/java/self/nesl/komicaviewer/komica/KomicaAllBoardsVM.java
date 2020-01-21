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
public class KomicaAllBoardsVM extends MyViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();
    private Web web;

    @Override
    public void loadAllBoards(){
        ArrayList<Board> arr=BoardDB.getKomicaBoards(web);
        if(arr!=null && arr.size()>0){
            boardslist.postValue(arr);
        }else{
            scrapyAllBoards();
        }
    }

    @Override
    public void scrapyAllBoards(){
        AndroidNetworking.get(web.getMenuUrl())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                ArrayList<Board> arr=toBoardlist(Jsoup.parse(response),web);
                BoardDB.updateKomicaBoardUrls(arr,web);
                boardslist.postValue(arr);
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("BlVM",web.getMenuUrl());
                Log.e("BlVM",anError.getErrorBody());
                Log.e("BlVM",anError.getErrorBody());
                Log.e("BlVM",anError.getErrorDetail());
                Log.e("BlVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public ArrayList<Board> toBoardlist(Document doc, Web web) {
        ArrayList<Board> boards = new ArrayList<Board>();
        for (Element ul : doc.select("ul")) {
            String ui_title = ul.getElementsByClass("category").text();
            for (Element li : ul.select("li")) {
                String li_title = li.text();
                String li_link = li.select("a").attr("href");
                if (li_link.contains("/index.")) {
                    li_link = li_link.substring(0, li_link.indexOf("/index."));
                }
                try {
                    boards.add(new Board(web,li_title, li_link)
                            .setTitle(li_title)
                            .setCategory(ui_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
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
