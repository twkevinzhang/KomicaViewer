package self.nesl.komicaviewer.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

import self.nesl.komicaviewer.gson.KThreadAdapter;
import self.nesl.komicaviewer.dto.KThread;

import static self.nesl.komicaviewer.Const.PARSER_HOST;
import static self.nesl.komicaviewer.util.Utils.print;

public abstract class BaseViewModel extends ViewModel {
    public MutableLiveData<KThread> post=new MutableLiveData<KThread>();
    public MutableLiveData<KThread> getPost() { return post; }
    private String postUrl;

    public void load(int page) {
        String parserHost=PARSER_HOST+"/thread";
        print(this,"AndroidNetworking GET",parserHost,"page",page+"");
        AndroidNetworking.get(parserHost)
                .addHeaders("url",postUrl)
                .addHeaders("page",page+"")
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                post(response);
            }

            @Override
            public void onError(ANError anError) {
                switch (anError.getErrorCode()){
                    case 404:
                        String newPostUrl=anError.getResponse().header("url");
                        print(this,"AndroidNetworking GET",newPostUrl);
                        AndroidNetworking.get(newPostUrl)
                                .build().getAsString(new StringRequestListener(){
                            @Override
                            public void onResponse(String response) {
                                print(this,"AndroidNetworking POST",parserHost);
                                AndroidNetworking.post(parserHost)
                                        .addBodyParameter("url",postUrl)
                                        .addBodyParameter("page",page+"")
                                        .addBodyParameter("element",response)
                                        .build().getAsJSONObject(new JSONObjectRequestListener(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        post(response);
                                    }
                                    @Override
                                    public void onError(ANError anError) { anError.printStackTrace(); }
                                });
                            }
                            @Override
                            public void onError(ANError anError) { anError.printStackTrace(); }
                        });
                        break;
                    default:
                        anError.printStackTrace();
                }
            }
        });
    }

    abstract public KThread beforePost(KThread kThread);

    public void post(JSONObject response){
        Gson gson = new GsonBuilder().registerTypeAdapter(KThread.class, new KThreadAdapter()).create();
        KThread newPost=gson.fromJson(response.toString(),KThread.class);
        newPost= beforePost(newPost);
        KThread old=post.getValue();
        if(old!=null){
            old.addAll(newPost.getReplies(false,1));
            post.setValue(old);
        }else{
            post.setValue(newPost);
        }
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
