package self.nesl.komicaviewer.history;

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
public class HistoryVM extends ViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();

    public void loadAllBoards(){

    }

    public LiveData<ArrayList<Board>> getBoards() {
        return boardslist;
    }
}
