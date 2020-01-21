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
public class MyViewModel extends ViewModel {
    public void loadAllBoards(){}
    public void scrapyAllBoards(){}
    public void loadTop50Boards(){}
    public void scrapyTop50Boards(){}


    public LiveData<ArrayList<Board>> getBoards() {
       return null;
    }
    public void setWeb(Web web){
    }
}
