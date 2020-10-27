package self.nesl.komicaviewer.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

import self.nesl.komicaviewer.db.BoardPreferences;
import self.nesl.komicaviewer.dto.Host;
import self.nesl.komicaviewer.dto.KThread;
import self.nesl.komicaviewer.gson.KThreadAdapter;
import self.nesl.komicaviewer.ui.BaseViewModel;

import static self.nesl.komicaviewer.util.Utils.print;

public class HomeViewModel extends ViewModel {
    public MutableLiveData<Host> host=new MutableLiveData<Host>();

    public MutableLiveData<Host> getHost() {
        return host;
    }
    public void setHost(Host host){
        this.host.postValue(host);
    }
}