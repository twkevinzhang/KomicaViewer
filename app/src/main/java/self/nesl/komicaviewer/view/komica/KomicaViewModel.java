package self.nesl.komicaviewer.view.komica;

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
import self.nesl.komicaviewer.parser.KomicaDocParser;


// 取得資料 的 模組
public class KomicaViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();
    private Web web;

    public void loadAllBoards() {
        ArrayList<Board> arr = BoardDB.getKomicaBoards(web);
        if (arr != null && arr.size() > 0) {
            boardslist.postValue(arr);
        } else {
            scrapyAllBoards();
        }
    }

    public void scrapyAllBoards() {
        AndroidNetworking.get(web.getMenuUrl())
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                ArrayList<Board> arr = new KomicaDocParser(web).toBoardlist(Jsoup.parse(response));
                BoardDB.updateKomicaBoardUrls(arr, web);
                boardslist.postValue(arr);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("BlVM", web.getMenuUrl());
                Log.e("BlVM", anError.getErrorBody());
                Log.e("BlVM", anError.getErrorBody());
                Log.e("BlVM", anError.getErrorDetail());
                Log.e("BlVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public void loadTop50Boards(){
        ArrayList<Board> arr=BoardDB.getKomicaTop50Boards(web);
        if(arr!=null && arr.size()>0){
            boardslist.postValue(arr);
        }else{
            scrapyTop50Boards();
        }
    }

    public void scrapyTop50Boards(){
        AndroidNetworking.get(web.getTop50BoardUrl())
                .build().getAsString(new StringRequestListener() {

            public void onResponse(String response) {
                ArrayList<Board> arr=new KomicaDocParser(web).toTop50Boardlist(Jsoup.parse(response));
                BoardDB.updateKomicaTop50BoardUrls(arr,web);
                boardslist.postValue(arr);
            }

            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("BlVM",web.getTop50BoardUrl());
                Log.e("BlVM",anError.getErrorBody());
                Log.e("BlVM",anError.getErrorDetail());
                Log.e("BlVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public LiveData<ArrayList<Board>> getBoards() {
        return boardslist;
    }

    public void setWeb(Web web) {
        this.web = web;
    }
}
