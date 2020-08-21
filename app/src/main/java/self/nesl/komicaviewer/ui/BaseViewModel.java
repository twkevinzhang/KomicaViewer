package self.nesl.komicaviewer.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import self.nesl.komicaviewer.model.Post;

import static self.nesl.komicaviewer.util.Utils.print;

public abstract class BaseViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Post>> arr=new MutableLiveData<ArrayList<Post>>();
    abstract public void load(int page);
    public MutableLiveData<ArrayList<Post>> getList() { return arr; }

    public void insertPostlist(ArrayList<Post> newArr){
        ArrayList<Post> oldPost=arr.getValue();
        if(oldPost!=null){
            LinkedHashSet set=  new LinkedHashSet<>();
            set.addAll(arr.getValue());
            set.addAll(newArr);
            oldPost.clear();
            oldPost.addAll(set);
        }else{
            oldPost=newArr;
        }
        arr.postValue(oldPost);
    }
}
