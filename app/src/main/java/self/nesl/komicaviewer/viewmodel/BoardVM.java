package self.nesl.komicaviewer.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardDB;
import self.nesl.komicaviewer.model.Board;
import self.nesl.komicaviewer.model.Web;


// 取得資料 的 模組
public class BoardVM extends ViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();
    private Web web;

    public void loadAllBoards(){
        AndroidNetworking.get(web.getMenuUrl())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                ArrayList<Board> arr=new DocParser().toBoardlist(Jsoup.parse(response),web);
                BoardDB.updateKomicaBoardUrls(arr,web);
                boardslist.postValue(arr);
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
                Log.e("BlVM",anError.getErrorBody());
                Log.e("BlVM",anError.getErrorDetail());
                Log.e("BlVM", String.valueOf(anError.getErrorCode()));
            }
        });
    }

    public void loadTop50Boards(){
        AndroidNetworking.get(web.getTop50BoardUrl())
                .build().getAsString(new StringRequestListener() {

            @Override
            public void onResponse(String response) {
                boardslist.postValue(new DocParser().toTop50Boardlist(Jsoup.parse(response),web));
            }

            @Override
            public void onError(ANError anError) {
                anError.printStackTrace();
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
        this.web=web;
    }
}
