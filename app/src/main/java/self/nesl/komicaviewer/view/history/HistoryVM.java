package self.nesl.komicaviewer.view.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.model.Board;


// 取得資料 的 模組
public class HistoryVM extends ViewModel {
    private MutableLiveData<ArrayList<Board>> boardslist = new MutableLiveData<>();

    public void loadAllBoards(){

    }

    public LiveData<ArrayList<Board>> getBoards() {
        return boardslist;
    }
}
