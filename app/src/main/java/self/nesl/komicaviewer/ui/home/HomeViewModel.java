package self.nesl.komicaviewer.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.model.Host;
import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Util.print;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Post>> list= new MutableLiveData<ArrayList<Post>>();

    public void update(Host host) {
        ArrayList<Post> arrayList= BoardPreferences.getBoards(host);
        if(arrayList==null || arrayList.size()==0){
            BoardPreferences.update(host, new Host.OnResponse() {
                @Override
                public void onResponse(ArrayList<Post> arrayList1) {
                    list.setValue(arrayList1);
                }
            });
        }else{
            list.setValue(arrayList);
        }
    }

    public LiveData<ArrayList<Post>> getList() {
        return list;
    }
}